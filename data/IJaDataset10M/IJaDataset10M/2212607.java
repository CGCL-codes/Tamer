package org.ndx.majick.ui.string;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.text.Document;
import org.ndx.majick.properties.Property;
import org.ndx.majick.ui.IClassUIProvider;
import org.ndx.majick.ui.IPropertyUIMetadata;
import org.ndx.majick.ui.object.ObjectUIProvider;

/**
 * Provides UI components for a String object
 * @author Nicolas Delsaux
 *
 */
public class StringUIProvider extends ObjectUIProvider implements IClassUIProvider {

    public StringPropertyMetadata createMetadata() {
        return new StringPropertyMetadata();
    }

    public JComponent getEditor(IPropertyUIMetadata metadata, Property<?> source) {
        StringPropertyMetadata stringMeta = (StringPropertyMetadata) metadata;
        JTextField textField = new JTextField();
        Document document = new StringBackedDocument((Property<String>) source, stringMeta);
        textField.setDocument(document);
        configureEditor(textField, metadata, source);
        return textField;
    }

    public JComponent getViewer(final IPropertyUIMetadata metadata, final Property<?> source) {
        final JLabel returned = new JLabel();
        returned.setText(source.get().toString());
        source.addPropertyChangeListener(new PropertyChangeListener() {

            public void propertyChange(PropertyChangeEvent evt) {
                returned.setText(source.get().toString());
            }
        });
        configureViewer(returned, metadata, source);
        return returned;
    }
}
