package org.infoset.component.library;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import org.infoset.component.ItemFilterComponentDefinition;
import org.infoset.xml.DocumentLoader;
import org.infoset.xml.Element;
import org.infoset.xml.Item;
import org.infoset.xml.ItemDestination;
import org.infoset.xml.Name;
import org.infoset.xml.Named;
import org.infoset.xml.XMLException;
import org.infoset.xml.filter.ItemFilter;
import org.infoset.xml.filter.RemoveDocumentFilter;
import org.infoset.xml.sax.SAXDocumentLoader;

/**
 *
 * @author alex
 */
public class XIncludeComponentDefinition extends ItemFilterComponentDefinition {

    static Name XINCLUDE = Name.create("{http://www.w3.org/2001/XInclude}include");

    public static class XInclude implements ItemFilter {

        DocumentLoader loader = new SAXDocumentLoader();

        ItemDestination output = null;

        int level = -1;

        public void send(Item item) throws XMLException {
            if (level >= 0) {
                switch(item.getType()) {
                    case ElementItem:
                        level++;
                        break;
                    case ElementEndItem:
                        level--;
                }
            } else {
                if (item.getType() == Item.ItemType.ElementItem && ((Named) item).getName().equals(XINCLUDE)) {
                    level = 0;
                    if (output == null) {
                        return;
                    }
                    Element xinclude = (Element) item;
                    String href = xinclude.getAttributeValue("href");
                    if (href == null) {
                        throw new XMLException("Missing 'href' attribute on xinclude.");
                    }
                    URI location = xinclude.getBaseURI().resolve(href);
                    String parse = xinclude.getAttributeValue("parse");
                    boolean asXML = !"text".equals(parse);
                    if (asXML) {
                        try {
                            XInclude nextXInclude = new XInclude();
                            nextXInclude.attach(new RemoveDocumentFilter(output));
                            loader.generate(location, nextXInclude);
                        } catch (IOException ex) {
                            throw new XMLException("Cannot load xincluded document " + location, ex);
                        }
                    } else {
                        try {
                            URL url = location.toURL();
                            URLConnection connection = url.openConnection();
                            InputStream is = connection.getInputStream();
                            int statusCode = HttpURLConnection.HTTP_OK;
                            if (connection instanceof HttpURLConnection) {
                                statusCode = ((HttpURLConnection) connection).getResponseCode();
                            }
                            String contentType = connection.getContentType();
                            String charset = "UTF-8";
                            if (contentType != null) {
                                int semicolon = contentType.indexOf(';');
                                if (semicolon >= 0) {
                                    String type = contentType.substring(0, semicolon);
                                    String rest = contentType.substring(semicolon + 1);
                                    int equals = rest.indexOf('=');
                                    if (equals >= 0 && rest.substring(0, equals).equals("charset")) {
                                        charset = rest.substring(equals + 1);
                                    }
                                    contentType = type;
                                }
                            }
                            Reader input = new InputStreamReader(is, charset);
                            StringBuilder builder = new StringBuilder();
                            char[] buffer = new char[8192];
                            int len;
                            while ((len = input.read(buffer)) > 0) {
                                builder.append(buffer, 0, len);
                            }
                            output.send(item.getInfoset().createItemConstructor().createCharacters(builder.toString()));
                        } catch (IOException ex) {
                            throw new XMLException("Cannot load xincluded text document " + location, ex);
                        }
                    }
                } else {
                    if (output != null) {
                        output.send(item);
                    }
                }
            }
        }

        public void attach(ItemDestination dest) {
            this.output = dest;
        }
    }

    /** Creates a new instance of IdentityComonentDefinition */
    public XIncludeComponentDefinition(Name name, String vendor, String version, URI vendorLocation) {
        super(name, vendor, version, vendorLocation, XInclude.class);
    }

    public Name getXINCLUDE() {
        return XINCLUDE;
    }
}
