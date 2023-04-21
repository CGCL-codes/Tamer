package com.safi.workshop.audio;

import java.io.File;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import com.safi.db.server.config.Prompt;
import com.safi.workshop.audio.utils.AudioConverter;
import com.safi.workshop.audio.utils.AudioException;
import com.safi.workshop.audio.utils.AudioUtils;
import com.safi.workshop.part.AsteriskDiagramEditorPlugin;
import com.safi.workshop.part.SafiWorkshopEditorUtil;
import com.swtdesigner.ResourceManager;

public class RecordPromptPage extends WizardPage {

    private AudioRecorder audioRecorder;

    private File convertedFile;

    /**
   * Create the wizard
   */
    public RecordPromptPage() {
        super("wizardPage");
        setTitle("Record Audio Prompt");
        setDescription("Record and edit a new prompt file.");
        setImageDescriptor(ResourceManager.getPluginImageDescriptor(AsteriskDiagramEditorPlugin.getDefault(), "icons/wizban/PromptWizard.gif"));
    }

    /**
   * Create contents of the wizard
   * 
   * @param parent
   */
    public void createControl(Composite parent) {
        Composite container = new Composite(parent, SWT.NULL);
        container.setLayout(new FillLayout());
        setControl(container);
        audioRecorder = new AudioRecorder(container, SWT.NONE) {

            @Override
            protected void audioFileRecorded() {
                setPageComplete(true);
            }
        };
        ImportAudioFileWizard wizard = (ImportAudioFileWizard) getWizard();
        if (wizard.getMode() == ImportAudioFileWizard.Mode.EDIT) {
            Prompt prompt = wizard.getPrompt();
            if (prompt != null) {
                try {
                    File f = SafiWorkshopEditorUtil.getPromptFile(prompt);
                    if (f != null) {
                        AudioConverter converter = new AudioConverter();
                        converter.setDesiredChannels(1);
                        converter.setDesiredSampleRate(8000f);
                        converter.setDesiredEncoding(AudioFormat.Encoding.PCM_SIGNED);
                        converter.setInputFile(f.getAbsolutePath());
                        AudioInputStream stream = converter.convert();
                        File nf = File.createTempFile("temp", "wav");
                        nf.deleteOnExit();
                        AudioSystem.write(stream, AudioFileFormat.Type.WAVE, nf);
                        audioRecorder.setFile(nf);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    MessageDialog.openError(getShell(), "Load Audio Error", "Couldn't load existing audio file for prompt " + prompt.getName() + ": " + e.getLocalizedMessage());
                }
            }
        }
    }

    static int[] exp_lut = { 0, 132, 396, 924, 1980, 4092, 8316, 16764 };

    int ulaw2linear(int ulawbyte) {
        int sign, exponent, mantissa, sample;
        ulawbyte = ~ulawbyte;
        sign = (ulawbyte & 0x80);
        exponent = (ulawbyte >> 4) & 0x07;
        mantissa = ulawbyte & 0x0F;
        sample = exp_lut[exponent] + (mantissa << (exponent + 3));
        if (sign != 0) sample = -sample;
        return (sample);
    }

    @Override
    public boolean isPageComplete() {
        return audioRecorder.isSaveable();
    }

    public File getFile() {
        return audioRecorder.getFile();
    }

    public File getConvertedFile() throws Exception {
        audioRecorder.save();
        File f = getFile();
        if (f == null) throw new AudioException("No file was recorded");
        return AudioUtils.convertToGSM(f);
    }

    @Override
    public void dispose() {
        audioRecorder.dispose();
        super.dispose();
    }
}
