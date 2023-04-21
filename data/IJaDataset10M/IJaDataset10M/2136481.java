package freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mediaPool.elementsContentsPanel;

import freestyleLearning.learningUnitViewAPI.*;
import freestyleLearning.learningUnitViewAPI.elementsContentsPanel.*;
import freestyleLearning.learningUnitViewAPI.events.learningUnitEvent.*;
import freestyleLearning.learningUnitViewAPI.events.learningUnitViewEvent.*;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mediaPool.data.xmlBindingSubclasses.*;
import freestyleLearningGroup.independent.util.*;
import freestyleLearningGroup.independent.gui.*;
import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * Abstract class generalizing features for all specific mediaPool content panels
 * @author Freestyle Learning Group
 */
abstract class FLGMediaPoolAbstractElementContentPanel extends FSLAbstractLearningUnitViewElementContentPanel {

    protected FLGInternationalization internationalization;

    protected JComponent[] editToolBarComponents;

    protected JColorChooser colorChooser;

    protected Color presentBGColor;

    protected Color selectedColor;

    protected String activeLearningUnitViewElementId;

    protected boolean scaleToFit;

    protected boolean bgColorForAllElements = true;

    protected String addFileString;

    protected String removeFileString;

    protected String backgroundColorString;

    protected javax.swing.filechooser.FileFilter fileSelectionFilter;

    protected FLGImageComponent imageComponent;

    protected JCheckBox checkBox_toggleBGColorOptions;

    private JFileChooser fileChooser;

    /**
     * Initialize for all special media content panels.
     * Initialization includes
     *   - internationalization
     *   - editToolbarComponents for additional files / features
     */
    public void init(FSLLearningUnitViewManager learningUnitViewManager, FSLLearningUnitEventGenerator learningUnitEventGenerator, boolean editMode) {
        super.init(learningUnitViewManager, learningUnitEventGenerator, editMode);
        internationalization = new FLGInternationalization("freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mediaPool.elementsContentsPanel.internationalization", getClass().getClassLoader());
        learningUnitViewManager.addLearningUnitViewListener(new AbstractMediaPoolElementContentPanel_Adapter());
        fileSelectionFilter = new FLGMediaPoolImageFileFilter();
        presentBGColor = (Color) UIManager.get("FSLMainFrameColor1");
        colorChooser = new JColorChooser();
        addFileString = internationalization.getString("button.addImageFile.tooltip");
        removeFileString = internationalization.getString("button.removeImageFile.tooltip");
        backgroundColorString = internationalization.getString("button.backgroundColor.tooltip");
    }

