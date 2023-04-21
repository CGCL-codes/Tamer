package recipedb.controller;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import recipedb.model.Ingredient;
import recipedb.model.persistence.DaoConfig;
import recipedb.model.persistence.iface.IngredientDao;
import com.ibatis.dao.client.DaoManager;

/**
 * @author arjen
 * 
 */
public class IngredientController {

    public static final String LISTS_STATE = "ingredientList";

    public static final String DETAILS_STATE = "ingredientDetails";

    public static final String CREATE_STATE = "ingredientCreate";

    public static final String EDIT_STATE = "ingredientEdit";

    public static final String DELETE_STATE = "ingredientDelete";

    private Ingredient selectedIngredient;

    private DataModel model;

    private Ingredient currentIngredient;

    private final DaoManager daoManager;

    private final IngredientDao ingredientDao;

    public Ingredient getCurrentIngredient() {
        return currentIngredient;
    }

    public void setCurrentIngredient(Ingredient currentIngredient) {
        this.currentIngredient = currentIngredient;
    }

    public IngredientController() {
        daoManager = DaoConfig.getDaoManager();
        ingredientDao = (IngredientDao) daoManager.getDao(IngredientDao.class);
    }

    public Ingredient getSelectedIngredient() {
        return selectedIngredient;
    }

    public void setSelectedIngredient(Ingredient selectedIngredient) {
        this.selectedIngredient = selectedIngredient;
    }

    public DataModel getIngredientList() {
        model = new ListDataModel(ingredientDao.getIngredientList());
        return model;
    }

    public String showCurrentIngredientDetails() {
        setIngredientFromRequestParam();
        return DETAILS_STATE;
    }

    public void setIngredientFromRequestParam() {
        Ingredient ingredient = getIngredientFromRequestParam();
        setCurrentIngredient(ingredient);
    }

    public Ingredient getIngredientFromRequestParam() {
        Ingredient ingredient = (Ingredient) model.getRowData();
        return ingredient;
    }

    public String editCurrentIngredient() {
        setIngredientFromRequestParam();
        return EDIT_STATE;
    }

    public String saveCurrentIngredient() {
        try {
            if (currentIngredient.getIngredientId() == null) {
                ingredientDao.insertIngredient(currentIngredient);
            } else {
                ingredientDao.updateIngredient(currentIngredient);
            }
            addSuccessMessage("Ingredient was successfully created.");
        } catch (Exception ex) {
            try {
                addErrorMessage(ex.getLocalizedMessage());
            } catch (Exception e) {
                addErrorMessage(e.getLocalizedMessage());
            }
        } finally {
        }
        return LISTS_STATE;
    }

    public static void addErrorMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_ERROR, msg, msg);
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage(null, facesMsg);
    }

    public static void addSuccessMessage(String msg) {
        FacesMessage facesMsg = new FacesMessage(FacesMessage.SEVERITY_INFO, msg, msg);
        FacesContext fc = FacesContext.getCurrentInstance();
        fc.addMessage("successInfo", facesMsg);
    }

    public String createIngredient() {
        this.currentIngredient = new Ingredient();
        return CREATE_STATE;
    }
}
