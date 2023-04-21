package cn.imgdpu.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import cn.imgdpu.util.GeneralMethod;
import com.swtdesigner.SWTResourceManager;

public class AboutDialog extends Dialog {

    private StyledText styledText;

    protected Object result;

    protected Shell shell;

    /**
	 * Create the dialog
	 * 
	 * @param parent
	 * @param style
	 */
    public AboutDialog(Shell parent, int style) {
        super(parent, style);
    }

    /**
	 * Create the dialog
	 * 
	 * @param parent
	 */
    public AboutDialog(Shell parent) {
        this(parent, SWT.NONE);
    }

    /**
	 * Open the dialog
	 * 
	 * @return the result
	 */
    public Object open() {
        createContents();
        shell.open();
        shell.layout();
        Display display = getParent().getDisplay();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        return result;
    }

    /**
	 * Create contents of the dialog
	 */
    protected void createContents() {
        shell = new Shell(getParent(), SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
        shell.setLayout(new FormLayout());
        shell.setImage(SWTResourceManager.getImage(AboutDialog.class, "/cn/imgdpu/ico/local.gif"));
        GeneralMethod.getGeneralMethod().setDisLoc(shell, 490, 320);
        shell.setSize(490, 320);
        shell.setText("关于GSA");
        final Group group = new Group(shell, SWT.NONE);
        final FormData fd_group = new FormData();
        fd_group.bottom = new FormAttachment(100, -5);
        fd_group.right = new FormAttachment(100, -5);
        fd_group.top = new FormAttachment(0, 5);
        fd_group.left = new FormAttachment(0, 5);
        group.setLayoutData(fd_group);
        group.setLayout(new FormLayout());
        CLabel copyrightLabel;
        copyrightLabel = new CLabel(group, SWT.NONE);
        final FormData fd_copyrightLabel = new FormData();
        fd_copyrightLabel.left = new FormAttachment(0, 8);
        fd_copyrightLabel.top = new FormAttachment(0, 230);
        fd_copyrightLabel.bottom = new FormAttachment(100, -5);
        copyrightLabel.setLayoutData(fd_copyrightLabel);
        copyrightLabel.setImage(SWTResourceManager.getImage(AboutDialog.class, "/cn/imgdpu/ico/home_nav.gif"));
        copyrightLabel.setText("CopyRight 2009 olunx.com\t2009年03月30日");
        Button button;
        button = new Button(group, SWT.NONE);
        fd_copyrightLabel.right = new FormAttachment(button, -5, SWT.LEFT);
        button.setImage(SWTResourceManager.getImage(AboutDialog.class, "/cn/imgdpu/ico/delete_edit.gif"));
        final FormData fd_button = new FormData();
        fd_button.right = new FormAttachment(0, 466);
        fd_button.left = new FormAttachment(0, 345);
        fd_button.bottom = new FormAttachment(100, -5);
        fd_button.top = new FormAttachment(copyrightLabel, 0, SWT.TOP);
        button.setLayoutData(fd_button);
        button.setText("关闭");
        final CLabel label = new CLabel(group, SWT.NONE);
        label.setImage(SWTResourceManager.getImage(AboutDialog.class, "/cn/imgdpu/ico/school48.png"));
        final FormData fd_label = new FormData();
        fd_label.bottom = new FormAttachment(0, 65);
        fd_label.right = new FormAttachment(0, 466);
        fd_label.left = new FormAttachment(0, 8);
        fd_label.top = new FormAttachment(0, 5);
        label.setLayoutData(fd_label);
        label.setText("关于我们\n\nolunx：还可以做很多功能！ Fatkun：OK,就由你负责吧！。olunx:...");
        styledText = new StyledText(group, SWT.WRAP | SWT.V_SCROLL | SWT.READ_ONLY | SWT.BORDER);
        styledText.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_WIDGET_BACKGROUND));
        styledText.setText("简介：\r\n    GSA(Guangdong Pharmaceutical University Student Assistant)，中文名称：广药学生助手。一款饭后余作小软件，希望能给我们平凡的网上生活带来一点点方便，对你有帮助的话就将就着用吧。\r\n\r\n声明：\r\n    本软件公开源码，但仅供用于学习目的。本软件中使用的 [SWT/JFace] [commons-logging] [HttpClient] [FtpClient] [HttpCore] [HttpParser] [SQLite/JDBC] [JDom]  为第三方开源工具包。由于考虑到相关可能涉及到他人商业性的利益，决定不公开某些功能的源码，以免对他人造成不必要的损失。\r\n\r\n程序:olunx(olunx@qq.com) Fatkun(cyk.cn@qq.com)\n文档:Fuquan(fuquan.c@qq.com)");
        final FormData fd_styledText = new FormData();
        fd_styledText.bottom = new FormAttachment(button, -5, SWT.TOP);
        fd_styledText.right = new FormAttachment(100, -5);
        fd_styledText.top = new FormAttachment(label, 5, SWT.BOTTOM);
        fd_styledText.left = new FormAttachment(label, 0, SWT.LEFT);
        styledText.setLayoutData(fd_styledText);
        button.addListener(SWT.Selection, new Listener() {

            @Override
            public void handleEvent(Event arg0) {
                shell.dispose();
            }
        });
    }
}
