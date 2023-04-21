package net.sf.joafip.performance.items.service;

import java.io.File;
import net.sf.joafip.NotStorableClass;
import net.sf.joafip.StorableAccess;
import net.sf.joafip.logger.JoafipLogger;
import net.sf.joafip.service.FilePersistenceClassNotFoundException;
import net.sf.joafip.service.FilePersistenceDataCorruptedException;
import net.sf.joafip.service.FilePersistenceException;
import net.sf.joafip.service.FilePersistenceInvalidClassException;
import net.sf.joafip.service.FilePersistenceNotSerializableException;
import net.sf.joafip.service.FilePersistenceTooBigForSerializationException;
import net.sf.joafip.store.service.StoreClassNotFoundException;

/**
 * 
 * @author luc peuvrier
 * 
 */
@SuppressWarnings("PMD")
@NotStorableClass
@StorableAccess
public class InserterFewInsertAndExport extends AbstractInserter {

    private static final JoafipLogger LOGGER = JoafipLogger.getLogger(InserterFewInsertAndExport.class);

    public InserterFewInsertAndExport(final String pathName) throws FilePersistenceException, FilePersistenceInvalidClassException, FilePersistenceNotSerializableException, FilePersistenceClassNotFoundException, FilePersistenceDataCorruptedException, StoreClassNotFoundException, FilePersistenceTooBigForSerializationException {
        super(pathName);
    }

    public static void main(final String[] args) {
        final File dataDirectory = new File("runtime");
        for (final File file : dataDirectory.listFiles()) {
            file.delete();
        }
        dataDirectory.mkdirs();
        InserterFewInsertAndExport inserter;
        try {
            inserter = new InserterFewInsertAndExport("runtime");
            inserter.run(1);
            inserter.export("runtime");
            inserter.close();
        } catch (final Throwable throwable) {
            LOGGER.fatal("error", throwable);
        }
    }
}
