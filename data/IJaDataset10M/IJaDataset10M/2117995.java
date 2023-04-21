package bunwarpj;

import ij.IJ;
import ij.ImagePlus;
import ij.Macro;
import ij.gui.GenericDialog;
import ij.gui.ImageCanvas;
import ij.gui.PointRoi;
import ij.gui.Roi;
import ij.gui.Toolbar;
import ij.process.FloatProcessor;
import ij.process.ImageProcessor;
import java.awt.Choice;
import java.awt.Component;
import java.awt.Frame;
import java.awt.Rectangle;
import java.awt.Scrollbar;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowEvent;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class to create the dialog for bUnwarpJ.
 */
public class MainDialog extends GenericDialog {

    /** fast mode constant */
    public static int FAST_MODE = 0;

    /** accurate mode constant */
    public static int ACCURATE_MODE = 1;

    /** unidirectional ("mono") mode constant */
    public static int MONO_MODE = 2;

    /** List of available images in ImageJ */
    private ImagePlus[] imageList;

    /** Canvas of the source image */
    private ImageCanvas sourceIc;

    /** Canvas of the target image */
    private ImageCanvas targetIc;

    /** Image representation for source image */
    private ImagePlus sourceImp;

    /** Image representation for target image */
    private ImagePlus targetImp;

    /** initial source image processor */
    private ImageProcessor originalSourceIP;

    /** initial target image processor */
    private ImageProcessor originalTargetIP;

    /** Model for source image */
    private BSplineModel source;

    /** Model for target image */
    private BSplineModel target;

    /** Mask for source image */
    private Mask sourceMsk;

    /** Mask for target image */
    private Mask targetMsk;

    /** Initial affine matrix for the source image */
    private double[][] sourceAffineMatrix = null;

    /** Initial affine matrix for the target image */
    private double[][] targetAffineMatrix = null;

    /** Point handlers for the landmarks in the source image */
    private PointHandler sourcePh;

    /** Point handlers for the landmarks in the target image */
    private PointHandler targetPh;

    /** Boolean for clearing mask */
    private boolean clearMask = false;

    /** Toolbar handler */
    private PointToolbar tb = new PointToolbar(Toolbar.getInstance(), this);

    /** flag to see if the finalAction was launched */
    private boolean finalActionLaunched = false;

    /** flag to stop the registration */
    private boolean stopRegistration = false;

    /** index of the source choice */
    private int sourceChoiceIndex = 0;

    /** index of the target choice */
    private int targetChoiceIndex = 1;

    /** minimum scale deformation */
    private int min_scale_deformation = 0;

    /** maximum scale deformation */
    private int max_scale_deformation = 2;

    /** mode ("Accurate" by default) */
    private int mode = MainDialog.ACCURATE_MODE;

    /** image subsampling factor at highest resolution level */
    private int maxImageSubsamplingFactor = 0;

    /** divergence weight */
    private double divWeight = 0;

    /** curl weight */
    private double curlWeight = 0;

    /** landmarks weight */
    private double landmarkWeight = 0;

    /** image similarity weight */
    private double imageWeight = 1;

    /** consistency weight */
    private double consistencyWeight = 10;

    /** flag for rich output (verbose option) */
    private boolean richOutput = false;

    /** flag for save transformation option */
    private boolean saveTransformation = false;

    /** minimum image scale */
    private int min_scale_image = 0;

    /** maximum depth for the image pyramid */
    private int imagePyramidDepth = 3;

    /** stopping threshold */
    private double stopThreshold = 1e-2;

    /** consistency flag (if false, unidirectional registration is done) */
    private boolean bIsReverse = true;

    /** macro flag */
    private boolean bMacro = false;

    /** region of interest of the source image before calling the plugin */
    private Roi previousSourceRoi;

    /** region of interest of the target image before calling the plugin */
    private Roi previousTargetRoi;

    /** source image choice */
    private Choice sourceChoice = null;

    /** target image choice */
    private Choice targetChoice = null;

