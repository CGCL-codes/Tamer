package org.fao.waicent.kids.giews.dao;

import com.ibatis.dao.client.Dao;

public interface MiscellaneousDAO extends Dao {

    public int getDatasetCatalogID(int dataset_id);
}
