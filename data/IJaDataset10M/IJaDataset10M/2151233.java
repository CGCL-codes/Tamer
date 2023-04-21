package es.eucm.eadventure.common.auxiliar;

import java.io.File;

/**
 * File filter abstract class, combining the java.io.FileFilter and the
 * javax.swing.filechooser.FileFilter.
 * 
 * @author Bruno Torijano Bueno
 */
public abstract class FileFilter extends javax.swing.filechooser.FileFilter implements java.io.FileFilter {

    @Override
    public abstract boolean accept(File file);

    @Override
    public abstract String getDescription();
}
