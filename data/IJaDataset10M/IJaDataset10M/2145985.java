package org.mooym;

import java.awt.BorderLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.Action;
import javax.swing.ActionMap;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import org.jdesktop.application.Application;
import org.jdesktop.application.ResourceMap;
import org.mooym.swing.AudioListConverter;
import org.mooym.swing.BindingUtils;
import org.mooym.swing.SwingComponentFactory;

/**
 * This is the main frame of the application.
 * 
 * @author roesslerj
 */
public class MainFrame extends JFrame {

    private static final String ACTION_OPEN_BROWSEFOLDERDIALOG = "openBrowseFolderDialog";

    private static final String FRAME_NAME = "mainFrame";

    /**
   * Windows adapter to ask before exit.
   */
    private class MainFrameListener extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {
            int option = JOptionPane.showConfirmDialog(MainFrame.this, "Do you really want to exit?", "Really Exit?", JOptionPane.YES_NO_OPTION);
            if (option == JOptionPane.YES_OPTION) {
                mooym.exit(e);
            }
        }
    }

    private static final SwingComponentFactory componentFactory = new SwingComponentFactory(FRAME_NAME);

    private final JButton startIndexBtn = componentFactory.createButton("startIndexBtn");

    private final JLabel statusLbl = componentFactory.createLabel("statusLbl");

    private final JList audioRecordsLst = componentFactory.createList("audioRecordsLst");

    private final JMenu fileMenu = componentFactory.createMenu("fileMnu");

    private final MooymData mooymData;

    private final Application mooym;

    private final JTextField rootPathTF = componentFactory.createTextField("rootPathTxf");

    private final boolean stopEnabled = false;

    public MainFrame(MooymData mooymData) {
        mooym = Application.getInstance();
        this.mooymData = mooymData;
        setName(FRAME_NAME);
        initializeGui();
        bind();
        finalizeGui();
    }

    private void bind() {
        BindingUtils.bindTextField(mooymData, MooymData.ROOT_PATH, rootPathTF);
        BindingUtils.bindTextField(mooymData, MooymData.STATUS_MSG, statusLbl);
        BindingUtils.bindList(mooymData, MooymData.AUDIO_RECORD_LIST, audioRecordsLst, new AudioListConverter(mooymData.getAudioRecordList()));
    }

    private void finalizeGui() {
        addWindowListener(new MainFrameListener());
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        ResourceMap resourceMap = mooym.getContext().getResourceMap(getClass());
        resourceMap.injectComponents(this);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void initializeGui() {
        ActionMap actionMap = mooym.getContext().getActionMap();
        Action startIndexingAction = actionMap.get(Mooym.ACTION_START_INDEXING);
        Action stopIndexingAction = actionMap.get(Mooym.ACTION_STOP_INDEXING);
        audioRecordsLst.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        audioRecordsLst.setPrototypeCellValue(mooymData.getRootPath());
        Border border = new EmptyBorder(2, 4, 2, 4);
        JScrollPane scrollPane = new JScrollPane(audioRecordsLst);
        scrollPane.setBorder(border);
        rootPathTF.setAction(startIndexingAction);
        startIndexBtn.setAction(startIndexingAction);
        JPanel panel = new JPanel();
        panel.add(rootPathTF);
        panel.add(startIndexBtn);
        statusLbl.setBorder(border);
        add(scrollPane, BorderLayout.CENTER);
        add(panel, BorderLayout.NORTH);
        add(statusLbl, BorderLayout.SOUTH);
        Action startMoodyTagWizard = actionMap.get(Mooym.ACTION_START_MOODY_TAGWIZARD);
        JMenuBar menuBar = new JMenuBar();
        fileMenu.add(new JMenuItem(startMoodyTagWizard));
        menuBar.add(fileMenu);
        setJMenuBar(menuBar);
    }

    public JButton getIndexButton() {
        return startIndexBtn;
    }
}
