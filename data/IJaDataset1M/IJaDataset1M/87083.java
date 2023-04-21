package org.dolmen.mvc.view;

import org.dolmen.mvc.controller.ControllerListener;

/**
 * a specialized {@link View} dedicated to editing a bean
 * 
 * @since 0.0.1
 * @author <a href="mailto:straahd@users.sourceforge.net">Laurent Lecomte
 * 
 */
public interface EditView extends View {

    /**
	 * close the view
	 * 
	 */
    public void closeEditView();

    /**
	 * open the view
	 * 
	 * @param aControllerListener
	 */
    public void openEditView(ControllerListener aControllerListener);

    /**
	 * set the bean to manage
	 * 
	 * @param aBean
	 *          the bean
	 */
    public void setBean(Object aBean);

    /**
	 * update bean depending of user's data
	 * 
	 * @param aBean
	 *          the bean
	 * @throws Exception
	 */
    public void updateBean(Object aBean) throws Exception;

    /**
	 * Update the components from the bean
	 * 
	 * @param aBean
	 *          the bean
	 * @throws Exception
	 */
    public void updateComponentFromBean(Object aBean) throws Exception;

    /**
	 * must return true if user's data are valid and bean can be persisted
	 * 
	 * @param aBean
	 *          the bean
	 * @return true if valid
	 * @throws Exception
	 */
    public boolean validateBean(Object aBean) throws Exception;
}
