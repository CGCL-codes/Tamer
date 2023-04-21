package edu.chop.bic.cnv.domain;

import org.apache.wicket.markup.html.border.Border;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.ajax.markup.html.repeater.data.sort.AjaxFallbackOrderByLink;
import org.apache.wicket.ajax.IAjaxCallDecorator;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.link.PageLink;
import org.apache.wicket.markup.html.link.Link;
import org.apache.wicket.markup.html.link.BookmarkablePageLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.Component;
import edu.chop.bic.cnv.ui.Help;

/**
 * Ajaxified version of {@link org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder}
 * 
 * @see org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder
 * 
 * @since 1.2.1
 * 
 * @author Igor Vaynberg (ivaynberg)
 * 
 */
public abstract class AjaxFallbackOrderByBorderWithTriangles extends Border {

    private static final long serialVersionUID = 1L;

    /**
	 * Constructor
	 * 
	 * @param id
	 * @param property
	 * @param stateLocator
	 */
    public AjaxFallbackOrderByBorderWithTriangles(String id, String property, ISortStateLocator stateLocator) {
        this(id, property, stateLocator, AjaxFallbackOrderByLink.DefaultCssProvider.getInstance(), null);
    }

    /**
	 * Constructor
	 * 
	 * @param id
	 * @param property
	 * @param stateLocator
	 * @param cssProvider
	 */
    public AjaxFallbackOrderByBorderWithTriangles(String id, String property, ISortStateLocator stateLocator, AjaxFallbackOrderByLink.ICssProvider cssProvider) {
        this(id, property, stateLocator, cssProvider, null);
    }

    /**
	 * Constructor
	 * 
	 * @param id
	 * @param property
	 * @param stateLocator
	 * @param decorator
	 */
    public AjaxFallbackOrderByBorderWithTriangles(String id, String property, ISortStateLocator stateLocator, IAjaxCallDecorator decorator) {
        this(id, property, stateLocator, AjaxFallbackOrderByLink.DefaultCssProvider.getInstance(), decorator);
    }

    /**
	 * Constructor
	 * 
	 * @param id
	 * @param property
	 * @param stateLocator
	 * @param cssProvider
	 * @param decorator
	 */
    public AjaxFallbackOrderByBorderWithTriangles(String id, String property, ISortStateLocator stateLocator, AjaxFallbackOrderByLink.ICssProvider cssProvider, final IAjaxCallDecorator decorator) {
        super(id);
        Link helpLink = new BookmarkablePageLink("helpLink", Help.class);
        if (getSortorderString(property).equals("Type")) {
            Component variationTypeColumn = new WebMarkupContainer("variationTypeColumn").setOutputMarkupId(true);
            variationTypeColumn.setMarkupId("variation_type_column");
            helpLink.setAnchor(variationTypeColumn);
        } else if (getSortorderString(property).equals("Start")) {
            Component startColumn = new WebMarkupContainer("startColumn").setOutputMarkupId(true);
            startColumn.setMarkupId("start_column");
            helpLink.setAnchor(startColumn);
        } else if (getSortorderString(property).equals("End")) {
            Component endColumn = new WebMarkupContainer("endColumn").setOutputMarkupId(true);
            endColumn.setMarkupId("end_column");
            helpLink.setAnchor(endColumn);
        } else if (getSortorderString(property).equals("SNPs")) {
            Component snpsColumn = new WebMarkupContainer("snpsColumn").setOutputMarkupId(true);
            snpsColumn.setMarkupId("snps_column");
            helpLink.setAnchor(snpsColumn);
        } else if (getSortorderString(property).equals("Size")) {
            Component sizeColumn = new WebMarkupContainer("sizeColumn").setOutputMarkupId(true);
            sizeColumn.setMarkupId("seg_size_column");
            helpLink.setAnchor(sizeColumn);
        } else if (getSortorderString(property).equals("Frequency")) {
            Component frequencyColumn = new WebMarkupContainer("frequencyColumn").setOutputMarkupId(true);
            frequencyColumn.setMarkupId("frequency_column");
            helpLink.setAnchor(frequencyColumn);
        } else if (getSortorderString(property).equals("Sample")) {
            Component sampleColumn = new WebMarkupContainer("sampleColumn").setOutputMarkupId(true);
            sampleColumn.setMarkupId("sample_column");
            helpLink.setAnchor(sampleColumn);
        }
        add(helpLink);
        helpLink.add(new Label("helpLabel", getSortorderString(property)));
        AjaxFallbackOrderByLink link = new AjaxFallbackOrderByLink("orderByLink", property, stateLocator, cssProvider, decorator) {

            private static final long serialVersionUID = 1L;

            protected void onSortChanged() {
                AjaxFallbackOrderByBorderWithTriangles.this.onSortChanged();
            }

            protected void onAjaxClick(AjaxRequestTarget target) {
                AjaxFallbackOrderByBorderWithTriangles.this.onAjaxClick(target);
            }
        };
        add(link);
        add(new AjaxFallbackOrderByLink.CssModifier(link, cssProvider));
        link.add(getBodyContainer());
    }

    /**
	 * This method is a hook for subclasses to perform an action after sort has changed
	 */
    protected void onSortChanged() {
    }

    protected abstract void onAjaxClick(AjaxRequestTarget target);

    public String getSortorderString(String sortorderObjectString) {
        String headerString = "";
        if (sortorderObjectString.equals("locStart")) {
            headerString = "Start";
        } else if (sortorderObjectString.equals("locEnd")) {
            headerString = "End";
        } else if (sortorderObjectString.equals("band")) {
            headerString = "Band";
        } else if (sortorderObjectString.equals("cnvType")) {
            headerString = "Type";
        } else if (sortorderObjectString.equals("numMark")) {
            headerString = "SNPs";
        } else if (sortorderObjectString.equals("frequency")) {
            headerString = "Frequency";
        } else if (sortorderObjectString.equals("segSize")) {
            headerString = "Size";
        } else {
            headerString = sortorderObjectString;
        }
        return headerString;
    }
}
