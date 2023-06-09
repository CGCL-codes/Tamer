package javax.swing.plaf.basic;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.CellRendererPane;
import javax.swing.JComponent;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.TableHeaderUI;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

public class BasicTableHeaderUI extends TableHeaderUI {

    public static ComponentUI createUI(JComponent h) {
        return new BasicTableHeaderUI();
    }

    protected JTableHeader header;

    protected MouseInputListener mouseInputListener;

    protected CellRendererPane rendererPane;

    protected Border cellBorder;

    public class MouseInputHandler implements MouseInputListener {

        public void mouseClicked(MouseEvent e) {
        }

        public void mouseDragged(MouseEvent e) {
        }

        public void mouseEntered(MouseEvent e) {
        }

        public void mouseExited(MouseEvent e) {
        }

        public void mouseMoved(MouseEvent e) {
        }

        public void mousePressed(MouseEvent e) {
        }

        public void mouseReleased(MouseEvent e) {
        }
    }

    protected MouseInputListener createMouseInputListener() {
        return new MouseInputHandler();
    }

    public BasicTableHeaderUI() {
        mouseInputListener = createMouseInputListener();
    }

    protected void installDefaults() {
        LookAndFeel.installColorsAndFont(header, "TableHeader.background", "TableHeader.foreground", "TableHeader.font");
        cellBorder = UIManager.getBorder("TableHeader.cellBorder");
    }

    protected void installKeyboardActions() {
    }

    protected void installListeners() {
        header.addMouseListener(mouseInputListener);
    }

    public void installUI(JComponent c) {
        header = (JTableHeader) c;
        rendererPane = new CellRendererPane();
        installDefaults();
        installKeyboardActions();
        installListeners();
    }

    protected void uninstallDefaults() {
        header.setBackground(null);
        header.setForeground(null);
        header.setFont(null);
    }

    protected void uninstallKeyboardActions() {
    }

    protected void uninstallListeners() {
        header.removeMouseListener(mouseInputListener);
    }

    public void uninstallUI(JComponent c) {
        uninstallListeners();
        uninstallKeyboardActions();
        uninstallDefaults();
    }

    public void paint(Graphics gfx, JComponent c) {
        TableColumnModel cmod = header.getColumnModel();
        int ncols = cmod.getColumnCount();
        if (ncols == 0) return;
        Rectangle clip = gfx.getClipBounds();
        TableCellRenderer defaultRend = header.getDefaultRenderer();
        for (int i = 0; i < ncols; ++i) {
            Rectangle bounds = header.getHeaderRect(i);
            if (bounds.intersects(clip)) {
                Rectangle oldClip = gfx.getClipBounds();
                TableColumn col = cmod.getColumn(i);
                TableCellRenderer rend = col.getHeaderRenderer();
                if (rend == null) rend = defaultRend;
                Object val = col.getHeaderValue();
                Component comp = rend.getTableCellRendererComponent(header.getTable(), val, false, false, -1, i);
                comp.setFont(header.getFont());
                comp.setBackground(header.getBackground());
                comp.setForeground(header.getForeground());
                if (comp instanceof JComponent) ((JComponent) comp).setBorder(cellBorder);
                rendererPane.paintComponent(gfx, comp, header, bounds.x, bounds.y, bounds.width, bounds.height);
            }
        }
    }

    public Dimension getPreferredSize(JComponent c) {
        TableColumnModel cmod = header.getColumnModel();
        TableCellRenderer defaultRend = header.getDefaultRenderer();
        int ncols = cmod.getColumnCount();
        Dimension ret = new Dimension(0, 0);
        int spacing = 0;
        if (header.getTable() != null && header.getTable().getIntercellSpacing() != null) spacing = header.getTable().getIntercellSpacing().width;
        for (int i = 0; i < ncols; ++i) {
            TableColumn col = cmod.getColumn(i);
            TableCellRenderer rend = col.getHeaderRenderer();
            if (rend == null) rend = defaultRend;
            Object val = col.getHeaderValue();
            Component comp = rend.getTableCellRendererComponent(header.getTable(), val, false, false, -1, i);
            comp.setFont(header.getFont());
            comp.setBackground(header.getBackground());
            comp.setForeground(header.getForeground());
            if (comp instanceof JComponent) ((JComponent) comp).setBorder(cellBorder);
            Dimension d = comp.getPreferredSize();
            ret.width += spacing;
            ret.height = Math.max(d.height, ret.height);
        }
        ret.width = cmod.getTotalColumnWidth();
        return ret;
    }
}
