package org.equanda.tool.translationsEditor;

import javolution.lang.TextBuilder;
import org.equanda.domain.xml.Action;
import org.equanda.domain.xml.Choice;
import org.equanda.domain.xml.*;
import org.equanda.domain.xml.transform.RootTable;
import org.equanda.translations.Key;
import org.equanda.translations.Translation;
import org.equanda.util.StringSplitter;
import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.TreeMap;

/**
 * JPanel with the edit boxes to for label, caption, help for all languages
 *
 * @author <a href="mailto:florin@paragon-software.ro">Florin</a>
 * @author <a href="mailto:joachim@progs.be">Joachim Van der Auwera</a>
 */
public class KeyEditorPanel extends JPanel {

    private JTextField context = new JTextField();

    private JTextArea description = new JTextArea();

    private TreeMap<String, JTextField[]> translationsTextFields;

    private String[] languages;

    private Key key;

    private KeyTableModel model;

    private DomainModel domainModel;

    private Runnable hasChanged = new Runnable() {

        public void run() {
        }
    };

    public void setDomainModel(DomainModel domainModel) {
        this.domainModel = domainModel;
    }

    public KeyEditorPanel(String[] languages, KeyTableModel model) {
        this.languages = languages;
        this.model = model;
        this.setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane();
        add(tabs, BorderLayout.CENTER);
        JPanel trans = new JPanel(new GridBagLayout());
        tabs.add("Translation", trans);
        GridBagConstraints constr = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(2, 2, 2, 2), 0, 0);
        constr.weightx = 1.0;
        trans.add(new JLabel("Context : "), constr);
        constr.gridx = 1;
        trans.add(context, constr);
        description.setEditable(false);
        tabs.add("Description", new JScrollPane(description));
        translationsTextFields = new TreeMap<String, JTextField[]>();
        for (String language : languages) {
            constr.gridy++;
            constr.gridx = 0;
            trans.add(new JLabel(" "), constr);
            JTextField[] textFields = { new JTextField(), new JTextField(), new JTextField() };
            translationsTextFields.put(language, textFields);
            constr.gridy += 2;
            constr.gridx = 0;
            trans.add(new JLabel("Language: " + language), constr);
            constr.gridy++;
            constr.gridx = 0;
            trans.add(new JLabel("Label"), constr);
            constr.gridx = 1;
            trans.add(textFields[0], constr);
            constr.gridy++;
            constr.gridx = 0;
            trans.add(new JLabel("Caption"), constr);
            constr.gridx = 1;
            trans.add(textFields[1], constr);
            constr.gridy++;
            constr.gridx = 0;
            trans.add(new JLabel("Help"), constr);
            constr.gridx = 1;
            trans.add(textFields[2], constr);
        }
        constr.gridy++;
        constr.gridx = 0;
        trans.add(new JLabel(" "), constr);
    }

    public void setCurrentKey(Key key, Map<String, Translation> translations) {
        this.key = key;
        context.setText(key.getContext());
        for (String language : languages) {
            JTextField label = translationsTextFields.get(language)[0];
            JTextField caption = translationsTextFields.get(language)[1];
            JTextField help = translationsTextFields.get(language)[2];
            Translation trans = translations.get(language);
            label.setText((trans != null) ? trans.getLabel() : null);
            caption.setText((trans != null) ? trans.getCaption() : null);
            help.setText((trans != null) ? trans.getHelp() : null);
        }
        if (domainModel != null && "equanda-table".equals(key.getContext())) {
            TextBuilder tb = TextBuilder.newInstance();
            StringSplitter ss = new StringSplitter(key.getKey(), '.');
            if (ss.hasNext()) {
                String base = ss.next();
                if ("table".equals(base) || "page".equals(base)) {
                    addTable(tb, ss.next());
                } else if ("field".equals(base)) {
                    RootTable table = addTable(tb, ss.next());
                    addField(tb, table, ss.next());
                } else if ("choice".equals(base)) {
                    RootTable table = addTable(tb, ss.next());
                    Field field = addField(tb, table, ss.next());
                    addChoice(tb, field, ss.next());
                } else if ("type".equals(base)) {
                    RootTable table = addTable(tb, ss.next());
                    addType(tb, table, ss.next());
                } else if ("group".equals(base)) {
                    RootTable table = addTable(tb, ss.next());
                    addGroup(tb, table, ss.next());
                } else if ("select".equals(base)) {
                    RootTable table = addTable(tb, ss.next());
                    addSelect(tb, table, ss.next());
                } else if ("parameter".equals(base)) {
                    RootTable table = addTable(tb, ss.next());
                    Select select = addSelect(tb, table, ss.next());
                    addSelectField(tb, select, ss.next());
                } else if ("action".equals(base)) {
                    RootTable table = addTable(tb, ss.next());
                    addAction(tb, table, ss.next());
                } else if ("param".equals(base)) {
                    RootTable table = addTable(tb, ss.next());
                    Action action = addAction(tb, table, ss.next());
                    addActionParam(tb, action, ss.next());
                }
            }
            description.setText(tb.toString());
        } else {
            description.setText("");
        }
    }

    private RootTable addTable(TextBuilder tb, String tableName) {
        RootTable table = domainModel.getRootTable(tableName);
        if (null != table && null != table.getDescription() && !"".equals(table.getDescription())) {
            tb.append("Table ");
            tb.append(tableName);
            tb.append(" : ");
            tb.append(table.getDescription());
            tb.append('\n');
        }
        return table;
    }

    private Table addType(TextBuilder tb, RootTable root, String typeName) {
        Table table = root.getTable(typeName);
        if (null != table && null != table.getDescription() && !"".equals(table.getDescription())) {
            tb.append("Type ");
            tb.append(typeName);
            tb.append(" : ");
            tb.append(table.getDescription());
            tb.append('\n');
        }
        return table;
    }

    private Field addField(TextBuilder tb, RootTable table, String fieldName) {
        Field field = null;
        if (null != table) {
            field = table.getField(fieldName);
            if (null != field && null != field.getDescription() && !"".equals(field.getDescription())) {
                tb.append("Field ");
                tb.append(fieldName);
                tb.append(" : ");
                tb.append(field.getDescription());
                tb.append('\n');
            }
        }
        return field;
    }

    private Choice addChoice(TextBuilder tb, Field field, String choiceName) {
        Choice choice = null;
        if (null != field) {
            java.util.List<Choice> choices = field.getChoices();
            for (Choice c : choices) if (choiceName.equals(c.getName())) choice = c;
            if (null != choice && null != choice.getDescription() && !"".equals(choice.getDescription())) {
                tb.append("Choice ");
                tb.append(choiceName);
                tb.append(" : ");
                tb.append(choice.getDescription());
                tb.append('\n');
            }
        }
        return choice;
    }

    private Group addGroup(TextBuilder tb, RootTable root, String groupName) {
        Group group = root.getGroup(groupName);
        if (null != group && null != group.getDescription() && !"".equals(group.getDescription())) {
            tb.append("Group ");
            tb.append(groupName);
            tb.append(" : ");
            tb.append(group.getDescription());
            tb.append('\n');
        }
        return group;
    }

    private Select addSelect(TextBuilder tb, RootTable root, String selectName) {
        Select select = root.getSelect(selectName);
        if (null != select && null != select.getDescription() && !"".equals(select.getDescription())) {
            tb.append("Type ");
            tb.append(selectName);
            tb.append(" : ");
            tb.append(select.getDescription());
            tb.append('\n');
        }
        return select;
    }

    private void addSelectField(TextBuilder tb, Select select, String fieldName) {
        if (fieldName == null) return;
        Field field = null;
        for (Selection s : select.getSelectionsDeep()) {
            if (null != s.getField()) {
                if (fieldName.equals(s.getField().getName())) {
                    field = s.getField();
                    break;
                }
            }
        }
        if (null != field && null != field.getDescription() && !"".equals(field.getDescription())) {
            tb.append("Selection on field ");
            tb.append(fieldName);
            tb.append(" : ");
            tb.append(field.getDescription());
            tb.append('\n');
        }
    }

    private Action addAction(TextBuilder tb, RootTable root, String actionName) {
        Action action = root.getAction(actionName);
        if (null != action && null != action.getDescription() && !"".equals(action.getDescription())) {
            tb.append("Action ");
            tb.append(actionName);
            tb.append(" : ");
            tb.append(action.getDescription());
            tb.append('\n');
        }
        return action;
    }

    private void addActionParam(TextBuilder tb, Action action, String paramName) {
        Parameter param = null;
        for (Parameter p : action.getParameters()) {
            if (paramName.equals(p.getName())) {
                param = p;
                break;
            }
        }
        if (null != param && null != param.getDescription() && !"".equals(param.getDescription())) {
            tb.append("Parameter ");
            tb.append(paramName);
            tb.append(" : ");
            tb.append(param.getDescription());
            tb.append('\n');
        }
    }

    public void save() {
        if (key == null) return;
        key.setContext(context.getText());
        TreeMap<String, Translation> translations = model.getTranslation(key);
        for (String language : languages) {
            JTextField label = translationsTextFields.get(language)[0];
            JTextField caption = translationsTextFields.get(language)[1];
            JTextField help = translationsTextFields.get(language)[2];
            Translation translation = null;
            if (translations != null) translation = translations.get(language);
            if (translation == null) translation = new Translation();
            translation.setLabel(label.getText());
            translation.setCaption(caption.getText());
            translation.setHelp(help.getText());
            model.addTranslation(key, language, translation);
        }
        hasChanged.run();
    }

    /** Clear the fields. Useful when last key was removed from the table and there's no other to update the fields. */
    public void clear() {
        context.setText("");
        for (String language : languages) {
            JTextField label = translationsTextFields.get(language)[0];
            JTextField caption = translationsTextFields.get(language)[1];
            JTextField help = translationsTextFields.get(language)[2];
            label.setText("");
            caption.setText("");
            help.setText("");
        }
    }

    /** Put cursor in first translation item. */
    public void focus() {
        JTextField label = translationsTextFields.get(languages[0])[0];
        label.requestFocus();
    }

    public void setHasChanged(Runnable hasChanged) {
        this.hasChanged = hasChanged;
    }
}
