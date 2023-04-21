package toxTree.apps;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.border.EtchedBorder;
import toxTree.ui.EditorFactory;
import toxtree.data.DataContainer;
import toxtree.data.DataModule;
import toxtree.data.DecisionMethodsDataModule;
import toxtree.ui.DataModulePanel;
import toxtree.ui.StatusBar;
import toxtree.ui.editors.SwingEditorFactory;
import toxtree.ui.molecule.TopPanel;
import toxtree.ui.tree.molecule.CompoundPanel;

public abstract class CompoundMethodApplication extends AbstractApplication {

    protected JSplitPane splitPanel;

    protected JPanel mainPanel;

    protected CompoundPanel compoundPanel;

    protected DataModulePanel dataModulePanel;

    protected TopPanel strucEntryPanel;

    protected File fileToOpen = null;

    public CompoundMethodApplication(String title, Color bgColor, Color fColor) {
        super(title);
        mainFrame.getContentPane().add(createMenuBar(), BorderLayout.NORTH);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        compoundPanel = createCompoundPanel(dataModule.getDataContainer(), bgColor, fColor);
        if (((DecisionMethodsDataModule) dataModule).getTreeResult() != null) ((DecisionMethodsDataModule) dataModule).getTreeResult().addPropertyChangeListener(compoundPanel);
        dataModulePanel = createDataModulePanel(dataModule);
        strucEntryPanel = new TopPanel();
        strucEntryPanel.setAutoscrolls(false);
        strucEntryPanel.setDataContainer(dataModule.getDataContainer());
        splitPanel = createSplitPanel(JSplitPane.HORIZONTAL_SPLIT);
        mainPanel.add(strucEntryPanel, BorderLayout.NORTH);
        mainPanel.add(splitPanel, BorderLayout.CENTER);
        JPanel sPanel = createStatusBar();
        mainFrame.getContentPane().add(sPanel, BorderLayout.SOUTH);
        mainFrame.getContentPane().add(mainPanel, BorderLayout.CENTER);
        String t = getTitle();
        if ((t != null) && (!t.equals(""))) mainFrame.setTitle(t.toString());
        mainFrame.pack();
        mainFrame.setVisible(true);
        centerScreen();
    }

    protected JSplitPane createSplitPanel(int splitDirection) {
        JSplitPane sp = new JSplitPane(splitDirection, compoundPanel, dataModulePanel);
        sp.setOneTouchExpandable(false);
        sp.setDividerLocation(300);
        return sp;
    }

    protected JPanel createStatusBar() {
        StatusBar sPanel = new StatusBar();
        if (((DecisionMethodsDataModule) dataModule).getTreeResult() != null) ((DecisionMethodsDataModule) dataModule).getTreeResult().addPropertyChangeListener(sPanel);
        sPanel.setPreferredSize(new Dimension(300, 24));
        sPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
        sPanel.setDataContainer(dataModule.getDataContainer());
        return sPanel;
    }

    protected abstract String getTitle();

    protected CompoundPanel createCompoundPanel(DataContainer dataContainer, Color bgColor, Color fColor) {
        CompoundPanel cp = new CompoundPanel(dataModule.getDataContainer(), bgColor, fColor);
        cp.setPreferredSize(new Dimension(300, 500));
        cp.setMinimumSize(new Dimension(100, 200));
        return cp;
    }

    protected abstract DataModulePanel createDataModulePanel(DataModule dataModule);

    @Override
    protected void parseCmdArgs(String[] args) {
        char option;
        int p = 0;
        while (p < args.length) {
            option = args[p].charAt(0);
            if (option != '-') continue;
            option = args[p].charAt(1);
            switch(option) {
                case 'f':
                    {
                        p++;
                        if (p >= args.length) break;
                        fileToOpen = new File(args[p]);
                        if (!fileToOpen.exists()) {
                            logger.error("File do not exists!\t", fileToOpen.getAbsolutePath());
                            fileToOpen = null;
                        }
                        break;
                    }
            }
            p++;
        }
    }

    @Override
    protected DataModule createDataModule() {
        return null;
    }

    @Override
    protected ImageIcon getIcon() {
        return null;
    }
}
