package net.sf.joafip.hugelist;

import net.sf.joafip.TestConstant;
import net.sf.joafip.TestException;
import net.sf.joafip.service.FilePersistenceBuilder;
import net.sf.joafip.service.FilePersistenceClassNotFoundException;
import net.sf.joafip.service.FilePersistenceDataCorruptedException;
import net.sf.joafip.service.FilePersistenceException;
import net.sf.joafip.service.FilePersistenceInvalidClassException;
import net.sf.joafip.service.FilePersistenceNotSerializableException;
import net.sf.joafip.service.IFilePersistence;

/**
 * common to all "huge list" tests
 * 
 * @author luc peuvrier
 * 
 */
public class AbstractHugeList {

    protected String runtimeDirPath;

    private static final int PAGE_SIZE = 4 * 1024;

    protected static final int NUMBER_OF_PAGE = 4 * 1024;

    private static final boolean CRASH_SAFE_MODE = false;

    private static final boolean GARBAGE = false;

    protected IFilePersistence filePersistence;

    public AbstractHugeList() throws FilePersistenceException, FilePersistenceInvalidClassException, FilePersistenceNotSerializableException, FilePersistenceClassNotFoundException, FilePersistenceDataCorruptedException, TestException {
        super();
        runtimeDirPath = TestConstant.getWinRamDiskRuntimeDir();
        final FilePersistenceBuilder builder = new FilePersistenceBuilder();
        builder.setPathName(runtimeDirPath);
        builder.setFileCache(PAGE_SIZE, NUMBER_OF_PAGE);
        builder.setProxyMode(true);
        builder.setRemoveFiles(false);
        builder.setGarbageManagement(GARBAGE);
        builder.setCrashSafeMode(CRASH_SAFE_MODE);
        filePersistence = builder.build();
    }
}
