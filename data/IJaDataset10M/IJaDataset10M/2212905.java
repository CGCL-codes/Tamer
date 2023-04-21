package net.sf.iauthor.ui.editors;

import net.sf.iauthor.core.Actor;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;

/**
 * @author Andreas Beckers
 * 
 */
public class ActorEditorInput implements IEditorInput {

    private final Actor _actor;

    /**
	 * 
	 */
    public ActorEditorInput(Actor character) {
        _actor = character;
    }

    /**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
    @Override
    public Object getAdapter(@SuppressWarnings("rawtypes") Class adapter) {
        if (adapter == Actor.class) return _actor;
        return null;
    }

    /**
	 * @see org.eclipse.ui.IEditorInput#exists()
	 */
    @Override
    public boolean exists() {
        return false;
    }

    /**
	 * @see org.eclipse.ui.IEditorInput#getImageDescriptor()
	 */
    @Override
    public ImageDescriptor getImageDescriptor() {
        return null;
    }

    /**
	 * @see org.eclipse.ui.IEditorInput#getName()
	 */
    @Override
    public String getName() {
        return _actor.getName();
    }

    /**
	 * @see org.eclipse.ui.IEditorInput#getPersistable()
	 */
    @Override
    public IPersistableElement getPersistable() {
        return null;
    }

    /**
	 * @see org.eclipse.ui.IEditorInput#getToolTipText()
	 */
    @Override
    public String getToolTipText() {
        return getName();
    }
}
