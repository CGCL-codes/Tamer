package net.pluce.nxt.network;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import net.pluce.nxt.network.impl.AbstractRobotConnection;

/**
 * Mock implementation of the Robot Connection. Read a byte file filled with int and act as a robot connection
 * Write in the file with DataOutputStream.writeInt() to be sure this class can read it
 * @author Pluce
 */
public class MockRobotConnection extends AbstractRobotConnection implements RobotConnection {

    File fakeRobot;

    /**
	 * Construct a fake robot sending the content of a file as network data
	 * @param fakeRobotFileName file name where data should be read
	 */
    public MockRobotConnection(String fakeRobotFileName) {
        fakeRobot = new File(fakeRobotFileName);
    }

    @Override
    public InputStream getInputStream() throws IllegalStateException {
        try {
            return new FileInputStream(fakeRobot);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public OutputStream getOutputStream() throws IllegalStateException {
        try {
            return new FileOutputStream(fakeRobot);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean close() throws RobotConnectionException {
        return true;
    }

    @Override
    public boolean isOpened() {
        return true;
    }

    @Override
    protected boolean initiate() {
        return true;
    }
}
