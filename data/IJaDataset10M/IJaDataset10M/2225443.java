package co.edu.unal.ungrid.image.dicom.database;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Constructor;
import co.edu.unal.ungrid.image.dicom.core.AttributeList;
import co.edu.unal.ungrid.image.dicom.core.DicomInputStream;

/**
 * <p>
 * This class allows the reconstruction of a database from the stored instance
 * files, such as when the database schema model has been changed.
 * </p>
 * 
 * 
 */
public class RebuildDatabaseFromInstanceFiles {

    private static void processFileOrDirectory(DatabaseInformationModel databaseInformationModel, File file) {
        if (file.isDirectory() && !file.getName().toUpperCase().equals("CVS")) {
            System.err.println("Recursing into directory " + file);
            try {
                File listOfFiles[] = file.listFiles();
                for (int i = 0; i < listOfFiles.length; ++i) {
                    processFileOrDirectory(databaseInformationModel, listOfFiles[i]);
                }
            } catch (Exception e) {
                e.printStackTrace(System.err);
            }
        } else if (file.isFile()) {
            if (!file.isHidden()) {
                System.err.println("Doing file " + file);
                try {
                    DicomInputStream dfi = new DicomInputStream(new BufferedInputStream(new FileInputStream(file)));
                    AttributeList list = new AttributeList();
                    list.read(dfi);
                    dfi.close();
                    databaseInformationModel.insertObject(list, file.getAbsolutePath());
                } catch (Exception e) {
                    e.printStackTrace(System.err);
                }
            } else {
                System.err.println("Skipping hidden " + file);
            }
        } else {
            System.err.println("Not a directory or file " + file);
        }
    }

    /**
	 * <p>
	 * Read the DICOM files listed on the command line, load them into the
	 * specified model and store the database files in the specified location.
	 * </p>
	 * 
	 * @param arg
	 *            the class name of the model, the (full) path of the database
	 *            file prefix, and a list of DICOM file names or directories
	 */
    public static void main(String arg[]) {
        RebuildDatabaseFromInstanceFiles ourselves = new RebuildDatabaseFromInstanceFiles();
        if (arg.length >= 3) {
            String databaseModelClassName = arg[0];
            String databaseFileName = arg[1];
            if (databaseModelClassName.indexOf('.') == -1) {
                databaseModelClassName = "co.edu.unal.ungrid.image.dicom.database." + databaseModelClassName;
            }
            DatabaseInformationModel databaseInformationModel = null;
            try {
                Class<?> classToUse = ourselves.getClass().getClassLoader().loadClass(databaseModelClassName);
                Class<?>[] parameterTypes = { databaseFileName.getClass() };
                Constructor<?> constructorToUse = classToUse.getConstructor(parameterTypes);
                Object[] args = { databaseFileName };
                databaseInformationModel = (DatabaseInformationModel) (constructorToUse.newInstance(args));
            } catch (Exception e) {
                e.printStackTrace(System.err);
                System.exit(0);
            }
            int i = 2;
            while (i < arg.length) {
                String name = arg[i++];
                File file = new File(name);
                processFileOrDirectory(databaseInformationModel, file);
            }
        } else {
            System.err.println("Usage: java co.edu.unal.ungrid.image.dicom.database.RebuildDatabaseFromInstanceFiles databaseModelClassName databaseFilePathPrefix databaseFileName path(s)");
        }
    }
}
