package net.sf.mareco.search.gui;

import java.util.Arrays;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.IChoiceRenderer;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

public class AdvancedSearchPanel extends Panel {

    private static final long serialVersionUID = 8558082879659549067L;

    private String query;

    private Packaging packaging;

    public AdvancedSearchPanel(String id) {
        super(id);
        PropertyModel dropDownModel = new PropertyModel(this, "packaging");
        PropertyModel queryModel = new PropertyModel(this, "query");
        add(new Label("q", queryModel));
        add(new Label("p", dropDownModel));
        Form form = new Form("form");
        add(form);
        form.add(new TextField("qInput", queryModel));
        form.add(new DropDownChoice("packaging", dropDownModel, Arrays.asList(Packaging.values()), new IChoiceRenderer() {

            private static final long serialVersionUID = 6773958379866880768L;

            @Override
            public Object getDisplayValue(Object object) {
                return ((Packaging) object).toString();
            }

            @Override
            public String getIdValue(Object object, int index) {
                return String.valueOf(index);
            }
        }));
    }

    public Packaging getPackaging() {
        return packaging;
    }

    public String getQuery() {
        return query;
    }

    public void setPackaging(Packaging packaging) {
        this.packaging = packaging;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public static enum Packaging {

        jar, war, ear, pom, bundle
    }
}
