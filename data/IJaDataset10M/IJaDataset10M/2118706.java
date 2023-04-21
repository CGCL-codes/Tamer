package folder;

import datastructure.DataStructure;
import files.*;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import touchcomponent.*;

/**
 *  Folder of the datastructure which
 * contains its sub-levels & file lists.
 *
 * @author Claire+ l'arbre
 */
public class Folder extends TouchComponent {

    private static final String DEFAULT_TEXT = "Folder";

    private static final double DEFAULT_DEFAULTOPACITY = 0.90;

    private static final double DEFAULT_LOCKEDOPACITY = 1.00;

    private static final double DEFAULT_MOVINGOPACITY = 0.50;

    private static final String DEFAULT_SELECTEDFOLDERFULL = "DefaultSelectedFolderFull.png";

    private static final String DEFAULT_SELECTEDFOLDEREMPTY = "DefaultSelectedFolderEmpty.png";

    private static final String DEFAULT_SELECTEDFOLDERWITHFILES = "DefaultSelectedFolderWithFiles.png";

    private static final String DEFAULT_SELECTEDFOLDERWITHNODES = "DefaultSelectedFolderWithNodes.png";

    private static final String DEFAULT_UNSELECTEDFOLDERFULL = "DefaultUnselectedFolderFull.png";

    private static final String DEFAULT_UNSELECTEDFOLDEREMPTY = "DefaultUnselectedFolderEmpty.png";

    private static final String DEFAULT_UNSELECTEDFOLDERWITHFILES = "DefaultUnselectedFolderWithFiles.png";

    private static final String DEFAULT_UNSELECTEDFOLDERWITHNODES = "DefaultUnselectedFolderWithNodes.png";

    private static final String DEFAULT_FOLDERPATH = "/images/";

    private String myText;

    private double myDefaultOpacity;

    private double myLockedOpacity;

    private double myMovingOpacity;

    private FileList myFileList;

    private boolean isSelected;

    private boolean isOpen;

    private PossibleFolder myType;

    private TouchComponentAdapter myTouchComponentAdapterThis;

    public Folder() {
        this(DEFAULT_TEXT, DEFAULT_DEFAULTOPACITY, DEFAULT_LOCKEDOPACITY, DEFAULT_MOVINGOPACITY);
    }

    /**
    * Create a Folder of the dataStructure
    * @param text : the string label of the folder
     * @param opacity : the default opacity of the folder, between 0.00 and 1.00
     * @param lockedOpacity : the opacity of the folder when it's selected, betwen 0.00 and 1.00
     * @param movingOpacity : the opacity of the folder, when it's moved, betwen 0.00 and 1.00   
    */
    public Folder(String text, double opacity, double lockedOpacity, double movingOpacity) {
        myText = text;
        myDefaultOpacity = opacity;
        myLockedOpacity = lockedOpacity;
        myMovingOpacity = movingOpacity;
        myOpacity = myDefaultOpacity;
        myFileList = new FileList();
        isSelected = false;
        isOpen = false;
        myType = PossibleFolder.empty;
        myTouchComponentAdapterThis = new TouchComponentAdapter() {

            @Override
            public void TouchClicked(TouchComponentEvent event) {
                switch(myType) {
                    case empty:
                        break;
                    case withNodes:
                        if (!isOpen) ((DataStructure) event.getSource()).openFolder(Folder.this); else ((DataStructure) event.getSource()).closeFolder(Folder.this);
                        isOpen = !isOpen;
                        break;
                    case withFiles:
                        if (!isOpen) ((DataStructure) event.getSource()).openFolder(Folder.this); else ((DataStructure) event.getSource()).closeFolder(Folder.this);
                        isOpen = !isOpen;
                        break;
                    case full:
                        if (!isOpen) ((DataStructure) event.getSource()).openFolder(Folder.this); else ((DataStructure) event.getSource()).closeFolder(Folder.this);
                        isOpen = !isOpen;
                        break;
                    default:
                        System.out.println("Folder.ToucheComponentAdapter.TouchClicked: " + "Error, wrong value for myType");
                        break;
                }
                setSelected(true);
            }

            @Override
            public void TouchPressed(TouchComponentEvent event) {
            }

            @Override
            public void TouchReleased(TouchComponentEvent event) {
            }

            @Override
            public void TouchTranslate(TouchComponentEvent event, double translateX, double translateY) {
            }
        };
    }

    public String getText() {
        return myText;
    }

    public void setText(String text) {
        myText = text;
    }

    public double getDefaultOpacity() {
        return myDefaultOpacity;
    }

    public void setDefaultOpacity(double defaultOpacity) {
        this.myDefaultOpacity = defaultOpacity;
        repaint();
    }

    public double getLockedOpacity() {
        return myLockedOpacity;
    }

    public void setLockedOpacity(double lockedOpacity) {
        this.myLockedOpacity = lockedOpacity;
        repaint();
    }

    public double getMovingOpacity() {
        return myMovingOpacity;
    }

    public void setMovingOpacity(double movingOpacity) {
        this.myMovingOpacity = movingOpacity;
        repaint();
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean isSelected) {
        this.isSelected = isSelected;
        repaint();
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setIsOpen(boolean isOpen) {
        this.isOpen = isOpen;
        repaint();
    }

    public void addFile(File file) {
        myFileList.addFile(file);
    }

    public void addFolder(Folder folder) {
    }

    @Override
    public void paint(Graphics g) {
        Image myImage = null;
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        if (isSelected) {
            switch(myType) {
                case full:
                    myImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource(DEFAULT_FOLDERPATH + DEFAULT_SELECTEDFOLDERFULL));
                    break;
                case withFiles:
                    myImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource(DEFAULT_FOLDERPATH + DEFAULT_SELECTEDFOLDERWITHFILES));
                    break;
                case withNodes:
                    myImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource(DEFAULT_FOLDERPATH + DEFAULT_SELECTEDFOLDERWITHNODES));
                    break;
                case empty:
                    myImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource(DEFAULT_FOLDERPATH + DEFAULT_SELECTEDFOLDEREMPTY));
                    break;
            }
        } else {
            switch(myType) {
                case full:
                    myImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource(DEFAULT_FOLDERPATH + DEFAULT_UNSELECTEDFOLDERFULL));
                    break;
                case withFiles:
                    myImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource(DEFAULT_FOLDERPATH + DEFAULT_UNSELECTEDFOLDERWITHFILES));
                    break;
                case withNodes:
                    myImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource(DEFAULT_FOLDERPATH + DEFAULT_UNSELECTEDFOLDERWITHNODES));
                    break;
                case empty:
                    myImage = Toolkit.getDefaultToolkit().getImage(getClass().getResource(DEFAULT_FOLDERPATH + DEFAULT_UNSELECTEDFOLDEREMPTY));
                    break;
            }
        }
        prepareImage(myImage, this);
        g.drawImage(myImage, 0, 0, (int) (getWidth()), (int) (getHeight()), null);
        g.drawString(myText, 0, getHeight() * 4 / 5);
    }

    @Override
    public void setVisible(boolean aFlag) {
        super.setVisible(aFlag);
        if (aFlag) {
            addTouchComponentListener(myTouchComponentAdapterThis);
        } else {
            removeTouchComponentListener(myTouchComponentAdapterThis);
        }
    }

    @Override
    public Folder clone() {
        return new Folder(myText, myOpacity, myLockedOpacity, myMovingOpacity);
    }

    private enum PossibleFolder {

        full, withFiles, withNodes, empty
    }
}
