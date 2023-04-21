package org.agile.dfs.name.service;

import java.util.List;
import org.agile.dfs.core.entity.BlockItem;
import org.agile.dfs.core.entity.DfsSchema;
import org.agile.dfs.name.manager.BlockItemManager;
import org.agile.dfs.name.manager.SchemaManager;
import org.agile.dfs.util.ServiceFactory;

public class BlockServiceImpl implements BlockService {

    private BlockItemManager blockManager = ServiceFactory.findService(BlockItemManager.class);

    private SchemaManager schemaManager = ServiceFactory.findService(SchemaManager.class);

    public void commit(String schema, String fileId, String blockId) {
        DfsSchema ds = schemaManager.findByName(schema);
        BlockItem block = blockManager.findById(ds, blockId);
        block.setStatus(BlockItem.STATUS_NORMAL);
        blockManager.update(ds, block);
    }

    public BlockItem locate(String schema, String fileId) {
        DfsSchema ds = schemaManager.findByName(schema);
        List<BlockItem> items = blockManager.findByFileId(ds, fileId);
        if (items == null || items.size() == 0) {
            return createNewBlock(ds, fileId, 0);
        } else {
            BlockItem block = items.get(items.size() - 1);
            if (block.free() == 0 || BlockItem.STATUS_NORMAL.equals(block.getStatus())) {
                block = createNewBlock(ds, fileId, block.getBlockNo());
            }
            return block;
        }
    }

    private BlockItem createNewBlock(DfsSchema schema, String fileId, int maxBlockNo) {
        BlockItem block = new BlockItem();
        block.setNodeId("nodeId");
        block.setFileId(fileId);
        block.setBlockNo(maxBlockNo + 1);
        block.setCapacity(64 * 1024 * 1024);
        block.setSize(0);
        block.setStatus(BlockItem.STATUS_READY);
        blockManager.create(schema, block);
        return block;
    }
}
