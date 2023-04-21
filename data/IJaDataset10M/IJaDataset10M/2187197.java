package freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mindmaps.elementsContentsPanel;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import java.util.*;
import freestyleLearning.learningUnitViewAPI.*;
import freestyleLearning.learningUnitViewAPI.elementsContentsPanel.*;
import freestyleLearning.learningUnitViewAPI.events.learningUnitEvent.*;
import freestyleLearning.learningUnitViewAPI.events.learningUnitViewEvent.*;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mindmaps.*;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mindmaps.data.xmlBindingSubclasses.*;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mindmaps.freemind.main.*;
import freestyleLearningGroup.freestyleLearning.learningUnitViewManagers.mindmaps.freemind.view.mindmapview.*;
import freestyleLearningGroup.independent.util.*;

public class FLGMindmapsMindmapElementContentPanel extends FSLAbstractLearningUnitViewElementContentPanel {

    private FreeMindAdapter freemindInternalFrame;

    private boolean active;

    private JComponent[] editToolBarComponents;

    private JComponent[] leftEditToolBarComponents;

    private Graphics g;

    private JScrollPane scrollPane;

    private MapView mindmapPane;

    public void init(FSLLearningUnitViewManager learningUnitViewManager, FSLLearningUnitEventGenerator learningUnitEventGenerator, boolean editMode) {
        super.init(learningUnitViewManager, learningUnitEventGenerator, editMode);
    }

    protected java.awt.Component getPrintableComponent() {
        Component printableComponent = null;
        try {
            printableComponent = freemindInternalFrame.getPrintPanel();
        } catch (Exception e) {
            System.err.println("Could not get printable mindmap component.");
        }
        return printableComponent;
    }

    public boolean isModifiedByUserInput() {
        return freemindInternalFrame.isModified();
    }

    public void saveUserChanges() {
        File mindmapsFile;
        FLGMindmapsElement learningUnitViewElement = (FLGMindmapsElement) learningUnitViewElementsManager.getLearningUnitViewElement(learningUnitViewElementId, true);
        learningUnitViewElement.setLastModificationDate(String.valueOf(new Date().getTime()));
        if (learningUnitViewElement.getMindmapFileName() == null) {
            mindmapsFile = learningUnitViewElementsManager.createNewFileForElementsExternalData(learningUnitViewElement.ELEMENT_TYPE_TEXT, ".mm", learningUnitViewElementId);
            learningUnitViewElement.setMindmapFileName(mindmapsFile.getName());
        } else {
            String relativeFileName = learningUnitViewElementsManager.getRelativeFileNameVersionForWriting(learningUnitViewElement.getMindmapFileName(), learningUnitViewElement, learningUnitViewElement.ELEMENT_TYPE_TEXT, ".mm");
            mindmapsFile = learningUnitViewElementsManager.resolveRelativeFileName(relativeFileName, learningUnitViewElement);
            learningUnitViewElement.setMindmapFileName(relativeFileName);
        }
        FLGFileUtility.writeStringIntoFile(freemindInternalFrame.getXMLRepresentationOfMindmap(), mindmapsFile);
    }

    protected void setActiveLearningUnitViewElementPanel(boolean active) {
        super.setActiveLearningUnitViewElementPanel(active);
        freemindInternalFrame.setEditable(editMode && activeLearningUnitViewElementPanel);
    }

    protected JComponent[] getEditToolBarComponents() {
        return editToolBarComponents;
    }

    protected JComponent[] getLeftEditToolBarComponents() {
        return leftEditToolBarComponents;
    }

    protected void buildIndependentUI(String learningUnitViewElementId) {
        this.learningUnitViewElementId = learningUnitViewElementId;
        buildIndependentUI();
    }

