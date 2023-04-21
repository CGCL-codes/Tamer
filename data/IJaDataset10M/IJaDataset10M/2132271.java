package sky4s.test.ui.table.hide;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

/**
 * @version 1.0 05/31/99
 */
public class ColumnButtonScrollPane extends JScrollPane {

    Component columnButton;

    public ColumnButtonScrollPane(JTable table) {
        super(table);
        TableColumnModel cm = table.getColumnModel();
        LimitedTableHeader header = new LimitedTableHeader(cm);
        table.setTableHeader(header);
        columnButton = createUpperCorner(header);
        setCorner(UPPER_RIGHT_CORNER, columnButton);
        setVerticalScrollBarPolicy(VERTICAL_SCROLLBAR_ALWAYS);
        ColumnButtonScrollPaneLayout layout = new ColumnButtonScrollPaneLayout();
        setLayout(layout);
        layout.syncWithScrollPane(this);
    }

    protected Component createUpperCorner(JTableHeader header) {
        ColumnButton corner = new ColumnButton(header);
        return corner;
    }

    public class LimitedTableHeader extends JTableHeader {

        public LimitedTableHeader(TableColumnModel cm) {
            super(cm);
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            columnButton.repaint();
        }
    }

    public class ColumnButton extends JPanel {

        JTable table;

        TableColumnModel cm;

        JButton revealButton;

        JButton hideButton;

        Stack stack;

        public ColumnButton(JTableHeader header) {
            setLayout(new GridLayout(1, 2));
            setBorder(new LinesBorder(SystemColor.controlShadow, new Insets(0, 1, 0, 0)));
            stack = new Stack();
            table = header.getTable();
            cm = table.getColumnModel();
            revealButton = createButton(header, SwingConstants.WEST);
            hideButton = createButton(header, SwingConstants.EAST);
            add(revealButton);
            add(hideButton);
            revealButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    TableColumn column = (TableColumn) stack.pop();
                    cm.addColumn(column);
                    if (stack.empty()) {
                        revealButton.setEnabled(false);
                    }
                    hideButton.setEnabled(true);
                    table.sizeColumnsToFit(-1);
                }
            });
            hideButton.addActionListener(new ActionListener() {

                public void actionPerformed(ActionEvent e) {
                    int n = cm.getColumnCount();
                    TableColumn column = cm.getColumn(n - 1);
                    stack.push(column);
                    cm.removeColumn(column);
                    if (n < 3) {
                        hideButton.setEnabled(false);
                    }
                    revealButton.setEnabled(true);
                    table.sizeColumnsToFit(-1);
                }
            });
            if (1 < cm.getColumnCount()) {
                hideButton.setEnabled(true);
            } else {
                hideButton.setEnabled(false);
            }
            revealButton.setEnabled(false);
        }

        protected JButton createButton(JTableHeader header, int direction) {
            int iconHeight = 8;
            JButton button = new JButton();
            button.setIcon(new ArrowIcon(iconHeight, direction, true));
            button.setDisabledIcon(new ArrowIcon(iconHeight, direction, false));
            button.setRequestFocusEnabled(false);
            button.setForeground(header.getForeground());
            button.setBackground(header.getBackground());
            button.setBorder(UIManager.getBorder("TableHeader.cellBorder"));
            return button;
        }
    }
}
