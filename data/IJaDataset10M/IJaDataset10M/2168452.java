package net.sf.pim.action;

import net.sf.component.config.ConfigHelper;
import net.sf.pim.UiUtil;
import net.sf.pim.model.psp.WorkList;
import net.sf.util.persistence.LegacyWorkDataManager;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;

/**
 * @author lzhang
 */
public class ImportAction extends UiAction {

    private String path = ConfigHelper.getStringProperty("work.importhome");

    public ImportAction() {
        super();
        name = "查看...";
        gif = "import.gif";
    }

    public void run() {
        super.run();
        if (UiUtil.getActiveTableEditor().getName().equals("日志")) {
            parent.checkSave();
            FileDialog fd = new FileDialog(parent.getTv().getTable().getShell(), SWT.OPEN | SWT.MULTI);
            fd.setText("查看文件中的工作列表");
            fd.setFilterExtensions(new String[] { "*.xml" });
            fd.setFilterPath(path);
            fd.open();
            String fnames[] = fd.getFileNames();
            if (fnames == null || fnames.length == 0) return;
            path = fd.getFilterPath();
            for (int i = 0; i < fnames.length; i++) fnames[i] = fd.getFilterPath() + "/" + fnames[i];
            parent.setMulti(true);
            parent.setData(new WorkList(LegacyWorkDataManager.importWork(fnames)));
        }
    }
}
