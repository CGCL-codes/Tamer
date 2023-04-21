package com.umc.plugins.gui;

import java.util.Collection;
import com.umc.beans.media.Photo;

/**
 * This is the basic interface for implementing UMC gui plugins especially for the photo area.
 * Read the method declarations to see which method must be strictly implemented by the plugin developer!
 * 
 * @author DonGyros
 * @version 0.1
 */
public interface IPluginPhoto extends IPluginBase {

    /**
	 * IMPLEMENT THIS METHOD
	 * 
	 * The UMC backend will set the selected {@link Photo} bean from the photo tree in the manage area by using this method.
	 *  
	 * @param p a {@link Photo} object
	 */
    public void setPhotoBean(Collection<Photo> photoList);

    public Collection<Photo> update();
}
