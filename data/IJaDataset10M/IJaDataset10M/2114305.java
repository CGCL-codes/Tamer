package ch.arpage.collaboweb.model;

import java.util.List;

/**
 * View model class
 *
 * @author <a href="mailto:patrick@arpage.ch">Patrick Herber</a>
 */
public class View extends AbstractBean {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = 1L;

    private int typeId;

    private int viewTypeId;

    private ViewType viewType;

    private String styleSheet;

    private List<Action> actions;

    /**
	 * Returns the typeId.
	 * @return the typeId
	 */
    public int getTypeId() {
        return typeId;
    }

    /**
	 * Set the typeId.
	 * @param typeId the typeId to set
	 */
    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    /**
	 * Returns the actions.
	 * @return the actions
	 */
    public List<Action> getActions() {
        return actions;
    }

    /**
	 * Set the actions.
	 * @param actions the actions to set
	 */
    public void setActions(List<Action> actions) {
        this.actions = actions;
    }

    /**
	 * Returns the styleSheet.
	 * @return the styleSheet
	 */
    public String getStyleSheet() {
        return styleSheet;
    }

    /**
	 * Set the styleSheet.
	 * @param styleSheet the styleSheet to set
	 */
    public void setStyleSheet(String styleSheet) {
        this.styleSheet = styleSheet;
    }

    /**
	 * Returns the viewTypeId.
	 * @return the viewTypeId
	 */
    public int getViewTypeId() {
        return viewTypeId;
    }

    /**
	 * Set the viewTypeId.
	 * @param viewTypeId the viewTypeId to set
	 */
    public void setViewTypeId(int viewTypeId) {
        this.viewTypeId = viewTypeId;
    }

    /**
	 * Returns the viewType.
	 * @return the viewType
	 */
    public ViewType getViewType() {
        return viewType;
    }

    /**
	 * Set the viewType.
	 * @param viewType the viewType to set
	 */
    public void setViewType(ViewType viewType) {
        this.viewType = viewType;
        if (viewType != null) {
            this.viewTypeId = viewType.getViewTypeId();
        }
    }

    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof View)) return false;
        final View bean = (View) other;
        if (!(bean.getTypeId() == getTypeId())) return false;
        if (bean.getViewType() == null && getViewType() != null) return false;
        if (bean.getViewType() != null && getViewType() == null) return false;
        return (bean.getViewType().getViewTypeId() == getViewType().getViewTypeId());
    }

    public int hashCode() {
        int result;
        result = getClass().getName().hashCode();
        result = 29 * result + getTypeId();
        if (getViewType() != null) {
            result = 29 * result + (getViewType().getViewTypeId());
        }
        return result;
    }
}
