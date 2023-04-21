package org.openxml4j.opc;

import java.net.URI;
import java.net.URISyntaxException;
import org.openxml4j.exceptions.InvalidFormatException;
import org.openxml4j.exceptions.InvalidOperationException;
import org.openxml4j.exceptions.InvalidPackageURI;

/**
 * Helper for part and pack URI.
 * 
 * @author Julien Chable, CDubet, Kim Ung
 * @version 0.1
 */
public final class PackagingURIHelper {

    /**
	 * Package root URI.
	 */
    private static URI packageRootUri;

    /**
	 * Extension name of a relationship part.
	 */
    public static final String RELATIONSHIP_PART_EXTENSION_NAME;

    /**
	 * Segment name of a relationship part.
	 */
    public static final String RELATIONSHIP_PART_SEGMENT_NAME;

    /**
	 * Segment name of the package properties folder.
	 */
    public static final String PACKAGE_PROPERTIES_SEGMENT_NAME;

    /**
	 * Core package properties art name.
	 */
    public static final String PACKAGE_CORE_PROPERTIES_NAME;

    /**
	 * Forward slash URI separator.
	 */
    public static final char FORWARD_SLASH_CHAR;

    /**
	 * Forward slash URI separator.
	 */
    public static final String FORWARD_SLASH_STRING;

    /**
	 * Prefix pack:// for external relationship.
	 */
    public static final String PACK_PREFIX;

    /**
	 * Package relationships part URI
	 */
    public static final URI PACKAGE_RELATIONSHIPS_ROOT_URI;

    /**
	 * Package relationships part name.
	 */
    public static final PackagePartName PACKAGE_RELATIONSHIPS_ROOT_PART_NAME;

    /**
	 * Core properties part URI.
	 */
    public static final URI CORE_PROPERTIES_URI;

    /**
	 * Core properties partname.
	 */
    public static final PackagePartName CORE_PROPERTIES_PART_NAME;

    /**
	 * Root package URI.
	 */
    public static final URI PACKAGE_ROOT_URI;

    /**
	 * Root package part name.
	 */
    public static final PackagePartName PACKAGE_ROOT_PART_NAME;

    static {
        RELATIONSHIP_PART_SEGMENT_NAME = "_rels";
        RELATIONSHIP_PART_EXTENSION_NAME = ".rels";
        PACK_PREFIX = "pack://";
        FORWARD_SLASH_CHAR = '/';
        FORWARD_SLASH_STRING = "/";
        PACKAGE_PROPERTIES_SEGMENT_NAME = "docProps";
        PACKAGE_CORE_PROPERTIES_NAME = "core.xml";
        URI uriPACKAGE_ROOT_URI = null;
        URI uriPACKAGE_RELATIONSHIPS_ROOT_URI = null;
        URI uriPACKAGE_PROPERTIES_URI = null;
        try {
            uriPACKAGE_ROOT_URI = new URI("/");
            uriPACKAGE_RELATIONSHIPS_ROOT_URI = new URI(FORWARD_SLASH_CHAR + RELATIONSHIP_PART_SEGMENT_NAME + FORWARD_SLASH_CHAR + RELATIONSHIP_PART_EXTENSION_NAME);
            packageRootUri = new URI("/");
            uriPACKAGE_PROPERTIES_URI = new URI(FORWARD_SLASH_CHAR + PACKAGE_PROPERTIES_SEGMENT_NAME + FORWARD_SLASH_CHAR + PACKAGE_CORE_PROPERTIES_NAME);
        } catch (URISyntaxException e) {
        }
        PACKAGE_ROOT_URI = uriPACKAGE_ROOT_URI;
        PACKAGE_RELATIONSHIPS_ROOT_URI = uriPACKAGE_RELATIONSHIPS_ROOT_URI;
        CORE_PROPERTIES_URI = uriPACKAGE_PROPERTIES_URI;
        PackagePartName tmpPACKAGE_ROOT_PART_NAME = null;
        PackagePartName tmpPACKAGE_RELATIONSHIPS_ROOT_PART_NAME = null;
        PackagePartName tmpCORE_PROPERTIES_URI = null;
        try {
            tmpPACKAGE_RELATIONSHIPS_ROOT_PART_NAME = createPartName(PACKAGE_RELATIONSHIPS_ROOT_URI);
            tmpCORE_PROPERTIES_URI = createPartName(CORE_PROPERTIES_URI);
            tmpPACKAGE_ROOT_PART_NAME = new PackagePartName(PACKAGE_ROOT_URI, false);
        } catch (InvalidFormatException e) {
        }
        PACKAGE_RELATIONSHIPS_ROOT_PART_NAME = tmpPACKAGE_RELATIONSHIPS_ROOT_PART_NAME;
        CORE_PROPERTIES_PART_NAME = tmpCORE_PROPERTIES_URI;
        PACKAGE_ROOT_PART_NAME = tmpPACKAGE_ROOT_PART_NAME;
    }