    /** minimum scale choice */
    private Choice minScaleChoice = null;

    /** maximum scale choice */
    private Choice maxScaleChoice = null;

    /** accurate mode choice */
    private Choice modeChoice = null;

    /** resampling text field */
    private TextField resamplingTextField = null;

    /** resampling scroll bar */
    private Scrollbar resamplingSlider = null;

    /** consistency weight text field */
    private TextField consistencyWeightTextField = null;

    /**
	 * Create a new instance of MainDialog.
	 *
	 * @param parentWindow pointer to the parent window
	 * @param imageList list of images from ImageJ
	 * @param mode default registration mode (0 = Fast, 1 = Accurate, 2 = Mono)
	 * @param maxImageSubsamplingFactor subsampling factor at highest resolution level
	 * @param min_scale_deformation default minimum scale deformation value
	 * @param max_scale_deformation default maximum scale deformation value
	 * @param divWeight default divergence weight
	 * @param curlWeight default curl weight
	 * @param landmarkWeight default landmarks weight
	 * @param imageWeight default image similarity weight
	 * @param consistencyWeight default consistency weight
	 * @param stopThreshold default stopping threshold
	 * @param richOutput default verbose flag
	 * @param saveTransformation default save transformations flag
	 */
    public MainDialog(final Frame parentWindow, final ImagePlus[] imageList, final int mode, final int maxImageSubsamplingFactor, final int min_scale_deformation, final int max_scale_deformation, final double divWeight, final double curlWeight, final double landmarkWeight, final double imageWeight, final double consistencyWeight, final double stopThreshold, final boolean richOutput, final boolean saveTransformation) {
        super("bUnwarpJ", null);
        setModal(false);
        this.imageList = imageList;
        this.mode = mode;
        this.maxImageSubsamplingFactor = maxImageSubsamplingFactor;
        this.min_scale_deformation = min_scale_deformation;
        this.max_scale_deformation = max_scale_deformation;
        this.divWeight = divWeight;
        this.curlWeight = curlWeight;
        this.landmarkWeight = landmarkWeight;
        this.imageWeight = imageWeight;
        this.consistencyWeight = consistencyWeight;
        this.stopThreshold = stopThreshold;
        this.richOutput = richOutput;
        this.saveTransformation = saveTransformation;
        String[] titles = new String[imageList.length];
        for (int i = 0; i < titles.length; ++i) titles[i] = imageList[i].getTitle();
        addChoice("Source_Image", titles, titles[sourceChoiceIndex]);
        this.sourceChoice = (Choice) super.getChoices().lastElement();
        addChoice("Target_Image", titles, titles[targetChoiceIndex]);
        this.targetChoice = (Choice) super.getChoices().lastElement();
        String[] sRegistrationModes = { "Fast", "Accurate", "Mono" };
        addChoice("Registration Mode", sRegistrationModes, sRegistrationModes[this.mode]);
        this.modeChoice = (Choice) super.getChoices().lastElement();
        this.addSlider("Image_Subsample_Factor", 0, 7, this.maxImageSubsamplingFactor);
        this.resamplingSlider = (Scrollbar) (super.getSliders().lastElement());
        this.resamplingSlider.addAdjustmentListener(new AdjustmentListener() {

            public void adjustmentValueChanged(AdjustmentEvent adjustmentEvent) {
                MainDialog.this.maxImageSubsamplingFactor = Integer.parseInt(resamplingTextField.getText());
            }
        });
        this.resamplingTextField = (TextField) (super.getNumericFields().lastElement());
        this.resamplingTextField.setEnabled(false);
        addMessage("------ Advanced Options ------");
        String[] sMinScaleDeformationChoices = { "Very Coarse", "Coarse", "Fine", "Very Fine" };
        addChoice("Initial_Deformation :", sMinScaleDeformationChoices, sMinScaleDeformationChoices[this.min_scale_deformation]);
        this.minScaleChoice = (Choice) super.getChoices().lastElement();
        String[] sMaxScaleDeformationChoices = { "Very Coarse", "Coarse", "Fine", "Very Fine", "Super Fine" };
        addChoice("Final_Deformation :", sMaxScaleDeformationChoices, sMaxScaleDeformationChoices[this.max_scale_deformation]);
        this.maxScaleChoice = (Choice) super.getChoices().lastElement();
        addNumericField("Divergence_Weight :", this.divWeight, 1);
        addNumericField("Curl_Weight :", this.curlWeight, 1);
        addNumericField("Landmark_Weight :", this.landmarkWeight, 1);
        addNumericField("Image_Weight :", this.imageWeight, 1);
        addNumericField("Consistency_Weight :", this.consistencyWeight, 1);
        this.consistencyWeightTextField = (TextField) super.getNumericFields().lastElement();
        if (this.mode == MainDialog.MONO_MODE) {
            this.bIsReverse = false;
            this.consistencyWeightTextField.setEnabled(false);
        }
        addNumericField("Stop_Threshold :", this.stopThreshold, 2);
        addCheckbox(" Verbose ", this.richOutput);
        addCheckbox(" Save_Transformations ", this.saveTransformation);
        this.bMacro = Macro.getOptions() != null;
        createSourceImage(bIsReverse);
        createTargetImage();
        loadPointRoiAsLandmarks();
        setSecondaryPointHandlers();
    }

