package org.slasoi.businessManager.common.service.impl;

import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;
import org.slasoi.businessManager.common.dao.PriceVariationTypeDAO;
import org.slasoi.businessManager.common.model.EmPriceVariationType;
import org.slasoi.businessManager.common.service.PriceVariationTypeManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "priceVariationTypeService")
public class PriceVariationTypeManagerImpl implements PriceVariationTypeManager {

    static Logger log = Logger.getLogger(PriceVariationTypeManagerImpl.class);

    @Autowired
    private PriceVariationTypeDAO PriceVariationTypesDAO;

    @Transactional(propagation = Propagation.REQUIRED)
    public List<EmPriceVariationType> getPriceVariationTypes() {
        log.info("getPriceVariationTypes()");
        return PriceVariationTypesDAO.getList();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public EmPriceVariationType getPriceVariationTypeById(Long id) {
        log.info("getCharacteristicById():Id:" + id);
        return PriceVariationTypesDAO.load(id);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public void savePriceVariationType(EmPriceVariationType object) {
        if (object.getNuIdVariationType() == 0) object.setNuIdVariationType(null);
        log.info("saveCharacteristic()");
        PriceVariationTypesDAO.saveOrUpdate(object);
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.REQUIRED)
    public void deletePriceVariationType(List<Long> ids) {
        log.info("deletePriceVariationTypes()");
        Iterator<Long> itIds = ids.iterator();
        while (itIds.hasNext()) {
            EmPriceVariationType cht = PriceVariationTypesDAO.load(itIds.next());
            PriceVariationTypesDAO.delete(cht);
        }
    }
}
