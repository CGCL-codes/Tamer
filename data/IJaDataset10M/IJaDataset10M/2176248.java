package com.sitescape.team.taglib;

import java.util.List;

/**
 * <a href="ParamAncestorTag.java.html"><b><i>View Source</i></b></a>
 *
 * @author  Brian Wing Shun Chan
 * @version $Revision: 1.5 $
 *
 */
public interface SlidingTableRowAncestorTag {

    public void addRow(String id, List columns, Boolean headerRow);
}
