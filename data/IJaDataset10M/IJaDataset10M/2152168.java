package org.one.stone.soup.grfxML.plugin.system;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceAdapter;
import java.io.IOException;
import java.net.URL;
import org.one.stone.soup.browser.Browser;
import org.one.stone.soup.grfxML.DataState;
import org.one.stone.soup.grfxML.DataString;
import org.one.stone.soup.grfxML.GrfxMLEngine;
import org.one.stone.soup.grfxML.plugin.SimplePlugin;
import org.one.stone.soup.grfxML.plugin.grfxMLCaster;
import org.one.stone.soup.mjdb.data.field.DataLinker;
import org.one.stone.soup.mjdb.data.field.Field;

public class FileDragPlugin extends SimplePlugin {

    public static final int ARG_DRAG = 0;

    public static final int ARG_URL = 1;

    private DataState startDrag = new DataState();

    private DataString url = new DataString();

    private DragSource dragSource;

    public class DGListener implements DragGestureListener {

        public void dragGestureRecognized(DragGestureEvent dge) {
            URLTransfer data = new URLTransfer(url.getValue());
            dge.startDrag(DragSource.DefaultCopyNoDrop, data);
        }
    }

    public class DSListener extends DragSourceAdapter {
    }

    public class URLTransfer implements Transferable {

        private URL url;

        private DataFlavor flavor;

        public URLTransfer(String urlName) {
            try {
                url = new URL(Browser.getURLFor(urlName));
                flavor = new DataFlavor("application/x-java-url");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        public boolean isDataFlavorSupported(DataFlavor flavor) {
            if (flavor.getMimeType().equals(this.flavor.getMimeType())) {
                return true;
            } else {
                return false;
            }
        }

        public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
            return url;
        }

        public DataFlavor[] getTransferDataFlavors() {
            return new DataFlavor[] { flavor };
        }
    }

    private DGListener dgListener;

    public FileDragPlugin(GrfxMLEngine engine) {
        super(engine);
        dgListener = new DGListener();
        dragSource = DragSource.getDefaultDragSource();
        dragSource.createDefaultDragGestureRecognizer(getEngine().getController().getComponent(), DnDConstants.ACTION_COPY_OR_MOVE, dgListener);
    }

    public void initialize() {
    }

    public void register(DataLinker store) {
        startDrag = grfxMLCaster.cast(startDrag, getArg(startDrag, ARG_DRAG, store));
        url = grfxMLCaster.cast(url, getArg(url, ARG_URL, store));
    }

    public void replace(Field oldObj, Field newObj) {
        startDrag = grfxMLCaster.replace(startDrag, oldObj, newObj);
        url = grfxMLCaster.replace(url, oldObj, newObj);
    }

    public void stop() {
    }

    public void process() {
        if (startDrag.getValue() == true) {
        }
    }
}
