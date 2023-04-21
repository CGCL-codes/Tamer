package model.persistent;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import model.IModel;

public class FilePersistentModel extends PersistentModel {

    private static final long serialVersionUID = 1;

    private File file;

    public void setFile(File modelFile) {
        this.file = modelFile;
    }

    public File getFile() {
        return file;
    }

    public IModel load() {
        try {
            BufferedInputStream buffer = new BufferedInputStream(new FileInputStream(file));
            ObjectInput i = new ObjectInputStream(buffer);
            setModel((IModel) i.readObject());
            buffer.close();
            return getModel();
        } catch (ClassNotFoundException e1) {
            throw new RuntimeException(e1);
        } catch (IOException e2) {
            throw new RuntimeException(e2);
        }
    }

    public void save() {
        try {
            BufferedOutputStream buffer = new BufferedOutputStream(new FileOutputStream(file));
            ObjectOutput o = new ObjectOutputStream(buffer);
            o.writeObject(getModel());
            buffer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
