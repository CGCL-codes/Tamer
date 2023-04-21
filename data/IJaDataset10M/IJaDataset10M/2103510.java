package blue.ui.core.score;

import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.net.URLDecoder;
import java.util.List;
import blue.BlueSystem;
import blue.soundObject.AudioFile;
import blue.soundObject.PolyObject;
import blue.utility.SoundFileUtilities;

/**
 * @author Steven Yi
 */
public class ScoreTimelineDropTargetListener implements DropTargetListener {

    DropTarget target;

    private ScoreTimeCanvas sTimeCanvas;

    public ScoreTimelineDropTargetListener(ScoreTimeCanvas sTimeCanvas) {
        this.sTimeCanvas = sTimeCanvas;
        target = new DropTarget(sTimeCanvas, this);
    }

    public void dragEnter(DropTargetDragEvent dtde) {
        boolean isFile = dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor);
        if (!isFile) {
            if (dtde.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                isFile = true;
            }
        }
        if (isFile) {
            dtde.acceptDrag(DnDConstants.ACTION_LINK);
        } else {
            dtde.rejectDrag();
        }
    }

    public void dragOver(DropTargetDragEvent dtde) {
    }

    public void dropActionChanged(DropTargetDragEvent dtde) {
    }

    public void drop(DropTargetDropEvent dtde) {
        try {
            Transferable tr = dtde.getTransferable();
            if (dtde.isDataFlavorSupported(DataFlavor.javaFileListFlavor)) {
                dtde.acceptDrop(DnDConstants.ACTION_LINK);
                List list = (List) tr.getTransferData(DataFlavor.javaFileListFlavor);
                if (list.size() != 1) {
                    dtde.dropComplete(false);
                    return;
                }
                String s = list.get(0).toString().trim();
                if (!s.toLowerCase().endsWith("wav") && !s.toLowerCase().endsWith("aif") && !s.toLowerCase().endsWith("aiff")) {
                    dtde.dropComplete(false);
                    return;
                }
                String sObjName = s.substring(s.lastIndexOf(File.separator) + 1);
                Point p = dtde.getLocation();
                int index = sTimeCanvas.pObj.getLayerNumForY(p.y);
                AudioFile af = new AudioFile();
                af.setName(sObjName);
                af.setSoundFileName(BlueSystem.getRelativePath(s));
                PolyObject pObj = sTimeCanvas.getPolyObject();
                float startTime = (float) p.x / pObj.getPixelSecond();
                float dur = SoundFileUtilities.getDurationInSeconds(s);
                af.setStartTime(startTime);
                af.setSubjectiveDuration(dur);
                pObj.addSoundObject(index, af);
                dtde.dropComplete(true);
                return;
            } else if (dtde.isDataFlavorSupported(DataFlavor.stringFlavor)) {
                dtde.acceptDrop(DnDConstants.ACTION_LINK);
                String str = (String) tr.getTransferData(DataFlavor.stringFlavor);
                if (!str.startsWith("file://")) {
                    dtde.dropComplete(false);
                    return;
                }
                str = str.substring(7).trim();
                if (!str.toLowerCase().endsWith("wav") && !str.toLowerCase().endsWith("aif") && !str.toLowerCase().endsWith("aiff")) {
                    System.err.println("Could not open file: " + str);
                    dtde.dropComplete(false);
                    return;
                }
                str = URLDecoder.decode(str);
                str = str.replaceAll(" ", "\\ ");
                File f = new File(str);
                if (!f.exists()) {
                    dtde.dropComplete(false);
                    return;
                }
                String sObjName = str.substring(str.lastIndexOf(File.separator) + 1);
                Point p = dtde.getLocation();
                int index = sTimeCanvas.pObj.getLayerNumForY(p.y);
                AudioFile af = new AudioFile();
                af.setName(sObjName);
                af.setSoundFileName(str);
                PolyObject pObj = sTimeCanvas.getPolyObject();
                float startTime = (float) p.x / pObj.getPixelSecond();
                float dur = SoundFileUtilities.getDurationInSeconds(str);
                af.setStartTime(startTime);
                af.setSubjectiveDuration(dur);
                pObj.addSoundObject(index, af);
                dtde.dropComplete(true);
                return;
            }
            dtde.rejectDrop();
        } catch (Exception e) {
            e.printStackTrace();
            dtde.rejectDrop();
        }
    }

    public void dragExit(DropTargetEvent dte) {
    }
}
