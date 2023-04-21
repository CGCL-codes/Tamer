package edu.toronto.cs.ome.controller;

import java.util.Collection;
import edu.toronto.cs.ome.view.View;

/** This interface describes the methods that need to be implemented by any
  * <code>Plugin</code> in OME. A Plugin in OME is a collection of routines 
  * (specifically <code>PluginMethods</code>) that get hooked up to the view's GUI.  
  * Plugins are the intended place to add framework specific functionality to the tool. 
  * Plugins are described in considerable detail in the OME3 PowerUser Manual.
  *
  * <P>Plugins <B>must</B> also provide the following method:
  *
  * <P><code>public static boolean isCompatibleWith(OMEModel model)</code>
  *
  * <P>This method is called prior to construction of the plugin to determine if
  * it should be used for a particular model.  It is not included in the
  * interface proper because Java does not allow for static methods to be
  * placed in interfaces.
  */
public interface OMEPlugin {

    /** Returns a collection  of our <code>PluginMethod</code>s that are to be
      * placed on the OME toolbar. 
      *
      * @param v the view been provided 
      */
    public Collection getToolbarMethods(View v);

    /** Returns a collection  of our <code>PluginMethod</code>s that are to be
      * placed on the OME menubar. Presumably these are all menus that will
      * be populated through the getSubmenu() method. 
      *
      * @param v the view been provided 
      */
    public Collection getMenubarMethods(View v);

    /** Returns a collection  of our <code>PluginMethod</code>s that are to be
      * placed in the OME popup-menu (when the user clicks the right mouse
      * button).  These methods (and their subMenus) are the only ones that
      * will have their setClicked method called, and this will happen 
      * once--prior to the first call to nextParamter(). 
      *
      * @param v the view been provided 
      */
    public Collection getPopupMethods(View v);

    public void setView(View view);
}
