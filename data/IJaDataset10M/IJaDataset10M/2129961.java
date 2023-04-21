package com.jeecms.cms.manager.impl;

import java.io.Serializable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.jeecms.common.hibernate3.Updater;
import com.jeecms.core.JeeCoreManagerImpl;
import com.jeecms.cms.dao.ChnlModelItemDao;
import com.jeecms.cms.entity.ChnlModelItem;
import com.jeecms.cms.manager.ChnlModelItemMng;

@Service
@Transactional
public class ChnlModelItemMngImpl extends JeeCoreManagerImpl<ChnlModelItem> implements ChnlModelItemMng {

    @Override
    public Object updateByUpdater(Updater updater) {
        ChnlModelItem item = (ChnlModelItem) super.updateByUpdater(updater);
        return item;
    }

    @Override
    public ChnlModelItem save(ChnlModelItem item) {
        super.save(item);
        return item;
    }

    @Override
    public ChnlModelItem findById(Serializable id) {
        ChnlModelItem item = super.findById(id);
        return item;
    }

    @Override
    public ChnlModelItem deleteById(Serializable id) {
        ChnlModelItem item = super.deleteById(id);
        return item;
    }

    @Autowired
    public void setDao(ChnlModelItemDao dao) {
        super.setDao(dao);
    }

    public ChnlModelItemDao getDao() {
        return (ChnlModelItemDao) super.getDao();
    }
}