    /**
	 * Set source Mask.
	 *
	 * @param sFileName source mask file name
	 */
    public void setSourceMask(String sFileName) {
        this.sourceMsk.readFile(sFileName);
    }

    /**
	 * Set source intial affine matrix.
	 *
	 * @param affineMatrix initial affine matrix
	 */
    public void setSourceAffineMatrix(double[][] affineMatrix) {
        this.sourceAffineMatrix = affineMatrix;
    }

    /**
	 * Get source Mask.
	 */
    public Mask getSourceMask() {
        return this.sourceMsk;
    }

    /**
	 * Get target Mask.
	 */
    public Mask getTargetMask() {
        return this.targetMsk;
    }

    /**
	 * Actions to be taken during the dialog.
	 */
    public synchronized void actionPerformed(final ActionEvent ae) {
        super.actionPerformed(ae);
        if (wasOKed() || wasCanceled()) notify();
    }

    /**
	 * Actions to be taken when closing the dialog.
	 */
    public synchronized void windowClosing(WindowEvent e) {
        super.windowClosing(e);
        notify();
    }

    /**
	 * Show main bUnwarpJ dialog
	 * 
	 */
    public synchronized void showDialog() {
        super.showDialog();
        if (Macro.getOptions() != null) return;
        try {
            wait();
        } catch (InterruptedException e) {
            IJ.error("Dialog " + getTitle() + " was interrupted.");
        }
    }

