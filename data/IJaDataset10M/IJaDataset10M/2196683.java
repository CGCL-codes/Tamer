package org.dengues.ui.query;

import java.util.Arrays;
import java.util.List;
import org.dengues.core.sql.SqlUtils;
import org.dengues.model.database.ForeignKey;
import org.dengues.ui.i18n.Messages;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

/**
 * Qiang.Zhang.Adolf@gmail.com class global comment. Detailled comment <br/>
 * 
 * $Id: Dengues.epf 2007-12-17 qiang.zhang $
 * 
 */
public class JoinOptionsDialog extends Dialog {

    private final ForeignKey foreignKey;

    private Button zSQL92Btn;

    private Button zUsebutton;

    private Combo zOpcombo;

    private Button zRJoinBtn;

    private Button zLJoinBtn;

    /**
     * Qiang.Zhang.Adolf@gmail.com JoinOptionsDialog constructor comment.
     * 
     * @param parentShell
     * @param foreignKey
     */
    public JoinOptionsDialog(Shell parentShell, ForeignKey foreignKey) {
        super(parentShell);
        this.foreignKey = foreignKey;
        setShellStyle(getShellStyle() | SWT.RESIZE);
    }

    @Override
    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText(Messages.getString("JoinOptionsDialog.joinTitle"));
        newShell.setSize(400, 220);
    }

    @Override
    protected Control createDialogArea(Composite parent) {
        Composite areaContainer = (Composite) super.createDialogArea(parent);
        Composite composite = new Composite(areaContainer, SWT.NONE);
        GridLayout gridLayout = new GridLayout(3, false);
        composite.setLayout(gridLayout);
        GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
        composite.setLayoutData(gridData);
        Text text = new Text(composite, SWT.BORDER | SWT.CENTER);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        text.setLayoutData(gridData);
        text.setText(SqlUtils.getAllColumnName(foreignKey.getSource()));
        text.setEditable(false);
        zOpcombo = new Combo(composite, SWT.BORDER);
        zOpcombo.setItems(SqlUtils.FK_OPERATORS);
        if (foreignKey.getProperties().get(SqlUtils.FK_OPERATOR) != null) {
            zOpcombo.select(Arrays.asList(SqlUtils.FK_OPERATORS).indexOf(foreignKey.getProperties().get(SqlUtils.FK_OPERATOR)));
        } else {
            zOpcombo.select(0);
        }
        gridData = new GridData();
        gridData.widthHint = 50;
        zOpcombo.setLayoutData(gridData);
        text = new Text(composite, SWT.BORDER | SWT.CENTER);
        text.setText(SqlUtils.getAllColumnName(foreignKey.getTarget()));
        text.setEditable(false);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        text.setLayoutData(gridData);
        Label label = new Label(areaContainer, SWT.NONE);
        label.setText(Messages.getString("JoinOptionsDialog.joinLabel"));
        zLJoinBtn = new Button(areaContainer, SWT.CHECK);
        zLJoinBtn.setText(Messages.getString("JoinOptionsDialog.lJoin") + foreignKey.getSource().getTable().getName());
        zLJoinBtn.setSelection(SqlUtils.FK_LJOIN.equals(foreignKey.getProperties().get(SqlUtils.FK_JOIN)) | SqlUtils.FK_FJOIN.equals(foreignKey.getProperties().get(SqlUtils.FK_JOIN)));
        zRJoinBtn = new Button(areaContainer, SWT.CHECK);
        zRJoinBtn.setText(Messages.getString("JoinOptionsDialog.rJoin") + foreignKey.getTarget().getTable().getName());
        zRJoinBtn.setSelection(SqlUtils.FK_RJOIN.equals(foreignKey.getProperties().get(SqlUtils.FK_JOIN)) | SqlUtils.FK_FJOIN.equals(foreignKey.getProperties().get(SqlUtils.FK_JOIN)));
        composite = new Composite(areaContainer, SWT.NONE);
        gridLayout = new GridLayout(2, false);
        composite.setLayout(gridLayout);
        gridData = new GridData(GridData.FILL_HORIZONTAL);
        composite.setLayoutData(gridData);
        zSQL92Btn = new Button(composite, SWT.RADIO);
        zSQL92Btn.setText("SQL92");
        zUsebutton = new Button(composite, SWT.RADIO);
        zUsebutton.setText("Use+");
        if (foreignKey.getProperties().get(SqlUtils.FK_SQLTYPE) != null) {
            Boolean boolean1 = SqlUtils.FK_SQL92.equals(foreignKey.getProperties().get(SqlUtils.FK_SQLTYPE));
            zSQL92Btn.setSelection(boolean1);
            zUsebutton.setSelection(!boolean1);
        } else {
            zSQL92Btn.setSelection(true);
            zUsebutton.setSelection(false);
        }
        return areaContainer;
    }

    @Override
    protected void okPressed() {
        String join = SqlUtils.FK_NJOIN;
        if (zLJoinBtn.getSelection()) {
            if (zRJoinBtn.getSelection()) {
                join = SqlUtils.FK_FJOIN;
            } else {
                join = SqlUtils.FK_LJOIN;
            }
        } else {
            if (zRJoinBtn.getSelection()) {
                join = SqlUtils.FK_RJOIN;
            }
        }
        List<ForeignKey> fksFromFK = QueryUiUtils.getFksFromFK(foreignKey);
        for (ForeignKey foreignKey2 : fksFromFK) {
            foreignKey2.getProperties().put(SqlUtils.FK_SQL92, zSQL92Btn.getSelection() ? SqlUtils.FK_SQL92 : SqlUtils.FK_USEADD);
            foreignKey2.getProperties().put(SqlUtils.FK_OPERATOR, zOpcombo.getText());
            foreignKey2.getProperties().put(SqlUtils.FK_JOIN, join);
        }
        super.okPressed();
    }
}
