package org.jlense.uiworks.workbench;

import java.awt.Image;
import java.net.URL;
import java.util.Hashtable;

/**
 * Provides Workbench 'About' information.
 */
public interface IAboutInfo {

    /**
     * Returns a new image which can be shown in an "about" dialog for this product.
     * Products designed to run "headless" typically would not have such an image.
     * <p>
     * Clients are responsible for ensuring that the returned image is properly
     * disposed after it has served its purpose.
     * </p>
     * 
     * @return the about image, or <code>null</code> if none
     */
    public Image getAboutImage();

    /**
     * Returns the app name. 
     *
     * @return the app name
     */
    public String getAppName();

    /**
     * Returns the build id for this product.
     * <p>
     * The build id represents any builds or updates made in support of a major
     * release. Development teams typically may produce many builds only a subset
     * of which get shipped to customers.
     * </p>
     *
     * @return the build id
     */
    public String getBuildID();

    /**
     * Returns the default preferences obtained from the configuration.
     *
     * @return the default preferences obtained from the configuration
     */
    public Hashtable getConfigurationPreferences();

    /**
     * Returns the copyright notice for this product.
     * <p>
     * The copyright notice is typically shown in the product's "about" dialog.
     * </p>
     *
     * @return the copyright notice
     */
    public String getCopyright();

    /**
     * Returns the default perpective id.  This value will be used
     * as the default perspective for the product until the user overrides
     * it from the preferences.
     * 
     * @return the default perspective id, or <code>null</code> if none
     */
    public String getDefaultPerspective();

    /**
     * Returns the full name of this product.
     * <p>
     * The full name also includes additional information about the particular
     * variant of the product.
     * </p>
     *
     * @return the full name of this product
     */
    public String getDetailedName();

    /**
     * Returns the name of this product.
     *
     * @return the name of this product
     */
    public String getName();

    /**
     * Returns an image descriptor for this product's icon.
     * Products designed to run "headless" typically would not have such an image.
     * <p>
     * The image is typically used in the top left corner of all windows associated
     * with the product.
     * </p>
     *
     * @return an image descriptor for the product's icon, or <code>null</code> if
     *  none
     */
    public Image getProductImage();

    /**
     * Returns the URL for this product's main page on the world wide web.
     *
     * @return the product URL
     */
    public String getProductURL();

    /**
     * Returns a new image like the one that would have been shown in a "splash" 
     * screen when this product starts up. Products designed to run "headless" would
     * not need such an image.
     * <p>
     * Note: This is spec'd to return a new instance.
     * Clients are responsible for ensuring that the returned image is properly
     * disposed after it has served its purpose.
     * </p>
     * 
     * @return the splash screen image, or <code>null</code> if none
     */
    public Image getSplashImage();

    /**
     * Returns the version number of this product.
     * <p>
     * The recommended format is <it>X</it>.<it>Y</it> where <it>X</it> and 
     * <it>Y</it> are the major and minor version numbers, respectively; for
     * example: 5.02. However, arbitrary strings are allowed.
     * </p>
     *
     * @return the product version number
     */
    public String getVersion();

    /**
     * Returns a <code>URL</code> for the welcome page.
     * Products designed to run "headless" typically would not have such an page.
     * 
     * @return the welcome page, or <code>null</code> if none
     */
    public URL getWelcomePageURL();

    /**
     * Returns a <code>URL</code> for the master help set.
     * Products designed to run "headless" typically would not have a help set.
     * 
     * @return the URL, or <code>null</code> if none
     */
    public URL getMasterHelpSetURL();
}
