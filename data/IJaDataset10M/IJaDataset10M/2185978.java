package ejp.presenter.gui;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import ejp.presenter.api.filters.parameters.AbstractParameter;
import ejp.presenter.api.util.CustomLogger;
import ejp.presenter.xml.ConfigurationManager;

/**
 * Parameters customization dialog.
 * 
 * @author Sebastien Vauclair
 * @version <code>$Revision: 1.6 $<br>$Date: 2005/02/14 12:06:21 $</code>
 */
public class ParametersDialog extends JDialog implements ActionListener {

    protected final JPanel jpButtons;

    protected final JButton jbOK;

    protected final JButton jbDefault;

    protected final JButton jbCancel;

    protected final AbstractParameter[] parameters;

    protected final Object[] previousParametersValues;

    protected final String filterName;

    protected boolean returnValue = false;

    protected boolean readOnly = false;

    public ParametersDialog(Dialog aParent, String aFilterName, AbstractParameter[] aParameters) {
        super(aParent, true);
        filterName = aFilterName;
        parameters = aParameters;
        previousParametersValues = new Object[parameters.length];
        JPanel jpMain = new JPanel();
        jpMain.setDoubleBuffered(true);
        jpMain.setLayout(new GridBagLayout());
        getContentPane().add(jpMain, BorderLayout.CENTER);
        GridBagConstraints gbcTemp = new GridBagConstraints();
        gbcTemp.gridx = 0;
        gbcTemp.fill = GridBagConstraints.BOTH;
        gbcTemp.weightx = 1;
        gbcTemp.weighty = 1;
        gbcTemp.insets = Utils.INSETS_5;
        for (int i = 0; i < aParameters.length; i++) {
            aParameters[i].setDialog(this);
            gbcTemp.gridy = i;
            jpMain.add(aParameters[i].getPanel(), gbcTemp);
        }
        jpButtons = new JPanel();
        jpButtons.setDoubleBuffered(true);
        jpButtons.setLayout(new FlowLayout(FlowLayout.RIGHT));
        getContentPane().add(jpButtons, BorderLayout.SOUTH);
        jbOK = addButton("OK");
        jbDefault = addButton("Set Default");
        jbCancel = addButton("Cancel");
        Utils.setSameSize(new JButton[] { jbOK, jbDefault, jbCancel });
        setResizable(false);
        setTitle(filterName);
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent aEvent) {
                closeDialog(true);
            }
        });
        pack();
        Utils.centerDialog(this, aParent);
    }

    /**
   * 
   * Requires <code>jpButtons</code> to be initialized.
   */
    protected JButton addButton(String aText) {
        JButton result = new JButton(aText);
        Utils.setCommonProperties(result);
        result.addActionListener(this);
        JPanel jpTemp = new JPanel();
        jpTemp.setDoubleBuffered(true);
        jpTemp.add(result);
        jpButtons.add(jpTemp);
        return result;
    }

    /**
   * Blocks until one of the OK or Cancel buttons is clicked or the window is
   * closed.
   * 
   * @return <code>true</code> iff the dialog was closed using the OK button
   */
    public boolean showDialog() {
        for (int i = 0; i < parameters.length; i++) previousParametersValues[i] = parameters[i].getValue();
        setVisible(true);
        return returnValue;
    }

    protected void closeDialog(boolean aCancel) {
        if (aCancel) for (int i = 0; i < parameters.length; i++) parameters[i].setValue(previousParametersValues[i]);
        returnValue = !aCancel;
        setVisible(false);
    }

    public void setReadOnly() {
        readOnly = true;
        setTitle(filterName + " (read-only)");
        for (int i = 0; i < parameters.length; i++) parameters[i].setReadOnly();
    }

    public void actionPerformed(ActionEvent aEvent) {
        Object src = aEvent.getSource();
        if (src == jbOK || src == jbCancel) {
            closeDialog(src == jbCancel);
        } else if (src == jbDefault) {
            ConfigurationManager.getInstance().saveParametersValues(filterName, parameters);
        } else CustomLogger.INSTANCE.warning("Unable to handle action event from an unknown source");
    }
}
