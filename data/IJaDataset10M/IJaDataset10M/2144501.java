package org.reprap.scanning.GUI;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.File;
import javax.swing.*;
import java.awt.EventQueue;
import java.util.ArrayList;
import Jama.Matrix;
import org.jdesktop.layout.GroupLayout;
import org.jdesktop.layout.GroupLayout.ParallelGroup;
import org.jdesktop.layout.LayoutStyle;
import javax.swing.filechooser.*;
import org.reprap.scanning.FeatureExtraction.*;
import org.reprap.scanning.FileIO.*;
import org.reprap.scanning.FileIO.MainPreferences.Papersize;
import org.reprap.scanning.FunctionLibraries.ImageFile;
import org.reprap.scanning.FunctionLibraries.MatrixManipulations;
import org.reprap.scanning.FunctionLibraries.STLFile;
import org.reprap.scanning.FunctionLibraries.TexturePatch;
import org.reprap.scanning.Geometry.*;
import org.reprap.scanning.Calibration.*;
import org.reprap.scanning.DataStructures.*;

public class Main extends JFrame {

    private static final double tau = Math.PI * 2;

    private boolean save = true;

    private static final long serialVersionUID = 1L;

    private double calibrationsheetwidth, calibrationsheetheight;

    private double calibrationsheetxlength, calibrationsheetylength;

    private int calibrationsheetpixelswidth, calibrationsheetpixelsheight;

    private Ellipse[] calibrationcircles;

    private Ellipse[] imageellipses;

    private Point2d[] calibrationcirclecenters;

    private Point3d[] surfacepoints;

    private Triangle3D[] surfacetriangles;

    private MainPreferences prefs;

    private JProgressBar jProgressBar1, jProgressBar2;

    private JLabel jLabelTitle, jLabelProgressBar1, jLabelProgressBar2, jLabelOutputLog;

    private JTextArea jTextAreaOutput;

    private JScrollPane jScrollPanel;

    private Image[] images;

    private JButton JButtonNext;

    private JButton JButtonCancel;

    private JButton JButtonPrevious;

    private Ellipse standardcalibrationcircleonprintedsheet;

    static int gap = 18;

    private JList filelist;

    private JComboBox Papersize;

    private JLabel widthlabel, heightlabel;

    private static final int calibrationsheetinterrogationnumberofsteps = 1;

    private static final int calibrationnumberofsubsteps = 5;

    private static final int objectvoxelisationnumberofsteps = 2;

    private static final int numberofotherprogressbarsteps = 2;

    public static enum steps {

        calibrationsheet, fileio, calibrationsheetinterrogation, calibration, objectfindingvoxelisation, texturematching, writetofile, end
    }

    ;

    private static final steps stepsarray[] = steps.values();

    private static steps laststep = steps.valueOf("end");

    private static steps firststep = steps.valueOf("calibrationsheet");

    private steps currentstep = firststep;

