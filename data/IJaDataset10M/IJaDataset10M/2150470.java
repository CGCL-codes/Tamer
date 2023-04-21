package ru.amse.jsynchro.fileSystem;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import ru.amse.jsynchro.kernel.DirectoryTree;
import ru.amse.jsynchro.kernel.IDirectoryTree;
import ru.amse.jsynchro.logging.ILogger;
import ru.amse.jsynchro.net.Command;
import ru.amse.jsynchro.net.stream.ProtocolInputStream;
import ru.amse.jsynchro.net.stream.ProtocolOutputStream;

@SuppressWarnings("serial")
public class RemoteFileSystem implements FileSystem {

    private OutputStream socketOut;

    private ObjectOutput myOout;

    private InputStream socketIn;

    private ObjectInput oin;

    private ILogger logger;

    public RemoteFileSystem(InputStream socketIn, OutputStream socketOut, ILogger logger) throws IOException {
        this.socketIn = socketIn;
        this.socketOut = socketOut;
        myOout = new ObjectOutputStream(socketOut);
        oin = new ObjectInputStream(socketIn);
        this.logger = logger;
    }

    public ObjectInput getObjectInput() {
        return oin;
    }

    public int auth(String userName, String userPassword) throws IOException {
        myOout.writeObject(userName);
        myOout.writeObject(userPassword);
        String answer = null;
        try {
            answer = (String) oin.readObject();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
        return Integer.parseInt(answer);
    }

    public boolean deleteFile(File f) throws IOException {
        myOout.writeObject(new Command(Command.DELETE_FILE));
        myOout.writeObject(f.getAbsolutePath());
        try {
            if (oin.readObject().equals("ok")) {
                return true;
            }
            if (oin.readObject().equals("not_ok")) {
                return false;
            }
        } catch (ClassNotFoundException e) {
            logger.exception(e);
        }
        return false;
    }

    public String getTreeRoot() throws IOException {
        myOout.writeObject(new Command(Command.GET_TREE_ROOT));
        String result = null;
        try {
            result = (String) oin.readObject();
        } catch (ClassNotFoundException e) {
            logger.exception(e);
        }
        return result;
    }

    public IDirectoryTree getDirectoryTree(String root) throws IOException {
        myOout.writeObject(new Command(Command.GET_TREE));
        myOout.writeObject(root);
        IDirectoryTree tree = null;
        try {
            tree = (DirectoryTree) oin.readObject();
            tree.setFS(this);
        } catch (ClassNotFoundException e) {
            logger.exception(e);
        }
        return tree;
    }

    public InputStream openInputStream(String path, long length) throws IOException {
        myOout.writeObject(new Command(Command.GET_FILE));
        myOout.writeObject(path);
        return new ProtocolInputStream(socketIn, length, myOout);
    }

    public OutputStream openOutputStream(String path, long length) throws IOException {
        myOout.writeObject(new Command(Command.WRITE_FILE));
        myOout.writeObject(path);
        myOout.writeObject(new Long(length));
        try {
            oin.readObject();
        } catch (ClassNotFoundException e) {
            logger.exception(e);
        }
        return new ProtocolOutputStream(socketOut, length, oin);
    }

    public void closeInputStream(InputStream is) throws IOException {
        myOout.writeObject(new Command(Command.CLOSE_INPUT_STREAM));
    }

    public void closeSession() throws IOException {
        myOout.writeObject(new Command(Command.END));
        socketIn.close();
    }

    public void makeDir(File dir) throws IOException {
        myOout.writeObject(new Command(Command.MAKE_DIRECTORY));
        myOout.writeObject(dir.getAbsolutePath());
    }

    public File[] listFiles(File dir) throws IOException {
        myOout.writeObject(new Command(Command.LIST_FILES));
        myOout.writeObject(dir.getAbsolutePath());
        File[] result = null;
        try {
            result = (File[]) oin.readObject();
        } catch (ClassNotFoundException e) {
            logger.exception(e);
        }
        return result;
    }

    public void closeOutputStream(OutputStream is) throws IOException {
        System.out.println("closeOutputStream");
        myOout.writeObject(new Command(Command.CLOSE_OUTPUT_STREAM));
    }

    public long getFileLength(String path) throws IOException {
        myOout.writeObject(new Command(Command.GET_FILE_LENGTH));
        myOout.writeObject(path);
        long result = 0;
        try {
            result = (Long) oin.readObject();
        } catch (ClassNotFoundException e) {
            logger.exception(e);
        }
        return result;
    }

    public boolean isDirectory(File file) throws IOException {
        myOout.writeObject(new Command(Command.IS_DIRECTORY));
        myOout.writeObject(file.getPath());
        Boolean result = false;
        try {
            result = (Boolean) oin.readObject();
        } catch (ClassNotFoundException e) {
            logger.exception(e);
        }
        return result;
    }

    public void attrExecute(File file) throws IOException {
        myOout.writeObject(new Command(Command.ATTR_EXECUTE));
        myOout.writeObject(file.getPath());
    }

    public void attrHidden(File file) throws IOException {
        myOout.writeObject(new Command(Command.ATTR_HIDDEN));
        myOout.writeObject(file.getPath());
    }

    public void attrRead(File file) throws IOException {
        myOout.writeObject(new Command(Command.ATTR_READ));
        myOout.writeObject(file.getPath());
    }

    public void attrReadOnly(File file) throws IOException {
        myOout.writeObject(new Command(Command.ATTR_READONLY));
        myOout.writeObject(file.getPath());
    }

    public void attrWrite(File file) throws IOException {
        myOout.writeObject(new Command(Command.ATTR_WRITE));
        myOout.writeObject(file.getPath());
    }
}
