// Copyright 2008 Konrad Twardowski
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

package org.makagiga.commons.html;

import java.io.Reader;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

import org.makagiga.commons.MArrayList;
import org.makagiga.commons.Net;
import org.makagiga.commons.TK;
import org.makagiga.commons.annotation.Important;

/**
 * @since 3.0, 4.0 (org.makagiga.commons.html package)
 */
public final class HTMLAutodiscovery {

	// private

	private String baseURL;
	
	// public

	public HTMLAutodiscovery() { }

	public List<HTMLAutodiscovery.Link> findLinks(final Reader reader) {
		MArrayList<HTMLAutodiscovery.Link> result = new MArrayList<>();

		boolean inHTMLHead = false;
		boolean inHTMLLink = false;
		boolean inHTMLRoot = false;
		Link link = null;

		Scanner scanner = new Scanner(reader);
		scanner.useDelimiter("\\<");
		while (scanner.hasNext()) {
			String token = scanner.next().trim();
			String upperCaseToken = TK.toUpperCase(token);

			// parse HTML and find all "link" tags
			if (inHTMLRoot && inHTMLHead) {
				// add link
				if (inHTMLLink) {
					if (link.href != null)
						result.add(link);
					inHTMLLink = false;
				}

				if (upperCaseToken.startsWith("LINK")) {
					link = new Link();
					inHTMLLink = true;
// FIXME: 2.0: parse attributes
					if ((link.href == null) && TK.containsIgnoreCase(token, "href=", Locale.ENGLISH)) {
						link.href = getAttribute(token, "href");
						if ((baseURL != null) && !Net.isHTTP(link.href)) {
							if (!TK.startsWith(link.href, '/'))
								link.href = ("/" + link.href);
							link.href = (baseURL + link.href);
						}
					}
					if ((link.rel == null) && TK.containsIgnoreCase(token, "rel=", Locale.ENGLISH))
						link.rel = getAttribute(token, "rel");
					if ((link.title == null) && TK.containsIgnoreCase(token, "title=", Locale.ENGLISH)) {
						link.title = getAttribute(token, "title");
						link.title = TK.unescapeXML(link.title);
					}
					if ((link.type == null) && TK.containsIgnoreCase(token, "type=", Locale.ENGLISH))
						link.type = getAttribute(token, "type");
				}

				continue; // while
			}

			if (!inHTMLRoot && upperCaseToken.startsWith("HTML")) {
				inHTMLRoot = true;
			}
			else if (
				inHTMLRoot &&
				!inHTMLHead &&
				(
					upperCaseToken.startsWith("HEAD") ||
					// for broken websites
					upperCaseToken.startsWith("LINK") ||
					upperCaseToken.startsWith("META") ||
					upperCaseToken.startsWith("SCRIPT") ||
					upperCaseToken.startsWith("TITLE")
				)
			) {
				inHTMLHead = true;
			}
		}

		return result;
	}

	public static String getAttribute(final String line, final String name) {
		final String UPPER_NAME = TK.toUpperCase(name);
		String result = getValue(line, UPPER_NAME, '"');
		if (result == null)
			result = getValue(line, UPPER_NAME, '\'');

		return result;
	}

	public String getBaseURL() { return baseURL; }

	public void setBaseURL(final String baseURL) { this.baseURL = baseURL; }
	
	// private

	private static String getValue(final String line, final String name, final char quote) {
		final String ATTR_START = name + "=" + quote;
		int startAttrIndex = TK.toUpperCase(line).indexOf(ATTR_START);
		if (startAttrIndex != -1) {
			String attr = line.substring(startAttrIndex + ATTR_START.length());
			int endAttrIndex = attr.indexOf(quote);

			if (endAttrIndex != -1)
				return attr.substring(0, endAttrIndex);
		}

		return null;
	}

	// public classes

	public static final class Link {

		// private

		private String href;
		private String rel;
		private String title;
		private String type;

		// public

		public Link() { }

		public String getHref() { return href; }

		public String getRel() { return rel; }

		public String getTitle() { return title; }

		public String getType() { return type; }

		public boolean isFeed() {
			return
				"alternate".equals(rel) &&
				(
					"application/atom+xml".equals(type) ||
					"application/rdf+xml".equals(type) ||
					"application/rss+xml".equals(type) ||
					"text/xml".equals(type)
				);
		}

		@Important
		@Override
		public String toString() {
			return TK.isEmpty(title) ? href : title;
		}

	}

}
