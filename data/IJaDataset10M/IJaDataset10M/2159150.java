package org.makagiga.plugins.mobilebarcode;

import static org.makagiga.commons.UI._;
import java.net.URI;
import java.util.Objects;
import javax.swing.text.JTextComponent;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.makagiga.commons.Globals;
import org.makagiga.commons.MDataAction;
import org.makagiga.commons.MLogger;
import org.makagiga.commons.TK;
import org.makagiga.commons.swing.MDialog;
import org.makagiga.commons.swing.MLabel;
import org.makagiga.commons.swing.MMenu;
import org.makagiga.commons.swing.MMessage;
import org.makagiga.commons.swing.MPanel;
import org.makagiga.commons.swing.MText;
import org.makagiga.plugins.GeneralPlugin;
import org.makagiga.plugins.PluginException;

public final class Plugin extends GeneralPlugin implements MText.GlobalMenu {

    private BarcodeAction linkAction;

    @Override
    public void onInit() throws PluginException {
    }

    @Override
    public void onPostInit() throws PluginException {
        linkAction = new BarcodeAction();
        Globals.addLinkAction(linkAction);
        MText.addGlobalMenu(this);
    }

    @Override
    public void onDestroy() throws PluginException {
        Globals.removeLinkAction(linkAction);
        MText.removeGlobalMenu(this);
    }

    @Override
    public void onGlobalMenu(final JTextComponent c, final MMenu menu) {
        String s = c.getSelectedText();
        menu.add(new BarcodeAction(TK.isEmpty(s) ? c.getText() : s));
    }

    private final class BarcodeAction extends MDataAction<String> {

        public BarcodeAction() {
            super(null, Plugin.this.getName());
        }

        public BarcodeAction(final String contents) {
            super(contents, Plugin.this.getName());
            setEnabled(!TK.isEmpty(contents));
        }

        @Override
        public String getData() {
            URI uri = this.getValue(Globals.LINK_URI, null);
            return (uri != null) ? uri.toString() : super.getData();
        }

        @Override
        public void onAction() {
            String contents = this.getData();
            MDialog dialog = new MDialog(this.getSourceWindow(), this.getName(), MDialog.SIMPLE_DIALOG);
            dialog.addWest(new BarcodePanel(BarcodeFormat.QR_CODE, contents));
            dialog.packFixed();
            dialog.exec();
        }
    }

    private static final class BarcodePanel extends MPanel {

        private BarcodePanel(final BarcodeFormat format, final String contents) {
            setToolTipText(format.getName());
            MLabel l = new MLabel();
            addCenter(l);
            if (contents == null) {
                l.setText(_("No Text"));
            } else {
                MultiFormatWriter writer = new MultiFormatWriter();
                try {
                    BitMatrix matrix = writer.encode(contents, format, 150, 150);
                    l.setImage(MatrixToImageWriter.toBufferedImage(matrix));
                } catch (WriterException exception) {
                    MLogger.exception(exception);
                    l.makeLargeMessage();
                    l.setIconName("ui/error");
                    l.setText(exception.getMessage());
                }
            }
        }
    }
}
