package org.neuroph.netbeans.ide.project.type.ocr.imr;

import java.awt.Component;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.StringTokenizer;
import javax.swing.JComponent;
import javax.swing.event.ChangeListener;
import org.neuroph.contrib.imgrec.ColorMode;
import org.neuroph.contrib.imgrec.FractionRgbData;
import org.neuroph.contrib.imgrec.ImageRecognitionHelper;
import org.neuroph.contrib.imgrec.image.Dimension;
import org.neuroph.contrib.ocr.OcrHelper;
import org.neuroph.core.NeuralNetwork;
import org.neuroph.core.learning.TrainingSet;
import org.neuroph.netbeans.ide.project.NeurophProjectFilesFactory;
import org.neuroph.netbeans.ide.project.type.ocr.UtilWizardPanel1;
import org.neuroph.netbeans.ide.project.type.ocr.UtilWizardPanel2;
import org.neuroph.netbeans.ide.project.type.ocr.utils.ImageRecognitionCommunication;
import org.neuroph.netbeans.ide.project.type.ocr.utils.ImagesLoader;
import org.neuroph.netbeans.ideservices.CreateTrainigSetFileServiceInterface;
import org.neuroph.util.TransferFunctionType;
import org.neuroph.util.VectorParser;
import org.openide.WizardDescriptor;
import org.openide.windows.TopComponent;
import org.openide.windows.WindowManager;

public final class IMRWizardIterator implements WizardDescriptor.InstantiatingIterator {

    private int index;

    private WizardDescriptor wizard;

    private WizardDescriptor.Panel[] panels;

    /**
     * Initialize panels representing individual wizard's steps and sets
     * various properties for them influencing wizard appearance.
     */
    private WizardDescriptor.Panel[] getPanels() {
        if (panels == null) {
            panels = new WizardDescriptor.Panel[] { new ImageRecognitionWizardPanel1(), new ImageRecognitionWizardPanel2(), new UtilWizardPanel1(), new UtilWizardPanel2() };
            String[] steps = createSteps();
            for (int i = 0; i < panels.length; i++) {
                Component c = panels[i].getComponent();
                if (steps[i] == null) {
                    steps[i] = c.getName();
                }
                if (c instanceof JComponent) {
                    JComponent jc = (JComponent) c;
                    jc.putClientProperty("WizardPanel_contentSelectedIndex", new Integer(i));
                    jc.putClientProperty("WizardPanel_contentData", steps);
                    jc.putClientProperty("WizardPanel_autoWizardStyle", Boolean.TRUE);
                    jc.putClientProperty("WizardPanel_contentDisplayed", Boolean.TRUE);
                    jc.putClientProperty("WizardPanel_contentNumbered", Boolean.TRUE);
                }
            }
        }
        return panels;
    }

    public Set instantiate() throws IOException {
        String imageDir = (String) wizard.getProperty("imageDir");
        String junkDir = (String) wizard.getProperty("junkDir");
        ColorMode colorMode = (ColorMode) wizard.getProperty("colorMode");
        Dimension samplingResollution = (Dimension) wizard.getProperty("resolution");
        String trainigSetName = (String) wizard.getProperty("trainigSetName");
        List<String> imageLabels = createTrainingSet(imageDir, junkDir, samplingResollution, colorMode, trainigSetName);
        String neuralNetworkName = (String) wizard.getProperty("networkName");
        String neurons = (String) wizard.getProperty("neurons");
        String transferFunction = (String) wizard.getProperty("transferFunction");
        createNeuralNetwork(neuralNetworkName, transferFunction, samplingResollution, neurons, imageLabels, colorMode);
        TopComponent tc = WindowManager.getDefault().findTopComponent("ImgRecTestTopComponent");
        tc.open();
        return Collections.EMPTY_SET;
    }

    public void initialize(WizardDescriptor wizard) {
        this.wizard = wizard;
    }

    public void uninitialize(WizardDescriptor wizard) {
        panels = null;
    }

    public WizardDescriptor.Panel current() {
        return getPanels()[index];
    }

