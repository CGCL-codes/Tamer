package net.sourceforge.bricksviewer.application;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.JPopupMenu;
import net.sourceforge.bricksviewer.brick.*;
import net.sourceforge.bricksviewer.brick.io.JarSerializedBrickStyleLoader;
import net.sourceforge.bricksviewer.brick.io.LocalFileXMLBrickStyleLoader;
import net.sourceforge.bricksviewer.scene.*;
import net.sourceforge.bricksviewer.view.BricksViewerPanel;
import net.sourceforge.bricksviewer.view.Camera;

public class BricksViewerApplication extends JFrame {

    private boolean flushStyleCacheOnLoad;

    private BricksViewerPanel bricksView;

    private SceneContainer sceneContainer = null;

    private SceneLoader sceneLoader;

    private int instructionIndex;

    public BricksViewerApplication() throws MaterialLoadingException {
        super("BricksViewer");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        addMenus();
        bricksView = new BricksViewerPanel(640, 480);
        getContentPane().add(bricksView, BorderLayout.CENTER);
        pack();
        setVisible(true);
        sceneLoader = new SceneLoader();
        sceneLoader.registerBrickStyleLoader(new JarSerializedBrickStyleLoader());
        sceneLoader.registerBrickStyleLoader(new LocalFileXMLBrickStyleLoader());
        bricksView.startAnimation();
    }

    public static void main(String[] args) {
        BricksViewerApplication application;
        try {
            JPopupMenu.setDefaultLightWeightPopupEnabled(false);
            JFrame.setDefaultLookAndFeelDecorated(true);
            application = new BricksViewerApplication();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getFlushStyleCacheOnLoad() {
        return flushStyleCacheOnLoad;
    }

    public void setFlushStyleCacheOnLoad(boolean val) {
        flushStyleCacheOnLoad = val;
    }

    public void openLXFFile(File file) {
        Camera mainCamera;
        Map cameras;
        try {
            if (flushStyleCacheOnLoad) {
                sceneLoader.flushStyleCache();
            }
            sceneContainer = sceneLoader.loadSceneContainer(file);
            bricksView.setBricks(sceneContainer.getBricks());
            instructionIndex = sceneContainer.getNumberOfInstructions() - 1;
            cameras = sceneContainer.getCameras();
            mainCamera = (Camera) cameras.get("0");
            if (mainCamera != null) {
                bricksView.updateViewFromCamera(mainCamera);
            }
        } catch (SceneLoadingException e) {
            e.printStackTrace();
        }
    }

    private void addMenus() {
        JMenuBar menuBar = new JMenuBar();
        menuBar.setOpaque(true);
        menuBar.setPreferredSize(new Dimension(320, 20));
        ApplicationMenuHandler applicationMenuHandler = new ApplicationMenuHandler(this);
        menuBar.add(applicationMenuHandler.getMenu());
        FileMenuHandler fileMenuHandler = new FileMenuHandler(this);
        menuBar.add(fileMenuHandler.getMenu());
        InstructionsMenuHandler instructionsMenuHandler = new InstructionsMenuHandler(this);
        menuBar.add(instructionsMenuHandler.getMenu());
        AdvancedMenuHandler advancedMenuHandler = new AdvancedMenuHandler(this);
        menuBar.add(advancedMenuHandler.getMenu());
        setJMenuBar(menuBar);
    }

    protected void goToFirstInstructionStep() {
        instructionIndex = 0;
        updateInstructionVisibility();
    }

    protected void goToPreviousInstructionStep() {
        if (instructionIndex > 0) {
            instructionIndex--;
            updateInstructionVisibility();
        }
    }

    protected void goToNextInstructionStep() {
        if (instructionIndex < sceneContainer.getNumberOfInstructions() - 1) {
            instructionIndex++;
            updateInstructionVisibility();
        }
    }

    protected void goToLastInstructionStep() {
        instructionIndex = sceneContainer.getNumberOfInstructions() - 1;
        updateInstructionVisibility();
    }

    protected void updateInstructionVisibility() {
        Collection instructionSteps = sceneContainer.getInstructionSteps();
        int i = 0;
        Iterator it;
        InstructionStep step;
        it = instructionSteps.iterator();
        while (it.hasNext() && (i <= instructionIndex)) {
            step = (InstructionStep) it.next();
            setStepVisibility(step, true);
            i++;
        }
        while (it.hasNext()) {
            step = (InstructionStep) it.next();
            setStepVisibility(step, false);
        }
        bricksView.setNeedsRefresh();
    }

    protected void setStepVisibility(InstructionStep step, boolean visible) {
        Brick brick;
        BrickPart brickPart;
        Collection parts;
        Iterator it;
        parts = step.getParts();
        it = parts.iterator();
        while (it.hasNext()) {
            brickPart = (BrickPart) it.next();
            brick = brickPart.getBrick();
            brick.setVisible(visible);
        }
    }
}
