package eu.livotov.tpt.demo.dialogs;

import com.vaadin.terminal.ExternalResource;
import com.vaadin.terminal.Resource;
import com.vaadin.terminal.ThemeResource;
import eu.livotov.tpt.TPTApplication;
import eu.livotov.tpt.demo.api.DemoItem;
import eu.livotov.tpt.gui.dialogs.DownloadDialog;
import eu.livotov.tpt.gui.dialogs.OptionDialog;
import eu.livotov.tpt.gui.dialogs.OptionKind;
import eu.livotov.tpt.i18n.TM;
import java.io.Serializable;

/**
 *
 * @author dll
 */
public class ConfirmationDialogDemoItem implements DemoItem, Serializable {

    public ConfirmationDialogDemoItem() {
    }

    public boolean hasSourceCode() {
        return true;
    }

    public boolean hasShowCase() {
        return true;
    }

    public String getItemName() {
        return TM.get("cdd.title");
    }

    public Resource getIcon() {
        return new ThemeResource("icons/confirmationdialog.png");
    }

    public String getItemDescription() {
        return TM.get("cdd.info");
    }

    public String getItemSourceCode() {
        return "" + "final OptionDialog dlg = new OptionDialog ( TPTApplication.getCurrentApplication () );\n" + "dlg.showConfirmationDialog ( \"Hello\", \"That's a deal. Agree ?\", new OptionDialog.OptionDialogResultListener () {\n" + "\tpublic void dialogClosed ( OptionKind closeEvent )\n" + "\t{\n" + "\t\tTPTApplication.getCurrentApplication ().getMainWindow ().showNotification ( \"The deal was \" + + closeEvent.name ());\n" + "\t}\n" + "});";
    }

    public void performShowCase() {
        final OptionDialog dlg = new OptionDialog(TPTApplication.getCurrentApplication());
        dlg.showConfirmationDialog(TM.get("cdd.title"), TM.get("cdd.message"), new OptionDialog.OptionDialogResultListener() {

            public void dialogClosed(OptionKind closeEvent) {
                TPTApplication.getCurrentApplication().getMainWindow().showNotification(TM.get("cdd.reply") + closeEvent.name());
            }
        });
    }
}