    /**
	 * Action to be taken when choices change.
	 * 
	 * @param e item event
	 * 
	 */
    public void itemStateChanged(ItemEvent e) {
        super.itemStateChanged(e);
        Object o = e.getSource();
        if (!(o instanceof Choice)) return;
        Choice originChoice = (Choice) o;
        if (originChoice == this.sourceChoice) {
            final int newChoiceIndex = originChoice.getSelectedIndex();
            if (sourceChoiceIndex != newChoiceIndex) {
                if (targetChoiceIndex != newChoiceIndex) {
                    sourceChoiceIndex = newChoiceIndex;
                    ungrayImage(sourcePh.getPointAction());
                    cancelSource();
                    targetPh.removePoints();
                    targetImp.setRoi(this.previousTargetRoi);
                    createSourceImage(bIsReverse);
                    loadPointRoiAsLandmarks();
                    setSecondaryPointHandlers();
                } else {
                    targetChoiceIndex = sourceChoiceIndex;
                    sourceChoiceIndex = newChoiceIndex;
                    this.targetChoice.select(targetChoiceIndex);
                    permuteImages(bIsReverse);
                }
            }
        } else if (originChoice == this.targetChoice) {
            final int newChoiceIndex = originChoice.getSelectedIndex();
            if (targetChoiceIndex != newChoiceIndex) {
                if (sourceChoiceIndex != newChoiceIndex) {
                    targetChoiceIndex = newChoiceIndex;
                    ungrayImage(targetPh.getPointAction());
                    cancelTarget();
                    sourcePh.removePoints();
                    sourceImp.setRoi(this.previousSourceRoi);
                    createTargetImage();
                    loadPointRoiAsLandmarks();
                    setSecondaryPointHandlers();
                } else {
                    sourceChoiceIndex = targetChoiceIndex;
                    targetChoiceIndex = newChoiceIndex;
                    this.sourceChoice.select(sourceChoiceIndex);
                    permuteImages(bIsReverse);
                }
            }
        } else if (originChoice == this.modeChoice) {
            final int accurate_mode = originChoice.getSelectedIndex();
            if (accurate_mode == MainDialog.MONO_MODE) this.consistencyWeightTextField.setEnabled(false); else this.consistencyWeightTextField.setEnabled(true);
        } else if (originChoice == this.minScaleChoice) {
            final int new_min_scale_deformation = originChoice.getSelectedIndex();
            int new_max_scale_deformation = max_scale_deformation;
            if (max_scale_deformation < new_min_scale_deformation) new_max_scale_deformation = new_min_scale_deformation;
            if (new_min_scale_deformation != min_scale_deformation || new_max_scale_deformation != max_scale_deformation) {
                min_scale_deformation = new_min_scale_deformation;
                max_scale_deformation = new_max_scale_deformation;
                computeImagePyramidDepth();
                restartModelThreads(bIsReverse);
            }
            this.minScaleChoice.select(min_scale_deformation);
            this.maxScaleChoice.select(max_scale_deformation);
        } else if (originChoice == this.maxScaleChoice) {
            final int new_max_scale_deformation = originChoice.getSelectedIndex();
            int new_min_scale_deformation = min_scale_deformation;
            if (new_max_scale_deformation < min_scale_deformation) new_min_scale_deformation = new_max_scale_deformation;
            if (new_max_scale_deformation != max_scale_deformation || new_min_scale_deformation != min_scale_deformation) {
                min_scale_deformation = new_min_scale_deformation;
                max_scale_deformation = new_max_scale_deformation;
                computeImagePyramidDepth();
                restartModelThreads(bIsReverse);
            }
            this.maxScaleChoice.select(max_scale_deformation);
            this.minScaleChoice.select(min_scale_deformation);
        }
    }

    /**
	 * Apply the transformation defined by the spline coefficients to the source
	 * image.
	 *
	 * @param intervals intervals in the deformation
	 * @param cx b-spline X- coefficients
	 * @param cy b-spline Y- coefficients
	 */
    public void applyTransformationToSource(int intervals, double[][] cx, double[][] cy) {
        MiscTools.applyTransformationToSourceMT(this.sourceImp, this.targetImp, this.source, intervals, cx, cy);
        cancelSource();
        this.targetPh.removePoints();
        createSourceImage(false);
        setSecondaryPointHandlers();
    }

    /**
	 * Apply a raw transformation to the source image.
	 *
	 * @param transformation_x X- mapping
	 * @param transformation_y Y- mapping
	 */
    public void applyRawTransformationToSource(double[][] transformation_x, double[][] transformation_y) {
        MiscTools.applyRawTransformationToSource(this.sourceImp, this.targetImp, this.source, transformation_x, transformation_y);
        cancelSource();
        this.targetPh.removePoints();
        createSourceImage(false);
        setSecondaryPointHandlers();
    }

    /**
	 * Free the memory used in the program.
	 */
    public void freeMemory() {
        imageList = null;
        sourceIc = null;
        targetIc = null;
        sourceImp = null;
        targetImp = null;
        source = null;
        target = null;
        sourcePh = null;
        targetPh = null;
        tb = null;
        Runtime.getRuntime().gc();
    }

