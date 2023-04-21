package repairIovichAN;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import javax.faces.FacesException;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.model.SelectItem;
import repairIovichAN.util.JsfUtil;
import repairIovichAN.exceptions.NonexistentEntityException;
import repairIovichAN.util.PagingInfo;

/**
 *
 * @author dosER
 */
public class ItemsIovichANController {

    public ItemsIovichANController() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        jpaController = (ItemsIovichANJpaController) facesContext.getApplication().getELResolver().getValue(facesContext.getELContext(), null, "itemsIovichANJpa");
        pagingInfo = new PagingInfo();
        converter = new ItemsIovichANConverter();
    }

    private ItemsIovichAN itemsIovichAN = null;

    private List<ItemsIovichAN> itemsIovichANItems = null;

    private ItemsIovichANJpaController jpaController = null;

    private ItemsIovichANConverter converter = null;

    private PagingInfo pagingInfo = null;

    public PagingInfo getPagingInfo() {
        if (pagingInfo.getItemCount() == -1) {
            pagingInfo.setItemCount(jpaController.getItemsIovichANCount());
        }
        return pagingInfo;
    }

    public SelectItem[] getItemsIovichANItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(jpaController.findItemsIovichANEntities(), false);
    }

    public SelectItem[] getItemsIovichANItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(jpaController.findItemsIovichANEntities(), true);
    }

    public ItemsIovichAN getItemsIovichAN() {
        if (itemsIovichAN == null) {
            itemsIovichAN = (ItemsIovichAN) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentItemsIovichAN", converter, null);
        }
        if (itemsIovichAN == null) {
            itemsIovichAN = new ItemsIovichAN();
        }
        return itemsIovichAN;
    }

    public String listSetup() {
        reset(true);
        return "itemsIovichAN_list";
    }

    public String createSetup() {
        reset(false);
        itemsIovichAN = new ItemsIovichAN();
        return "itemsIovichAN_create";
    }

    public String create() {
        try {
            jpaController.create(itemsIovichAN);
            JsfUtil.addSuccessMessage("Товар был успешно создан.");
        } catch (Exception e) {
            JsfUtil.ensureAddErrorMessage(e, "Возникла ошибка.");
            return null;
        }
        return listSetup();
    }

    public String detailSetup() {
        return scalarSetup("itemsIovichAN_detail");
    }

    public String editSetup() {
        return scalarSetup("itemsIovichAN_edit");
    }

    private String scalarSetup(String destination) {
        reset(false);
        itemsIovichAN = (ItemsIovichAN) JsfUtil.getObjectFromRequestParameter("jsfcrud.currentItemsIovichAN", converter, null);
        if (itemsIovichAN == null) {
            String requestItemsIovichANString = JsfUtil.getRequestParameter("jsfcrud.currentItemsIovichAN");
            JsfUtil.addErrorMessage("The itemsIovichAN with id " + requestItemsIovichANString + " no longer exists.");
            return relatedOrListOutcome();
        }
        return destination;
    }

    public String edit() {
        String itemsIovichANString = converter.getAsString(FacesContext.getCurrentInstance(), null, itemsIovichAN);
        String currentItemsIovichANString = JsfUtil.getRequestParameter("jsfcrud.currentItemsIovichAN");
        if (itemsIovichANString == null || itemsIovichANString.length() == 0) {
            String outcome = editSetup();
            if ("itemsIovichAN_edit".equals(outcome)) {
                JsfUtil.addErrorMessage("Невозможно редактировать. Попробуйте ещё.");
            }
            return outcome;
        }
        try {
            jpaController.edit(itemsIovichAN);
            JsfUtil.addSuccessMessage("Успешно изменено.");
        } catch (NonexistentEntityException ne) {
            JsfUtil.addErrorMessage(ne.getLocalizedMessage());
            return listSetup();
        } catch (Exception e) {
            JsfUtil.ensureAddErrorMessage(e, "Ошибка базы данных.");
            return null;
        }
        return detailSetup();
    }

    public String destroy() {
        String idAsString = JsfUtil.getRequestParameter("jsfcrud.currentItemsIovichAN");
        Integer id = new Integer(idAsString);
        try {
            jpaController.destroy(id);
            JsfUtil.addSuccessMessage("Успешно удалено.");
        } catch (NonexistentEntityException ne) {
            JsfUtil.addErrorMessage(ne.getLocalizedMessage());
            return relatedOrListOutcome();
        } catch (Exception e) {
            JsfUtil.ensureAddErrorMessage(e, "Ошибка базы данных.");
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

    public List<ItemsIovichAN> getItemsIovichANItems() {
        if (itemsIovichANItems == null) {
            getPagingInfo();
            itemsIovichANItems = jpaController.findItemsIovichANEntities(pagingInfo.getBatchSize(), pagingInfo.getFirstItem());
        }
        return itemsIovichANItems;
    }

    public String next() {
        reset(false);
        getPagingInfo().nextPage();
        return "itemsIovichAN_list";
    }

    public String prev() {
        reset(false);
        getPagingInfo().previousPage();
        return "itemsIovichAN_list";
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
        itemsIovichAN = null;
        itemsIovichANItems = null;
        pagingInfo.setItemCount(-1);
        if (resetFirstItem) {
            pagingInfo.setFirstItem(0);
        }
    }

    public void validateCreate(FacesContext facesContext, UIComponent component, Object value) {
        ItemsIovichAN newItemsIovichAN = new ItemsIovichAN();
        String newItemsIovichANString = converter.getAsString(FacesContext.getCurrentInstance(), null, newItemsIovichAN);
        String itemsIovichANString = converter.getAsString(FacesContext.getCurrentInstance(), null, itemsIovichAN);
        if (!newItemsIovichANString.equals(itemsIovichANString)) {
            createSetup();
        }
    }

    public Converter getConverter() {
        return converter;
    }
}
