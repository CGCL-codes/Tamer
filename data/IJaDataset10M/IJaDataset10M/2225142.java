package view.backing;

import java.io.Serializable;
import oracle.adf.model.BindingContext;
import oracle.binding.BindingContainer;
import oracle.binding.OperationBinding;

public class DeptDetailsBean implements Serializable {

    public String departmentName;

    public DeptDetailsBean() {
        super();
    }

    /**
   * This is the method fired after the framework processes the URL parameters.
   * If the URL parameters were set into backing bean properties, then thse are available here
   * Or if EL was used to store values in a scope, EL can be used here as well.
   */
    public void loadDepartmentDetails() {
        System.out.println("Entering > loadDepartmentDetails");
        System.out.println("Got dept name :" + getDepartmentName());
        BindingContainer bc = BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding operationBinding = bc.getOperationBinding("getDepartmentDetails");
        operationBinding.getParamsMap().put("deptName", getDepartmentName());
        operationBinding.execute();
        System.out.println("Exiting < loadDepartmentDetails");
    }

    /**
   * This is the method fired after the framework processes the URL parameters.
   * If the URL parameters were set into backing bean properties, then thse are available here
   * Or if EL was used to store values in a scope, EL can be used here as well.
   */
    public void loadDepartmentDetailsURL(String deptName) {
        System.out.println("Entering > loadDepartmentDetailsURL");
        System.out.println("Got dept name :" + deptName);
        BindingContainer bc = BindingContext.getCurrent().getCurrentBindingsEntry();
        OperationBinding operationBinding = bc.getOperationBinding("getDepartmentDetails");
        operationBinding.getParamsMap().put("deptName", deptName);
        operationBinding.execute();
        System.out.println("Exiting < loadDepartmentDetailsURL");
    }

    public void setDepartmentName(String name) {
        this.departmentName = name;
    }

    public String getDepartmentName() {
        return departmentName;
    }
}
