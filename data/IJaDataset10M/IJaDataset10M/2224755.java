package org.kaleidofoundry.core.store;

import static org.kaleidofoundry.core.store.GuiceFileStoreConstants.FileSystemStorePluginName;
import org.kaleidofoundry.core.context.RuntimeContext;
import org.kaleidofoundry.core.plugin.Declare;
import com.google.inject.Inject;

/**
 * @author Jerome RADUGET
 */
@Declare(FileSystemStorePluginName)
public class GuiceFileSystemStore extends FileSystemStore {

    /**
    * @param context
    */
    @Inject
    public GuiceFileSystemStore(final RuntimeContext<FileStore> context) {
        super(context);
    }
}
