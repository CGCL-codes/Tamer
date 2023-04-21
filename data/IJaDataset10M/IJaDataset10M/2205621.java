package com.ivis.xprocess.ui.views;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.part.ViewPart;
import com.ivis.xprocess.ui.util.ViewUtil;

public class OutputView extends ViewPart {

    private PrintStream orignalSystemOut = System.out;

    private Text outputArea;

    @Override
    public void createPartControl(Composite parent) {
        parent.setLayout(new FillLayout());
        outputArea = new Text(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.WRAP);
        System.setOut(new ViewPrintStream(new ViewStream()));
    }

    @Override
    public void setFocus() {
    }

    @Override
    public void dispose() {
        System.setOut(orignalSystemOut);
        super.dispose();
    }

    class ViewStream extends OutputStream {

        @Override
        public void write(int b) throws IOException {
            byte[] singleByte = new byte[] { (byte) b };
            final String str = new String(singleByte);
            Display display = ViewUtil.getDisplay();
            display.asyncExec(new Runnable() {

                public void run() {
                    if ((outputArea != null) && !outputArea.isDisposed()) {
                        outputArea.append(str);
                    }
                }
            });
        }
    }

    class ViewPrintStream extends PrintStream {

        public ViewPrintStream(OutputStream out) {
            super(out);
        }
    }
}
