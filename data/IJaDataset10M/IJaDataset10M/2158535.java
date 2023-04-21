package org.lindenb.swing;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.StringReader;
import java.lang.reflect.Constructor;
import java.net.URI;
import java.net.URL;
import java.util.Vector;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.swing.Icon;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JTable;
import javax.swing.JTree;
import javax.swing.event.DocumentEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.JTextComponent;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.lindenb.util.Pair;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

/**
 * @author pierre
 *
 */
public abstract class ConstrainedAction<T> extends ObjectAction<T> {

    private static final long serialVersionUID = 1L;

    private Vector<Validator1<?>> validators = new Vector<Validator1<?>>(1, 1);

    /**
	 * @param object
	 */
    public ConstrainedAction(T object) {
        super(object);
    }

    /**
	 * @param object
	 * @param name
	 */
    public ConstrainedAction(T object, String name) {
        super(object, name);
    }

    /**
	 * @param object
	 * @param name
	 * @param icon
	 */
    public ConstrainedAction(T object, String name, Icon icon) {
        super(object, name, icon);
    }

    private abstract static class Validator1<X extends JComponent> {

        X component;

        Validator1(X component) {
            this.component = component;
        }

        public abstract String getErrorMessage();
    }

    private abstract static class Validator2<X extends JComponent, Y> extends Validator1<X> {

        Y param;

        Validator2(X component, Y param) {
            super(component);
            this.param = param;
        }
    }

    private static String name(Component c) {
        return c.getName() != null ? c.getName() : c.getClass().getSimpleName().toLowerCase();
    }

    public void mustMatchPattern(JTextComponent component, Pattern pattern) {
        addTextValidator(new Validator2<JTextComponent, Pattern>(component, pattern) {

            public String getErrorMessage() {
                return (param.matcher(component.getText()).matches() ? null : name(component) + " doesn\'t match " + param.pattern());
            }
        });
    }

    public void mustBeARegexPattern(JTextComponent component) {
        addTextValidator(new Validator1<JTextComponent>(component) {

            public String getErrorMessage() {
                try {
                    Pattern.compile(component.getText());
                    return null;
                } catch (PatternSyntaxException e) {
                    return e.getMessage();
                }
            }
        });
    }

    public void mustNotEmpty(JTextComponent component, boolean trim) {
        addTextValidator(new Validator2<JTextComponent, Boolean>(component, trim) {

            public String getErrorMessage() {
                String s = this.component.getText();
                if (this.param) s = s.trim();
                return s.length() == 0 ? name(this.component) + " is Empty" : null;
            }
        });
    }

    public void mustBeInteger(JTextComponent component) {
        mustBeAClass(component, Integer.class);
    }

    public void mustBeShort(JTextComponent component) {
        mustBeAClass(component, Short.class);
    }

    public void mustBeLong(JTextComponent component) {
        mustBeAClass(component, Long.class);
    }

    public void mustBeFloat(JTextComponent component) {
        mustBeAClass(component, Float.class);
    }

    public void mustBeDouble(JTextComponent component) {
        mustBeAClass(component, Double.class);
    }

    public void mustBeURL(JTextComponent component) {
        mustBeAClass(component, URL.class);
    }

    public void mustBeURI(JTextComponent component) {
        mustBeAClass(component, URI.class);
    }

    public <X> void mustBeAClass(JTextComponent component, Class<X> clazz) {
        addTextValidator(new Validator2<JTextComponent, Class<X>>(component, clazz) {

            @Override
            public String getErrorMessage() {
                try {
                    Constructor<X> cstor = this.param.getConstructor(String.class);
                    cstor.newInstance(this.component.getText());
                    return null;
                } catch (Exception e) {
                    return "Cannot cast " + name(this.component) + " to " + this.param + "(" + this.component.getText() + "\" : " + e.getMessage();
                }
            }
        });
    }

    public void mustBeOpaqueURI(JTextComponent component) {
        addTextValidator(new Validator1<JTextComponent>(component) {

            @Override
            public String getErrorMessage() {
                try {
                    URI uri = new URI(component.getText());
                    if (!uri.isOpaque()) return "" + name(this.component) + " is not an opaque uri";
                    return null;
                } catch (Exception e) {
                    return "Cannot cast " + name(this.component) + " to an opaque URI";
                }
            }
        });
    }

