package uk.ac.ebi.taxy;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;
import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

/**
 * This class defines a dialog for displaying descriptions of a collection of
 * available plug-ins and selecting one of them. To use it:
 * <ul>
 * <li> Create an instance with the constructor.
 * <li> Call the <code>show</code> method.
 * <li> Call the <code>getSelectedPlug-in</code>.
 * </ul>
 */
@SuppressWarnings("serial")
public class PluginDialog extends javax.swing.JDialog implements java.awt.event.ActionListener {

    private java.util.HashMap<String, PluginDescription> _descriptions;

    private java.util.HashMap<String, Class<TaxonomyPlugin>> _plugins;

    private javax.swing.JTextArea _description;

    private javax.swing.JButton _okButton;

    private javax.swing.JButton _cancelButton;

    private Class<TaxonomyPlugin> _selectedPlugin;

    /**
    * Constructor for creating a dialog of available plug-ins.
    * 
    * @param parentFrame
    *           The parent window.
    * @param plugins
    *           A collection of <code>Class</code> objects of classes that
    *           implement the interface <code>TaxonomyPlugin</code>.
    * @param descriptions
    *           A collection of <code>PluginDescription</code> objects that
    *           describe the implementations of the available plug-ins.
    */
    public PluginDialog(javax.swing.JFrame parentFrame, ArrayList<Class<TaxonomyPlugin>> plugins, ArrayList<Class<PluginDescription>> descriptions) {
        super(parentFrame, "Available Plug-ins Dialog", true);
        iniDescriptions(plugins, descriptions);
        javax.swing.ButtonGroup buttonGroup = new javax.swing.ButtonGroup();
        javax.swing.JPanel radioPanel = new javax.swing.JPanel();
        radioPanel.setLayout(new java.awt.GridLayout(0, 1));
        _description = new javax.swing.JTextArea("", 15, 30);
        _description.setEditable(false);
        for (int i = 0; i < plugins.size(); ++i) {
            Class<TaxonomyPlugin> pluginClass = plugins.get(i);
            PluginDescription desc = _descriptions.get(pluginClass.getName());
            JRadioButton button = new JRadioButton(pluginClass.getName());
            if (desc != null) {
                button.setText(desc.getName());
            }
            button.setActionCommand(desc.getPluginClassName());
            button.addActionListener(this);
            if (i == 0) {
                _description.setText(desc.getDescription());
                _selectedPlugin = pluginClass;
                button.setSelected(true);
            }
            buttonGroup.add(button);
            radioPanel.add(button);
        }
        _description.setLineWrap(true);
        _description.setWrapStyleWord(true);
        _okButton = new javax.swing.JButton("Ok");
        _okButton.addActionListener(this);
        _cancelButton = new javax.swing.JButton("Cancel");
        _cancelButton.addActionListener(this);
        javax.swing.JPanel buttonArea = new javax.swing.JPanel();
        buttonArea.add(_okButton);
        buttonArea.add(_cancelButton);
        JLabel message = new JLabel("  Select one of the following Plug-ins:");
        Container contentPane = getContentPane();
        contentPane.setLayout(new java.awt.BorderLayout());
        Box content = Box.createVerticalBox();
        Box selectionRegion = Box.createHorizontalBox();
        content.add(javax.swing.Box.createVerticalStrut(10));
        selectionRegion.add(javax.swing.Box.createHorizontalStrut(10));
        selectionRegion.add(radioPanel);
        selectionRegion.add(new javax.swing.JScrollPane(_description));
        selectionRegion.add(javax.swing.Box.createHorizontalStrut(10));
        content.add(selectionRegion);
        contentPane.add(message, BorderLayout.NORTH);
        contentPane.add(content, BorderLayout.CENTER);
        contentPane.add(buttonArea, BorderLayout.SOUTH);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        pack();
        centreWindow(parentFrame);
    }

    /**
    * Get the plug-in selected by the user.
    * 
    * @return The selected plug-in or <code>null</code> if none was selected.
    */
    public Class<TaxonomyPlugin> getSelectedPlugin() {
        return _selectedPlugin;
    }

    /**
    * React to the dialog's buttons. It react to events from the plug-in
    * selection radio buttons and the <i>OK</i> and <i>CANCEL</i> buttons.
    */
    public void actionPerformed(java.awt.event.ActionEvent event) {
        javax.swing.AbstractButton button = (javax.swing.AbstractButton) event.getSource();
        if (button == _okButton) {
            setVisible(false);
        } else if (button == _cancelButton) {
            _selectedPlugin = null;
            setVisible(false);
        } else {
            String className = button.getActionCommand();
            PluginDescription description = _descriptions.get(className);
            if (description != null) {
                _description.setText(description.getDescription());
            }
            _selectedPlugin = _plugins.get(className);
        }
    }

    private void centreWindow(javax.swing.JFrame parentFrame) {
        Point parentLocation = parentFrame.getLocation();
        Dimension parentSize = parentFrame.getSize();
        int parentPosX = (int) parentLocation.getX();
        int parentPosY = (int) parentLocation.getY();
        int parentSizeX = (int) parentSize.getWidth();
        int parentSizeY = (int) parentSize.getHeight();
        int sizeX = (int) getSize().getWidth();
        int sizeY = (int) getSize().getHeight();
        int posX = parentPosX + (parentSizeX - sizeX) / 2;
        int posY = parentPosY + (parentSizeY - sizeY) / 2;
        setLocation(posX, posY);
    }

    private void iniDescriptions(ArrayList<Class<TaxonomyPlugin>> classNames, ArrayList<Class<PluginDescription>> descriptions) {
        try {
            _descriptions = new HashMap<String, PluginDescription>();
            _plugins = new HashMap<String, Class<TaxonomyPlugin>>();
            for (int i = 0; i < classNames.size(); ++i) {
                Class<TaxonomyPlugin> c = classNames.get(i);
                _descriptions.put(c.getName(), null);
                _plugins.put(c.getName(), c);
            }
            for (int i = 0; i < classNames.size(); ++i) {
                Class<PluginDescription> descriptionClass = descriptions.get(i);
                PluginDescription desc = descriptionClass.newInstance();
                _descriptions.put(desc.getPluginClassName(), desc);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
