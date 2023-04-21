package ch.intertec.storybook.model.handler;

import org.hibernate.Session;
import ch.intertec.storybook.model.DocumentModel;
import ch.intertec.storybook.model.hbn.dao.CategoryDAOImpl;
import ch.intertec.storybook.model.hbn.entity.AbstractEntity;
import ch.intertec.storybook.model.hbn.entity.Category;
import ch.intertec.storybook.view.MainFrame;
import ch.intertec.storybook.view.SbColumnFactory;

/**
 * @author martin
 * 
 */
public class CategoryEntityHandler extends AbstractEntityHandler {

    public CategoryEntityHandler(MainFrame mainFrame) {
        super(mainFrame, SbColumnFactory.getInstance().getCategoryColumns());
    }

    @Override
    public AbstractEntity createNewEntity() {
        DocumentModel model = mainFrame.getDocumentModel();
        Session session = model.beginTransaction();
        CategoryDAOImpl dao = new CategoryDAOImpl(session);
        Integer nextSort = dao.getNextSort();
        model.commit();
        Category category = new Category();
        category.setSort(nextSort);
        return category;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Class<T> getDAOClass() {
        return (Class<T>) CategoryDAOImpl.class;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Class<T> getEntityClass() {
        return (Class<T>) Category.class;
    }
}
