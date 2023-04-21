package org.speech.asr.gui.view.editor.dictionary;

import com.jgoodies.forms.layout.RowSpec;
import com.jgoodies.forms.layout.Size;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.constant.ConfirmationResult;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.binding.value.support.ValueHolder;
import org.springframework.richclient.dialog.FormBackedDialogPage;
import org.springframework.richclient.dialog.TitledPageApplicationDialog;
import org.springframework.richclient.filechooser.FileChooserComboBox;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.Form;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.swing.SandboxSwingBindingFactory;
import org.springframework.richclient.layout.TableLayoutBuilder;
import javax.swing.*;
import java.util.Arrays;
import java.io.File;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 10, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class DictionaryImportDialog extends TitledPageApplicationDialog {

    /**
   * slf4j Logger.
   */
    private static final Logger log = LoggerFactory.getLogger(DictionaryImportDialog.class.getName());

    private ValidatingFormModel formModel;

    private DictionaryImportModel model;

    private ConfirmationResult result;

    private String dialogId;

    private FileChooserComboBox fileChooser;

    public DictionaryImportDialog() {
        this.dialogId = "dictionaryImport";
        model = new DictionaryImportModel();
        formModel = FormModelHelper.createFormModel(model, true, dialogId);
        Form form = new DictionaryImportForm();
        setDialogPage(new FormBackedDialogPage(form));
        setTitle(getMessage(dialogId + ".title"));
    }

    protected boolean onFinish() {
        formModel.commit();
        result = ConfirmationResult.OK;
        return true;
    }

    protected void onCancel() {
        super.onCancel();
        result = ConfirmationResult.CANCEL;
    }

    private class DictionaryImportForm extends AbstractForm {

        public DictionaryImportForm() {
            super(formModel);
            setId(dialogId);
        }

        protected JComponent createFormControl() {
            TableLayoutBuilder table = new TableLayoutBuilder();
            SandboxSwingBindingFactory bf = new SandboxSwingBindingFactory(getFormModel());
            fileChooser = new FileChooserComboBox(getFormModel(), "path");
            fileChooser.setLabelMessageCode(dialogId + ".path.label");
            table.row();
            table.cell(fileChooser.getControl());
            table.relatedGapRow();
            table.cell(new JLabel("format"), "colGrId=labels colSpec=right:pref").labelGapCol();
            table.cell(bf.createBoundComboBox("format", new ValueHolder(Arrays.asList(new String[] { "elektroda.pl", "sphinx4" }))).getControl());
            return table.getPanel();
        }
    }

    private class DictionaryImportModel {

        private String path;

        private String format;

        public String getPath() {
            return path;
        }

        public void setPath(String path) {
            this.path = path;
        }

        public String getFormat() {
            return format;
        }

        public void setFormat(String format) {
            this.format = format;
        }
    }

    public File getSelectedFile() {
        return fileChooser.getSelectedFile();
    }

    public String getFormat() {
        return model.getFormat();
    }

    /**
   * Getter dla pola 'result'.
   *
   * @return wartosc pola 'result'.
   */
    public ConfirmationResult getResult() {
        return result;
    }
}
