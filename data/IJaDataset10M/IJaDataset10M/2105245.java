package ca.ucalgary.cpsc.ebe.fitClipse.testPersistence;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import aaftt.TestResultStorage;

public class TestResultPersistence implements IPersistence {

    private File file;

    public TestResultPersistence(String path) {
        init(path);
    }

    @Override
    public void init(String path) {
        File file = new File(path);
        File folder = new File(file.getParent());
        if (!folder.exists()) folder.mkdirs();
        this.file = file;
        try {
            this.file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Object load() throws FileNotFoundException {
        TestResultStorage loadedObject;
        InputStream in = null;
        ObjectInputStream o;
        try {
            in = new FileInputStream(file);
            o = new ObjectInputStream(in);
            loadedObject = (TestResultStorage) o.readObject();
            o.close();
            in.close();
        } catch (IOException e) {
            return null;
        } catch (ClassNotFoundException e) {
            return null;
        } finally {
            try {
                in.close();
            } catch (IOException e) {
            }
        }
        return loadedObject;
    }

    @Override
    public void save(Object object) {
        OutputStream out;
        try {
            out = new FileOutputStream(file);
            ObjectOutputStream oout = new ObjectOutputStream(out);
            oout.writeObject(object);
            oout.close();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
