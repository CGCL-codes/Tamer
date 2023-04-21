package net.sf.elbe.ui.dialogs.preferences;

import java.util.Iterator;
import java.util.SortedMap;
import java.util.TreeMap;
import net.sf.elbe.core.model.schema.SyntaxValueProviderRelation;
import net.sf.elbe.ui.valueproviders.ValueProvider;
import net.sf.elbe.ui.widgets.BaseWidgetUtils;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

public class SyntaxValueProviderDialog extends Dialog {

    private SyntaxValueProviderRelation relation;

    private SortedMap class2ValueProviderMap;

    private String[] syntaxOids;

    private SortedMap vpName2classMap;

    private SyntaxValueProviderRelation returnRelation;

    private Combo oidCombo;

    private Combo valueEditorCombo;

    public SyntaxValueProviderDialog(Shell parentShell, SyntaxValueProviderRelation relation, SortedMap class2ValueProviderMap, String[] syntaxOids) {
        super(parentShell);
        this.relation = relation;
        this.class2ValueProviderMap = class2ValueProviderMap;
        this.syntaxOids = syntaxOids;
        this.returnRelation = null;
        this.vpName2classMap = new TreeMap();
        for (Iterator it = this.class2ValueProviderMap.values().iterator(); it.hasNext(); ) {
            ValueProvider vp = (ValueProvider) it.next();
            vpName2classMap.put(vp.getCellEditorName(), vp.getClass().getName());
        }
    }

    protected void configureShell(Shell newShell) {
        super.configureShell(newShell);
        newShell.setText("Attribute Value Editor");
    }

    protected void okPressed() {
        this.returnRelation = new SyntaxValueProviderRelation(this.oidCombo.getText(), (String) this.vpName2classMap.get(this.valueEditorCombo.getText()));
        super.okPressed();
    }

    protected Control createDialogArea(Composite parent) {
        Composite composite = (Composite) super.createDialogArea(parent);
        Composite c = BaseWidgetUtils.createColumnContainer(composite, 2, 1);
        BaseWidgetUtils.createLabel(c, "Syntax OID:", 1);
        this.oidCombo = BaseWidgetUtils.createCombo(c, this.syntaxOids, -1, 1);
        if (this.relation != null && this.relation.getSyntaxOID() != null) {
            this.oidCombo.setText(this.relation.getSyntaxOID());
        }
        this.oidCombo.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                validate();
            }
        });
        BaseWidgetUtils.createLabel(c, "Value Editor:", 1);
        this.valueEditorCombo = BaseWidgetUtils.createReadonlyCombo(c, (String[]) vpName2classMap.keySet().toArray(new String[0]), -1, 1);
        if (this.relation != null && this.relation.getValueProviderClassname() != null && this.class2ValueProviderMap.containsKey(this.relation.getValueProviderClassname())) {
            this.valueEditorCombo.setText(((ValueProvider) this.class2ValueProviderMap.get(this.relation.getValueProviderClassname())).getCellEditorName());
        }
        this.valueEditorCombo.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e) {
                validate();
            }
        });
        return composite;
    }

    private void validate() {
        super.getButton(IDialogConstants.OK_ID).setEnabled(!"".equals(this.valueEditorCombo.getText()) && !"".equals(this.oidCombo.getText()));
    }

    public SyntaxValueProviderRelation getRelation() {
        return returnRelation;
    }
}
