package com.nhncorp.cubridqa.configuration.composite;

import java.io.File;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import com.nhncorp.cubridqa.configuration.SvnModel;
import com.nhncorp.cubridqa.utils.PropertiesUtil;
import com.nhncorp.cubridqa.utils.XstreamHelper;

/**
 * 
 * @deprecated
 *
 */
public class SvnComposite extends ParentComposite {

    private Text svnPasswd;

    private Text svnUser;

    private Text svnUrl;

    private String filePath;

    public SvnComposite(Composite parent, int style, String filePath) {
        super(parent, style);
        this.filePath = filePath;
        final GridLayout gridLayout = new GridLayout();
        gridLayout.numColumns = 2;
        setLayout(gridLayout);
        final Label label_6 = new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        final Label label_7 = new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        final Label svnUrlLabel = new Label(this, SWT.NONE);
        svnUrlLabel.setText("SVN URL:");
        svnUrl = new Text(this, SWT.BORDER);
        final GridData gd_svnUrl = new GridData(SWT.FILL, SWT.CENTER, true, false);
        svnUrl.setLayoutData(gd_svnUrl);
        final Label label = new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        final Label label_1 = new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        final Label svnUserLabel = new Label(this, SWT.NONE);
        svnUserLabel.setText("SVN User:");
        svnUser = new Text(this, SWT.BORDER);
        final GridData gd_svnUser = new GridData(SWT.FILL, SWT.CENTER, true, false);
        svnUser.setLayoutData(gd_svnUser);
        final Label label_2 = new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        final Label label_3 = new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        final Label svnPasswordLabel = new Label(this, SWT.NONE);
        svnPasswordLabel.setText("SVN Password:");
        svnPasswd = new Text(this, SWT.BORDER | SWT.PASSWORD);
        final GridData gd_svnPasswd = new GridData(SWT.FILL, SWT.CENTER, true, false);
        svnPasswd.setLayoutData(gd_svnPasswd);
        final Label label_4 = new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        final Label label_5 = new Label(this, SWT.NONE);
        new Label(this, SWT.NONE);
        loadData();
    }

    private void loadData() {
        File file = new File(filePath);
        if (file != null && file.exists()) {
            SvnModel model = (SvnModel) XstreamHelper.fromXml(this.filePath);
            this.svnUrl.setText(model.getSvnUrl());
            this.svnUser.setText(model.getSvnUser());
            this.svnPasswd.setText(model.getSvnPasswd());
        }
    }

    @Override
    public boolean save() {
        SvnModel model = new SvnModel();
        model.setSvnPasswd(svnPasswd.getText());
        model.setSvnUrl(svnUrl.getText());
        model.setSvnUser(svnUser.getText());
        XstreamHelper.save(model, filePath);
        PropertiesUtil.setValue("svnpath", svnUrl.getText());
        PropertiesUtil.setValue("svnuser", svnUser.getText());
        PropertiesUtil.setValue("svnpassword", svnPasswd.getText());
        return true;
    }
}
