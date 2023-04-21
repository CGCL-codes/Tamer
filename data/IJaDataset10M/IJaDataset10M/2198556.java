package fr.soleil.TangoArchiving.ArchivingApi.DbImpl.db.mysql;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import fr.esrf.Tango.AttrWriteType;
import fr.esrf.Tango.DevState;
import fr.esrf.Tango.ErrSeverity;
import fr.esrf.TangoDs.TangoConst;
import fr.soleil.TangoArchiving.ArchivingApi.ConfigConst;
import fr.soleil.TangoArchiving.ArchivingApi.DbImpl.BasicRecorderDataBaseApiImpl;
import fr.soleil.TangoArchiving.ArchivingApi.DbImpl.IDataBaseApiTools;
import fr.soleil.TangoArchiving.ArchivingTools.Tools.ArchivingException;
import fr.soleil.TangoArchiving.ArchivingTools.Tools.GlobalConst;
import fr.soleil.TangoArchiving.ArchivingTools.Tools.ScalarEvent;
import fr.soleil.TangoArchiving.ArchivingTools.Tools.StringFormater;

public class MySqlRecorderDataBaseApiImpl extends BasicRecorderDataBaseApiImpl {

    public MySqlRecorderDataBaseApiImpl(IDataBaseApiTools dataBaseApiTools) {
        super(dataBaseApiTools);
    }

    public void insert_ScalarData(ScalarEvent scalarEvent) throws ArchivingException {
        String attName = scalarEvent.getAttribute_complete_name().trim();
        long time = scalarEvent.getTimeStamp();
        Timestamp timeSt = new Timestamp(time);
        int writable = scalarEvent.getWritable();
        Object value = scalarEvent.getValue();
        StringBuffer query = new StringBuffer();
        String selectFields = "";
        if (writable == AttrWriteType._READ || writable == AttrWriteType._WRITE) {
            selectFields = "?, ?";
        } else {
            selectFields = "?, ?, ?";
        }
    }

    /**
	 * @roseuid 45C9969F00C4
	 */
    protected void prepareQueryInsertModeRecord() {
    }

    /**
	 * @param attribute_name
	 * @throws fr.soleil.TangoArchiving.ArchivingTools.Tools.ArchivingException
	 * @roseuid 45EC21D600A3
	 */
    public void updateModeRecord(String attribute_name) throws ArchivingException {
        System.out.println("SPJZ ==> MySqlRecorderDataBaseApiImpl.updateModeRecord");
    }
}
