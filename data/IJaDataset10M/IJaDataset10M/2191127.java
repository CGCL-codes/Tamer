package com.sparkit.extracta.data.table;

import java.util.*;
import org.w3c.dom.*;
import com.sparkit.extracta.*;

/**
 * This is default header-excluded table data provider. It defines a createTableData
 * to return a default table data object without header row defined.
 *
 * @version 1.0
 * @author Bostjan Vester
 * @author Dejan Pazin
 * @author Dominik Roblek
 */
public class DefaultNoHeaderTableDataProvider extends AbstractTableDataProvider {

    /**
   * This creates a default table data object with header row excluded in data. This is
   * used for simplified process of creating a custom data objects: one should only
   * extend this data provider and override the create data method to return an
   * appropriate data implementation - all other mechanisms (cache i.e.) are
   * still applicable.
   */
    public ITableData createTableData(Node rootNode, int nTableIndex) throws ExtractaException {
        Node tableNode = DomHelper.getLastSingleChild(rootNode, "table");
        if ((tableNode != null) && (nTableIndex == 0)) {
            return new DefaultTableData(tableNode, true);
        } else {
            List tableNodes = DomHelper.getLastChildren(rootNode, "table");
            return ((nTableIndex < tableNodes.size()) && (nTableIndex >= 0)) ? new DefaultTableData((Node) tableNodes.get(nTableIndex), true) : null;
        }
    }
}
