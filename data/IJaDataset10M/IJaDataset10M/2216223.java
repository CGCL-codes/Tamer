package org.agile.dfs.name.service;

import junit.framework.Assert;
import org.agile.dfs.core.entity.DfsSchema;
import org.agile.dfs.name.BaseNameNodeTestCase;
import org.agile.dfs.util.ServiceFactory;

public class FileServiceImplTest extends BaseNameNodeTestCase {

    private static FileService fileService = transactionFactory.findService(FileServiceImpl.class, new String[] { "create*", "mk*", "delete*" });

    private static SchemaService schemaService = ServiceFactory.findService(SchemaServiceImpl.class);

    private String schema = "phoenix";

    protected void setUp() throws Exception {
        super.setUp();
        schemaService.destroy(schema);
        if (!schemaService.exists(schema)) {
            schemaService.build(new DfsSchema(schema, "http://www.agile.com/dfs/name"));
        }
    }

    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testCreateNewFile() {
        String file = "/home/testCreateNewFile";
        fileService.delete(schema, file);
        Assert.assertTrue(!fileService.exists(schema, file));
        fileService.mkdirs(schema, "/home");
        fileService.createNewFile(schema, file);
        Assert.assertTrue(fileService.exists(schema, file));
    }

    public void testCreateNewFiles() {
        String file = "/home/" + new java.util.Random().nextLong() + "/testCreateNewFile";
        fileService.delete(schema, file);
        Assert.assertTrue(!fileService.exists(schema, file));
        fileService.createNewFiles(schema, file);
        Assert.assertTrue(fileService.exists(schema, file));
    }

    public void testMkdir() {
        String dir = "/home/" + new java.util.Random().nextLong() + "dir";
        fileService.delete(schema, dir);
        Assert.assertTrue(!fileService.exists(schema, dir));
        fileService.mkdir(schema, "/home");
        fileService.mkdir(schema, dir);
        Assert.assertTrue(fileService.exists(schema, dir));
    }

    public void testMkdirs() {
        String dir = "/home/" + new java.util.Random().nextLong() + "/dir";
        fileService.delete(schema, dir);
        Assert.assertTrue(!fileService.exists(schema, dir));
        fileService.mkdirs(schema, dir);
        Assert.assertTrue(fileService.exists(schema, dir));
    }

    public void testDelete() {
        String dir1 = "/home/" + new java.util.Random().nextLong() + "/dir";
        fileService.delete(schema, dir1);
        Assert.assertTrue(!fileService.exists(schema, dir1));
        fileService.mkdirs(schema, dir1);
        String dir2 = "/home/" + new java.util.Random().nextLong() + "/dir";
        fileService.delete(schema, dir2);
        Assert.assertTrue(!fileService.exists(schema, dir2));
        fileService.mkdirs(schema, dir2);
        fileService.delete(schema, "/home");
        Assert.assertTrue(!fileService.exists(schema, dir1));
        Assert.assertTrue(!fileService.exists(schema, dir2));
    }
}
