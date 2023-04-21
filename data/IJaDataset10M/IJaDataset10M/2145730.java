package net.sf.karatasi.updater;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import org.jetbrains.annotations.NotNull;
import org.xml.sax.Attributes;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.helpers.XMLReaderFactory;

/** Read the xml versions file from the url and present the data.
 * An instance always stores only data for one module.
 *
 * @author <a href="mailto:kussinger@sourceforge.net">Mathias Kussinger</a>
 */
public class Versions extends DefaultHandler {

    /** The structure of the data for a file bundle.
     */
    public final class Bundle {

        /** The bundle type, values as defined for the xml file. */
        private String type = null;

        /** The md5 hash value of the bundle. */
        private String md5Hash = null;

        /** The sha256 hash value of the bundle. */
        private String sha256Hash = null;

        /** The URL of the bundle. */
        private String url = null;

        /** Private default constructor */
        private Bundle() {
        }

        /** Getter for the type string.
         * @return the type
         */
        public String getType() {
            return type;
        }

        /** Getter for the md5 hash value.
         * @return the hash
         */
        public String getMd5Hash() {
            return md5Hash;
        }

        /** Getter for the sha256 hash value.
         * @return the hash
         */
        public String getSha256Hash() {
            return sha256Hash;
        }

        /** Getter for the file URL
         * @return the url
         */
        public String getUrl() {
            return url;
        }
    }

    /** The structure of a release.
     */
    private final class Release {

        /** The version number. */
        private VersionNumber version = null;

        /** The creation date. */
        @SuppressWarnings("unused")
        private long date = 0;

        /** The requirements text. */
        @SuppressWarnings("unused")
        private String requirements = null;

        /** The other attributes, like db and sync, the map uses the attribute names as keys. */
        private final Map<String, String> attributes = new HashMap<String, String>();

        /** The file bundles, by their type. */
        private final Map<String, Bundle> bundles = new HashMap<String, Bundle>();

        /** The description strings by their language keys. */
        private final Map<String, String> descriptions = new HashMap<String, String>();

        /** Private default constructor */
        private Release() {
        }
    }

    /** The name of the module the stored data belong to. */
    private String actualModuleName = null;

    /** The releases of the modules by their version. */
    private Map<VersionNumber, Release> releases = null;

    /** Default constructor.
     */
    public Versions() {
        super();
    }

    /** Constructor loading a versions file.
     * @param versionsFileURL the URL of the versions file
     * @param moduleName the name of the module, we are only loading version information for this module
     */
    public Versions(@NotNull final String versionsUrl, @NotNull final String moduleName) {
        super();
        try {
            read(versionsUrl, moduleName);
        } catch (final Exception e) {
            releases = null;
        }
    }

    /** Read a versions file and update the version data.
     * @param inputStream an input stream from the versions file
     * @param moduleName the name of the module, we are only loading version information for this module
     * @throws SAXException if parsing fails
     * @throws IOException
     */
    public void read(@NotNull final InputStream inputStream, @NotNull final String moduleName) throws SAXException, IOException {
        releases = new HashMap<VersionNumber, Release>();
        actualModuleName = moduleName;
        final XMLReader xmlReader = XMLReaderFactory.createXMLReader();
        xmlReader.setContentHandler(this);
        xmlReader.setErrorHandler(this);
        final BufferedReader inReader = new BufferedReader(new InputStreamReader(inputStream));
        final InputSource xmlInput = new InputSource(inReader);
        xmlReader.parse(xmlInput);
    }

    /** Read a versions file and update the version data.
     * @param versionsUrl the URL of the versions file
     * @param moduleName the name of the module, we are only loading version information for this module
     * @throws SAXException if parsing fails
     * @throws IOException
     */
    public void read(@NotNull final String versionsUrl, @NotNull final String moduleName) throws SAXException, IOException {
        final URL inUrl = new URL(versionsUrl);
        final InputStream inputStream = inUrl.openStream();
        read(inputStream, moduleName);
    }