    /**
     * Creates standard transparent content panel
     * and add/remove editToolBarButtons for adding/removing images.
     * Labels for editToolBarButtons may be changed by defining own values to
     * <code>addFileString</code> and <code>addFileString</code> properties 
     * in <code>init()</code> method.
     */
    protected void buildIndependentUI() {
        fileChooser = new JFileChooser();
        editToolBarComponents = new FLGEditToolBarButton[3];
        editToolBarComponents[0] = new FLGEditToolBarButton(loadImage("buttonAddFile.gif"), addFileString, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                addFile();
                buildDependentUI(true);
            }
        });
        editToolBarComponents[1] = new FLGEditToolBarButton(loadImage("buttonRemoveFile.gif"), removeFileString, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                removeFile();
                buildDependentUI(true);
            }
        });
        editToolBarComponents[2] = new FLGEditToolBarButton(loadImage("buttonBGColor.gif"), backgroundColorString, new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                selectedColor = showColorChooserDialog();
                String activeId = learningUnitViewManager.getActiveLearningUnitViewElementId();
                FLGMediaPoolElement activeElement = (FLGMediaPoolElement) learningUnitViewElementsManager.getLearningUnitViewElement(activeId, true);
                activeElement.setBackgroundColor(Integer.toString(selectedColor.getRGB()));
                activeElement.setModified(true);
                if (bgColorForAllElements) {
                    setElementsColor(selectedColor);
                }
                learningUnitViewElementsManager.setModified(true);
                buildDependentUI(true);
            }
        });
        editToolBarComponents[0].setEnabled(false);
        editToolBarComponents[1].setEnabled(false);
    }

    protected void setElementsColor(Color selectedColor) {
        String[] topLevelIds = learningUnitViewElementsManager.getTopLevelLearningUnitViewElementsIds();
        for (int i = 0; i < topLevelIds.length; i++) {
            FLGMediaPoolElement element = (FLGMediaPoolElement) learningUnitViewElementsManager.getLearningUnitViewElement(topLevelIds[i], true);
            element.setBackgroundColor(Integer.toString(selectedColor.getRGB()));
            element.setModified(true);
            String[] childrenIds = learningUnitViewElementsManager.getChildrenIdsOfLearningUnitViewElement(topLevelIds[i]);
            for (int j = 0; j < childrenIds.length; j++) {
                FLGMediaPoolElement child_element = (FLGMediaPoolElement) learningUnitViewElementsManager.getLearningUnitViewElement(childrenIds[i], true);
                child_element.setBackgroundColor(Integer.toString(selectedColor.getRGB()));
                child_element.setModified(true);
            }
        }
    }

    /**
     * Select and set background color.
     * @return <code>Color</color> background color
     */
    protected Color showColorChooserDialog() {
        bgColorForAllElements = false;
        JPanel dialogPanel = new JPanel();
        dialogPanel.setLayout(new FLGColumnLayout());
        dialogPanel.setOpaque(false);
        dialogPanel.setBorder(BorderFactory.createTitledBorder(internationalization.getString("border.colorChooser.title")));
        JPanel dialogInnerPanel = new JPanel();
        dialogInnerPanel.setLayout(new FlowLayout());
        dialogInnerPanel.setOpaque(false);
        final JButton colorButton = new JButton();
        try {
            String presentLAF = UIManager.getLookAndFeel().getClass().getName();
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
            SwingUtilities.updateComponentTreeUI(colorButton);
            UIManager.setLookAndFeel(presentLAF);
        } catch (Exception e) {
            System.out.println(e);
        }
        colorButton.setBackground(presentBGColor);
        colorButton.setBorder(BorderFactory.createLoweredBevelBorder());
        colorButton.setPreferredSize(new Dimension(40, 20));
        String activeId = learningUnitViewManager.getActiveLearningUnitViewElementId();
        final FLGMediaPoolElement activeElement = (FLGMediaPoolElement) learningUnitViewElementsManager.getLearningUnitViewElement(activeId, true);
        if (activeElement.getBackgroundColor() != null) {
            colorButton.setBackground(new Color(Integer.parseInt(activeElement.getBackgroundColor())));
            presentBGColor = new Color(Integer.parseInt(activeElement.getBackgroundColor()));
        }
        colorButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                Color selectedColor = colorChooser.showDialog(null, internationalization.getString("dialog.colorChooser.title"), presentBGColor);
                if (selectedColor != null) colorButton.setBackground(selectedColor);
            }
        });
        checkBox_toggleBGColorOptions = new JCheckBox(internationalization.getString("checkbox.setColorOptions.label"), true);
        checkBox_toggleBGColorOptions.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                bgColorForAllElements = checkBox_toggleBGColorOptions.isSelected();
            }
        });
        checkBox_toggleBGColorOptions.setSelected(false);
        final JButton resetButton = new JButton(internationalization.getString("button.resetBackground.label"));
        resetButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                colorButton.setBackground((Color) UIManager.get("FSLMainFrameColor1"));
            }
        });
        dialogInnerPanel.add(new JLabel(internationalization.getString("label.presentColor.text")));
        dialogInnerPanel.add(colorButton);
        dialogInnerPanel.add(resetButton);
        dialogPanel.add(dialogInnerPanel, FLGColumnLayout.LEFTEND);
        FLGOptionPane colorChooserDialog = new FLGOptionPane(null, dialogPanel, internationalization.getString("dialog.colorChooser.title"), FLGOptionPane.OK_CANCEL_OPTION, FLGOptionPane.DEFAULT_OPTION);
        if (FLGOptionPane.showDialog(colorChooserDialog) == FLGOptionPane.OK_OPTION) {
            setOpaque(true);
            presentBGColor = colorButton.getBackground();
            setBackground(presentBGColor);
        } else {
            setOpaque(true);
            presentBGColor = colorButton.getBackground();
        }
        repaint();
        return presentBGColor;
    }

    /**
     * Implementation for showing additional image files.
     */
    protected void buildDependentUI(boolean reloadIfAlreadyLoaded) {
        if (learningUnitViewElementsManager != null) {
            FLGMediaPoolElement mediaPoolElement = (FLGMediaPoolElement) learningUnitViewElementsManager.getLearningUnitViewElement(learningUnitViewElementId, false);
            if (mediaPoolElement != null) {
                if (mediaPoolElement.getType().equals("audio")) {
                    editToolBarComponents[0].setEnabled(true);
                    editToolBarComponents[1].setEnabled(true);
                }
                if (mediaPoolElement.getAdditionalFileName() != null) {
                    createImage();
                    layoutImage();
                } else {
                    removeAll();
                    repaint();
                }
                if (mediaPoolElement.getBackgroundColor() != null) {
                    setBackground(new Color(Integer.valueOf(mediaPoolElement.getBackgroundColor())));
                } else {
                    setBackground((Color) UIManager.get("FSLMainFrameColor1"));
                }
            } else {
                removeAll();
                repaint();
                setBackground((Color) UIManager.get("FSLMainFrameColor1"));
            }
        }
    }

    protected void createImage() {
        FLGUIUtilities.startLongLastingOperation();
        imageComponent = new FLGImageComponent(true, scaleToFit);
        if (learningUnitViewElementsManager != null) {
            FLGMediaPoolElement mediaPoolElement = (FLGMediaPoolElement) learningUnitViewElementsManager.getLearningUnitViewElement(learningUnitViewElementId, false);
            if (mediaPoolElement != null) {
                File imageFile = learningUnitViewElementsManager.resolveRelativeFileName(mediaPoolElement.getAdditionalFileName(), mediaPoolElement);
                try {
                    Image image = FLGImageUtility.loadImageAndWait(imageFile.toURL());
                    imageComponent.setImage(image);
                } catch (Exception e) {
                    System.out.println(e);
                }
                imageComponent.setOpaque(false);
                layoutImage();
            }
        }
        FLGUIUtilities.stopLongLastingOperation();
    }

    protected void layoutImage() {
        removeAll();
        if (imageComponent != null) {
            if (scaleToFit) {
                setLayout(new BorderLayout());
                add(imageComponent);
            } else {
                imageComponent.setOpaque(false);
                JPanel picturePanel = new JPanel(new FLGSingleLayout(FLGSingleLayout.CENTER, FLGSingleLayout.CENTER, FLGSingleLayout.SHRINK_AS_NEEDED, FLGSingleLayout.SHRINK_AS_NEEDED, true));
                picturePanel.setOpaque(false);
                picturePanel.add(imageComponent);
                JScrollPane scrollPane = new JScrollPane(picturePanel);
                scrollPane.setOpaque(false);
                setLayout(new GridLayout(1, 1));
                scrollPane.getViewport().setOpaque(false);
                scrollPane.setBorder(BorderFactory.createEmptyBorder());
                add(scrollPane);
            }
        }
    }

    /**
     * Load image from /images package directory
     * @param <code>imageFileName</code> name of image file (no path)
     * @return <code>Image</code> Image loaded from file
     */
    protected Image loadImage(String imageFileName) {
        return FLGImageUtility.loadImageAndWait(getClass().getResource("images/" + imageFileName));
    }

    /** 
     * Performes action associated with remove file button
     * May be overwritten by specific subclasses.
     */
    protected void removeFile() {
        FLGMediaPoolElement mediaPoolElement = (FLGMediaPoolElement) learningUnitViewElementsManager.getLearningUnitViewElement(activeLearningUnitViewElementId, false);
        mediaPoolElement.setAdditionalFileName(null);
        learningUnitViewElementsManager.setModified(true);
    }

    /** 
     * Performes action associated with add file button
     * May be overwritten by specific subclasses.
     */
    protected void addFile() {
        File[] fileList = showAddFileDialog(fileSelectionFilter);
        FLGUIUtilities.startLongLastingOperation();
        FLGMediaPoolElement mediaPoolElement = (FLGMediaPoolElement) learningUnitViewElementsManager.getLearningUnitViewElement(activeLearningUnitViewElementId, false);
        for (int i = 0; i < fileList.length; i++) {
            if (!fileList[i].isDirectory()) {
                File sourceFile = fileList[i];
                String extension = sourceFile.getName().substring(sourceFile.getName().lastIndexOf('.'));
                File destinationFile = learningUnitViewElementsManager.createNewFileForElementsExternalData("additionalFile", extension, activeLearningUnitViewElementId);
                FLGFileUtility.copyFile(sourceFile, destinationFile);
                FLGMediaPoolElement newMediaPoolElement = (FLGMediaPoolElement) learningUnitViewElementsManager.getLearningUnitViewElement(activeLearningUnitViewElementId, false);
                newMediaPoolElement.setAdditionalFileName(destinationFile.getName());
                learningUnitViewElementsManager.setModified(true);
            }
        }
        FLGUIUtilities.stopLongLastingOperation();
    }

    private File[] showAddFileDialog(javax.swing.filechooser.FileFilter filter) {
        File[] selectedFiles = new File[0];
        fileChooser.setFileFilter(filter);
        fileChooser.setMultiSelectionEnabled(true);
        if (fileChooser.showOpenDialog(FLGUIUtilities.getMainFrame()) == JFileChooser.APPROVE_OPTION) {
            selectedFiles = fileChooser.getSelectedFiles();
        }
        return selectedFiles;
    }

    protected JComponent[] getEditToolBarComponents() {
        return editToolBarComponents;
    }

    /** 
     * Check if file extension matches filter array.
     * May be accessed from subclasses implementing specific filter classes.
     * @param <code>extension</code> file extension String
     * @param <code>extension</code> file extension String
     * @return <code>true</code> if matches
     */
    protected boolean match(String extension, String[] filterExtensions) {
        for (int i = 0; i < filterExtensions.length; i++) {
            if (extension.equalsIgnoreCase(filterExtensions[i])) return true;
        }
        return false;
    }

    class FLGMediaPoolImageFileFilter extends javax.swing.filechooser.FileFilter {

        private String[] pictureExtensions = { ".gif", ".jpg", ".png" };

        public boolean accept(File file) {
            if (file.isDirectory()) return true;
            if (file.getName().lastIndexOf('.') > 0) {
                String extension = file.getName().substring(file.getName().lastIndexOf('.'));
                return match(extension, pictureExtensions);
            }
            return false;
        }

        public String getDescription() {
            return internationalization.getString("text.imageFileFilter.description");
        }
    }

    class AbstractMediaPoolElementContentPanel_Adapter extends FSLLearningUnitViewAdapter {

        public void learningUnitViewFullScreenModeSelected(FSLLearningUnitViewEvent event) {
            if (event.isFullScreenModeRequested()) {
            }
        }

        public void learningUnitViewScaleModeChanged(FSLLearningUnitViewEvent event) {
            scaleToFit = !scaleToFit;
            buildDependentUI(false);
        }

        public void learningUnitViewElementActivated(FSLLearningUnitViewEvent event) {
            activeLearningUnitViewElementId = event.getActiveLearningUnitViewElementId();
            FLGMediaPoolElement element = (FLGMediaPoolElement) learningUnitViewElementsManager.getLearningUnitViewElement(activeLearningUnitViewElementId, false);
            if (element.getBackgroundColor() != null) {
                presentBGColor = new Color(Integer.valueOf(element.getBackgroundColor()));
            } else {
                presentBGColor = (Color) UIManager.get("FSLMainFrameColor1");
            }
            buildDependentUI(false);
        }
    }
}
