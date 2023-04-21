package galao.peer.view;

import galao.Galao;
import galao.core.properties.L10N;
import galao.net.peers.PeerMap;
import galao.peer.handler.PeerRequestHandler;
import galao.peer.handler.message.PeerRequestMessage;
import galao.peer.model.PeerModel;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.table.TableColumn;

public class PeerView extends JComponent {

    private JTable peerView;

    private JPopupMenu context;

    public PeerView() {
        this.setLayout(new BorderLayout());
        this.peerView = new JTable(new PeerModel());
        this.peerView.getTableHeader().setReorderingAllowed(false);
        this.peerView.setAutoCreateRowSorter(true);
        JScrollPane jsp = new JScrollPane(this.peerView);
        this.addContextMenu(this.peerView);
        this.add(jsp, BorderLayout.CENTER);
    }

    private void addContextMenu(JComponent c) {
        this.context = new JPopupMenu("Peers");
        JMenu addPeer = new JMenu(L10N.get("peer.add"));
        JMenuItem addPeerByIp = new JMenuItem(L10N.get("peer.add.byIp"));
        addPeerByIp.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                String ip = JOptionPane.showInputDialog(PeerView.this, L10N.get("peer.enter.id"));
                try {
                    InetAddress ipp = InetAddress.getByName(ip);
                    PeerRequestMessage psm = new PeerRequestMessage();
                    psm.setRecipientAdress(ipp);
                    Galao.getGateway().send(psm);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }
        });
        addPeer.add(addPeerByIp);
        JMenuItem removePeer = new JMenuItem(L10N.get("peer.remove"));
        removePeer.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                PeerMap pm = PeerMap.getInstance();
                BigInteger id = null;
                int rowSelectedForDeletion = PeerView.this.peerView.getSelectedRow();
                if (rowSelectedForDeletion >= 0) {
                    id = new BigInteger(PeerView.this.peerView.getValueAt(rowSelectedForDeletion, 0).toString());
                    pm.remove(id);
                }
            }
        });
        this.context.add(addPeer);
        this.context.add(removePeer);
        MouseListener ml = new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent e) {
                this.handleEvent(e);
            }

            private void handleEvent(MouseEvent e) {
                if (e.isPopupTrigger()) {
                    PeerView.this.showContextMenu(e.getComponent(), e.getX(), e.getY());
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                int y = e.getY();
                int x = e.getX();
                int row = PeerView.this.peerView.rowAtPoint(new Point(x, y));
                if (row >= 0) {
                    PeerView.this.peerView.getSelectionModel().setSelectionInterval(row, row);
                }
                this.handleEvent(e);
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                this.handleEvent(e);
            }
        };
        c.getParent().addMouseListener(ml);
        c.addMouseListener(ml);
    }

    protected void showContextMenu(Component c, int i, int j) {
        this.context.show(c, i, j + 2);
    }
}
