package odrop.shared.dto;

import java.util.HashMap;
import odrop.shared.dto.lists.ProductionsDTO;
import odrop.shared.enumerators.RecipeType;
import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * RecipeDTO extends the ProductionDTO (as they share all the attributes) and additionally contains
 * information about a recipe (recipeID, craftLevel required to use it, producable items, successRate etc.).
 * 
 * @author divStar
 *
 */
public class RecipeDTO extends ProductionDTO implements IsSerializable {

    private int recipeID;

    private int craftLevel;

    private RecipeType type;

    private int successRate;

    private String statUseName;

    private int statUseValue;

    private ProductionsDTO produced = new ProductionsDTO();

    private static HashMap<Integer, Integer> productionRecipes = new HashMap<Integer, Integer>();

    public int getRecipeID() {
        return recipeID;
    }

    public void setRecipeID(int recipeID) {
        this.recipeID = recipeID;
    }

    public int getCraftLevel() {
        return craftLevel;
    }

    public void setCraftLevel(int craftLevel) {
        this.craftLevel = craftLevel;
    }

    public RecipeType getType() {
        return type;
    }

    public void setType(RecipeType type) {
        this.type = type;
    }

    public int getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(int successRate) {
        this.successRate = successRate;
    }

    public String getStatUseName() {
        return statUseName;
    }

    public void setStatUseName(String statUseName) {
        this.statUseName = statUseName;
    }

    public int getStatUseValue() {
        return statUseValue;
    }

    public void setStatUseValue(int statUseValue) {
        this.statUseValue = statUseValue;
    }

    public void setProduced(ProductionsDTO produced) {
        this.produced = produced;
    }

    public ProductionsDTO getProduced() {
        return this.produced;
    }

    /**
	 * Default constructor
	 */
    public RecipeDTO() {
    }

    public HashMap<Integer, Integer> getProductionRecipes() {
        return productionRecipes;
    }
}
