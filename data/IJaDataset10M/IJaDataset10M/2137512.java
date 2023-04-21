package net.sf.joafip.service;

import java.io.File;
import net.sf.joafip.TestConstant;
import net.sf.joafip.TestException;
import net.sf.joafip.entity.EnumFilePersistenceCloseAction;
import net.sf.joafip.file.service.FileIOException;
import net.sf.joafip.file.service.HelperFileUtil;
import net.sf.joafip.kvstore.service.HeapException;
import net.sf.joafip.logger.JoafipLogger;
import net.sf.joafip.store.service.objectfortest.Bob1;

/**
 * 
 * @author luc peuvrier
 * 
 */
public class AbstractCrash {

    protected final JoafipLogger logger = JoafipLogger.getLogger(getClass());

    private String globalFlagFilePath;

    private String dataFlagFilePath;

    private String dataFilePath;

    private String dataCopyFilePath;

    private static final boolean GARBAGE = false;

    private static final boolean CRASH_SAFE = true;

    private String winRamDiskRuntimeDir;

    protected void setupAndCreate() throws FilePersistenceException, FilePersistenceInvalidClassException, FilePersistenceNotSerializableException, FilePersistenceClassNotFoundException, FilePersistenceDataCorruptedException, FilePersistenceTooBigForSerializationException, TestException {
        winRamDiskRuntimeDir = TestConstant.getWinRamDiskRuntimeDir();
        globalFlagFilePath = winRamDiskRuntimeDir + "all.flag";
        dataFlagFilePath = winRamDiskRuntimeDir + "data.flag";
        dataFilePath = winRamDiskRuntimeDir + "store.data";
        dataCopyFilePath = winRamDiskRuntimeDir + "store_copy.data";
        final FilePersistenceBuilder builder = new FilePersistenceBuilder();
        builder.setPathName(winRamDiskRuntimeDir);
        builder.setProxyMode(true);
        builder.setRemoveFiles(true);
        builder.setGarbageManagement(GARBAGE);
        builder.setCrashSafeMode(CRASH_SAFE);
        final IFilePersistence filePersistence = builder.build();
        final IDataAccessSession dataAccessSession = filePersistence.createDataAccessSession();
        dataAccessSession.open();
        dataAccessSession.setObject("key", new Bob1());
        dataAccessSession.close(EnumFilePersistenceCloseAction.SAVE);
        filePersistence.close();
    }

    protected void assertContent() throws FilePersistenceException, FilePersistenceInvalidClassException, FilePersistenceNotSerializableException, FilePersistenceClassNotFoundException, FilePersistenceDataCorruptedException, FilePersistenceTooBigForSerializationException {
        final FilePersistenceBuilder builder = new FilePersistenceBuilder();
        builder.setPathName(winRamDiskRuntimeDir);
        builder.setProxyMode(true);
        builder.setRemoveFiles(false);
        builder.setGarbageManagement(GARBAGE);
        builder.setCrashSafeMode(CRASH_SAFE);
        final IFilePersistence filePersistence = builder.build();
        final IDataAccessSession dataAccessSession = filePersistence.createDataAccessSession();
        dataAccessSession.open();
        if (dataAccessSession.getObject("key") == null) {
            logger.error("must have an object");
        } else {
            logger.info("OK!");
        }
        dataAccessSession.close(EnumFilePersistenceCloseAction.SAVE);
        filePersistence.close();
    }

    protected void simulateCrashDataLost() throws HeapException, FileIOException {
        File file = new File(globalFlagFilePath);
        file.delete();
        file = new File(dataFlagFilePath);
        file.delete();
        final File destinationFile = new File(dataCopyFilePath);
        final File sourceFile = new File(dataFilePath);
        HelperFileUtil.getInstance().copyFile(sourceFile, destinationFile);
    }
}
