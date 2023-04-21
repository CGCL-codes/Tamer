package org.nakedobjects.persistence.cache;

import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.ObjectPerstsistenceException;
import org.nakedobjects.object.io.Memento;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import org.apache.log4j.Logger;

public class SnapShotWriter {

    private static final Logger LOG = Logger.getLogger(SnapShotWriter.class);

    private ObjectOutputStream oos;

    public void open() throws ObjectPerstsistenceException {
        File tempFile = file(snapshotFilename, version, true);
        LOG.info("Saving objects in " + tempFile + "...");
        oos = null;
        try {
            oos = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(tempFile)));
        } catch (FileNotFoundException e) {
            throw new ObjectPerstsistenceException("Failed to find file " + tempFile, e);
        } catch (IOException e) {
            throw new ObjectPerstsistenceException("Failed to write to file " + tempFile, e);
        } finally {
        }
    }

    public void close() {
        if (oos != null) {
            try {
                oos.close();
            } catch (IOException e) {
                LOG.error("Failed to close file " + tempFile, e);
            }
        }
        File file = file(snapshotFilename, version, false);
        tempFile.renameTo(file);
        LOG.info("File renamed as " + file);
    }

    public void writeInt(int i) {
        oos.writeInt(i);
    }

    public void writeClassName(String className) {
        oos.writeObject(className);
    }

    public void writeOid(Object oid) {
        oos.writeObject(className);
    }

    public void writeNakedObject(NakedObject object) {
        Memento memento = new Memento(object);
        LOG.debug("write 2: " + i++ + " " + specification.getFullName() + "/" + memento);
        oos.writeObject(memento);
    }
}
