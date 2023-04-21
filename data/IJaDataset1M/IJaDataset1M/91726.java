package chapter06.section04;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.CheckBoxMultipleChoice;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.ListChoice;
import org.apache.wicket.markup.html.form.ListMultipleChoice;
import org.apache.wicket.markup.html.form.RadioChoice;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.util.value.ValueMap;

/**
 * @author dashorst
 */
@SuppressWarnings("unused")
public class SelectionComponentsPage extends WebPage {

    public SelectionComponentsPage() {
        class MilkType implements Serializable {

            private final long id;

            private final String name;

            public MilkType(long id, String name) {
                this.id = id;
                this.name = name;
            }
        }
        class Cheese implements Serializable {

            String category;

            List<String> milkTypes = new ArrayList<String>();

            MilkType milkType;

            public MilkType getMilkType() {
                return milkType;
            }

            public void setMilkType(MilkType milkType) {
                this.milkType = milkType;
            }

            public String getCategory() {
                return category;
            }

            public void setCategory(String category) {
                this.category = category;
            }

            public List<String> getMilkTypes() {
                return milkTypes;
            }

            public void setMilkTypes(List<String> milkType) {
                this.milkTypes = milkType;
            }
        }
        class MilkTypesModel extends LoadableDetachableModel {

            @Override
            protected Object load() {
                return Arrays.asList("Bison", "Camel", "Cow", "Goat", "Reindeer", "Sheep", "Yak");
            }
        }
        Cheese cheese = new Cheese();
        List<String> categories = Arrays.asList("Fresh", "Whey", "Goat or sheep", "Hard", "Blue vein");
        add(new ListChoice("category1", new PropertyModel(cheese, "category"), categories));
        cheese.setCategory("Blue vein");
        add(new DropDownChoice("category2", new PropertyModel(cheese, "category"), categories));
        add(new RadioChoice("category3", new PropertyModel(cheese, "category"), categories).setSuffix(" "));
        List<String> choices = Arrays.asList("Camel", "Cow", "Goat", "Reindeer", "Sheep", "Yak");
        add(new ListMultipleChoice("milkTypes1", new PropertyModel(cheese, "milkTypes"), choices));
        cheese.getMilkTypes().clear();
        cheese.getMilkTypes().add("Cow");
        cheese.getMilkTypes().add("Yak");
        add(new CheckBoxMultipleChoice("milkTypes2", new PropertyModel(cheese, "milkTypes"), choices).setSuffix(" "));
        List<MilkType> milktypes = Arrays.asList(new MilkType(1, "Camel"), new MilkType(2, "Cow"), new MilkType(3, "Goat"));
        add(new DropDownChoice("milkTypes3", new PropertyModel(cheese, "milkType"), milktypes, new ChoiceRenderer("name", "id")));
        class Customer implements Serializable {

            Boolean wantsspam;
        }
        Customer cust = new Customer();
        add(new CheckBox("wantsspam", new PropertyModel(cust, "wantsspam")));
        ValueMap searchPars = new ValueMap();
        searchPars.put("q", "");
        searchPars.put("milktypes", new ArrayList());
        Form form = new Form("form744", new CompoundPropertyModel(searchPars)) {

            @Override
            protected void onSubmit() {
            }
        };
        add(form);
        form.add(new TextField("q"));
        final WebMarkupContainer wmc = new WebMarkupContainer("wmc");
        wmc.setVisible(false);
        wmc.setOutputMarkupPlaceholderTag(true);
        form.add(wmc);
        form.add(new AjaxCheckBox("advanced", new PropertyModel(wmc, "visible")) {

            @Override
            protected void onUpdate(AjaxRequestTarget target) {
                target.addComponent(wmc);
            }
        });
        wmc.add(new CheckBoxMultipleChoice("milktypes", new MilkTypesModel()).setPrefix("").setSuffix(""));
    }
}
