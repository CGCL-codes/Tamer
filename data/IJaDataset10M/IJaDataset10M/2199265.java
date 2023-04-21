package com.fbmc.ms.service;

import com.fbmc.ms.dao.RecordDAO;
import com.fbmc.ms.to.RecordSearchCriteria;
import com.fbmc.ms.to.RecordTO;
import com.ncs.iframe.base.pfw.dao.MapClassSupportDAO;
import com.ncs.iframe.commons.util.pagination.ListAndPagingInfo;
import org.springframework.dao.DuplicateKeyException;
import java.sql.Timestamp;
import java.util.Date;

public class RecordServiceImpl implements RecordService {

    private RecordDAO recordDAO;

    private MapClassSupportDAO mapClassSupportDAO;

    public RecordDAO getRecordDAO() {
        return recordDAO;
    }

    public void setRecordDAO(RecordDAO recordDAO) {
        this.recordDAO = recordDAO;
    }

    public MapClassSupportDAO getMapClassSupportDAO() {
        return mapClassSupportDAO;
    }

    public void setMapClassSupportDAO(MapClassSupportDAO mapClassSupportDAO) {
        this.mapClassSupportDAO = mapClassSupportDAO;
    }

    public RecordTO add(RecordTO to) throws DuplicateKeyException {
        int inserted = recordDAO.insert(to);
        if (inserted < 1) {
            throw new RuntimeException("Record was not added.");
        }
        return to;
    }

    public void delete(RecordTO record) {
        mapClassSupportDAO.remove(record);
    }

    public RecordTO search(RecordTO record) {
        return (RecordTO) mapClassSupportDAO.findByPrimaryKey(record);
    }

    public RecordTO update(RecordTO record) {
        record.setUpdateTs(new Timestamp(new Date().getTime()));
        int updated = mapClassSupportDAO.update(record);
        if (updated < 1) {
            throw new RuntimeException("Record was not updated.");
        }
        return record;
    }
}
