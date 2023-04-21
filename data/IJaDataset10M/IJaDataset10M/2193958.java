package edu.chop.bic.cnv.ui.links;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import java.util.List;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.ajax.AjaxEventBehavior;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.AttributeModifier;
import edu.chop.bic.cnv.ui.links.FableLink;
import edu.chop.bic.cnv.ui.links.EntrezLink;
import edu.chop.bic.cnv.ui.links.GeneLink;

public class ContentTable extends Panel {

    private boolean displayGenes = false;

    public ContentTable(String name, IModel model, final List contentList, boolean displayGenes) {
        super(name, model);
        this.displayGenes = displayGenes;
        final WebMarkupContainer container = new WebMarkupContainer("container") {

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("style", "cursor:pointer");
                tag.put("style", "float:right");
            }
        };
        container.setOutputMarkupId(true);
        container.add(new AjaxEventBehavior("onclick") {

            public void onEvent(AjaxRequestTarget target) {
                if (contentList != null && contentList.get(0) != null && contentList.get(0) != "-") {
                    if (isDisplayGenes()) {
                        setDisplayGenes(false);
                    } else {
                        setDisplayGenes(true);
                    }
                    target.addComponent(container);
                }
            }
        });
        add(container);
        String label = "";
        if (contentList != null && contentList.get(0) != null && contentList.get(0) != "-") {
            label += contentList.size() + " gene";
            if (contentList.size() > 1) {
                label += "s";
            }
        } else {
            label += "-";
        }
        final boolean noResults = label.equals("-");
        Label count = new Label("count", label) {

            public boolean isVisible() {
                if (isDisplayGenes() || noResults) {
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("style", "float:right");
            }
        };
        count.setOutputMarkupId(true);
        container.add(count);
        ListView contentListView = new ListView("content", contentList) {

            protected void populateItem(final ListItem listitem) {
                final String symbol = (String) listitem.getModelObject();
                listitem.add(new Label("symbol", symbol) {

                    @Override
                    protected void onComponentTag(ComponentTag tag) {
                        super.onComponentTag(tag);
                        tag.put("style", "font-weight:bold");
                        tag.put("style", "width:10px");
                    }
                });
                if (symbol.equals("-")) {
                    listitem.add(new Label("Fable", " "));
                    listitem.add(new Label("Entrez", " "));
                    listitem.add(new Label("Gene", " "));
                } else {
                    if (!symbol.startsWith("hsa")) listitem.add(new FableLink("Fable", listitem.getModel(), symbol)); else listitem.add(new Label("Fable", " "));
                    listitem.add(new EntrezLink("Entrez", listitem.getModel(), symbol));
                    listitem.add(new GeneLink("Gene", listitem.getModel(), symbol));
                }
            }

            public boolean isVisible() {
                if (noResults) return false;
                if (isDisplayGenes()) {
                    return true;
                } else {
                    return false;
                }
            }
        };
        contentListView.setOutputMarkupId(true).setOutputMarkupPlaceholderTag(true);
        container.add(contentListView);
        add(new Label("blankdash", new Model("&ndash;")) {

            public boolean isVisible() {
                return noResults;
            }
        }.setEscapeModelStrings(false).add(new AttributeModifier("style", true, new Model("align:center;text-align:center"))));
    }

    public boolean isDisplayGenes() {
        return displayGenes;
    }

    public void setDisplayGenes(boolean displayGenes) {
        this.displayGenes = displayGenes;
    }
}
