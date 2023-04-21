package passreminder.action;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.window.Window;
import passreminder.DBManager;
import passreminder.I18Nable;
import passreminder.Messages;
import passreminder.ModelManager;
import passreminder.PassReminder;
import passreminder.UIManager;
import passreminder.model.Item;
import passreminder.ui.SearchDialog;

public class SearchAction extends Action implements I18Nable {

    private String search;

    public SearchAction() {
        updateLanguage();
        setImageDescriptor(ImageDescriptor.createFromURL(getClass().getResource("search.gif")));
    }

    public void run() {
        SearchDialog searchDialog = new SearchDialog(PassReminder.getInstance().getShell(), Messages.getString("search.prompt"));
        if (searchDialog.open() == Window.CANCEL) {
            PassReminder.getInstance().setStatus(Messages.getString("action_cancelled"));
            PassReminder.getInstance().refreshTitle();
            return;
        }
        search = searchDialog.getSearch().toLowerCase();
        int count = DBManager.getInstance().iListMain.size();
        int foundCount = 0;
        for (int i = 0; i < count; i++) {
            Item item = (Item) DBManager.getInstance().iListMain.get(i);
            if (item.isUserGroup() && (item.name.toLowerCase().indexOf(search) > -1 || item.user.toLowerCase().indexOf(search) > -1 || item.password.toLowerCase().indexOf(search) > -1 || item.command.toLowerCase().indexOf(search) > -1 || item.description.toLowerCase().indexOf(search) > -1 || ModelManager.getInstance().toGroup(item.groupId).name.toLowerCase().indexOf(search) > -1)) {
                foundCount++;
                item.wordSearching = new String(search);
            } else item.wordSearching = null;
        }
        if (foundCount > 0) {
            ModelManager.groupSearch.name = Messages.getString("tree.search.result", search);
            UIManager.getInstance().refresh();
            UIManager.getInstance().folderTreeViewer.setSelection(new StructuredSelection(ModelManager.groupSearch), true);
            UIManager.getInstance().folderTreeViewer.getTree().setFocus();
        }
        PassReminder.getInstance().setStatus(Messages.getString("search.found", foundCount));
    }

    public void updateLanguage() {
        this.setText(Messages.getString("search.action.text"));
        if (search != null) ModelManager.groupSearch.name = Messages.getString("tree.search.result", search);
    }
}
