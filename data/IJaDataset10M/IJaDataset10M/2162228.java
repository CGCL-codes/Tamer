package com.umc.plugins.gui;

import java.util.Collection;
import com.umc.beans.media.Movie;

/**
 * This is the basic interface for implementing UMC gui plugins especially for the movie area.
 * Read the method declarations to see which method must be strictly implemented by the plugin developer!
 * 
 * @author DonGyros
 * @version 0.1
 */
public interface IPluginMovie extends IPluginBase {

    public void setMovieBean(Collection<Movie> movieList, String language);

    public Collection<Movie> update();
}
