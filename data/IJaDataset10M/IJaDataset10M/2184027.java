package gui;

import chart.CandleStick;
import chart.TimeSeriesView;
import chart.View;
import com.toedter.calendar.JDateChooser;
import database.DataModel;
import exception.MyDBException;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.Date;
import java.sql.SQLException;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import util.GeneralProcedures;
import util.MarkadProperties;
import util.MyDBConnection;

/**
* This code was edited or generated using CloudGarden's Jigloo
* SWT/Swing GUI Builder, which is free for non-commercial
* use. If Jigloo is being used commercially (ie, by a corporation,
* company or business for any purpose whatever) then you
* should purchase a license for each developer using Jigloo.
* Please visit www.cloudgarden.com for details.
* Use of Jigloo implies acceptance of these licensing terms.
* A COMMERCIAL LICENSE HAS NOT BEEN PURCHASED FOR
* THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED
* LEGALLY FOR ANY CORPORATE OR COMMERCIAL PURPOSE.
*/
public class StockView extends javax.swing.JDialog implements MouseListener {

    private JSplitPane jSplitPane1;

    private JLabel jLabel2;

    private JDateChooser jDateChooserTo;

    private JDateChooser jDateChooserSince;

    private JButton jButtonClose;

    private JButton jButtonRefresh;

    private JTabbedPane jTabbedPaneTop;

    private JLabel jLabel3;

    private JPanel jPanelDown;

    private int stockID = -1;

    private String name = "";

    private View candleStick = new CandleStick();

    private View timeSeries = new TimeSeriesView();

    private DataModel dataModel;

    /**
	* Auto-generated main method to display this JDialog
	*/
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        StockView inst = new StockView(frame, 4);
        inst.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        inst.setVisible(true);
    }

    public StockView(JFrame frame, String name) {
        super(frame);
        this.name = name;
        dataModel = new DataModel();
        initGUI();
    }

    public StockView(JFrame frame, int stockID) {
        super(frame);
        this.stockID = stockID;
        dataModel = new DataModel();
        initGUI();
    }

    public void setChart(JPanel chart, String tabTitle) {
        chart.setSize(new Dimension(200, 192));
        jTabbedPaneTop.addTab(tabTitle, chart);
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == jButtonRefresh) {
            Date since = new Date(jDateChooserSince.getDate().getTime());
            Date to = new Date(jDateChooserTo.getDate().getTime());
            dataModel.executeDataRefresh(stockID, since, to);
        } else if (e.getSource() == jButtonClose) dispose();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    private void initGUI() {
        try {
            {
                jSplitPane1 = new JSplitPane();
                getContentPane().add(jSplitPane1, BorderLayout.CENTER);
                jSplitPane1.setOrientation(JSplitPane.VERTICAL_SPLIT);
                jSplitPane1.setDividerLocation(420);
                jSplitPane1.setOneTouchExpandable(true);
                jSplitPane1.setPreferredSize(new java.awt.Dimension(570, 477));
                {
                    jPanelDown = new JPanel();
                    jSplitPane1.add(jPanelDown, JSplitPane.BOTTOM);
                    GridBagLayout jPanel1Layout = new GridBagLayout();
                    jPanel1Layout.columnWidths = new int[] { 7, 7, 7, 7 };
                    jPanel1Layout.rowHeights = new int[] { 7, 7 };
                    jPanel1Layout.columnWeights = new double[] { 0.1, 0.1, 0.1, 0.1 };
                    jPanel1Layout.rowWeights = new double[] { 0.1, 0.1 };
                    jPanelDown.setLayout(jPanel1Layout);
                    {
                        jButtonClose = new JButton();
                        jPanelDown.add(jButtonClose, new GridBagConstraints(3, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                        jButtonClose.setText("Close");
                        jButtonClose.addMouseListener(this);
                    }
                    {
                        jLabel2 = new JLabel();
                        jPanelDown.add(jLabel2, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                        jLabel2.setText("Since:");
                    }
                    {
                        jLabel3 = new JLabel();
                        jPanelDown.add(jLabel3, new GridBagConstraints(2, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                        jLabel3.setText("To");
                    }
                    {
                        jDateChooserSince = new JDateChooser();
                        jPanelDown.add(jDateChooserSince, new GridBagConstraints(1, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                    }
                    {
                        jDateChooserTo = new JDateChooser();
                        jPanelDown.add(jDateChooserTo, new GridBagConstraints(3, 0, 1, 1, 0.0, 0.0, GridBagConstraints.WEST, GridBagConstraints.HORIZONTAL, new Insets(0, 0, 0, 0), 0, 0));
                    }
                    {
                        jButtonRefresh = new JButton();
                        jPanelDown.add(jButtonRefresh, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 2, 0));
                        jButtonRefresh.setText("Refresh");
                        jButtonRefresh.addMouseListener(this);
                    }
                }
                {
                    jTabbedPaneTop = new JTabbedPane();
                    jSplitPane1.add(jTabbedPaneTop, JSplitPane.TOP);
                    jTabbedPaneTop.setPreferredSize(new java.awt.Dimension(487, 139));
                }
                {
                }
            }
            jDateChooserSince.setDate(MarkadProperties.getDate(MarkadProperties.seriesFrom));
            jDateChooserTo.setDate(MarkadProperties.getDate(MarkadProperties.seriesTo));
            initChart();
            this.setSize(596, 520);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initChart() {
        MyDBConnection myC = new MyDBConnection();
        try {
            myC.init();
            if (stockID == -1) stockID = GeneralProcedures.getAzioneID(myC.getMyConnection(), name); else name = GeneralProcedures.getAzioneName(myC.getMyConnection(), stockID);
            setTitle("Chart for " + name);
            Date since = new Date(jDateChooserSince.getDate().getTime());
            Date to = new Date(jDateChooserTo.getDate().getTime());
            dataModel.addObserver(candleStick);
            dataModel.addObserver(timeSeries);
            dataModel.executeDataRefresh(stockID, since, to);
            setChart(candleStick.getPanel(), "Candle Stick");
            setChart(timeSeries.getPanel(), "Time serie");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (MyDBException e) {
        } finally {
            myC.close();
        }
    }
}
