package connector.client.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

public class FilePopup extends JPopupMenu implements ActionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = -7622400536127986298L;

    private TransferComponent comp;

    private FileTransferPanel panel;

    private JMenuItem cancel = new JMenuItem("Cancelar");

    private JMenuItem clean = new JMenuItem("Eliminar");

    public FilePopup(FileTransferPanel panel) {
        cancel.addActionListener(this);
        cancel.setActionCommand("cancel");
        clean.addActionListener(this);
        clean.setActionCommand("clean");
        this.panel = panel;
    }

    public void setFileComponent(TransferComponent comp) {
        this.comp = comp;
        this.removeAll();
        if (comp.isCompleted() || comp.isEnded()) {
            this.add(clean);
        } else {
            this.add(cancel);
        }
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == cancel) {
            comp.cancelTransfer();
        } else {
            panel.removeTransfer(comp);
        }
    }
}