    public static void main(String[] args) {
        if (args.length == 0) {
            Thread.currentThread().setName("Main");
            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    try {
                        Thread.currentThread().setName("RepRap Scanning");
                        new Main().setStep(firststep);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error in the main GUI: " + ex);
                        ex.printStackTrace();
                    }
                }
            });
        } else {
            final String filename = args[0];
            Thread.currentThread().setName("Main");
            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    try {
                        Thread.currentThread().setName("RepRap Scanning");
                        new Main(filename).setStep(firststep);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(null, "Error in the main GUI: " + ex);
                        ex.printStackTrace();
                    }
                }
            });
        }
    }

    public Main() {
        prefs = new MainPreferences(stepsarray.length);
        initComponents();
    }

    public Main(String filename) {
        if (filename != "") System.out.println("Using non-standard properties file " + filename);
        prefs = new MainPreferences(stepsarray.length, filename);
        initComponents();
    }

    public void setStep(steps stepnumber) {
        currentstep = stepnumber;
        JButtonCancel.setText("Cancel");
        JButtonPrevious.setText("Previous");
        boolean first = true;
        for (int i = 0; i < (currentstep.ordinal()); i++) if (prefs.SkipStep[i] == false) first = false;
        JButtonPrevious.setEnabled(!first);
        boolean finish = true;
        for (int i = (currentstep.ordinal() + 2); i < prefs.SkipStep.length; i++) if (prefs.SkipStep[i] == false) finish = false;
        if (finish) JButtonNext.setText("Finish"); else JButtonNext.setText("Next");
        if (!(stepnumber.equals(firststep) && (prefs.SkipStep[stepnumber.ordinal()]))) currentstep = stepnumber;
        if (!JButtonPrevious.isEnabled() && !firststep.equals(stepnumber)) firststep = stepnumber;
        String Title = "Reprap 3D Scanning from photos - Step " + (currentstep.ordinal() + 1) + " of " + laststep.ordinal();
        setTitle(Title);
        try {
            switch(currentstep) {
                case calibrationsheet:
                    if ((prefs.SkipStep[currentstep.ordinal()]) && (prefs.calibrationpatterns.getSize() != 0)) setStep(stepsarray[currentstep.ordinal() + 1]); else ChooseCalibrationSheet();
                    break;
                case fileio:
                    if ((prefs.SkipStep[currentstep.ordinal()]) && (prefs.imagefiles.getSize() != 0)) setStep(stepsarray[currentstep.ordinal() + 1]); else ChooseImagesAndOutputFile();
                    break;
                case calibrationsheetinterrogation:
                    FindCalibrationSheetCirclesEtc();
                    break;
                case calibration:
                    Calibration();
                    break;
                case objectfindingvoxelisation:
                    FindCoarseVoxelisedObject();
                    break;
                case texturematching:
                    MarchingTexturePatchMatching();
                    break;
                case writetofile:
                    OutputSTLFile(prefs.OutputFileName.getText(), surfacetriangles, surfacepoints, prefs.OutputObjectName.getText(), true);
                    break;
                case end:
                    end();
                    break;
            }
        } catch (Exception e) {
            System.out.println("Error in step " + (currentstep.ordinal() + 1));
            System.out.println(e);
        }
    }

    public void MarchingTexturePatchMatching() {
        JProgressBar bar = new JProgressBar(0, 1);
        bar.setValue(0);
        try {
            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    jProgressBar2.setValue(jProgressBar2.getValue() + 1);
                    jLabelProgressBar1.setText("Estimating Surface Triangles");
                }
            });
        } catch (Exception e) {
            System.out.println("Exception in updating the progress bar" + e.getMessage());
        }
        TexturePatch.minaveragesineofanglebetweenplaneandcamera = Math.sin((prefs.AlgorithmSettingTextureMatchingMinimumAverageAngleBetweenCameraAndPlaneInDegrees / (double) 360) * tau);
        TexturePatch.minimumsimilarityrange = prefs.AlgorithmSettingTextureMatchingMinimumSimilarityRange;
        TexturePatch.triangularnumberofsamplepoints = prefs.AlgorithmSettingTextureMatchingNthTriangularNumberOfSamples;
        TexturePatch.anglestep = ((double) prefs.AlgorithmSettingTextureMatchingAngleAccuracyInDegrees / (double) 360) * tau;
        TexturePatch.minimumvalidabsolutevalueofsecondderivative = prefs.AlgorithmSettingTextureMatchingMinimumAbsoluteSecondDerivative;
        TexturePatch.minimumvalidmaxsimilarity = prefs.AlgorithmSettingTextureMatchingMinimumValidMaxSimilarity;
        TexturePatch.maxoverlap = prefs.AlgorithmSettingTextureMatchingMaxOverlapForSnapToFit;
        TexturePatch.debug = prefs.DebugIndividualTextureMatch;
        String filename = new File(MainPreferences.path).getParent() + File.separatorChar + "InitialLineSegmentForTextureMatching.properties";
        InitialLineSegmentForTextureMatching textureprefs = new InitialLineSegmentForTextureMatching(filename);
        boolean valid = !prefs.AlgorithmSettingTextureMatchingManualSelectionOfInitialLineSegment;
        if (valid) try {
            valid = false;
            textureprefs.loadProperties();
            valid = textureprefs.referenceimage != -1;
            if (valid) valid = (textureprefs.referenceimage >= 0) && (textureprefs.referenceimage < images.length);
            if (valid) valid = !images[textureprefs.referenceimage].skipprocessing;
            if (!valid) System.out.println("Pre-selected initial line segment reference image is not valid, initiating manual selection");
        } catch (Exception e) {
            System.out.println("Error reading pre-selected initial line segment from file, initiating manual selection");
        }
        if (!valid) {
            boolean exit = false;
            textureprefs.referenceimage = -1;
            while (!exit) {
                textureprefs.referenceimage++;
                if (textureprefs.referenceimage >= images.length) textureprefs.referenceimage = 0;
                if (!images[textureprefs.referenceimage].skipprocessing) {
                    GraphicsFeedback graphics = new GraphicsFeedback();
                    graphics.ShowImage(images[textureprefs.referenceimage]);
                    boolean[][] processedpixels = images[textureprefs.referenceimage].getProcessedPixels();
                    PixelColour black = new PixelColour(PixelColour.StandardColours.Black);
                    for (int x = 0; x < images[textureprefs.referenceimage].width; x++) for (int y = 0; y < images[textureprefs.referenceimage].height; y++) if (processedpixels[x][y]) graphics.Print(x, y, black, 0, 0);
                    textureprefs.initialline = graphics.ManualSelectionOfLineSegment(new PixelColour(PixelColour.StandardColours.Red), new PixelColour(PixelColour.StandardColours.Green));
                    exit = (textureprefs.initialline.CalculateLengthSquared() != 0);
                }
            }
            try {
                textureprefs.saveProperties();
            } catch (Exception e) {
                System.out.println("Error writing initial line segment properties.");
            }
        }
        Matrix P = images[textureprefs.referenceimage].getWorldtoImageTransformMatrix();
        Matrix H = new Matrix(3, 3);
        H.set(0, 0, P.get(0, 0));
        H.set(0, 1, P.get(0, 1));
        H.set(0, 2, P.get(0, 3));
        H.set(1, 0, P.get(1, 0));
        H.set(1, 1, P.get(1, 1));
        H.set(1, 2, P.get(1, 3));
        H.set(2, 0, P.get(2, 0));
        H.set(2, 1, P.get(2, 1));
        H.set(2, 2, P.get(2, 3));
        Point2d a2d = new Point2d(MatrixManipulations.PseudoInverse(H).times(textureprefs.initialline.start.minusEquals(images[textureprefs.referenceimage].originofimagecoordinates).ConvertPointTo3x1Matrix()));
        Point2d b2d = new Point2d(MatrixManipulations.PseudoInverse(H).times(textureprefs.initialline.end.minusEquals(images[textureprefs.referenceimage].originofimagecoordinates).ConvertPointTo3x1Matrix()));
        Point3d a = new Point3d(a2d, 0);
        Point3d b = new Point3d(b2d, 0);
        Point3d[] initialpoints = new Point3d[2];
        initialpoints[0] = a.clone();
        initialpoints[1] = b.clone();
        TrianglePlusVertexArray tripatches = new TrianglePlusVertexArray(initialpoints, new Triangle3D[0]);
        double length = Math.sqrt(a2d.CalculateDistanceSquared(b2d));
        double angle = a2d.GetAngleMeasuredAntiClockwiseFromPositiveX(b2d);
        Point3d[] c = new Point3d[2];
        c[0] = new Point3d(a2d.GetOtherPoint(angle + (((double) 60 / (double) 360) * tau), length), 0);
        c[1] = new Point3d(a2d.GetOtherPoint(angle - (((double) 60 / (double) 360) * tau), length), 0);
        if ((prefs.Debug) && (prefs.DebugMarchingTextureMatch)) {
            GraphicsFeedback graphics = new GraphicsFeedback(true);
            graphics.ShowImage(images[textureprefs.referenceimage]);
            Line3d[] lines = new Line3d[4];
            lines[0] = new Line3d(a, c[0]);
            lines[1] = new Line3d(b, c[0]);
            lines[2] = new Line3d(a, c[1]);
            lines[3] = new Line3d(b, c[1]);
            graphics.PrintLineSegment(textureprefs.initialline, new PixelColour(PixelColour.StandardColours.Red));
            for (int j = 0; j < 2; j++) graphics.PrintLineSegment(lines[j], new PixelColour(PixelColour.StandardColours.Green), images[textureprefs.referenceimage]);
            for (int j = 2; j < 4; j++) graphics.PrintLineSegment(lines[j], new PixelColour(PixelColour.StandardColours.Gray), images[textureprefs.referenceimage]);
            filename = prefs.DebugSaveOutputImagesFolder + File.separatorChar + "MarchingTextureMatchStartingPosition.jpg";
            graphics.SaveImage(filename);
        }
        double minangle = 0;
        double maxangle = tau * 0.25;
        boolean[] originalskip = new boolean[images.length];
        Plane dividingplane = new Plane(a, b, new Point3d(a2d, 1));
        boolean c0halfspace = dividingplane.GetHalfspace(c[0]);
        for (int i = 0; i < images.length; i++) {
            originalskip[i] = images[i].skipprocessing;
            if (!originalskip[i]) {
                Point3d C = new Point3d(MatrixManipulations.GetRightNullSpace(images[i].getWorldtoImageTransformMatrix()));
                images[i].skipprocessing = (dividingplane.GetHalfspace(C) == c0halfspace);
            }
        }
        for (int i = 0; i < 2; i++) {
            if (i == 1) {
                for (int j = 0; j < images.length; j++) if (!originalskip[j]) images[j].skipprocessing = !images[j].skipprocessing;
                Point3d swap = a.clone();
                a = b.clone();
                b = swap.clone();
            }
            Point3d normal = new Point3d(0, 0, 1);
            try {
                TexturePatch.SimilarityMeasure candnormal = TexturePatch.FindNewCandNormal(minangle, maxangle, c[i], a, b, normal, images);
                if (!candnormal.isBlank()) {
                    Point3d newc = candnormal.c.clone();
                    normal = candnormal.normal.clone();
                    for (int k = 0; k < candnormal.skipimage.length; k++) {
                        if (!candnormal.skipimage[k]) {
                            Point2d[] vertex = new Point2d[3];
                            vertex[0] = images[k].getWorldtoImageTransform(a.ConvertPointTo4x1Matrix());
                            vertex[1] = images[k].getWorldtoImageTransform(b.ConvertPointTo4x1Matrix());
                            vertex[2] = images[k].getWorldtoImageTransform(newc.ConvertPointTo4x1Matrix());
                            BoundingPolygon2D polygon = new BoundingPolygon2D(vertex);
                            images[k].setProcessedPixelsForPolygon(polygon);
                        }
                    }
                    int cindex = tripatches.AddVertex(newc);
                    tripatches.AddTriangle(new Triangle3D(0, 1, cindex, tripatches.GetVertexArray(), normal));
                }
            } catch (TexturePatch.TexturePatchException e) {
            }
        }
        for (int i = 0; i < originalskip.length; i++) images[i].skipprocessing = originalskip[i];
        double doubleinitialarea = 0;
        int i = 0;
        while (i < tripatches.GetTriangleArrayLength()) {
            if (i == 0) doubleinitialarea = tripatches.GetTriangleArray()[0].getArea(tripatches.GetVertexArray()) * 2;
            if ((prefs.Debug) && (prefs.DebugMarchingTextureMatch)) {
                filename = prefs.DebugSaveOutputImagesFolder + File.separatorChar + "MarchingTextureMatch" + Integer.toString(i) + "a.jpg";
                OutputDebugTriangleOverlayImage(tripatches, i, images[textureprefs.referenceimage], filename);
            }
            tripatches.ExpandTexturePatch(i, images, prefs.AlgorithmSettingTextureMatchingMaxDistanceToSnapToTriangleVertex, doubleinitialarea);
            System.out.print((double) ((int) (((double) i / (double) tripatches.GetTriangleArrayLength()) * 10000)) / 100 + "%");
            System.out.println("    count: " + i + " of " + tripatches.GetTriangleArrayLength() + " triangles with a total of " + tripatches.GetVertexArrayLength() + " vertices");
            bar.setMaximum(tripatches.GetTriangleArrayLength());
            bar.setValue(i);
            final JProgressBar temp = bar;
            try {
                EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        jProgressBar1.setMinimum(temp.getMinimum());
                        jProgressBar1.setMaximum(temp.getMaximum());
                        jProgressBar1.setValue(temp.getValue());
                    }
                });
            } catch (Exception e) {
                System.out.println("Exception in updating the progress bar" + e.getMessage());
            }
            if ((prefs.Debug) && (prefs.DebugMarchingTextureMatch)) {
                filename = prefs.DebugSaveOutputImagesFolder + File.separatorChar + "MarchingTextureMatch" + Integer.toString(i) + "b.jpg";
                OutputDebugTriangleOverlayImage(tripatches, i, images[textureprefs.referenceimage], filename);
            }
            i++;
        }
        if (tripatches.GetTriangleArrayLength() == 0) {
            System.out.println("Initial seed triangle creation failed");
        }
        surfacepoints = tripatches.GetVertexArray();
        surfacetriangles = tripatches.GetTriangleArray();
        final String text = "Object bounding volume surface estimated using " + surfacetriangles.length + " triangles.\n";
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                jTextAreaOutput.setText(jTextAreaOutput.getText() + text);
            }
        });
        setStep(stepsarray[currentstep.ordinal() + 1]);
    }

    private void ChooseCalibrationSheet() {
        jLabelTitle.setText("Selection of Calibration Pattern");
        JButton jButtonBrowse;
        JComboBox jComboBox;
        JLabel printinglabel = new JLabel();
        widthlabel = new JLabel();
        heightlabel = new JLabel();
        JLabel horizontalmarginlabel = new JLabel();
        JLabel verticalmarginlabel = new JLabel();
        printinglabel.setText("Printed Sheet Dimensions");
        widthlabel.setText("Custom Width (mm)");
        heightlabel.setText("Custom Height (mm)");
        horizontalmarginlabel.setText("Horizontal Margin (mm)");
        verticalmarginlabel.setText("Vertical Margin (mm)");
        String[] papernames = new String[prefs.PaperSizeList.getSize()];
        for (int i = 0; i < papernames.length; i++) {
            Papersize current = (Papersize) prefs.PaperSizeList.getElementAt(i);
            papernames[i] = current.Name + ", " + current.width + "mm x " + current.height + "mm";
        }
        Papersize = new JComboBox(papernames);
        if (Papersize.getItemCount() > 0) Papersize.setSelectedIndex(prefs.CurrentPaperSizeIndexNumber);
        jButtonBrowse = new JButton();
        jButtonBrowse.setText("Browse");
        jComboBox = new JComboBox(prefs.calibrationpatterns);
        if (jComboBox.getItemCount() > 0) jComboBox.setSelectedIndex(prefs.CurrentCalibrationPatternIndexNumber);
        Papersize.setEnabled(!prefs.PaperSizeIsCustom.isSelected());
        prefs.PaperCustomSizeHeightmm.setEnabled(prefs.PaperSizeIsCustom.isSelected());
        prefs.PaperCustomSizeWidthmm.setEnabled(prefs.PaperSizeIsCustom.isSelected());
        widthlabel.setEnabled(prefs.PaperSizeIsCustom.isSelected());
        heightlabel.setEnabled(prefs.PaperSizeIsCustom.isSelected());
        JButtonNext.setEnabled(prefs.calibrationpatterns.getSize() != 0);
        getContentPane().removeAll();
        GroupLayout thislayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(thislayout);
        thislayout.setHorizontalGroup(thislayout.createParallelGroup(GroupLayout.LEADING).add(defaulthorizontal(thislayout)).add(thislayout.createSequentialGroup().addContainerGap().add(thislayout.createParallelGroup(GroupLayout.LEADING).add(thislayout.createSequentialGroup().add(jComboBox, GroupLayout.PREFERRED_SIZE, 500, 500).addPreferredGap(LayoutStyle.RELATED).add(jButtonBrowse).addContainerGap(gap, gap)).add(printinglabel).add(Papersize, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).add(thislayout.createSequentialGroup().addPreferredGap(LayoutStyle.UNRELATED, gap, gap).add(thislayout.createParallelGroup(GroupLayout.LEADING).add(prefs.CalibrationSheetKeepAspectRatioWhenPrinted).add(prefs.PaperSizeIsCustom)).addPreferredGap(LayoutStyle.UNRELATED, gap, gap).add(thislayout.createParallelGroup(GroupLayout.LEADING).add(prefs.PaperOrientationIsPortrait).add(prefs.PaperOrientationIsLandscape))).add(thislayout.createSequentialGroup().add(widthlabel).addPreferredGap(LayoutStyle.UNRELATED, gap, gap).add(prefs.PaperCustomSizeWidthmm, gap * 3, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.UNRELATED, gap, gap).add(horizontalmarginlabel).addPreferredGap(LayoutStyle.UNRELATED, gap, gap).add(prefs.PaperMarginHorizontalmm, gap * 3, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).add(thislayout.createSequentialGroup().add(heightlabel).addPreferredGap(LayoutStyle.UNRELATED, gap, gap).add(prefs.PaperCustomSizeHeightmm, gap * 3, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).addPreferredGap(LayoutStyle.UNRELATED, gap, gap).add(verticalmarginlabel).addPreferredGap(LayoutStyle.UNRELATED, gap, gap).add(prefs.PaperMarginVerticalmm, gap * 3, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))).addContainerGap(gap, gap)));
        thislayout.setVerticalGroup(thislayout.createParallelGroup(GroupLayout.LEADING).add(defaultvertical(thislayout)).add(thislayout.createSequentialGroup().addPreferredGap(LayoutStyle.RELATED, 3 * gap, 3 * gap).add(thislayout.createParallelGroup(GroupLayout.BASELINE).add(jComboBox).add(jButtonBrowse)).addPreferredGap(LayoutStyle.RELATED).add(printinglabel).addPreferredGap(LayoutStyle.RELATED).add(Papersize, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).add(thislayout.createParallelGroup(GroupLayout.BASELINE).add(prefs.CalibrationSheetKeepAspectRatioWhenPrinted).add(prefs.PaperOrientationIsPortrait)).addPreferredGap(LayoutStyle.RELATED).add(thislayout.createParallelGroup(GroupLayout.BASELINE).add(prefs.PaperSizeIsCustom).add(prefs.PaperOrientationIsLandscape)).addPreferredGap(LayoutStyle.RELATED).add(thislayout.createParallelGroup(GroupLayout.BASELINE).add(widthlabel).add(prefs.PaperCustomSizeWidthmm).add(horizontalmarginlabel).add(prefs.PaperMarginHorizontalmm)).addPreferredGap(LayoutStyle.RELATED).add(thislayout.createParallelGroup(GroupLayout.BASELINE).add(heightlabel).add(prefs.PaperCustomSizeHeightmm).add(verticalmarginlabel).add(prefs.PaperMarginVerticalmm))));
        jComboBox.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                JComboBox cb = (JComboBox) evt.getSource();
                try {
                    prefs.CurrentCalibrationPatternIndexNumber = cb.getSelectedIndex();
                } catch (Exception e) {
                    System.out.println("Error updating Calibration selection combo box");
                    System.out.println(e);
                }
            }
        });
        jButtonBrowse.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                int index = 0;
                JFileChooser Chooser = new JFileChooser();
                String[] imagefile = new String[] { "jpg", "JPG", "jpeg", "JPEG" };
                FileFilter filter = new FileExtensionFilter(imagefile, "JPEG image file (*.jpg, *.jpeg)");
                Chooser.setFileFilter(filter);
                Chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if (JFileChooser.APPROVE_OPTION == Chooser.showDialog(null, "Select Calibration Sheet image file")) {
                    File file = Chooser.getSelectedFile();
                    if (ImageFile.IsInvalid(file.toString())) JOptionPane.showMessageDialog(getContentPane(), "Selected file, " + file.toString() + ", does not seem to be an image file"); else {
                        index = prefs.calibrationpatterns.getSize();
                        prefs.calibrationpatterns.addElement(file.toString());
                        prefs.CurrentCalibrationPatternIndexNumber = index;
                        prefs.calibrationpatterns.setSelectedItem(file.toString());
                    }
                }
                JButtonNext.setEnabled(prefs.calibrationpatterns.getSize() != 0);
            }
        });
        prefs.PaperSizeIsCustom.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                Papersize.setEnabled(!prefs.PaperSizeIsCustom.isSelected());
                prefs.PaperCustomSizeHeightmm.setEnabled(prefs.PaperSizeIsCustom.isSelected());
                prefs.PaperCustomSizeWidthmm.setEnabled(prefs.PaperSizeIsCustom.isSelected());
                widthlabel.setEnabled(prefs.PaperSizeIsCustom.isSelected());
                heightlabel.setEnabled(prefs.PaperSizeIsCustom.isSelected());
                prefs.SanityCheckMargins();
            }
        });
        Papersize.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                JComboBox cb = (JComboBox) evt.getSource();
                try {
                    prefs.CurrentPaperSizeIndexNumber = cb.getSelectedIndex();
                    prefs.PaperSizeList.setSelectedItem(prefs.PaperSizeList.getElementAt(prefs.CurrentPaperSizeIndexNumber));
                    prefs.SanityCheckMargins();
                } catch (Exception e) {
                    System.out.println("Error updating Paper size combo box");
                    System.out.println(e);
                }
            }
        });
        prefs.PaperMarginVerticalmm.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent e) {
                prefs.SanityCheckMargins();
            }
        });
        prefs.PaperMarginHorizontalmm.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent e) {
                prefs.SanityCheckMargins();
            }
        });
        prefs.PaperCustomSizeHeightmm.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent e) {
                prefs.SanityCheckMargins();
            }
        });
        prefs.PaperCustomSizeWidthmm.addFocusListener(new java.awt.event.FocusAdapter() {

            public void focusLost(java.awt.event.FocusEvent e) {
                prefs.SanityCheckMargins();
            }
        });
    }

    private void ChooseImagesAndOutputFile() {
        jLabelTitle.setText("Selection of Images");
        JButton jButtonAdd = new JButton();
        JButton jButtonRemove = new JButton();
        jButtonAdd.setText("Add");
        jButtonRemove.setText("Remove");
        JLabel jLabel2 = new javax.swing.JLabel();
        jLabel2.setText("Selection of output file");
        jLabel2.setFont(new java.awt.Font("DejaVu Sans", java.awt.Font.BOLD, 13));
        JLabel jLabel3 = new javax.swing.JLabel();
        jLabel3.setText("Optional Internal Object Name");
        jLabel3.setFont(new java.awt.Font("DejaVu Sans", java.awt.Font.PLAIN, 13));
        JButton jButtonBrowse = new javax.swing.JButton();
        jButtonBrowse.setText("Browse");
        JButtonNext.setEnabled((prefs.imagefiles.getSize() != 0) && (prefs.OutputFileName.getText().length() != 0));
        filelist = new JList(prefs.imagefiles);
        filelist.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        JScrollPane jScrollPanel = new JScrollPane();
        jScrollPanel.setViewportView(filelist);
        getContentPane().removeAll();
        GroupLayout thislayout = new GroupLayout(getContentPane());
        getContentPane().setLayout(thislayout);
        thislayout.setHorizontalGroup(thislayout.createParallelGroup(GroupLayout.LEADING).add(defaulthorizontal(thislayout)).add(thislayout.createSequentialGroup().addContainerGap().add(thislayout.createParallelGroup(GroupLayout.LEADING).add(jScrollPanel).add(jLabel2).add(prefs.OutputFileName).add(thislayout.createSequentialGroup().add(jLabel3).addPreferredGap(LayoutStyle.RELATED).add(prefs.OutputObjectName))).addPreferredGap(LayoutStyle.RELATED).add(thislayout.createParallelGroup(GroupLayout.LEADING).add(jButtonAdd, GroupLayout.PREFERRED_SIZE, 5 * gap, GroupLayout.PREFERRED_SIZE).add(jButtonRemove, GroupLayout.PREFERRED_SIZE, 5 * gap, GroupLayout.PREFERRED_SIZE).add(jButtonBrowse, GroupLayout.PREFERRED_SIZE, 5 * gap, GroupLayout.PREFERRED_SIZE)).addContainerGap(gap, gap)));
        thislayout.setVerticalGroup(thislayout.createParallelGroup(GroupLayout.LEADING).add(defaultvertical(thislayout)).add(thislayout.createSequentialGroup().addPreferredGap(LayoutStyle.RELATED, 3 * gap, 3 * gap).add(thislayout.createParallelGroup(GroupLayout.LEADING).add(jScrollPanel, GroupLayout.DEFAULT_SIZE, 10 * gap, GroupLayout.PREFERRED_SIZE).add(thislayout.createSequentialGroup().add(jButtonAdd).addPreferredGap(LayoutStyle.RELATED).add(jButtonRemove))).addPreferredGap(LayoutStyle.UNRELATED, gap, gap).add(thislayout.createSequentialGroup().add(jLabel2).addPreferredGap(LayoutStyle.UNRELATED, gap, gap).add(thislayout.createParallelGroup(GroupLayout.LEADING).add(prefs.OutputFileName, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).add(jButtonBrowse)).addPreferredGap(LayoutStyle.RELATED, gap, gap).add(thislayout.createParallelGroup(GroupLayout.LEADING).add(jLabel3).add(prefs.OutputObjectName, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)))));
        jButtonAdd.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                JFileChooser Chooser = new JFileChooser();
                Chooser.setMultiSelectionEnabled(true);
                String[] imagefile = new String[] { "jpg", "JPG", "jpeg", "JPEG" };
                FileFilter filter = new FileExtensionFilter(imagefile, "JPEG image file (*.jpg, *.jpeg)");
                Chooser.setFileFilter(filter);
                Chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if (JFileChooser.APPROVE_OPTION == Chooser.showDialog(null, "Select")) {
                    File[] files = Chooser.getSelectedFiles();
                    for (int i = 0; i < files.length; i++) {
                        prefs.imagefiles.addElement(files[i].toString());
                    }
                }
                JButtonNext.setEnabled((prefs.imagefiles.getSize() != 0) && (prefs.OutputFileName.getText().length() != 0));
            }
        });
        jButtonRemove.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (filelist.getSelectedIndices().length > 0) {
                    int[] tmp = filelist.getSelectedIndices();
                    int[] selectedIndices = filelist.getSelectedIndices();
                    for (int i = tmp.length - 1; i >= 0; i--) {
                        selectedIndices = filelist.getSelectedIndices();
                        prefs.imagefiles.remove(selectedIndices[i]);
                    }
                }
                JButtonNext.setEnabled((prefs.imagefiles.getSize() != 0) && (prefs.OutputFileName.getText().length() != 0));
            }
        });
        jButtonBrowse.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                JFileChooser Chooser = new JFileChooser();
                String[] stl = new String[] { "stl", "STL" };
                FileFilter filter = new FileExtensionFilter(stl, "STL file");
                Chooser.setFileFilter(filter);
                Chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                if (JFileChooser.APPROVE_OPTION == Chooser.showSaveDialog(null)) {
                    File file = Chooser.getSelectedFile();
                    prefs.OutputFileName.setText(file.toString());
                }
                JButtonNext.setEnabled((prefs.imagefiles.getSize() != 0) && (prefs.OutputFileName.getText().length() != 0));
            }
        });
        prefs.OutputFileName.addKeyListener(new java.awt.event.KeyListener() {

            public void keyPressed(java.awt.event.KeyEvent keyEvent) {
                JButtonNext.setEnabled((prefs.imagefiles.getSize() != 0) && (prefs.OutputFileName.getText().length() != 0));
            }

            public void keyTyped(java.awt.event.KeyEvent keyEvent) {
                JButtonNext.setEnabled((prefs.imagefiles.getSize() != 0) && (prefs.OutputFileName.getText().length() != 0));
            }

            public void keyReleased(java.awt.event.KeyEvent keyEvent) {
                JButtonNext.setEnabled((prefs.imagefiles.getSize() != 0) && (prefs.OutputFileName.getText().length() != 0));
            }
        });
    }

    private void FindCalibrationSheetCirclesEtc() {
        class workingThread implements Runnable {

            public void run() {
                ProcessCalibrationSheet();
                final int value = calibrationsheetinterrogationnumberofsteps;
                EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        jProgressBar2.setValue(value);
                    }
                });
                EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        JButtonNext.setEnabled(true);
                        JButtonPrevious.setEnabled(true);
                    }
                });
                setStep(stepsarray[currentstep.ordinal() + 1]);
            }
        }
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                JButtonNext.setEnabled(false);
                JButtonPrevious.setEnabled(false);
                jLabelProgressBar1 = new javax.swing.JLabel();
                jProgressBar2 = new JProgressBar(0, (prefs.imagefiles.getSize() * calibrationnumberofsubsteps) + objectvoxelisationnumberofsteps + calibrationsheetinterrogationnumberofsteps + numberofotherprogressbarsteps);
                jProgressBar2.setValue(0);
                jLabelProgressBar2 = new javax.swing.JLabel();
                jLabelOutputLog = new javax.swing.JLabel();
                jScrollPanel = new JScrollPane();
                jTextAreaOutput = new JTextArea();
                jLabelProgressBar1 = new javax.swing.JLabel();
                jLabelTitle.setText("Interrogating Calibration Sheet Image");
                jTextAreaOutput.setColumns(20);
                jTextAreaOutput.setRows(5);
                jTextAreaOutput.setEditable(false);
                jTextAreaOutput.setLineWrap(true);
                jScrollPanel.setViewportView(jTextAreaOutput);
                jLabelProgressBar1.setFont(new java.awt.Font("Dialog", 0, 12));
                jLabelProgressBar1.setText("");
                jLabelProgressBar2.setFont(new java.awt.Font("Dialog", 0, 12));
                jLabelProgressBar2.setText("");
                jLabelOutputLog.setFont(new java.awt.Font("Dialog", 0, 12));
                jLabelOutputLog.setText("Processing Log");
                getContentPane().removeAll();
                GroupLayout thislayout = new GroupLayout(getContentPane());
                getContentPane().setLayout(thislayout);
                thislayout.setHorizontalGroup(thislayout.createParallelGroup(GroupLayout.LEADING).add(defaulthorizontal(thislayout)).add(thislayout.createSequentialGroup().addPreferredGap(LayoutStyle.UNRELATED, gap, gap).add(thislayout.createParallelGroup(GroupLayout.LEADING).add(jLabelProgressBar1).add(jProgressBar1).add(jLabelProgressBar2).add(jProgressBar2).add(jLabelOutputLog).add(jScrollPanel)).addPreferredGap(LayoutStyle.UNRELATED, gap, gap)));
                thislayout.setVerticalGroup(thislayout.createParallelGroup(GroupLayout.LEADING).add(defaultvertical(thislayout)).add(thislayout.createSequentialGroup().addPreferredGap(LayoutStyle.RELATED, 3 * gap, 3 * gap).add(jProgressBar1).addPreferredGap(LayoutStyle.RELATED).add(jLabelProgressBar1).addPreferredGap(LayoutStyle.UNRELATED).add(jProgressBar2).addPreferredGap(LayoutStyle.RELATED).add(jLabelProgressBar2).addPreferredGap(LayoutStyle.UNRELATED).add(jLabelOutputLog).addPreferredGap(LayoutStyle.RELATED).add(jScrollPanel).addPreferredGap(LayoutStyle.UNRELATED, 5 * gap, 5 * gap)));
            }
        });
        JButtonNext.setEnabled(false);
        JButtonPrevious.setEnabled(false);
        jProgressBar1 = new JProgressBar(0, 1);
        jProgressBar1.setValue(0);
        jProgressBar2 = new JProgressBar(0, 1);
        jProgressBar2.setValue(0);
        Thread t = new Thread(new workingThread());
        t.start();
    }

    private void Calibration() {
        class workingThread implements Runnable {

            public void run() {
                images = new Image[prefs.imagefiles.getSize()];
                for (int j = 0; j < prefs.imagefiles.getSize(); j++) {
                    final String text2 = "Processing image " + (j + 1) + " of " + prefs.imagefiles.getSize();
                    try {
                        EventQueue.invokeLater(new Runnable() {

                            public void run() {
                                jLabelProgressBar2.setText(text2);
                            }
                        });
                    } catch (Exception e) {
                        System.out.println("Exception in updating the progress bar" + e.getMessage());
                    }
                    boolean compute = true;
                    if (prefs.SkipStep[currentstep.ordinal()]) {
                        compute = false;
                        try {
                            String filename = new File(prefs.imagefiles.getElementAt(j).toString()).getParent() + File.separatorChar + "UndistortedImage" + new File(prefs.imagefiles.getElementAt(j).toString()).getName();
                            images[j] = new Image(filename);
                            ProcessedImageProperties io = new ProcessedImageProperties(prefs.imagefiles.getElementAt(j).toString() + ".properties");
                            io.loadProperties();
                            images[j].originofimagecoordinates = io.originofimagecoordinates.clone();
                            images[j].setWorldtoImageTransformMatrix(io.WorldToImageTransform);
                            images[j].skipprocessing = io.skipprocessing;
                            filename = new File(prefs.imagefiles.getElementAt(j).toString()).getParent() + File.separatorChar + "SegmentedImage" + new File(prefs.imagefiles.getElementAt(j).toString()).getName();
                            if (!io.skipprocessing) images[j].SetProcessedPixels(new PixelColour().ConvertGreyscaleToBoolean(ImageFile.ReadImageFromFile(new float[0], filename), images[j].width, images[j].height, 128));
                        } catch (Exception e) {
                            System.out.println("Error reading processed image properties from file, initiating real-time processing");
                            images[j] = new Image();
                            compute = true;
                        }
                    }
                    if (compute) {
                        float[] kernel = GetGaussianFilter(3);
                        images[j] = new Image(prefs.imagefiles.getElementAt(j).toString(), kernel);
                        Point2d[] ellipsecenters = new Point2d[0];
                        if (!images[j].skipprocessing) ellipsecenters = FindEllipses(j);
                        if (Continue(j, ellipsecenters)) {
                            PointPairMatch circles = MatchCircles(j, ellipsecenters);
                            if (prefs.Debug) {
                                if (prefs.DebugCalibrationSheetBarycentricEstimate) {
                                    for (int useallpointpairs = 0; useallpointpairs < 2; useallpointpairs++) {
                                        PointPair2D[] basepairs;
                                        PointPair2D[] correct = circles.getMatchedPoints();
                                        if (useallpointpairs == 1) {
                                            basepairs = new PointPair2D[correct.length];
                                            for (int i = 0; i < correct.length; i++) basepairs[i] = correct[i].clone();
                                        } else {
                                            basepairs = new PointPair2D[3];
                                            for (int i = 0; i < 3; i++) basepairs[i] = correct[i].clone();
                                        }
                                        Point2d offset = new Point2d(calibrationsheetwidth / 2, calibrationsheetheight / 2);
                                        PixelColour[][] newimage = new PixelColour[calibrationsheetpixelswidth][calibrationsheetpixelsheight];
                                        for (int x = 0; x < calibrationsheetpixelswidth; x++) {
                                            for (int y = 0; y < calibrationsheetpixelsheight; y++) {
                                                Point2d point = new Point2d(x, y);
                                                point.x = point.x * (calibrationsheetwidth / calibrationsheetpixelswidth);
                                                point.y = point.y * (calibrationsheetheight / calibrationsheetpixelsheight);
                                                point.minus(offset);
                                                PointPair2D pair = new PointPair2D();
                                                pair.pointone = point.clone();
                                                boolean success = pair.EstimateSecondPoint(basepairs);
                                                if (success) newimage[x][y] = images[j].InterpolatePixelColour(pair.pointtwo); else newimage[x][y] = new PixelColour();
                                            }
                                            if (x % 100 == 0) System.out.print(".");
                                        }
                                        System.out.println();
                                        int dx = (int) (calibrationsheetpixelswidth / 200);
                                        int dy = (int) (calibrationsheetpixelsheight / 200);
                                        for (int i = 0; i < correct.length; i++) {
                                            Point2d point = correct[i].pointone.clone();
                                            point.plus(offset);
                                            point.x = point.x * (calibrationsheetpixelswidth / calibrationsheetwidth);
                                            point.y = point.y * (calibrationsheetpixelsheight / calibrationsheetheight);
                                            for (int x = (int) point.x - dx; x < (int) point.x + dx; x++) for (int y = (int) point.y - dy; y < (int) point.y + dy; y++) if ((x >= 0) && (x < calibrationsheetpixelswidth) && (y >= 0) && (y <= calibrationsheetpixelsheight)) newimage[x][y] = new PixelColour((byte) 255);
                                        }
                                        String filename;
                                        if (useallpointpairs == 0) filename = prefs.DebugSaveOutputImagesFolder + File.separatorChar + "BarycentricCalibrationSheetImageUsingFirst3Matches" + new File(images[j].filename).getName() + ".jpg"; else filename = prefs.DebugSaveOutputImagesFolder + File.separatorChar + "BarycentricCalibrationSheetImageUsingAllMatches" + new File(images[j].filename).getName() + ".jpg";
                                        GraphicsFeedback graphics = new GraphicsFeedback(true);
                                        graphics.ShowPixelColourArray(newimage, calibrationsheetpixelswidth, calibrationsheetpixelsheight);
                                        graphics.SaveImage(filename);
                                    }
                                }
                                if (prefs.DebugPointPairMatching) {
                                    int size = 1;
                                    ArrayList<PointPair2D[]> listofallmismatchedsubsets = new ArrayList<PointPair2D[]>();
                                    if (prefs.DebugPointPairMatchingSubsets) {
                                        Testing test = new Testing(circles.getMatchedPoints(), calibrationcirclecenters, calibrationsheetwidth, calibrationsheetheight);
                                        test.printcorrect = false;
                                        test.printincorrect = false;
                                        test.printsummary = true;
                                        test.outputfailedimage = false;
                                        test.printblankifincorrect = false;
                                        test.comparemethods = true;
                                        images[j].skipprocessing = true;
                                        test.image = images[j].clone();
                                        images[j].skipprocessing = false;
                                        test.outputCorrectMatches();
                                        listofallmismatchedsubsets = test.Combinations();
                                        size = listofallmismatchedsubsets.size();
                                    }
                                    for (int i = 0; i < size; i++) {
                                        GraphicsFeedback graphics1 = new GraphicsFeedback(true);
                                        Image calibrationsheet = new Image(prefs.calibrationpatterns.getElementAt(prefs.CurrentCalibrationPatternIndexNumber).toString());
                                        graphics1.ShowImage(calibrationsheet);
                                        GraphicsFeedback graphics2 = new GraphicsFeedback(true);
                                        graphics2.ShowImage(images[j]);
                                        PointPair2D[] pairs;
                                        if (!prefs.DebugPointPairMatchingSubsets) pairs = circles.getMatchedPoints(); else pairs = listofallmismatchedsubsets.get(i);
                                        PixelColour.StandardColours[] standardcolours = PixelColour.StandardColours.values();
                                        for (int k = 0; k < pairs.length; k++) {
                                            int calibrationsheetindex = -1;
                                            int imageellipseindex = -1;
                                            for (int l = 0; l < calibrationcirclecenters.length; l++) if (calibrationcirclecenters[l].isApproxEqual(pairs[k].pointone, 0.001)) calibrationsheetindex = l;
                                            for (int l = 0; l < imageellipses.length; l++) if (imageellipses[l].GetCenter().isApproxEqual(pairs[k].pointtwo, 0.001)) imageellipseindex = l;
                                            PixelColour colour;
                                            if (k < standardcolours.length) colour = new PixelColour(standardcolours[k]); else colour = new PixelColour(standardcolours[standardcolours.length - 1]);
                                            if (calibrationsheetindex >= 0) graphics1.PrintEllipse(calibrationcircles[calibrationsheetindex], colour, new Point2d(0, 0));
                                            if (imageellipseindex >= 0) graphics2.PrintEllipse(imageellipses[imageellipseindex], colour, images[j].originofimagecoordinates);
                                        }
                                        String filename;
                                        if (!prefs.DebugPointPairMatchingSubsets) filename = prefs.DebugSaveOutputImagesFolder + File.separatorChar + "CircleandEllipseMatchesForCalibrationSheetWithImage" + new File(images[j].filename).getName() + ".jpg"; else filename = prefs.DebugSaveOutputImagesFolder + File.separatorChar + "calibrationsheetforsubset" + i + ".jpg";
                                        graphics1.SaveImage(filename);
                                        if (!prefs.DebugPointPairMatchingSubsets) filename = prefs.DebugSaveOutputImagesFolder + File.separatorChar + "CircleandEllipseMatchesinImage" + new File(images[j].filename).getName() + ".jpg"; else filename = prefs.DebugSaveOutputImagesFolder + File.separatorChar + "imageforsubset" + i + ".jpg";
                                        graphics2.SaveImage(filename);
                                    }
                                }
                            }
                            LensDistortion distortion = EstimatingCameraParameters(j, circles);
                            if (!images[j].skipprocessing) {
                                UndoLensDistortion(j, distortion, false);
                                ImageSegmentation(j);
                                GraphicsFeedback(j);
                            }
                        }
                        if (prefs.SaveProcessedImageProperties) {
                            ProcessedImageProperties io = new ProcessedImageProperties(prefs.imagefiles.getElementAt(j).toString() + ".properties");
                            io.originofimagecoordinates = images[j].originofimagecoordinates.clone();
                            if (!images[j].skipprocessing) io.WorldToImageTransform = images[j].getWorldtoImageTransformMatrix().copy(); else io.WorldToImageTransform = new Matrix(3, 4);
                            io.skipprocessing = images[j].skipprocessing;
                            try {
                                io.saveProperties();
                            } catch (Exception e) {
                                System.out.println("Error writing processed image properties.");
                            }
                            if ((images[j].width != 0) && (images[j].height != 0)) {
                                GraphicsFeedback graphics = new GraphicsFeedback();
                                graphics.ShowImage(images[j]);
                                graphics.SaveImage(new File(prefs.imagefiles.getElementAt(j).toString()).getParent() + File.separatorChar + "UndistortedImage" + new File(prefs.imagefiles.getElementAt(j).toString()).getName());
                                if (!images[j].skipprocessing) {
                                    graphics = new GraphicsFeedback();
                                    graphics.ShowBinaryimage(images[j].getProcessedPixels(), images[j].width, images[j].height);
                                    graphics.SaveImage(new File(prefs.imagefiles.getElementAt(j).toString()).getParent() + File.separatorChar + "SegmentedImage" + new File(prefs.imagefiles.getElementAt(j).toString()).getName());
                                }
                            }
                        }
                    }
                    final int value = calibrationsheetinterrogationnumberofsteps + (calibrationnumberofsubsteps * (j + 1));
                    EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            jProgressBar2.setValue(value);
                        }
                    });
                }
                EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        JButtonNext.setEnabled(true);
                        JButtonPrevious.setEnabled(true);
                    }
                });
                setStep(stepsarray[currentstep.ordinal() + 1]);
            }
        }
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                JButtonNext.setEnabled(false);
                JButtonPrevious.setEnabled(false);
                jProgressBar1 = new JProgressBar(0, 1);
                jProgressBar1.setValue(0);
                jProgressBar2.setValue(calibrationsheetinterrogationnumberofsteps);
                jLabelTitle.setText("Calibration");
                jLabelProgressBar1.setText(" ");
                jLabelProgressBar2.setText(" ");
                getContentPane().removeAll();
                GroupLayout thislayout = new GroupLayout(getContentPane());
                getContentPane().setLayout(thislayout);
                thislayout.setHorizontalGroup(thislayout.createParallelGroup(GroupLayout.LEADING).add(defaulthorizontal(thislayout)).add(thislayout.createSequentialGroup().addPreferredGap(LayoutStyle.UNRELATED, gap, gap).add(thislayout.createParallelGroup(GroupLayout.LEADING).add(jLabelProgressBar1).add(jProgressBar1).add(jLabelProgressBar2).add(jProgressBar2).add(jLabelOutputLog).add(jScrollPanel)).addPreferredGap(LayoutStyle.UNRELATED, gap, gap)));
                thislayout.setVerticalGroup(thislayout.createParallelGroup(GroupLayout.LEADING).add(defaultvertical(thislayout)).add(thislayout.createSequentialGroup().addPreferredGap(LayoutStyle.RELATED, 3 * gap, 3 * gap).add(jProgressBar1).addPreferredGap(LayoutStyle.RELATED).add(jLabelProgressBar1).addPreferredGap(LayoutStyle.UNRELATED).add(jProgressBar2).addPreferredGap(LayoutStyle.RELATED).add(jLabelProgressBar2).addPreferredGap(LayoutStyle.UNRELATED).add(jLabelOutputLog).addPreferredGap(LayoutStyle.RELATED).add(jScrollPanel).addPreferredGap(LayoutStyle.UNRELATED, 5 * gap, 5 * gap)));
            }
        });
        JButtonNext.setEnabled(false);
        JButtonPrevious.setEnabled(false);
        jProgressBar2.setValue(calibrationsheetinterrogationnumberofsteps);
        Thread t = new Thread(new workingThread());
        t.start();
    }

    private void FindCoarseVoxelisedObject() {
        class workingThread implements Runnable {

            public void run() {
                AxisAlignedBoundingBox volumeofinterest = new AxisAlignedBoundingBox();
                volumeofinterest = new AxisAlignedBoundingBox();
                volumeofinterest.minx = -calibrationsheetwidth / 2;
                volumeofinterest.maxx = calibrationsheetwidth / 2;
                volumeofinterest.miny = -calibrationsheetheight / 2;
                volumeofinterest.maxy = calibrationsheetheight / 2;
                volumeofinterest.minz = 0;
                if (calibrationsheetheight > calibrationsheetwidth) volumeofinterest.maxz = calibrationsheetheight; else volumeofinterest.maxz = calibrationsheetwidth;
                AxisAlignedBoundingBox oldvolumeofinterest = volumeofinterest.clone();
                AxisAlignedBoundingBox[] surfacevoxels = new AxisAlignedBoundingBox[0];
                int countimages = 0;
                for (int j = 0; j < images.length; j++) if (!images[j].skipprocessing) countimages++;
                if (countimages < 2) {
                    final String text4 = "Error: Not enough valid images to extract 3D information. At least two are needed.\n";
                    EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            jTextAreaOutput.setText(jTextAreaOutput.getText() + text4);
                        }
                    });
                } else {
                    boolean compute = true;
                    if (prefs.SkipStep[currentstep.ordinal()]) {
                        compute = false;
                        Image[] oldimages = new Image[images.length];
                        for (int j = 0; j < images.length; j++) if (!images[j].skipprocessing) oldimages[j] = images[j].clone();
                        try {
                            for (int j = 0; j < images.length; j++) {
                                String filename = new File(prefs.imagefiles.getElementAt(j).toString()).getParent() + File.separatorChar + "RestrictedSearchSpaceImage" + new File(prefs.imagefiles.getElementAt(j).toString()).getName();
                                if (!images[j].skipprocessing) images[j].SetProcessedPixels(new PixelColour().ConvertGreyscaleToBoolean(ImageFile.ReadImageFromFile(new float[0], filename), images[j].width, images[j].height, 128));
                                final int temp = j;
                                final String text = "Restricting Search space for image " + (j + 1) + " of " + images.length;
                                EventQueue.invokeLater(new Runnable() {

                                    public void run() {
                                        jProgressBar1.setMinimum(0);
                                        jProgressBar1.setMaximum(images.length);
                                        jProgressBar1.setValue(temp);
                                        jLabelProgressBar1.setText(text);
                                    }
                                });
                            }
                        } catch (Exception e) {
                            System.out.println("Error reading restricted search image properties from file, initiating real-time processing");
                            compute = true;
                            for (int j = 0; j < images.length; j++) if (!images[j].skipprocessing) images[j] = oldimages[j].clone();
                        }
                    }
                    if (compute) {
                        Voxel voxels = Voxelisation(volumeofinterest);
                        surfacevoxels = voxels.getSurfaceVoxels();
                        if ((prefs.Debug) && (prefs.DebugVoxelisation)) {
                            try {
                                EventQueue.invokeLater(new Runnable() {

                                    public void run() {
                                        jProgressBar1.setValue(jProgressBar1.getMinimum());
                                        jProgressBar2.setValue(jProgressBar2.getValue() + 1);
                                        jLabelProgressBar1.setText("Converting Voxels to Triangles");
                                    }
                                });
                            } catch (Exception e) {
                                System.out.println("Exception in updating the progress bar" + e.getMessage());
                            }
                            TrianglePlusVertexArray triplusvertices = voxels.ConvertSurfaceVoxelsToTriangles(jProgressBar1);
                            final String text2 = "Object bounding volume voxelised surface uses " + triplusvertices.GetTriangleArrayLength() + " triangles.\n";
                            EventQueue.invokeLater(new Runnable() {

                                public void run() {
                                    jTextAreaOutput.setText(jTextAreaOutput.getText() + text2);
                                }
                            });
                            String filename = prefs.DebugSaveOutputImagesFolder + File.separatorChar + "VisualHullVoxelisation.stl";
                            System.out.println("Saving STL file " + filename);
                            OutputSTLFile(filename, triplusvertices.GetTriangleArray(), triplusvertices.GetVertexArray(), "Voxelisation of Visual Hull", false);
                            EventQueue.invokeLater(new Runnable() {

                                public void run() {
                                    jLabelTitle.setText("Finding Object");
                                    jLabelProgressBar2.setText("Finding Object");
                                }
                            });
                        }
                        if ((prefs.Debug) && (prefs.DebugShow3DFlythough)) {
                            Graphics3DFeedback graphics = new Graphics3DFeedback(oldvolumeofinterest, volumeofinterest, calibrationcirclecenters, standardcalibrationcircleonprintedsheet, images, surfacevoxels, prefs.DebugShow3DFlythoughImagePixelStep);
                            graphics.Display();
                        }
                        RestrictSearch(surfacevoxels);
                        if (prefs.SaveRestrictedSearchImageProperties) {
                            for (int j = 0; j < images.length; j++) if (!images[j].skipprocessing) {
                                GraphicsFeedback graphics = new GraphicsFeedback();
                                graphics.ShowBinaryimage(images[j].getProcessedPixels(), images[j].width, images[j].height);
                                graphics.SaveImage(new File(prefs.imagefiles.getElementAt(j).toString()).getParent() + File.separatorChar + "RestrictedSearchSpaceImage" + new File(prefs.imagefiles.getElementAt(j).toString()).getName());
                            }
                        }
                    }
                    if ((prefs.Debug) && (prefs.DebugShow3DFlythough)) {
                        Graphics3DFeedback graphics = new Graphics3DFeedback(oldvolumeofinterest, volumeofinterest, calibrationcirclecenters, standardcalibrationcircleonprintedsheet, images, surfacevoxels, 4);
                        graphics.Display();
                    }
                }
                setStep(stepsarray[currentstep.ordinal() + 1]);
            }
        }
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                JButtonNext.setEnabled(false);
                JButtonPrevious.setEnabled(false);
                jProgressBar1 = new JProgressBar(0, 1);
                jProgressBar1.setValue(0);
                jProgressBar2.setValue((prefs.imagefiles.getSize() * calibrationnumberofsubsteps) + calibrationsheetinterrogationnumberofsteps);
                jLabelTitle.setText("Finding Object");
                jLabelProgressBar1.setText(" ");
                jLabelProgressBar2.setText("Finding Object");
                getContentPane().removeAll();
                GroupLayout thislayout = new GroupLayout(getContentPane());
                getContentPane().setLayout(thislayout);
                thislayout.setHorizontalGroup(thislayout.createParallelGroup(GroupLayout.LEADING).add(defaulthorizontal(thislayout)).add(thislayout.createSequentialGroup().addPreferredGap(LayoutStyle.UNRELATED, gap, gap).add(thislayout.createParallelGroup(GroupLayout.LEADING).add(jLabelProgressBar1).add(jProgressBar1).add(jLabelProgressBar2).add(jProgressBar2).add(jLabelOutputLog).add(jScrollPanel)).addPreferredGap(LayoutStyle.UNRELATED, gap, gap)));
                thislayout.setVerticalGroup(thislayout.createParallelGroup(GroupLayout.LEADING).add(defaultvertical(thislayout)).add(thislayout.createSequentialGroup().addPreferredGap(LayoutStyle.RELATED, 3 * gap, 3 * gap).add(jProgressBar1).addPreferredGap(LayoutStyle.RELATED).add(jLabelProgressBar1).addPreferredGap(LayoutStyle.UNRELATED).add(jProgressBar2).addPreferredGap(LayoutStyle.RELATED).add(jLabelProgressBar2).addPreferredGap(LayoutStyle.UNRELATED).add(jLabelOutputLog).addPreferredGap(LayoutStyle.RELATED).add(jScrollPanel).addPreferredGap(LayoutStyle.UNRELATED, 5 * gap, 5 * gap)));
            }
        });
        JButtonNext.setEnabled(false);
        JButtonPrevious.setEnabled(false);
        jProgressBar2.setValue((prefs.imagefiles.getSize() * calibrationnumberofsubsteps) + calibrationsheetinterrogationnumberofsteps);
        Thread t = new Thread(new workingThread());
        t.start();
    }

    private void OutputSTLFile(final String filename, final Triangle3D[] triangles, final Point3d[] points, final String objectname, final boolean finalstep) {
        class workingThread implements Runnable {

            public void run() {
                String error;
                if ((filename != "") && (triangles.length != 0)) {
                    error = STLFile.Write(jProgressBar1, triangles, points, objectname, filename);
                } else error = "Output filename blank or nothing to write, not saving.";
                final String text5 = error;
                EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        jTextAreaOutput.setText(jTextAreaOutput.getText() + text5);
                    }
                });
                if (finalstep) {
                    final String text = "Processing complete. Click Finish to exit.\n";
                    EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            JButtonNext.setEnabled(true);
                            JButtonPrevious.setEnabled(true);
                            jProgressBar1.setValue(jProgressBar1.getMaximum());
                            jProgressBar2.setValue(jProgressBar2.getMaximum());
                            jTextAreaOutput.setText(jTextAreaOutput.getText() + text);
                        }
                    });
                    if (prefs.SkipStep[currentstep.ordinal() + 1]) setStep(stepsarray[currentstep.ordinal() + 1]);
                }
            }
        }
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                JButtonNext.setEnabled(false);
                JButtonPrevious.setEnabled(false);
                jProgressBar1 = new JProgressBar(0, 1);
                jProgressBar1.setValue(0);
                jProgressBar2.setValue((prefs.imagefiles.getSize() * calibrationnumberofsubsteps) + calibrationsheetinterrogationnumberofsteps + objectvoxelisationnumberofsteps);
                jLabelTitle.setText("Writing Output File");
                jLabelProgressBar1.setText(" ");
                jLabelProgressBar2.setText("Writing Output File");
                getContentPane().removeAll();
                GroupLayout thislayout = new GroupLayout(getContentPane());
                getContentPane().setLayout(thislayout);
                thislayout.setHorizontalGroup(thislayout.createParallelGroup(GroupLayout.LEADING).add(defaulthorizontal(thislayout)).add(thislayout.createSequentialGroup().addPreferredGap(LayoutStyle.UNRELATED, gap, gap).add(thislayout.createParallelGroup(GroupLayout.LEADING).add(jLabelProgressBar1).add(jProgressBar1).add(jLabelProgressBar2).add(jProgressBar2).add(jLabelOutputLog).add(jScrollPanel)).addPreferredGap(LayoutStyle.UNRELATED, gap, gap)));
                thislayout.setVerticalGroup(thislayout.createParallelGroup(GroupLayout.LEADING).add(defaultvertical(thislayout)).add(thislayout.createSequentialGroup().addPreferredGap(LayoutStyle.RELATED, 3 * gap, 3 * gap).add(jProgressBar1).addPreferredGap(LayoutStyle.RELATED).add(jLabelProgressBar1).addPreferredGap(LayoutStyle.UNRELATED).add(jProgressBar2).addPreferredGap(LayoutStyle.RELATED).add(jLabelProgressBar2).addPreferredGap(LayoutStyle.UNRELATED).add(jLabelOutputLog).addPreferredGap(LayoutStyle.RELATED).add(jScrollPanel).addPreferredGap(LayoutStyle.UNRELATED, 5 * gap, 5 * gap)));
            }
        });
        JButtonNext.setEnabled(false);
        JButtonPrevious.setEnabled(false);
        Thread t = new Thread(new workingThread());
        t.start();
    }

    private void end() {
        if (save) {
            try {
                prefs.save();
            } catch (Exception e) {
                System.out.println("Error saving preferences");
                System.out.println(e);
            }
        }
        dispose();
        System.exit(0);
    }

    private void initComponents() {
        surfacetriangles = new Triangle3D[0];
        surfacepoints = new Point3d[0];
        setDefaultLookAndFeelDecorated(false);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        Dimension frameSize = new Dimension(640, 480);
        if (screenSize.width < frameSize.width) frameSize = new Dimension(screenSize.width, screenSize.height);
        setPreferredSize(frameSize);
        setLocation((screenSize.width - frameSize.width) / 2, (screenSize.height - frameSize.height) / 2);
        jLabelTitle = new JLabel();
        jLabelTitle.setSize(360, gap);
        JButtonNext = new JButton();
        JButtonCancel = new JButton();
        JButtonPrevious = new JButton();
        jProgressBar1 = new JProgressBar(0, 1);
        jProgressBar2 = new JProgressBar(0, 1);
        jProgressBar1.setValue(1);
        jProgressBar2.setValue(1);
        JButtonNext.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (!currentstep.equals(laststep)) setStep(stepsarray[currentstep.ordinal() + 1]);
            }
        });
        JButtonPrevious.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                if (!currentstep.equals(firststep)) setStep(stepsarray[currentstep.ordinal() - 1]);
            }
        });
        JButtonCancel.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                save = prefs.SaveOnProgramCancel;
                setStep(laststep);
            }
        });
        addWindowListener(new WindowAdapter() {

            public void windowClosing(WindowEvent evt) {
                save = prefs.SaveOnProgramWindowClose;
                setStep(laststep);
            }
        });
        pack();
        setVisible(true);
    }

    /**************************************************************************************************************************************************************************
	 * 
	 * Private methods invoked by without user interaction one after the other by the Calibration step
	 * 
	 * These are primarily in different methods for tidyness only
	 *
	 **************************************************************************************************************************************************************************/
    private void ProcessCalibrationSheet() {
        JProgressBar bar = new JProgressBar(0, 1);
        try {
            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    jLabelProgressBar1.setText("Please wait...");
                    jLabelProgressBar2.setText("Finding Circles in Calibration Sheet");
                }
            });
        } catch (Exception e) {
            System.out.println("Exception in updating the progress bar" + e.getMessage());
        }
        Image calibrationsheet = new Image();
        double circleradius = 0;
        calibrationcircles = new Ellipse[0];
        boolean compute = true;
        if (prefs.SkipStep[currentstep.ordinal()]) {
            compute = false;
            CalibrationSheetProperties io = new CalibrationSheetProperties(prefs.calibrationpatterns.getElementAt(prefs.CurrentCalibrationPatternIndexNumber).toString() + ".properties");
            try {
                io.load();
                circleradius = io.circleradius;
                calibrationsheet.width = io.width;
                calibrationsheet.height = io.height;
                calibrationcircles = io.calibrationcircles.clone();
            } catch (Exception e) {
                System.out.println("Error reading in pre-computed calibration sheet properties. Reverting to interrogation of calibration sheet image");
                compute = true;
            }
        }
        if (compute) {
            calibrationsheet = new Image(prefs.calibrationpatterns.getElementAt(prefs.CurrentCalibrationPatternIndexNumber).toString());
            EdgeExtraction2D edges = new EdgeExtraction2D(calibrationsheet);
            edges.NonMaximalLocalSuppressionAndThresholdStrengthMap(prefs.AlgorithmSettingCalibrationSheetEdgeStrengthThreshold);
            edges.RemoveSpuriousPoints();
            FindEllipses find = new FindEllipses(edges, 1);
            bar.setValue(0);
            double minsquared = 0;
            double angle = ((double) 10 / 360) * tau;
            double maxsquared = Double.MAX_VALUE;
            while (bar.getValue() < bar.getMaximum()) {
                bar = find.ExtractEllipses(minsquared, maxsquared, angle, 1);
                if (maxsquared == Double.MAX_VALUE) {
                    Ellipse[] ellipses = find.getEllipses();
                    for (int i = 0; i < ellipses.length; i++) {
                        if (ellipses[i].IsAbsoluteBlackEllipseOnAbsoluteWhiteBackground(calibrationsheet, 0.9)) {
                            minsquared = Math.pow(ellipses[i].GetMinorSemiAxisLength() * 0.95 * 2, 2);
                            maxsquared = Math.pow(ellipses[i].GetMajorSemiAxisLength() * 1.05 * 2, 2);
                            i = ellipses.length;
                        }
                    }
                }
                final JProgressBar temp = bar;
                final boolean foundcircle = maxsquared != Double.MAX_VALUE;
                try {
                    EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            jProgressBar1.setMinimum(temp.getMinimum());
                            jProgressBar1.setMaximum(temp.getMaximum());
                            jProgressBar1.setValue(temp.getValue());
                            if (foundcircle) jLabelProgressBar1.setText("Finding Circles in Calibration Sheet");
                        }
                    });
                } catch (Exception e) {
                    System.out.println("Exception in updating the progress bar" + e.getMessage());
                }
            }
            calibrationcircles = find.getEllipses();
            circleradius = 0;
            for (int i = 0; i < calibrationcircles.length; i++) circleradius = circleradius + calibrationcircles[i].GetMinorSemiAxisLength() + calibrationcircles[i].GetMajorSemiAxisLength();
            circleradius = circleradius / (2 * calibrationcircles.length);
            if (prefs.SaveCalibrationSheetProperties) {
                CalibrationSheetProperties io = new CalibrationSheetProperties(prefs.calibrationpatterns.getElementAt(prefs.CurrentCalibrationPatternIndexNumber).toString() + ".properties");
                io.circleradius = circleradius;
                io.width = calibrationsheet.width;
                io.height = calibrationsheet.height;
                io.calibrationcircles = calibrationcircles.clone();
                try {
                    io.save();
                } catch (Exception e) {
                    System.out.println("Error writing calibration sheet properties.");
                }
            }
        }
        calibrationsheetpixelswidth = calibrationsheet.width;
        calibrationsheetpixelsheight = calibrationsheet.height;
        double w, h;
        if (prefs.PaperSizeIsCustom.isSelected()) {
            w = Double.valueOf(prefs.PaperCustomSizeWidthmm.getText());
            h = Double.valueOf(prefs.PaperCustomSizeHeightmm.getText());
        } else {
            Papersize current = (Papersize) prefs.PaperSizeList.getElementAt(prefs.CurrentPaperSizeIndexNumber);
            w = current.width;
            h = current.height;
        }
        if (!prefs.PaperOrientationIsPortrait.isSelected()) {
            double tempw = w;
            w = h;
            h = tempw;
        }
        if (prefs.PaperOrientationIsPortrait.isSelected()) {
            calibrationsheetwidth = w - Double.valueOf(prefs.PaperMarginHorizontalmm.getText());
            calibrationsheetheight = h - Double.valueOf(prefs.PaperMarginVerticalmm.getText());
        } else {
            calibrationsheetheight = w - Double.valueOf(prefs.PaperMarginHorizontalmm.getText());
            calibrationsheetwidth = h - Double.valueOf(prefs.PaperMarginVerticalmm.getText());
        }
        if (calibrationsheetheight < 1) calibrationsheetheight = 1;
        if (calibrationsheetwidth < 1) calibrationsheetwidth = 1;
        double xscale = calibrationsheetwidth / calibrationsheet.width;
        double yscale = calibrationsheetheight / calibrationsheet.height;
        if (prefs.CalibrationSheetKeepAspectRatioWhenPrinted.isSelected()) {
            if (calibrationsheet.height > calibrationsheet.width) {
                calibrationsheetwidth = yscale * calibrationsheet.width;
                xscale = yscale;
            } else {
                calibrationsheetheight = xscale * calibrationsheet.height;
                yscale = xscale;
            }
        }
        calibrationcirclecenters = new Point2d[calibrationcircles.length];
        Point2d calibrationcsheetcenter = new Point2d(calibrationsheetwidth / 2, calibrationsheetheight / 2);
        for (int i = 0; i < calibrationcircles.length; i++) {
            calibrationcirclecenters[i] = new Point2d((calibrationcircles[i].GetCenter().x * xscale), (calibrationcircles[i].GetCenter().y * yscale));
            calibrationcirclecenters[i].minus(calibrationcsheetcenter);
        }
        calibrationsheetxlength = circleradius * xscale;
        calibrationsheetylength = circleradius * yscale;
        double calibrationsheetangle = 0;
        if (calibrationsheetylength > calibrationsheetxlength) calibrationsheetangle = tau / 4;
        standardcalibrationcircleonprintedsheet = new Ellipse(new Point2d(0, 0), calibrationsheetxlength, calibrationsheetylength, calibrationsheetangle);
        String extratext = "";
        if (calibrationsheetxlength != calibrationsheetylength) {
            String direction;
            if (calibrationsheetangle == 0) direction = "X"; else direction = "Y";
            extratext = "Given the size of the printed sheet this means calibration circles are printed as ellipses \noriented in the " + direction + " direction with semi-axis lengths of " + new BigDecimal(calibrationsheetxlength, new MathContext(4, RoundingMode.DOWN)).toString() + "mm and " + new BigDecimal(calibrationsheetylength, new MathContext(4, RoundingMode.DOWN)).toString() + "mm\n";
        } else extratext = "Given the size of the printed sheet this means calibration circles are printed as circles \nwith radius of " + new BigDecimal(calibrationsheetxlength, new MathContext(4, RoundingMode.DOWN)).toString() + "mm\n";
        final String firsttext = "" + calibrationcirclecenters.length + " circle(s) found in calibration sheet with radius estimated to be approximately " + Math.round(circleradius) + " pixels \n" + extratext;
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                jTextAreaOutput.setText(firsttext);
            }
        });
    }

    private Point2d[] FindEllipses(int i) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                jProgressBar1.setValue(jProgressBar1.getMinimum());
                jProgressBar2.setValue(jProgressBar2.getValue() + 1);
                jLabelProgressBar1.setText("Finding Ellipses in the image");
            }
        });
        double lowestangleinradians = ((double) prefs.AlgorithmSettingMaximumCameraAngleFromVerticalInDegrees / 360) * tau;
        int width = prefs.AlgorithmSettingResampledImageWidthForEllipseDetection;
        double scale = (double) images[i].width / (double) width;
        int height = (int) ((double) images[i].height / scale);
        PixelColour[][] scaledpixels = new PixelColour[width][height];
        for (int x = 0; x < width; x++) {
            final JProgressBar temp = new JProgressBar(0, width);
            final int h = height;
            temp.setValue(x);
            try {
                EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        jProgressBar1.setMinimum(temp.getMinimum());
                        jProgressBar1.setMaximum(temp.getMaximum());
                        jProgressBar1.setValue(temp.getValue());
                        jLabelProgressBar1.setText("Temporarily resampling image to " + temp.getMaximum() + "x" + h);
                    }
                });
            } catch (Exception e) {
                System.out.println("Exception in updating the progress bar" + e.getMessage());
            }
            for (int y = 0; y < height; y++) {
                Point2d point = new Point2d(x * scale, y * scale);
                scaledpixels[x][y] = images[i].InterpolatePixelColour(point);
            }
        }
        Image scaledimage = new Image(scaledpixels, width, height);
        double asquared = Math.pow(scaledimage.width, 2) + Math.pow(scaledimage.height, 2);
        double a = Math.sqrt(asquared);
        double h = a / Math.sin(lowestangleinradians);
        double b = Math.sqrt((h * h) - asquared);
        double radiustowidth = calibrationsheetxlength / calibrationsheetwidth;
        double radiustoheight = calibrationsheetylength / calibrationsheetheight;
        double maxradius = radiustowidth * scaledimage.width;
        if (radiustoheight * scaledimage.height > maxradius) maxradius = radiustoheight * scaledimage.height;
        double maxsquared = Math.pow((maxradius * 2), 2);
        double minsquared = Math.pow((maxradius * 2 * (b / h)), 2);
        ;
        double accumulatorquantumsize = 5;
        EdgeExtraction2D edges = new EdgeExtraction2D(scaledimage);
        if ((prefs.Debug) && (prefs.DebugEdgeFindingForEllipseDetection)) {
            GraphicsFeedback graphics = new GraphicsFeedback(true);
            byte[][] temp = edges.GetStrengthMap();
            boolean[][] newimage = new boolean[scaledimage.width][scaledimage.height];
            for (int x = 0; x < scaledimage.width; x++) for (int y = 0; y < scaledimage.height; y++) newimage[x][y] = ((int) temp[x][y] != 0);
            graphics.ShowBinaryimage(newimage, scaledimage.width, scaledimage.height);
            String filename = prefs.DebugSaveOutputImagesFolder + File.separatorChar + "InitialEdgeStrengthMapImage" + new File(images[i].filename).getName() + ".jpg";
            graphics.SaveImage(filename);
        }
        edges.NonMaximalLocalSuppressionAndThresholdStrengthMap(prefs.AlgorithmSettingEdgeStrengthThreshold);
        edges.RemoveSpuriousPoints();
        if ((prefs.Debug) && (prefs.DebugEdgeFindingForEllipseDetection)) {
            GraphicsFeedback graphics = new GraphicsFeedback(true);
            byte[][] temp = edges.GetStrengthMap();
            boolean[][] newimage = new boolean[scaledimage.width][scaledimage.height];
            for (int x = 0; x < scaledimage.width; x++) for (int y = 0; y < scaledimage.height; y++) newimage[x][y] = ((int) temp[x][y] != 0);
            graphics.ShowBinaryimage(newimage, scaledimage.width, scaledimage.height);
            String filename = prefs.DebugSaveOutputImagesFolder + File.separatorChar + "FilteredEdgeStrengthMapImage" + new File(images[i].filename).getName() + ".jpg";
            graphics.SaveImage(filename);
        }
        int countellipses = 0;
        boolean keep[] = new boolean[0];
        FindEllipses find = new FindEllipses(edges, standardcalibrationcircleonprintedsheet.GetMinorSemiAxisLength() / standardcalibrationcircleonprintedsheet.GetMajorSemiAxisLength());
        JProgressBar bar = new JProgressBar(0, 1);
        bar.setValue(0);
        while (bar.getValue() < bar.getMaximum()) {
            bar = find.ExtractEllipses(minsquared, maxsquared, lowestangleinradians, accumulatorquantumsize);
            final JProgressBar temp = bar;
            try {
                EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        jProgressBar1.setMinimum(temp.getMinimum());
                        jProgressBar1.setMaximum(temp.getMaximum());
                        jProgressBar1.setValue(temp.getValue());
                        jLabelProgressBar1.setText("Finding Ellipses in the resampled image");
                    }
                });
            } catch (Exception e) {
                System.out.println("Exception in updating the progress bar" + e.getMessage());
            }
        }
        imageellipses = find.getEllipses();
        keep = new boolean[imageellipses.length];
        for (int j = 0; j < imageellipses.length; j++) {
            imageellipses[j] = new Ellipse(imageellipses[j].GetCenter().timesEquals(scale), imageellipses[j].GetMajorSemiAxisLength() * scale, imageellipses[j].GetMinorSemiAxisLength() * scale, imageellipses[j].GetOrientationAngleinRadians());
            keep[j] = imageellipses[j].IsBlackEllipseOnWhiteBackground(images[i], prefs.AlgorithmSettingEdgeStrengthThreshold, prefs.AlgorithmSettingEllipseValidityThresholdPercentage);
            if (keep[j]) countellipses++;
        }
        if ((prefs.Debug) && (prefs.DebugEllipseFinding)) {
            PixelColour red = new PixelColour(PixelColour.StandardColours.Red);
            PixelColour green = new PixelColour(PixelColour.StandardColours.Green);
            GraphicsFeedback graphics = new GraphicsFeedback(true);
            graphics.ShowImage(images[i]);
            for (int j = 0; j < imageellipses.length; j++) {
                if (keep[j]) graphics.OutlineEllipse(imageellipses[j], green, images[i].originofimagecoordinates); else graphics.OutlineEllipse(imageellipses[j], red, images[i].originofimagecoordinates);
            }
            String filename = prefs.DebugSaveOutputImagesFolder + File.separatorChar + "FoundEllipsesInImage" + new File(images[i].filename).getName() + ".jpg";
            graphics.SaveImage(filename);
        }
        if ((prefs.Debug) && (prefs.DebugManualEllipseSelection)) {
            PixelColour red = new PixelColour(PixelColour.StandardColours.Red);
            PixelColour green = new PixelColour(PixelColour.StandardColours.Green);
            GraphicsFeedback graphics = new GraphicsFeedback(false);
            graphics.ShowImage(images[i]);
            graphics.savedimagefilename = prefs.DebugSaveOutputImagesFolder + File.separatorChar + "ManuallySelectedFoundEllipsesInImage" + new File(images[i].filename).getName() + ".jpg";
            imageellipses = graphics.ManualSelectionOfEllipses(keep, imageellipses, images[i].originofimagecoordinates, red, green);
            countellipses = imageellipses.length;
            keep = new boolean[countellipses];
            for (int j = 0; j < imageellipses.length; j++) keep[j] = true;
        }
        Point2d[] ellipsecenters = new Point2d[countellipses];
        countellipses = 0;
        for (int j = 0; j < imageellipses.length; j++) {
            if (keep[j]) {
                imageellipses[j].ResetCenter(imageellipses[j].GetCentreOfGravity(images[i], prefs.AlgorithmSettingEdgeStrengthThreshold));
                ellipsecenters[countellipses] = imageellipses[j].GetCenter();
                countellipses++;
            }
        }
        final String text = "Number of Ellipses detected in image " + (i + 1) + "=" + ellipsecenters.length + "\n";
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                jTextAreaOutput.setText(jTextAreaOutput.getText() + text);
            }
        });
        return ellipsecenters;
    }

    private boolean Continue(int j, Point2d[] ellipsecenters) {
        boolean returnvalue = (!images[j].skipprocessing);
        if (!returnvalue) {
            final String text = "Error: File " + prefs.imagefiles.elementAt(j).toString() + " is not a recognisable image file, this image will not be processed further\n";
            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    jTextAreaOutput.setText(jTextAreaOutput.getText() + text);
                }
            });
        } else {
            returnvalue = (ellipsecenters.length != 0);
            if (!returnvalue) {
                final String text = "Error: No ellipses found in the image, this image will not be processed further\n";
                EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        jTextAreaOutput.setText(jTextAreaOutput.getText() + text);
                    }
                });
            } else {
                boolean skip;
                int count = 2;
                int third = 0;
                if (ellipsecenters.length >= prefs.AlgorithmSettingMinimumFoundValidEllipses) {
                    for (int i = 2; i < ellipsecenters.length; i++) {
                        if (count < 4) {
                            skip = ellipsecenters[i].isCollinear(ellipsecenters[0], ellipsecenters[1]);
                            if (count == 3) {
                                skip = skip || ellipsecenters[i].isCollinear(ellipsecenters[0], ellipsecenters[third]);
                                skip = skip || ellipsecenters[i].isCollinear(ellipsecenters[1], ellipsecenters[third]);
                            }
                            if (!skip) {
                                count++;
                                if (count == 3) third = i;
                            }
                        }
                    }
                }
                returnvalue = count >= 4;
                if (!returnvalue) {
                    final String text = "Error: Not enough valid ellipses found in the image or all centre points are in a line, this image will not be processed further.\n" + prefs.AlgorithmSettingMinimumFoundValidEllipses + " is the minimum number of ellipses needed.\n";
                    EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            jTextAreaOutput.setText(jTextAreaOutput.getText() + text);
                        }
                    });
                }
            }
        }
        images[j].skipprocessing = !returnvalue;
        return returnvalue;
    }

    private PointPairMatch MatchCircles(int i, Point2d[] ellipsecenters) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                jProgressBar1.setValue(jProgressBar1.getMinimum());
                jProgressBar2.setValue(jProgressBar2.getValue() + 1);
                jLabelProgressBar1.setText("Matching Calibration Circles in the image");
            }
        });
        PointPairMatch circles = new PointPairMatch(ellipsecenters);
        JProgressBar bar = new JProgressBar(0, 1);
        bar.setValue(bar.getMinimum());
        double distancesquaredthreshold = prefs.AlgorithmSettingPointPairMatchingDistanceThreshold * prefs.AlgorithmSettingPointPairMatchingDistanceThreshold;
        while (bar.getValue() < bar.getMaximum()) {
            bar = circles.BruteForchBarycentricMatchCircles(calibrationcirclecenters, distancesquaredthreshold);
            final JProgressBar temp = bar;
            try {
                EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        jProgressBar1.setMinimum(temp.getMinimum());
                        jProgressBar1.setMaximum(temp.getMaximum());
                        jProgressBar1.setValue(temp.getValue());
                    }
                });
            } catch (Exception e) {
                System.out.println("Exception in updating the progress bar" + e.getMessage());
            }
        }
        final String text4 = "Number of Matched Point Pairs in image " + (i + 1) + "=" + circles.getNumberofPointPairs() + "\n";
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                jTextAreaOutput.setText(jTextAreaOutput.getText() + text4);
            }
        });
        return circles;
    }

    private LensDistortion EstimatingCameraParameters(int i, PointPairMatch circles) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                jProgressBar1.setMinimum(0);
                jProgressBar1.setValue(jProgressBar1.getMinimum());
                jProgressBar1.setMaximum(prefs.AlgorithmSettingMaxBundleAdjustmentNumberOfIterations);
                jProgressBar2.setValue(jProgressBar2.getValue() + 1);
                jLabelProgressBar1.setText("Estimating and fine-tuning camera parameters");
            }
        });
        LensDistortion distortion = new LensDistortion();
        images[i].matchingpoints = circles.getMatchedPoints();
        if (!images[i].skipprocessing) distortion = IndividualCameraCalibration(i, prefs.AlgorithmSettingMaxBundleAdjustmentNumberOfIterations);
        if (!images[i].skipprocessing) {
            for (int j = 0; j < images[i].matchingpoints.length; j++) {
                images[i].matchingpoints[j].pointtwo = distortion.UndoThisLayerOfDistortion(images[i].matchingpoints[j].pointtwo);
            }
            final int progress = prefs.AlgorithmSettingMaxBundleAdjustmentNumberOfIterations;
            try {
                EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        jProgressBar1.setValue(progress);
                    }
                });
            } catch (Exception e) {
                System.out.println("Exception in updating the progress bar" + e.getMessage());
            }
        }
        return distortion;
    }

    private void UndoLensDistortion(int i, LensDistortion distortion, boolean colour) {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                jLabelProgressBar1.setText("Undoing lens distortion");
                jProgressBar1.setValue(jProgressBar1.getMinimum());
                jProgressBar2.setValue(jProgressBar2.getValue() + 1);
            }
        });
        if (!images[i].skipprocessing) images[i].NegateLensDistortion(distortion, jProgressBar1);
    }

    private void ImageSegmentation(int i) {
        EdgeExtraction2D edges = new EdgeExtraction2D(images[i]);
        edges.NonMaximalLocalSuppressionAndThresholdStrengthMap(prefs.AlgorithmSettingEdgeStrengthThreshold);
        edges.RemoveSpuriousPoints();
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                jLabelProgressBar1.setText("Finding visible portion of Calibration Sheet");
                jProgressBar1.setValue(jProgressBar1.getMinimum());
                jProgressBar2.setValue(jProgressBar2.getValue() + 1);
            }
        });
        Ellipse[] matchedellipses = new Ellipse[images[i].matchingpoints.length];
        for (int j = 0; j < images[i].matchingpoints.length; j++) {
            Ellipse tempellipse = standardcalibrationcircleonprintedsheet.clone();
            tempellipse.ResetCenter(images[i].matchingpoints[j].pointone);
            matchedellipses[j] = new Ellipse(images[i].getWorldtoImageTransformMatrix(), images[i].originofimagecoordinates, tempellipse, 360);
            matchedellipses[j].ResetCenter(images[i].matchingpoints[j].pointtwo);
        }
        Point2d[] corners = new Point2d[4];
        corners[0] = images[i].getWorldtoImageTransform(new Point3d(((double) (-calibrationsheetwidth) / 2) - 1, ((double) (-calibrationsheetheight) / 2) - 1, 0).ConvertPointTo4x1Matrix());
        corners[1] = images[i].getWorldtoImageTransform(new Point3d(((double) (-calibrationsheetwidth) / 2) - 1, ((double) (calibrationsheetheight) / 2) + 1, 0).ConvertPointTo4x1Matrix());
        corners[2] = images[i].getWorldtoImageTransform(new Point3d(((double) (calibrationsheetwidth) / 2) + 1, ((double) (calibrationsheetheight) / 2) + 1, 0).ConvertPointTo4x1Matrix());
        corners[3] = images[i].getWorldtoImageTransform(new Point3d(((double) (calibrationsheetwidth) / 2) + 1, ((double) (-calibrationsheetheight) / 2) - 1, 0).ConvertPointTo4x1Matrix());
        ImageSegmentation segmented = new ImageSegmentation(edges, corners, images[i].width, images[i].height);
        JProgressBar bar = new JProgressBar(0, 1);
        bar.setValue(bar.getMinimum());
        while (bar.getValue() < bar.getMaximum()) {
            bar = segmented.Segment(matchedellipses, images[i]);
            final JProgressBar temp = bar;
            try {
                EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        jProgressBar1.setMinimum(temp.getMinimum());
                        jProgressBar1.setMaximum(temp.getMaximum());
                        jProgressBar1.setValue(temp.getValue());
                    }
                });
            } catch (Exception e) {
                System.out.println("Exception in updating the progress bar" + e.getMessage());
            }
        }
        images[i].SetProcessedPixels(segmented.GetCalibrationSheet());
        if (prefs.Debug && prefs.DebugImageSegmentation) {
            segmented.SaveGreyscaleImageSegmentation(prefs.DebugSaveOutputImagesFolder + File.separatorChar + "SegmentedImage" + new File(images[i].filename).getName() + ".jpg");
            GraphicsFeedback graphics1 = new GraphicsFeedback(true);
            graphics1.ShowImage(images[i]);
            GraphicsFeedback graphics2 = new GraphicsFeedback(true);
            graphics2.ShowImage(images[i]);
            boolean[][] processedpixels = images[i].getProcessedPixels();
            PixelColour black = new PixelColour(PixelColour.StandardColours.Black);
            PixelColour white = new PixelColour(PixelColour.StandardColours.White);
            for (int x = 0; x < images[i].width; x++) for (int y = 0; y < images[i].height; y++) if (processedpixels[x][y]) graphics1.Print(x, y, white, 0, 0); else graphics2.Print(x, y, black, 0, 0);
            String filename = prefs.DebugSaveOutputImagesFolder + File.separatorChar + "SegmentedImageWhite" + new File(images[i].filename).getName() + ".jpg";
            graphics1.SaveImage(filename);
            filename = prefs.DebugSaveOutputImagesFolder + File.separatorChar + "SegmentedImageBlack" + new File(images[i].filename).getName() + ".jpg";
            graphics2.SaveImage(filename);
        }
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                jProgressBar1.setValue(jProgressBar1.getMinimum());
                jProgressBar2.setValue(jProgressBar2.getValue() + 1);
            }
        });
    }

    /**************************************************************************************************************************************************************************
	 * 
	 * Private methods invoked by without user interaction one after the other by the Object Estimation step
	 * These are primarily in different methods for tidyness only
	 *
	 **************************************************************************************************************************************************************************/
    private Voxel Voxelisation(AxisAlignedBoundingBox volumeofinterest) {
        Voxel rootvoxel = new Voxel();
        JProgressBar bar = new JProgressBar(0, 1);
        bar.setValue(bar.getMinimum());
        while (bar.getValue() < bar.getMaximum()) {
            bar = rootvoxel.Voxelise(images, volumeofinterest, prefs.AlgorithmSettingVolumeSubDivision, bar);
            final JProgressBar temp = bar;
            double voxelresolution = rootvoxel.getVoxelResolution();
            BigDecimal bigdecimal = new BigDecimal(voxelresolution, new MathContext(4, RoundingMode.DOWN));
            final String res = bigdecimal.toPlainString();
            try {
                EventQueue.invokeLater(new Runnable() {

                    public void run() {
                        jProgressBar1.setMinimum(temp.getMinimum());
                        jProgressBar1.setMaximum(temp.getMaximum());
                        jProgressBar1.setValue(temp.getValue());
                        jLabelProgressBar1.setText("Estimating Bounding Volume Surface using boxes with a maximum of " + res + "mm on a side.");
                    }
                });
            } catch (Exception e) {
                System.out.println("Exception in updating the progress bar" + e.getMessage());
            }
        }
        rootvoxel.RestrictSubVoxelsToGroundedOnes();
        final String text2 = "Object bounding volume surface estimated using " + rootvoxel.getNumberofSurfaceSubVoxels() + " boxes.\n";
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                jTextAreaOutput.setText(jTextAreaOutput.getText() + text2);
            }
        });
        return rootvoxel;
    }

    private void RestrictSearch(AxisAlignedBoundingBox[] surfacevoxels) {
        try {
            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    jProgressBar1.setValue(jProgressBar1.getMinimum());
                    jProgressBar2.setValue(jProgressBar2.getValue() + 1);
                    jLabelProgressBar1.setText("Restricting Search space");
                }
            });
        } catch (Exception e) {
            System.out.println("Exception in updating the progress bar" + e.getMessage());
        }
        JProgressBar bar = new JProgressBar(0, images.length * surfacevoxels.length);
        bar.setValue(0);
        for (int i = 0; i < images.length; i++) if (!images[i].skipprocessing) {
            images[i].SetAllPixelsToProcessed();
            for (int j = 0; j < surfacevoxels.length; j++) {
                images[i].setPixeltoUnProcessedIfVolumeOfInterestBackProjectsToIt(surfacevoxels[j]);
                bar.setValue(bar.getValue() + 1);
                final JProgressBar temp2 = bar;
                final String text = "Restricting Search space for image " + (i + 1) + " of " + images.length;
                try {
                    EventQueue.invokeLater(new Runnable() {

                        public void run() {
                            jProgressBar1.setMinimum(temp2.getMinimum());
                            jProgressBar1.setMaximum(temp2.getMaximum());
                            jProgressBar1.setValue(temp2.getValue());
                            jLabelProgressBar1.setText(text);
                        }
                    });
                } catch (Exception e) {
                    System.out.println("Exception in updating the progress bar" + e.getMessage());
                }
            }
            if (prefs.Debug && prefs.DebugRestrictedSearch) {
                String filename = prefs.DebugSaveOutputImagesFolder + File.separatorChar + "RestrictedSearchForImageBinaryImage" + new File(images[i].filename).getName() + ".jpg";
                GraphicsFeedback graphics = new GraphicsFeedback(true);
                boolean[][] processedpixels = images[i].getProcessedPixels();
                graphics.ShowBinaryimage(processedpixels, images[i].width, images[i].height);
                graphics.SaveImage(filename);
                graphics.ShowImage(images[i]);
                PixelColour black = new PixelColour(PixelColour.StandardColours.Black);
                for (int x = 0; x < images[i].width; x++) for (int y = 0; y < images[i].height; y++) {
                    if (processedpixels[x][y]) {
                        graphics.Print(x, y, black, 0, 0);
                    }
                }
                filename = prefs.DebugSaveOutputImagesFolder + File.separatorChar + "RestrictedSearchForImageColourImage" + new File(images[i].filename).getName() + ".jpg";
                graphics.SaveImage(filename);
            }
        }
        bar.setValue(bar.getValue() + 1);
        final JProgressBar temp2 = bar;
        try {
            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    jProgressBar1.setMinimum(temp2.getMinimum());
                    jProgressBar1.setMaximum(temp2.getMaximum());
                    jProgressBar1.setValue(temp2.getValue());
                }
            });
        } catch (Exception e) {
            System.out.println("Exception in updating the progress bar" + e.getMessage());
        }
    }

    private void GraphicsFeedback(int j) {
        if (prefs.Debug && prefs.DebugImageOverlay) {
            PixelColour colour;
            GraphicsFeedback graphics = new GraphicsFeedback(true);
            graphics.ShowImage(images[j]);
            if (!images[j].skipprocessing) {
                PointPair2D[] matchingpoints = images[j].matchingpoints.clone();
                colour = new PixelColour(PixelColour.StandardColours.White);
                for (int i = 0; i < matchingpoints.length; i++) {
                    Point2d temp = matchingpoints[i].pointtwo.clone();
                    graphics.PrintPoint(temp.x, temp.y, colour);
                }
                for (int i = 0; i < matchingpoints.length; i++) {
                    colour = new PixelColour(PixelColour.StandardColours.Teal);
                    Ellipse tempellipse = standardcalibrationcircleonprintedsheet.clone();
                    tempellipse.ResetCenter(matchingpoints[i].pointone);
                    Ellipse ellipse = new Ellipse(images[j].getWorldtoImageTransformMatrix(), images[j].originofimagecoordinates, tempellipse, 360);
                    Point2d temp = ellipse.GetCenter();
                    graphics.PrintPoint(temp.x, temp.y, colour);
                    graphics.OutlineEllipse(ellipse, colour, new Point2d(0, 0));
                }
                colour = new PixelColour(PixelColour.StandardColours.Teal);
                AxisAlignedBoundingBox volumebox = new AxisAlignedBoundingBox();
                volumebox.minx = -calibrationsheetwidth / 2;
                volumebox.maxx = calibrationsheetwidth / 2;
                volumebox.miny = -calibrationsheetheight / 2;
                volumebox.maxy = calibrationsheetheight / 2;
                volumebox.minz = 0;
                if (calibrationsheetwidth > calibrationsheetheight) volumebox.maxz = calibrationsheetwidth; else volumebox.maxz = calibrationsheetheight;
                Point3d[] vertices = volumebox.GetCornersof3DBoundingBox();
                for (int k = 4; k < 8; k++) {
                    Line3d line = new Line3d(vertices[k - 4], vertices[k]);
                    Line3d portion = images[j].GetPortionOfLineInFrontOfCamera(line);
                    Point3d upperend = portion.GetPointonLine(1);
                    if (upperend.z < volumebox.maxz) volumebox.maxz = upperend.z;
                }
                Point2d[] corners = volumebox.GetImageProjectionOfCornersof3DBoundingBox(images[j].originofimagecoordinates, images[j].getWorldtoImageTransformMatrix());
                int[][] pointpair = AxisAlignedBoundingBox.GetPointPairIndicesFor3DWireFrameLines();
                for (int i = 0; i < 12; i++) {
                    LineSegment2D line = new LineSegment2D(corners[pointpair[i][0]], corners[pointpair[i][1]]);
                    graphics.PrintLineSegment(line, colour);
                }
            }
            String filename = prefs.DebugSaveOutputImagesFolder + File.separatorChar + "ImageOverlay" + new File(images[j].filename).getName() + ".jpg";
            graphics.SaveImage(filename);
        }
    }

    /***********************************************************************************************************************************************
 * 
 * Other private methods
 * 
 ************************************************************************************************************************************************/
    private float[] GetGaussianFilter(double radius) {
        int r = (int) (Math.ceil(radius));
        int masksize = (r * 2 + 1) * (r * 2 + 1);
        float[] mask = new float[masksize];
        float sigma = r / 3;
        float total = 0;
        int index = 0;
        for (int i = -r; i <= r; i++) {
            for (int j = -r; j <= r; j++) {
                float distancesquared = (i * i) + (j * j);
                if (distancesquared > ((r * r) + 1)) mask[index] = 0; else {
                    mask[index] = (float) (Math.exp(-(distancesquared) / (2 * sigma * sigma)) / (tau * sigma * sigma));
                    total += mask[index];
                }
                index++;
            }
        }
        for (int i = 0; i < masksize; i++) mask[i] /= total;
        return mask;
    }

    private LensDistortion IndividualCameraCalibration(int n, int maxbundleadjustmentiterations) {
        CalibrateCamera camera;
        LensDistortion distortion = new LensDistortion();
        Point2d imagecenter = new Point2d(((double) images[n].width * 0.5), ((double) images[n].height * 0.5));
        images[n].originofimagecoordinates = imagecenter.clone();
        camera = new CalibrateCamera(images[n], maxbundleadjustmentiterations);
        Matrix K = camera.getCameraMatrix();
        double errorcode = K.get(2, 2);
        if (errorcode == 1) distortion = CalculateIndividualCameraCalibration(n, K, maxbundleadjustmentiterations);
        if ((camera.warnings != "") || (errorcode != 1)) {
            if (errorcode == -1) {
                images[n].skipprocessing = true;
                camera.warnings = camera.warnings + "Skipping further processing of this image.\n\n";
            }
            final String warnings = "Warnings for image " + (n + 1) + "\n" + camera.warnings;
            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    jTextAreaOutput.setText(jTextAreaOutput.getText() + warnings);
                }
            });
        }
        return distortion;
    }

    private LensDistortion CalculateIndividualCameraCalibration(int i, Matrix cameramatrix, int maxbundleadjustmentiterations) {
        PointPair2D[] newpointpairs = new PointPair2D[images[i].matchingpoints.length];
        for (int j = 0; j < newpointpairs.length; j++) {
            newpointpairs[j] = images[i].matchingpoints[j].clone();
            newpointpairs[j].pointtwo.minus(images[i].originofimagecoordinates);
        }
        CalibrateImage calibration = new CalibrateImage(newpointpairs, prefs.AlgorithmSettingMaxBundleAdjustmentNumberOfIterations);
        calibration.CalculateTranslationandRotation(cameramatrix);
        calibration.setZscalefactor(images[i].originofimagecoordinates, cameramatrix);
        if ((prefs.Debug) && (prefs.DebugCalibrationSheetPlanarHomographyEstimate)) {
            Matrix H = calibration.getHomography();
            Point2d offset = new Point2d(calibrationsheetwidth / 2, calibrationsheetheight / 2);
            PixelColour[][] newimage = new PixelColour[calibrationsheetpixelswidth][calibrationsheetpixelsheight];
            for (int x = 0; x < calibrationsheetpixelswidth; x++) {
                for (int y = 0; y < calibrationsheetpixelsheight; y++) {
                    Point2d point = new Point2d(x, y);
                    point.x = point.x * (calibrationsheetwidth / calibrationsheetpixelswidth);
                    point.y = point.y * (calibrationsheetheight / calibrationsheetpixelsheight);
                    point.minus(offset);
                    Point2d imagepoint = new Point2d(H.times(point.ConvertPointTo3x1Matrix()));
                    imagepoint.plus(images[i].originofimagecoordinates);
                    newimage[x][y] = images[i].InterpolatePixelColour(imagepoint);
                }
                if (x % 100 == 0) System.out.print(".");
            }
            System.out.println();
            int dx = (int) (calibrationsheetpixelswidth / 200);
            int dy = (int) (calibrationsheetpixelsheight / 200);
            for (int j = 0; j < images[i].matchingpoints.length; j++) {
                Point2d point = images[i].matchingpoints[j].pointone.clone();
                point.plus(offset);
                point.x = point.x * (calibrationsheetpixelswidth / calibrationsheetwidth);
                point.y = point.y * (calibrationsheetpixelsheight / calibrationsheetheight);
                for (int x = (int) point.x - dx; x < (int) point.x + dx; x++) for (int y = (int) point.y - dy; y < (int) point.y + dy; y++) if ((x >= 0) && (x < calibrationsheetpixelswidth) && (y >= 0) && (y <= calibrationsheetpixelsheight)) newimage[x][y] = new PixelColour((byte) 255);
            }
            GraphicsFeedback graphics = new GraphicsFeedback(true);
            graphics.ShowPixelColourArray(newimage, calibrationsheetpixelswidth, calibrationsheetpixelsheight);
            String filename = prefs.DebugSaveOutputImagesFolder + File.separatorChar + "PlanarHomographyCalibrationSheetEstimate" + new File(images[i].filename).getName() + ".jpg";
            graphics.SaveImage(filename);
        }
        double maxradiussquared = images[i].originofimagecoordinates.CalculateDistanceSquared(new Point2d(0, 0));
        if (images[i].originofimagecoordinates.CalculateDistanceSquared(new Point2d(images[i].width, 0)) > maxradiussquared) maxradiussquared = images[i].originofimagecoordinates.CalculateDistanceSquared(new Point2d(images[i].width, 0));
        if (images[i].originofimagecoordinates.CalculateDistanceSquared(new Point2d(0, images[i].height)) > maxradiussquared) maxradiussquared = images[i].originofimagecoordinates.CalculateDistanceSquared(new Point2d(0, images[i].height));
        if (images[i].originofimagecoordinates.CalculateDistanceSquared(new Point2d(images[i].width, images[i].height)) > maxradiussquared) maxradiussquared = images[i].originofimagecoordinates.CalculateDistanceSquared(new Point2d(images[i].width, images[i].height));
        Matrix K = cameramatrix.copy();
        Matrix R = calibration.getRotation();
        Matrix t = calibration.getTranslation();
        double z = calibration.getZscalefactor();
        Matrix Z = MatrixManipulations.getZscaleMatrix(z);
        Matrix P = MatrixManipulations.WorldToImageTransformMatrix(cameramatrix, R, t, Z);
        LensDistortion distortion = new LensDistortion(images[i].width / 10, images[i].height / 10, P, newpointpairs, false, maxradiussquared);
        distortion.CalculateDistortion();
        double k1 = distortion.getDistortionCoefficient();
        Point2d origin = images[i].originofimagecoordinates.clone();
        CalibrationBundleAdjustment Adjust = new CalibrationBundleAdjustment();
        Adjust.ellipse = standardcalibrationcircleonprintedsheet.clone();
        Adjust.stepsaroundcircle = prefs.AlgorithmSettingStepsAroundCircleCircumferenceForEllipseEstimationInBundleAdjustment;
        Adjust.BundleAdjustment(maxbundleadjustmentiterations, images[i].matchingpoints, K, R, t, z, k1, origin, jProgressBar1);
        distortion.SetDistortionCoefficient(k1, origin);
        images[i].originofimagecoordinates = origin.clone();
        Z = MatrixManipulations.getZscaleMatrix(z);
        P = MatrixManipulations.WorldToImageTransformMatrix(K, R, t, Z);
        images[i].setWorldtoImageTransformMatrix(P);
        return distortion;
    }

    private void OutputDebugTriangleOverlayImage(TrianglePlusVertexArray tripatches, int i, Image image, String filename) {
        GraphicsFeedback graphics = new GraphicsFeedback(true);
        graphics.ShowImage(image);
        Triangle3D[] triangles = tripatches.GetTriangleArray();
        Point3d[] vertices = tripatches.GetVertexArray();
        PixelColour[] colours = new PixelColour[triangles.length];
        for (int j = 0; j < triangles.length; j++) {
            colours[j] = new PixelColour(PixelColour.StandardColours.White);
            if (j > i) colours[j] = new PixelColour(PixelColour.StandardColours.Blue);
            if (j == i) colours[j] = new PixelColour(PixelColour.StandardColours.Red);
        }
        for (int j = 0; j < triangles.length; j++) {
            Point3d[] trianglevertices = new Point3d[3];
            int[] index = triangles[j].GetFace();
            for (int n = 0; n < 3; n++) trianglevertices[n] = vertices[index[n]];
            graphics.TintPolygon(trianglevertices, colours[j], image);
        }
        for (int j = 0; j < triangles.length; j++) {
            int[] index = triangles[j].GetFace();
            if (j < i) colours[j] = new PixelColour(PixelColour.StandardColours.Red);
            Line3d line1 = new Line3d(vertices[index[0]], vertices[index[1]]);
            Line3d line2 = new Line3d(vertices[index[1]], vertices[index[2]]);
            Line3d line3 = new Line3d(vertices[index[0]], vertices[index[2]]);
            graphics.PrintLineSegment(line1, colours[j], image);
            graphics.PrintLineSegment(line2, colours[j], image);
            graphics.PrintLineSegment(line3, colours[j], image);
        }
        int[] index = triangles[i].GetFace();
        Line3d line1 = new Line3d(vertices[index[0]], vertices[index[1]]);
        Line3d line2 = new Line3d(vertices[index[1]], vertices[index[2]]);
        Line3d line3 = new Line3d(vertices[index[0]], vertices[index[2]]);
        graphics.PrintLineSegment(line1, colours[i], image);
        graphics.PrintLineSegment(line2, colours[i], image);
        graphics.PrintLineSegment(line3, colours[i], image);
        graphics.SaveImage(filename);
    }

    private ParallelGroup defaulthorizontal(GroupLayout layout) {
        ParallelGroup temp = layout.createParallelGroup(GroupLayout.LEADING).add(layout.createSequentialGroup().addContainerGap(gap, gap).add(jLabelTitle)).add(GroupLayout.TRAILING, layout.createSequentialGroup().addContainerGap(gap, Short.MAX_VALUE).add(JButtonCancel).addPreferredGap(LayoutStyle.RELATED).add(JButtonPrevious).addPreferredGap(LayoutStyle.RELATED).add(JButtonNext).addContainerGap(gap, gap));
        return temp;
    }

    private ParallelGroup defaultvertical(GroupLayout layout) {
        ParallelGroup temp = layout.createParallelGroup(GroupLayout.BASELINE).add(layout.createSequentialGroup().addContainerGap(gap, gap).add(jLabelTitle).addContainerGap(gap, gap)).add(layout.createSequentialGroup().addContainerGap(gap, Short.MAX_VALUE).add(layout.createParallelGroup(GroupLayout.LEADING).add(JButtonCancel).add(JButtonPrevious).add(JButtonNext)).addContainerGap(gap, gap));
        return temp;
    }
}
