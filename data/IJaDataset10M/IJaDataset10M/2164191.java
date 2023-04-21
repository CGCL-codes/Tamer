package org.agile.dfs.name.service;

import junit.framework.Assert;
import org.agile.dfs.core.entity.BlockItem;
import org.agile.dfs.core.entity.DfsSchema;
import org.agile.dfs.name.BaseNameNodeTestCase;
import org.agile.dfs.util.ServiceFactory;

public class BlockServiceImplTest extends BaseNameNodeTestCase {

    private BlockService blockService = transactionFactory.findService(BlockServiceImpl.class, new String[] { "commit", "locate" });

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
        if (schemaService.exists(schema)) {
            schemaService.destroy(schema);
        }
    }

    public void testLocateCommit() {
        String file = "/home/" + new java.util.Random().nextLong() + "/some.jpg";
        fileService.delete(schema, file);
        String fileId = fileService.createNewFiles(schema, file);
        BlockItem block = blockService.locate(schema, fileId);
        Assert.assertNotNull(block);
        Assert.assertTrue(block.getBlockNo() == 1);
        blockService.commit(schema, fileId, block.getId());
        BlockItem block2 = blockService.locate(schema, fileId);
        Assert.assertNotNull(block2);
        Assert.assertTrue(block2.getBlockNo() == 2);
        fileService.delete(schema, file);
    }
}
