package edu.vt.eng.swat.workflow.db.base.dao;

import java.util.List;
import edu.vt.eng.swat.workflow.db.base.entity.EquipType;

public interface EquipTypeDao {

    public EquipType findById(Long id);

    public List<EquipType> getAll();
}
