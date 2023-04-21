package org.speech.asr.gui.view.repo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.speech.asr.gui.constant.ConfirmationResult;
import org.speech.asr.gui.context.AppContext;
import org.speech.asr.common.entity.CorpusEntity;
import org.speech.asr.gui.util.dictionary.DictionariesSource;
import org.speech.asr.gui.util.dictionary.model.DictionaryValueModelWrapper;
import org.speech.asr.gui.util.dictionary.model.ValueType;
import org.springframework.binding.form.ValidatingFormModel;
import org.springframework.binding.value.ValueModel;
import org.springframework.richclient.dialog.CompositeDialogPage;
import org.springframework.richclient.dialog.TitledPageApplicationDialog;
import org.springframework.richclient.dialog.TreeCompositeDialogPage;
import org.springframework.richclient.form.AbstractForm;
import org.springframework.richclient.form.Form;
import org.springframework.richclient.form.FormModelHelper;
import org.springframework.richclient.form.binding.swing.SandboxSwingBindingFactory;
import org.springframework.richclient.form.builder.TableFormBuilder;
import javax.swing.*;

/**
 * //@todo class description
 * <p/>
 * Creation date: May 13, 2009 <br/>
 * <a href="http://www.grapesoftware.com">www.grapesoftware.com</a>
 *
 * @author Lukasz Olczak
 */
public class CorpusPropertiesDialog extends TitledPageApplicationDialog {

    /**
   * slf4j Logger.
   */
    private static final Logger log = LoggerFactory.getLogger(CorpusPropertiesDialog.class.getName());

    private ValidatingFormModel corpusFormModel;

    private CompositeDialogPage compositePage;

    private CorpusEntity corpus;

    private ConfirmationResult result;

    private String dialogId;

    private CorpusPropertiesDialog(CorpusEntity corpus, String dialogId) {
        this.corpus = corpus;
        this.dialogId = dialogId;
        createValueModel();
        setValueModelWrappers();
        compositePage = new TreeCompositeDialogPage(dialogId);
        Form corpusForm = new CorpusForm();
        Form audioFormatForm = new AudioFormatFormat();
        compositePage.addForm(corpusForm);
        compositePage.addForm(audioFormatForm);
        setDialogPage(compositePage);
        setTitle(getMessage(dialogId + ".title"));
    }

    private void setValueModelWrappers() {
        wrapProperty("audioFormat.encoding", ValueType.STRING, "audio.format.encoding");
        wrapProperty("audioFormat.samplingRate", ValueType.INTEGER, "audio.format.samplingRate");
        wrapProperty("audioFormat.sampleSize", ValueType.INTEGER, "audio.format.sampleSizeInBits");
        wrapProperty("audioFormat.endian", ValueType.INTEGER, "audio.format.endian");
        wrapProperty("language", ValueType.STRING, "language");
    }

    private void wrapProperty(String propertyName, ValueType type, String prefix) {
        DictionariesSource dictSource = AppContext.getInstance().getDictionarySource();
        ValueModel vm = corpusFormModel.getValueModel(propertyName);
        DictionaryValueModelWrapper wrapper = new DictionaryValueModelWrapper(dictSource, type, prefix, vm);
        corpusFormModel.add(propertyName, wrapper.getValueModel());
    }

    public static CorpusPropertiesDialog createNewDialog(CorpusEntity corpus) {
        return new CorpusPropertiesDialog(corpus, "newCorpus");
    }

    public static CorpusPropertiesDialog createEditDialog(CorpusEntity corpus) {
        return new CorpusPropertiesDialog(corpus, "editCorpus");
    }

    private void createValueModel() {
        corpusFormModel = FormModelHelper.createFormModel(corpus, true, dialogId);
    }

    protected boolean onFinish() {
        corpusFormModel.commit();
        result = ConfirmationResult.OK;
        return true;
    }

    protected void onCancel() {
        super.onCancel();
        result = ConfirmationResult.CANCEL;
    }

    private class CorpusForm extends AbstractForm {

        protected CorpusForm() {
            super(corpusFormModel);
            setId("corpus");
        }

        protected JComponent createFormControl() {
            SandboxSwingBindingFactory bf = new SandboxSwingBindingFactory(getFormModel());
            TableFormBuilder formBuilder = new TableFormBuilder(getBindingFactory());
            DictionariesSource dictSource = AppContext.getInstance().getDictionarySource();
            formBuilder.setLabelAttributes("colGrId=label colSpec=right:pref");
            formBuilder.addSeparator("General");
            formBuilder.row();
            formBuilder.add("name");
            formBuilder.row();
            formBuilder.add("description");
            formBuilder.row();
            formBuilder.add(bf.createBoundComboBox("language", dictSource.getItems("language"), "text"));
            formBuilder.row();
            return formBuilder.getForm();
        }
    }

    private class AudioFormatFormat extends AbstractForm {

        protected AudioFormatFormat() {
            super(corpusFormModel);
            setId("audioFormat");
        }

        protected JComponent createFormControl() {
            SandboxSwingBindingFactory bf = new SandboxSwingBindingFactory(getFormModel());
            TableFormBuilder formBuilder = new TableFormBuilder(getBindingFactory());
            DictionariesSource dictSource = AppContext.getInstance().getDictionarySource();
            formBuilder.setLabelAttributes("colGrId=label colSpec=right:pref");
            formBuilder.addSeparator("Audio format");
            formBuilder.row();
            formBuilder.add(bf.createBoundComboBox("audioFormat.encoding", dictSource.getItems("audio.format.encoding"), "text"));
            formBuilder.row();
            formBuilder.add(bf.createBoundComboBox("audioFormat.samplingRate", dictSource.getItems("audio.format.samplingRate"), "text"));
            formBuilder.row();
            formBuilder.add(bf.createBoundComboBox("audioFormat.sampleSize", dictSource.getItems("audio.format.sampleSizeInBits"), "text"));
            formBuilder.row();
            formBuilder.add("audioFormat.signed");
            formBuilder.row();
            formBuilder.add(bf.createBoundComboBox("audioFormat.endian", dictSource.getItems("audio.format.endian"), "text"));
            formBuilder.row();
            return formBuilder.getForm();
        }
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
