package jpatch.boundary;

import inyo.RtInterface;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.*;
import javax.swing.*;
import javax.imageio.*;
import javax.sound.sampled.*;
import javax.vecmath.*;
import patterns.TextureParser;
import buoy.event.*;
import buoy.widget.*;
import jpatch.entity.*;
import jpatch.renderer.*;
import jpatch.auxilary.*;
import jpatch.boundary.settings.PovraySettings;
import jpatch.boundary.settings.Settings;

public final class Animator extends BFrame {

    private static Animator INSTANCE;

    private ArrayList listObjects = new ArrayList();

    private BorderContainer content = new BorderContainer();

    private Camera camera1 = new Camera("Camera 1");

    private CameraViewport cameraViewport = new CameraViewport(camera1, listObjects);

    private PoseSliders poseSliders;

    private MotionCurveEditor motionCurveEditor;

    private MotionCurveDisplay motionCurveDisplay;

    private BSplitPane splitPane;

    private String strPrefix = "frame_";

    private BDialog propertiesDialog;

    private BTextField textStart;

    private BTextField textEnd;

    private BTextField textRate;

    private BTextField textModelDir;

    private BTextField textPrefix;

    private BTextArea textPov;

    private BTextArea textRib;

    private float fStart;

    private float fEnd;

    private float fPosition;

    private float fFramerate = 24;

    private Clip clip;

    private HashMap mapMotionCurves = new HashMap();

    private HashMap mapFilenames = new HashMap();

    private MotionKey activeKey;

    private MotionCurve activeCurve;

    private RenderExtension re = new RenderExtension(new String[] { "povray", "", "renderman", "" });

    private Animator() {
        if (jpatch.VersionInfo.release) {
            setTitle("JPatch Animator " + jpatch.VersionInfo.version);
        } else {
            setTitle("JPatch Animator " + jpatch.VersionInfo.version + " compiled " + jpatch.VersionInfo.compileTime);
        }
        INSTANCE = this;
        try {
            UIManager.setLookAndFeel(Settings.getInstance().lookAndFeelClassname);
            JPopupMenu.setDefaultLightWeightPopupEnabled(false);
        } catch (Exception e) {
        }
        fStart = fPosition = 0;
        fEnd = 10 * fFramerate - 1;
        camera1.setPosition(new Point3d(0f, 0f, -100f));
        mapMotionCurves.put(camera1, new MotionCurveSet.Camera(camera1));
        listObjects.add(camera1);
        poseSliders = new PoseSliders();
        motionCurveEditor = new MotionCurveEditor();
        motionCurveDisplay = motionCurveEditor.getMotionCurveDisplay();
        splitPane = new BSplitPane(BSplitPane.VERTICAL, cameraViewport.getWidget(), motionCurveEditor);
        splitPane.addEventLink(ValueChangedEvent.class, this, "rerenderViewports");
        BSplitPane splitPane2 = new BSplitPane(BSplitPane.HORIZONTAL, splitPane, poseSliders.getContent());
        setContent(splitPane2);
        setMenuBar(new AnimatorMainMenu());
        pack();
        addEventLink(WindowClosingEvent.class, this, "quit");
        setBounds(new java.awt.Rectangle(0, 0, 1000, 700));
        setVisible(true);
        poseSliders.init();
        rerenderViewports();
        setActiveObject(camera1);
        new MainFrame(new Model());
    }

    public void NEW() {
        listObjects.clear();
        mapMotionCurves.clear();
        fStart = fPosition = 0;
        fEnd = 10 * fFramerate - 1;
        strPrefix = "frame_";
        camera1.setOrientation(0, 0, 0);
        camera1.setFocalLength(50);
        camera1.setPosition(new Point3d(0f, 0f, -100f));
        mapMotionCurves.put(camera1, new MotionCurveSet.Camera(camera1));
        listObjects.add(camera1);
        setActiveObject(camera1);
        rerenderViewports();
    }

    public void setClip(Clip clip) {
        if (this.clip != null) this.clip.flush();
        this.clip = clip;
        clip.setMicrosecondPosition((long) (fPosition / fFramerate * 1000));
    }

    public Clip getClip() {
        return clip;
    }

