package org.jcvi.vics.web.gwt.download.client.formatter;

import com.google.gwt.user.client.ui.*;
import org.jcvi.vics.web.gwt.common.client.core.BrowserDetector;
import org.jcvi.vics.web.gwt.common.client.panel.ClickableGrid;
import org.jcvi.vics.web.gwt.common.client.ui.HoverListener;
import org.jcvi.vics.web.gwt.common.client.ui.HoverStyleSetter;
import org.jcvi.vics.web.gwt.common.client.ui.imagebundles.ImageBundleFactory;
import org.jcvi.vics.web.gwt.common.client.ui.link.ActionLink;
import org.jcvi.vics.web.gwt.common.client.ui.table.SortableTable;
import org.jcvi.vics.web.gwt.common.client.ui.table.columns.TableColumn;
import org.jcvi.vics.web.gwt.common.client.ui.table.columns.TextColumn;
import org.jcvi.vics.web.gwt.download.client.model.Publication;
import java.util.Iterator;
import java.util.List;

/**
 * User: Lfoster
 * Date: Aug 18, 2006
 * Time: 5:36:00 PM
 * Formatter for publications.  Makes presentation-level code.
 * <p/>
 * TODO consider moving much of his code to the server side.  Server is better equipped to deal
 * with certain of the parsing issues, because it has no GWT constraints.
 */
public class PublicationFormatter extends BaseDownloadFormatter {

    private static final int AUTHOR_MIN_CHARS = 65;

    private static final int SUMMARY_MIN_CHARS = 1000;

    private static final int ABSTRACT_MIN_CHARS = 1000;

    private static final String EXPAND_DISPLAY = "expand all";

    private static final String COLLAPSE_DISPLAY = "collapse all";

    private static final int ABBREVIATED_TEXT_LENGTH = 30;

    public Panel getAuthors(Publication publication) {
        return common.getExpandablePromptedPanel("Authors", getFormattedAuthors(publication), "DownloadByPublicationAuthor", AUTHOR_MIN_CHARS);
    }

    public Panel getTitle(Publication publication) {
        HTMLPanel panel = new HTMLPanel(publication.getTitle());
        panel.setStyleName("DownloadByPublicationPublicationTitle");
        return panel;
    }

    public Panel getSummary(Publication publication) {
        return common.getExpandablePromptedPanel("Summary", publication.getSummary(), "DownloadByPublicationSummary", SUMMARY_MIN_CHARS);
    }

    public Panel getAbstract(Publication publication) {
        return common.getExpandablePromptedPanel("Abstract", publication.getAbstract(), "DownloadByPublicationAbstract", ABSTRACT_MIN_CHARS);
    }

    /**
     * Citation is title and abbreviated, et al author list.
     *
     * @param titleWidgets a List to accrue titles for resizing purposes
     * @return
     */
    public ClickableGrid getCitation(Publication publication, List titleWidgets) {
        ClickableGrid grid = new ClickableGrid(2, 2);
        grid.setCellSpacing(0);
        grid.setCellPadding(0);
        grid.getRowFormatter().setStyleName(0, "DownloadTabTopRow");
        grid.getRowFormatter().setStyleName(1, "DownloadTabMiddleRow");
        Image bulletImage;
        bulletImage = ImageBundleFactory.getControlImageBundle().getSquareBulletUnselectedImage().createImage();
        if (BrowserDetector.isIE()) bulletImage.setStyleName("DownloadPublicationBulletInTabIE"); else bulletImage.setStyleName("DownloadPublicationBulletInTab");
        grid.setWidget(0, 0, bulletImage);
        grid.getCellFormatter().setVerticalAlignment(0, 0, HasVerticalAlignment.ALIGN_TOP);
        HTML title = new HTML(publication.getTitle());
        title.setStyleName("DownloadPublicationTitleInTab");
        title.addMouseListener(new HoverStyleSetter(title, "DownloadPublicationTitleInTab", "DownloadPublicationTitleInTabHover", (HoverListener) null));
        titleWidgets.add(title);
        grid.setWidget(0, 1, title);
        HTML empty = new HTML("&nbsp;");
        empty.setStyleName("DownloadEmptyInTab");
        grid.setWidget(1, 0, empty);
        HTML authors = new HTML(abbreviateAuthors(publication));
        authors.setStyleName("DownloadPublicationAuthorsInTab");
        grid.setWidget(1, 1, authors);
        return grid;
    }

    /**
     * Data Files pane.  Helps users browse and download.
     */
    public Panel getDataFiles(VerticalPanel parentPanel, Publication publication, DownloadableDataNodeFilter filter, TreeListener dataFileEventListener) {
        HorizontalPanel expansionControlPanel = new HorizontalPanel();
        if (publication.getDataFiles() != null && publication.getDataFiles().size() > 0) {
            ActionLink expandLink = new ActionLink(EXPAND_DISPLAY);
            ActionLink collapseLink = new ActionLink(COLLAPSE_DISPLAY);
            expansionControlPanel.add(expandLink);
            expansionControlPanel.add(collapseLink);
            parentPanel.add(expansionControlPanel);
            List rootItems = new DataFileFormatter().addDataFileTrees(parentPanel, publication.getDataFiles(), filter, dataFileEventListener);
            ExpansionClickListener expander = new ExpansionClickListener(rootItems);
            expandLink.addClickListener(expander);
            collapseLink.addClickListener(expander);
        }
        return parentPanel;
    }

