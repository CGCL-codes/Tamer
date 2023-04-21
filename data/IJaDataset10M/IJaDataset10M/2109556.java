package versusSNP.util.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import versusSNP.Parameter;
import versusSNP.gui.UICaption;
import versusSNP.gui.widgets.MyFileChooser;
import versusSNP.gui.widgets.MyFileFilter;

public class BrowseEnsureAction implements ActionListener {

    private JTextField txtField;

    private MyFileFilter[] filters;

    private String ensureName;

    private BrowseEnsureAction(JTextField txtField) {
        this.txtField = txtField;
        this.filters = new MyFileFilter[] {};
    }

    private BrowseEnsureAction(JTextField txtField, MyFileFilter[] filters) {
        this.txtField = txtField;
        this.filters = filters;
    }

    public BrowseEnsureAction(JTextField txtField, String ensureName) {
        this(txtField);
        this.ensureName = ensureName;
    }

    public BrowseEnsureAction(JTextField txtField, MyFileFilter[] filters, String ensureName) {
        this(txtField, filters);
        this.ensureName = ensureName;
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals(UICaption.dialog_caption_browse)) {
            MyFileChooser chooser = new MyFileChooser(Parameter.path_program_blastall);
            if (filters.length != 0) {
                for (MyFileFilter filter : filters) chooser.addChoosableFileFilter(filter);
                chooser.setFileFilter(filters[0]);
            }
            loopFileChooser(chooser);
        }
    }

    private void loopFileChooser(MyFileChooser chooser) {
        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            if (txtField != null) {
                if (chooser.getSelectedFile().getName().startsWith(ensureName)) {
                    txtField.setText(chooser.getSelectedFile().getAbsolutePath());
                } else {
                    if (ensureName.equals("blastall")) JOptionPane.showMessageDialog(null, UICaption.dialog_label_prompt_blast_path_blastall, UICaption.dialog_caption_error, JOptionPane.ERROR_MESSAGE); else if (ensureName.equals("formatdb")) JOptionPane.showMessageDialog(null, UICaption.dialog_label_prompt_blast_path_formatdb, UICaption.dialog_caption_error, JOptionPane.ERROR_MESSAGE);
                    loopFileChooser(chooser);
                }
            }
        }
    }

    public void setTxtField(JTextField txtField) {
        this.txtField = txtField;
    }
}