    public static Animator getInstance() {
        return (INSTANCE == null) ? new Animator() : INSTANCE;
    }

    public ArrayList getObjectList() {
        return listObjects;
    }

    public void enumerateLights() {
        int num = 0;
        for (Iterator it = listObjects.iterator(); it.hasNext(); ) {
            Object o = it.next();
            if (o instanceof AnimLight) {
                ((AnimLight) o).setNumber(num++);
            }
        }
    }

    public float getStart() {
        return fStart;
    }

    public float getEnd() {
        return fEnd;
    }

    public float getPosition() {
        return fPosition;
    }

    public float getFramerate() {
        return fFramerate;
    }

    public void stop() {
        motionCurveEditor.stop();
    }

    public MotionKey getActiveKey() {
        return activeKey;
    }

    public MotionCurve getActiveCurve() {
        return activeCurve;
    }

    public void setActiveKey(MotionKey key) {
        activeKey = key;
    }

    public void setActiveCurve(MotionCurve curve) {
        activeCurve = curve;
    }

    public void setPosition(float position) {
        fPosition = position;
        System.out.println("position = " + position);
        for (Iterator it = listObjects.iterator(); it.hasNext(); ) {
            AnimObject animObject = (AnimObject) it.next();
            ((MotionCurveSet) mapMotionCurves.get(animObject)).setPosition(position);
            if (animObject instanceof AnimModel) {
                Model model = ((AnimModel) animObject).getModel();
                model.applyMorphs();
                model.setPose();
            }
        }
        motionCurveEditor.setPosition(position);
        poseSliders.moveSliders();
        poseSliders.repaint();
        rerenderViewports();
        if (clip != null && !clip.isRunning()) clip.setMicrosecondPosition((long) (fPosition / fFramerate * 1000));
    }

    public void redrawMotionCurveDisplay() {
        motionCurveDisplay.repaint();
    }

    public void reinitMotionCurveDisplay() {
        motionCurveDisplay.init();
        motionCurveEditor.repaint();
    }

    public AnimObject getActiveObject() {
        return poseSliders.getActiveObject();
    }

    public void setActiveObject(AnimObject object) {
        poseSliders.setActiveObject(object);
        rerenderViewports();
        reinitMotionCurveDisplay();
    }

    public void rerenderViewports() {
        cameraViewport.rerender();
    }

    public void repaintViewports() {
        cameraViewport.repaint();
    }

    public MotionCurveSet getMotionCurveSetFor(AnimObject animObject) {
        return (MotionCurveSet) mapMotionCurves.get(animObject);
    }

    public void setMotionCurveSetFor(AnimObject animObject, MotionCurveSet motionCurveSet) {
        mapMotionCurves.put(animObject, motionCurveSet);
    }

    public void setMorphValue(AnimModel animModel, Morph morph) {
        MotionCurve.Float mc = ((MotionCurveSet.Model) mapMotionCurves.get(animModel)).morph(morph);
        activeCurve = mc;
        activeKey = mc.setFloatAt(fPosition, morph.getValue());
        motionCurveEditor.repaint();
        rerenderViewports();
    }

    public void updateCurvesFor(AnimObject animObject) {
        ((MotionCurveSet) mapMotionCurves.get(animObject)).updateCurves(fPosition);
        motionCurveEditor.repaint();
    }

    public void addObject(AnimObject object) {
        addObject(object, null);
    }

    public void addObject(AnimObject object, String filename) {
        addObject(object, filename, MotionCurveSet.createMotionCurveSetFor(object));
    }

    public void addObject(AnimObject object, String filename, MotionCurveSet motionCurveSet) {
        listObjects.add(object);
        mapFilenames.put(object, filename);
        mapMotionCurves.put(object, motionCurveSet);
        if (object instanceof AnimLight) enumerateLights();
    }

    public void removeObject(AnimObject object) {
        if (object != camera1) {
            listObjects.remove(object);
            mapMotionCurves.remove(object);
            if (object == getActiveObject()) setActiveObject(camera1); else {
                poseSliders.init();
                rerenderViewports();
            }
            if (object instanceof AnimLight) enumerateLights();
        }
    }

    public Camera getActiveCamera() {
        return camera1;
    }

