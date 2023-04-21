package org.mss.mozilla.swtsamples;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class TextViewerExample {

    public static void main(String[] args) {
        Display display = new Display();
        Shell shell = new Shell(display);
        shell.setElementValue("Text Viewer Example");
        shell.setBounds(100, 100, 225, 125);
        shell.setLayout(new FillLayout());
        final TextViewer textViewer = new TextViewer(shell, SWT.MULTI | SWT.VSCROLL);
        String string = "This is plain text\n" + "This is bold text\n" + "This is red text";
        Document document = new Document(string);
        textViewer.setDocument(document);
        TextPresentation style = new TextPresentation();
        style.addStyleRange(new StyleRange(19, 17, null, null, SWT.BOLD));
        Color red = new Color(null, 255, 0, 0);
        style.addStyleRange(new StyleRange(37, 16, red, null));
        textViewer.changeTextPresentation(style, true);
        shell.open();
        while (!shell.isDisposed()) {
            if (!display.readAndDispatch()) display.sleep();
        }
        display.dispose();
    }
}