    /**
	 * Method to color the area of the mask.
	 *
	 * @param ph image point handler
	 */
    public void grayImage(final PointHandler ph) {
        if (ph == sourcePh) {
            final int Xdim = this.source.getWidth();
            final int Ydim = this.source.getHeight();
            double[] source_data = new double[Xdim * Ydim];
            MiscTools.extractImage(this.sourceImp.getProcessor(), source_data);
            final FloatProcessor fp = new FloatProcessor(Xdim, Ydim);
            float[] fp_array = (float[]) fp.getPixels();
            int ij = 0;
            for (int i = 0; i < Ydim; i++) {
                int i_offset = i * Xdim;
                for (int j = 0; j < Xdim; j++, ij++) {
                    if (sourceMsk.getValue(j, i)) fp_array[j + i_offset] = (float) source_data[ij]; else fp_array[j + i_offset] = (float) (0.5 * source_data[ij]);
                }
            }
            fp.resetMinAndMax();
            sourceImp.setProcessor(sourceImp.getTitle(), fp);
            sourceImp.updateAndDraw();
        } else {
            final int Xdim = this.target.getWidth();
            final int Ydim = this.target.getHeight();
            double[] source_data = new double[Xdim * Ydim];
            MiscTools.extractImage(this.targetImp.getProcessor(), source_data);
            final FloatProcessor fp = new FloatProcessor(Xdim, Ydim);
            float[] fp_array = (float[]) fp.getPixels();
            int ij = 0;
            for (int i = 0; i < Ydim; i++) {
                int i_offset = i * Xdim;
                for (int j = 0; j < Xdim; j++, ij++) if (targetMsk.getValue(j, i)) fp_array[j + i_offset] = (float) source_data[ij]; else fp_array[j + i_offset] = (float) (0.5 * source_data[ij]);
            }
            fp.resetMinAndMax();
            targetImp.setProcessor(targetImp.getTitle(), fp);
            targetImp.updateAndDraw();
        }
    }

    /**
	 * Get finalActionLaunched flag.
	 */
    public boolean isFinalActionLaunched() {
        return finalActionLaunched;
    }

    /**
	 * Get clearMask flag.
	 */
    public boolean isClearMaskSet() {
        return clearMask;
    }

    /**
	 * Get saveTransformation flag.
	 */
    public boolean isSaveTransformationSet() {
        return saveTransformation;
    }

    /**
	 * Set saveTransformation flag.
	 */
    public void setSaveTransformation(boolean b) {
        this.saveTransformation = b;
    }

    /**
	 * Get stopRegistration flag.
	 */
    public boolean isStopRegistrationSet() {
        return stopRegistration;
    }

    /**
	 * Join the threads for the source and target images.
	 */
    public void joinThreads() {
        try {
            source.getThread().join();
            target.getThread().join();
        } catch (InterruptedException e) {
            IJ.error("Unexpected interruption exception" + e);
        }
    }

    /**
	 * Restore the initial conditions.
	 */
    public void restoreAll() {
        ungrayImage(sourcePh.getPointAction());
        ungrayImage(targetPh.getPointAction());
        cancelSource();
        cancelTarget();
        tb.restorePreviousToolbar();
        Toolbar.getInstance().repaint();
        ProgressBar.resetProgressBar();
        Runtime.getRuntime().gc();
    }

    /**
	 * Set the clearMask flag.
	 */
    public void setClearMask(boolean val) {
        clearMask = val;
    }

    /**
	 * Set the stopRegistration flag to true.
	 */
    public void setStopRegistration() {
        stopRegistration = true;
    }

    /**
	 * Get the source initial affine matrix.
	 */
    public double[][] getSourceAffineMatrix() {
        return this.sourceAffineMatrix;
    }

    /**
	 * Get the target initial affine matrix.
	 */
    public double[][] getTargetAffineMatrix() {
        return this.targetAffineMatrix;
    }

