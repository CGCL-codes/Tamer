package org.hardtokenmgmt.admin.ui.panels.news;

import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.SwingUtilities;
import org.hardtokenmgmt.common.vo.NewsDataVO;
import org.hardtokenmgmt.core.ui.UIHelper;

/**
 * 
 * Special Edit button used in News Table
 * 
 * @author Philip Vendil 20 mar 2009
 *
 * @version $Id$
 */
public class NewsListEditButton extends JButton {

    private static final long serialVersionUID = 1L;

    private boolean edit = false;

    private final NewsDataVO news;

    private JComponent thisPanel = this;

    public NewsListEditButton(NewsDataVO news, boolean edit) {
        this.edit = edit;
        this.news = news;
        setIcon(UIHelper.getImage("view_small.png"));
        if (edit) {
            setToolTipText(UIHelper.getText("editnews.editnews"));
        } else {
            setToolTipText(UIHelper.getText("editnews.viewnews"));
        }
        addActionListener(new ActionListener() {

            public void actionPerformed(final ActionEvent e) {
                performAction();
            }
        });
        addMouseListener(new MouseAdapter() {

            public void mousePressed(final MouseEvent e) {
                performAction();
            }
        });
    }

    private void performAction() {
        EditNewsDialog dialog = new EditNewsDialog(news, (Window) SwingUtilities.getRoot(thisPanel), edit);
        dialog.setLocationRelativeTo(SwingUtilities.getRootPane(thisPanel));
        dialog.setVisible(true);
    }
}