    public void preferences() {
        propertiesDialog = new BDialog(this, "Preferences", true);
        propertiesDialog.setResizable(false);
        textModelDir = new BTextField(Settings.getInstance().directories.jpatchFiles.getPath(), 20);
        FormContainer form = new FormContainer(3, 1);
        BButton buttonBrowse = new BButton("browse");
        form.add(new BLabel("Model directory:"), 0, 0);
        form.add(textModelDir, 1, 0);
        form.add(buttonBrowse, 2, 0);
        RowContainer buttons = new RowContainer();
        BButton buttonOK = new BButton("OK");
        BButton buttonCancel = new BButton("Cancel");
        buttons.add(buttonOK);
        buttons.add(buttonCancel);
        propertiesDialog.addEventLink(WindowClosingEvent.class, propertiesDialog, "dispose");
        buttonCancel.addEventLink(CommandEvent.class, propertiesDialog, "dispose");
        buttonOK.addEventLink(CommandEvent.class, this, "setPrefs");
        buttonBrowse.addEventLink(CommandEvent.class, this, "openFileChooser");
        ColumnContainer content = new ColumnContainer();
        content.add(form);
        content.add(buttons);
        propertiesDialog.setContent(content);
        propertiesDialog.pack();
        ((Window) propertiesDialog.getComponent()).setLocationRelativeTo(getComponent());
        propertiesDialog.setVisible(true);
    }

    public void properties() {
        propertiesDialog = new BDialog(this, "Sequence properties", true);
        propertiesDialog.setResizable(false);
        FormContainer form = new FormContainer(2, 4);
        form.add(new BLabel("Sequence start:"), 0, 0);
        form.add(new BLabel("Sequence end:"), 0, 1);
        form.add(new BLabel("Frame rate:"), 0, 2);
        form.add(new BLabel("File prefix:"), 0, 3);
        textStart = new BTextField("" + fStart, 20);
        textEnd = new BTextField("" + fEnd, 20);
        textRate = new BTextField("" + fFramerate, 20);
        textPrefix = new BTextField(strPrefix, 20);
        textPov = new BTextArea(re.getRenderString("povray", ""), 10, 50);
        textRib = new BTextArea(re.getRenderString("renderman", ""), 10, 50);
        form.add(textStart, 1, 0);
        form.add(textEnd, 1, 1);
        form.add(textRate, 1, 2);
        form.add(textPrefix, 1, 3);
        RowContainer buttons = new RowContainer();
        BButton buttonOK = new BButton("OK");
        BButton buttonCancel = new BButton("Cancel");
        buttons.add(buttonOK);
        buttons.add(buttonCancel);
        BTabbedPane tabbedPane = new BTabbedPane();
        tabbedPane.add(new BScrollPane(textPov), "POV-Ray");
        tabbedPane.add(new BScrollPane(textRib), "RenderMan");
        ColumnContainer content = new ColumnContainer();
        content.add(form);
        content.add(tabbedPane);
        content.add(buttons);
        propertiesDialog.addEventLink(WindowClosingEvent.class, propertiesDialog, "dispose");
        buttonCancel.addEventLink(CommandEvent.class, propertiesDialog, "dispose");
        buttonOK.addEventLink(CommandEvent.class, this, "setProperties");
        propertiesDialog.setContent(content);
        propertiesDialog.pack();
        ((Window) propertiesDialog.getComponent()).setLocationRelativeTo(getComponent());
        propertiesDialog.setVisible(true);
    }

    public void setRenderString(String format, String version, String renderString) {
        re.setRenderString(format, version, renderString);
    }

    public String getRenderString(String format, String version) {
        return re.getRenderString(format, version);
    }

    public StringBuffer renderStrings(String prefix) {
        return re.xml(prefix);
    }

