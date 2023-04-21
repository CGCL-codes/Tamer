package com.wateray.ipassbook.ui.view.contentPanel;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import javax.swing.JScrollPane;
import com.wateray.ipassbook.commom.Constant;
import com.wateray.ipassbook.kernel.event.IPassbookEvent;
import com.wateray.ipassbook.kernel.service.SubjectService;
import com.wateray.ipassbook.ui.model.SubjectPageTableModel;
import com.wateray.ipassbook.ui.widget.treeTable.JTreeTable;
import com.wateray.ipassbook.util.LanguageLoader;

/**
 * @author c-bryu
 * 
 */
public class SubjectPage extends AbstractPage implements IPage {

    /**
	 * serialVersionUID
	 */
    private static final long serialVersionUID = 1L;

    private static SubjectPage thisPage;

    private SubjectPageTableModel subjectTableModel;

    private JTreeTable subjectTreeTable;

    /**
	 * @return an instance of the SubjectPage.
	 * */
    public static SubjectPage getInstance() {
        if (thisPage == null) {
            thisPage = new SubjectPage();
        }
        return (SubjectPage) thisPage;
    }

    private SubjectPage() {
        super();
    }

    protected Component getCenterPane() {
        return new JScrollPane(getSubjectTree());
    }

    private JTreeTable getSubjectTree() {
        if (subjectTreeTable == null) {
            subjectTableModel = new SubjectPageTableModel(getColumnNames(), getData(Constant.EXPENSE_KBN));
            subjectTreeTable = new JTreeTable(subjectTableModel);
            subjectTreeTable.setFillsViewportHeight(true);
        }
        return subjectTreeTable;
    }

    /***/
    private Vector<String> getColumnNames() {
        Vector<String> columnNames = new Vector<String>();
        columnNames.add(LanguageLoader.getString("Name"));
        columnNames.add(LanguageLoader.getString("Visible"));
        columnNames.add(LanguageLoader.getString("Owner"));
        columnNames.add(LanguageLoader.getString("Memo"));
        return columnNames;
    }

    private Vector getData(String flag) {
        Vector vect = new Vector();
        SubjectService service = new SubjectService();
        List list = service.getAllSubjectList();
        Iterator it = list.iterator();
        while (it.hasNext()) {
            vect.add(it.next());
        }
        return vect;
    }
}
