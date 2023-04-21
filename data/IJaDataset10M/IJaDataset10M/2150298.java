package gov.sns.apps.template;

import gov.sns.application.*;
import javax.swing.*;

/**
 * TemplateViewerWindow
 *
 * @author  somebody
 */
class TemplateWindow extends XalWindow implements SwingConstants {

    /** Creates a new instance of MainWindow */
    public TemplateWindow(final XalDocument aDocument) {
        super(aDocument);
        setSize(800, 600);
    }
}
