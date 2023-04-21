package sandbox.tool.data.example.swt;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Sash;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import sandbox.tool.app.CloseCallback;

public class Main {

    public static void main(final String[] args) {
        new SwtApplication() {

            protected Shell createMainShell(final Display display) {
                final Shell shell = new Shell(display);
                shell.setLayout(new FormLayout());
                final Text text1 = new Text(shell, SWT.MULTI);
                final FormData text1FormData = new FormData();
                text1FormData.left = new FormAttachment(0);
                text1FormData.top = new FormAttachment(0);
                text1FormData.bottom = new FormAttachment(100);
                text1.setLayoutData(text1FormData);
                final Sash sash = new Sash(shell, SWT.VERTICAL);
                final FormData sashFormData = new FormData();
                sashFormData.top = new FormAttachment(0);
                sashFormData.bottom = new FormAttachment(100);
                sashFormData.left = new FormAttachment(text1);
                sashFormData.width = 8;
                sash.setLayoutData(sashFormData);
                final Text text2 = new Text(shell, SWT.MULTI);
                final FormData text2FormData = new FormData();
                text2FormData.top = new FormAttachment(0);
                text2FormData.left = new FormAttachment(sash);
                text2FormData.right = new FormAttachment(100);
                text2FormData.bottom = new FormAttachment(100);
                text2.setLayoutData(text2FormData);
                return shell;
            }
        }.start(new CloseCallback() {

            public void onClose() {
                System.out.println("Exit");
            }
        });
    }
}
