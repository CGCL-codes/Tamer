package tasklist;

import java.util.*;
import javax.swing.*;
import javax.swing.tree.*;
import org.gjt.sp.jedit.*;
import org.gjt.sp.jedit.msg.*;
import common.swingworker.*;

public class OpenBuffersTaskList extends AbstractTreeTaskList {

    public OpenBuffersTaskList(View view) {
        super(view, jEdit.getProperty("tasklist.openfiles.open-files", "Open Files:"));
        putClientProperty("isCloseable", Boolean.FALSE);
    }

    @Override
    protected boolean canRun() {
        return jEdit.getBooleanProperty("tasklist.show-open-files", true);
    }

    @Override
    protected List<String> getBuffersToScan() {
        Set<String> openBuffers = new HashSet<String>();
        EditPane[] editPanes = view.getEditPanes();
        for (EditPane editPane : editPanes) {
            Buffer[] buffers = editPane.getBufferSet().getAllBuffers();
            for (Buffer buffer : buffers) {
                if (Binary.isBinary(buffer)) {
                    continue;
                }
                openBuffers.add(buffer.getPath());
            }
        }
        return new ArrayList<String>(openBuffers);
    }
}