    private void openFileChooser() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fileChooser.setSelectedFile(new File(textModelDir.getText()));
        if (fileChooser.showDialog(this.getComponent(), "Select") == JFileChooser.APPROVE_OPTION) {
            textModelDir.setText(fileChooser.getSelectedFile().getPath());
        }
    }

    private void setProperties() {
        try {
            fStart = Float.parseFloat(textStart.getText());
            fEnd = Float.parseFloat(textEnd.getText());
            fFramerate = Math.round(Float.parseFloat(textRate.getText()));
            re.setRenderString("povray", "", textPov.getText());
            re.setRenderString("renderman", "", textRib.getText());
            strPrefix = textPrefix.getText();
            propertiesDialog.dispose();
            reinitMotionCurveDisplay();
            motionCurveEditor.setPosition(fPosition);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void setPrefs() {
        Settings.getInstance().directories.jpatchFiles = new File(textModelDir.getText());
        Settings.getInstance().directories.save();
        propertiesDialog.dispose();
    }

    public void setPrefix(String prefix) {
        strPrefix = prefix;
    }

    public String getPrefix() {
        return strPrefix;
    }

    void parseTimesheet(String filename) {
        try {
            AnimObject animObject = getActiveObject();
            if (animObject instanceof AnimModel) {
                MotionCurveSet.Model mcs = ((MotionCurveSet.Model) mapMotionCurves.get(animObject));
                Model model = ((AnimModel) animObject).getModel();
                BufferedReader brFile = new BufferedReader(new FileReader(filename));
                String strLine;
                while ((strLine = brFile.readLine()) != null) {
                    String mpFrame = strLine.substring(0, 5);
                    String mpMouth = strLine.substring(25, (strLine.length() >= 38) ? 38 : strLine.length());
                    if (mpFrame.equals("Frame")) continue;
                    int frame = Integer.parseInt(mpFrame.replaceAll("^\\s*", "").replaceAll("\\s*$", ""));
                    Morph morph = model.getMorphFor(mpMouth.replaceAll("^\\s*", "").replaceAll("\\s*$", ""));
                    if (morph != null) {
                        for (Iterator it = model.getPhonemeMorphSet().iterator(); it.hasNext(); ) {
                            Morph m = (Morph) it.next();
                            MotionCurve.Float mc = mcs.morph(m);
                            mc.setFloatAt((float) (frame - 1), (m == morph) ? 1 : 0);
                        }
                    }
                }
                motionCurveEditor.repaint();
                rerenderViewports();
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    private void quit() {
        if (JOptionPane.showConfirmDialog(Animator.getInstance().getComponent(), "Are you sure?", "Quit JPatch", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            dispose();
            System.exit(0);
        }
    }

    public void renderCurrentFrame() {
        new ProgressDisplay((int) Math.round(fPosition + 1), (int) Math.round(fPosition + 1));
    }

    /**
	 * @param frameName
	 * @param progressDisplay
	 */
    public void renderFrame(String frameName, ProgressDisplay progressDisplay) {
        Settings settings = Settings.getInstance();
        ArrayList models = new ArrayList();
        ArrayList lights = new ArrayList();
        for (Iterator it = Animator.getInstance().getObjectList().iterator(); it.hasNext(); ) {
            Object o = it.next();
            if (o instanceof AnimModel) {
                models.add(o);
            }
            if (o instanceof AnimLight) {
                lights.add(o);
            }
        }
        switch(settings.export.rendererToUse) {
            case INYO:
                {
                    progressDisplay.clearText();
                    progressDisplay.addText("Working directory is: " + settings.export.workingDirectory + "\n");
                    progressDisplay.addText("Invoking Inyo...");
                    TextureParser.setTexturePath(settings.export.inyo.textureDirectory.getPath());
                    InyoRenderer3 renderer = new InyoRenderer3(models, Animator.getInstance().getActiveCamera(), lights);
                    Image image = renderer.render(new RtInterface());
                    if (image != null) {
                        File imageFile = new File(settings.export.workingDirectory, frameName + ".png");
                        try {
                            ImageIO.write((BufferedImage) image, "png", imageFile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        progressDisplay.loadImage(imageFile);
                    }
                }
                break;
            case RENDERMAN:
                {
                    RibRenderer4 renderer = new RibRenderer4();
                    File ribFile = new File(settings.export.workingDirectory, frameName + ".rib");
                    progressDisplay.clearText();
                    progressDisplay.addText("Working directory is: " + settings.export.workingDirectory + "\n");
                    progressDisplay.addText("Generating geometry...");
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(ribFile));
                        renderer.writeToFile(models, Animator.getInstance().getActiveCamera(), lights, writer, frameName + ".tif");
                        writer.close();
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                    String[] ribCmd = { settings.export.renderman.executable.getPath(), frameName + ".rib" };
                    String[] ribEnv = settings.export.renderman.environmentVariables.split(";");
                    File imageFile = new File(settings.export.workingDirectory, frameName + ".tif");
                    if (imageFile.exists()) imageFile.delete();
                    StringBuffer sb = new StringBuffer();
                    sb.append("Starting RenderMAN renderer using:\n");
                    for (int i = 0; i < ribCmd.length; sb.append(ribCmd[i++]).append(" ")) ;
                    sb.append("\n");
                    if (ribEnv != null) {
                        sb.append("Environment variables:\n");
                        for (int i = 0; i < ribEnv.length; sb.append(ribEnv[i++]).append("\n")) ;
                    }
                    sb.append("\n");
                    sb.append("--------------------------------------------------------------------------------\n");
                    progressDisplay.addText(sb.toString());
                    try {
                        Process rib = Runtime.getRuntime().exec(ribCmd, ribEnv, settings.export.workingDirectory);
                        new ProcessMonitor(rib, progressDisplay);
                        rib.waitFor();
                        if (settings.export.deletePerFrameFilesAfterRendering) ribFile.delete();
                        progressDisplay.loadImage(imageFile);
                    } catch (Exception exception) {
                        exception.printStackTrace();
                    }
                }
                break;
            case POVRAY:
                {
                    progressDisplay.clearText();
                    progressDisplay.addText("Working directory is: " + settings.export.workingDirectory + "\n");
                    progressDisplay.addText("Generating geometry...");
                    File povrayFile = new File(settings.export.workingDirectory, frameName + ".pov");
                    try {
                        PovrayRenderer3 renderer = new PovrayRenderer3();
                        BufferedWriter writer = new BufferedWriter(new FileWriter(povrayFile));
                        renderer.writeFrame(models, camera1, lights, re.getRenderString("povray", ""), writer);
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ArrayList listCmd = new ArrayList();
                    listCmd.add(settings.export.povray.executable);
                    if (settings.export.povray.version == PovraySettings.Version.UNIX) {
                        listCmd.add("+I" + frameName + ".pov");
                    } else {
                        listCmd.add("/RENDER");
                        listCmd.add(frameName + ".pov");
                        listCmd.add("/EXIT");
                    }
                    listCmd.add("+O" + frameName + ".png");
                    listCmd.add("+W" + settings.export.imageWidth);
                    listCmd.add("+H" + settings.export.imageHeight);
                    listCmd.add("-D");
                    listCmd.add("-P");
                    listCmd.add("+FN8");
                    switch(settings.export.povray.antialiasingMethod) {
                        case OFF:
                            listCmd.add("-A");
                            break;
                        case METHOD_1:
                            listCmd.add("+A" + settings.export.povray.antialiasingThreshold);
                            listCmd.add("+AM 1");
                            listCmd.add("+R" + settings.export.povray.antialiasingLevel);
                            break;
                        case METHOD_2:
                            listCmd.add("+A" + settings.export.povray.antialiasingThreshold);
                            listCmd.add("+AM 2");
                            listCmd.add("+R" + settings.export.povray.antialiasingLevel);
                            break;
                    }
                    if (settings.export.povray.antialiasingJitter != 0) {
                        listCmd.add("+J" + settings.export.povray.antialiasingJitter);
                    } else {
                        listCmd.add("-J");
                    }
                    String[] povCmd = (String[]) listCmd.toArray(new String[0]);
                    String[] povEnv = settings.export.povray.environmentVariables.split(";");
                    StringBuffer sb = new StringBuffer();
                    sb.append("Starting POV-Ray renderer using:\n");
                    for (int i = 0; i < povCmd.length; sb.append(povCmd[i++]).append(" ")) ;
                    sb.append("\n");
                    if (povEnv != null) {
                        sb.append("Environment variables:\n");
                        for (int i = 0; i < povEnv.length; sb.append(povEnv[i++]).append("\n")) ;
                    }
                    sb.append("\n");
                    sb.append("--------------------------------------------------------------------------------\n");
                    progressDisplay.addText(sb.toString());
                    File imageFile = new File(settings.export.workingDirectory, frameName + ".png");
                    if (imageFile.exists()) imageFile.delete();
                    try {
                        Process pov = Runtime.getRuntime().exec(povCmd, povEnv, settings.export.workingDirectory);
                        progressDisplay.setRendererProcess(pov);
                        new ProcessMonitor(pov, progressDisplay);
                        pov.waitFor();
                        if (settings.export.deletePerFrameFilesAfterRendering) povrayFile.delete();
                        progressDisplay.loadImage(imageFile);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
        }
    }

    public void renderingDone(Image image) {
    }

    public void renderAnimation() {
        final BDialog dialog = new BDialog(this, "Render animation", true);
        dialog.setResizable(false);
        final BTextField textFirst = new BTextField("1", 20);
        final BTextField textLast = new BTextField("" + ((int) (Math.round(fEnd - fStart)) + 1), 20);
        FormContainer form = new FormContainer(2, 2);
        form.add(new BLabel("First frame:"), 0, 0);
        form.add(new BLabel("Last frame:"), 0, 1);
        form.add(textFirst, 1, 0);
        form.add(textLast, 1, 1);
        RowContainer buttons = new RowContainer();
        BButton buttonOK = new BButton("OK");
        BButton buttonCancel = new BButton("Cancel");
        buttons.add(buttonOK);
        buttons.add(buttonCancel);
        dialog.addEventLink(WindowClosingEvent.class, dialog, "dispose");
        buttonCancel.addEventLink(CommandEvent.class, dialog, "dispose");
        buttonOK.addEventLink(CommandEvent.class, new Object() {

            private void processEvent() {
                try {
                    int iFirst = Integer.parseInt(textFirst.getText());
                    int iLast = Integer.parseInt(textLast.getText());
                    dialog.dispose();
                    new ProgressDisplay((int) Math.round(fStart + iFirst), (int) Math.round(fStart + iLast));
                } catch (NumberFormatException e) {
                }
            }
        });
        ColumnContainer content = new ColumnContainer();
        content.add(form);
        content.add(buttons);
        dialog.setContent(content);
        dialog.pack();
        ((Window) dialog.getComponent()).setLocationRelativeTo(getComponent());
        dialog.setVisible(true);
    }

    public StringBuffer xml() {
        StringBuffer sb = new StringBuffer();
        sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>").append("\n");
        sb.append("<sequence>").append("\n");
        sb.append("\t<name>New Sequence</name>").append("\n");
        sb.append("\t<start>" + fStart + "</start>").append("\n");
        sb.append("\t<end>" + fEnd + "</end>").append("\n");
        sb.append("\t<framerate>" + fFramerate + "</framerate>").append("\n");
        sb.append("\t<prefix>" + strPrefix + "</prefix>").append("\n");
        sb.append(renderStrings("\t"));
        for (Iterator it = listObjects.iterator(); it.hasNext(); ) {
            Object o = it.next();
            if (o instanceof Camera) {
                Camera cam = (Camera) o;
                sb.append("\t<camera>").append("\n");
                sb.append("\t\t<name>" + cam.getName() + "</name>").append("\n");
                getMotionCurveSetFor(cam).xml(sb, "\t\t");
                sb.append("\t</camera>").append("\n");
            }
        }
        for (Iterator it = listObjects.iterator(); it.hasNext(); ) {
            Object o = it.next();
            if (o instanceof AnimLight) {
                AnimLight light = (AnimLight) o;
                sb.append("\t<lightsource>").append("\n");
                sb.append("\t\t<name>" + light.getName() + "</name>").append("\n");
                if (!light.isActive()) sb.append("\t\t<inactive/>").append("\n");
                sb.append(light.renderStrings("\t\t"));
                getMotionCurveSetFor(light).xml(sb, "\t\t");
                sb.append("\t</lightsource>").append("\n");
            }
        }
        for (Iterator it = listObjects.iterator(); it.hasNext(); ) {
            Object o = it.next();
            if (o instanceof AnimModel) {
                AnimModel animModel = (AnimModel) o;
                String filename = (String) mapFilenames.get(animModel);
                sb.append("\t<model>").append("\n");
                sb.append("\t\t<name>" + animModel.getName() + "</name>").append("\n");
                sb.append("\t\t<filename>" + filename + "</filename>").append("\n");
                int sdo = animModel.getSubdivisionOffset();
                if (sdo != 0) {
                    sb.append("\t\t<subdivisionoffset>" + sdo + "</subdivisionoffset>").append("\n");
                }
                sb.append(animModel.renderStrings("\t\t"));
                getMotionCurveSetFor(animModel).xml(sb, "\t\t");
                sb.append("\t</model>").append("\n");
            }
        }
        sb.append("</sequence>").append("\n");
        return sb;
    }

    public void setFramerate(float fps) {
        fFramerate = fps;
    }

    public void setStart(float start) {
        fStart = start;
    }

    public void setEnd(float end) {
        fEnd = end;
    }

    static class ImagePanel extends JPanel {

        /**
		 * 
		 */
        private static final long serialVersionUID = 1L;

        private Image image;

        ImagePanel() {
            setImage(new BufferedImage(Settings.getInstance().export.imageWidth, Settings.getInstance().export.imageWidth, BufferedImage.TYPE_INT_RGB));
        }

        void setImage(Image image) {
            this.image = image;
            setPreferredSize(new Dimension(image.getWidth(null), image.getHeight(null)));
            repaint();
        }

        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (image != null) g.drawImage(image, 0, 0, this);
        }
    }

    class ProgressDisplay extends BDialog {

        private ColumnContainer columnContainer = new ColumnContainer();

        private ImagePanel imagePanel;

        private BProgressBar progressBar = new BProgressBar();

        private BButton buttonAbort = new BButton("Abort");

        private BButton buttonClose = new BButton("Close");

        private BTextArea textArea = new BTextArea(10, 80);

        private BScrollBar scrollBar;

        private OverlayContainer overlayContainer = new OverlayContainer();

        private final int frames;

        private volatile StopableThread rendererThread;

        private volatile Process rendererProcess;

        ProgressDisplay(final int start, int stop) {
            super(Animator.getInstance(), true);
            frames = stop - start + 1;
            imagePanel = new ImagePanel();
            progressBar.setMinimum(0);
            progressBar.setMaximum(frames);
            textArea.setWrapStyle(BTextArea.WRAP_WORD);
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            textArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
            textArea.setEditable(false);
            textArea.setBackground(Color.BLACK);
            textArea.getComponent().setForeground(Color.GREEN);
            BScrollPane scrollPane = new BScrollPane(textArea, BScrollPane.SCROLLBAR_NEVER, BScrollPane.SCROLLBAR_ALWAYS);
            scrollBar = scrollPane.getVerticalScrollBar();
            LayoutInfo layoutInfo = new LayoutInfo(LayoutInfo.CENTER, LayoutInfo.HORIZONTAL);
            overlayContainer.add(buttonAbort);
            overlayContainer.add(buttonClose);
            buttonClose.setVisible(false);
            columnContainer.add(progressBar, layoutInfo);
            columnContainer.add(new AWTWidget(imagePanel));
            columnContainer.add(scrollPane, layoutInfo);
            columnContainer.add(overlayContainer);
            buttonAbort.addEventLink(CommandEvent.class, this, "abort");
            buttonClose.addEventLink(CommandEvent.class, this, "dispose");
            addEventLink(WindowClosingEvent.class, this, "close");
            setContent(columnContainer);
            pack();
            setResizable(false);
            rendererThread = new StopableThread() {

                public void run() {
                    running = true;
                    for (int f = 0; f < frames && running; f++) {
                        int fn = start + f - (int) (Math.round(fStart));
                        setTitle("Rendering frame " + fn + " (" + (f + 1) + " of " + frames + ")");
                        progressBar.setValue(f);
                        setPosition(start + f - 1);
                        String frameName = strPrefix + ((fn < 10) ? "00000" + fn : (fn < 100) ? "0000" + fn : (fn < 1000) ? "000" + fn : (fn < 10000) ? "00" + fn : (fn < 100000) ? "0" + fn : "" + fn);
                        renderFrame(frameName, ProgressDisplay.this);
                    }
                    running = false;
                    rendererThread = null;
                    rendererProcess = null;
                    done();
                    setTitle("Done. " + frames + " frames rendered.");
                }
            };
            rendererThread.start();
            setVisible(true);
        }

        class StopableThread extends Thread {

            volatile boolean running;
        }

        void setImage(Image image) {
            imagePanel.setImage(image);
        }

        void loadImage(File imageFile) {
            if (rendererThread != null && rendererThread.running) {
                if (imageFile.exists()) {
                    try {
                        Image image;
                        if (imageFile.getPath().endsWith(".tif") && !ImageIO.getImageReadersByFormatName("tiff").hasNext()) {
                            addText("");
                            addText("********************************************************************************");
                            addText("The renderer generated a tiff image, but no ImageIO tiff-reader is available.");
                            addText("Plase install the Java Advanced Imagige (JAI) IO Tools package available from Sun Microsystems at http://java.sun.com/products/java-media/jai/current.html");
                            addText("********************************************************************************");
                            addText("");
                            addText("Attempting to load the tiff image with JPatch's builtin tiff-reader (which can't handle compressed files)...");
                            addText("");
                            image = (new ReadTiff()).loadImage(imageFile);
                        } else image = ImageIO.read(imageFile);
                        Graphics g = image.getGraphics();
                        g.setColor(Color.BLACK);
                        g.drawString(imageFile.getParent() + File.separator, 8, 16);
                        g.drawString(imageFile.getName(), 8, 32);
                        g.setColor(Color.WHITE);
                        g.drawString(imageFile.getParent() + File.separator, 7, 15);
                        g.drawString(imageFile.getName(), 7, 31);
                        imagePanel.setImage(image);
                    } catch (Exception e) {
                        Settings settings = Settings.getInstance();
                        Image image = new BufferedImage(settings.export.imageWidth, settings.export.imageHeight, BufferedImage.TYPE_INT_RGB);
                        Graphics g = image.getGraphics();
                        g.setColor(Color.RED);
                        g.drawString("Can't display image - error reading file", 8, 16);
                        imagePanel.setImage(image);
                    }
                } else {
                    Settings settings = Settings.getInstance();
                    Image image = new BufferedImage(settings.export.imageWidth, settings.export.imageHeight, BufferedImage.TYPE_INT_RGB);
                    Graphics g = image.getGraphics();
                    g.setColor(Color.RED);
                    g.drawString("Can't display image - file not found", 8, 16);
                    imagePanel.setImage(image);
                }
            }
        }

        void setRendererProcess(Process process) {
            rendererProcess = process;
        }

        private void abort() {
            if (rendererThread != null) {
                rendererThread.running = false;
                rendererThread.stop();
            }
            if (rendererProcess != null) rendererProcess.destroy();
            setTitle("Rendering aborted");
            done();
        }

        private void close() {
            if (rendererThread != null && rendererThread.running) {
                if (JOptionPane.showConfirmDialog(getComponent(), "Do you want to stop the rendering process?", "Confirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                    rendererThread.running = false;
                    if (rendererProcess != null) rendererProcess.destroy();
                    dispose();
                }
            } else dispose();
        }

        void done() {
            buttonAbort.setVisible(false);
            buttonClose.setVisible(true);
            progressBar.setValue(progressBar.getMaximum());
        }

        void clearText() {
            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    textArea.setText("");
                }
            });
        }

        void addText(final String line) {
            EventQueue.invokeLater(new Runnable() {

                public void run() {
                    textArea.append(line);
                    textArea.append("\n");
                    scrollBar.setValue(scrollBar.getMaximum());
                }
            });
        }
    }

    class ProcessMonitor {

        ProcessMonitor(final Process process, final ProgressDisplay progressDisplay) {
            (new Thread() {

                public void run() {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    try {
                        while ((line = br.readLine()) != null) progressDisplay.addText(line);
                    } catch (IOException e) {
                        progressDisplay.addText("*** ABORTED ***");
                    }
                }
            }).start();
            (new Thread() {

                public void run() {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    try {
                        while ((line = br.readLine()) != null) progressDisplay.addText(line);
                    } catch (IOException e) {
                        progressDisplay.addText("*** ABORTED ***");
                    }
                }
            }).start();
        }
    }
}
