package ro.masc.server.gui;

import ro.masc.server.gui.listener.ActionKeyListener;
import javax.swing.*;
import java.awt.*;
import java.util.EventObject;

/**
 * Description
 *
 * @author <a href="mailto:andrei.chiritescu@gmail.com">Andrei Chiritescu</a>
 * @version $Revision: 1.2 $
 *          $Date: 2005/05/08 21:04:25 $
 */
public class CreateAgletWindow extends JDialog {

    public static final String PATH_LABEL = "window.create.label.path";

    public static final String CREATE_BUTTON_LABEL = "window.create.label.button.ok";

    public static final String CLOSE_BUTTON_LABEL = "window.create.label.button.close";

    public static final String ADD_BUTTON_LABEL = "window.create.label.button.add";

    private JTextField pathField;

    private JList optionField;

    private JButton buttonClose;

    private JButton buttonOk;

    private JButton buttonAdd;

    Object selVal;

    MainWindow main;

    public CreateAgletWindow(MainWindow parent) throws HeadlessException {
        super(parent, true);
        main = parent;
        JLabel usernameLabel = new JLabel(parent.resolve(PATH_LABEL));
        pathField = new JTextField();
        pathField.setColumns(20);
        JPanel pathPanel = new JPanel();
        pathPanel.setLayout(new FlowLayout());
        pathPanel.add(usernameLabel);
        pathPanel.add(pathField);
        buttonAdd = new JButton(parent.resolve(ADD_BUTTON_LABEL));
        buttonAdd.addActionListener(new ButtonAddListener());
        pathPanel.add(buttonAdd);
        JPanel allPanel = new JPanel();
        optionField = new JList();
        optionField.setListData(main.getMediator().getAgletList().toArray());
        allPanel.setLayout(new FlowLayout());
        allPanel.add(optionField);
        JPanel buttonPanel = new JPanel();
        buttonOk = new JButton(parent.resolve(CREATE_BUTTON_LABEL));
        buttonOk.addActionListener(new ButtonOKListener());
        buttonClose = new JButton(parent.resolve(CLOSE_BUTTON_LABEL));
        buttonClose.addActionListener(new ButtonCloseListener());
        buttonPanel.add(buttonOk);
        buttonPanel.add(buttonClose);
        JPanel main = new JPanel();
        main.setLayout(new BorderLayout());
        main.add(BorderLayout.NORTH, pathPanel);
        main.add(BorderLayout.CENTER, allPanel);
        main.add(BorderLayout.SOUTH, buttonPanel);
        this.getContentPane().add(BorderLayout.CENTER, main);
        pack();
    }

    public Object getSelectedValue() {
        return selVal;
    }

    class ButtonAddListener extends ActionKeyListener {

        protected void processEvent(EventObject e) {
            if (pathField.getText() != null && !pathField.equals("")) {
                main.getMediator().addToAgletList(pathField.getText());
                optionField.setListData(main.getMediator().getAgletList().toArray());
                CreateAgletWindow.this.pack();
                CreateAgletWindow.this.repaint();
            }
        }
    }

    class ButtonOKListener extends ActionKeyListener {

        protected void processEvent(EventObject e) {
            if (optionField.getSelectedValue() != null) {
                selVal = optionField.getSelectedValue();
                dispose();
            }
        }
    }

    class ButtonCloseListener extends ActionKeyListener {

        protected void processEvent(EventObject e) {
            selVal = null;
            dispose();
        }
    }
}
