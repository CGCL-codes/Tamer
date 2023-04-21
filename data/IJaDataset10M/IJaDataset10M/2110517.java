package net.sf.jrecipebox.action;

import com.opensymphony.xwork.ActionSupport;
import com.opensymphony.xwork.Action;
import net.sf.jrecipebox.domain.recipe.Recipe;
import net.sf.jrecipebox.db.dao.RecipeDaoAware;
import net.sf.jrecipebox.db.dao.RecipeDao;

public class RecipeDetailsAction extends ActionSupport implements RecipeDaoAware {

    private long recipeId = -1;

    private Recipe recipe;

    private RecipeDao recipeDao;

    public String execute() throws Exception {
        if (this.recipeId > 0) {
            this.recipe = recipeDao.getRecipeById(this.recipeId);
        }
        return Action.SUCCESS;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setRecipeDao(RecipeDao recipeDao) {
        this.recipeDao = recipeDao;
    }

    public long getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(long recipeId) {
        this.recipeId = recipeId;
    }
}
