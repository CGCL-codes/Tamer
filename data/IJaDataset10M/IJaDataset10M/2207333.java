package com.oneandone.sushi.fs.zip;

import com.oneandone.sushi.fs.Features;
import com.oneandone.sushi.fs.Filesystem;
import com.oneandone.sushi.fs.Node;
import com.oneandone.sushi.fs.NodeInstantiationException;
import com.oneandone.sushi.fs.World;
import com.oneandone.sushi.fs.file.FileNode;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.zip.ZipFile;

public class ZipFilesystem extends Filesystem {

    private static final String ZIP_SEPARATOR = "!/";

    public ZipFilesystem(World world, String name) {
        super(world, '/', new Features(false, false, false, false, false, false), name);
    }

    public ZipNode node(URI uri, Object extra) throws NodeInstantiationException {
        String schemeSpecific;
        String path;
        Node jar;
        if (extra != null) {
            throw new NodeInstantiationException(uri, "unexpected extra argument: " + extra);
        }
        checkOpaque(uri);
        schemeSpecific = uri.getRawSchemeSpecificPart();
        path = after(schemeSpecific, ZIP_SEPARATOR);
        if (path == null) {
            throw new NodeInstantiationException(uri, "missing '" + ZIP_SEPARATOR + "'");
        }
        if (path.endsWith(getSeparator())) {
            throw new NodeInstantiationException(uri, "invalid tailing " + getSeparator());
        }
        if (path.startsWith(getSeparator())) {
            throw new NodeInstantiationException(uri, "invalid heading " + getSeparator());
        }
        try {
            jar = getWorld().node(schemeSpecific.substring(0, schemeSpecific.length() - path.length() - ZIP_SEPARATOR.length()));
        } catch (URISyntaxException e) {
            throw new NodeInstantiationException(uri, "invalid jar file in jar url", e);
        }
        if (!(jar instanceof FileNode)) {
            throw new NodeInstantiationException(uri, "file node expected, got: " + jar.getURI());
        }
        try {
            return root((FileNode) jar).node(path, null);
        } catch (IOException e) {
            throw new NodeInstantiationException(uri, "world exception", e);
        }
    }

    public ZipRoot root(FileNode jar) throws IOException {
        return new ZipRoot(this, new ZipFile(jar.getAbsolute()));
    }

    public ZipNode node(FileNode jar, String path) throws IOException {
        return root(jar).node(path, null);
    }
}