    /**
	 * Gets the URI for the package root.
	 * 
	 * @return URI of the package root.
	 */
    public static URI getPackageRootUri() {
        return packageRootUri;
    }

    /**
	 * Know if the specified URI is a relationship part name.
	 * 
	 * @param partUri
	 *            URI to check.
	 * @return <i>true</i> if the URI <i>false</i>.
	 */
    public static boolean isRelationshipPartURI(URI partUri) {
        if (partUri == null) throw new IllegalArgumentException("partUri");
        return partUri.getPath().matches(".*" + RELATIONSHIP_PART_SEGMENT_NAME + ".*" + RELATIONSHIP_PART_EXTENSION_NAME + "$");
    }

    /**
	 * Get file name from the specified URI.
	 */
    public static String getFilename(URI uri) {
        if (uri != null) {
            String path = uri.getPath();
            int len = path.length();
            int num2 = len;
            while (--num2 >= 0) {
                char ch1 = path.charAt(num2);
                if (ch1 == PackagingURIHelper.FORWARD_SLASH_CHAR) return path.substring(num2 + 1, len);
            }
        }
        return "";
    }

    /**
	 * Get the file name without the trailing extension.
	 */
    public static String getFilenameWithoutExtension(URI uri) {
        String filename = getFilename(uri);
        int dotIndex = filename.lastIndexOf(".");
        if (dotIndex == -1) return filename;
        return filename.substring(0, dotIndex);
    }

    /**
	 * Get the directory path from the specified URI.
	 */
    public static URI getPath(URI uri) {
        if (uri != null) {
            String path = uri.getPath();
            int len = path.length();
            int num2 = len;
            while (--num2 >= 0) {
                char ch1 = path.charAt(num2);
                if (ch1 == PackagingURIHelper.FORWARD_SLASH_CHAR) {
                    try {
                        return new URI(path.substring(0, num2));
                    } catch (URISyntaxException e) {
                        return null;
                    }
                }
            }
        }
        return null;
    }

    /**
	 * Combine les deux URI.
	 * 
	 * @param prefix
	 *            L'URI de pr�fixe.
	 * @param suffix
	 *            L'URI de suffixe.
	 * @return
	 */
    public static URI combine(URI prefix, URI suffix) {
        URI retUri = null;
        try {
            retUri = new URI(combine(prefix.getPath(), suffix.getPath()));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("Prefix and suffix can't be combine !");
        }
        return retUri;
    }

    /**
	 * Combine a string URI with a prefix and a suffix.
	 */
    public static String combine(String prefix, String suffix) {
        if (!prefix.endsWith("" + FORWARD_SLASH_CHAR) && !suffix.startsWith("" + FORWARD_SLASH_CHAR)) return prefix + FORWARD_SLASH_CHAR + suffix; else if ((!prefix.endsWith("" + FORWARD_SLASH_CHAR) && suffix.startsWith("" + FORWARD_SLASH_CHAR) || (prefix.endsWith("" + FORWARD_SLASH_CHAR) && !suffix.startsWith("" + FORWARD_SLASH_CHAR)))) return prefix + suffix; else return "";
    }

