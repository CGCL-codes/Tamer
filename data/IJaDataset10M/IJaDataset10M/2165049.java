package org.vmaster.client.gui;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.text.MessageFormat;
import java.util.EventObject;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import org.dhiller.presentation.gui.FrameView;
import org.vmaster.client.commands.CmdCreateRepository;
import org.vmaster.common.exceptions.VMasterServerException;
import biz.xsoftware.impl.presentation.gui.JFrameJDialog;
import biz.xsoftware.impl.presentation.swing.GraphPaperConstraints;
import biz.xsoftware.impl.presentation.swing.GraphPaperLayout;
import biz.xsoftware.impl.remote.adapters.TransportException;
import biz.xsoftware.impl.thread.ExceptionHandler;

public class CreateRepositoryView extends FrameView {

    private static final String className = CreateRepositoryView.class.getName();

    private static final Logger log = Logger.getLogger(className);

    JTextField nameField;

    JTextField pathField;

    public void addComponents(JFrameJDialog f) {
        f.setLocation(150, 150);
        f.setSize(300, 200);
        Container cont = f.getContentPane();
        GraphPaperLayout graph = new GraphPaperLayout();
        cont.setLayout(graph);
        JLabel nameLabel = new JLabel(bundle.getString("label.repository.name"));
        nameField = new JTextField();
        JLabel pathLabel = new JLabel(bundle.getString("label.repository.path"));
        pathField = new JTextField();
        nameField.setText("htmlprojects");
        pathField.setText("REPOSITORY");
        Dimension dim = nameField.getPreferredSize();
        dim.width = 300;
        nameField.setPreferredSize(dim);
        dim = pathField.getPreferredSize();
        dim.width = 300;
        pathField.setPreferredSize(dim);
        JButton okButton = new JButton(globalBundle.getString("button.ok"));
        JButton cancelButton = new JButton(globalBundle.getString("button.cancel"));
        GraphPaperConstraints c = new GraphPaperConstraints();
        c.insets = new Insets(3, 3, 3, 3);
        c.anchor = GridBagConstraints.WEST;
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 1;
        c.gridheight = 1;
        cont.add(nameLabel, c);
        c.gridx = 1;
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridheight = 1;
        cont.add(nameField, c);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        cont.add(pathLabel, c);
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 3;
        c.gridheight = 1;
        cont.add(pathField, c);
        c.anchor = GridBagConstraints.EAST;
        c.insets.right = 15;
        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 2;
        c.gridheight = 1;
        cont.add(okButton, c);
        c.anchor = GridBagConstraints.WEST;
        c.insets.left = 15;
        c.gridx = 2;
        c.gridy = 2;
        c.gridwidth = 2;
        c.gridheight = 1;
        cont.add(cancelButton, c);
        Dimension prefSize = graph.getPreferredLayoutSize(cont);
        f.setSize(prefSize.width + 10, prefSize.height + 25);
        adapter.registerEventHandler(okButton, "actionPerformed", this, "onOkButtonClick");
        adapter.registerEventHandler(cancelButton, "actionPerformed", this, "onExitWindow");
    }

    protected String getClassName() {
        return className;
    }

    public void onOkButtonClick(EventObject event) {
        String name = nameField.getText();
        String path = pathField.getText();
        MessageFormat formatter = new MessageFormat("");
        CmdCreateRepository busLogic = new CmdCreateRepository();
        try {
            busLogic.createRepository(name, path);
        } catch (VMasterServerException e) {
            JOptionPane.showMessageDialog((Component) window, e.getMessage(), globalBundle.getString("optionpane.error"), JOptionPane.ERROR_MESSAGE);
            return;
        } catch (TransportException e) {
            ExceptionHandler.handle(e);
            return;
        }
        Object[] messageArguments = { name, path };
        formatter.applyPattern(globalBundle.getString("optionpane.success.message"));
        String message = formatter.format(messageArguments);
        JOptionPane.showMessageDialog((Component) window, message, globalBundle.getString("optionpane.success"), JOptionPane.INFORMATION_MESSAGE);
        onExitWindow(null);
    }
}
