package ru.amse.jsynchro.kernel.task;

import java.io.File;
import java.io.IOException;
import junit.framework.TestCase;
import ru.amse.jsynchro.fileSystem.FileSystem;
import ru.amse.jsynchro.fileSystem.LocalFileSystem;
import ru.amse.jsynchro.kernel.RandomFolder;

public class HistoryTest extends TestCase {

    private final String localTestFolder = "./test_dir/dir1";

    private final String serverTestFolder = "./test_dir/server";

    private final FileSystem fs = new LocalFileSystem();

    public void testNoChanges() {
        int comparesNumber = 5;
        try {
            HistoryTask task = new HistoryTask();
            task.analyzeLocal(localTestFolder, fs);
            for (int i = 0; i < comparesNumber; i++) {
                assertFalse(task.analyzeLocal(localTestFolder, fs));
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testWithChanges() {
        int comparesNumber = 5;
        try {
            HistoryTask task = new HistoryTask();
            task.analyzeLocal(localTestFolder, fs);
            for (int i = 0; i < comparesNumber; i++) {
                if (!RandomFolder.changeDir(new File(localTestFolder), 1.0)) {
                    fail();
                }
                assertTrue(task.analyzeLocal(localTestFolder, fs));
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }

    public void testAll() {
        int comparesNumber = 2;
        try {
            HistoryTask task = new HistoryTask();
            for (int i = 0; i < comparesNumber; i++) {
                task.analyzeLocal(localTestFolder, fs);
                if (!RandomFolder.changeDir(new File(localTestFolder), 1.0)) {
                    fail();
                }
                if (!RandomFolder.changeDir(new File(serverTestFolder), 1.0)) {
                    fail();
                }
                task.analyzeLocal(localTestFolder, fs);
                task.update();
                task.commit();
                Task undir = Task.createTask(localTestFolder, fs, serverTestFolder, fs, true, new TaskType(TaskType.UNDIRECTED));
                assertFalse(undir.analyze());
            }
        } catch (IOException e) {
            e.printStackTrace();
            fail();
        }
    }
}
