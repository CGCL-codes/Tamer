package ch.arpage.collaboweb.struts.forms;

import java.util.Map;
import ch.arpage.collaboweb.model.ViewType;

/**
 * ActionForm for the view type model class
 * 
 * @see ViewType
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class ViewTypeForm extends AbstractForm {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = 1L;

    private ViewType viewType;

    /**
	 * Creates a new ViewTypeForm
	 */
    public ViewTypeForm() {
        this(new ViewType());
    }

    /**
	 * Creates a new ViewTypeForm based on given viewType
	 * @param viewType	The viewType
	 */
    public ViewTypeForm(ViewType viewType) {
        this.viewType = viewType;
    }

    /**
	 * Returns the viewType.
	 * @return the viewType
	 */
    public ViewType getViewType() {
        return viewType;
    }

    /**
	 * Returns the communityId.
	 * @return the communityId
	 * @see ch.arpage.collaboweb.model.ViewType#getCommunityId()
	 */
    public long getCommunityId() {
        return viewType.getCommunityId();
    }

    /**
	 * Returns the contentType.
	 * @return the contentType
	 * @see ch.arpage.collaboweb.model.ViewType#getContentType()
	 */
    public String getContentType() {
        return viewType.getContentType();
    }

    /**
	 * Returns the labels.
	 * @return the labels
	 * @see ch.arpage.collaboweb.model.LabelableBean#getLabels()
	 */
    public Map<String, String> getLabels() {
        return viewType.getLabels();
    }

    /**
	 * Returns the viewTypeId.
	 * @return the viewTypeId
	 * @see ch.arpage.collaboweb.model.ViewType#getViewTypeId()
	 */
    public int getViewTypeId() {
        return viewType.getViewTypeId();
    }

    /**
	 * Set the communityId.
	 * @param communityId	the communityId to set
	 * @see ch.arpage.collaboweb.model.ViewType#setCommunityId(long)
	 */
    public void setCommunityId(long communityId) {
        viewType.setCommunityId(communityId);
    }

    /**
	 * Set the contentType.
	 * @param contentType	the contentType to set
	 * @see ch.arpage.collaboweb.model.ViewType#setContentType(java.lang.String)
	 */
    public void setContentType(String contentType) {
        viewType.setContentType(contentType);
    }

    /**
	 * Set the labels.
	 * @param labels	the labels to set
	 * @see ch.arpage.collaboweb.model.LabelableBean#setLabels(java.util.Map)
	 */
    public void setLabels(Map<String, String> labels) {
        viewType.setLabels(labels);
    }

    /**
	 * Set the viewTypeId.
	 * @param viewTypeId	the viewTypeId to set
	 * @see ch.arpage.collaboweb.model.ViewType#setViewTypeId(int)
	 */
    public void setViewTypeId(int viewTypeId) {
        viewType.setViewTypeId(viewTypeId);
    }
}
