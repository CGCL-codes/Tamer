package org.gerhardb.jibs.viewer;

import org.gerhardb.lib.dirtree.ExtendedDirectoryTree;
import org.gerhardb.lib.dirtree.filelist.FileList;
import javax.swing.JFrame;
import org.gerhardb.jibs.viewer.frame.ViewerActions;
import org.gerhardb.jibs.viewer.shows.PictureCache;
import org.gerhardb.lib.scroller.KeypadOps;
import org.gerhardb.lib.scroller.Scroller;
import org.gerhardb.lib.scroller.ScrollerSlider;

/**
 * Contract of the frame to hold the components.
 * 
 * @author Gerhard Beck
 */
public interface IFrame {

    public void setWaitCursor(boolean b);

    public JFrame getFrame();

    public ViewerActions getActions();

    public ScrollerSlider getSlider();

    public FileList getFileList();

    public ExtendedDirectoryTree getExtendedDirectoryTree();

    public void gotoRegularScreen();

    public void gotoFullScreen(boolean startNow);

    public void setViewer(IShow show);

    public void showHelp();

    public void statusCurrentPage();

    public IShow getShow();

    public KeypadOps getKeypadOps();

    public Scroller getScroller();

    public PictureCache getPictureCache();
}
