package jsfbeansKazhukhouskayaNC;

import dataKazhukhouskayaNC.TransportsKazhukhouskayaNC;
import jsfbeansKazhukhouskayaNC.util.JsfUtil;
import jsfbeansKazhukhouskayaNC.util.PaginationHelper;
import beansKazhukhouskayaNC.TransportsKazhukhouskayaNCFacade;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

@ManagedBean(name = "transportsKazhukhouskayaNCController")
@SessionScoped
public class TransportsKazhukhouskayaNCController implements Serializable {

    private TransportsKazhukhouskayaNC current;

    private DataModel items = null;

    @EJB
    private beansKazhukhouskayaNC.TransportsKazhukhouskayaNCFacade ejbFacade;

    private PaginationHelper pagination;

    private int selectedItemIndex;

    public TransportsKazhukhouskayaNCController() {
    }

    public TransportsKazhukhouskayaNC getSelected() {
        if (current == null) {
            current = new TransportsKazhukhouskayaNC();
            selectedItemIndex = -1;
        }
        return current;
    }

    public void setSelected(TransportsKazhukhouskayaNC curr) {
        current = curr;
    }

    private TransportsKazhukhouskayaNCFacade getFacade() {
        return ejbFacade;
    }

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[] { getPageFirstItem(), getPageFirstItem() + getPageSize() }));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        if (current == null) {
            return "List";
        }
        return "View";
    }

    public String prepareCreate() {
        current = new TransportsKazhukhouskayaNC();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TransportsKazhukhouskayaNCCreated"));
            recreateModel();
            return "List";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        if (current == null) {
            return "List";
        }
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TransportsKazhukhouskayaNCUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        performDestroy();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TransportsKazhukhouskayaNCDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            selectedItemIndex = count - 1;
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[] { selectedItemIndex, selectedItemIndex + 1 }).get(0);
        }
    }

    public DataModel getModel() {
        if (items == null) {
            items = new ListDataModel(getFacade().findAll());
        }
        return items;
    }

    public List getItems() {
        return (List) getModel().getWrappedData();
    }

    private void recreateModel() {
        items = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    @FacesConverter(forClass = TransportsKazhukhouskayaNC.class)
    public static class TransportsKazhukhouskayaNCControllerConverter implements Converter {

        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TransportsKazhukhouskayaNCController controller = (TransportsKazhukhouskayaNCController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "transportsKazhukhouskayaNCController");
            return controller.ejbFacade.find(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuffer sb = new StringBuffer();
            sb.append(value);
            return sb.toString();
        }

        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof TransportsKazhukhouskayaNC) {
                TransportsKazhukhouskayaNC o = (TransportsKazhukhouskayaNC) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + TransportsKazhukhouskayaNCController.class.getName());
            }
        }
    }
}
