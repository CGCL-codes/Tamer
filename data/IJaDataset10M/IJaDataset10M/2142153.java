package ru.mcfr.oxygen.framework.extensions;

import org.apache.log4j.Logger;
import ro.sync.ecss.extensions.api.AuthorDocumentController;
import ro.sync.ecss.extensions.api.AuthorOperationException;
import ro.sync.ecss.extensions.api.AuthorTableColumnWidthProvider;
import ro.sync.ecss.extensions.api.WidthRepresentation;
import ro.sync.ecss.extensions.api.node.AttrValue;
import ro.sync.ecss.extensions.api.node.AuthorElement;
import ro.sync.ecss.extensions.api.node.AuthorNode;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class TableColumnWidthProvider implements AuthorTableColumnWidthProvider {

    private static final String ELEMENT_NAME_COLGROUP = "группировка_колонок";

    private static final String ELEMENT_NAME_COL = "колонка";

    private static final String ELEMENT_NAME_THEAD = "thead";

    private static final String ATTR_NAME_THEAD = "тип_строки";

    private static final String ATTR_VALUE_THEAD = "шапка";

    private static final String ATTR_VALUE_TFOOT = "подвал";

    private static final String ELEMENT_NAME_TFOOT = "tfoot";

    private static final String ELEMENT_NAME_TBODY = "tbody";

    private static final String ATTR_NAME_SPAN = "colspan";

    private static final String ATTR_NAME_WIDTH = "width";

    private static final String CELL_NAME = "ячейка";

    private static final String ROW_NAME = "строка";

    private static final String HTML_HEADER_CELL_NAME = "th";

    private static Logger logger = Logger.getLogger(TableColumnWidthProvider.class.getName());

    private List<WidthRepresentation> colWidthSpecs = new ArrayList<WidthRepresentation>();

    private AuthorElement tableElement;

    public Integer getColSpan(AuthorElement cellElement) {
        Integer colspan = null;
        AttrValue attrValue = cellElement.getAttribute("colspan");
        if (attrValue != null) {
            try {
                int value = Integer.parseInt(attrValue.getValue());
                colspan = Integer.valueOf(Math.max(value, 1));
            } catch (NumberFormatException nfe) {
            }
        }
        return colspan;
    }

    public Integer getRowSpan(AuthorElement cellElement) {
        Integer rowspan = null;
        AttrValue attrValue = cellElement.getAttribute("rowspan");
        if (attrValue != null) {
            try {
                int value = Integer.parseInt(attrValue.getValue());
                rowspan = Integer.valueOf(Math.max(value, 1));
            } catch (NumberFormatException nfe) {
            }
        }
        return rowspan;
    }

    public void init(AuthorElement tableElement) {
        this.tableElement = tableElement;
        AuthorElement[] colGroupChildren = tableElement.getElementsByLocalName(ELEMENT_NAME_COLGROUP);
        if (colGroupChildren != null && colGroupChildren.length > 0) {
            for (int i = 0; i < colGroupChildren.length; i++) {
                AuthorElement child = colGroupChildren[i];
                AttrValue attrValue = child.getAttribute(ATTR_NAME_SPAN);
                int colgroupSpan = 1;
                if (attrValue != null) {
                    try {
                        colgroupSpan = Integer.parseInt(attrValue.getValue());
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                }
                attrValue = child.getAttribute(ATTR_NAME_WIDTH);
                String colgroupWidth = null;
                if (attrValue != null) {
                    colgroupWidth = attrValue.getValue();
                }
                List colgroupChildren = child.getContentNodes();
                for (Iterator iterator2 = colgroupChildren.iterator(); iterator2.hasNext(); ) {
                    AuthorNode cgChildNode = (AuthorNode) iterator2.next();
                    if (cgChildNode instanceof AuthorElement) {
                        AuthorElement cgChild = (AuthorElement) cgChildNode;
                        if (ELEMENT_NAME_COL.equals(cgChild.getLocalName())) {
                            colgroupSpan = -1;
                            AttrValue colWidthAttribute = cgChild.getAttribute(ATTR_NAME_WIDTH);
                            String colWidth = null;
                            if (colWidthAttribute != null) {
                                colWidth = colWidthAttribute.getValue();
                            } else if (colgroupWidth != null) {
                                colWidth = colgroupWidth;
                            }
                            AttrValue colSpanAttribute = cgChild.getAttribute(ATTR_NAME_SPAN);
                            int colSpan = 1;
                            if (colSpanAttribute != null) {
                                try {
                                    colSpan = Integer.parseInt(colSpanAttribute.getValue());
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                            }
                            for (int j = 0; j < colSpan; j++) {
                                colWidthSpecs.add(new WidthRepresentation(colWidth, true));
                            }
                        }
                    }
                }
                if (colgroupSpan > 0) {
                    for (int j = 0; j < colgroupSpan; j++) {
                        colWidthSpecs.add(new WidthRepresentation(colgroupWidth, true));
                    }
                }
            }
        }
    }

    public String getDescription() {
        return "Provides information about cells in HTML tables";
    }

    public boolean hasColumnSpecifications(AuthorElement tableElement) {
        return true;
    }

    public List<WidthRepresentation> getCellWidth(AuthorElement cellElement, int colNumberStart, int colSpan) {
        List<WidthRepresentation> toReturn = null;
        int size = colWidthSpecs.size();
        if (size >= colNumberStart && size >= colNumberStart + colSpan) {
            toReturn = new ArrayList<WidthRepresentation>(colSpan);
            for (int i = colNumberStart; i < colNumberStart + colSpan; i++) {
                toReturn.add(colWidthSpecs.get(i));
            }
        }
        return toReturn;
    }

    public void commitColumnWidthModifications(AuthorDocumentController authorDocumentController, WidthRepresentation[] colWidths, String tableCellsTagName) throws AuthorOperationException {
        if (CELL_NAME.equals(tableCellsTagName) || HTML_HEADER_CELL_NAME.equals(tableCellsTagName)) {
            AuthorElement[] colGroupChildren = tableElement.getElementsByLocalName(ELEMENT_NAME_COLGROUP);
            if (colGroupChildren != null && colGroupChildren.length > 0) {
                for (int i = 0; i < colGroupChildren.length; i++) {
                    AuthorElement child = colGroupChildren[i];
                    authorDocumentController.deleteNode(child);
                }
            } else {
                AuthorElement[] colChildren = tableElement.getElementsByLocalName(ELEMENT_NAME_COL);
                if (colChildren != null && colChildren.length > 0) {
                    for (int i = 0; i < colChildren.length; i++) {
                        AuthorElement colChild = colChildren[i];
                        authorDocumentController.deleteNode(colChild);
                    }
                }
            }
            if (colWidths != null && authorDocumentController != null && tableElement != null) {
                String xmlFragment = createXMLFragment(colWidths);
                int offset = getInsertColsOffset();
                if (offset == -1) {
                    throw new AuthorOperationException("No valid offset to insert the columns width specification.");
                }
                authorDocumentController.insertXMLFragment(xmlFragment, offset);
            }
        }
    }

    private int getInsertColsOffset() {
        int toReturn = -1;
        AuthorElement[] thead = tableElement.getElementsByLocalName(ELEMENT_NAME_THEAD);
        if (thead != null && thead.length > 0) {
            toReturn = thead[0].getStartOffset();
        } else {
            AuthorElement[] tbody = tableElement.getElementsByLocalName(ELEMENT_NAME_TBODY);
            if (tbody != null && tbody.length > 0) {
                toReturn = tbody[0].getStartOffset();
            } else {
                AuthorElement[] tr = tableElement.getElementsByLocalName(ROW_NAME);
                if (tr != null && tr.length > 0) {
                    toReturn = tr[0].getStartOffset();
                } else {
                    AuthorElement[] tfoot = tableElement.getElementsByLocalName(ELEMENT_NAME_TFOOT);
                    if (tfoot != null && tfoot.length > 0) {
                        toReturn = tfoot[0].getStartOffset();
                    }
                }
            }
        }
        return toReturn;
    }

    private String createXMLFragment(WidthRepresentation[] widthRepresentations) {
        StringBuffer fragment = new StringBuffer();
        String ns = tableElement.getNamespace();
        fragment.append("<" + ELEMENT_NAME_COLGROUP + " id=\"\">");
        for (int i = 0; i < widthRepresentations.length; i++) {
            WidthRepresentation width = widthRepresentations[i];
            fragment.append("<" + ELEMENT_NAME_COL + " id=\"\"");
            String strRepresentation = width.getWidthRepresentation();
            if (strRepresentation != null) {
                fragment.append(" width=\"" + width.getWidthRepresentation() + "\"");
            }
            if (ns != null && ns.length() > 0) {
                fragment.append(" xmlns=\"" + ns + "\"");
            }
            fragment.append("/>");
        }
        fragment.append("</" + ELEMENT_NAME_COLGROUP + ">");
        return fragment.toString();
    }

    public void commitTableWidthModification(AuthorDocumentController authorDocumentController, int newTableWidth, String tableCellsTagName) throws AuthorOperationException {
    }

    public WidthRepresentation getTableWidth(String tableCellsTagName) {
        WidthRepresentation toReturn = null;
        if (CELL_NAME.equals(tableCellsTagName) || HTML_HEADER_CELL_NAME.equals(tableCellsTagName)) {
            toReturn = getTableWidth();
        }
        return toReturn;
    }

    private WidthRepresentation getTableWidth() {
        WidthRepresentation toReturn = null;
        if (tableElement != null) {
            AttrValue widthAttr = tableElement.getAttribute(ATTR_NAME_WIDTH);
            if (widthAttr != null) {
                String width = widthAttr.getValue();
                if (width != null) {
                    toReturn = new WidthRepresentation(width, true);
                }
            }
        }
        return toReturn;
    }

    public boolean isTableAcceptingWidth(String tableCellsTagName) {
        return CELL_NAME.equals(tableCellsTagName) || HTML_HEADER_CELL_NAME.equals(tableCellsTagName);
    }

    public boolean isTableAndColumnsResizable(String tableCellsTagName) {
        return CELL_NAME.equals(tableCellsTagName) || HTML_HEADER_CELL_NAME.equals(tableCellsTagName);
    }

    public boolean isAcceptingFixedColumnWidths(String tableCellsTagName) {
        return CELL_NAME.equals(tableCellsTagName) || HTML_HEADER_CELL_NAME.equals(tableCellsTagName);
    }

    public boolean isAcceptingPercentageColumnWidths(String tableCellsTagName) {
        return CELL_NAME.equals(tableCellsTagName) || HTML_HEADER_CELL_NAME.equals(tableCellsTagName);
    }

    public boolean isAcceptingProportionalColumnWidths(String tableCellsTagName) {
        return CELL_NAME.equals(tableCellsTagName) || HTML_HEADER_CELL_NAME.equals(tableCellsTagName);
    }
}
