package net.sf.joafip.hugemap;

import java.util.Map;
import net.sf.joafip.entity.EnumFilePersistenceCloseAction;
import net.sf.joafip.service.FilePersistenceClassNotFoundException;
import net.sf.joafip.service.FilePersistenceDataCorruptedException;
import net.sf.joafip.service.FilePersistenceException;
import net.sf.joafip.service.FilePersistenceInvalidClassException;
import net.sf.joafip.service.FilePersistenceNotSerializableException;
import net.sf.joafip.service.FilePersistenceTooBigForSerializationException;
import net.sf.joafip.service.IDataAccessSession;
import net.sf.joafip.service.IFilePersistence;
import net.sf.joafip.store.service.StoreClassNotFoundException;

/**
 * get an element on the persisted map created by {@link HugeMapPopulate}<br>
 * This is to show that all the map is not loaded in memory<br>
 * 
 * @author luc peuvrier
 * 
 */
public final class HugeMapGetElement extends AbstractPersistence {

    public static void main(final String[] args) {
        final HugeMapGetElement hugeMapGetElement = new HugeMapGetElement();
        try {
            hugeMapGetElement.get();
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    private HugeMapGetElement() {
        super();
    }

    private void get() throws FilePersistenceException, FilePersistenceInvalidClassException, FilePersistenceNotSerializableException, FilePersistenceClassNotFoundException, FilePersistenceDataCorruptedException, StoreClassNotFoundException, FilePersistenceTooBigForSerializationException {
        final IFilePersistence filePersistence = createFilePersistence(false);
        final IDataAccessSession session = filePersistence.createDataAccessSession();
        final long startTime = System.currentTimeMillis();
        session.open();
        Map<Integer, String> map = getMap(session);
        final String value = map.get(50000);
        final long duration = System.currentTimeMillis() - startTime;
        final int numberOfObjectStateBeforeSave = filePersistence.getNumberOfObjectState();
        session.closeAndWait(EnumFilePersistenceCloseAction.SAVE);
        final int modified = filePersistence.getNumberOfModified();
        final int visited = filePersistence.getNumberOfVisited();
        System.out.println(value + " " + duration + " mS elapsed, " + numberOfObjectStateBeforeSave + " object loaded\n" + visited + " visited, " + modified + " modified");
        map = null;
        filePersistence.close();
    }

    @SuppressWarnings("unchecked")
    private Map<Integer, String> getMap(final IDataAccessSession session) throws FilePersistenceException {
        return (Map<Integer, String>) session.getObject("map");
    }
}
