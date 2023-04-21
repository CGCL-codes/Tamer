package de.pegasos.swing.icon;

import java.awt.Dimension;
import java.io.File;
import javax.swing.Icon;

/**
 * Is an standard Interface for plattform specific Icon handling
 * @author fragro
 */
public interface IconHandler {

    /**
     * Gets the Path of an Icon file by IconSize, IconContext and iconName
     * @param iconSize is the Size of the Icon prvided by IconSize enumeration
     * @param iconContext is the Context/Location of the icon provided by IconContext enumeration
     * @param iconName the exact name of the icon
     * @return the Path to the specified Icon file
     */
    public String getIconPath(IconSize iconSize, IconContext iconContext, String iconName);

    /**
     * Gets the Path of an Icon file with custom Dimensions by IconContext and iconName
     * @param dimension is the custom Dimension of the icon
     * @param iconContext is the IconContext/Location of the Icon
     * @param iconName is the exact name of the Icon
     * @return the Path to specified Icon file
     */
    public String getIconPath(Dimension dimension, IconContext iconContext, String iconName);

    /**
     * Gets the Path to an Icon with custom width and height by IconContext and iconName
     * @param width is the custom width of the Icon
     * @param height is the custom height of the Icon
     * @param iconContext is the Context/Location of the Icon provided by IconContext enumeration
     * @param iconName is the exact name of the icon
     * @return the path to the icon file
     */
    public String getIconPath(int width, int height, IconContext iconContext, String iconName);

    /**
     * Gets an Icon file by size, context and name of an Icon
     * @param iconSize is the size of an icon provided by the IconSize enumeration
     * @param iconContext is the theme context/location of the icon provided by IconContext enumeration
     * @param iconName Name of the specific icon
     * @return the Icon file
     */
    public File getIconFile(IconSize iconSize, IconContext iconContext, String iconName);

    /**
     * Gets an Icon file with individual dimensions by context and name of an Icon
     * @param dimension is an individual dimension of the icon
     * @param iconContext is the theme context/location of the icon provided by IconContext enumeration
     * @param iconName Name of the specific icon
     * @return the Icon file
     */
    public File getIconFile(Dimension dimension, IconContext iconContext, String iconName);

    /**
     * Gets an Icon file with individual width and height by context and name of an Icon
     * @param width is the individual width of the icon
     * @param height is the individual height of the icon
     * @param iconContext is the theme context/location of the icon provided by IconContext enumeration
     * @param iconName Name of the specific icon
     * @return the Icon file
     */
    public File getIconFile(int width, int height, IconContext iconContext, String iconName);

    /**
     * Gets an Icon by IconSize, IconContext and iconName
     * @param iconSize is the size of the icon provided by IconSize Enumeration
     * @param iconContext the context/location of the icon provided by IconContext enumeration
     * @param iconName is the correct name of the icon
     * @return the icon
     */
    public Icon getIcon(IconSize iconSize, IconContext iconContext, String iconName);

    /**
     * Gets an Icon with custom dimensions by context and name of an Icon
     * @param dimension is the Dimension of the icon
     * @param iconContext is the context/location of the icon
     * @param iconName is the correct name of the icon
     * @return the icon
     */
    public Icon getIcon(Dimension dimension, IconContext iconContext, String iconName);

    /**
     * Gets an Icon with custom width and height by IconContext and iconName
     * @param width is the custom widht of the icon
     * @param height is the custom height of the icon
     * @param iconContext is the context/location of the icon provided by IconContext enumeration
     * @param iconName is the correct name of the icon
     * @return the icon
     */
    public Icon getIcon(int width, int height, IconContext iconContext, String iconName);

    /**
     * Gets the name of an icon by an specified mimetype
     * @param mimetype is the mimetype
     * @return the icon specified by the mimetype
     */
    public String getIconNameByMimeType(String mimetype);

    /**
     * Gets an icon name by an file extension
     * @param extension the file extension
     * @return the icon specified by file extension
     */
    public String getIconNameByFileExtension(String extension);

    /**
     * Gets an generic icon for unknown content
     * @return the generic icon for unknown content
     */
    public String getIconNameUnknownMimeType();
}