    /**
	 * Fully relativize the source part URI against the target part URI.
	 * 
	 * @param sourceURI
	 *            The source part URI.
	 * @param targetURI
	 *            The target part URI.
	 * @return A fully relativize part name URI ('word/media/image1.gif',
	 *         '/word/document.xml' => 'media/image1.gif') else
	 *         <code>null</code>.
	 */
    public static URI relativizeURI(URI sourceURI, URI targetURI) {
        StringBuilder retVal = new StringBuilder();
        String[] segmentsSource = sourceURI.getPath().split("/", -1);
        String[] segmentsTarget = targetURI.getPath().split("/", -1);
        if (segmentsSource.length == 0) {
            throw new IllegalArgumentException("Can't relativize an empty source URI !");
        }
        if (segmentsTarget.length == 0) {
            throw new IllegalArgumentException("Can't relativize an empty target URI !");
        }
        if (sourceURI.toString().equals("/")) {
            return targetURI;
        }
        int segmentsTheSame = 0;
        for (int i = 0; i < segmentsSource.length && i < segmentsTarget.length; i++) {
            if (segmentsSource[i].equals(segmentsTarget[i])) {
                segmentsTheSame++;
            } else {
                break;
            }
        }
        if ((segmentsTheSame == 0 || segmentsTheSame == 1) && segmentsSource[0].equals("") && segmentsTarget[0].equals("")) {
            for (int i = 0; i < segmentsSource.length - 2; i++) {
                retVal.append("../");
            }
            for (int i = 0; i < segmentsTarget.length; i++) {
                if (segmentsTarget[i].equals("")) continue;
                retVal.append(segmentsTarget[i]);
                if (i != segmentsTarget.length - 1) retVal.append("/");
            }
            try {
                return new URI(retVal.toString());
            } catch (Exception e) {
                System.err.println(e);
                return null;
            }
        }
        if (segmentsTheSame == segmentsSource.length && segmentsTheSame == segmentsTarget.length) {
            retVal.append("");
        } else {
            if (segmentsTheSame == 1) {
                retVal.append("/");
            } else {
                for (int j = segmentsTheSame; j < segmentsSource.length - 1; j++) {
                    retVal.append("../");
                }
            }
            for (int j = segmentsTheSame; j < segmentsTarget.length; j++) {
                if (retVal.length() > 0 && retVal.charAt(retVal.length() - 1) != '/') {
                    retVal.append("/");
                }
                retVal.append(segmentsTarget[j]);
            }
        }
        try {
            return new URI(retVal.toString());
        } catch (Exception e) {
            System.err.println(e);
            return null;
        }
    }

    /**
	 * Resolve a source uri against a target.
	 * 
	 * @param sourcePartUri
	 *            The source URI.
	 * @param targetUri
	 *            The target URI.
	 * @return The resolved URI.
	 */
    public static URI resolvePartUri(URI sourcePartUri, URI targetUri) {
        if (sourcePartUri == null || sourcePartUri.isAbsolute()) {
            throw new IllegalArgumentException("sourcePartUri invalid - " + sourcePartUri);
        }
        if (targetUri == null || targetUri.isAbsolute()) {
            throw new IllegalArgumentException("targetUri invalid - " + targetUri);
        }
        return sourcePartUri.resolve(targetUri);
    }

    /**
	 * Get URI from a string path.
	 */
    public static URI getURIFromPath(String path) {
        URI retUri = null;
        try {
            retUri = new URI(path);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException("path");
        }
        return retUri;
    }

    /**
	 * Get the source part URI from a specified relationships part.
	 * 
	 * @param relationshipPartUri
	 *            The relationship part use to retrieve the source part.
	 * @return The source part URI from the specified relationships part.
	 */
    public static URI getSourcePartUriFromRelationshipPartUri(URI relationshipPartUri) {
        if (relationshipPartUri == null) throw new IllegalArgumentException("Le param�tre relationshipPartUri ne doit pas �tre null !");
        if (!isRelationshipPartURI(relationshipPartUri)) throw new IllegalArgumentException("L'URI ne doit pas �tre celle d'une partie de type relation.");
        if (relationshipPartUri.compareTo(PACKAGE_RELATIONSHIPS_ROOT_URI) == 0) return PACKAGE_ROOT_URI;
        String filename = relationshipPartUri.getPath();
        String filenameWithoutExtension = getFilenameWithoutExtension(relationshipPartUri);
        filename = filename.substring(0, ((filename.length() - filenameWithoutExtension.length()) - RELATIONSHIP_PART_EXTENSION_NAME.length()));
        filename = filename.substring(0, filename.length() - RELATIONSHIP_PART_SEGMENT_NAME.length() - 1);
        filename = combine(filename, filenameWithoutExtension);
        return getURIFromPath(filename);
    }

