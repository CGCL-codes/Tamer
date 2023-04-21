package org.vastenhouw.jphotar.dirbrowser;

import java.util.Arrays;
import java.io.File;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.ListSelectionEvent;
import org.vastenhouw.swing.AbstractStatusBar;
import org.vastenhouw.util.Debug;
import org.vastenhouw.util.FileInfo;
import org.vastenhouw.jphotar.folderbrowser.DirectoryEvent;
import org.vastenhouw.jphotar.folderbrowser.DirectoryEventListener;
import org.vastenhouw.jphotar.imagebrowser.*;
import org.vastenhouw.util.res.Resources;
import org.vastenhouw.jphotar.res.bundles.LblJPT;

public class StatusBar extends AbstractStatusBar implements DirectoryEventListener, ListSelectionListener {

    private static final double WEIGHTS[] = { 0.29999999999999999D, 0.29999999999999999D, 0.29999999999999999D };

    private int[] oldSelection;

    public StatusBar() {
        super(WEIGHTS);
    }

    public void directoryChanged(DirectoryEvent directoryevent) {
        File file = directoryevent.getDir();
        oldSelection = null;
        if (file.canRead()) {
            File[] imageFiles = FileInfo.getImages(file);
            refreshImageInfoSummary(imageFiles, false);
        }
    }

    public void valueChanged(ListSelectionEvent lse) {
        if (Debug.INFO) Debug.out.println("StatusBar.imageSelected");
        JList l = (JList) lse.getSource();
        ListModel lm = l.getModel();
        int[] curSelection = l.getSelectedIndices();
        if (!Arrays.equals(oldSelection, curSelection)) {
            File[] selFiles = new File[curSelection.length];
            for (int i = 0; i < curSelection.length; i++) {
                ImageInfo imgInf = (ImageInfo) lm.getElementAt(curSelection[i]);
                selFiles[i] = imgInf.getFile();
            }
            refreshImageInfoSummary(selFiles, true);
        }
    }

    private void refreshImageInfoSummary(File[] files, boolean selected) {
        if (files == null) {
            files = new File[0];
        }
        String nrs = (new Integer(files.length)).toString();
        ((JLabel) barComponents[0]).setText(selected ? Resources.getText(LblJPT.lbStatusBarSelectedImages, nrs) : Resources.getText(LblJPT.lbStatusBarImages, nrs));
        ((JLabel) barComponents[1]).setText(FileInfo.printSize(FileInfo.getImageSizes(files)));
    }
}
