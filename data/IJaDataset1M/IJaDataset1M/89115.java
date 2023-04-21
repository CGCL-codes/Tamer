package net.sourceforge.viea.core.controller;

import net.sourceforge.viea.core.model.VieAPluginModel;
import net.sourceforge.viea.core.view.VieAPluginDrawer;

/**
 * This interface have to be implements in order to code a new plug-in.
 * 
 * @author Xavier Detant <xavier.detant@gmail.com>
 * @version 0.0.1 Type creation
 */
public interface VieAPlugin {

    /**
     * Gets the controller plug-in.
     * 
     * @return the controller plug-in
     */
    VieAPluginController getController();

    /**
     * Gets the drawer (view) plug-in.
     * 
     * @return the drawer plug-in
     */
    VieAPluginDrawer getDrawer();

    /**
     * Gets the model plug-in.
     * 
     * @return the model plug-in
     */
    VieAPluginModel getModel();

    /**
     * Setup plugin.
     */
    void setupPlugin();
}
