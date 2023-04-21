// Copyright 2006 Konrad Twardowski
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//   http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package org.makagiga.feeds;

import static org.makagiga.commons.UI._;

// TODO: 2.0: implement missing spec. of Atom 1.0 and RSS 2.0

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.UnmarshallerHandler;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import org.makagiga.commons.FS;
import org.makagiga.commons.MArrayList;
import org.makagiga.commons.MLogger;
import org.makagiga.commons.Net;
import org.makagiga.commons.TK;
import org.makagiga.commons.cache.FileCache;
import org.makagiga.commons.html.HTMLAutodiscovery;
import org.makagiga.commons.html.HTMLEntities;
import org.makagiga.commons.xml.XMLHelper;
import org.makagiga.feeds.atom0x.Atom0xChannel;
import org.makagiga.feeds.atom10.Atom10Channel;
import org.makagiga.feeds.rdf.RDFChannel;
import org.makagiga.feeds.rss.RSSChannel;

public class Feed {
	
	// private

	private Info info;
	private InputStream input;
	private static final MLogger log = MLogger.get("feeds");
	private String contentType;
	private URL source;
	private URLConnection connection;
	
	// public
	
	public Feed() { }
	
	public synchronized void cancelDownload() {
		FS.close(input);
		if (connection instanceof HttpURLConnection) {
			HttpURLConnection.class.cast(connection).disconnect();
			connection = null;
		}
	}

	public AbstractChannel<? extends AbstractItem> download(final String url) throws FeedException, FeedListException {
		return download(url, null);
	}

	/**
	 * @since 2.0
	 */
	public synchronized AbstractChannel<? extends AbstractItem> download(
		final String url,
		final Class<AbstractChannel<? extends AbstractItem>> defaultHandler
	) throws FeedException, FeedListException {
		boolean allOK = false;
		boolean followTheAmp = false;
		boolean invalidXMLCharacter = false;
		boolean malformedByteSequence = false;
		boolean unknownEntity = false;
		info = new Info();
		while (true) {
			FS.BufferedFileInput fileInput = null; // cache input
			URL cacheURL = null;
			try {
				if (Net.isLocalFile(url)) {
					source = new URL(url);
					info.data = new File(source.toURI());
				}
				else {
					cacheURL = downloadToFile(url);
				}
				getInfo();

				// set feed type
				Class<? extends AbstractChannel<?>> handler = defaultHandler;
				if (handler == null)
					handler = info.handler;

				if (handler == null)
					handler = getHandlerByContentType();

				log.debugFormat("Content Type: \"%s\", Type: \"%s\"", contentType, handler);

				if (handler == null)
					throw new FeedException(_("Unknown feed format \"{0}\"", url));

				Unmarshaller unmarshaller = XMLHelper.createUnmarshaller(handler);
				unmarshaller.setEventHandler(new ValidationEventHandler() {
					public boolean handleEvent(final ValidationEvent e) {
						if (e.getSeverity() == ValidationEvent.FATAL_ERROR)
							log.errorFormat("%s [%s]", e.getMessage(), source);

						return true;
					}
				} );

				SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
				saxParserFactory.setNamespaceAware(true);

				XMLReader xmlReader = saxParserFactory.newSAXParser().getXMLReader();
				UnmarshallerHandler unmarshallerHandler = unmarshaller.getUnmarshallerHandler();
				xmlReader.setContentHandler(unmarshallerHandler);
				xmlReader.setEntityResolver(new EntityResolver() {
					public InputSource resolveEntity(final String publicId, final String systemId) {
						return new InputSource(new StringReader(""));
					}
				} );

				AbstractChannel<? extends AbstractItem> channel;
				if (info.data instanceof File) {
					fileInput = new FS.BufferedFileInput((File)info.data);
					doParse(xmlReader, fileInput, invalidXMLCharacter, malformedByteSequence, unknownEntity, followTheAmp);
					channel = (AbstractChannel<? extends AbstractItem>)unmarshallerHandler.getResult();
				}
				else {
					InputStream in = info.getInputStream();
					doParse(xmlReader, in, invalidXMLCharacter, malformedByteSequence, unknownEntity, followTheAmp);
					channel = (AbstractChannel<? extends AbstractItem>)unmarshallerHandler.getResult();

					// save feed in a cache file
					if (info.cache != null) {
						FS.BufferedFileOutput output = null;
						try {
							in.reset();
							output = new FS.BufferedFileOutput(info.cache);
							FS.copyStream(in, output);
						}
						finally {
							FS.close(output);
						}
					}
				}

				channel.finish();
				allOK = true;
				
				return channel;
			}
			catch (UnknownHostException exception) {
				MLogger.exception(exception);

				throw new FeedException(_("Feed address not found: \"{0}\"", url), exception);
			}
			catch (FeedException exception) {
				throw exception; // re-throw
			}
			catch (FeedListException exception) {
				throw exception; // re-throw
			}
			catch (Exception exception) {
				log.infoFormat("Use this link to validate RSS feed: http://www.feedvalidator.org/check.cgi?url=%s", url);
				
				if (!malformedByteSequence && exception.getClass().getSimpleName().equals("MalformedByteSequenceException")) {
					malformedByteSequence = true;
					log.warningFormat("Malformed byte sequence: %s", url);

					continue; // while
				}
				else if (exception instanceof SAXParseException) {
					String m = exception.getMessage();
					if (!followTheAmp && m.contains("The entity name must immediately follow the '&' in the entity reference")) {
						followTheAmp = true;
						log.warningFormat("Invalid entity: %s", url);

						continue; // while
					}
					else if (!invalidXMLCharacter && (m != null) && m.contains("An invalid XML character")) {
						invalidXMLCharacter = true;
						log.warningFormat("Invalid XML character: %s", url);
						
						continue; // while
					}
					else if (!unknownEntity && (m != null) && m.contains("The entity") && m.contains("was referenced, but not declared")) {
						unknownEntity = true;
						log.warningFormat("Unknown entity: %s", url);

						continue; // while
					}
				}
			
				MLogger.exception(exception);

				throw new FeedException(_("Could not download feed \"{0}\"", url), exception);
			}
			finally {
				info.handler = null;
				info.cache = null;
				// info.links; - in use
				info.data = null; // free memory
				FS.close(fileInput);

				// remove broken cache entry
				if (!allOK && (cacheURL != null))
					FileCache.getInstance().getGroup("feed").remove(cacheURL);
			}
		}
	}
	
