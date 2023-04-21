package edu.gsbme.messagehandler.UI;

import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import edu.gsbme.messagehandler.MessageHandler;
import edu.gsbme.messagehandler.MessageStructure;

/**
 * Message Tree content provider
 * @author David
 *
 */
public class MessageTreeContentProvider implements ITreeContentProvider {

    @Override
    public Object[] getChildren(Object parentElement) {
        if (parentElement instanceof MessageHandler) {
            return null;
        } else if (parentElement instanceof MessageStructure) {
            MessageStructure temp = (MessageStructure) parentElement;
            return temp.returnChildrenList().toArray();
        }
        return null;
    }

    @Override
    public Object getParent(Object element) {
        if (element instanceof MessageHandler) {
            return ((MessageHandler) element).getParent();
        } else if (element instanceof MessageStructure) {
            return ((MessageStructure) element).getParent();
        }
        return null;
    }

    @Override
    public boolean hasChildren(Object element) {
        if (element instanceof MessageHandler) {
            return false;
        } else if (element instanceof MessageStructure) {
            if (((MessageStructure) element).returnChildrenList().size() > 0) return true;
        }
        return false;
    }

    @Override
    public Object[] getElements(Object inputElement) {
        if (inputElement instanceof MessageHandler) {
            MessageHandler temp = (MessageHandler) inputElement;
            return temp.returnMessageList().toArray();
        } else if (inputElement instanceof MessageStructure) {
            return new Object[] { inputElement };
        }
        return null;
    }

    @Override
    public void dispose() {
    }

    @Override
    public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
    }
}
