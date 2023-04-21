package gui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Observable;
import java.util.Observer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTextField;
import javax.swing.WindowConstants;
import exception.MyDBException;
import util.IndiciProcedures;
import util.MyDBConnection;
import util.StoricoProcedures;
import net.IQuoteHistorical;
import net.updater.HistoricalTables;
import net.updater.HistoricalUpdaterYahoo;
import net.updater.IHistoricalUpdater;

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
public class ProgressDialog extends javax.swing.JDialog implements Observer, MouseListener, Runnable {

    public void mouseClicked(MouseEvent e) {
        if (e.getSource() == jButtonClose) dispose();
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    private JLabel jLabelText;

    private JProgressBar jProgressBarStatus;

    private ArrayList<HistoricalUpdaterYahoo> subjectsList = new ArrayList<HistoricalUpdaterYahoo>();

    private JLabel jLabelDisplay;

    private JButton jButtonClose;

    /**
	* Auto-generated main method to display this JDialog
	*/
    public static void main(String[] args) throws IOException {
        JFrame frame = new JFrame();
        ProgressDialog inst = new ProgressDialog(frame);
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yy");
        inst.setVisible(true);
        Date from = null;
        Date to = null;
        MyDBConnection myC = new MyDBConnection();
        try {
            myC.init();
            from = new Date(sdf.parse("01-01-07").getTime());
            to = new Date(System.currentTimeMillis());
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        Connection c = myC.getMyConnection();
        for (int i = 1; i < 7; i++) {
            try {
                HistoricalUpdaterYahoo up = new HistoricalUpdaterYahoo(i, HistoricalTables.STORICO_INDICI, from, to);
                up.CompleteFields(c);
                inst.add(up);
            } catch (MyDBException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        myC.close();
        inst.run();
    }

    public ProgressDialog(JFrame frame) {
        super(frame);
        initGUI();
        setResizable(false);
    }

    private void initGUI() {
        try {
            GridBagLayout thisLayout = new GridBagLayout();
            thisLayout.rowWeights = new double[] { 0.1, 0.1, 0.1, 0.1 };
            thisLayout.rowHeights = new int[] { 7, 7, 7, 7 };
            thisLayout.columnWeights = new double[] { 0.1 };
            thisLayout.columnWidths = new int[] { 7 };
            getContentPane().setLayout(thisLayout);
            this.setTitle("Updating...");
            this.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
            {
                jLabelText = new JLabel();
                getContentPane().add(jLabelText, new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                jLabelText.setText("Status");
            }
            {
                jProgressBarStatus = new JProgressBar();
                jProgressBarStatus.setIndeterminate(true);
                getContentPane().add(jProgressBarStatus, new GridBagConstraints(0, 2, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
            }
            {
                jLabelDisplay = new JLabel();
                getContentPane().add(jLabelDisplay, new GridBagConstraints(0, 1, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                jLabelDisplay.setMaximumSize(new java.awt.Dimension(100, 20));
                jLabelDisplay.setText("Starting...");
            }
            {
                jButtonClose = new JButton();
                getContentPane().add(jButtonClose, new GridBagConstraints(0, 3, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0));
                jButtonClose.setText("Close");
                jButtonClose.addMouseListener(this);
            }
            this.setSize(256, 215);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void add(HistoricalUpdaterYahoo up) {
        up.addObserver(this);
        subjectsList.add(up);
    }

    public void run() {
        setEnabled(false);
        setVisible(true);
        MyDBConnection myC = new MyDBConnection();
        try {
            myC.init();
        } catch (SQLException e) {
            myC.close();
            JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
        Connection c = myC.getMyConnection();
        for (HistoricalUpdaterYahoo up : subjectsList) {
            up.run();
            up.swapToDB(c);
            try {
                c.commit();
                Thread.sleep(1000);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            } catch (InterruptedException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
        myC.close();
        setEnabled(true);
        jProgressBarStatus.setIndeterminate(false);
        jProgressBarStatus.setValue(100);
        jLabelDisplay.setText("Finshed");
    }

    public void sendMessage(String message) {
        jLabelDisplay.setText(message);
    }

    public void update(Observable o, Object arg) {
        IHistoricalUpdater up = (IHistoricalUpdater) o;
        jLabelDisplay.setText(up.getLog());
    }
}
