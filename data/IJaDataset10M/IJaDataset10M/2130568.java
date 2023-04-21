package au.gov.qld.dnr.dss.view;

import au.gov.qld.dnr.dss.v1.framework.Framework;
import au.gov.qld.dnr.dss.v1.framework.interfaces.ResourceManager;
import au.gov.qld.dnr.dss.view.support.TitlePanel;
import au.gov.qld.dnr.dss.view.support.PositionComponentDecorator;
import au.gov.qld.dnr.dss.misc.exception.ResourceNotFoundException;
import au.gov.qld.dnr.dss.misc.Global;
import javax.swing.*;
import javax.swing.border.*;
import java.util.Vector;
import java.awt.*;
import java.awt.event.*;

/**
 * A DSS Dialog.
 */
public class DSSOkCancelDialog extends JDialog {

    /** Main component. */
    Component _component = null;

    Font _fontTitle = new Font("Helvetica", Font.PLAIN, 18);

    GridBagLayout _layoutButtons;

    GridBagConstraints _c;

    JLabel _labelTitle = new JLabel();

    JPanel _panelButtons = new JPanel();

    JButton _buttonOk = new JButton(resources.getProperty("dss.gui.button.ok.label", "OK"));

    JButton _buttonCancel = new JButton(resources.getProperty("dss.gui.button.cancel.label", "CANCEL"));

    public DSSOkCancelDialog(Frame frame, String title) {
        super(frame, title, true);
        getContentPane().setLayout(new BorderLayout());
        PositionComponentDecorator _positiondecorator = new PositionComponentDecorator(this, frame);
        _positiondecorator.setLocation();
        _labelTitle.setFont(_fontTitle);
        _layoutButtons = new GridBagLayout();
        _panelButtons.setLayout(_layoutButtons);
        _labelTitle.setBorder(getTitledBorder(null));
        _panelButtons.setBorder(getTitledBorder(null));
        _c = new GridBagConstraints();
        _c.insets = new Insets(10, 10, 10, 10);
        _c.gridx = 0;
        _c.gridy = 0;
        _c.weightx = 1.0;
        _c.weighty = 1.0;
        _c.anchor = GridBagConstraints.WEST;
        _layoutButtons.setConstraints(_buttonOk, _c);
        _panelButtons.add(_buttonOk);
        _c.gridx = 1;
        _c.gridy = 0;
        _c.weightx = 1.0;
        _c.weighty = 1.0;
        _c.anchor = GridBagConstraints.EAST;
        _layoutButtons.setConstraints(_buttonCancel, _c);
        _panelButtons.add(_buttonCancel);
        _labelTitle.setHorizontalAlignment(SwingConstants.CENTER);
        getContentPane().add(BorderLayout.SOUTH, _panelButtons);
        getContentPane().add(BorderLayout.NORTH, _labelTitle);
    }

    public void setTitleLabel(String title) {
        _labelTitle.setText(title);
    }

    public void setComponent(Component component) {
        _component = component;
        getContentPane().add(BorderLayout.CENTER, _component);
    }

    public void addOkButtonListener(ActionListener listener) {
        _buttonOk.addActionListener(listener);
    }

    public void addCancelButtonListener(ActionListener listener) {
        _buttonCancel.addActionListener(listener);
    }

    private Border getTitledBorder(String title) {
        Font borderFont = new Font("Sanserif", Font.BOLD, 14);
        Color borderColor = new Color(128, 0, 0);
        return BorderFactory.createTitledBorder(null, title, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, borderFont, borderColor);
    }

    static ResourceManager resources = Framework.getGlobalManager().getResourceManager();
}
