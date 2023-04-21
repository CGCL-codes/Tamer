package wilos.presentation.assistant.view.main;

import java.awt.Color;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.table.DefaultTableModel;
import wilos.presentation.assistant.control.ServersListParser;
import wilos.presentation.assistant.model.WizardServer;
import wilos.presentation.assistant.ressources.Bundle;
import wilos.presentation.assistant.ressources.ImagesService;
import wilos.presentation.assistant.view.dialogs.ErrorDialog;
import wilos.presentation.assistant.view.panels.LoginPanel;

public class ServersFrame {

    private JDialog serverDialog = null;

    private JPanel Fenetre = null;

    private JButton add = null;

    private JButton delete = null;

    private JButton valider = null;

    private JTable servs = null;

    private JButton cancel = null;

    private DefaultTableModel tables_serv;

    private JScrollPane scrollServ = null;

    private JButton save = null;

    /**
	 * This method initializes Fenetre1
	 * 
	 * @return javax.swing.JFrame
	 */
    private JDialog getServerDialog(JFrame parent) {
        if (serverDialog == null) {
            int x = (int) (Toolkit.getDefaultToolkit().getScreenSize().width / 2 - (605 / 2));
            int y = (int) (Toolkit.getDefaultToolkit().getScreenSize().height / 2 - (220 / 2));
            serverDialog = new JDialog(parent);
            serverDialog.setModal(true);
            serverDialog.setBounds(x, y, 605, 220);
            serverDialog.setResizable(false);
            serverDialog.setContentPane(getServerPane());
            serverDialog.setTitle(Bundle.getText("serversFrame.title"));
            serverDialog.setVisible(true);
        }
        return serverDialog;
    }

    /**
	 * This method initializes Fenetre
	 * 
	 * @return javax.swing.JPanel
	 */
    private JPanel getServerPane() {
        if (Fenetre == null) {
            Fenetre = new JPanel();
            Fenetre.setLayout(null);
            Fenetre.add(getAdd(), null);
            Fenetre.add(getDelete(), null);
            Fenetre.add(getValidate(), null);
            Fenetre.add(getCancel(), null);
            Fenetre.add(getScrollServ(), null);
            Fenetre.add(getApply(), null);
        }
        return Fenetre;
    }

