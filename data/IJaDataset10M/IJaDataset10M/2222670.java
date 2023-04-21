package com.datas.dao.system;

import java.util.List;
import com.datas.bean.model.system.SysModulHits;
import com.datas.bean.model.system.SysModulHitsExtended;
import com.datas.dao.GenericDAO;

public interface SysModulHitsDAO extends GenericDAO<SysModulHits> {

    List<SysModulHitsExtended> selectExtended(SysModulHits key);
}
