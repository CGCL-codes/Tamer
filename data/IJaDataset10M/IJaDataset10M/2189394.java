package net.sourceforge.eclipsex.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import net.sourceforge.eclipsex.loader.EXFileResource;
import net.sourceforge.eclipsex.loader.EXLoader;
import net.sourceforge.eclipsex.parser.ASClassDef;
import net.sourceforge.eclipsex.parser.ASElement;
import net.sourceforge.eclipsex.parser.ASIdentifier;

public class TestingUtils {

    /**
     * looks for a marker in the text and then (if not null) for matching string
     * and returns the offset at the end TODO return a Selection or better make
     * a class utility wich can eliminate the marker from the text or others
     * utils
     */
    public static int getASMarkerOffset(final String text, final String id, final String matching) {
        return getMarkerOffset(text, "/*##" + id + "##*/", matching);
    }

    private static int getMarkerOffset(final String text, final String marker, final String matching) {
        int offset = text.indexOf(marker);
        if (offset == -1) {
            throw new RuntimeException("Cannot find marker " + marker);
        }
        offset = offset + marker.length();
        if (matching != null) {
            offset = text.indexOf(matching, offset);
            if (offset == -1) {
                throw new RuntimeException("Cannot find matching " + matching + " for marker " + marker);
            }
            offset += matching.length();
        }
        return offset;
    }

    public static int getMXMLMarkerOffset(final String text, final String id, final String matching) {
        return getMarkerOffset(text, "<!--##" + id + "##-->", matching);
    }

    public static File getResourceFile(final String path) {
        return new File("src/test/resources/" + path);
    }

    public static InputStream getResourceStream(final String path) throws IOException {
        return new FileInputStream(getResourceFile(path));
    }

    public static ASIdentifier searchIdentifier(final ASClassDef classDef, final String name) {
        return searchIdentifier(classDef.getIdentifiers(), name);
    }

    public static ASIdentifier searchIdentifier(final Collection<? extends ASElement> elements, final String name) {
        for (ASElement element : elements) {
            if (element instanceof ASIdentifier) {
                ASIdentifier identifier = (ASIdentifier) element;
                if (identifier.getName().equals(name)) {
                    return identifier;
                }
            }
        }
        return null;
    }

    public static Collection<EXFileResource> getAllResources(final EXLoader loader, final EXFileResource root) throws Exception {
        Collection<EXFileResource> resources = new ArrayList<EXFileResource>();
        for (EXFileResource resource : loader.getResources(root.getRelativePath() + "/*")) {
            if (resource.isDirectory()) {
                resources.addAll(getAllResources(loader, resource));
            } else if (resource.getExtension() != null && (resource.getExtension().equals("as") || resource.getExtension().equals("mxml"))) {
                resources.add(resource);
            }
        }
        return resources;
    }
}
