package ossobook.client.gui.update.components.window;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import org.apache.commons.logging.LogFactory;
import ossobook.Messages;
import ossobook.client.base.metainfo.Project;
import ossobook.client.gui.common.OssobookFrame;
import ossobook.client.gui.update.components.other.GewoehnlichesSelbstkorrekturTextFeld;
import ossobook.client.gui.update.components.other.ProjektPanel;
import ossobook.client.gui.update.elements.other.NotizButton;
import ossobook.client.util.Configuration;
import ossobook.exceptions.StatementNotExecutedException;
import ossobook.queries.QueryManager;
import ossobook.queries.UserManager;

/**
 * @author ali
 * 
 */
public class ProjektVeraenderungsFenster extends JInternalFrame {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    private final QueryManager manager;

    private final Project project;

    private final JPanel maske;

    private final OssobookFrame parent;

    private final ProjektPanel window;

    public ProjektVeraenderungsFenster(QueryManager manager, Project project, OssobookFrame parentReference, ProjektPanel window) {
        super(Messages.getString("ProjektVeraenderungsFenster.0"));
        parent = parentReference;
        this.manager = manager;
        this.project = project;
        this.window = window;
        settings();
        maske = generiereMaske();
        setContentPane(maske);
    }

    private void settings() {
        this.setSize(Configuration.projektveraenderungsfenster_x, Configuration.projektveraenderungsfenster_y);
        setVisible(true);
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        setResizable(true);
        setMaximizable(true);
        setIconifiable(true);
        setClosable(true);
        setVisible(true);
    }

    /**
	 * 
	 */
    private JPanel generiereMaske() {
        JPanel result = new JPanel();
        try {
            String[][] info = manager.getProjektInformations(project);
            result.setLayout(new GridLayout(((info.length - 1) * 2 / 3), 3));
            for (String[] anInfo : info) {
                if ((anInfo[0].equals("projektNotiz")) || anInfo[0].equals("befundNotiz")) {
                    result.add(new JLabel(anInfo[0]));
                    NotizButton notebutton = new NotizButton(anInfo[0], parent);
                    notebutton.setNotizFeldText(anInfo[1]);
                    result.add(notebutton);
                } else {
                    if (!anInfo[0].equals("Zustand") && !anInfo[0].equals("Datum")) {
                        result.add(new JLabel(anInfo[0]));
                        GewoehnlichesSelbstkorrekturTextFeld cvtext = new GewoehnlichesSelbstkorrekturTextFeld(anInfo[1]);
                        if (anInfo[0].equals("ProjNr") || anInfo[0].equals("ProjEigentuemer") || anInfo[0].equals("zuletztSynchronisiert") || anInfo[0].equals("Datenbanknummer") || anInfo[0].equals("geloescht") || anInfo[0].equals("Nachrichtennummer")) {
                            cvtext.setEnabled(false);
                        } else {
                            cvtext.setEnabled(true);
                        }
                        result.add(cvtext);
                    }
                }
            }
        } catch (StatementNotExecutedException e) {
            LogFactory.getLog(ProjektVeraenderungsFenster.class).error(e, e);
        }
        JButton UpdateButton = new JButton(Messages.getString("ProjektVeraenderungsFenster.1"));
        UpdateButton.setBackground(Color.YELLOW);
        UpdateButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                updateAction();
            }
        });
        try {
            if (!(manager.getRight(project) == UserManager.WRITE) && !manager.getIsAdmin()) {
                UpdateButton.setEnabled(false);
            }
        } catch (StatementNotExecutedException e) {
            LogFactory.getLog(ProjektVeraenderungsFenster.class).error(e, e);
        }
        result.add(UpdateButton);
        return result;
    }

    /**
	 * 
	 */
    private void updateAction() {
        int components = maske.getComponentCount() - 1;
        String text;
        JLabel label;
        for (int i = 0; i < components; i = i + 2) {
            GewoehnlichesSelbstkorrekturTextFeld ctext;
            NotizButton notebtn;
            label = (JLabel) maske.getComponent(i);
            if ((label.getText().equals("projektNotiz")) || (label.getText().equals("befundNotiz"))) {
                notebtn = (NotizButton) maske.getComponent(i + 1);
                text = notebtn.toString();
            } else {
                ctext = (GewoehnlichesSelbstkorrekturTextFeld) maske.getComponent(i + 1);
                text = ctext.getText();
            }
            try {
                manager.updateProjekt(label.getText(), text, project);
            } catch (StatementNotExecutedException e) {
                LogFactory.getLog(ProjektVeraenderungsFenster.class).error(e, e);
            }
        }
        window.updateInfo();
        dispose();
    }
}
