package jskat.gui.main;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.BorderLayout;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Dimension;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.JScrollPane;
import javax.swing.border.EtchedBorder;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;
import java.awt.Toolkit;
import java.awt.Rectangle;
import jskat.control.JSkatMaster;
import jskat.data.JSkatDataModel;
import jskat.gui.JSkatGraphicRepository;

/**
 *
 * @author  Jan Sch�fer <jan.schaefer@b0n541.net>
 */
public class JSkatFrame extends JFrame implements Observer {

    /** Creates new form JSkatFrame */
    public JSkatFrame(JSkatMaster jskatMaster, JSkatDataModel dataModel, JSkatGraphicRepository jskatBitmaps) {
        this.dataModel = dataModel;
        this.dataModel.addObserver(this);
        jskatStrings = dataModel.getResourceBundle();
        this.jskatBitmaps = jskatBitmaps;
        this.jskatMaster = jskatMaster;
        initComponents();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     */
    private void initComponents() {
        setTitle("JSkat");
        AboutDialogAction aboutDialogAction = new AboutDialogAction(dataModel, this);
        HelpDialogAction helpDialogAction = new HelpDialogAction(dataModel, this);
        ExitJSkatAction exitJSkatAction = new ExitJSkatAction(jskatMaster);
        jSkatMenuBar = new JSkatMenuBar(dataModel, jskatBitmaps, aboutDialogAction, helpDialogAction, exitJSkatAction);
        setJMenuBar(jSkatMenuBar);
        jSkatToolBar = new JSkatToolBar(dataModel, jskatBitmaps, aboutDialogAction, helpDialogAction, exitJSkatAction);
        getContentPane().add(jSkatToolBar, BorderLayout.NORTH);
        playAreaPanel = new JSkatPlayArea(jskatMaster, dataModel, jskatBitmaps);
        getContentPane().add(playAreaPanel, BorderLayout.CENTER);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt) {
                jskatMaster.exitJSkat();
            }
        });
        infoPanel = new JPanel();
        infoPanel.setLayout(new GridBagLayout());
        GameScore gameScore = new GameScore(dataModel);
        dataModel.getCurrentRound().addObserver(gameScore);
        GridBagConstraints gridBagConstraints = new GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.insets = new Insets(5, 5, 5, 5);
        infoPanel.add(gameScore, gridBagConstraints);
        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new GridLayout(1, 0));
        JTable table = new JTable(new ScoreTableModel(dataModel));
        table.setShowHorizontalLines(false);
        table.setPreferredScrollableViewportSize(new Dimension(200, 400));
        table.setMinimumSize(new Dimension(200, 400));
        javax.swing.table.TableColumn column = null;
        for (int i = 0; i < 5; i++) {
            column = table.getColumnModel().getColumn(i);
            if (i == 4) {
                column.setPreferredWidth(5);
            } else {
                column.setPreferredWidth(30);
            }
        }
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(220, 400));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        gridBagConstraints.gridy = 1;
        infoPanel.add(scrollPane, gridBagConstraints);
        getContentPane().add(infoPanel, BorderLayout.WEST);
        statusBarPanel = new JPanel();
        statusBarPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
        statusBarPanel.setBorder(new EtchedBorder());
        statusBarLabel = new JLabel();
        statusBarLabel.setFont(new Font("Dialog", 0, 12));
        statusBarLabel.setText(jskatStrings.getString("greetings"));
        statusBarPanel.add(statusBarLabel);
        getContentPane().add(statusBarPanel, java.awt.BorderLayout.SOUTH);
        pack();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Rectangle frameSize = getBounds();
        setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
    }

    public void update(Observable o, Object arg) {
        System.out.println("JSkatFrame kriegt die neue Runde mit.");
    }

    /** Variables */
    private JSkatDataModel dataModel;

    private ResourceBundle jskatStrings;

    private JSkatGraphicRepository jskatBitmaps;

    private JSkatMaster jskatMaster;

    private JSkatToolBar jSkatToolBar;

    private JSkatMenuBar jSkatMenuBar;

    private JSkatPlayArea playAreaPanel;

    private JPanel infoPanel;

    private JPanel gameScore;

    private JPanel statusBarPanel;

    private JLabel statusBarLabel;
}
