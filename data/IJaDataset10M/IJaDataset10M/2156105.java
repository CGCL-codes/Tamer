package net.sf.appia.jgcs;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import net.sf.appia.protocols.group.Endpt;
import net.sf.appia.protocols.group.ViewID;
import net.sf.jgcs.JGCSException;
import net.sf.jgcs.membership.MembershipID;

public class AppiaMembershipID extends ViewID implements MembershipID {

    private static final long serialVersionUID = -8971339993160631028L;

    public AppiaMembershipID(long ltime, Endpt coord) {
        super(ltime, coord);
    }

    public AppiaMembershipID() {
        super();
    }

    public int compareTo(Object o) {
        if (o instanceof AppiaMembershipID) {
            ViewID id = (ViewID) o;
            if (this.equals(id)) return 0;
            if (ltime < id.ltime) return -1; else return 1;
        } else throw new ClassCastException("Could not compare with object " + o);
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
