package org.dcm4chee.web.war.tc;

import java.util.ArrayList;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.MultiLineLabel;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.Model;

/**
 * @author Bernhard Ableitinger <bernhard.ableitinger@agfa.com>
 * @version $Revision$ $Date$
 * @since May 27, 2011
 */
public class TCDetailsBibliographyTab extends Panel {

    private static final long serialVersionUID = 1L;

    public TCDetailsBibliographyTab(final String id) {
        super(id);
        ListView<String> list = new ListView<String>("bibliography-list", new Model<ArrayList<String>>() {

            private static final long serialVersionUID = 1L;

            @Override
            public ArrayList<String> getObject() {
                TCDetails o = getTCObject();
                ArrayList<String> biblio = o != null ? new ArrayList<String>(o.getBibliographicReferences()) : null;
                return biblio != null ? biblio : new ArrayList<String>(0);
            }
        }) {

            private static final long serialVersionUID = 1L;

            @Override
            protected void populateItem(final ListItem<String> item) {
                WebMarkupContainer wmc = new WebMarkupContainer("bibliography-separator") {

                    private static final long serialVersionUID = 1L;

                    @Override
                    public boolean isVisible() {
                        return item.getIndex() < getList().size() - 1;
                    }
                };
                item.add(new MultiLineLabel("bibliography-text", item.getModelObject()));
                item.add(wmc);
            }
        };
        add(list);
    }

    private TCDetails getTCObject() {
        return (TCDetails) getDefaultModelObject();
    }
}