    public String name() {
        return index + 1 + ". from " + getPanels().length;
    }

    public boolean hasNext() {
        return index < getPanels().length - 1;
    }

    public boolean hasPrevious() {
        return index > 0;
    }

    public void nextPanel() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        index++;
    }

    public void previousPanel() {
        if (!hasPrevious()) {
            throw new NoSuchElementException();
        }
        index--;
    }

    public void addChangeListener(ChangeListener l) {
    }

    public void removeChangeListener(ChangeListener l) {
    }

    private String[] createSteps() {
        String[] beforeSteps = null;
        Object prop = wizard.getProperty("WizardPanel_contentData");
        if (prop != null && prop instanceof String[]) {
            beforeSteps = (String[]) prop;
        }
        if (beforeSteps == null) {
            beforeSteps = new String[0];
        }
        String[] res = new String[(beforeSteps.length - 1) + panels.length];
        for (int i = 0; i < res.length; i++) {
            if (i < (beforeSteps.length - 1)) {
                res[i] = beforeSteps[i];
            } else {
                res[i] = panels[i - beforeSteps.length + 1].getComponent().getName();
            }
        }
        return res;
    }

    private List<String> createTrainingSet(String imageDir, String junkDir, Dimension samplingResolution, ColorMode colorMode, String trainigSetName) {
        HashMap<String, FractionRgbData> rgbDataMap = new HashMap<String, FractionRgbData>();
        List imageLabels = new ArrayList<String>();
        try {
            File labeledImagesDir = new File(imageDir);
            rgbDataMap.putAll(ImagesLoader.getFractionRgbDataForDirectory(labeledImagesDir, samplingResolution));
            for (String imgName : rgbDataMap.keySet()) {
                StringTokenizer st = new StringTokenizer(imgName, "._");
                String imageLabel = st.nextToken();
                if (!imageLabels.contains(imageLabel)) {
                    imageLabels.add(imageLabel);
                }
            }
            Collections.sort(imageLabels);
        } catch (IOException ioe) {
            System.err.println("Unable to load images from labeled images dir: '" + imageDir + "'");
            System.err.println(ioe.toString());
        }
        if ((junkDir != null) && (!junkDir.equals(""))) {
            try {
                File junkImagesDir = new File(junkDir);
                rgbDataMap.putAll(ImagesLoader.getFractionRgbDataForDirectory(junkImagesDir, samplingResolution));
            } catch (IOException ioe) {
                System.err.println("Unable to load images from junk images dir: '" + junkDir + "'");
                System.err.println(ioe.toString());
            }
        }
        TrainingSet activeTrainingSet = null;
        if (colorMode == ColorMode.FULL_COLOR) {
            activeTrainingSet = ImageRecognitionHelper.createTrainingSet(imageLabels, rgbDataMap);
        } else {
            activeTrainingSet = ImageRecognitionHelper.createBlackAndWhiteTrainingSet(imageLabels, rgbDataMap);
        }
        activeTrainingSet.setLabel(trainigSetName);
        for (CreateTrainigSetFileServiceInterface fileservices : ServiceLoader.load(CreateTrainigSetFileServiceInterface.class)) {
            fileservices.serialise(activeTrainingSet);
        }
        ImageRecognitionCommunication.getInstance().setTrainingSet(activeTrainingSet);
        return imageLabels;
    }

    public void createNeuralNetwork(String neuralNetworkName, String transferFunction, Dimension resolution, String hiddenLayersStr, List<String> imageLabels, ColorMode colorMode) {
        ArrayList<Integer> hiddenLayersNeuronsCount = VectorParser.parseInteger(hiddenLayersStr.trim());
        NeuralNetwork activeNeuralNetwork = OcrHelper.createNewNeuralNetwork(neuralNetworkName, resolution, colorMode, imageLabels, hiddenLayersNeuronsCount, TransferFunctionType.valueOf(transferFunction.toUpperCase()));
        NeurophProjectFilesFactory.getDefault().createNeuralNetworkFile(activeNeuralNetwork);
    }
}
