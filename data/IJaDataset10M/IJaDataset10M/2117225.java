package leland.web.wicket.general;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import leland.dao.ClientManager;
import leland.domain.Address;
import org.apache.wicket.extensions.ajax.markup.html.autocomplete.AutoCompleteTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.RequiredTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.string.Strings;

/**
 * @author Radu Cugut
 */
public class AddressEditPanel extends EntityEditPanel {

    private final Address address;

    public AddressEditPanel(String id, IModel<Address> model, Form<Object> form) {
        super(id, form);
        this.address = model.getObject();
        this.add(new AutoCompleteTextField<String>("input-street", new PropertyModel<String>(address, "street")) {

            @Override
            protected Iterator<String> getChoices(String rawInput) {
                if (Strings.isEmpty(rawInput)) return Collections.EMPTY_LIST.iterator();
                String input = rawInput.toLowerCase();
                List<String> list = Arrays.asList(new String[] { "Calea Circumvalatiunii", "Vasile Lucaciu", "C.D. Loga" });
                List<String> choices = new ArrayList<String>(10);
                for (String s : list) {
                    if (choices.size() == 10) break;
                    String a = s.toLowerCase();
                    if (a.startsWith(input) || a.contains(" " + input) || a.contains("-" + input) || a.contains("." + input)) choices.add(s);
                }
                return choices.iterator();
            }
        }.setRequired(true).setOutputMarkupPlaceholderTag(true));
        this.add(new RequiredTextField<String>("input-number", new PropertyModel<String>(address, "number")).setOutputMarkupPlaceholderTag(true));
        this.add(new TextField<String>("input-detailsOfStreetAddress", new PropertyModel<String>(address, "detailsOfStreetAddress")).setOutputMarkupPlaceholderTag(true));
        this.add(new RequiredTextField<String>("input-city", new PropertyModel<String>(address, "city")).setOutputMarkupPlaceholderTag(true));
        this.add(new RequiredTextField<String>("input-county", new PropertyModel<String>(address, "county")).setOutputMarkupPlaceholderTag(true));
        this.add(new TextField<String>("input-zip", new PropertyModel<String>(address, "zip")).setOutputMarkupPlaceholderTag(true));
        this.add(new TextField<String>("input-otherInfo", new PropertyModel<String>(address, "otherInfo")).setOutputMarkupPlaceholderTag(true));
    }
}
