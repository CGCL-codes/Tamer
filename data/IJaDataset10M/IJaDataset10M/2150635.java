package ch.arpage.collaboweb.struts.forms;

import ch.arpage.collaboweb.model.View;
import ch.arpage.collaboweb.model.ViewType;

/**
 * ActionForm for the view model class
 * 
 * @see View
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ViewForm extends AbstractForm {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = 1L;

    private View view;

    /**
	 * Creates a new ViewForm
	 */
    public ViewForm() {
        this(new View());
    }

    /**
	 * Creates a new ViewForm based on the given view
	 * @param view the view
	 */
    public ViewForm(View view) {
        this.view = view;
    }

    /**
	 * Returns the view.
	 * @return the view
	 */
    public View getView() {
        return view;
    }

    /**
	 * Returns the styleSheet.
	 * @return the styleSheet
	 * @see ch.arpage.collaboweb.model.View#getStyleSheet()
	 */
    public String getStyleSheet() {
        return view.getStyleSheet();
    }

    /**
	 * Returns the typeId.
	 * @return the typeId
	 * @see ch.arpage.collaboweb.model.View#getTypeId()
	 */
    public int getTypeId() {
        return view.getTypeId();
    }

    /**
	 * Returns the viewType.
	 * @return the viewType
	 * @see ch.arpage.collaboweb.model.View#getViewType()
	 */
    public ViewType getViewType() {
        return view.getViewType();
    }

    /**
	 * Returns the viewTypeId.
	 * @return the viewTypeId
	 * @see ch.arpage.collaboweb.model.View#getViewTypeId()
	 */
    public int getViewTypeId() {
        return view.getViewTypeId();
    }

    /**
	 * Set the styleSheet.
	 * @param styleSheet	The styleSheet to set
	 * @see ch.arpage.collaboweb.model.View#setStyleSheet(java.lang.String)
	 */
    public void setStyleSheet(String styleSheet) {
        view.setStyleSheet(styleSheet);
    }

    /**
	 * Set the typeId.
	 * @param typeId	The typeId to set
	 * @see ch.arpage.collaboweb.model.View#setTypeId(int)
	 */
    public void setTypeId(int typeId) {
        view.setTypeId(typeId);
    }

    /**
	 * Set the viewTypeId.
	 * @param viewTypeId	The viewTypeId to set
	 * @see ch.arpage.collaboweb.model.View#setViewTypeId(int)
	 */
    public void setViewTypeId(int viewTypeId) {
        view.setViewTypeId(viewTypeId);
    }
}
