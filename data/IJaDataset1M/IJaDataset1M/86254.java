package home.jes.recipe.client.db;

import home.jes.db.rhumba.client.RHAssociationTableProxy;
import home.jes.db.rhumba.client.RHCursorProxy;
import home.jes.db.rhumba.client.RHRowProxy;

/**
 *  Description of the Class
 *
 * @author     john
 * @created    July 17, 2005
 */
public class RecFolderConnectorProxy extends RHAssociationTableProxy {

    public RecFolderConnectorProxy(RecipeTableProxy recipeTable, FolderTableProxy folderTable, RecipeBoxDatabaseProxy db) {
        super("recfolders", recipeTable, folderTable, db);
        this.recipeTable = recipeTable;
        this.folderTable = folderTable;
    }

    public void addConnection(RHRowProxy recipeRow, RHRowProxy folderRow) {
        addAssociation(recipeRow.getId(), folderRow.getId());
    }

    public void deleteConnection(RHRowProxy recipeRow, RHRowProxy folderRow) {
        deleteAssociation(recipeRow.getId(), folderRow.getId());
    }

    /**
	 *  This will delete all connections that has the given folder
	 *
	 * @param  folderRow  Description of the Parameter
	 */
    public void deleteByFolder(RHRowProxy folderRow) {
        deleteByT2(folderRow.getId());
    }

    /**
	 *  This will delete all connections that has the given recipe
	 *
	 * @param  recipeRow  Description of the Parameter
	 */
    public void deleteByRecipe(RHRowProxy recipeRow) {
        deleteByT1(recipeRow.getId());
    }

    /**
	 *  Returns a list of recipe instances given a folder This is a many:many
	 *  relationship, so this returns a List
	 *
	 * @param  folderRow  Description of the Parameter
	 * @return            Description of the Return Value
	 */
    public RHCursorProxy findByFolder(RHRowProxy folderRow) {
        try {
            db.clearParms();
            db.addParm(new Integer(folderRow.getId()));
            Integer cur = (Integer) db.execute("RHDatabase.findByFolder");
            return new RHCursorProxy(recipeTable, cur);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
	 *  Returns a list of folder instances given a recipe This is a many:many
	 *  relationship, so it returns a list
	 *
	 * @param  recipeRow  Description of the Parameter
	 * @return            Description of the Return Value
	 */
    public RHCursorProxy findByRecipe(RHRowProxy recipeRow) {
        try {
            db.clearParms();
            db.addParm(new Integer(recipeRow.getId()));
            return new RHCursorProxy(folderTable, (Integer) db.execute("RHDatabase.findByRecipe"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public RecipeTableProxy getTable1() {
        return recipeTable;
    }

    public FolderTableProxy getTable2() {
        return folderTable;
    }

    private RecipeTableProxy recipeTable;

    private FolderTableProxy folderTable;
}
