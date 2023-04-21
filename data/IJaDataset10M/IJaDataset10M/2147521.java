package fr.univartois.cril.xtext.alloyplugin.api;

import org.eclipse.core.resources.IResource;

public interface IALSFactory {

    /**
	 * Get the IALSFile associated with this resource or null if this kind of resource can't be associated with an IALSFile.
	 * If the file doesn't exist yet, it's created and compiled. 
	 */
    public abstract IALSFile getIALSFile(IResource resource);

    /**
	 * remove a IALSFile
	 * */
    public abstract void remove(IResource Resource);
}
