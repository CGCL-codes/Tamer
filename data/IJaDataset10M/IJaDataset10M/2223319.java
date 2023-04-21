package net.sf.ajio.ihm;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JButton;
import net.olivierChabrol.ihm.OCPanel;
import net.sf.ajio.ihm.comp.FileChooser;
import net.sf.ajio.ihm.util.Language;
import net.sf.ajio.util.ClassPath;
import org.apache.log4j.Logger;

/**
 * AJIO
 *
 * @author Olivier CHABROL
 * @copyright (C)2004 Olivier CHABROL
 * AJIO
 */
public class PreferencesPanel extends OCPanel implements ActionListener {

    private static Logger _log = Logger.getLogger(PreferencesPanel.class.getName());

    private int longueur = 460;

    public PreferencesPanel() {
        super("preferencesPanel");
        initComponent();
        initField();
    }

    private void initField() {
        setEnabled("tJVMPath", false);
        setValue("tTmpPath", (new File(".")).getAbsolutePath());
        setValue("tJVMPath", ClassPath.getInstance().getJavaHome());
        setValue("tToPath", (new File(".")).getAbsolutePath());
    }

    private void initComponent() {
        setLayout(null);
        addLabelField("lJVMPath", Language.getInstance().getLabel("lJVMPath"), 5, 5, 200, 21);
        addTextField("tJVMPath", Language.getInstance().getLabel("tt.tJVMPath"), 5, 25, longueur, 21);
        JButton button = addButton("bJVMPath", Language.getInstance().getLabel("bJVMPath"), Language.getInstance().getLabel("tt.bJVMPath"), longueur + 10, 25, 30, 21);
        button.addActionListener(this);
        addLabelField("lToPath", Language.getInstance().getLabel("lToPath"), 5, 175, 200, 21);
        addTextField("tToPath", Language.getInstance().getLabel("tt.tToPath"), 5, 200, longueur, 21);
        button = addButton("bToPath", Language.getInstance().getLabel("bToPath"), Language.getInstance().getLabel("tt.bToPath"), longueur + 10, 200, 30, 21);
        button.addActionListener(this);
        addLabelField("lTmpPath", Language.getInstance().getLabel("lTmpPath"), 5, 125, 200, 21);
        addTextField("tTmpPath", Language.getInstance().getLabel("tt.tTmpPath"), 5, 150, longueur, 21);
        button = addButton("bTmpPath", Language.getInstance().getLabel("bTmpPath"), Language.getInstance().getLabel("tt.bTmpPath"), longueur + 10, 150, 30, 21);
        button.addActionListener(this);
    }

    private void chooseDir(String compName) {
        String realCompName = 't' + compName;
        boolean showDirOnly = false;
        if (realCompName.equals("tJVMPath") || realCompName.equals("tTmpPath")) showDirOnly = true;
        FileChooser fc = new FileChooser(getOCComponent(realCompName), showDirOnly);
        fc.showOpenDialog(this);
        setValue(realCompName, fc.getCurrentDirectory().getAbsolutePath());
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource().equals(getComponent("bJVMPath"))) {
            chooseDir("JVMPath");
        } else if (e.getSource().equals(getComponent("bTmpPath"))) {
            chooseDir("TmpPath");
        }
    }
}