    public SortableTable getAllFiltersTable(List filterValues) {
        SortableTable table = new SortableTable();
        table.setEvenRowStyle("tableRowAll");
        table.setOddRowStyle("tableRowAll");
        TableColumn column = new TextColumn("Available Geographic Areas");
        table.addColumn(column);
        int oldRowCount = table.getRowCount();
        for (int i = 0; filterValues != null && i < filterValues.size(); i++) {
            String nextFilterValue = filterValues.get(i).toString();
            table.setValue(oldRowCount + i, 0, nextFilterValue);
        }
        return table;
    }

    /**
     * Filters that have been selected, are represented here, in this table.
     *
     * @return table to which to add filter strings.
     */
    public SortableTable getSelectedFiltersTable() {
        SortableTable table = new SortableTable();
        table.setEvenRowStyle("tableRowAll");
        table.setOddRowStyle("tableRowAll");
        TableColumn column = new TextColumn("Selected Geographic Areas");
        table.addColumn(column);
        return table;
    }

    /**
     * Create an "et al" version of author names.
     * <p/>
     * If 1 author, show it
     * If 2 authors, show both, comma-separated
     * Else show first 2 authors + "et al."
     */
    private String abbreviateAuthors(Publication publication) {
        List authors = publication.getAuthors();
        StringBuffer returnBuffer = new StringBuffer();
        if (authors.size() == 0) returnBuffer.append("&nbsp;"); else if (authors.size() == 1) returnBuffer.append(authors.get(0)); else if (authors.size() >= 2) {
            returnBuffer.append(authors.get(0)).append(", ").append(authors.get(1));
            if (authors.size() > 2) returnBuffer.append(", et al.");
        }
        if (returnBuffer.toString().length() <= ABBREVIATED_TEXT_LENGTH) return returnBuffer.toString(); else return returnBuffer.toString().substring(0, ABBREVIATED_TEXT_LENGTH) + "...";
    }

    private String getFormattedAuthors(Publication publication) {
        List authors = publication.getAuthors();
        StringBuffer buffer = new StringBuffer();
        if (authors == null || authors.size() == 0) buffer.append("Unknown"); else {
            for (int i = 0; i < authors.size() - 1; i++) {
                buffer.append((String) authors.get(i));
                buffer.append(", ");
            }
            buffer.append(authors.get(authors.size() - 1));
        }
        return buffer.toString();
    }

    /**
     * Listener to two expansion click listeners, that fully contract or fully expand a tree.
     */
    public class ExpansionClickListener implements ClickListener {

        private static final int EXPAND_ALL = 0;

        private static final int EXPAND_NONE = 1;

        private static final int EXPAND_LOCAL = 2;

        private List _treeItems;

        private int _expandAllState = EXPAND_NONE;

        public ExpansionClickListener(List treeItems) {
            _treeItems = treeItems;
        }

        public void setExpandStateNeutral() {
            _expandAllState = EXPAND_LOCAL;
        }

        public boolean isFullyExpanded() {
            return _expandAllState == EXPAND_ALL;
        }

        public boolean isFullyCollapsed() {
            return _expandAllState == EXPAND_NONE;
        }

        public void onClick(Widget w) {
            if (w.toString().indexOf(EXPAND_DISPLAY) != -1) {
                setExpansionState(true);
            } else if (w.toString().indexOf(COLLAPSE_DISPLAY) != -1) {
                setExpansionState(false);
            }
        }

        public void refresh() {
            if (_expandAllState == EXPAND_ALL) {
                setExpansionState(true);
            } else if (_expandAllState == EXPAND_NONE) {
                setExpansionState(false);
            }
        }

        public void setExpansionState(boolean expandState) {
            if (expandState) {
                _expandAllState = EXPAND_ALL;
            } else {
                _expandAllState = EXPAND_NONE;
            }
            for (Iterator it = _treeItems.iterator(); it.hasNext(); ) {
                TreeItem item = (TreeItem) it.next();
                recursiveStateChange(item, expandState);
            }
        }

        private void recursiveStateChange(TreeItem item, boolean newState) {
            item.setState(newState);
            for (int i = 0; i < item.getChildCount(); i++) {
                recursiveStateChange(item.getChild(i), newState);
            }
        }
    }

    /**
     * Listener here, will detect when the state of any item has changed, and inform the
     * expansion click listener, that no absolute (but rather a local) expansion state is
     * in effect.
     */
    public class LocalCollapseExpandListener implements TreeListener {

        private ExpansionClickListener _expander;

        public void setExpander(ExpansionClickListener expander) {
            _expander = expander;
        }

        public void onTreeItemSelected(TreeItem item) {
        }

        public void onTreeItemStateChanged(TreeItem item) {
            _expander.setExpandStateNeutral();
        }
    }
}
