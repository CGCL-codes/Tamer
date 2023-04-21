package flickr;

import Composite.Photo;
import images.TabsDeDroite;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.AbstractCellEditor;
import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellEditor;

/**
 *
 * @author Administrateur
 */
public class JTableStack extends JTable implements KeyListener, MouseListener {

    JTableStackModel model;

    TabsDeDroite lesTabs;

    /** Creates a new instance of JTableStack */
    public JTableStack(JTableStackModel model, TabsDeDroite lesTabs) {
        super(model);
        this.model = model;
        this.lesTabs = lesTabs;
        setFillsViewportHeight(true);
        setRowHeight(100);
        setShowVerticalLines(false);
        setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        setAutoResizeMode(AUTO_RESIZE_OFF);
        ((DefaultTableCellRenderer) getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.CENTER);
        for (int i = 0; i < getColumnModel().getColumnCount(); i++) {
            switch(i) {
                case 0:
                    getColumnModel().getColumn(i).setPreferredWidth(100);
                    ;
                    break;
                case 1:
                    getColumnModel().getColumn(i).setPreferredWidth(130);
                    ;
                    break;
                case 2:
                    getColumnModel().getColumn(i).setPreferredWidth(60);
                    ;
                    break;
                case 3:
                    getColumnModel().getColumn(i).setPreferredWidth(150);
                    ;
                    break;
                case 4:
                    getColumnModel().getColumn(i).setPreferredWidth(100);
                    ;
                    break;
            }
        }
        addKeyListener(this);
        addMouseListener(this);
        model.setTable(this);
    }

    public void keyPressed(KeyEvent e) {
        int touche = e.getKeyCode();
        if (touche == KeyEvent.VK_DELETE) {
            int index = getSelectedRow();
            if (index >= 0) {
                Photo photo = (Photo) model.getElementAt(index);
                model.supprimerElement((Photo) model.getElementAt(index));
                getSelectionModel().setSelectionInterval(index - 1, index - 1);
            }
        }
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseClicked(MouseEvent e) {
        if (SwingUtilities.isRightMouseButton(e)) {
            final int index = rowAtPoint(e.getPoint());
            Photo photo = (Photo) model.getElementAt(index);
            JPopupMenu menuClicDroit = new JPopupMenu();
            ActionListener menuClicDroitListener = new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    if (e.getActionCommand().equals(new String("Retirer cette image de la liste"))) {
                        model.supprimerElement((Photo) model.getElementAt(index));
                    } else if (e.getActionCommand().equals(new String("Ouvrir cette image"))) {
                        lesTabs.creerVueImage((Photo) model.getElementAt(index), -1, lesTabs.getSelectedIndex());
                    }
                }
            };
            JMenuItem item;
            menuClicDroit.add(item = new JMenuItem("Ouvrir cette image"));
            item.setHorizontalTextPosition(JMenuItem.RIGHT);
            item.addActionListener(menuClicDroitListener);
            menuClicDroit.addSeparator();
            menuClicDroit.add(item = new JMenuItem("Retirer cette image de la liste"));
            item.addActionListener(menuClicDroitListener);
            menuClicDroit.setLabel("Justification");
            menuClicDroit.setBorder(new BevelBorder(BevelBorder.RAISED));
            menuClicDroit.show(this, e.getX(), e.getY());
        }
    }
}

class NomFlickrCellEditor extends AbstractCellEditor implements TableCellEditor {

    JComponent component = new JTextField();

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int rowIndex, int vColIndex) {
        if (isSelected) {
        }
        ((JTextField) component).setText((String) value);
        return component;
    }

    public Object getCellEditorValue() {
        return ((JTextField) component).getText();
    }
}