    /**
	 * Ungray image. It restores the original version of the image (without mask).
	 *
	 * @param pa point action pointer
	 */
    public void ungrayImage(final PointAction pa) {
        if (pa == sourcePh.getPointAction()) {
            if (this.sourceImp != null && this.sourceImp.getProcessor() != null) {
                this.sourceImp.setProcessor(sourceImp.getTitle(), this.originalSourceIP);
                sourceImp.updateImage();
            }
        } else {
            if (this.targetImp != null && this.targetImp.getProcessor() != null) {
                this.targetImp.setProcessor(this.targetImp.getTitle(), this.originalTargetIP);
                targetImp.updateImage();
            }
        }
    }

    /**
	 * Add the image list to the list of choices.
	 *
	 * @param choice list of choices
	 */
    private void addImageList(final Choice choice) {
        for (int k = 0; (k < imageList.length); k++) choice.add(imageList[k].getTitle());
    }

    /**
	 * Close all the variables related to the source image.
	 */
    private void cancelSource() {
        sourcePh.killListeners();
        sourcePh = null;
        sourceIc = null;
        sourceImp.killRoi();
        sourceImp.setRoi(this.previousSourceRoi);
        sourceImp = null;
        source = null;
        sourceMsk = null;
        Runtime.getRuntime().gc();
    }

    /**
	 * Close all the variables related to the target image.
	 */
    private void cancelTarget() {
        targetPh.killListeners();
        targetPh = null;
        targetIc = null;
        targetImp.killRoi();
        targetImp.setRoi(this.previousTargetRoi);
        targetImp = null;
        target = null;
        targetMsk = null;
        Runtime.getRuntime().gc();
    }

    /**
	 * Compute the depth of the image resolution pyramid.
	 */
    private void computeImagePyramidDepth() {
        this.imagePyramidDepth = max_scale_deformation - min_scale_deformation + 1;
    }

    /**
	 * Create the source image.
	 *
	 * @param bIsReverse determines the transformation direction (source-target=TRUE or target-source=FALSE)
	 */
    private void createSourceImage(boolean bIsReverse) {
        if (this.bMacro) {
            String macroOptions = Macro.getOptions();
            Choice thisChoice = this.sourceChoice;
            String item = thisChoice.getSelectedItem();
            item = Macro.getValue(macroOptions, "Source_Image", item);
            for (int i = 0; i < this.imageList.length; i++) if ((this.imageList[i].getTitle()).equals(item)) {
                this.sourceChoiceIndex = i;
                break;
            }
        }
        sourceImp = imageList[sourceChoiceIndex];
        if (this.sourceImp.getStackSize() > 1) this.originalSourceIP = this.sourceImp.getStack().getProcessor(1); else this.originalSourceIP = this.sourceImp.getProcessor();
        source = new BSplineModel(sourceImp.getProcessor(), bIsReverse, (int) Math.pow(2, this.maxImageSubsamplingFactor));
        this.computeImagePyramidDepth();
        source.setPyramidDepth(imagePyramidDepth + min_scale_image);
        sourceIc = sourceImp.getWindow().getCanvas();
        if (sourceImp.getStackSize() == 1) {
            sourceMsk = new Mask(sourceImp.getProcessor(), false);
        } else {
            sourceMsk = new Mask(sourceImp.getStack().getProcessor(2), true);
        }
        sourcePh = new PointHandler(sourceImp, tb, sourceMsk, this);
        tb.setSource(sourceImp, sourcePh);
    }

