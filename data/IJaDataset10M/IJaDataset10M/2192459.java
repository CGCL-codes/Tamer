package com.wozgonon.eventstore;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import junit.framework.TestCase;
import com.wozgonon.WozgononConstants;
import com.wozgonon.time.TimeSeriesType;

public class EventStoreManagerTest extends TestCase {

    public void setUp() throws IOException {
    }

    public void tearDown() {
    }

    /**
	 * This checks that the save and load functions work together without throwing exceptions.
	 * They also check that a file is actually created and is of the expected length.
	 * 
	 * FIXME A further, more comprehensive, test might be to take the hash code of the database content before and after restore.
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException 
	 */
    public void testSaveAndLoad() throws FileNotFoundException, IOException, ClassNotFoundException {
        final UseCaseManager manager = new UseCaseManager();
        final File test = File.createTempFile(WozgononConstants.WOZGONON, "test");
        try {
            final String before = manager.getSaveLoad().getName();
            manager.getSaveLoad().save(test);
            assert test.exists();
            assert test.length() > 1000;
            manager.getSaveLoad().load(test);
            assert before.equals(test.getName());
        } finally {
            if (test != null) {
                test.delete();
            }
        }
    }

    /**
	 * Checks that the database can be written as XML
	 * 
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
    public void testSaveXML() throws FileNotFoundException, IOException, ClassNotFoundException {
        final UseCaseManager manager = new UseCaseManager();
        final File test = File.createTempFile(WozgononConstants.WOZGONON, "test");
        try {
            final String before = manager.getSaveLoad().getName();
            manager.getSaveLoad().saveXML(test);
            assert test.length() > 1000;
        } finally {
            if (test != null) {
                test.delete();
            }
        }
    }

    public void testChangesSince() {
        final UseCaseManager manager = new UseCaseManager();
        final BaseData database = manager.getExtracts().getChangesSince(TimeSeriesType.TODAY, 10);
        assert database.numberOfCounters() > 10 : "FIXME nonsense condition - come up with some means of comparing";
    }
}
