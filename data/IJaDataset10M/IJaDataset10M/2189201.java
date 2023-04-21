package org.logitest.testlet;

import java.awt.*;
import java.util.*;
import javax.swing.*;
import org.jdom.*;
import org.logitest.*;
import org.logitest.util.*;
import org.apache.oro.text.regex.*;

/** An Testlet which will examine the response headers for name and value
	matches.
	
	@author Anthony Eden
*/
public class HeaderTestlet extends Testlet {

    /** Default constructor. */
    public HeaderTestlet() {
    }

    /** Get the expected name.
	
		@return The name
	*/
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**	Return the expected header value.
	
		@return The expected header value
	*/
    public String getValue() {
        return value;
    }

    /**	Set the expected header value.
	
		@param value The new expected value
	*/
    public void setValue(String value) {
        this.value = value;
    }

    /** Test the given resource. 
	
		@param resource The resource
	*/
    public boolean test(Resource resource) throws Exception {
        NameValuePairSet responseHeaders = resource.getResponseHeaders();
        boolean passed = false;
        PatternCompiler compiler = new Perl5Compiler();
        PatternMatcher matcher = new Perl5Matcher();
        Pattern namePattern = compiler.compile(getName());
        Pattern valuePattern = compiler.compile(getValue());
        if (responseHeaders != null) {
            Iterator pairs = responseHeaders.iterator();
            while (pairs.hasNext()) {
                NameValuePair pair = (NameValuePair) pairs.next();
                if (matcher.contains(pair.getName(), namePattern) && matcher.contains(pair.getValue(), valuePattern)) {
                    passed = true;
                    break;
                }
            }
        }
        if (passed) {
            setState(Test.PASSED);
        } else {
            setState(Test.FAILED);
        }
        return passed;
    }

    public void readConfiguration(Element element) {
        Element headerElement = element.getChild("header");
        setName(headerElement.getAttributeValue("name"));
        setValue(headerElement.getTextTrim());
    }

    public void writeConfiguration(Element element) {
        Element headerElement = new Element("header");
        headerElement.addAttribute("name", getName());
        headerElement.addContent(getValue());
        element.addContent(headerElement);
    }

    public boolean save() {
        Editor editor = (Editor) getEditor();
        setName(editor.nameField.getText());
        setValue(editor.valueField.getText());
        return true;
    }

    public void revert() {
        Editor editor = (Editor) getEditor();
        editor.nameField.setText(getName());
        editor.valueField.setText(getValue());
    }

    public Component getEditor() {
        if (editor == null) {
            editor = new Editor();
        }
        return editor;
    }

    protected Map getProperties() {
        if (properties == null) {
            properties = new HashMap();
            properties.put(DISPLAY_NAME, "Header Testlet");
            properties.put(DESCRIPTION, "Test for header matches.");
            properties.put(AUTHOR, "Anthony Eden");
            properties.put(VERSION, "1.0");
        }
        return properties;
    }

    class Editor extends JPanel {

        public Editor() {
            init();
        }

        private void init() {
            GridBagLayout gbl = new GridBagLayout();
            GridBagConstraints gbc = new GridBagConstraints();
            setLayout(gbl);
            gbc.insets = new Insets(1, 1, 1, 1);
            gbc.fill = GridBagConstraints.HORIZONTAL;
            nameLabel = new JLabel("Name:");
            gbc.weightx = 0;
            gbc.gridwidth = 1;
            gbl.setConstraints(nameLabel, gbc);
            add(nameLabel);
            nameField = new JTextField();
            nameField.setColumns(36);
            gbc.weightx = 1;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbl.setConstraints(nameField, gbc);
            add(nameField);
            valueLabel = new JLabel("Value:");
            gbc.weightx = 0;
            gbc.gridwidth = 1;
            gbl.setConstraints(valueLabel, gbc);
            add(valueLabel);
            valueField = new JTextField();
            gbc.weightx = 1;
            gbc.gridwidth = GridBagConstraints.REMAINDER;
            gbl.setConstraints(valueField, gbc);
            add(valueField);
        }

        JLabel nameLabel;

        JTextField nameField;

        JLabel valueLabel;

        JTextField valueField;
    }

    private String name;

    private String value;

    private Map properties;

    private Editor editor;
}
