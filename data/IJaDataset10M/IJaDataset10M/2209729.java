package uk.ac.cam.caret.minibix.archive.impl.file;

import java.io.*;
import uk.ac.cam.caret.minibix.archive.api.LowLevelStorageException;
import uk.ac.cam.caret.minibix.archive.impl.file.lock.*;

class Mapper {

    private static final int SUB_MAX = 1000;

    private File data;

    private Dispenser key;

    private Flock lock;

    private int sup, sub = SUB_MAX;

    Mapper(File base, Flock lock) throws LowLevelStorageException {
        if (!base.exists()) throw new LowLevelStorageException("Mapper base directory does not exist!");
        data = new File(base, "data");
        if (!data.exists()) throw new LowLevelStorageException("Mapper data directory does not exist!");
        key = new Dispenser(new File(base, "key"), new File(base, "keylock"));
        key.initTicket(1000);
        this.lock = lock;
    }

    private synchronized String getTmpKey() throws LowLevelStorageException {
        if (sub == SUB_MAX) {
            sup = key.dispenseNewTicket();
            sub = 0;
        }
        sub++;
        return sup + "-" + sub;
    }

    void addKey(int key, int value) throws LowLevelStorageException {
        try {
            String tmpnam = getTmpKey();
            File file = new File(data, tmpnam);
            BufferedWriter out = new BufferedWriter(new FileWriter(file));
            out.write(Integer.toString(value));
            out.close();
            lock.acquireExclusive();
            try {
                file.renameTo(new File(data, Integer.toString(key)));
            } finally {
                lock.drop();
            }
        } catch (IOException x) {
            throw new LowLevelStorageException("Could not write to mapper");
        }
    }

    int getKey(int key) throws LowLevelStorageException {
        try {
            lock.acquireShared();
            try {
                File file = new File(data, Integer.toString(key));
                if (!file.exists()) return -1;
                BufferedReader in = new BufferedReader(new FileReader(file));
                String value = in.readLine();
                in.close();
                return Integer.parseInt(value);
            } finally {
                lock.drop();
            }
        } catch (IOException x) {
            throw new LowLevelStorageException("Could not read from mapper");
        } catch (NumberFormatException x) {
            throw new LowLevelStorageException("Could not read from mapper");
        }
    }

    void deleteKey(int key) throws LowLevelStorageException {
        lock.acquireExclusive();
        try {
            File file = new File(data, Integer.toString(key));
            file.delete();
        } finally {
            lock.drop();
        }
    }
}
