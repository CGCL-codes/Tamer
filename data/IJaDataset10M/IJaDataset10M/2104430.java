package ontool.app.debugger;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.apache.commons.beanutils.PropertyUtils;
import com.jrefinery.ui.FontChooserDialog;
import com.jrefinery.ui.FontDisplayField;

/**
 * class_description
 * 
 * <p>Created in 03/10/2002 03:45:26
 * 
 * @author <a href="mailto:asrgomes@dca.fee.unicamp.br">Antonio S.R. Gomes<a/>
 * @version $Revision: 1.1 $
 * @since since_version
 */
public class FontEditor extends JPanel implements PropertyEditor {

    private Object bean;

    private String property;

    private JButton button;

    private Object originalValue;

    private Font currentFont;

    private FontDisplayField fd;

    public FontEditor() {
    }

    public FontEditor(Object bean, String property) {
        this.bean = bean;
        this.property = property;
        configure();
    }

    public void configure() {
        setLayout(new BorderLayout());
        originalValue = getOriginalValue();
        button = new JButton("...");
        button.addActionListener(new ButtonListener(button));
        fd = new FontDisplayField((Font) originalValue);
        currentFont = (Font) originalValue;
        removeAll();
        add(fd, "Center");
        add(button, "East");
    }

    protected Object getOriginalValue() {
        try {
            return PropertyUtils.getProperty(bean, property);
        } catch (Exception e) {
            throw new IllegalArgumentException("invalid bean/property");
        }
    }

    protected void commit() {
        if (hasChanged()) {
            originalValue = fd.getDisplayFont();
            try {
                PropertyUtils.setProperty(bean, property, originalValue);
            } catch (Exception e) {
                throw new IllegalArgumentException("invalid bean/property");
            }
        }
    }

    public boolean hasChanged() {
        if (originalValue == null) return true;
        return !originalValue.equals(fd.getDisplayFont());
    }

    private class ButtonListener implements ActionListener {

        JButton button;

        public ButtonListener(JButton b) {
            button = b;
        }

        public void actionPerformed(ActionEvent event) {
            FontChooserDialog d = new FontChooserDialog((Frame) null, "Choose Font", true, currentFont);
            d.pack();
            d.show();
            Font font = d.getSelectedFont();
            currentFont = font;
            fd.setDisplayFont(font);
            if (font != null) {
                fd.setDisplayFont(font);
                commit();
            }
        }
    }

    /**
	 * @see ontool.app.debugger.PropertyEditor#getComponent()
	 */
    public JComponent getComponent() {
        return this;
    }

    /**
	 * @see ontool.app.debugger.PropertyEditor#setBean(Object)
	 */
    public void setBean(Object obj) {
        this.bean = obj;
    }

    /**
	 * @see ontool.app.debugger.PropertyEditor#setEditorParam(Object)
	 */
    public void setEditorParam(Object param) {
    }

    /**
	 * @see ontool.app.debugger.PropertyEditor#setPropertyName(String)
	 */
    public void setPropertyName(String property) {
        this.property = property;
    }
}
