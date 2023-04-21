package com.google.gxp.compiler.parser;

import com.google.common.base.Preconditions;
import com.google.gxp.compiler.alerts.AlertSink;
import com.google.gxp.compiler.alerts.InfoAlert;
import com.google.gxp.compiler.alerts.SourcePosition;
import com.google.gxp.compiler.fs.FileRef;
import com.google.gxp.compiler.fs.FileSystem;
import java.io.IOException;
import java.io.InputStream;

/**
 * An entity resolver implementation that can resolve entities based on an
 * abstract file system that interprets public ids that start with // as
 * file system paths relative to the build system root, and that
 * can resolve System IDs under http://gxp.googlecode.com/svn/trunk/resources/
 * to a resouce available from the class path.
 */
public class FileSystemEntityResolver implements SourceEntityResolver {

    /**
   * prefix for files resolved using the java classloader.
   */
    private static final String EXTERNAL_ENTITY_PREFIX = "http://gxp.googlecode.com/svn/trunk/resources/";

    /**
   * prefix for public ids that are resolved relative to the revision control or
   * project root directory
   */
    private static final String SOURCE_ROOT_PUBLIC_ID_PREFIX = "//";

    private final FileSystem fileSystem;

    public FileSystemEntityResolver(FileSystem fileSystem) {
        this.fileSystem = Preconditions.checkNotNull(fileSystem);
    }

    /**
   * This implementation only allows systemIds which are in a predetermined
   * set.
   *
   * @throws IOException if the protocol is recognized but the underlying file
   *     could not be retrieved.
   * @throws UnsupportedExternalEntityException if the protocol isn't recognized
   */
    public InputStream resolveEntity(SourcePosition pos, String publicId, String systemId, AlertSink alertSink) throws IOException {
        if (systemId.startsWith(EXTERNAL_ENTITY_PREFIX)) {
            InputStream stream = resolveEntityFromResource(pos, systemId, EXTERNAL_ENTITY_PREFIX.length(), alertSink);
            if (stream != null) {
                return stream;
            }
        } else if (publicId != null && publicId.startsWith(SOURCE_ROOT_PUBLIC_ID_PREFIX)) {
            String relPath = publicId.substring(SOURCE_ROOT_PUBLIC_ID_PREFIX.length());
            if (!"".equals(relPath) && !relPath.startsWith("/")) {
                FileRef file = fileSystem.getRoot().join(relPath);
                alertSink.add(new EntityResolvedNotification(pos, publicId, file.toFilename()));
                return file.openInputStream();
            }
        }
        throw unresolved(pos, publicId, systemId, null);
    }

    private InputStream resolveEntityFromResource(SourcePosition pos, String systemId, int prefixLength, AlertSink alertSink) throws IOException {
        Class<?> cls = getClass();
        String resourceName = "/" + cls.getPackage().getName().replace('.', '/') + "/" + systemId.substring(prefixLength);
        alertSink.add(new EntityResolvedNotification(pos, systemId, resourceName));
        return cls.getResourceAsStream(resourceName);
    }

    private static RuntimeException unresolved(SourcePosition pos, String publicId, String systemId, Throwable cause) {
        UnsupportedExternalEntityException error = new UnsupportedExternalEntityException(pos, publicId, systemId);
        if (cause != null) {
            error.initCause(cause);
        }
        return error;
    }

    /**
   * a notication that an external entity has been resolved to a resource or
   * file.
   */
    public static class EntityResolvedNotification extends InfoAlert {

        private EntityResolvedNotification(SourcePosition entityRefPos, String id, String realPath) {
            super(entityRefPos, "Resolved entity `" + id + "` to `" + realPath + "`");
        }
    }
}
