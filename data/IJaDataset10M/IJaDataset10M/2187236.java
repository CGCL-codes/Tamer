package net.sourceforge.volunteer.bind;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import net.sourceforge.volunteer.bean.JavaBeanExtender;

/**
 * {description}
 * 
 * @author Vasiliy Gagin
 * 
 * @version $Revision$
 * @param <T>
 */
public class TextFieldsBinder<T> {

    private final List<TextFieldBinding> bindings = new ArrayList<TextFieldBinding>();

    private boolean settingBean = false;

    private final JavaBeanExtender<T> extender;

    /**
     * TextFieldsBinder
     * 
     * @param extender TODO
     * @throws BindingException
     */
    public TextFieldsBinder(JavaBeanExtender<T> extender) throws BindingException {
        this.extender = extender;
    }

    /**
     * @throws BindingException
     */
    public void resetBaseBean() {
        settingBean = true;
        try {
            for (TextFieldBinding binding : bindings) {
                binding.loadValue();
            }
        } catch (BindingException exc) {
            exc.printStackTrace();
        } finally {
            settingBean = false;
        }
    }

    /**
     * @param propertyName
     * @param field
     * @throws BindingException
     */
    public void bindField(String propertyName, JTextField field) throws BindingException {
        JavaBeanExtender.PropertyHelper property = extender.findProperty(propertyName);
        Document document = field.getDocument();
        final TextFieldBinding doc = new TextFieldBinding(property, document);
        bindings.add(doc);
    }

    private class TextFieldBinding {

        /** serialVersionUID */
        private static final long serialVersionUID = 1L;

        private final JavaBeanExtender.PropertyHelper property;

        private final Document document;

        /**
         * TextFieldBinding
         * 
         * @param property TODO
         * @param document
         * @throws BindingException
         */
        public TextFieldBinding(JavaBeanExtender.PropertyHelper property, Document document) throws BindingException {
            this.property = property;
            this.document = document;
            document.addDocumentListener(new DocumentListener() {

                public void removeUpdate(DocumentEvent e) {
                    process();
                }

                public void insertUpdate(DocumentEvent e) {
                    process();
                }

                public void changedUpdate(DocumentEvent e) {
                    process();
                }

                private void process() {
                    try {
                        if (!settingBean) {
                            saveValue();
                        }
                    } catch (BindingException exc) {
                        exc.printStackTrace();
                    }
                }
            });
        }

        /**
         * @param doc
         * @throws BindingException
         */
        private void loadValue() throws BindingException {
            try {
                document.remove(0, document.getLength());
                String fieldValue = (String) property.readFieldIfExtending();
                if (fieldValue != null && fieldValue.length() > 0) {
                    document.insertString(0, fieldValue, null);
                }
            } catch (BadLocationException exc) {
                throw new BindingException(exc);
            }
        }

        private void saveValue() throws BindingException {
            try {
                String text = document.getText(0, document.getLength());
                property.writeFieldIfExtending(text);
            } catch (BadLocationException exc) {
                throw new BindingException(exc);
            }
        }
    }
}
