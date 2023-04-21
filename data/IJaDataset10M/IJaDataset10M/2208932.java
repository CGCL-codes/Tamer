package ch.jester.common.ui.handlers;

import java.util.List;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.jface.util.LocalSelectionTransfer;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.dnd.Clipboard;
import ch.jester.common.ui.utility.GlobalClipBoard;
import ch.jester.common.ui.utility.SelectionUtility;

/**
 * Abstrakter PasteHandler, welcher einfach aus dem Clipborad die Daten holt, und an die Implementation
 * weiterreicht
 * @param <T>
 */
public abstract class ClonePasteHandler<T> extends AbstractCommandHandler {

    Clipboard board = GlobalClipBoard.getInstance();

    SelectionUtility mSelectionUtility = new SelectionUtility(null);

    @SuppressWarnings("unchecked")
    @Override
    public Object executeInternal(ExecutionEvent event) {
        LocalSelectionTransfer transfer = LocalSelectionTransfer.getTransfer();
        mSelectionUtility.setSelection((IStructuredSelection) board.getContents(transfer));
        return handlePaste(mSelectionUtility.getAsStructuredSelection().toList());
    }

    /**
	 * @return Anzahl Objekte in Selektion
	 */
    public int getPasteCount() {
        return mSelectionUtility.getSelectionCount();
    }

    /**
	 * @return die pasteSelection
	 */
    public ISelection getPasteSelection() {
        return mSelectionUtility.getSelection();
    }

    /**
	 * @param pPastedList neue Objekte, welche eingefügt werden sollen
	 * @return
	 */
    public abstract Object handlePaste(List<T> pPastedList);
}
