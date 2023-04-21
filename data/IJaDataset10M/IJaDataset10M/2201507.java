package fr.harlie.merge_pdf.action.mpdf;

import java.awt.event.ActionEvent;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.net.URL;
import javax.help.CSH;
import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;
import fr.harlie.merge_pdf.PdfMergerContext;
import fr.harlie.merge_pdf.action.AbstractPdfMergerAction;
import fr.harlie.merge_pdf.action.PdfActionEvent;

public class HelpAction extends AbstractPdfMergerAction {

    private static final long serialVersionUID = 1L;

    private static HelpAction action;

    public static HelpAction getAction() {
        if (HelpAction.action == null) {
            HelpAction.action = new HelpAction();
        }
        return HelpAction.action;
    }

    private HelpAction() {
        super("Sommaire de l'aide", new ImageIcon(PdfMergerContext.class.getClassLoader().getResource("icons/About16.gif")));
        this.putValue(Action.SHORT_DESCRIPTION, "Afficher la bo�te � propos de l'application.");
        this.putValue(Action.LONG_DESCRIPTION, "Afficher la bo�te � propos de l'application.");
        this.putValue(Action.MNEMONIC_KEY, KeyEvent.VK_A);
        this.putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(KeyEvent.VK_F1, InputEvent.SHIFT_MASK));
    }

    protected PdfActionEvent executeAction(ActionEvent e) {
        String helpHS = "help/fr/helpset.hs";
        ClassLoader cl = HelpAction.class.getClassLoader();
        try {
            URL hsURL = HelpSet.findHelpSet(cl, helpHS);
            HelpSet hs = new HelpSet(cl, hsURL);
            HelpBroker hb = hs.createHelpBroker();
            new CSH.DisplayHelpFromSource(hb).actionPerformed(e);
        } catch (Exception ee) {
            System.out.println("HelpSet " + ee.getMessage());
            System.out.println("HelpSet " + helpHS + " not found");
        }
        return null;
    }
}
