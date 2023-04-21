package com.condominium.expenses.managedbean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.UISelectOne;
import javax.faces.context.FacesContext;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.model.SelectItem;
import com.condominium.common.utils.JSFUtil;
import com.condominium.common.utils.NumberUtil;
import com.condominium.expenses.exception.ExpensesException;
import com.condominium.expenses.service.ExpensesService;
import com.condominium.expenses.view.ExpensesView;

@ManagedBean(name = "expensesBean")
@SessionScoped
public class ExpensesManagedBean implements Serializable {

    @ManagedProperty(value = "#{expensesService}")
    private ExpensesService expensesService;

    private ExpensesView expensesView = new ExpensesView();

    private ExpensesView expensesEditView = new ExpensesView();

    private List<ExpensesView> expensesList;

    private String expensesSearchProvier;

    private String expensesSearchMonth;

    private String expensesSearchYear;

    private String expensesSearchCategory;

    private boolean showTable = false;

    private String total;

    public void init() {
    }

    public void validateExpensesForm(ComponentSystemEvent event) {
        FacesContext fc = FacesContext.getCurrentInstance();
        UIComponent components = event.getComponent();
        UIInput impInput = (UIInput) components.findComponent("importeId");
        String impValue = impInput.getLocalValue().toString();
        UISelectOne expenseType = (UISelectOne) components.findComponent("egresoId");
        String expValue = expenseType.getLocalValue().toString();
        UISelectOne provUI = (UISelectOne) components.findComponent("proveedorId");
        String provValue = provUI.getLocalValue().toString();
        if (impValue.equals("".trim())) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "El importe es requerido.", "El importe es requerido."));
        }
        if (expValue.equals("-1")) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Seleccione un tipo de Egreso.", "Seleccione un tipo de Egreso."));
        }
        if (provValue.equals("-1")) {
            fc.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Seleccione Proveedor.", "Seleccione un Proveedor"));
        }
        if (!fc.getMessageList().isEmpty()) {
            fc.renderResponse();
        }
    }

    public String searchAction() {
        try {
            expensesList = expensesService.searchExpensesList(Integer.parseInt(expensesSearchMonth), Integer.parseInt(expensesSearchYear), Integer.parseInt(expensesSearchProvier), Integer.parseInt(expensesSearchCategory));
            if (!expensesList.isEmpty()) {
                double total = 0;
                for (ExpensesView v : expensesList) {
                    total += NumberUtil.parseDouble(v.getAmount());
                }
                this.total = NumberUtil.convertQuantity(total);
                this.setShowTable(true);
            } else {
                this.setShowTable(false);
                JSFUtil.writeMessage(FacesMessage.SEVERITY_ERROR, "No se encontraron resultados.", "No se encontraron resultados.");
            }
        } catch (ExpensesException e) {
            JSFUtil.writeMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getExceptionCode());
        }
        return null;
    }

    public void clean() {
        this.expensesList = null;
        this.showTable = false;
        this.expensesSearchProvier = "";
        this.expensesView = new ExpensesView();
    }

    public void fillDropDowns(ComponentSystemEvent event) {
        Calendar cal = Calendar.getInstance();
        if (expensesSearchMonth == null && expensesSearchYear == null) {
            this.expensesSearchMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
            this.expensesSearchYear = String.valueOf(cal.get(Calendar.YEAR));
        } else if (expensesSearchMonth != null && expensesSearchYear != null) {
            if (expensesSearchMonth.equals("") && expensesSearchYear.equals("")) {
                this.expensesSearchMonth = String.valueOf(cal.get(Calendar.MONTH) + 1);
                this.expensesSearchYear = String.valueOf(cal.get(Calendar.YEAR));
            }
        }
    }

    public String goAddExpenses() {
        return "registrar_egreso";
    }

    public String backAction() {
        this.clean();
        return "listado_egresos";
    }

    public String goEditExpensesAction() {
        String id = JSFUtil.getParam("expensesId");
        for (ExpensesView e : expensesList) {
            if (id.equals(e.getId())) {
                this.setExpensesEditView(e);
            }
        }
        return "editar_egreso";
    }

    public String editExpensesAction() {
        try {
            expensesService.editExpenses(expensesEditView);
            this.clean();
            JSFUtil.writeMessage(FacesMessage.SEVERITY_INFO, "El egreso se ha editado correctamente.", "El egreso se ha editado correctamente.");
        } catch (ExpensesException e) {
            JSFUtil.writeMessage(FacesMessage.SEVERITY_ERROR, e.getErrorCode(), "Hubo un error al tratar de editar el egreso.");
            return null;
        }
        return "listado_egresos";
    }

    public String addExpensesAction() {
        try {
            this.expensesService.addExpenses(expensesView);
            this.clean();
            JSFUtil.writeMessage(FacesMessage.SEVERITY_INFO, "El egreso se ha registrado correctamente.", "El egreso se ha registrado correctamente.");
        } catch (ExpensesException e) {
            JSFUtil.writeMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getExceptionCode());
            return null;
        }
        return "listado_egresos";
    }

    public String deleteExpensesAction() {
        String id = JSFUtil.getParam("expensesId");
        try {
            this.expensesService.deleteExpenses(Integer.parseInt(id));
            this.clean();
            JSFUtil.writeMessage(FacesMessage.SEVERITY_ERROR, "El Egreso se ha eliminado correctamente.", "El Egreso se ha eliminado correctamente.");
        } catch (NumberFormatException e) {
            JSFUtil.writeMessage(FacesMessage.SEVERITY_ERROR, "El Id no es entero.", "El Id no es entero.");
            return null;
        } catch (ExpensesException e) {
            JSFUtil.writeMessage(FacesMessage.SEVERITY_ERROR, e.getMessage(), e.getExceptionCode());
            return null;
        }
        return "listado_egresos";
    }

    public List<SelectItem> getExpensesItems() {
        List<SelectItem> list = new ArrayList<SelectItem>(0);
        try {
            list = expensesService.getExpensesItems();
        } catch (ExpensesException e) {
            list = new ArrayList<SelectItem>(0);
        }
        return list;
    }

    public List<SelectItem> getSuppliersList() {
        List<SelectItem> list = new ArrayList<SelectItem>(0);
        try {
            list = expensesService.getSuppliersItems();
        } catch (ExpensesException e) {
            list = new ArrayList<SelectItem>(0);
        }
        return list;
    }

    public ExpensesView getExpensesView() {
        return expensesView;
    }

    public void setExpensesView(ExpensesView expensesView) {
        this.expensesView = expensesView;
    }

    public void setExpensesService(ExpensesService expensesService) {
        this.expensesService = expensesService;
    }

    public List<ExpensesView> getExpensesList() {
        return expensesList;
    }

    public void setExpensesList(List<ExpensesView> expensesList) {
        this.expensesList = expensesList;
    }

    public String getExpensesSearchProvier() {
        return expensesSearchProvier;
    }

    public void setExpensesSearchProvier(String expensesSearchProvier) {
        this.expensesSearchProvier = expensesSearchProvier;
    }

    public String getExpensesSearchMonth() {
        return expensesSearchMonth;
    }

    public void setExpensesSearchMonth(String expensesSearchMonth) {
        this.expensesSearchMonth = expensesSearchMonth;
    }

    public String getExpensesSearchYear() {
        return expensesSearchYear;
    }

    public void setExpensesSearchYear(String expensesSearchYear) {
        this.expensesSearchYear = expensesSearchYear;
    }

    public boolean isShowTable() {
        return showTable;
    }

    public void setShowTable(boolean showTable) {
        this.showTable = showTable;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getExpensesSearchCategory() {
        return expensesSearchCategory;
    }

    public void setExpensesSearchCategory(String expensesSearchCategory) {
        this.expensesSearchCategory = expensesSearchCategory;
    }

    public ExpensesView getExpensesEditView() {
        return expensesEditView;
    }

    public void setExpensesEditView(ExpensesView expensesEditView) {
        this.expensesEditView = expensesEditView;
    }
}
