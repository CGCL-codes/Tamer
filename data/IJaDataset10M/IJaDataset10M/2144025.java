package net.sf.jgcs.jgroups;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import net.sf.jgcs.JGCSException;
import net.sf.jgcs.membership.MembershipID;
import org.jgroups.Address;
import org.jgroups.ViewId;

public class JGroupsMembershipID extends ViewId implements MembershipID {

    private static final long serialVersionUID = -1585298721725820115L;

    public JGroupsMembershipID() {
        super();
    }

    public JGroupsMembershipID(Address coord_addr, long id) {
        super(coord_addr, id);
    }

    public JGroupsMembershipID(Address coord_addr) {
        super(coord_addr);
    }

    public byte[] getBytes() throws JGCSException {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(byteStream);
            super.writeExternal(out);
            out.close();
        } catch (IOException e) {
            throw new JGCSException("Could not write to output stream", e);
        }
        return byteStream.toByteArray();
    }

    public void fromBytes(byte[] bytes) throws JGCSException {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(bytes);
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(byteStream);
            super.readExternal(in);
            in.close();
        } catch (IOException e) {
            throw new JGCSException("Could not read from input stream", e);
        } catch (ClassNotFoundException e) {
            throw new JGCSException("Could not read from input stream", e);
        }
    }
}