    /**
	 * Create an OPC compliant part name by throwing an exception if the URI is
	 * not valid.
	 * 
	 * @param partUri
	 *            The part name URI to validate.
	 * @return A valid part name object, else <code>null</code>.
	 * @throws InvalidFormatException
	 *             Throws if the specified URI is not OPC compliant.
	 */
    public static PackagePartName createPartName(URI partUri) throws InvalidFormatException {
        if (partUri == null) throw new IllegalArgumentException("partName");
        return new PackagePartName(partUri, true);
    }

    /**
	 * Create an OPC compliant part name.
	 * 
	 * @param partName
	 *            The part name to validate.
	 * @return The correspondant part name if valid, else <code>null</code>.
	 * @throws InvalidFormatException
	 *             Throws if the specified part name is not OPC compliant.
	 * @see #createPartName(URI)
	 */
    public static PackagePartName createPartName(String partName) throws InvalidFormatException {
        URI partNameURI;
        try {
            partNameURI = new URI(partName);
        } catch (URISyntaxException e) {
            throw new InvalidFormatException(e.getMessage());
        }
        return createPartName(partNameURI);
    }

    /**
	 * Create an OPC compliant part name by resolving it using a base part.
	 * 
	 * @param partName
	 *            The part name to validate.
	 * @param relativePart
	 *            The relative base part.
	 * @return The correspondant part name if valid, else <code>null</code>.
	 * @throws InvalidFormatException
	 *             Throws if the specified part name is not OPC compliant.
	 * @see #createPartName(URI)
	 */
    public static PackagePartName createPartName(String partName, PackagePart relativePart) throws InvalidFormatException {
        URI newPartNameURI;
        try {
            newPartNameURI = resolvePartUri(relativePart.getPartName().getURI(), new URI(partName));
        } catch (URISyntaxException e) {
            throw new InvalidFormatException(e.getMessage());
        }
        return createPartName(newPartNameURI);
    }

    /**
	 * Create an OPC compliant part name by resolving it using a base part.
	 * 
	 * @param partName
	 *            The part name URI to validate.
	 * @param relativePart
	 *            The relative base part.
	 * @return The correspondant part name if valid, else <code>null</code>.
	 * @throws InvalidFormatException
	 *             Throws if the specified part name is not OPC compliant.
	 * @see #createPartName(URI)
	 */
    public static PackagePartName createPartName(URI partName, PackagePart relativePart) throws InvalidFormatException {
        URI newPartNameURI = resolvePartUri(relativePart.getPartName().getURI(), partName);
        return createPartName(newPartNameURI);
    }

