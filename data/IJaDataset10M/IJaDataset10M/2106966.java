package home.jes.recipe.client.db;

import home.jes.db.rhumba.client.*;
import java.util.*;

public class RecipeProxy extends RHRowProxy {

    public RecipeProxy(RecipeTableProxy table) {
        super(table);
    }

    public RecipeProxy(RecipeTableProxy table, Hashtable struct) {
        super(table, struct);
    }

    public void setTitle(String title) {
        set("title", title);
    }

    public void setRate(int rate) {
        set("rate", new Integer(rate));
    }

    public void setIngredients(String ingredients) {
        set("ingredients", ingredients);
    }

    public void setSteps(String steps) {
        set("steps", steps);
    }

    public void setComments(String comments) {
        set("comments", comments);
    }

    public void setSource(String source) {
        set("source", source);
    }

    public void setPictures(String pictures) {
        set("pictures", pictures);
    }

    public String getTitle() {
        return (String) get("title");
    }

    public int getRate() {
        return ((Integer) get("rate")).intValue();
    }

    public String getIngredients() {
        return (String) get("ingredients");
    }

    public String getSteps() {
        return (String) get("steps");
    }

    public String getComments() {
        return (String) get("comments");
    }

    public String getSource() {
        return (String) get("source");
    }

    public String getPictures() {
        return (String) get("pictures");
    }

    public void addFolder(FolderProxy f) {
        ((RecipeTableProxy) table).folderConnector.addConnection(this, f);
    }

    public void deleteFolder(FolderProxy f) {
        ((RecipeTableProxy) table).folderConnector.deleteConnection(this, f);
    }

    public void removeFolders() {
        ((RecipeTableProxy) table).folderConnector.deleteByRecipe(this);
    }

    public RHCursorProxy getFolders() {
        return ((RecipeTableProxy) table).folderConnector.findByRecipe(this);
    }

    public RecipeTableProxy getTable() {
        return (RecipeTableProxy) table;
    }
}
