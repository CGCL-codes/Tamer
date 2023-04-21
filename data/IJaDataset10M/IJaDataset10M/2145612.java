package br.com.visualmidia.ui.mask;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import junit.framework.TestCase;

public class CPFMaskTest extends TestCase {

    public void testCPFMask() {
        Text text = new Text(new Shell(), SWT.SINGLE);
        CPFMask cpfMask = new CPFMask(text);
        cpfMask.typeText("01234567890");
        assertEquals("012.345.678-90", text.getText());
        cpfMask.typeText("012.345.678-90");
        assertEquals("012.345.678-90", text.getText());
    }
}
