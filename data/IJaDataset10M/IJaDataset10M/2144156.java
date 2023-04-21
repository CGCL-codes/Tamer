package org.kwantu.zakwantu;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxButton;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.CheckBox;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.markup.html.panel.Fragment;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.AbstractReadOnlyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.kwantu.zakwantu.util.RegressionTestingUtil;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import org.apache.wicket.markup.html.form.Check;
import org.apache.wicket.markup.html.form.CheckGroup;
import org.apache.wicket.markup.html.form.CheckGroupSelector;
import org.apache.wicket.markup.html.list.ListView;

/**
 * Support button arguments.
 * Adapted from an example by Alistair Maw
 */
public abstract class ButtonArgPanel extends Panel implements RegressionTestable {

    private static final Log LOG = LogFactory.getLog(ButtonArgPanel.class);

    private FeedbackPanel feedback;

    private int regressionTestingId;

    public ButtonArgPanel(String id, int regressionTestingId, LinkedHashMap<String, Object> unboundArgs, HashMap<String, ArrayList<PicklistItem>> choices) {
        super(id);
        this.regressionTestingId = regressionTestingId;
        Form form = new Form("form");
        add(form);
        RepeatingView fields = new RepeatingView("fields");
        form.add(fields);
        feedback = new FeedbackPanel("feedback");
        feedback.setMaxMessages(1);
        feedback.setOutputMarkupId(true);
        form.add(feedback);
        if (unboundArgs != null) {
            for (String key : unboundArgs.keySet()) {
                Object argVal = unboundArgs.get(key);
                LOG.info("Setting up dialog for property '" + key + "' value '" + argVal + "' type " + (argVal != null ? argVal.getClass().getName() : "null"));
                WebMarkupContainer row = new WebMarkupContainer(fields.newChildId());
                fields.add(row);
                IModel labelModel = new Model(key);
                row.add(new Label("name", labelModel));
                PropertyModel model = new PropertyModel(unboundArgs, key);
                if (choices != null && choices.containsKey(key)) {
                    if (argVal != null) {
                        String s = argVal.toString();
                        for (PicklistItem pi : choices.get(key)) {
                            if (pi.getName().equals(s)) {
                                model.setObject(pi);
                            }
                        }
                    }
                    row.add(new ChoiceEditor("editor", model, labelModel, new Model(choices.get(key))));
                } else if (argVal != null && argVal.getClass().equals(java.lang.Boolean.class)) {
                    row.add(new BooleanEditor("editor", model, labelModel));
                } else {
                    row.add(new StringEditor("editor", model, labelModel, LinkedHashMap.class, false));
                }
            }
        }
        form.add(new AjaxButton("okButton") {

            @Override
            public void onSubmit(AjaxRequestTarget target, Form form) {
                ButtonArgPanel.this.onSubmit(target, form);
            }

            @Override
            public String getMarkupId() {
                return ButtonArgPanel.this.getMarkupId() + ":okButton";
            }
        });
        form.add(new AjaxButton("cancelButton") {

            @Override
            public void onSubmit(AjaxRequestTarget target, Form form) {
                ButtonArgPanel.this.onCancel(target);
            }

            @Override
            public String getMarkupId() {
                return ButtonArgPanel.this.getMarkupId() + ":cancelButton";
            }
        });
    }

    /**
	 * Override this method to provide custom editors for your fields.
	 *
	 * @param field
	 *            Java Field in the bean you provided (call getType to retrieve
	 *            the class of the field).
	 * @param model
	 *            IModel that wraps the actual bean's field.
	 * @return
	 */
    protected Component getEditorForBeanField(Field field, IModel model) {
        final Class<?> type = field.getType();
        IModel labelModel = new Model(field.getName());
        if (String.class.isAssignableFrom(type)) {
            return new StringEditor("editor", model, labelModel, LinkedHashMap.class, false);
        } else if (Boolean.class.isAssignableFrom(type) || boolean.class.isAssignableFrom(type)) {
            return new BooleanEditor("editor", model, labelModel);
        } else if (Enum.class.isAssignableFrom(type)) {
            IModel enumChoices = new AbstractReadOnlyModel() {

                @Override
                public Object getObject() {
                    return Arrays.asList(type.getEnumConstants());
                }
            };
            return new EnumEditor("editor", model, labelModel, enumChoices);
        } else {
            throw new RuntimeException("Type " + type + " not supported.");
        }
    }

    public void feedbackError(String msg) {
        feedback.error(msg);
    }

    public FeedbackPanel getFeedbackPanel() {
        return feedback;
    }

    protected abstract void onSubmit(AjaxRequestTarget target, Form form);

    protected abstract void onCancel(AjaxRequestTarget target);

    private class MyList extends ArrayList implements Serializable {
    }

    private class StringEditor extends Fragment {

        public StringEditor(String id, IModel model, IModel labelModel, Class modelType, boolean required) {
            super(id, "stringEditor", ButtonArgPanel.this);
            add(new TextField("edit", model, modelType).setLabel(labelModel).setRequired(required));
        }
    }

    private class ChoiceEditor extends Fragment {

        public ChoiceEditor(String id, IModel model, IModel labelModel, IModel choices) {
            super(id, "choiceEditor", ButtonArgPanel.this);
            DropDownChoice dropDown = new DropDownChoice("edit", model, choices);
            dropDown.setLabel(labelModel);
            add(dropDown);
        }
    }

    private class BooleanEditor extends Fragment {

        public BooleanEditor(String id, IModel model, IModel labelModel) {
            super(id, "booleanEditor", ButtonArgPanel.this);
            add(new CheckBox("edit", model).setLabel(labelModel));
        }
    }

    private class EnumEditor extends Fragment {

        public EnumEditor(String id, IModel model, IModel labelModel, IModel choices) {
            super(id, "enumEditor", ButtonArgPanel.this);
            add(new DropDownChoice("edit", model, choices).setLabel(labelModel));
        }
    }

    private class MultipleChoiceEditor extends Fragment {

        public MultipleChoiceEditor(String id, IModel choices) {
            super(id, "multipleChoiceEditor", choices);
            CheckGroup checkGroup = new CheckGroup("group", choices);
            add(checkGroup);
            checkGroup.add(new CheckGroupSelector("groupSelector"));
            ListView listView = new ListView("listItems", choices) {

                @Override
                protected void populateItem(ListItem item) {
                    item.add(new Check("check", item.getModel()));
                    item.add(new Label("label", item.getModel()));
                }
            };
            checkGroup.add(listView);
        }
    }

    @Override
    public String getRegressionTestingMarkupId() {
        return RegressionTestingUtil.getMarkupId(this, regressionTestingId);
    }

    @Override
    public String getMarkupId() {
        return getRegressionTestingMarkupId() + ":buttonArgPanel";
    }
}
