package de.iritgo.aktera.ui.ng.formular;

import java.util.List;
import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.avalon.framework.logger.Logger;
import de.iritgo.aktera.model.ModelException;
import de.iritgo.aktera.spring.SpringTools;
import de.iritgo.aktera.ui.UIControllerException;
import de.iritgo.aktera.ui.form.CommandDescriptor;
import de.iritgo.aktera.ui.form.CommandInfo;
import de.iritgo.aktera.ui.form.FieldDescriptor;
import de.iritgo.aktera.ui.form.FormularDescriptor;
import de.iritgo.aktera.ui.form.GroupDescriptor;
import de.iritgo.aktera.ui.form.PageDescriptor;
import de.iritgo.simplelife.math.NumberTools;

public class Formular {

    /** Formular configuration */
    private Configuration configuration;

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    /** Our logger */
    private Logger logger;

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public FormularDescriptor createFormularDescriptor() throws UIControllerException {
        try {
            FormularDescriptor formularDescriptor = new FormularDescriptor();
            formularDescriptor.setBundle(configuration.getChild("bundle").getValue("Aktera"));
            formularDescriptor.setIdField(configuration.getChild("key").getValue(null));
            formularDescriptor.setLabelWidth(NumberTools.toInt(configuration.getChild("labelWidth").getValue("0"), 0));
            createGroups(configuration, formularDescriptor);
            createGroupLists(configuration, formularDescriptor);
            Configuration[] pagesConfig = configuration.getChildren("page");
            for (Configuration pageConfig : pagesConfig) {
                String pageBundle = pageConfig.getAttribute("bundle", formularDescriptor.getBundle());
                PageDescriptor page = formularDescriptor.addPage(pageConfig.getAttribute("name"), pageBundle);
                page.setPosition(positionStringToValue(pageConfig.getAttribute("pos", "C")));
                page.setIcon(pageConfig.getAttribute("icon", null));
                page.setInactiveIcon(pageConfig.getAttribute("inactiveIcon", null));
                createGroups(pageConfig, formularDescriptor);
                createGroupLists(pageConfig, formularDescriptor);
            }
            modifyGroups(configuration, formularDescriptor);
            formularDescriptor.sort();
            return formularDescriptor;
        } catch (ModelException x) {
            throw new UIControllerException(x);
        } catch (ConfigurationException x) {
            throw new UIControllerException(x);
        }
    }

    /**
	 * Create gGroup descriptors for each group child of the specified
	 * configuration node.
	 *
	 * @param config
	 *            The parent configuration.
	 * @param formular
	 *            The formular descriptor.
	 */
    private void createGroups(Configuration config, FormularDescriptor formular) throws ConfigurationException, ModelException {
        for (Configuration groupConfig : config.getChildren("group")) {
            String id = groupConfig.getAttribute("id", null);
            String name = groupConfig.getAttribute("name", null);
            if (name != null) {
                if (id != null) {
                    throw new ModelException("Both id and name specified for group '" + id + "'");
                }
                String groupBundle = groupConfig.getAttribute("bundle", formular.getBundle());
                GroupDescriptor group = formular.addGroup(name, groupBundle);
                group.setPosition(positionStringToValue(groupConfig.getAttribute("pos", "C")));
                group.setVisible(NumberTools.toBool(groupConfig.getAttribute("visible", "true"), true));
                group.setTitleVisible(NumberTools.toBool(groupConfig.getAttribute("titleVisible", "true"), true));
                group.setIcon(groupConfig.getAttribute("icon", null));
                group.setLabel(groupConfig.getAttribute("label", group.getLabel()));
                createFields(groupConfig, formular, group, null);
            }
        }
    }

