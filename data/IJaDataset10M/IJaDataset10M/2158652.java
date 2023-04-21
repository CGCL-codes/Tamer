package cookbook.control;

import cookbook.model.CookbookManager;
import cookbook.LanguageContent;
import cookbook.model.RecipeList;
import cookbook.view.UtilityClass;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.JOptionPane;
import javax.swing.SwingWorker;

/**
 * Handles action events from every GUI component that can save recipe files.
 * 
 * @author Dominik Schaufelberger
 */
public class SaveAction extends AbstractAction {

    /**
     * Creates an abstrac action with given label.
     * 
     * @param label
     *          component label
     */
    public SaveAction(String label) {
        super(label);
    }

    /**
     * {@inheritDoc }
     * @param e 
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        final CookbookManager manager = CookbookManager.getCookbookManager();
        RecipeList rList = manager.getRList();
        String recipeName = manager.getRDescription().getName();
        if (UtilityClass.isValidRecipeName(recipeName)) {
            boolean saving = true;
            if (rList.recipeExists(recipeName) && manager.getOpenRecipeID() != rList.getID(recipeName)) {
                int continueSaving = JOptionPane.showConfirmDialog(UtilityClass.getActiveFrame(), LanguageContent.recipe_dialog_save_text, LanguageContent.recipe_dialog_title, JOptionPane.YES_NO_OPTION);
                if (continueSaving == JOptionPane.NO_OPTION) {
                    saving = false;
                }
            }
            if (saving) {
                SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {

                    @Override
                    protected Void doInBackground() throws Exception {
                        manager.saveFile();
                        return Void.class.newInstance();
                    }
                };
                worker.execute();
            }
        } else {
            JOptionPane.showMessageDialog(UtilityClass.getActiveFrame(), LanguageContent.recipe_dialog_invalid_input, LanguageContent.recipe_dialog_title, JOptionPane.INFORMATION_MESSAGE);
        }
    }
}
