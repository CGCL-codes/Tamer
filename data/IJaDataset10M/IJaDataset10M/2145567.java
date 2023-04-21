package lektor.gui.help;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class HelpGui extends JPanel {

    private String helpHeader = "<html><center><h2>Hilfe zu LektOr</center></h2></html>";

    private String helpExplanation = "<html>Aufgrund der Möglichkeiten die uns " + "gegeben sind, haben wir uns entschieden dem Kunden ein " + "Benutzerhandbuch in HTML-Form zur Verfügung zu stellen. Dieses wird " + "nicht intern im Programm dargestellt, sondern durch den von Ihnen " + "favorisierten HTML-Browser interpretiert." + "<br><br>" + "Falls der Button unten auf dieser Maske nicht funktioniert muss " + "eine Anpassung der Konfigurtionsdatei durchgeführt werden." + "<br><br>" + "Nähere Informationen dazu entnehmen Sie bitte Ihrem " + "Benutzerhandbuch. Danke!</html>";

    private String help = "...weiter zur Hilfe";

    private JLabel lblHeader = new JLabel(helpHeader);

    private JLabel lblExplanation = new JLabel(helpExplanation);

    private JButton buttonHelp = new JButton(help);

    public HelpGui(ActionListener al) {
        super();
        appendActionListener(al);
        init();
        validate();
    }

    private void appendActionListener(ActionListener al) {
        buttonHelp.setActionCommand("helpinterface_help");
        buttonHelp.addActionListener(al);
    }

    public void init() {
        this.setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(lblHeader, BorderLayout.NORTH);
        panel.add(lblExplanation, BorderLayout.CENTER);
        buttonHelp.setRolloverEnabled(true);
        panel.add(buttonHelp, BorderLayout.SOUTH);
        this.add(panel, BorderLayout.CENTER);
    }
}
