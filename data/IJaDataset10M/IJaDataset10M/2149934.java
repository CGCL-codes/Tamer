package net.community.chest.jfree.jfreechart.data.general;

import org.jfree.data.general.DatasetGroup;
import org.w3c.dom.Element;

/**
 * <P>Copyright 2008 as per GPLv2</P>
 *
 * @author Lyor G.
 * @since Jan 27, 2009 2:36:41 PM
 */
public class BaseDatasetGroup extends DatasetGroup {

    /**
	 * 
	 */
    private static final long serialVersionUID = 432611704449414651L;

    public BaseDatasetGroup() {
        super();
    }

    public BaseDatasetGroup(String id) {
        super(id);
    }

    public static final String ID_ATTR = "id";

    public BaseDatasetGroup(Element elem) {
        this(elem.getAttribute(ID_ATTR));
    }
}