    /**
	 * Create target image.
	 */
    private void createTargetImage() {
        if (this.bMacro) {
            String macroOptions = Macro.getOptions();
            Choice thisChoice = this.targetChoice;
            String item = thisChoice.getSelectedItem();
            item = Macro.getValue(macroOptions, "Target_Image", item);
            for (int i = 0; i < this.imageList.length; i++) if ((this.imageList[i].getTitle()).equals(item)) {
                this.targetChoiceIndex = i;
                break;
            }
        }
        targetImp = imageList[targetChoiceIndex];
        if (this.targetImp.getStackSize() > 1) this.originalTargetIP = this.targetImp.getStack().getProcessor(1); else this.originalTargetIP = this.targetImp.getProcessor();
        target = new BSplineModel(targetImp.getProcessor(), true, (int) Math.pow(2, this.maxImageSubsamplingFactor));
        this.computeImagePyramidDepth();
        target.setPyramidDepth(imagePyramidDepth + min_scale_image);
        targetIc = targetImp.getWindow().getCanvas();
        if (targetImp.getStackSize() == 1) {
            targetMsk = new Mask(targetImp.getProcessor(), false);
        } else {
            targetMsk = new Mask(targetImp.getStack().getProcessor(2), true);
        }
        targetPh = new PointHandler(targetImp, tb, targetMsk, this);
        tb.setTarget(targetImp, targetPh);
    }

    /**
	 * Load point rois in the source and target images as landmarks.
	 */
    private void loadPointRoiAsLandmarks() {
        Roi roiSource = this.previousSourceRoi = sourceImp.getRoi();
        Roi roiTarget = this.previousTargetRoi = targetImp.getRoi();
        if (roiSource instanceof PointRoi && roiTarget instanceof PointRoi) {
            PointRoi prSource = (PointRoi) roiSource;
            int[] xSource = prSource.getXCoordinates();
            PointRoi prTarget = (PointRoi) roiTarget;
            int[] xTarget = prTarget.getXCoordinates();
            int numOfPoints = xSource.length;
            if (numOfPoints != xTarget.length) return;
            int[] ySource = prSource.getYCoordinates();
            int[] yTarget = prTarget.getYCoordinates();
            Rectangle recSource = prSource.getBounds();
            int originXSource = recSource.x;
            int originYSource = recSource.y;
            Rectangle recTarget = prTarget.getBounds();
            int originXTarget = recTarget.x;
            int originYTarget = recTarget.y;
            for (int i = 0; i < numOfPoints; i++) {
                sourcePh.addPoint(xSource[i] + originXSource, ySource[i] + originYSource);
                targetPh.addPoint(xTarget[i] + originXTarget, yTarget[i] + originYTarget);
            }
            sourceImp.setRoi(sourcePh);
            targetImp.setRoi(targetPh);
        }
    }

    /**
	 * Permute the pointer for the target and source images.
	 *
	 * @param bIsReverse determines the transformation direction (source-target=TRUE or target-source=FALSE)
	 */
    private void permuteImages(boolean bIsReverse) {
        final ImageCanvas swapIc = this.sourceIc;
        this.sourceIc = this.targetIc;
        this.targetIc = swapIc;
        final ImagePlus swapImp = this.sourceImp;
        this.sourceImp = this.targetImp;
        this.targetImp = swapImp;
        final ImageProcessor swapIP = this.originalSourceIP;
        this.originalSourceIP = this.originalTargetIP;
        this.originalTargetIP = swapIP;
        final Mask swapMsk = this.sourceMsk;
        this.sourceMsk = this.targetMsk;
        this.targetMsk = swapMsk;
        final PointHandler swapPh = this.sourcePh;
        this.sourcePh = this.targetPh;
        this.targetPh = swapPh;
        setSecondaryPointHandlers();
        double[][] swapMatrix = null;
        Roi swapRoi = this.previousSourceRoi;
        this.previousSourceRoi = this.previousTargetRoi;
        this.previousTargetRoi = swapRoi;
        if (this.sourceAffineMatrix != null) swapMatrix = new double[2][3];
        for (int i = 0; i < 2; i++) for (int j = 0; j < 3; j++) {
            if (this.sourceAffineMatrix != null) swapMatrix[i][j] = this.sourceAffineMatrix[i][j];
            if (this.targetAffineMatrix != null) this.sourceAffineMatrix[i][j] = this.targetAffineMatrix[i][j];
        }
        if (swapMatrix != null) {
            if (this.targetAffineMatrix == null) this.targetAffineMatrix = new double[2][3];
            for (int i = 0; i < 2; i++) for (int j = 0; j < 3; j++) this.targetAffineMatrix[i][j] = swapMatrix[i][j];
        }
        tb.setSource(this.sourceImp, this.sourcePh);
        tb.setTarget(this.targetImp, this.targetPh);
        this.source = new BSplineModel(this.sourceImp.getProcessor(), bIsReverse, (int) Math.pow(2, this.maxImageSubsamplingFactor));
        this.source.setPyramidDepth(imagePyramidDepth + min_scale_image);
        this.target = new BSplineModel(this.targetImp.getProcessor(), true, (int) Math.pow(2, this.maxImageSubsamplingFactor));
        this.target.setPyramidDepth(imagePyramidDepth + min_scale_image);
    }

