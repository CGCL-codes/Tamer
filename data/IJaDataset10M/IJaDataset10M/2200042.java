package fr.soleil.bensikin.xml;

import java.beans.ExceptionListener;
import java.beans.XMLEncoder;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import fr.soleil.bensikin.Bensikin;

public class PersistentColumnModelEncoder extends XMLEncoder {

    public PersistentColumnModelEncoder() throws FileNotFoundException {
        super(getOutputStream());
        ExceptionListener exceptionListener = new ExceptionListener() {

            public void exceptionThrown(Exception e) {
                e.printStackTrace();
            }
        };
        this.setExceptionListener(exceptionListener);
    }

    private static OutputStream getOutputStream() throws FileNotFoundException {
        String path = Bensikin.getPathToResources();
        String absp = path + "/beans";
        File f = new File(absp);
        if (!f.canWrite()) {
            f.mkdir();
        }
        absp += "/columnModel" + ".bean";
        OutputStream out = new BufferedOutputStream(new FileOutputStream(new File(absp)));
        return out;
    }
}
