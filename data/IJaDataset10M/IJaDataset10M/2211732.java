package test.swing.controls;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JDialog;

/**
 *
 * @author detlevs
 */
public class Java2AutoControlsTester extends JDialog {

    private Java2sAutoTextField txtControl;

    private Java2sAutoComboBox cboControl;

    public Java2AutoControlsTester() {
        setTitle("Java2AutoControlsTester");
        setSize(100, 100);
        cboControl = new Java2sAutoComboBox(getListData());
        txtControl = new Java2sAutoTextField(getListData(), cboControl);
        txtControl.setEditable(true);
        add(txtControl);
        setVisible(true);
    }

    private List getListData() {
        File file = new File(".");
        String[] files = file.list();
        return Arrays.asList(files);
    }

    public static void main(String[] args) {
        new Java2AutoControlsTester();
    }
}
