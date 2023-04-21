package wicketrocks.visibility;

import java.util.Arrays;
import java.util.List;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.OnChangeAjaxBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxCheckBox;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.Radio;
import org.apache.wicket.markup.html.form.RadioGroup;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import core.console.ConsoleTool;
import entity.Entity;

/**
 * @author manuelbarzi
 * @version 20111201180314
 */
public class VisibilityPage extends WebPage {

    private Entity selectedEntity;

    private Boolean selected;

    public VisibilityPage() {
        final WebMarkupContainer container = new WebMarkupContainer("container");
        add(container);
        container.setOutputMarkupPlaceholderTag(true);
        container.setVisible(true);
        Form<Void> form = new Form<Void>("form");
        add(form);
        final RadioGroup<Boolean> group = new RadioGroup<Boolean>("group", new Model<Boolean>(container.isVisible()));
        form.add(group);
        group.add(new AjaxFormChoiceComponentUpdatingBehavior() {

            protected void onUpdate(AjaxRequestTarget target) {
                ConsoleTool.getInstance().writeLine(group.getModelObject());
                container.setVisible(group.getModelObject());
                target.addComponent(container);
            }
        });
        Radio<Boolean> radio1 = new Radio<Boolean>("radio1", new Model<Boolean>(Boolean.TRUE), group);
        group.add(radio1);
        Radio<Boolean> radio2 = new Radio<Boolean>("radio2", new Model<Boolean>(Boolean.FALSE), group);
        group.add(radio2);
        Form<Void> form2 = new Form<Void>("form2");
        add(form2);
        final AjaxCheckBox check = new AjaxCheckBox("check", new PropertyModel(this, "selected")) {

            protected void onUpdate(AjaxRequestTarget target) {
                ConsoleTool.getInstance().writeLine(selected);
            }
        };
        check.setOutputMarkupPlaceholderTag(true);
        check.setVisible(false);
        form2.add(check);
        List<Entity> list = Arrays.asList(new Entity[] { new Entity("coco"), new Entity("liso"), new Entity("pata"), new Entity("tero") });
        DropDownChoice<Entity> select = new DropDownChoice<Entity>("select", new PropertyModel<Entity>(this, "selectedEntity"), list);
        select.setNullValid(true);
        select.add(new OnChangeAjaxBehavior() {

            protected void onUpdate(AjaxRequestTarget target) {
                Entity selectedEntity = getSelectedEntity();
                ConsoleTool.getInstance().writeLine(selectedEntity);
                if (selectedEntity != null && selectedEntity.getId().equals("coco")) {
                    ConsoleTool.getInstance().writeLine("show it");
                    check.setVisible(true);
                } else {
                    check.setVisible(false);
                    setSelected(false);
                }
                target.addComponent(check);
            }
        });
        form2.add(select);
    }

    public void setSelectedEntity(Entity selected) {
        this.selectedEntity = selected;
    }

    public Entity getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelected(Boolean selected) {
        this.selected = selected;
    }

    public Boolean getSelected() {
        return selected;
    }
}
