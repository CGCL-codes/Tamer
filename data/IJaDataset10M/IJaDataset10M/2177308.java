package org.kaleidofoundry.core.store;

import static org.kaleidofoundry.core.store.FileStoreConstants.ClasspathStorePluginName;
import java.io.InputStream;
import java.net.URI;
import org.kaleidofoundry.core.context.IllegalContextParameterException;
import org.kaleidofoundry.core.context.RuntimeContext;
import org.kaleidofoundry.core.lang.annotation.Immutable;
import org.kaleidofoundry.core.lang.annotation.NotNull;
import org.kaleidofoundry.core.plugin.Declare;
import org.kaleidofoundry.core.system.JavaSystemHelper;

/**
 * Classpath file store implementation<br/>
 * <br/>
 * <b>This implementation is only for read only use</b> - the methods store, remove, move will throws {@link ResourceException}<br/>
 * 
 * @author Jerome RADUGET
 * @see FileStoreContextBuilder enum of context configuration properties available
 */
@Immutable
@Declare(ClasspathStorePluginName)
public class ClasspathFileStore extends AbstractFileStore implements FileStore {

    /**
    * @param context
    */
    public ClasspathFileStore(@NotNull final RuntimeContext<FileStore> context) {
        super(context);
    }

    /**
    * @param baseUri
    * @param context
    */
    public ClasspathFileStore(final String baseUri, final RuntimeContext<FileStore> context) {
        super(baseUri, context);
    }

    /**
    * @see AbstractFileStore#AbstractFileStore()
    */
    ClasspathFileStore() {
        super();
    }

    /**
    * @return class classLoader to use
    */
    @NotNull
    protected ClassLoader getClassLoader() {
        final ClassLoader classLoader;
        final String strClass = context.getString(FileStoreContextBuilder.Classloader);
        if (strClass == null) {
            classLoader = Thread.currentThread().getContextClassLoader();
        } else {
            try {
                classLoader = Class.forName(strClass).getClassLoader();
            } catch (final ClassNotFoundException cnfe) {
                throw new IllegalContextParameterException(FileStoreContextBuilder.Classloader, strClass, context, cnfe);
            }
        }
        return classLoader;
    }

    @Override
    public FileStoreType[] getStoreType() {
        return new FileStoreType[] { FileStoreTypeEnum.classpath };
    }

    @Override
    public boolean isReadOnly() {
        return true;
    }

    @Override
    protected ResourceHandler doGet(final URI resourceBinding) throws ResourceNotFoundException, ResourceException {
        final StringBuilder localPath = new StringBuilder();
        if (resourceBinding.getHost() != null) {
            localPath.append(resourceBinding.getHost()).append("/");
        }
        localPath.append(resourceBinding.getPath());
        if (localPath.charAt(0) == '/') {
            final InputStream in = JavaSystemHelper.getResourceAsStream(getClassLoader(), localPath.substring(1));
            if (in != null) {
                return createResourceHandler(resourceBinding.toString(), in);
            } else {
                throw new ResourceNotFoundException(resourceBinding.toString());
            }
        } else {
            final InputStream in = JavaSystemHelper.getResourceAsStream(getClassLoader(), localPath.toString());
            if (in != null) {
                return createResourceHandler(resourceBinding.toString(), in);
            } else {
                throw new ResourceNotFoundException(resourceBinding.toString());
            }
        }
    }

    @Override
    protected void doRemove(final URI resourceUri) throws ResourceNotFoundException, ResourceException {
        throw new ResourceException("store.readonly.illegal", context.getName());
    }

    @Override
    protected void doStore(final URI resourceUri, final ResourceHandler resource) throws ResourceException {
        throw new ResourceException("store.readonly.illegal", context.getName());
    }
}