    /** Get release count.
     * @return number of releases of the module found.
     */
    public int countReleases() {
        return releases.size();
    }

    /** Get the count of release later than a version number.
     * @param version the reference version
     * @return number of releases of the module found.
     */
    public int countReleasesLaterThen(final VersionNumber version) {
        int count = 0;
        for (final VersionNumber aVersion : releases.keySet()) {
            if (aVersion.compareTo(version) > 0) {
                count++;
            }
        }
        return count;
    }

    /** Get the highest version number available.
     * @return the version number or null if no version exists.
     */
    public VersionNumber highestVersionNumber() {
        VersionNumber version = null;
        for (final VersionNumber aVersion : releases.keySet()) {
            if (version == null || aVersion.compareTo(version) > 0) {
                version = aVersion;
            }
        }
        return version;
    }

    /** Get the highest version number available for a bundle type.
     * @param bundleType the type string of the bundle
     * @return the version number or null if no version exists.
     */
    public VersionNumber highestVersionNumber(final String bundleType) {
        VersionNumber version = null;
        for (final VersionNumber aVersion : releases.keySet()) {
            if ((version == null || aVersion.compareTo(version) > 0) && getBundleUrl(aVersion, bundleType) != null) {
                version = aVersion;
            }
        }
        return version;
    }

    /** Get bundle url  of the specified type for a version.
     * @param version the version of the bundle
     * @param type the type of the bundle, coding as for versions.xml
     * @return the url of the bundle, or null if no such bundle exists
     */
    public String getBundleUrl(final VersionNumber version, final String type) {
        final Release release = releases.get(version);
        if (release == null) {
            return null;
        }
        final Bundle bundle = release.bundles.get(type);
        if (bundle == null) {
            return null;
        }
        return bundle.url;
    }

    /** Get bundle of the specified type for a version.
     * @param version the version of the bundle
     * @param type the type of the bundle, coding as for versions.xml
     * @return the url of the bundle, or null if no such bundle exists
     */
    public Bundle getBundle(final VersionNumber version, final String type) {
        final Release release = releases.get(version);
        if (release == null) {
            return null;
        }
        final Bundle bundle = release.bundles.get(type);
        if (bundle == null) {
            return null;
        }
        return bundle;
    }

    /** Get a string with the html description of all versions between two version numbers.
     * The descriptions are ordered in ascending sequence.
     * @param lowVersion the lowest version number we print out, if null we start from the beginning
     * @param highVersion the highest version number we print out, if null be run up to the top
     * @param language the language tag, if the tag is not available for a version, the language with the empty tag is used
     * @return the description text with html formating
     */
    public String getDescriptions(final VersionNumber lowVersion, final VersionNumber highVersion, final String language) {
        VersionNumber lastVersion = null;
        if (lowVersion != null) {
            for (final VersionNumber aVersion : releases.keySet()) {
                if (aVersion.compareTo(lowVersion) >= 0) {
                    continue;
                }
                if (lastVersion == null || aVersion.compareTo(lastVersion) > 0) {
                    lastVersion = aVersion;
                }
            }
        }
        final StringBuffer descriptionAccumulator = new StringBuffer();
        do {
            VersionNumber nextVersion = null;
            for (final VersionNumber aVersion : releases.keySet()) {
                if (aVersion.compareTo(highVersion) > 0) {
                    continue;
                }
                if (lastVersion != null && aVersion.compareTo(lastVersion) <= 0) {
                    continue;
                }
                if (nextVersion == null || aVersion.compareTo(nextVersion) < 0) {
                    nextVersion = aVersion;
                }
            }
            lastVersion = nextVersion;
            if (lastVersion != null) {
                final Release release = releases.get(lastVersion);
                addDescription(descriptionAccumulator, release, language);
            }
        } while (lastVersion != null);
        return descriptionAccumulator.toString();
    }

