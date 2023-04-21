package net.sf.pim.contract.wizard;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;
import net.sf.component.table.BindedTableViewer;
import net.sf.pim.UiUtil;
import net.sf.pim.contract.Contract;
import net.sf.util.StringUtil;
import org.eclipse.jface.wizard.Wizard;

/**
 * 生成导入csv的向导
 * @author Administrator
 */
public class CSVWizard extends Wizard {

    private String fileName;

    private Map<String, Integer> associations;

    private boolean skipFirstLine;

    public void init() {
        associations = new HashMap<String, Integer>();
        setNeedsProgressMonitor(true);
    }

    @Override
    public boolean performFinish() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(fileName));
            br.readLine();
            BindedTableViewer btv = UiUtil.getActiveTableEditor().getViewer();
            for (String[] ss = StringUtil.toArray(br.readLine()); ss.length != 0; ss = StringUtil.toArray(br.readLine())) {
                try {
                    Contract contract = new Contract();
                    for (String key : associations.keySet()) {
                        if (associations.get(key) > 0) contract.setProperty(key, ss[associations.get(key) - 1]);
                    }
                    btv.getModel().addRow(contract);
                } catch (Exception re) {
                    re.printStackTrace();
                }
            }
            btv.getModel().setDirty(true);
            btv.refresh(false);
            btv.getTable().setSelection(btv.getModel().getItemCount() - 1);
            btv.getTableCursor().setSelection(btv.getModel().getItemCount() - 1, btv.getTableCursor().getColumn());
            br.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public void addPages() {
        SelectFileWizardPage page1 = new SelectFileWizardPage("");
        MakeAssociationsWizardPage page2 = new MakeAssociationsWizardPage("");
        addPage(page1);
        addPage(page2);
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isSkipFirstLine() {
        return skipFirstLine;
    }

    public void setSkipFirstLine(boolean skipFirstLine) {
        this.skipFirstLine = skipFirstLine;
    }

    public Map<String, Integer> getAssociations() {
        return associations;
    }
}