    private JButton getCancel() {
        if (cancel == null) {
            cancel = new JButton();
            cancel.setBounds(new Rectangle(310, 140, 100, 25));
            cancel.setText(Bundle.getText("serversFrame.cancel"));
            cancel.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    serverDialog.dispose();
                }
            });
        }
        return cancel;
    }

    private JButton getApply() {
        if (save == null) {
            save = new JButton();
            save.setBounds(new Rectangle(420, 140, 100, 25));
            save.setText(Bundle.getText("serversFrame.apply"));
            save.addActionListener(new ActionListener() {

                @SuppressWarnings("static-access")
                public void actionPerformed(ActionEvent e) {
                    ArrayList<WizardServer> new_list_serv = new ArrayList<WizardServer>();
                    JOptionPane opt1 = new JOptionPane();
                    boolean valide = true;
                    for (int i = 0; i < servs.getRowCount() && valide; i++) {
                        if ((String) servs.getValueAt(i, 1) == null || (String) servs.getValueAt(i, 0) == null) {
                            valide = false;
                            opt1.showMessageDialog(opt1, Bundle.getText("serversFrame.error"), "Error", JOptionPane.ERROR_MESSAGE);
                            opt1.setVisible(true);
                        } else {
                            new_list_serv.add(new WizardServer((String) servs.getValueAt(i, 0), (String) servs.getValueAt(i, 1), i));
                        }
                    }
                    if (valide) {
                        LoginPanel.list_serv.saveServersList(new_list_serv);
                    }
                }
            });
        }
        return save;
    }

    /**
	 * This method initializes add
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getAdd() {
        if (add == null) {
            add = new JButton();
            add.setBounds(new Rectangle(540, 25, 35, 35));
            add.setIcon(ImagesService.getImageIcon("images.iconAdd"));
            add.addActionListener(new ActionListener() {

                @SuppressWarnings("unchecked")
                public void actionPerformed(ActionEvent e) {
                    String alias = "";
                    String url = "";
                    boolean ok = true;
                    for (int i = 0; (i < servs.getRowCount()) && (ok == true); i++) {
                        alias = (String) servs.getValueAt(i, 0);
                        url = (String) servs.getValueAt(i, 1);
                        if ((alias == null) || (alias.equals("")) || (url == null) || (url.equals(""))) {
                            ok = false;
                        }
                    }
                    if (ok == true) {
                        tables_serv.addRow(new Vector());
                        servs.setEditingRow(tables_serv.getRowCount());
                    }
                }
            });
        }
        return add;
    }

    /**
	 * This method initializes delete
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getDelete() {
        if (delete == null) {
            delete = new JButton();
            delete.setBounds(new Rectangle(540, 80, 35, 35));
            delete.setIcon(ImagesService.getImageIcon("images.iconDelete"));
            delete.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (servs.getRowCount() == 1) {
                        new ErrorDialog(Bundle.getText("serversFrame.aLineError"));
                    } else if (servs.getSelectedRow() != -1) {
                        int currentRow = servs.getSelectedRow();
                        servs.editingCanceled(new ChangeEvent(this));
                        tables_serv.removeRow(currentRow);
                    }
                }
            });
        }
        return delete;
    }

    /**
	 * This method initializes valider
	 * 
	 * @return javax.swing.JButton
	 */
    private JButton getValidate() {
        if (valider == null) {
            valider = new JButton();
            valider.setBounds(new Rectangle(200, 140, 100, 25));
            valider.setText(Bundle.getText("serversFrame.ok"));
            valider.addActionListener(new ActionListener() {

                @SuppressWarnings("static-access")
                public void actionPerformed(ActionEvent e) {
                    ArrayList<WizardServer> new_list_serv = new ArrayList<WizardServer>();
                    JOptionPane opt1 = new JOptionPane();
                    boolean valide = true;
                    for (int i = 0; i < servs.getRowCount() && valide; i++) {
                        if ((String) servs.getValueAt(i, 1) == null) {
                            valide = false;
                            opt1.showMessageDialog(opt1, Bundle.getText("serversFrame.error"), "Error", JOptionPane.ERROR_MESSAGE);
                            opt1.setVisible(true);
                        } else {
                            new_list_serv.add(new WizardServer((String) servs.getValueAt(i, 0), (String) servs.getValueAt(i, 1), i));
                        }
                    }
                    if (valide) {
                        LoginPanel.list_serv.saveServersList(new_list_serv);
                        serverDialog.dispose();
                    }
                }
            });
        }
        return valider;
    }

    public ServersFrame(JFrame parent) {
        getServerDialog(parent);
    }

    /**
	 * This method initializes servs
	 * 
	 * @return javax.swing.JTable
	 */
    private JTable getServs() {
        if (this.servs == null) {
            ServersListParser listes = new ServersListParser();
            List<WizardServer> serv = listes.getServersList();
            Vector<String> rowName = new Vector<String>();
            rowName.add(Bundle.getText("serversFrame.nom"));
            rowName.add(Bundle.getText("serversFrame.address"));
            Vector<Vector<String>> data = new Vector<Vector<String>>();
            for (int i = 0; i < serv.size(); i++) {
                Vector<String> tmp = new Vector<String>();
                tmp.add(serv.get(i).getAlias());
                tmp.add(serv.get(i).getAddress());
                data.add(tmp);
            }
            this.tables_serv = new DefaultTableModel(data, rowName);
            this.servs = new JTable(this.tables_serv);
            this.servs.setGridColor(Color.black);
            this.servs.setVisible(true);
        }
        return servs;
    }

    private JScrollPane getScrollServ() {
        if (this.scrollServ == null) {
            scrollServ = new JScrollPane();
            scrollServ.setBounds(new Rectangle(20, 20, 500, 100));
            scrollServ.setViewportView(getServs());
            scrollServ.setVisible(true);
        }
        return scrollServ;
    }
}
