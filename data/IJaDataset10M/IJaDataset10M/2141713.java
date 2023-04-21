package com.visitrend.ndvis.plugins;

import java.awt.Cursor;
import javax.swing.Icon;
import com.visitrend.ndvis.app.NDVis;

/**
 * PlugIns are modal, so only one can be activated at a time, but that is all
 * taken care of transparently so you don't have to worry about that in
 * implementing classes. If you want to grab user mouse events on ImagePanel
 * instances, simply have the implementing class also implement MouseListener or
 * MouseMotionListener; the main NDVis app takes care of the rest. See
 * NDVis.setActivePlugIn(). No other listener types are currently supported, but
 * you can add them to that method, or refactor I suppose.
 * 
 * <p>
 * 
 * I haven't completely finished the "plugin architecture" or methods of the
 * app. If you create one, you should really implement UserPlugInMaker, or at
 * least put in code there. However, it might be quicker just to hack up
 * CorePlugInMaker although I'd rather you didn't ;-)
 * 
 * @author John T. Langton - jlangton at visitrend dot com
 * 
 */
public interface PlugInINF {

    /**
	 * This method will be called on construction of the app and all
	 * implementing classes. Implementing classes don't have to do anything,
	 * this is simply an opportunity for them to get a reference to the main
	 * NDVis app if they need one.
	 * 
	 * @param ndvis
	 */
    public abstract void init(NDVis ndvis);

    public abstract String getName();

    public abstract void setName(String name);

    public abstract Cursor getCursor();

    public abstract void setCursor(Cursor cursor);

    public abstract Icon getIcon();

    public abstract void setIcon(Icon i);
}
