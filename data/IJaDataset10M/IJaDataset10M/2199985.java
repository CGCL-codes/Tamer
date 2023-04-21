package edu.mta.ok.nworkshop.utils;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

/**
 * Helper class that provide static methods for saving/loading objects from/to files.
 */
public final class FileUtils {

    /**
	 * A generic method that loads an object from a given file
	 * 
	 * @param <T> The generic type that we want to load from the file. Notice that the type class should implement Serializable.
	 * @param fileName The name of the file that we want to load the object from
	 * @return the object loaded from the given file, or null in case there was an error
	 */
    @SuppressWarnings(value = "unchecked")
    public static <T extends Serializable> T loadDataFromFile(String fileName) {
        T retVal = null;
        ObjectInputStream ois = null;
        try {
            ois = new ObjectInputStream(new FileInputStream(fileName));
            retVal = (T) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (ois != null) {
                FileUtils.outputClose(ois);
            }
        }
        return retVal;
    }

    /**
	 * Generic method for saving data to a binary file
	 * 
	 * @param <T> The generic type we want to save to a file. Notice that the type class should implement Serializable
	 * @param object the object that we want to write to a file
	 * @param fileName the name of the file that we want to write the object to 
	 * @return an indication if the process succeeded
	 */
    public static <T extends Serializable> boolean saveDataToFile(T object, String fileName) {
        ObjectOutputStream objOut = null;
        boolean retVal = false;
        try {
            objOut = new ObjectOutputStream(new FileOutputStream(new File(fileName)));
            objOut.writeObject(object);
            retVal = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (objOut != null) {
                FileUtils.outputClose(objOut);
            }
        }
        return retVal;
    }

    /**
	 * Open an output stream for the given file name.
	 * 
	 * @param fileName the name of the new file the stream will be opened to.
	 * @return a new ObjectOutputStream connected to the sent file name.
	 */
    public static ObjectOutputStream getObjectOutputStream(String fileName) {
        ObjectOutputStream retVal = null;
        try {
            retVal = new ObjectOutputStream(new FileOutputStream(new File(fileName)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retVal;
    }

    public static void outputClose(final Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                System.out.println("Error while closing object. error: " + closeable);
            }
        }
    }
}
