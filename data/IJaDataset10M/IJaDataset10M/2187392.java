package edu.chop.bic.cnv.domain;

import edu.chop.bic.cnv.ui.Search;
import edu.chop.bic.cnv.ui.CnvFeedback;
import edu.chop.bic.cnv.ui.Help;
import org.apache.wicket.AttributeModifier;
import org.apache.wicket.PageParameters;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.image.ContextImage;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.LoadableDetachableModel;

public class CnvHeaderToolbar extends AbstractToolbar {

    private static final long serialVersionUID = 1L;

    private ISortStateLocator stateLocator;

    private CnvFeedback savedSearchFeedback;

    private WebMarkupContainer sortorderRow;

    /**
     * Constructor
     *
     * @param table        data table this toolbar will be attached to
     * @param stateLocator locator for the ISortState implementation used by sortable headers
     */
    public CnvHeaderToolbar(DataTable table, ISortStateLocator stateLocator, PageParameters params, CnvFeedback savedSearchFeedback, WebMarkupContainer sortorderRow) {
        super(table);
        this.stateLocator = stateLocator;
        this.savedSearchFeedback = savedSearchFeedback;
        this.sortorderRow = sortorderRow;
        RepeatingView headers = new RepeatingView("headers");
        add(headers);
        final IColumn[] columns = table.getColumns();
        for (int i = 0; i < columns.length; i++) {
            final IColumn column = columns[i];
            WebMarkupContainer item = new WebMarkupContainer(headers.newChildId());
            headers.add(item);
            WebMarkupContainer header = null;
            if (column.isSortable()) {
                header = newSortableHeader("header", column.getSortProperty(), getStateLocator(), params, Search.class);
            } else {
                header = new WebMarkupContainer("header");
            }
            Link labelLink = new BookmarkablePageLink("labelLink", Help.class) {

                public boolean isVisible() {
                    return !column.isSortable();
                }
            };
            if (column.getHeader("label").getModelObjectAsString().equals("Type")) {
                header.add(new AttributeModifier("style", true, new Model("text-align: right")));
            } else if (column.getHeader("label").getModelObjectAsString().equals("Ethnicity")) {
                header.add(new AttributeModifier("style", true, new Model("text-align: right")));
                Component ethnicityColumn = new WebMarkupContainer("ethnicityColumn").setOutputMarkupId(true);
                ethnicityColumn.setMarkupId("ethnicity_column");
                labelLink.setAnchor(ethnicityColumn);
            } else if (column.getHeader("label").getModelObjectAsString().equals("Band")) {
                header.add(new AttributeModifier("style", true, new Model("text-align: right")));
                Component bandColumn = new WebMarkupContainer("bandColumn").setOutputMarkupId(true);
                bandColumn.setMarkupId("band_column");
                labelLink.setAnchor(bandColumn);
            } else if (column.getHeader("label").getModelObjectAsString().equals("Start")) {
                header.add(new AttributeModifier("style", true, new Model("text-align: right")));
            } else if (column.getHeader("label").getModelObjectAsString().equals("End")) {
                header.add(new AttributeModifier("style", true, new Model("text-align: right")));
            } else if (column.getHeader("label").getModelObjectAsString().equals("SNPs")) {
                header.add(new AttributeModifier("style", true, new Model("text-align: right")));
            } else if (column.getHeader("label").getModelObjectAsString().equals("segSize")) {
                header.add(new AttributeModifier("style", true, new Model("text-align: right")));
            } else if (column.getHeader("label").getModelObjectAsString().equals("CNVs")) {
                header.add(new AttributeModifier("style", true, new Model("text-align: center")));
                Component cnvColumn = new WebMarkupContainer("cnvColumn").setOutputMarkupId(true);
                cnvColumn.setMarkupId("cnv_column");
                labelLink.setAnchor(cnvColumn);
            } else if (column.getHeader("label").getModelObjectAsString().equals("CNVR")) {
                header.add(new AttributeModifier("style", true, new Model("text-align: center")));
                Component cnvrColumn = new WebMarkupContainer("cnvrColumn").setOutputMarkupId(true);
                cnvrColumn.setMarkupId("cnvr_column");
                labelLink.setAnchor(cnvrColumn);
            } else if (column.getHeader("label").getModelObjectAsString().equals("CNV Blocks")) {
                header.add(new AttributeModifier("style", true, new Model("text-align: center")));
                Component cnvblockColumn = new WebMarkupContainer("cnvblockColumn").setOutputMarkupId(true);
                cnvblockColumn.setMarkupId("cnv_blocks_column");
                labelLink.setAnchor(cnvblockColumn);
            } else if (column.getHeader("label").getModelObjectAsString().equals("DGV CNVs")) {
                header.add(new AttributeModifier("style", true, new Model("text-align: center")));
                Component dgvColumn = new WebMarkupContainer("dgvColumn").setOutputMarkupId(true);
                dgvColumn.setMarkupId("dgv_column");
                labelLink.setAnchor(dgvColumn);
            } else if (column.getHeader("label").getModelObjectAsString().equals("UCSC")) {
                header.add(new AttributeModifier("style", true, new Model("text-align: center")));
                Component ucscColumn = new WebMarkupContainer("ucscColumn").setOutputMarkupId(true);
                ucscColumn.setMarkupId("ucsc_column");
                labelLink.setAnchor(ucscColumn);
            } else if (column.getHeader("label").getModelObjectAsString().equals("Content")) {
                header.add(new AttributeModifier("style", true, new Model("text-align: center")));
                Component contentColumn = new WebMarkupContainer("contentColumn").setOutputMarkupId(true);
                contentColumn.setMarkupId("content_column");
                labelLink.setAnchor(contentColumn);
            } else if (column.getHeader("label").getModelObjectAsString().equals("Frequency")) {
                header.add(new AttributeModifier("style", true, new Model("text-align: right")));
            } else if (column.getHeader("label").getModelObjectAsString().equals("segMean")) {
                header.add(new AttributeModifier("style", true, new Model("text-align: right")));
                Component segMeanColumn = new WebMarkupContainer("segMeanColumn").setOutputMarkupId(true);
                segMeanColumn.setMarkupId("seg_mean_column");
                labelLink.setAnchor(segMeanColumn);
            } else if (column.getHeader("label").getModelObjectAsString().equals("GAD")) {
                header.add(new AttributeModifier("style", true, new Model("text-align: center")));
                Component gadColumn = new WebMarkupContainer("gadColumn").setOutputMarkupId(true);
                gadColumn.setMarkupId("gad_column");
                labelLink.setAnchor(gadColumn);
            } else if (column.getHeader("label").getModelObjectAsString().equals("Sample")) {
                header.add(new AttributeModifier("style", true, new Model("text-align: right")));
                Component sampleColumn = new WebMarkupContainer("sampleColumn").setOutputMarkupId(true);
                sampleColumn.setMarkupId("sample_column");
                labelLink.setAnchor(sampleColumn);
            } else if (column.getHeader("label").getModelObjectAsString().equals("#")) {
                header.add(new AttributeModifier("style", true, new Model("text-align: right")));
            } else if (!column.getHeader("label").getModelObjectAsString().equals("")) {
                header.add(new AttributeModifier("style", true, new Model("text-align: center")));
                Component customTrackColumn = new WebMarkupContainer("customTrackColumn").setOutputMarkupId(true);
                customTrackColumn.setMarkupId("custom_track_column");
                labelLink.setAnchor(customTrackColumn);
            } else {
                header.add(new AttributeModifier("style", true, new Model("text-align: center")));
            }
            item.add(header);
            item.setRenderBodyOnly(true);
            header.add(new ContextImage("triangle", new LoadableDetachableModel() {

                protected Object load() {
                    if (column.isSortable()) {
                        String sortorder = column.getSortProperty();
                        if (getStateLocator().getSortState().toString().contains(sortorder)) {
                            if (!(sortorder.equals("numMark") || sortorder.equals("segSize") || sortorder.equals("frequency"))) {
                                return getStateLocator().getSortState().toString().contains("ascending=true") ? "img/ascending.jpg" : "img/descending.jpg";
                            } else {
                                return getStateLocator().getSortState().toString().contains("ascending=false") ? "img/ascending.jpg" : "img/descending.jpg";
                            }
                        } else {
                            return "img/closed.jpg";
                        }
                    }
                    return "img/closed.jpg";
                }

                public boolean getOutputMarkupId() {
                    return true;
                }
            }) {

                public boolean isVisible() {
                    return column.isSortable();
                }
            });
            String headerObjectString = column.getHeader("label").getModelObjectAsString();
            header.add(labelLink);
            labelLink.add(new Label("label", headerObjectString));
        }
    }

    /**
     * Factory method for sortable header components. A sortable header component must have id of
     * <code>headerId</code> and conform to markup specified in <code>HeadersToolbar.html</code>
     *
     * @param headerId header component id
     * @param property property this header represents
     * @param locator  sort state locator
     * @return created header component
     */
    protected WebMarkupContainer newSortableHeader(String headerId, String property, ISortStateLocator locator, PageParameters params, Class page) {
        return new AjaxFallbackOrderByBorderWithTriangles(headerId, property, locator) {

            private static final long serialVersionUID = 1L;

            protected void onAjaxClick(AjaxRequestTarget target) {
                getTable().setCurrentPage(0);
                ((CnvDataTable) getTable()).setHeaderClicked(true);
                target.addComponent(getTable());
                savedSearchFeedback.clear();
                target.addComponent(savedSearchFeedback);
                target.addComponent(sortorderRow);
            }

            protected void onSortChanged() {
                getTable().setCurrentPage(0);
                ((CnvDataTable) getTable()).setHeaderClicked(true);
            }
        };
    }

    public ISortStateLocator getStateLocator() {
        return stateLocator;
    }

    public void setStateLocator(ISortStateLocator stateLocator) {
        this.stateLocator = stateLocator;
    }
}