    /**
	 * Add all group list groups
	 *
	 * @param config The formular configuration
	 * @param formular The formular description
	 * @throws ConfigurationException In case of an configuration error
	 * @throws ModelException In case of an configuration error
	 */
    private void createGroupLists(Configuration config, FormularDescriptor formular) throws ConfigurationException, ModelException {
        for (Configuration groupListConfig : config.getChildren("groupList")) {
            List<FormularGroup> formularGroups = (List<FormularGroup>) SpringTools.getBean(groupListConfig.getAttribute("bean"));
            for (FormularGroup formularGroup : formularGroups) {
                createGroups(formularGroup.getConfiguration(), formular);
            }
        }
    }

    /**
	 * Create the fields of a group or multi field.
	 *
	 */
    private void createFields(Configuration parent, FormularDescriptor formular, GroupDescriptor parentGroup, FieldDescriptor parentField) throws ConfigurationException {
        Configuration[] children = parent.getChildren();
        for (Configuration childConfig : children) {
            String bundle = childConfig.getAttribute("bundle", parentGroup != null ? parentGroup.getBundle() : parentField.getBundle());
            if ("field".equals(childConfig.getName())) {
                FieldDescriptor field = new FieldDescriptor(childConfig.getAttribute("name"), bundle, childConfig.getAttribute("editor", ""), NumberTools.toInt(childConfig.getAttribute("size", "0"), 0));
                field.setLabel(childConfig.getAttribute("label", null));
                field.setToolTip(childConfig.getAttribute("tip", null));
                field.setRows(NumberTools.toInt(childConfig.getAttribute("rows", "6"), 6));
                field.setNoLabel(NumberTools.toBool(childConfig.getAttribute("nolabel", childConfig.getAttribute("noLabel", "false")), false));
                field.setTrim(NumberTools.toBool(childConfig.getAttribute("trim", "false"), false));
                if (childConfig.getAttribute("unbound", null) != null) {
                    field.setUnbound(childConfig.getAttributeAsBoolean("unbound", false));
                }
                field.setSelectable(childConfig.getAttributeAsBoolean("selectable", false));
                field.setValidationClassName(childConfig.getAttribute("validator", null));
                if (childConfig.getAttribute("readonly", null) != null) {
                    field.setReadOnly(childConfig.getAttributeAsBoolean("readonly", false));
                }
                if (childConfig.getAttribute("duty", null) != null) {
                    field.setDuty(childConfig.getAttributeAsBoolean("duty", false));
                }
                if (childConfig.getAttribute("submit", null) != null) {
                    field.setSubmit(childConfig.getAttributeAsBoolean("submit", false));
                }
                if (parentGroup != null) {
                    parentGroup.addField(field);
                } else if (parentField != null) {
                    parentField.addField(field);
                }
                createCommandsForField(childConfig, formular, field);
            } else if ("comment".equals(childConfig.getName())) {
                FieldDescriptor field = new FieldDescriptor(childConfig.getAttribute("label"), bundle, "", 0);
                field.setComment(true);
                if (parentGroup != null) {
                    parentGroup.addField(field);
                } else if (parentField != null) {
                    parentField.addField(field);
                }
            } else if ("buttons".equals(childConfig.getName())) {
                FieldDescriptor field = new FieldDescriptor(childConfig.getAttribute("id", "dummy"), null, "", 0);
                field.setUnbound(true);
                field.setLabel("0.empty");
                if (parentGroup != null) {
                    parentGroup.addField(field);
                } else if (parentField != null) {
                    parentField.addField(field);
                }
                createCommandsForField(childConfig, formular, field);
                field.setBundle("Aktera");
            } else if ("multi".equals(childConfig.getName()) && parentField == null) {
                FieldDescriptor field = new FieldDescriptor(childConfig.getAttribute("label"), bundle, "", 0);
                field.setMulti(true);
                if (parentGroup != null) {
                    parentGroup.addField(field);
                    createFields(childConfig, formular, null, field);
                }
            }
        }
    }

