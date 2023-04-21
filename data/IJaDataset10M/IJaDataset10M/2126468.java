package com.narirelays.ems.struts2.actions.basicinfo;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import net.sf.json.JSONArray;
import org.apache.struts2.ServletActionContext;
import com.narirelays.ems.applogic.OperResult;
import com.narirelays.ems.resources.StorageService;
import com.narirelays.ems.services.DepartmentManagementService;
import com.narirelays.ems.services.MeasureTableManagementService;
import com.narirelays.ems.services.ProductionProcessManagementService;
import com.narirelays.ems.struts2.MyBaseAction;
import com.opensymphony.xwork2.ActionSupport;

public class ProcessManagement extends MyBaseAction {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    public String queryProcessOutput() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.queryProcessOutput(request.getParameter("process_id"));
        }
        return SUCCESS;
    }

    public String queryProcessInput() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.queryProcessInput(request.getParameter("process_id"));
        }
        return SUCCESS;
    }

    public String deleteProcessInput() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.deleteProcessInput(request.getParameter("process_id"), request.getParameter("measure_id"));
        }
        return SUCCESS;
    }

    public String addProcessInput() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.addProcessInput(request.getParameter("process_id"), request.getParameter("measure_id"));
        }
        return SUCCESS;
    }

    public String deleteProcessOutput() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.deleteProcessOutput(request.getParameter("process_id"), request.getParameter("measure_id"));
        }
        return SUCCESS;
    }

    public String addProcessOutput() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.addProcessOutput(request.getParameter("process_id"), request.getParameter("measure_id"));
        }
        return SUCCESS;
    }

    public String addProcess() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.addProcess(request.getParameter("template_id"), request.getParameter("hierarchy_id"), parameterMap);
        }
        return SUCCESS;
    }

    public String modifyProcess() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.modifyProcess(request.getParameter("process_id"), request.getParameter("template_id"), request.getParameter("hierarchy_id"), parameterMap);
        }
        return SUCCESS;
    }

    public String deleteProcess() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.deleteProcess(request.getParameter("process_id"));
        }
        return SUCCESS;
    }

    public String associatePTInput() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.associatePT2EM(request.getParameter("PT_ID"), request.getParameter("EM_ID"), true);
        }
        return SUCCESS;
    }

    public String associatePTOutput() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.associatePT2EM(request.getParameter("PT_ID"), request.getParameter("EM_ID"), false);
        }
        return SUCCESS;
    }

    public String deleteAssociatePTInput() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.deleteAssociatePT2EM(request.getParameter("PT_ID"), parameterMap, true);
        }
        return SUCCESS;
    }

    public String deleteAssociatePTOutput() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.deleteAssociatePT2EM(request.getParameter("PT_ID"), parameterMap, false);
        }
        return SUCCESS;
    }

    public String queryAllProcessTemplate() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.queryAllProcessTemplate();
        }
        return SUCCESS;
    }

    public String queryProcessTemplateInAndOut() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.queryProcessTemplateInAndOut(request.getParameter("PT_ID"));
        }
        return SUCCESS;
    }

    public String queryProcessInAndOut() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.queryProcessInAndOut(request.getParameter("process_id"));
        }
        return SUCCESS;
    }

    public String moveProcessTemplate() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.moveProcessTemplate(request.getParameter("id"), request.getParameter("to_parent_id"));
        }
        return SUCCESS;
    }

    public String modifyProcessTemplate() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.modifyProcessTemplate(request.getParameter("id"), parameterMap);
        }
        return SUCCESS;
    }

    public String deleteProcessTemplate() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.deleteProcessTemplate(request.getParameter("id"));
        }
        return SUCCESS;
    }

    public String addProcessTemplate() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.addProcessTemplate(request.getParameter("parent_id"), parameterMap);
        }
        return SUCCESS;
    }

    public String queryAllProcessTemplateCategory() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.queryAllProcessTemplateCategory();
        }
        return SUCCESS;
    }

    public String modifyProcessTemplateCategory() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.modifyProcessTemplateCategory(request.getParameter("id"), parameterMap);
        }
        return SUCCESS;
    }

    public String deleteProcessTemplateCategory() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.deleteProcessTemplateCategory(request.getParameter("id"));
        }
        return SUCCESS;
    }

    public String addProcessTemplateCatetory() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.addProcessTemplateCatetory(parameterMap);
        }
        return SUCCESS;
    }

    public String queryAllMeasurePoints() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.queryAllMeasurePoints(parameterMap);
        }
        return SUCCESS;
    }

    public String queryDepartmentProcesses() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.queryDepartmentProcesses(request.getParameter("dep_id"));
        }
        return SUCCESS;
    }

    public String addProductBase() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.addProductBase(parameterMap);
        }
        return SUCCESS;
    }

    public String deleteProductBase() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.deleteProductBase(request.getParameter("base_id"));
        }
        return SUCCESS;
    }

    public String modifyProductBase() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.modifyProductBase(request.getParameter("base_id"), parameterMap);
        }
        return SUCCESS;
    }

    public String addProduct() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.addProduct(request.getParameter("parent_id"), parameterMap);
        }
        return SUCCESS;
    }

    public String deleteProduct() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.deleteProduct(request.getParameter("product_id"));
        }
        return SUCCESS;
    }

    public String modifyProduct() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.modifyProduct(request.getParameter("parent_id"), request.getParameter("product_id"), parameterMap);
        }
        return SUCCESS;
    }

    public String queryProductsByTemplate() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.queryProductsByTemplate(request.getParameter("template_id"));
        }
        return SUCCESS;
    }

    public String queryTemplatesByProduct() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.queryTemplatesByProduct(request.getParameter("product_id"));
        }
        return SUCCESS;
    }

    public String queryProductProcesses() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.queryProductProcesses(request.getParameter("product_id"));
        }
        return SUCCESS;
    }

    public String associateTemplateProduct() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.associateTemplateProduct(request.getParameter("template_id"), request.getParameter("product_id"));
        }
        return SUCCESS;
    }

    public String deleteAssociateTemplateProduct() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.deleteAssociateTemplateProduct(request.getParameter("template_id"), request.getParameter("product_id"));
        }
        return SUCCESS;
    }

    public String addProductMeasure() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.addProductMeasure(request.getParameter("product_id"), request.getParameter("process_id"), request.getParameter("time_id"), parameterMap);
        }
        return SUCCESS;
    }

    public String modifyProductMeasure() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.modifyProductMeasure(request.getParameter("product_id"), request.getParameter("process_id"), request.getParameter("time_id"), request.getParameter("old_time_id"), parameterMap);
        }
        return SUCCESS;
    }

    public String deleteProductMeasure() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.deleteProductMeasure(request.getParameter("product_id"), request.getParameter("process_id"), request.getParameter("time_id"));
        }
        return SUCCESS;
    }

    public String queryProductMeasures() {
        ProductionProcessManagementService productionProcessManagementService = (ProductionProcessManagementService) StorageService.ctx.getBean("productionProcessManagementService");
        if (productionProcessManagementService != null) {
            resultInfo = productionProcessManagementService.queryProductMeasures(request.getParameter("product_id"), request.getParameter("process_id"), request.getParameter("start_time"), request.getParameter("end_time"));
        }
        return SUCCESS;
    }
}
