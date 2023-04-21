
package org.makagiga.plugins.weather;

import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;

import org.makagiga.commons.FS;
import org.makagiga.commons.TK;
import org.makagiga.commons.MArrayList;
import org.makagiga.commons.xml.SimpleXMLReader;

/**
 * @see <a href="http://developer.yahoo.com/geo/geoplanet/guide/api-reference.html">http://developer.yahoo.com/geo/geoplanet/guide/api-reference.html</a>
 */
public final class Location {

	// private
	
	private final MArrayList<Place> places = new MArrayList<>();

	// public
	
	public Location(final InputStream input) throws IOException {
		Objects.requireNonNull(input);

		SimpleXMLReader reader = null;
		try {
			reader = new SimpleXMLReader() {
				private final LinkedHashSet<String> desc = new LinkedHashSet<>();
				private Place place;
				@Override
				protected void onEnd(final String name) {
					if (name.equals("place")) {
						this.place.desc = TK.toString(this.desc, ", ");
						Location.this.places.add(this.place);
						this.place = null;
						this.desc.clear();
					}
				}
				@Override
				protected void onStart(final String name) {
					if ((this.place == null) && name.equals("place")) {
						this.place = new Place();
					}
					else if (this.place != null) {
						switch (name) {
							case "name":
								this.place.name = this.getValue();
								break;
							case "woeid":
								this.place.woeid = this.getValue();
								break;
							case "admin1":
							case "admin2":
							case "country":
								String s = this.getValue();
								if (!TK.isEmpty(s))
									this.desc.add(s);
								break;
							case "placeTypeName":
								this.place.typeCode = this.getIntegerAttribute("code", 0);
								this.place.typeName = this.getValue();
								break;
						}
					}
				}
			};
			reader.read(input);
		}
		finally {
			FS.close(reader);
		}
		places.sort();
	}

	public List<Place> getPlaces() { return places; }
	
	public static URL getAPIURL(final String query) throws MalformedURLException {
// TEST: http://where.yahooapis.com/v1/places.q%28Szczecin%29?select=long&appid=D9.gcJ3V34EbbuwWlEZ_VZdIP3IlP0aCGcrl5QptgOhze_wZCaZRUnEVLDcWOQo8D1D6qgQvfw--
// TEST: http://where.yahooapis.com/v1/places.q%28Szczecin%29;count=0?&select=long&appid=D9.gcJ3V34EbbuwWlEZ_VZdIP3IlP0aCGcrl5QptgOhze_wZCaZRUnEVLDcWOQo8D1D6qgQvfw--
		String appID = "D9.gcJ3V34EbbuwWlEZ_VZdIP3IlP0aCGcrl5QptgOhze_wZCaZRUnEVLDcWOQo8D1D6qgQvfw--";
		
		URL url = new URL(
			"http://where.yahooapis.com/v1/places.q(" + TK.escapeURL(query) + ");count=0?&select=long&" +
			"appid=" + TK.escapeURL(appID)
		);
		//System.err.println(url);
		
		return url;
	}

	// public classes
	
	public static final class Place implements Comparable<Place> {
	
		// private
		
		private int typeCode;
		private String desc;
		private String name;
		private String typeName;
		private String woeid;
	
		// public

		public String getName() {
			return Objects.toString(name, "?");
		}

		public String getWOEID() {
			return Objects.toString(woeid, "");
		}
		
		@Override
		public String toString() {
			StringBuilder s = new StringBuilder();
			
			if (!TK.isEmpty(typeName))
				s.append(typeName).append(": ");
			
			if (!TK.isEmpty(name))
				s.append(name);
			
			if (!TK.isEmpty(desc)) {
				if (s.length() > 0)
					s.append(", ");
				s.append(desc);
			}
			
			s.append(", WOEID=").append(woeid);
			
			return s.toString();
		}
		
		// Comparable
		
		@Override
		public int compareTo(final Place o) {
			int c1 = (this.typeCode == 0) ? Integer.MAX_VALUE : this.typeCode;
			int c2 = (o.typeCode == 0) ? Integer.MAX_VALUE : o.typeCode;
		
			return Integer.compare(c1, c2);
		}
		
		// private
		
		private Place() { }
	
	}

}