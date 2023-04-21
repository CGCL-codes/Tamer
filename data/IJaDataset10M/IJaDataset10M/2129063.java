package net.sf.joafip.bugtree;

import net.sf.joafip.NotStorableClass;
import net.sf.joafip.StorableAccess;
import net.sf.joafip.entity.EnumFilePersistenceCloseAction;
import net.sf.joafip.redblacktree.service.RBTException;
import net.sf.joafip.service.FilePersistenceBuilder;
import net.sf.joafip.service.FilePersistenceClassNotFoundException;
import net.sf.joafip.service.FilePersistenceDataCorruptedException;
import net.sf.joafip.service.FilePersistenceException;
import net.sf.joafip.service.FilePersistenceInvalidClassException;
import net.sf.joafip.service.FilePersistenceNotSerializableException;
import net.sf.joafip.service.FilePersistenceTooBigForSerializationException;
import net.sf.joafip.service.IDataAccessSession;
import net.sf.joafip.service.IFilePersistence;

@NotStorableClass
@StorableAccess
public final class MainTreeHeapFileNoAutoSave extends AbstractBugTree {

    private IDataAccessSession session;

    private MainTreeHeapFileNoAutoSave() {
        super();
    }

    public static void main(final String[] args) {
        try {
            final MainTreeHeapFileNoAutoSave main = new MainTreeHeapFileNoAutoSave();
            main.run();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    private void run() throws FilePersistenceException, FilePersistenceInvalidClassException, FilePersistenceNotSerializableException, FilePersistenceClassNotFoundException, FilePersistenceDataCorruptedException, FilePersistenceTooBigForSerializationException, RBTException {
        final FilePersistenceBuilder builder = new FilePersistenceBuilder();
        builder.setPathName("runtime");
        builder.setFileCache(10 * 1024, 10 * 1024);
        builder.setProxyMode(true);
        builder.setRemoveFiles(true);
        builder.setCrashSafeMode(false);
        builder.setGarbageManagement(false);
        final IFilePersistence filePersistence = builder.build();
        session = filePersistence.createDataAccessSession();
        session.open();
        Tree<String> tree = new Tree<String>();
        session.setObject("key", tree);
        session.close(EnumFilePersistenceCloseAction.SAVE);
        session.open();
        tree = (Tree<String>) session.getObject("key");
        appendLoop(tree);
        session.close(EnumFilePersistenceCloseAction.SAVE);
        filePersistence.close();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Tree<String> treeAppend(final Tree<String> tree, final int count, final String element) throws RBTException {
        Tree<String> newTree = super.treeAppend(tree, count, element);
        if (count % 1000 == 1) {
            try {
                session.close(EnumFilePersistenceCloseAction.SAVE);
                session.open();
                newTree = (Tree<String>) session.getObject("key");
            } catch (Exception exception) {
                throw new RBTException(exception);
            }
        }
        return newTree;
    }
}