    /**
	 * Validate a part URI by returning a boolean.
	 * ([M1.1],[M1.3],[M1.4],[M1.5],[M1.6])
	 * 
	 * (OPC Specifications 8.1.1 Part names) :
	 * 
	 * Part Name Syntax
	 * 
	 * The part name grammar is defined as follows:
	 * 
	 * <i>part_name = 1*( "/" segment )
	 * 
	 * segment = 1*( pchar )</i>
	 * 
	 * 
	 * (pchar is defined in RFC 3986)
	 * 
	 * @param partUri
	 *            The URI to validate.
	 * @return <b>true</b> if the URI is valid to the OPC Specifications, else
	 *         <b>false</b>
	 * 
	 * @see #createPartName(URI)
	 */
    public static boolean isValidPartName(URI partUri) {
        if (partUri == null) throw new IllegalArgumentException("partUri");
        try {
            createPartName(partUri);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
	 * Decode a URI by converting all percent encoded character into a String
	 * character.
	 * 
	 * @param uri
	 *            The URI to decode.
	 * @return The specified URI in a String with converted percent encoded
	 *         characters.
	 */
    public static String decodeURI(URI uri) {
        StringBuffer retVal = new StringBuffer();
        String uriStr = uri.toASCIIString();
        char c;
        for (int i = 0; i < uriStr.length(); ++i) {
            c = uriStr.charAt(i);
            if (c == '%') {
                if (((uriStr.length() - i) < 2)) {
                    throw new IllegalArgumentException("The uri " + uriStr + " contain invalid encoded character !");
                }
                char decodedChar = (char) Integer.parseInt(uriStr.substring(i + 1, i + 3), 16);
                retVal.append(decodedChar);
                i += 2;
                continue;
            }
            retVal.append(c);
        }
        return retVal.toString();
    }

    /**
	 * Build a part name where the relationship should be stored ((ex
	 * /word/document.xml -> /word/_rels/document.xml.rels)
	 * 
	 * @param partUri
	 *            Source part URI
	 * @return the full path (as URI) of the relation file
	 * @throws InvalidOperationException
	 *             Throws if the specified URI is a relationshp part.
	 */
    public static PackagePartName getRelationshipPartName(PackagePartName partName) {
        if (partName == null) throw new IllegalArgumentException("partName");
        if (PackagingURIHelper.PACKAGE_ROOT_URI.getPath() == partName.getURI().getPath()) return PackagingURIHelper.PACKAGE_RELATIONSHIPS_ROOT_PART_NAME;
        if (partName.isRelationshipPartURI()) throw new InvalidOperationException("Can't be a relationship part");
        String fullPath = partName.getURI().getPath();
        String filename = getFilename(partName.getURI());
        fullPath = fullPath.substring(0, fullPath.length() - filename.length());
        fullPath = combine(fullPath, PackagingURIHelper.RELATIONSHIP_PART_SEGMENT_NAME);
        fullPath = combine(fullPath, filename);
        fullPath = fullPath + PackagingURIHelper.RELATIONSHIP_PART_EXTENSION_NAME;
        PackagePartName retPartName;
        try {
            retPartName = createPartName(fullPath);
        } catch (InvalidFormatException e) {
            return null;
        }
        return retPartName;
    }

    /**
	 * Creates a pack URI from a standard URI.
	 * 
	 * Based on ECMA 376 Annex B3.
	 */
    public static String createPackURIFromPartName(URI packageUri, PackagePartName partName) {
        if (packageUri == null) throw new IllegalArgumentException("uri");
        if (partName == null) throw new IllegalArgumentException("partName");
        String retUri = packageUri.toString();
        if (packageUri.getRawFragment() != null) retUri = retUri.substring(0, retUri.indexOf("#"));
        retUri = retUri.replaceAll("%", "%25");
        retUri = retUri.replaceAll("\\?", "%3f");
        retUri = retUri.replaceAll("@", "%40");
        retUri = retUri.replaceAll(":", "%3a");
        retUri = retUri.replaceAll(",", "%2c");
        retUri = retUri.replaceAll("/", ",");
        retUri = PACK_PREFIX + retUri;
        retUri = retUri + FORWARD_SLASH_CHAR;
        try {
            URI uri2 = new URI(retUri);
            retUri = uri2.resolve(partName.getURI()).toString();
        } catch (URISyntaxException e) {
            return null;
        }
        return retUri;
    }

    /**
	 * Retrieve a package URI and part name from a Pack URI. The last two
	 * arguments are return by the method.
	 * 
	 * Based on ECMA 376 Annex B2.
	 * 
	 * @param resourceUri Resource URI to resolve.
	 * @param retPackageUri The returned package URI from the resource URI.
	 * @param retPartName The returned part name from the resource URI.
	 */
    public static void resolvePackURI(String resourceUri, URI retPackageUri, PackagePartName retPartName) throws InvalidPackageURI {
        try {
            URI uri = new URI(resourceUri);
            resolvePackURI(uri, retPackageUri, retPartName);
        } catch (Exception e) {
            retPackageUri = null;
            retPartName = null;
            return;
        }
    }

    /**
	 * Retrieve a package URI and part name from a Pack URI. The last two
	 * arguments are return by the method.
	 * 
	 * Based on ECMA 376 Annex B2.
	 * 
	 * @param resourceUri Resource URI to resolve.
	 * @param retPackageUri The returned package URI from the resource URI.
	 * @param retPartName The returned part name from the resource URI.
	 */
    public static void resolvePackURI(URI resourceUri, URI retPackageURI, PackagePartName retPartName) throws InvalidPackageURI {
        String authority = resourceUri.getRawAuthority();
        String path = resourceUri.getRawPath();
        authority = authority.replace(',', '/');
        authority = authority.replaceAll("%25", "%");
        authority = authority.replaceAll("%3f", "?");
        authority = authority.replaceAll("%40", "@");
        authority = authority.replaceAll("%3a", ":");
        authority = authority.replaceAll("%2c", ",");
        try {
            retPackageURI = new URI(authority);
        } catch (URISyntaxException e) {
            retPackageURI = null;
        }
        if (path == null) {
            retPartName = null;
            return;
        }
        try {
            retPartName = PackagingURIHelper.createPartName(path);
        } catch (InvalidFormatException e) {
            throw new IllegalArgumentException("Invalid pack URI -> Invalid part name");
        }
    }
}
