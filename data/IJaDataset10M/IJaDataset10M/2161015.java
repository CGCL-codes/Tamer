package com.yinzhijie.dt.runtime.makezip;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IExportWizard;
import org.eclipse.ui.IWorkbench;
import com.yinzhijie.dt.runtime.makezip.ziputil.ZipUtil;

/**
 * zip压缩向导式对话框
 * 
 * @author dingruichao
 * @version 1.0.0
 */
public class RtAddToZipWizard extends Wizard implements IExportWizard {

    public static final String Q1 = "QUESTION_1";

    public static final String Q2 = "QUESTION_2";

    private RtAddToZipPageOne one;

    private RtAddToZipPageTwo two;

    public RtAddToZipWizard() {
        one = new RtAddToZipPageOne();
        two = new RtAddToZipPageTwo();
        this.addPage(one);
        this.addPage(two);
        this.setWindowTitle("zip生成");
    }

    public boolean canFinish() {
        if (this.getContainer().getCurrentPage() == two) if (two.isPageComplete()) {
            return true;
        } else {
            return false;
        } else return false;
    }

    public boolean performFinish() {
        List<File> tozipList = new ArrayList<File>();
        if (two.getParamFile() != null) {
            tozipList.add(two.getParamFile());
        }
        for (Object f : one.getRtFileMap().values().toArray()) {
            tozipList.add((File) f);
        }
        if (two.getCheakFlg() == 1) {
            if (two.getFeatureFile() == null) {
                MessageDialog.openInformation(getShell(), "提示", "请选择Zip文件位置");
                return false;
            }
            try {
                ZipUtil.AddFilesToZip(tozipList, two.getFeatureFile(), "", 7);
                MessageBox messageBox = new MessageBox(getShell(), SWT.OK);
                messageBox.setMessage("文件导出成功");
                messageBox.open();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (two.getCheakFlg() == 2) {
            if (two.getSaveNewFile() == null) {
                MessageDialog.openInformation(getShell(), "提示", "请选择导出文件保存位置");
                return false;
            }
            File saveZipFile = two.getSaveNewFile();
            try {
                saveZipFile.createNewFile();
                ZipUtil.toZip(tozipList, saveZipFile, "", 7);
                MessageBox messageBox = new MessageBox(getShell(), SWT.OK);
                messageBox.setMessage("文件导出成功");
                messageBox.open();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public boolean performCancel() {
        return MessageDialog.openConfirm(getShell(), "确认", "确定取消本次导出zip文件吗?");
    }

    public void init(IWorkbench arg0, IStructuredSelection arg1) {
    }
}
