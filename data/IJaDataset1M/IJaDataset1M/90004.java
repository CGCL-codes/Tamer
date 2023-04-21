package weka.gui;

import weka.core.Instances;
import weka.gui.arffviewer.ArffPanel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * A downsized version of the ArffViewer, displaying only one Instances-Object.
 *
 *
 * @see weka.gui.arffviewer.ArffViewer
 * @author FracPete (fracpete at waikato dot ac dot nz)
 * @version $Revision: 1.4 $ 
 */
public class ViewerDialog extends JDialog implements ChangeListener {

    /** for serialization */
    private static final long serialVersionUID = 6747718484736047752L;

    /** Signifies an OK property selection */
    public static final int APPROVE_OPTION = 0;

    /** Signifies a cancelled property selection */
    public static final int CANCEL_OPTION = 1;

    /** the result of the user's action, either OK or CANCEL */
    protected int m_Result = CANCEL_OPTION;

    /** Click to activate the current set parameters */
    protected JButton m_OkButton = new JButton("OK");

    /** Click to cancel the dialog */
    protected JButton m_CancelButton = new JButton("Cancel");

    /** Click to undo the last action */
    protected JButton m_UndoButton = new JButton("Undo");

    /** the panel to display the Instances-object */
    protected ArffPanel m_ArffPanel = new ArffPanel();

    /**
   * initializes the dialog with the given parent
   * 
   * @param parent the parent for this dialog
   */
    public ViewerDialog(Frame parent) {
        super(parent, true);
        createDialog();
    }

    /**
   * creates all the elements of the dialog
   */
    protected void createDialog() {
        JPanel panel;
        setTitle("Viewer");
        getContentPane().setLayout(new BorderLayout());
        m_ArffPanel.addChangeListener(this);
        getContentPane().add(m_ArffPanel, BorderLayout.CENTER);
        panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(panel, BorderLayout.SOUTH);
        m_UndoButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                undo();
            }
        });
        getContentPane().add(panel, BorderLayout.SOUTH);
        m_CancelButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                m_Result = CANCEL_OPTION;
                setVisible(false);
            }
        });
        m_OkButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                m_Result = APPROVE_OPTION;
                setVisible(false);
            }
        });
        panel.add(m_UndoButton);
        panel.add(m_OkButton);
        panel.add(m_CancelButton);
        pack();
    }

    /**
   * sets the instances to display
   */
    public void setInstances(Instances inst) {
        m_ArffPanel.setInstances(new Instances(inst));
    }

    /**
   * returns the currently displayed instances
   */
    public Instances getInstances() {
        return m_ArffPanel.getInstances();
    }

    /**
   * sets the state of the buttons 
   */
    protected void setButtons() {
        m_OkButton.setEnabled(true);
        m_CancelButton.setEnabled(true);
        m_UndoButton.setEnabled(m_ArffPanel.canUndo());
    }

    /**
   * returns whether the data has been changed
   * 
   * @return true if the data has been changed
   */
    public boolean isChanged() {
        return m_ArffPanel.isChanged();
    }

    /**
   * undoes the last action 
   */
    private void undo() {
        m_ArffPanel.undo();
    }

    /**
   * Invoked when the target of the listener has changed its state.
   */
    public void stateChanged(ChangeEvent e) {
        setButtons();
    }

    /**
   * Pops up the modal dialog and waits for Cancel or OK.
   *
   * @return either APPROVE_OPTION, or CANCEL_OPTION
   */
    public int showDialog() {
        m_Result = CANCEL_OPTION;
        setVisible(true);
        setButtons();
        return m_Result;
    }

    /**
   * Pops up the modal dialog and waits for Cancel or OK.
   *
   * @param inst the instances to display
   * @return either APPROVE_OPTION, or CANCEL_OPTION
   */
    public int showDialog(Instances inst) {
        setInstances(inst);
        return showDialog();
    }
}