    public void mustBeAbsoluteURI(JTextComponent component) {
        addTextValidator(new Validator1<JTextComponent>(component) {

            @Override
            public String getErrorMessage() {
                try {
                    URI uri = new URI(component.getText());
                    if (!uri.isAbsolute()) return "" + name(this.component) + " is not an absolute uri";
                    return null;
                } catch (Exception e) {
                    return "Cannot cast " + name(this.component) + " to an absolute URI";
                }
            }
        });
    }

    public void mustHaveMaxLength(JTextComponent component, int maxLengthExclusive) {
        addTextValidator(new Validator2<JTextComponent, Integer>(component, maxLengthExclusive) {

            @Override
            public String getErrorMessage() {
                int n = this.component.getText().length();
                return n < this.param ? null : name(this.component) + " is too large: " + n + " chars (max:" + this.param + ")";
            }
        });
    }

    public void mustHaveMinLength(JTextComponent component, int minLengthInclusive) {
        addTextValidator(new Validator2<JTextComponent, Integer>(component, minLengthInclusive) {

            @Override
            public String getErrorMessage() {
                int n = this.component.getText().length();
                return n >= this.param ? null : name(this.component) + " is too short: " + n + " chars (min:" + this.param + ")";
            }
        });
    }

    public void mustBeInClassPath(JTextComponent component) {
        addTextValidator(new Validator1<JTextComponent>(component) {

            @Override
            public String getErrorMessage() {
                try {
                    Class.forName(this.component.getText());
                    return null;
                } catch (ClassNotFoundException e) {
                    return this.component.getText() + " is not in CLASSPATH :" + e.getMessage();
                }
            }
        });
    }

    public void mustBeValidXML(JTextComponent component, boolean namespaceAware) {
        addTextValidator(new Validator2<JTextComponent, Boolean>(component, namespaceAware) {

            @Override
            public String getErrorMessage() {
                try {
                    SAXParserFactory f = SAXParserFactory.newInstance();
                    f.setNamespaceAware(this.param);
                    f.setValidating(false);
                    f.setXIncludeAware(false);
                    SAXParser parser = f.newSAXParser();
                    parser.parse(new InputSource(new StringReader(this.component.getText())), new DefaultHandler());
                    return null;
                } catch (Exception e) {
                    return name(this.component) + " is not valid XML :" + e.getMessage();
                }
            }
        });
    }

    public void mustBeInRange(JTextComponent component, int minInclusive, int maxExclusive) {
        addTextValidator(new Validator2<JTextComponent, Pair<Integer, Integer>>(component, new Pair<Integer, Integer>(minInclusive, maxExclusive)) {

            @Override
            public String getErrorMessage() {
                int value;
                try {
                    value = Integer.parseInt(this.component.getText().trim());
                    if (value < this.param.first()) return name(this.component) + " should be greater or equal to " + this.param.first();
                    if (value >= this.param.second()) return name(this.component) + " should be stricly lower than " + this.param.second();
                    return null;
                } catch (NumberFormatException err) {
                    return name(this.component) + ": not a valid number";
                }
            }
        });
    }

    public void mustBeInRange(JTextComponent component, double minInclusive, double maxInclusive) {
        addTextValidator(new Validator2<JTextComponent, Pair<Double, Double>>(component, new Pair<Double, Double>(minInclusive, maxInclusive)) {

            @Override
            public String getErrorMessage() {
                double value;
                try {
                    value = Double.parseDouble(this.component.getText().trim());
                    if (value < this.param.first()) return name(this.component) + " should be greater or equal to " + this.param.first();
                    if (value > this.param.second()) return name(this.component) + " should be stricly lower than " + this.param.second();
                    return null;
                } catch (NumberFormatException err) {
                    return name(this.component) + ": not a valid number";
                }
            }
        });
    }

    public void mustHaveSelection(JTextComponent component) {
        addTextValidator(new Validator1<JTextComponent>(component) {

            @Override
            public String getErrorMessage() {
                if (this.component.getSelectionStart() == this.component.getSelectionEnd()) {
                    return name(this.component) + " : selection is empty";
                }
                return null;
            }
        });
    }

    public void mustHaveRows(JTable table) {
        mustHaveRows(table, 1, Integer.MAX_VALUE);
    }

    public void mustHaveRows(JTable table, int rowCount) {
        mustHaveRows(table, rowCount, rowCount + 1);
    }

