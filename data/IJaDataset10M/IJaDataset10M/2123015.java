package tuxiazi.dao.dbpartitionhelper;

import halo.dao.partition.DbPartitionHelper;
import halo.dao.query.PartitionTableInfo;
import java.util.Map;

public class Tuxiazi_FeedDbPartitionHelper extends DbPartitionHelper {

    private String dsKey = "mysql_ds_tuxiazi_feed";

    @Override
    public PartitionTableInfo parse(String name, Map<String, Object> ctxMap) {
        PartitionTableInfo partitionTableInfo = new PartitionTableInfo();
        partitionTableInfo.setDsKey(dsKey);
        partitionTableInfo.setTableName(name);
        partitionTableInfo.setAliasName(name);
        return partitionTableInfo;
    }
}
