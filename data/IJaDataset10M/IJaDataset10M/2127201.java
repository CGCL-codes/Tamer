package net.sf.theotherpages.samples.swingsample.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JToolBar;
import net.sf.theotherpages.data.PageData;
import net.sf.theotherpages.samples.swingsample.ui.action.PaginationInAction;

public class MyPanel extends JPanel {

    private static final String FIRST_PAGE = "FIRST_PAGE";

    private static final String NEXT_PAGE = "NEXT_PAGE";

    private static final String PREVIOUS_PAGE = "PREVIOUS_PAGE";

    JToolBar toolBar = new JToolBar();

    MyTableModel tableModel = new MyTableModel();

    JButton next = new JButton(new ImageIcon("samples/SwingSample/images/right.gif"));

    JButton prev = new JButton(new ImageIcon("images/left.gif"));

    JLabel gotoPageLable = new JLabel(" goto ");

    JComboBox gotoPageList = new JComboBox();

    JTable table = new JTable(tableModel);

    public MyPanel() {
        super(new BorderLayout());
        table.setPreferredScrollableViewportSize(new Dimension(500, 70));
        JScrollPane scrollPane = new JScrollPane(table);
        addButtons(toolBar);
        add(toolBar, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        showPage(FIRST_PAGE);
    }

    protected void addButtons(JToolBar toolBar) {
        prev.setToolTipText("This is the left button");
        prev.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showPage(PREVIOUS_PAGE);
            }
        });
        toolBar.add(prev);
        gotoPageList.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                String gotoPage = String.valueOf(gotoPageList.getSelectedIndex() + 1);
                System.out.println(gotoPage);
                showPage(gotoPage);
            }
        });
        toolBar.add(gotoPageLable);
        toolBar.add(gotoPageList);
        next.setToolTipText("This is the right button");
        next.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                showPage(NEXT_PAGE);
            }
        });
        toolBar.add(next);
    }

    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("TableDemo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        MyPanel newContentPane = new MyPanel();
        newContentPane.setOpaque(true);
        frame.setContentPane(newContentPane);
        frame.pack();
        frame.setVisible(true);
    }

    private void showPage(String pageType) {
        PageData pageData = null;
        PaginationInAction action = new PaginationInAction();
        try {
            if (pageType.equals(FIRST_PAGE)) {
                pageData = action.getFirstPage();
                int lastPage = pageData.getLastPageNo();
                for (int x = 1; x <= lastPage; x++) {
                    gotoPageList.addItem(new Integer(x));
                }
            } else if (pageType.equals(NEXT_PAGE)) {
                pageData = action.getNextPage();
            } else if (pageType.equals(PREVIOUS_PAGE)) {
                pageData = action.getPreviousPage();
            } else {
                pageData = action.goToPage(Integer.parseInt(pageType));
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        List list = pageData.getPageRecords();
        tableModel.loadPage(list);
        tableModel.fireTableDataChanged();
        if (pageData.isFirstPage()) {
            prev.setVisible(false);
        } else {
            prev.setVisible(true);
        }
        if (pageData.isLastPage()) {
            next.setVisible(false);
        } else {
            next.setVisible(true);
        }
        gotoPageList.setSelectedIndex(pageData.getCurrentPageNo() - 1);
    }
}