	public synchronized List<HTMLAutodiscovery.Link> getLinks() { return info.links; }
	
	public synchronized URL getSource() { return source; }
	
	// private
	
	private void doParse(
		final XMLReader reader,
		final InputStream input,
		final boolean invalidXMLCharacter,
		final boolean malformedByteSequence,
		final boolean unknownEntity,
		final boolean followTheAmp
	) throws Exception {
		if (invalidXMLCharacter || malformedByteSequence || unknownEntity || followTheAmp) {
			StringBuilder buf = FS.readLines(input, info.getEncoding(), true);

			// fix "Foo & Bar"
			if (followTheAmp) {
				TK.fastReplace(buf, " & ", " &amp; ");
			}

			// filter invalid characters
			if (invalidXMLCharacter) {
				TK.fastReplace(buf, "\u0000\u001b", "?");
				TK.fastReplace(buf, "\u0010", "?");
			}

			// fix some common XML entity errors
			if (unknownEntity) {
				for (Map.Entry<String, String> entity : HTMLEntities.getMap().entrySet())
					TK.fastReplace(buf, "&" + entity.getKey() + ";", entity.getValue());
			}
			reader.parse(new InputSource(new StringReader(buf.toString())));
		}
		else {
			reader.parse(new InputSource(input));
		}
	}

	private URL downloadToFile(final String url) throws IOException {
		URL connectionURL = null;
		contentType = null;
		try {
			String unescapedURL = (url == null) ? "" : url.replace("&amp;", "&");

			if (unescapedURL.startsWith("feed:")) {
				if (unescapedURL.startsWith("feed://"))
					// feed://example.com/rss -> http://example.com/rss
					unescapedURL = unescapedURL.replaceFirst("feed", "http");
				else
					// feed:http://example.com/rss -> http://example.com/rss
					unescapedURL = unescapedURL.substring("feed:".length());
			}
			// example.com/rss -> http://example.com/rss
			if (!unescapedURL.startsWith("http://") && !unescapedURL.startsWith("https://"))
				unescapedURL = "http://" + unescapedURL;

			source = new URL(unescapedURL);
			connection = source.openConnection();
			Net.setupConnection(connection, Net.SETUP_COMPRESS);
			
			// HACK: workaround for crappy services
			String host = source.getHost();
			if (
				"digg.com".equals(host) ||
				host.endsWith(".livejournal.com")
			) {
				connection.setRequestProperty("User-Agent", "Firefox");
			}

			connection = Net.checkTemporaryRedirect(connection, Net.SETUP_COMPRESS);
			long connectionLastModified = connection.getLastModified();
			connectionURL = connection.getURL();

			FileCache.Group cache = FileCache.getInstance().getGroup("feed");

			File file = cache.getFile(connectionURL, connectionLastModified);
			if ((file != null) && (file.length() != 0)) {
				log.debugFormat("Using file from cache: %s", file);

				info.data = file;
				
				return connectionURL;
			}
			
			if (connectionLastModified != 0) {
				info.cache = cache.newPath(connectionURL, connectionLastModified, null).toFile();
				log.debugFormat("Creating new cache entry: %s", info.cache);
			}
			
			input = new BufferedInputStream(Net.getInputStream(connection));
			contentType = connection.getContentType();
			
			info.data = new ByteArrayOutputStream(1024 * 64);
			FS.copyStream(input, (OutputStream)info.data);
		}
		finally {
			cancelDownload();
		}
		
		return connectionURL;
	}
	
