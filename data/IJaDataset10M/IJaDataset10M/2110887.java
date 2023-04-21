package org.opennms.netmgt.dao.hibernate;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.opennms.netmgt.dao.AssetRecordDao;
import org.opennms.netmgt.model.OnmsAssetRecord;

public class AssetRecordDaoHibernate extends AbstractDaoHibernate<OnmsAssetRecord, Integer> implements AssetRecordDao {

    public AssetRecordDaoHibernate() {
        super(OnmsAssetRecord.class);
    }

    public OnmsAssetRecord findByNodeId(Integer id) {
        return (OnmsAssetRecord) findUnique("from OnmsAssetRecord rec where rec.nodeId = ?", id);
    }

    @SuppressWarnings("unchecked")
    public Map<String, Integer> findImportedAssetNumbersToNodeIds(String foreignSource) {
        List assetNumbers = getHibernateTemplate().find("select a.node.id, a.assetNumber from OnmsAssetRecord a where a.assetNumber like '" + foreignSource + "%'");
        Map<String, Integer> assetNumberMap = new HashMap<String, Integer>();
        for (Iterator it = assetNumbers.iterator(); it.hasNext(); ) {
            Object[] an = (Object[]) it.next();
            assetNumberMap.put((String) an[1], (Integer) an[0]);
        }
        return assetNumberMap;
    }
}
