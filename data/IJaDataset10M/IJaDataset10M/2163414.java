package ch.intertec.storybook.view.table;

import java.beans.PropertyChangeEvent;
import java.util.ArrayList;
import org.hibernate.Session;
import ch.intertec.storybook.SbConstants.ViewName;
import ch.intertec.storybook.controller.DocumentController;
import ch.intertec.storybook.model.DocumentModel;
import ch.intertec.storybook.model.hbn.dao.ChapterDAOImpl;
import ch.intertec.storybook.model.hbn.entity.AbstractEntity;
import ch.intertec.storybook.model.hbn.entity.Chapter;
import ch.intertec.storybook.model.hbn.entity.Part;
import ch.intertec.storybook.view.MainFrame;
import ch.intertec.storybook.view.SbColumnFactory;

/**
 * @author martin
 * 
 */
@SuppressWarnings("serial")
public class ChapterTable extends AbstractTable {

    public ChapterTable(MainFrame mainFrame) {
        super(mainFrame);
    }

    @Override
    public void init() {
        columns = SbColumnFactory.getInstance().getChapterColumns();
        allowMultiDelete = false;
    }

    protected void modelPropertyChangeLocal(PropertyChangeEvent evt) {
        try {
            String propName = evt.getPropertyName();
            if (DocumentController.ChapterProps.INIT.check(propName)) {
                initTableModel(evt);
            } else if (DocumentController.ChapterProps.UPDATE.check(propName)) {
                updateEntity(evt);
            } else if (DocumentController.ChapterProps.NEW.check(propName)) {
                newEntity(evt);
            } else if (DocumentController.ChapterProps.DELETE.check(propName)) {
                deleteEntity(evt);
            } else if (DocumentController.PartProps.UPDATE.check(propName)) {
                updateParts(evt);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateParts(PropertyChangeEvent evt) {
        Part oldPart = (Part) evt.getOldValue();
        Part newPart = (Part) evt.getNewValue();
        for (int row = 0; row < tableModel.getRowCount(); ++row) {
            if (oldPart.equals(newPart)) {
                tableModel.setValueAt(newPart, row, 1);
            }
        }
    }

    @Override
    protected void sendSetEntityToEdit(int row) {
        if (row == -1) {
            return;
        }
        Chapter chapter = (Chapter) getEntityFromRow(row);
        ctrl.setChapterToEdit(chapter);
        mainFrame.showView(ViewName.EDITOR);
    }

    @Override
    protected void sendSetNewEntityToEdit(AbstractEntity entity) {
        ctrl.setChapterToEdit((Chapter) entity);
        mainFrame.showView(ViewName.EDITOR);
    }

    @Override
    protected synchronized void sendDeleteEntity(int row) {
        Chapter chapter = (Chapter) getEntityFromRow(row);
        ctrl.deleteChapter(chapter);
    }

    @Override
    protected synchronized void sendDeleteEntities(int[] rows) {
        ArrayList<Long> ids = new ArrayList<Long>();
        for (int row : rows) {
            Chapter chapter = (Chapter) getEntityFromRow(row);
            ids.add(chapter.getId());
        }
        ctrl.deleteMultiChapters(ids);
    }

    @Override
    protected AbstractEntity getEntity(Long id) {
        DocumentModel model = mainFrame.getDocumentModel();
        Session session = model.beginTransaction();
        ChapterDAOImpl dao = new ChapterDAOImpl(session);
        Chapter chapter = dao.find(id);
        model.commit();
        return chapter;
    }

    @Override
    protected AbstractEntity getNewEntity() {
        return new Chapter();
    }
}
