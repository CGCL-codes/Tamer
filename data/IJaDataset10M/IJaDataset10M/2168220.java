package com.yuksekisler.application;

import java.util.List;
import com.yuksekisler.domain.equipment.Equipment;
import com.yuksekisler.domain.equipment.InspectionReport;
import com.yuksekisler.domain.work.LifeTime;

public interface EquipmentService extends CrudService {

    InspectionReport saveInspectionReport(Long reportEquipmentId, InspectionReport report, String uploadedUUID);

    List<Equipment> getAvailableEquipments(LifeTime lifetime, Long categoryID, Long workID);

    void removeEntity(Long id, Class<Equipment> clazz);

    Equipment findByInspectionReport(InspectionReport inspectionReport);
}
