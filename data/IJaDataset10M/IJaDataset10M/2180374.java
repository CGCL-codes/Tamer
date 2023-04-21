package genestudio.Controllers.FileExplorer;

import genestudio.Controllers.ContigBuilder.ContigBuilderController;
import genestudio.Controllers.Main.MainWindowCode;
import genestudio.Interfaces.Messages.ErrorMessage;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 *
 * @author nrovinskiy
 */
public class NextListener implements ActionListener {

    MainWindowCode mwcMain;

    FileExplorerController fecHost;

    public NextListener(MainWindowCode main, FileExplorerController host) {
        this.mwcMain = main;
        fecHost = host;
    }

    public void actionPerformed(ActionEvent e) {
        if ((!fecHost.txtReference.getText().trim().isEmpty()) && (fecHost.getReference() == null)) {
            ErrorMessage errMsg = new ErrorMessage(fecHost, "<html>File " + fecHost.txtReference.getText().trim() + "<br> is broken or has unsupported format!<br><br></html>");
            errMsg.setVisible(true);
            errMsg = null;
            return;
        }
        fecHost.dispose();
        ContigBuilderController cbcContigd = new ContigBuilderController(mwcMain, fecHost.getReference(), fecHost.getContigs());
        cbcContigd.setVisible(true);
    }
}