    protected void buildIndependentUI() {
        setLayout(new java.awt.BorderLayout());
        scrollPane = new JScrollPane();
        add(scrollPane, BorderLayout.CENTER);
        freemindInternalFrame = new FreeMindAdapter((FLGMindmapsManager) learningUnitViewManager, scrollPane);
        scrollPane.addComponentListener(new ScrollPaneComponentListener());
        freemindInternalFrame.setEditable(false);
        freemindInternalFrame.addHyperlinkListener(new FSLLearningUnitViewElementContentPanel_HyperlinkAdapter());
        FLGFreemindFrameEditButtonsFactory.FLGFileCreator fileCreator = new FLGFreemindFrameEditButtonsFactory.FLGFileCreator() {

            public File createFile(String fileExtension) {
                return learningUnitViewElementsManager.createNewFileForElementsExternalData("image", fileExtension, learningUnitViewElementId);
            }
        };
        editToolBarComponents = FLGFreemindFrameEditButtonsFactory.createDefaultFreemindFrameEditComponents(freemindInternalFrame, fileCreator);
        leftEditToolBarComponents = FLGFreemindFrameEditButtonsFactory.createDefaultFreemindFrameLeftEditComponents(freemindInternalFrame);
        updateUI();
    }

    protected void buildDependentUI(boolean reloadIfAlreadyLoaded) {
        boolean contentAvailable = false;
        if (learningUnitViewElementsManager != null && learningUnitViewElementId != null) {
            FLGMindmapsElement learningUnitViewElement = (FLGMindmapsElement) learningUnitViewElementsManager.getLearningUnitViewElement(learningUnitViewElementId, false);
            if (learningUnitViewElement != null) {
                if (learningUnitViewElement.getMindmapFileName() != null) {
                    File currentFile = null;
                    try {
                        currentFile = freemindInternalFrame.getController().getModel().getFile();
                    } catch (Exception e) {
                    }
                    File newFile = learningUnitViewElementsManager.resolveRelativeFileName(learningUnitViewElement.getMindmapFileName(), learningUnitViewElement);
                    if (currentFile == null || !currentFile.equals(newFile) || reloadIfAlreadyLoaded) try {
                        freemindInternalFrame.getController().getMode().getModeController().load(learningUnitViewElementsManager.resolveRelativeFileName(learningUnitViewElement.getMindmapFileName(), learningUnitViewElement));
                    } catch (FileNotFoundException e) {
                        System.err.println("Mindmap-File not found.");
                    } catch (IOException e) {
                        System.err.println("Mindmap-File could not be loaded.");
                    }
                } else {
                    freemindInternalFrame.getController().getMode().getModeController().newMap(learningUnitViewElement.getTitle());
                    freemindInternalFrame.getController().getModel().setFile(learningUnitViewElementsManager.resolveRelativeFileName("dummyFileName", learningUnitViewElement));
                }
                contentAvailable = true;
            }
        }
        freemindInternalFrame.setEditable(editMode && activeLearningUnitViewElementPanel);
    }

    public void freemindZoom(float zoom) {
        freemindInternalFrame.zoom(zoom);
    }

    public boolean freemindFind() {
        return freemindInternalFrame.find();
    }

    public boolean freemindFindNext() {
        return freemindInternalFrame.findNext();
    }

    class ScrollPaneComponentListener implements ComponentListener {

        public void componentHidden(ComponentEvent e) {
        }

        public void componentMoved(ComponentEvent e) {
        }

        public void componentResized(ComponentEvent e) {
            File currentFile = null;
            try {
                currentFile = freemindInternalFrame.getController().getModel().getFile();
            } catch (Exception ex) {
            }
            if (currentFile != null) {
                freemindInternalFrame.getController().moveToRoot();
            }
        }

        public void componentShown(ComponentEvent e) {
        }
    }

    public void updateUI() {
        super.updateUI();
        if (scrollPane != null) {
            scrollPane.updateUI();
        }
        File currentFile = null;
        try {
            currentFile = freemindInternalFrame.getController().getModel().getFile();
        } catch (Exception ex) {
        }
        if (currentFile != null) {
            freemindInternalFrame.getController().moveToRoot();
        }
    }
}
