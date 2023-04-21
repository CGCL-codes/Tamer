package edu.chop.bic.cnv.ui.links;

import org.apache.wicket.model.IModel;
import org.apache.wicket.markup.html.link.ExternalLink;
import org.apache.wicket.markup.html.link.PopupSettings;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.ComponentTag;

public class FableLink extends Panel {

    public FableLink(String name, String symbol) {
        super(name);
        String external = "http://fable.chop.edu/index.jsp?query=";
        external += symbol;
        external += "&submitbutton=+Go+&lucene=false&geneListerQuery=&";
        external += "submithit=true&aliasRaw=&normalization=true&sortOrder=RELEVANCE&intlimit=25";
        ExternalLink link = new ExternalLink(name, external, "F") {

            @Override
            protected void onComponentTag(ComponentTag tag) {
                super.onComponentTag(tag);
                tag.put("title", "Search 'Fable' for articles about this gene");
                tag.put("style", "font-style:italic");
            }

            ;
        };
        add(link);
    }
}
