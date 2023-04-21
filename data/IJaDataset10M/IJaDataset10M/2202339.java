package net.sf.jncu.protocol.v1_0.app;

import java.io.IOException;
import java.io.InputStream;
import net.sf.jncu.protocol.DockCommandFromNewton;

/**
 * This command sends a package to the desktop. It's issued repeatedly in
 * response to a <tt>kDBackupPackages</tt> message.
 * 
 * <pre>
 * 'apkg'
 * length
 * package id
 * package data
 * </pre>
 */
public class DPackage extends DockCommandFromNewton {

    /** <tt>kDPackage</tt> */
    public static final String COMMAND = "apkg";

    private int id;

    private byte[] data;

    /**
	 * Creates a new command.
	 */
    public DPackage() {
        super(COMMAND);
    }

    @Override
    protected void decodeCommandData(InputStream data) throws IOException {
        setId(ntohl(data));
        byte[] b = new byte[getLength() - 4];
        readAll(data, b);
        setData(b);
    }

    /**
	 * Get the package id.
	 * 
	 * @return the id.
	 */
    public int getId() {
        return id;
    }

    /**
	 * Set the package id.
	 * 
	 * @param id
	 *            the id.
	 */
    protected void setId(int id) {
        this.id = id;
    }

    /**
	 * Get the data.
	 * 
	 * @return the data.
	 */
    public byte[] getData() {
        return data;
    }

    /**
	 * Set the data.
	 * 
	 * @param data
	 *            the data.
	 */
    protected void setData(byte[] data) {
        this.data = data;
    }
}
