package werkzeugkasten.twowaysql.editor;

import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.ui.editors.text.ForwardingDocumentProvider;
import org.eclipse.ui.editors.text.TextFileDocumentProvider;
import org.eclipse.ui.texteditor.IDocumentProvider;
import werkzeugkasten.twowaysql.Constants;

public class TwoWaySqlDocumentProvider extends TextFileDocumentProvider implements IPropertyChangeListener {

    public TwoWaySqlDocumentProvider() {
        IDocumentProvider provider = new TextFileDocumentProvider();
        provider = new ForwardingDocumentProvider(Constants.PARTITION_TYPE_TWOWAYSQL, new TwoWaySqlDocumentSetupParticipant(), provider);
        setParentDocumentProvider(provider);
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
    }
}
