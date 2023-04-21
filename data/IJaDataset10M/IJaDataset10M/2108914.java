package info.metlos.jcdc.fileshare;

import info.metlos.jcdc.util.INamed;
import info.metlos.jcdc.views.IVisualisable;
import info.metlos.jdc.fileshare.IFileListProvider;

/**
 * This is an interface that must be implemented by all
 * {@link IFileListProvider}s that want to be configurable in the GUI.
 * 
 * @author metlos
 * 
 * @version $Id: IVisualFileListProvider.java 133 2007-09-12 01:07:57Z metlos $
 */
public interface IVisualFileListProvider extends IVisualisable, INamed, IFileListProvider {

    /**
	 * Sets the descriptive name of this provider.
	 * 
	 * @param name
	 */
    void setName(String name);

    IVisualFileListProvider clone();
}
