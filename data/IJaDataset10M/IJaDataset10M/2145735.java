package net.sf.myway.edit.ui.views;

import java.util.List;

/**
 * @author Andreas Beckers
 * @version $Revision: 1.1 $
 */
public class TreeRoot {

    private List<?> _children;

    /**
	 * @return the children
	 */
    public List<?> getChildren() {
        return _children;
    }

    /**
	 * @param children the children to set
	 */
    public void setChildren(List<?> children) {
        _children = children;
    }
}
