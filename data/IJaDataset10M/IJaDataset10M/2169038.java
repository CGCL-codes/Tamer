package com.narirelays.ems.struts2.actions.device;

import com.narirelays.ems.resources.StorageService;
import com.narirelays.ems.services.EquipmentManagementService;
import com.narirelays.ems.struts2.MyBaseAction;

public class EquipmentManagement extends MyBaseAction {

    public String queryAllEquipment() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.queryAllEquipment();
        }
        return SUCCESS;
    }

    public String queryEquipmentByDepartment() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.queryEquipmentByDepartment(request.getParameter("department_id"), parameterMap);
        }
        return SUCCESS;
    }

    public String addEquipment() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.addEquipment(request.getParameter("department_id"), parameterMap);
        }
        return SUCCESS;
    }

    public String modifyEquipment() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.modifyEquipment(request.getParameter("id"), parameterMap);
        }
        return SUCCESS;
    }

    public String deleteEquipment() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.deleteEquipment(request.getParameter("id"));
        }
        return SUCCESS;
    }

    public String queryMaintainRecordByEquipment() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.queryMaintainRecordByEquipment(request.getParameter("equipment_id"));
        }
        return SUCCESS;
    }

    public String addMaintainRecord() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.addMaintainRecord(request.getParameter("equipment_id"), parameterMap);
        }
        return SUCCESS;
    }

    public String modifyMaintainRecord() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.modifyMaintainRecord(request.getParameter("id"), parameterMap);
        }
        return SUCCESS;
    }

    public String deleteMaintainRecord() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.deleteMaintainRecord(request.getParameter("id"));
        }
        return SUCCESS;
    }

    public String queryAllFitting() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.queryAllFitting();
        }
        return SUCCESS;
    }

    public String addFitting() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.addFitting(parameterMap);
        }
        return SUCCESS;
    }

    public String modifyFitting() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.modifyFitting(request.getParameter("id"), parameterMap);
        }
        return SUCCESS;
    }

    public String deleteFitting() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.deleteFitting(request.getParameter("id"));
        }
        return SUCCESS;
    }

    public String queryDepreciationCaculatorByEquipment() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.queryDepreciationCaculatorByEquipment(request.getParameter("equipment_id"));
        }
        return SUCCESS;
    }

    public String addDepreciationCaculator() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.addDepreciationCaculator(request.getParameter("equipment_id"), request.getParameter("algorithm_id"), parameterMap);
        }
        return SUCCESS;
    }

    public String deleteDepreciationCaculator() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.deleteDepreciationCaculator(request.getParameter("equipment_id"));
        }
        return SUCCESS;
    }

    public String queryAllMeasureEquipment() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.queryAllMeasureEquipment(parameterMap);
        }
        return SUCCESS;
    }

    public String addMeasureEquipment() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.addMeasureEquipment(parameterMap);
        }
        return SUCCESS;
    }

    public String modifyMeasureEquipment() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.modifyMeasureEquipment(request.getParameter("id"), parameterMap);
        }
        return SUCCESS;
    }

    public String deleteMeasureEquipment() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.deleteMeasureEquipment(request.getParameter("id"));
        }
        return SUCCESS;
    }

    public String associateET() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.associateET(request.getParameter("equipment_id"), request.getParameter("fitting_id"));
        }
        return SUCCESS;
    }

    public String deleteAssociateET() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.deleteAssociateET(request.getParameter("equipment_id"), request.getParameter("fitting_id"));
        }
        return SUCCESS;
    }

    public String queryFittingByEquipment() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.queryFittingByEquipment(request.getParameter("equipment_id"));
        }
        return SUCCESS;
    }

    public String checkMeasureEquipment() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.checkMeasureEquipment(request.getParameter("measureEquipment_ids"), parameterMap);
        }
        return SUCCESS;
    }

    public String queryAllDepAlgorithm() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.queryAllDepAlgorithm();
        }
        return SUCCESS;
    }

    public String addDepAlgorithm() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.addDepAlgorithm(parameterMap);
        }
        return SUCCESS;
    }

    public String modifyDepAlgorithm() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.modifyDepAlgorithm(request.getParameter("id"), parameterMap);
        }
        return SUCCESS;
    }

    public String deleteDepAlgorithm() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.deleteDepAlgorithm(request.getParameter("id"));
        }
        return SUCCESS;
    }

    public String querySimilarMearsure() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.querySimilarMearsure(request.getParameter("queryKey"));
        }
        return SUCCESS;
    }

    public String queryEquipemntCalculator() {
        EquipmentManagementService equipmentManagementService = (EquipmentManagementService) StorageService.ctx.getBean("equipmentManagementService");
        if (equipmentManagementService != null) {
            resultInfo = equipmentManagementService.queryEquipemntCalculator(request.getParameter("equipment_id"));
        }
        return SUCCESS;
    }
}
