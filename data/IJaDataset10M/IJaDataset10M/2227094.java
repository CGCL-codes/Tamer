package DAOs;

import DAOs.util.PagingInfo;
import beans.EmpFile;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javax.faces.FacesException;
import DAOs.util.JsfUtil;
import DAOs.exceptions.NonexistentEntityException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;

/**
 *
 * @author Ioana C
 */
public class EmpFileController {

    public EmpFileController() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        jpaController = (EmpFileJpaController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "empFileJpa");
        pagingInfo = new PagingInfo();
        converter = new EmpFileConverter();
    }

    private EmpFile empFile = null;

    private List<EmpFile> empFileItems = null;

    private EmpFileJpaController jpaController = null;

    private EmpFileConverter converter = null;

    private PagingInfo pagingInfo = null;

    public PagingInfo getPagingInfo() {
        if (pagingInfo.getItemCount() == -1) {
            pagingInfo.setItemCount(jpaController.getEmpFileCount());
        }
        return pagingInfo;
    }

    public SelectItem[] getEmpFileItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(jpaController.findEmpFileEntities(), false);
    }

    public SelectItem[] getEmpFileItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(jpaController.findEmpFileEntities(), true);
    }

    public EmpFile getEmpFile() {
        if (empFile == null) {
            empFile = (EmpFile) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentEmpFile", converter, null);
        }
        if (empFile == null) {
            empFile = new EmpFile();
        }
        return empFile;
    }

    public String listSetup() {
        reset(true);
        return "empFile_list";
    }

    public String createSetup() {
        reset(false);
        empFile = new EmpFile();
        return "empFile_create";
    }

    public String create() {
        try {
            jpaController.create(empFile);
            JsfUtil.addSuccessMessage("EmpFile was successfully created.");
        } catch (Exception e) {
            JsfUtil.ensureAddErrorMessage(e, "A persistence error occurred.");
            return null;
        }
        return listSetup();
    }

    public String detailSetup() {
        return scalarSetup("empFile_detail");
    }

    public String editSetup() {
        return scalarSetup("empFile_edit");
    }

    private String scalarSetup(String destination) {
        reset(false);
        empFile = (EmpFile) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentEmpFile", converter, null);
        if (empFile == null) {
            String requestEmpFileString = JsfUtil.getRequestParameter("jsfcrud.currentEmpFile");
            JsfUtil.addErrorMessage("The empFile with id " + requestEmpFileString + " no longer exists.");
            return relatedOrListOutcome();
        }
        return destination;
    }

    public String edit() {
        String empFileString = converter.getAsString(FacesContext.getCurrentInstance(), null, empFile);
        String currentEmpFileString = JsfUtil.getRequestParameter("jsfcrud.currentEmpFile");
        if (empFileString == null || empFileString.length() == 0 || !empFileString.equals(currentEmpFileString)) {
            String outcome = editSetup();
            if ("empFile_edit".equals(outcome)) {
                JsfUtil.addErrorMessage("Could not edit empFile. Try again.");
            }
            return outcome;
        }
        try {
            jpaController.edit(empFile);
            JsfUtil.addSuccessMessage("EmpFile was successfully updated.");
        } catch (NonexistentEntityException ne) {
            JsfUtil.addErrorMessage(ne.getLocalizedMessage());
            return listSetup();
        } catch (Exception e) {
            JsfUtil.ensureAddErrorMessage(e, "A persistence error occurred.");
            return null;
        }
        return detailSetup();
    }

    public String destroy() {
        String idAsString = JsfUtil.getRequestParameter("jsfcrud.currentEmpFile");
        Integer id = new Integer(idAsString);
        try {
            jpaController.destroy(id);
            JsfUtil.addSuccessMessage("EmpFile was successfully deleted.");
        } catch (NonexistentEntityException ne) {
            JsfUtil.addErrorMessage(ne.getLocalizedMessage());
            return relatedOrListOutcome();
        } catch (Exception e) {
            JsfUtil.ensureAddErrorMessage(e, "A persistence error occurred.");
            return null;
        }
        return relatedOrListOutcome();
    }

    private String relatedOrListOutcome() {
        String relatedControllerOutcome = relatedControllerOutcome();
        if (relatedControllerOutcome != null) {
            return relatedControllerOutcome;
        }
        return listSetup();
    }

    public List<EmpFile> getEmpFileItems() {
        if (empFileItems == null) {
            getPagingInfo();
            empFileItems = jpaController.findEmpFileEntities(pagingInfo.getBatchSize(), pagingInfo.getFirstItem());
        }
        return empFileItems;
    }

    public String next() {
        reset(false);
        getPagingInfo().nextPage();
        return "empFile_list";
    }

    public String prev() {
        reset(false);
        getPagingInfo().previousPage();
        return "empFile_list";
    }

    private String relatedControllerOutcome() {
        String relatedControllerString = JsfUtil.getRequestParameter("jsfcrud.relatedController");
        String relatedControllerTypeString = JsfUtil.getRequestParameter("jsfcrud.relatedControllerType");
        if (relatedControllerString != null && relatedControllerTypeString != null) {
            FacesContext context = FacesContext.getCurrentInstance();
            Object relatedController = context.getApplication().getELResolver().getValue(context.getELContext(), null, relatedControllerString);
            try {
                Class<?> relatedControllerType = Class.forName(relatedControllerTypeString);
                Method detailSetupMethod = relatedControllerType.getMethod("detailSetup");
                return (String) detailSetupMethod.invoke(relatedController);
            } catch (ClassNotFoundException e) {
                throw new FacesException(e);
            } catch (NoSuchMethodException e) {
                throw new FacesException(e);
            } catch (IllegalAccessException e) {
                throw new FacesException(e);
            } catch (InvocationTargetException e) {
                throw new FacesException(e);
            }
        }
        return null;
    }

    private void reset(boolean resetFirstItem) {
        empFile = null;
        empFileItems = null;
        pagingInfo.setItemCount(-1);
        if (resetFirstItem) {
            pagingInfo.setFirstItem(0);
        }
    }

    public void validateCreate(FacesContext facesContext, UIComponent component, Object value) {
        EmpFile newEmpFile = new EmpFile();
        String newEmpFileString = converter.getAsString(FacesContext.getCurrentInstance(), null, newEmpFile);
        String empFileString = converter.getAsString(FacesContext.getCurrentInstance(), null, empFile);
        if (!newEmpFileString.equals(empFileString)) {
            createSetup();
        }
    }

    public Converter getConverter() {
        return converter;
    }
}
