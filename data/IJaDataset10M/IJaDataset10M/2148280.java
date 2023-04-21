package net.sourceforge.jppt.records;

import java.io.IOException;
import java.io.InputStream;
import org.apache.poi.util.LittleEndian;

/**
 * UserEditAtom implementation.
 * 
 * @author Dmitry Lukin (nikula at users.sourceforge.net)
 */
public class UserEditAtom extends Record {

    public static final short sid = 4085;

    /**
	 * Id of slide currently selected in view
	 */
    public int lastSlideID;

    /**
	 * Major and minor app version that did the save
	 */
    public int version;

    /**
	 * File offset of UsereditAtom of the previous incremental save. 0 after a full save
	 */
    public int offsetLastEdit;

    /**
	 * File offset to persist pointers for this save operation
	 */
    public int offsetPersistDirectory;

    /**
	 * Persist reference to the document persist object
	 */
    public int documentRef;

    /**
	 * Seed value for persist object id management
	 */
    public int maxPersistWritten;

    /**
	 * View type see table below
	 */
    public int lastViewType;

    public UserEditAtom(RecordHeader op, InputStream in) throws IOException {
        super(op, in);
    }

    /**
     * Checks the sid matches the expected side for this record
     *
     * @param id   the expected sid.
     */
    protected void validateSid(int id) {
        if (id != sid) {
            throw new RecordFormatException("Not a UserEditAtom record");
        }
    }

    protected void fillFields(RecordHeader op, InputStream in) throws IOException {
        lastSlideID = LittleEndian.readInt(in);
        version = LittleEndian.readInt(in);
        offsetLastEdit = LittleEndian.readInt(in);
        offsetPersistDirectory = LittleEndian.readInt(in);
        documentRef = LittleEndian.readInt(in);
        maxPersistWritten = LittleEndian.readInt(in);
        lastViewType = LittleEndian.readShort(in);
    }

    public short getSid() {
        return UserEditAtom.sid;
    }
}