	private Class<? extends AbstractChannel<?>> getHandlerByContentType() {
		if (contentType == null)
			return null;
		
		// reqular web page
		if (contentType.startsWith("text/"))
			return null;
		
		if (contentType.startsWith("application/atom+xml"))
			return Atom10Channel.class;
		
		if (contentType.startsWith("application/rdf+xml"))
			return RDFChannel.class;

		// if (contentType.startsWith("application/rss+xml") || contentType.startsWith("text/xml"))
		return RSSChannel.class;
	}
	
	private void getInfo() throws FeedListException, FileNotFoundException {
		FS.TextReader reader = null;
		try {
			if (info.data instanceof File)
				reader = FS.getUTF8Reader((File)info.data);
			else
				reader = FS.getUTF8Reader(info.getInputStream());
			
			boolean isXML = false;
			Scanner scanner = new Scanner(reader);
			scanner.useDelimiter("\\<");
			while (scanner.hasNext()) {
				String token = scanner.next().trim();
				String upperCaseToken = TK.toUpperCase(token);

				if (upperCaseToken.startsWith("HTML")) {
					// reset reader
					FS.close(reader);
					if (info.data instanceof File)
						reader = FS.getUTF8Reader((File)info.data);
					else
						reader = FS.getUTF8Reader(info.getInputStream());

					HTMLAutodiscovery ha = new HTMLAutodiscovery();
					StringBuilder baseURL = new StringBuilder();
					URL sourceURL = getSource(); // synchronized
					baseURL
						.append(sourceURL.getProtocol())
						.append("://")
						.append(sourceURL.getHost());
					ha.setBaseURL(baseURL.toString());
					for (HTMLAutodiscovery.Link i : ha.findLinks(reader)) {
						if (i.isFeed()) {
							if (info.links == null)
								info.links = new MArrayList<>();
							info.links.add(i);
						}
					}

					break; // while
				}

				// NOTE: some RSS feeds don't have <?xml... header
				if (token.startsWith("feed")) {
					if (token.contains("http://www.w3.org/2005/Atom"))
						info.handler = Atom10Channel.class;
					else if (token.contains("http://purl.org/atom/ns#"))
						info.handler = Atom0xChannel.class;

					break; // while
				}
				else if (token.startsWith("rdf:RDF")) {
					info.handler = RDFChannel.class;

					break; // while
				}
				else if (token.startsWith("rss")) {
					info.handler = RSSChannel.class;

					break; // while
				}
				else if (!isXML && token.startsWith("?xml")) {
					isXML = true;
					if (token.contains("encoding"))
						info.setEncoding(HTMLAutodiscovery.getAttribute(token, "encoding"));
				}
			}

			// "return" a list of feed links
			if (info.links != null)
				throw new FeedListException(this);
		}
		finally {
			FS.close(reader);
		}
	}

	// package

	synchronized void setSource(final URL value) { source = value; }
	
	// private classes
	
	private static final class Info {

		// private
		
		private Class<? extends AbstractChannel<?>> handler;
		private File cache;
		private List<HTMLAutodiscovery.Link> links;
		private Object data;
		private String encoding = "UTF-8";
		
		// public

		public String getEncoding() { return encoding; }
		
		public void setEncoding(final String value) {
			if (value == null) {
				encoding = "UTF-8";
			}
			else {
				try {
					if (!Charset.isSupported(value)) {
						MLogger.warning("feeds", "Unsupported encoding: \"%s\"", value); // static
						encoding = "UTF-8";
					}
					else {
						encoding = value;
					}
				}
				catch (Exception exception) {
					MLogger.exception(exception);
					encoding = "UTF-8";
				}
			}
		}

		public InputStream getInputStream() {
			return new ByteArrayInputStream(ByteArrayOutputStream.class.cast(data).toByteArray());
		}
		
	}

}
