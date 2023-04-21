package org.stanwood.media.actions;

import java.io.File;
import org.stanwood.media.MediaDirectory;
import org.stanwood.media.extensions.IExtension;
import org.stanwood.media.model.IEpisode;
import org.stanwood.media.model.IFilm;

/**
 * This interface should be implemented by classes that are used to represent actions
 * that can be performed on media files in a media directory
 */
public interface IAction extends IExtension {

    /**
	 * This is called once before any of the media files are processed to allow the action
	 * to perform setup tasks
	 * @param dir The media directory
	 * @throws ActionException Thrown if their is a problem with the action
	 */
    public void init(MediaDirectory dir) throws ActionException;

    /**
	 * Used to perform the action upon a TV episode file.
	 * @param episode The episode information
	 * @param mediaFile The media file
	 * @param dir File media directory the files belongs to
	 * @param actionEventHandler Used to notify the action performer about changes
	 * @throws ActionException Thrown if their is a problem with the action
	 */
    public void perform(MediaDirectory dir, IEpisode episode, File mediaFile, IActionEventHandler actionEventHandler) throws ActionException;

    /**
	 * Used to perform the action upon a film file.
	 * @param film The film information
	 * @param part The part number of the film, or null if it does not have parts
	 * @param mediaFile The media file
	 * @param dir File media directory the files belongs to
	 * @param actionEventHandler Used to notify the action performer about changes
	 * @throws ActionException Thrown if their is a problem with the action
	 */
    public void perform(MediaDirectory dir, IFilm film, File mediaFile, Integer part, IActionEventHandler actionEventHandler) throws ActionException;

    /**
	 * Used to set the value of actions parameter
	 * @param key The key of the parameter
	 * @param value The value of the parameter
	 * @throws ActionException Thrown if their is a problem setting the parameter
	 */
    public void setParameter(String key, String value) throws ActionException;

    /**
	 * Used to notify the action if test mode is enabled
	 * @param testMode True if test mode is enabled, otherwise false
	 */
    public void setTestMode(boolean testMode);

    /**
	 * Used to find out if test mode is enabled
	 * @return True if test mode is enabled, otherwise false
	 */
    public boolean isTestMode();

    /**
	 * This is called so the action can perform on directories within the media directory
	 * @param mediaDir The media directory
	 * @param dir The directory the action is to perform on
	 * @param actionEventHandler Used to notify the action performer about changes
	 * @throws ActionException Thrown if their is a problem with the action
	 */
    public void performOnDirectory(MediaDirectory mediaDir, File dir, IActionEventHandler actionEventHandler) throws ActionException;

    /**
	 * This is called once for each action after they have finished performing on all media files
	 * within the media directory.
	 * @param dir The media directory
	 * @throws ActionException Thrown if their is a problem with the action
	 */
    public void finished(MediaDirectory dir) throws ActionException;
}
