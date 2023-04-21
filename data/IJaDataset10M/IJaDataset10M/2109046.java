package gui.infopanel;

import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

public abstract class InfoPanelDialog extends JDialog implements RunnableForm {

    private String name;

    private TableView table;

    private static final String CUSTOM_DISPOSE_EVENT = "CUSTOM_DISPOSE_EVENT";

    public InfoPanelDialog(String name, String title) {
        this.name = name;
        this.setModal(true);
        JPanel contentPane = (JPanel) getContentPane();
        contentPane.setBorder(new EmptyBorder(6, 8, 6, 8));
        setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        InputMap inputMap = contentPane.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = contentPane.getActionMap();
        AbstractAction disposeAction = new AbstractAction() {

            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        };
        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), CUSTOM_DISPOSE_EVENT);
        actionMap.put(CUSTOM_DISPOSE_EVENT, disposeAction);
    }

    public String getName() {
        return name;
    }

    public void setTableView(TableView t) {
        table = t;
    }

    public TableView getTableView() {
        return table;
    }
}