    public void mustHaveRows(JTable table, int minInclusive, int maxExclusive) {
        table.getModel().addTableModelListener(new TableModelListener() {

            @Override
            public void tableChanged(TableModelEvent e) {
                validate();
            }
        });
        addValidator(new Validator2<JTable, Pair<Integer, Integer>>(table, new Pair<Integer, Integer>(minInclusive, maxExclusive)) {

            @Override
            public String getErrorMessage() {
                int n = this.component.getModel().getRowCount();
                return n >= this.param.first() && n < this.param.second() ? null : "Illegale Number of Rows";
            }
        });
    }

    public void mustHaveOneRowSelected(JList list) {
        mustHaveRowsSelected(list, 1);
    }

    public void mustHaveRowsSelected(JList list, int rowCount) {
        mustHaveRowsSelected(list, rowCount, rowCount + 1);
    }

    public void mustHaveRowsSelected(JList list, int minInclusive, int maxExclusive) {
        list.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                validate();
            }
        });
        addValidator(new Validator2<JList, Pair<Integer, Integer>>(list, new Pair<Integer, Integer>(minInclusive, maxExclusive)) {

            @Override
            public String getErrorMessage() {
                int n = this.component.getSelectedIndices().length;
                return n >= this.param.first() && n < this.param.second() ? null : name(this.component) + ": Illegal Number of Selected Rows " + this.param.first() + "<=" + n + "<" + this.param.second();
            }
        });
    }

    public void mustHaveOneRowSelected(JTable table) {
        mustHaveRowsSelected(table, 1);
    }

    public void mustHaveRowsSelected(JTable table, int rowCount) {
        mustHaveRowsSelected(table, rowCount, rowCount + 1);
    }

    public void mustHaveAtLeastOneRowSelected(JTable table) {
        mustHaveRowsSelected(table, 1, Integer.MAX_VALUE);
    }

    public void mustHaveRowsSelected(JTable table, int minInclusive, int maxExclusive) {
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                validate();
            }
        });
        addValidator(new Validator2<JTable, Pair<Integer, Integer>>(table, new Pair<Integer, Integer>(minInclusive, maxExclusive)) {

            @Override
            public String getErrorMessage() {
                int n = this.component.getSelectedRowCount();
                return n >= this.param.first() && n < this.param.second() ? null : name(this.component) + ": Illegale Number of Selected Rows";
            }
        });
    }

    public void mustBeSelected(JTree tree) {
        tree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {

            @Override
            public void valueChanged(TreeSelectionEvent e) {
                validate();
            }
        });
        addValidator(new Validator1<JTree>(tree) {

            @Override
            public String getErrorMessage() {
                return component.getSelectionCount() == 0 ? name(component) + " is not selected" : null;
            }
        });
    }

    public void mustBeSelected(JTable table) {
        mustBeSelected(table, 1, Integer.MAX_VALUE);
    }

    public void mustBeSelected(JTable table, int rowCount) {
        mustBeSelected(table, rowCount, rowCount + 1);
    }

    public void mustBeSelected(JTable table, int minInclusive, int maxExclusive) {
        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {
                validate();
            }
        });
        addValidator(new Validator2<JTable, Pair<Integer, Integer>>(table, new Pair<Integer, Integer>(minInclusive, maxExclusive)) {

            @Override
            public String getErrorMessage() {
                int n = this.component.getSelectedRowCount();
                return n >= this.param.first() && n < this.param.second() ? null : "Illegal Number of Selected Rows";
            }
        });
    }

    public void mustBeSelected(JComboBox cbox) {
        cbox.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                validate();
            }
        });
        addValidator(new Validator1<JComboBox>(cbox) {

            @Override
            public String getErrorMessage() {
                int n = this.component.getSelectedIndex();
                return n != -1 ? null : name(this.component) + " Should be selected";
            }
        });
    }

    protected Validator1<?> addTextValidator(Validator1<JTextComponent> v) {
        v.component.getDocument().addDocumentListener(new DocumentAdapter() {

            @Override
            public void documentChanged(DocumentEvent e) {
                validate();
            }
        });
        return addValidator(v);
    }

    public Validator1<?> addValidator(Validator1<?> v) {
        this.validators.addElement(v);
        validate();
        return v;
    }

    public String getErrorMessage() {
        String msg = null;
        for (Validator1<?> v : this.validators) {
            msg = v.getErrorMessage();
            if (msg != null) break;
        }
        return msg;
    }

    protected void validate() {
        setEnabled(getErrorMessage() == null);
    }
}
