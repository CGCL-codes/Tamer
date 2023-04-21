package org.gvsig.hyperlink.actions;

import java.io.Serializable;
import java.net.URI;
import org.gvsig.hyperlink.AbstractActionManager;
import org.gvsig.hyperlink.AbstractHyperLinkPanel;
import com.iver.andami.PluginServices;

public class PdfFormat extends AbstractActionManager implements Serializable {

    public static final String actionCode = "PDF_format";

    public AbstractHyperLinkPanel createPanel(URI doc) throws UnsupportedOperationException {
        return new PdfPanel(doc);
    }

    public String getActionCode() {
        return actionCode;
    }

    public boolean hasPanel() {
        return true;
    }

    public void showDocument(URI doc) {
        throw new UnsupportedOperationException();
    }

    public String getDescription() {
        return PluginServices.getText(this, "Shows_PDF_files_in_gvSIG");
    }

    public String getName() {
        return PluginServices.getText(this, "PDF_format");
    }
}
