package net.sourceforge.contactmanager.uicomponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.JButton;
import net.sourceforge.contactmanager.businessobjects.Contact;
import net.sourceforge.contactmanager.data.DataAccessComponent;
import net.sourceforge.contactmanager.data.SelectCriteria;
import net.sourceforge.contactmanager.dialogs.ExceptionDialog;
import net.sourceforge.contactmanager.internalframes.SearchInternalFrame;

public class SearchButton extends JButton implements ActionListener {

    /**
	 * 
	 */
    private static final long serialVersionUID = -6613919945430699959L;

    private SearchInternalFrame win;

    public SearchButton(SearchInternalFrame win) {
        addActionListener(this);
        setText("Search");
        setMnemonic('S');
        this.win = win;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println("Search");
        SelectCriteria[] criteria = win.getCriteria();
        DataAccessComponent dao;
        try {
            dao = DataAccessComponent.getInstance();
        } catch (Exception e2) {
            ExceptionDialog.display(e2);
            return;
        }
        Contact[] contacts = new Contact[0];
        try {
            contacts = dao.search(criteria);
        } catch (SQLException e1) {
            ExceptionDialog.display(e1);
        }
        win.setSearchList(contacts);
    }
}
