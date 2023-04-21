package com.sxi.override.digibanker.service.ovrd;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.dao.DataAccessException;
import com.sxi.cometd.pages.utils.OverrideEntryParams;
import com.sxi.override.digibanker.dao.ovrd.OverrideDao;
import com.sxi.override.digibanker.model.ovrd.OverrideBean;
import com.sxi.override.digibanker.model.ovrd.OverrideModel;

/**
 * @author Emmanuel Nollase - emanux created 2009 7 21 - 12:47:14
 */
public class OverrideServiceImpl implements OverrideService {

    private static final Log log = LogFactory.getLog(OverrideServiceImpl.class);

    private OverrideDao overrideDao;

    public void setOverrideDao(OverrideDao overrideDao) {
        this.overrideDao = overrideDao;
    }

    public List<OverrideBean> numberOfOverrides(final int numOvrd) {
        final List<OverrideBean> numOvrds = new ArrayList<OverrideBean>();
        for (int i = 0; i < numOvrd; i++) {
            numOvrds.add(new OverrideBean());
        }
        return numOvrds;
    }

    public boolean save(OverrideModel overrideModel, final OverrideEntryParams entryParams) {
        boolean _save = false;
        overrideModel.setFuncCd(entryParams.getFuncId());
        overrideModel.setRefNo(entryParams.getRefNo());
        overrideModel.setOvrdKey(entryParams.getOvrdkey());
        overrideModel.setRequestBy(entryParams.getSubmittedBy());
        overrideModel.setTxnDt(new Date());
        try {
            overrideDao.save(overrideModel);
            _save = true;
        } catch (DataAccessException e) {
            log.error("Error saving serialized object", e);
        }
        return _save;
    }

    public List<OverrideModel> findAll() {
        return overrideDao.findAll();
    }

    public List<OverrideModel> findOverrides(int first, int count) {
        return overrideDao.findOverrides(first, count);
    }

    public int findOverridesSize() {
        return overrideDao.findOverridesSize();
    }

    public OverrideModel findByRefNo(String refno) {
        return null;
    }

    public void update(OverrideModel model) {
        overrideDao.update(model);
    }
}