    /**
	 * Modify group descriptors for each group child of the specified
	 * configuration node.
	 *
	 * @param config
	 *            The parent configuration.
	 * @param formular
	 *            The formular descriptor.
	 */
    private void modifyGroups(Configuration config, FormularDescriptor formular) throws ConfigurationException, ModelException {
        for (Configuration groupConfig : config.getChildren("group")) {
            String id = groupConfig.getAttribute("id", null);
            String name = groupConfig.getAttribute("name", null);
            if (id != null) {
                if (name != null) {
                    throw new ModelException("Both id and name specified for group '" + id + "'");
                }
                GroupDescriptor group = formular.getGroup(id);
                if (group == null) {
                    throw new ModelException("Unable to find group '" + id + "'");
                }
                Configuration[] groupChildren = groupConfig.getChildren();
                for (Configuration childConfig : groupChildren) {
                    if ("field".equals(childConfig.getName())) {
                        String fieldId = childConfig.getAttribute("id", null);
                        String fieldName = childConfig.getAttribute("id", null);
                        if (id != null && name != null) {
                            throw new ModelException("Both id and name specified for field '" + id + "'");
                        }
                        if (id != null) {
                            FieldDescriptor field = group.getField(fieldId);
                            if (field == null) {
                                throw new ModelException("Unable to find field '" + fieldId + "' in group '" + id + "'");
                            }
                            createCommandsForField(childConfig, formular, field);
                        }
                    }
                }
            }
        }
    }

    /**
	 * Create commands for a field.
	 *
	 * @param config
	 *            The field configuration.
	 * @param formular
	 *            The formular descriptor.
	 * @param field
	 *            The field descriptor.
	 */
    private void createCommandsForField(Configuration config, FormularDescriptor formular, FieldDescriptor field) throws ConfigurationException {
        Configuration[] commandChildren = config.getChildren("command");
        for (Configuration commandConfig : commandChildren) {
            String model = commandConfig.getAttribute("model", commandConfig.getAttribute("bean", null));
            CommandInfo cmd = new CommandInfo(model, commandConfig.getAttribute("name"), commandConfig.getAttribute("label", null));
            if (commandConfig.getAttribute("bean", null) != null) {
                cmd.setBean(true);
            }
            cmd.setIcon(commandConfig.getAttribute("icon", null));
            CommandDescriptor command = field.getCommands().add(cmd);
            command.setBundle(commandConfig.getAttribute("bundle", field.getBundle() != null ? field.getBundle() : formular.getBundle()));
            Configuration[] parameterChildren = commandConfig.getChildren("parameter");
            for (Configuration parameterConfig : parameterChildren) {
                command.withParameter(parameterConfig.getAttribute("name"), parameterConfig.getAttribute("value"));
            }
            parameterChildren = commandConfig.getChildren("param");
            for (Configuration parameterConfig : parameterChildren) {
                command.withParameter(parameterConfig.getAttribute("name"), parameterConfig.getAttribute("value"));
            }
            Configuration[] attributeChildren = commandConfig.getChildren("attribute");
            for (Configuration attributeConfig : attributeChildren) {
                command.withParameter(attributeConfig.getAttribute("name"), attributeConfig.getAttribute("value"));
            }
        }
    }

    /**
	 * Helper method to convert position strings to position integers.
	 *
	 * @param pos
	 *            The position string.
	 * @return The position value.
	 */
    protected int positionStringToValue(String pos) {
        int position = 0;
        if ("SS".equals(pos)) {
            position = -20;
        } else if ("S".equals(pos)) {
            position = -10;
        } else if ("T".equals(pos) || "L".equals(pos)) {
            position = -5;
        } else if ("M".equals(pos) || "C".equals(pos)) {
            position = 0;
        } else if ("B".equals(pos) || "R".equals(pos)) {
            position = 5;
        } else if ("E".equals(pos)) {
            position = 10;
        } else if ("EE".equals(pos)) {
            position = 20;
        } else {
            position = NumberTools.toInt(pos, 0);
        }
        return position;
    }
}
