package org.databene.gui.swing.delegate;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.databene.commons.BeanUtil;
import org.databene.commons.StringUtil;
import org.databene.commons.bean.ObservableBean;
import org.databene.commons.converter.ToStringConverter;

/**
 * {@link JTextArea} implementation that serves as delegate of a property of a JavaBean object.<br/><br/>
 * Created: 22.08.2010 07:29:11
 * @since 0.2.4
 * @author Volker Bergmann
 */
public class PropertyTextArea extends JTextArea {

    /** uid for serialization */
    private static final long serialVersionUID = 4917700588809725874L;

    private Object bean;

    private String propertyName;

    private ToStringConverter toStringConverter;

    boolean locked;

    public PropertyTextArea(Object bean, String propertyName) {
        this.bean = bean;
        this.propertyName = propertyName;
        this.toStringConverter = new ToStringConverter();
        this.setLineWrap(true);
        this.setWrapStyleWord(true);
        this.setRows(5);
        this.locked = true;
        Listener listener = new Listener();
        if (bean instanceof ObservableBean) ((ObservableBean) bean).addPropertyChangeListener(propertyName, listener);
        this.getDocument().addDocumentListener(listener);
        this.locked = false;
        refresh();
    }

    /**
	 * reads the current property value and writes it to the text field.
	 */
    void refresh() {
        if (!locked) {
            locked = true;
            Object propertyValue = BeanUtil.getPropertyValue(bean, propertyName);
            String text = toStringConverter.convert(propertyValue);
            text = StringUtil.escape(text);
            if (!getText().equals(text)) setText(text);
            locked = false;
        }
    }

    /**
	 * writes the current text field content to the property.
	 */
    void update() {
        if (!locked) {
            locked = true;
            Document document = getDocument();
            String text;
            try {
                text = document.getText(0, document.getLength());
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
            text = StringUtil.unescape(text);
            if (!text.equals(BeanUtil.getPropertyValue(bean, propertyName))) BeanUtil.setPropertyValue(bean, propertyName, text);
            locked = false;
        }
    }

    class Listener implements PropertyChangeListener, DocumentListener {

        public void propertyChange(PropertyChangeEvent evt) {
            refresh();
        }

        public void changedUpdate(DocumentEvent evt) {
            update();
        }

        public void insertUpdate(DocumentEvent evt) {
            update();
        }

        public void removeUpdate(DocumentEvent evt) {
            update();
        }
    }
}
