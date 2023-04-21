package ch.intertec.storybook.view.dialog.rename;

import java.util.List;
import org.hibernate.Session;
import ch.intertec.storybook.controller.DocumentController;
import ch.intertec.storybook.model.DocumentModel;
import ch.intertec.storybook.model.hbn.dao.ItemDAOImpl;
import ch.intertec.storybook.model.hbn.entity.Item;
import ch.intertec.storybook.toolkit.I18N;
import ch.intertec.storybook.view.MainFrame;

@SuppressWarnings("serial")
public class RenameItemCategoryDialog extends AbstractRenameDialog {

    public RenameItemCategoryDialog(MainFrame mainFrame) {
        super(mainFrame);
    }

    @Override
    protected List<String> getList() {
        DocumentModel model = mainFrame.getDocumentModel();
        Session session = model.beginTransaction();
        ItemDAOImpl dao = new ItemDAOImpl(session);
        List<String> ret = dao.findCategories();
        model.commit();
        return ret;
    }

    @Override
    protected void rename(String oldValue, String newValue) {
        DocumentModel model = mainFrame.getDocumentModel();
        DocumentController ctrl = mainFrame.getDocumentController();
        Session session = model.beginTransaction();
        ItemDAOImpl dao = new ItemDAOImpl(session);
        List<Item> items = dao.findByCategory(oldValue);
        model.commit();
        for (Item item : items) {
            item.setCategory(newValue);
            ctrl.updateItem(item);
        }
    }

    @Override
    protected String getDlgTitle() {
        return I18N.getMsg("msg.item.rename.category");
    }
}