    /** Get a string with the html description of all versions that are higher than a given version.
     * The descriptions are ordered in ascending sequence.
     * @param lowVersion the lowest version number we print out, if null we start from the beginning
     * @param highVersion the highest version number we print out, if null be run up to the top
     * @param language the language tag, if the tag is not available for a version, the language with the empty tag is used
     * @return the description text with html formating
     */
    public String getLaterDescriptions(final VersionNumber lowVersion, final VersionNumber highVersion, final String language) {
        VersionNumber lastVersion = lowVersion;
        final StringBuffer descriptionAccumulator = new StringBuffer();
        do {
            VersionNumber nextVersion = null;
            for (final VersionNumber aVersion : releases.keySet()) {
                if (aVersion.compareTo(highVersion) > 0) {
                    continue;
                }
                if (lastVersion != null && aVersion.compareTo(lastVersion) <= 0) {
                    continue;
                }
                if (nextVersion == null || aVersion.compareTo(nextVersion) < 0) {
                    nextVersion = aVersion;
                }
            }
            lastVersion = nextVersion;
            if (lastVersion != null) {
                final Release release = releases.get(lastVersion);
                addDescription(descriptionAccumulator, release, language);
            }
        } while (lastVersion != null);
        return descriptionAccumulator.toString();
    }

    /** String buffer for collecting raw string data. */
    private final StringBuffer rawDataAccumulator = new StringBuffer();

    /** String buffer for collecting description text. */
    private final StringBuffer descriptionAccumulator = new StringBuffer();

    /** The open release container. */
    private Release openRelease = null;

    /** The open bundle container. */
    private Bundle openBundle = null;

    /** The language key of a description text. */
    private String descriptionLanguageKey = null;

    /** Flag if list of description items has been opened. */
    private boolean descriptionListIsOpen = false;

    @Override
    public void characters(final char[] buffer, final int start, final int length) {
        if (rawDataAccumulator != null) {
            rawDataAccumulator.append(buffer, start, length);
        }
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes attributes) {
        rawDataAccumulator.setLength(0);
        if (localName.equals("releases")) {
        } else if (localName.equals("release")) {
            openRelease = null;
            openBundle = null;
            final String moduleName = attributes.getValue("", "module");
            if (moduleName != null && moduleName.equals(actualModuleName)) {
                openRelease = new Release();
                openRelease.version = new VersionNumber(attributes.getValue("", "version"));
                openRelease.date = convertDayToDate(attributes.getValue("", "date"));
                for (int n = 0; n < attributes.getLength(); n++) {
                    openRelease.attributes.put(attributes.getType(n), attributes.getValue(n));
                }
            }
        } else if (localName.equals("requirements")) {
            if (openRelease != null) {
                rawDataAccumulator.setLength(0);
            }
        } else if (localName.equals("bundle")) {
            if (openRelease != null) {
                openBundle = new Bundle();
                openBundle.type = attributes.getValue("", "type");
                openBundle.md5Hash = attributes.getValue("", "md5");
                openBundle.sha256Hash = attributes.getValue("", "sha256");
                rawDataAccumulator.setLength(0);
            }
        } else if (localName.equals("description")) {
            if (openRelease != null) {
                descriptionLanguageKey = attributes.getValue("", "language");
                if (descriptionLanguageKey == null) {
                    descriptionLanguageKey = "";
                }
                descriptionAccumulator.setLength(0);
                descriptionListIsOpen = false;
            }
        } else if (localName.equals("li")) {
            if (openRelease != null) {
                rawDataAccumulator.setLength(0);
            }
        } else if (localName.equals("p")) {
            if (openRelease != null) {
                rawDataAccumulator.setLength(0);
            }
        }
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        if (localName.equals("releases")) {
        } else if (localName.equals("release")) {
            if (openRelease != null) {
                releases.put(openRelease.version, openRelease);
            }
        } else if (localName.equals("requirements")) {
            if (openRelease != null) {
                openRelease.requirements = rawDataAccumulator.toString();
            }
        } else if (localName.equals("bundle")) {
            if (openRelease != null && openBundle != null) {
                openBundle.url = rawDataAccumulator.toString();
                openRelease.bundles.put(openBundle.type, openBundle);
                openBundle = null;
            }
        } else if (localName.equals("description")) {
            if (openRelease != null && descriptionLanguageKey != null) {
                if (descriptionListIsOpen) {
                    descriptionAccumulator.append("</ul>\n");
                    descriptionListIsOpen = false;
                }
                openRelease.descriptions.put(descriptionLanguageKey, descriptionAccumulator.toString());
                descriptionLanguageKey = null;
            }
        } else if (localName.equals("li")) {
            if (openRelease != null) {
                if (!descriptionListIsOpen) {
                    descriptionAccumulator.append("<ul>\n");
                    descriptionListIsOpen = true;
                }
                descriptionAccumulator.append("<li>");
                descriptionAccumulator.append(stringToHTMLString(rawDataAccumulator.toString()));
                descriptionAccumulator.append("</li>\n");
            }
        } else if (localName.equals("p")) {
            if (openRelease != null) {
                if (descriptionListIsOpen) {
                    descriptionAccumulator.append("</ul>\n");
                    descriptionListIsOpen = false;
                }
                descriptionAccumulator.append("<p>");
                descriptionAccumulator.append(stringToHTMLString(rawDataAccumulator.toString()));
                descriptionAccumulator.append("</p>\n");
            }
        }
    }

