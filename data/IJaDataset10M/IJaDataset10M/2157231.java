package junit.jbjf.tasks;

import java.io.File;
import java.util.HashMap;
import junit.framework.TestCase;
import org.jbjf.core.ITask;
import org.jbjf.junit.JBJFTaskHarness;
import org.jbjf.tasks.BackupDirectory;
import org.jbjf.xml.JBJFBatchDefinition;
import org.jbjf.xml.XMLFileParser;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * <p>
 * The <code>TestBackupDirectory</code> class will unit test the
 * org.jbjf.tasks.BackupDirectory task.
 * </p>
 * <p>
 * @author  Adym S. Lincoln<br>
 * Copyright (C) 2007. JBJF All rights reserved.
 * @version 1.0.0
 * @since   1.0.0
 * </p>
 * <p>
 * @see 
 * </p>
 */
public class TestBackupDirectory extends JBJFTaskHarness {

    public ITask testTask = new org.jbjf.tasks.BackupDirectory();

    private String xmlBatchDefinition = "./etc/test-task-backup-directory.xml";

    private HashMap taskResources;

    /**
     * Default constructor.
     */
    public TestBackupDirectory() {
        super("./etc/test-task-backup-directory.xml");
        setJBJFDefinitionFile("./etc/test-task-backup-directory.xml");
        setJobName("test-backup-directory");
    }

    public void testTask1() {
        try {
            runBatch();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (new File("C:\\My Data\\backups\\backup.zip").exists() && new File("C:\\My Data\\backups\\backup-01-03-2011.zip").exists()) {
            TestCase.assertTrue(true);
        } else {
            TestCase.fail();
        }
    }

    public void testTask2() {
        try {
            BackupDirectory ltestTask = new BackupDirectory();
            this.taskResources = new HashMap();
            this.taskResources.put("source-directory", "C:\\My Data\\projects\\eam\\docs\\process-specifications");
            this.taskResources.put("backup-directory", "C:\\My Data\\backups");
            this.taskResources.put("backup-filename", "testTask2");
            ltestTask.setResources(this.taskResources);
            ltestTask.setBackupDirectory("C:\\My Data\\backups");
            ltestTask.setBackupFilename("testTask2");
            ltestTask.setSourceDirectory("C:\\My Data\\projects\\eam\\docs\\process-specifications");
            ltestTask.setDateStamp(null);
            ltestTask.setLog(getLog());
            ltestTask.runTask(getJobStack());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (new File("C:\\My Data\\backups\\testTask2.zip").exists()) {
            TestCase.assertTrue(true);
        } else {
            TestCase.fail();
        }
    }

    public void testTask3() {
        try {
            BackupDirectory ltestTask = new BackupDirectory();
            this.taskResources = new HashMap();
            this.taskResources.put("source-directory", "C:\\My Data\\projects\\eam\\docs\\process-specifications");
            this.taskResources.put("backup-directory", "C:\\My Data\\backups");
            this.taskResources.put("backup-filename", "testTask3");
            this.taskResources.put("datestamp", "MM-dd-yyyy");
            ltestTask.setResources(this.taskResources);
            ltestTask.setBackupDirectory("C:\\My Data\\backups");
            ltestTask.setBackupFilename("testTask3");
            ltestTask.setSourceDirectory("C:\\My Data\\projects\\eam\\docs\\process-specifications");
            ltestTask.setDateStamp("MM-dd-yyyy");
            ltestTask.setLog(getLog());
            ltestTask.runTask(getJobStack());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (new File("C:\\My Data\\backups\\testTask3-01-03-2011.zip").exists()) {
            TestCase.assertTrue(true);
        } else {
            TestCase.fail();
        }
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
}