    /**
	 * Remove the points from the points handlers of the source and target image.
	 */
    private void removePoints() {
        sourcePh.removePoints();
        targetPh.removePoints();
    }

    /**
	 * Re-launch the threads for the image models of the source and target.
	 *
	 * @param bIsReverse boolean variable to indicate the use of consistency
	 */
    private void restartModelThreads(boolean bIsReverse) {
        stopSourceThread();
        stopTargetThread();
        source = null;
        target = null;
        Runtime.getRuntime().gc();
        source = new BSplineModel(sourceImp.getProcessor(), bIsReverse, (int) Math.pow(2, this.maxImageSubsamplingFactor));
        source.setPyramidDepth(imagePyramidDepth + min_scale_image);
        target = new BSplineModel(targetImp.getProcessor(), true, (int) Math.pow(2, this.maxImageSubsamplingFactor));
        target.setPyramidDepth(imagePyramidDepth + min_scale_image);
    }

    /**
	 * Set the secondary point handlers.
	 */
    private void setSecondaryPointHandlers() {
        sourcePh.setSecondaryPointHandler(targetImp, targetPh);
        targetPh.setSecondaryPointHandler(sourceImp, sourcePh);
    }

    /**
	 * Stop the thread of the source image.
	 */
    private void stopSourceThread() {
        if (source.getThread() == null) return;
        while (source.getThread().isAlive()) {
            source.getThread().interrupt();
        }
    }

    /**
	 * Stop the thread of the target image.
	 */
    private void stopTargetThread() {
        if (target.getThread() == null) return;
        while (target.getThread().isAlive()) {
            target.getThread().interrupt();
        }
    }

    /**
	 * Get source point handler.
	 */
    public PointHandler getSourcePh() {
        return this.sourcePh;
    }

    /**
	 * Get target point handler.
	 */
    public PointHandler getTargetPh() {
        return this.targetPh;
    }

    /**
	 * Get source point handler.
	 */
    public Mask getSourceMsk() {
        return this.sourceMsk;
    }

    /**
	 * Get target point handler.
	 */
    public Mask getTargetMsk() {
        return this.targetMsk;
    }

    /**
	 * Set final action launched flag.
	 */
    public void setFinalActionLaunched(boolean b) {
        this.finalActionLaunched = b;
    }

    /**
	 * Set toolbar tools all up.
	 */
    public void setToolbarAllUp() {
        tb.setAllUp();
    }

    /**
	 * Repaint toolbar.
	 */
    public void repaintToolbar() {
        tb.repaint();
    }

    /**
	 * Get target image model.
	 */
    public BSplineModel getTarget() {
        return this.target;
    }

    /**
	 * Get original source image process.
	 */
    public ImageProcessor getOriginalSourceIP() {
        return this.originalSourceIP;
    }

    /**
	 * Get original target image process.
	 */
    public ImageProcessor getOriginalTargetIP() {
        return this.originalTargetIP;
    }

    /**
	 * Get source image model.
	 */
    public BSplineModel getSource() {
        return this.source;
    }

    /**
	 * Get the macro flag
	 * 
	 * @return macro flag
	 */
    public boolean isMacroCall() {
        return bMacro;
    }
}
