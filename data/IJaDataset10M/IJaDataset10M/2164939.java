package shag.bean;

import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import shag.table.BeanColumn;

/**
 * @author kflanagan
 */
public class ProxyTextField extends JTextField implements DocumentListener, ProxyComponent {

    private BeanProxy _bean;

    BeanColumn _property = null;

    public ProxyTextField(BeanColumn property) {
        setProperty(property);
    }

    public void setProperty(BeanColumn property) {
        _property = property;
        if (_property.getColumnClass().equals(Integer.class)) {
            setDocument(new WholeNumberDocument(5));
        }
    }

    public BeanColumn getProperty() {
        return _property;
    }

    public void setBean(BeanProxy bean) {
        getDocument().removeDocumentListener(this);
        _bean = bean;
        if (_property != null && !"".equals(_property)) {
            setBackground(UIManager.getColor("TextField.background"));
            setToolTipText(null);
            setText("");
            if (_bean == null) {
                return;
            }
            try {
                setText(_bean.getProperty(_property.getPropertyName()).toString());
            } catch (NullPointerException ex) {
                setText("");
            } catch (Exception ex) {
                setText("ERROR");
                setToolTipText(ex.getMessage());
                setBackground(Color.RED);
            }
        }
        getDocument().addDocumentListener(this);
    }

    private void doDocEvent(DocumentEvent e) {
        if (_bean != null) {
            try {
                Object obj;
                if (_property.getColumnClass().equals(Integer.class)) {
                    try {
                        obj = new Integer(getText());
                    } catch (NumberFormatException nfe) {
                        obj = null;
                    }
                } else {
                    obj = getText();
                }
                _bean.setProperty(_property.getPropertyName(), obj);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public void changedUpdate(DocumentEvent e) {
        doDocEvent(e);
    }

    public void insertUpdate(DocumentEvent e) {
        doDocEvent(e);
    }

    public void removeUpdate(DocumentEvent e) {
        doDocEvent(e);
    }
}
