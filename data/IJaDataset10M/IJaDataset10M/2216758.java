package herschel.phs.prophandler.tools.missionevolver.gui;

import herschel.phs.prophandler.tools.missionevolver.MissionEvolverMain;
import herschel.phs.prophandler.tools.missionevolver.gui.dialog.AboutDialog;
import herschel.phs.prophandler.tools.missionevolver.gui.dialog.CommonDialog;
import herschel.phs.prophandler.tools.missionevolver.util.PropertyHandler;
import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

public class MissionEvolverFrame extends JFrame {

    public static ImageIcon ICON = new ImageIcon(AboutDialog.class.getResource(MissionEvolverMain.RESOURCE_BASE_DIR + "gear_small.png"));

    private MissionEvolverMediator m_mediator;

    private AboutDialog m_acercaDe;

    public MissionEvolverFrame() {
        super("Mission Evolver Tool");
        setIconImage(ICON.getImage());
        setSize(600, 600);
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        List<String> databases = null;
        String databaseString = PropertyHandler.getInstance().getProperty("hcss.databases");
        if (databaseString != null) {
            databases = Arrays.asList(databaseString.split(";"));
        }
        CommonDialog cd = new CommonDialog(this, true, "Database selection");
        cd.addField("Database", databases.toArray());
        cd.init();
        cd.setVisible(true);
        String database = cd.getResult("Database");
        cd.dispose();
        System.setProperty("hcss.phs.database", database);
        System.setProperty("var.hcss.dir", PropertyHandler.getInstance().getProperty("var.hcss.dir"));
        m_mediator = new MissionEvolverMediator(this);
        setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent e) {
                exit();
            }
        });
        setVisible(true);
    }

    public void exit() {
        int confirm = JOptionPane.showOptionDialog(this, "Do you really want to exit?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
        if (confirm == 0) {
            dispose();
            System.exit(0);
        }
    }
}