    /** Converts a Java string into a html string with the required escape sequences.
     * From http://stackoverflow.com/questions/1224996/java-convert-string-to-html-string
     * @param string the Java input string
     * @return the converted Java string
     */
    private String stringToHTMLString(final String string) {
        final StringBuffer sb = new StringBuffer(string.length());
        final int len = string.length();
        char c;
        for (int i = 0; i < len; i++) {
            c = string.charAt(i);
            if (c == ' ' || c == '\n') {
                sb.append(' ');
            } else if (c == '"') {
                sb.append("&quot;");
            } else if (c == '&') {
                sb.append("&amp;");
            } else if (c == '<') {
                sb.append("&lt;");
            } else if (c == '>') {
                sb.append("&gt;");
            } else if (c == '\n') {
                sb.append(' ');
            } else {
                final int ci = 0xffff & c;
                if (ci < 160) {
                    sb.append(c);
                } else {
                    sb.append("&#");
                    sb.append(new Integer(ci).toString());
                    sb.append(';');
                }
            }
        }
        return sb.toString();
    }

    /** Convert a day value from the XML file into a Java time int.
     * @param dayString the date as a german date string
     * @return the date as ms since 1.1.1970
     */
    private long convertDayToDate(@NotNull final String dayString) {
        final String[] dayMonYear = dayString.split("\\.");
        final GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeZone(TimeZone.getTimeZone("GMT"));
        calendar.set(Integer.parseInt(dayMonYear[2]), Integer.parseInt(dayMonYear[1]) - 1, Integer.parseInt(dayMonYear[0]));
        return calendar.getTimeInMillis();
    }

    /** Add the description of a version to a string buffer.
     * @param stringBuffer the buffer to add the description data to.
     * @param release the release we want to document
     * @param language the language tag, if the tag is not available for a version, the language with the empty tag is used
     */
    private void addDescription(@NotNull final StringBuffer stringBuffer, @NotNull final Release release, @NotNull final String language) {
        stringBuffer.append("<b>");
        stringBuffer.append(release.version.toString());
        stringBuffer.append("</b>:<br>\n");
        String description = release.descriptions.get(language);
        if (description == null) {
            description = release.descriptions.get("");
            if (description == null) {
                description = "";
            }
        }
        stringBuffer.append(description);
        stringBuffer.append("<br>\n");
    }
}
