package org.argouml.ui;

import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import org.apache.log4j.Logger;
import org.argouml.i18n.Translator;
import org.argouml.language.sql.GeneratorSql;
import org.argouml.language.sql.SqlCodeCreator;
import org.argouml.util.ArgoDialog;

/**
 * Small dialog for selecting a {@link SqlCodeCreator} for creating DDL
 * statements.
 * 
 * @author drahmann
 */
public final class SelectCodeCreatorDialog extends ArgoDialog {

    private JLabel lblSelect;

    private JScrollPane spList;

    private JTable tblCreators;

    private static boolean executed = false;

    private static final Logger LOG = Logger.getLogger(SelectCodeCreatorDialog.class);

    /**
     * Shows the dialog for selecting a code creator for generating proper DDL
     * statements.
     * 
     * @return <code>true</code>, if the user selected OK, <code>false</code>
     *         otherwise.
     */
    public static boolean execute() {
        if (!executed) {
            SelectCodeCreatorDialog d = new SelectCodeCreatorDialog();
            d.setVisible(true);
        }
        return executed;
    }

    /**
     * Creates a new dialog for selecting a code creator.
     * 
     */
    private SelectCodeCreatorDialog() {
        super(Translator.localize("argouml-sql.select-dialog.title"), OK_CANCEL_OPTION, true);
        setPreferredSize(new Dimension(400, 300));
        GridBagLayout l = new GridBagLayout();
        l.rowWeights = new double[] { 0, 1 };
        l.columnWeights = new double[] { 1 };
        JPanel content = new JPanel();
        content.setLayout(l);
        lblSelect = new JLabel(Translator.localize("argouml-sql.select-dialog.label-select") + ":");
        tblCreators = new JTable(new TableModelCodeCreators());
        spList = new JScrollPane(tblCreators);
        getOkButton().setEnabled(false);
        content.add(lblSelect, GridBagUtils.captionConstraints(0, 0, GridBagUtils.LEFT));
        content.add(spList, GridBagUtils.clientAlignConstraints(0, 1));
        setContent(content);
        tblCreators.getSelectionModel().addListSelectionListener(new ListSelectionListener() {

            public void valueChanged(ListSelectionEvent e) {
                if (tblCreators.getSelectedRowCount() > 0) {
                    getOkButton().setEnabled(true);
                } else {
                    getOkButton().setEnabled(false);
                }
            }
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == getOkButton()) {
            try {
                int index = tblCreators.getSelectedRow();
                if (index >= 0 && index < tblCreators.getRowCount()) {
                    SqlCodeCreator scc = (SqlCodeCreator) tblCreators.getModel().getValueAt(index, -1);
                    GeneratorSql.getInstance().setSqlCodeCreator(scc);
                    executed = true;
                }
            } catch (Exception exc) {
                LOG.error("Exception", exc);
                String message = Translator.localize("argouml-sql.exceptions.no_sqlcodecreator");
                ExceptionDialog ed = new ExceptionDialog(ProjectBrowser.getInstance(), message, exc);
                ed.setModal(true);
                ed.setVisible(true);
            }
        }
        dispose();
    }
}
