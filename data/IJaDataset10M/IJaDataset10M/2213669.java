package fedora.server.storage;

import java.io.InputStream;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import fedora.server.errors.ServerException;
import fedora.server.storage.types.AuditRecord;
import fedora.server.storage.types.DSBindingMapAugmented;
import fedora.server.storage.types.Datastream;
import fedora.server.storage.types.DigitalObject;
import fedora.server.storage.types.DisseminationBindingInfo;
import fedora.server.storage.types.Disseminator;
import fedora.server.storage.types.MethodDef;
import fedora.server.storage.types.MethodParmDef;
import fedora.server.storage.types.ObjectMethodsDef;

/**
 * A partial implementation of {@link DOReader} for use in unit tests. Add more
 * mocking to this class as needed, or override methods in sub-classes.
 * 
 * @author Jim Blake
 */
public class MockDOReader implements DOReader {

    protected final DigitalObject theObject;

    public MockDOReader(DigitalObject theObject) {
        this.theObject = theObject;
    }

    public Datastream GetDatastream(String datastreamID, Date versDateTime) throws ServerException {
        List<Datastream> datastreams = theObject.datastreams(datastreamID);
        if (datastreams.isEmpty()) {
            return null;
        }
        Collections.sort(datastreams, new Comparator<Datastream>() {

            public int compare(Datastream o1, Datastream o2) {
                return o2.DSCreateDT.compareTo(o1.DSCreateDT);
            }
        });
        if (versDateTime == null) {
            return datastreams.get(0);
        } else {
            for (Datastream datastream : datastreams) {
                if (datastream.DSCreateDT.before(versDateTime)) {
                    return datastream;
                }
            }
            return null;
        }
    }

    public String GetObjectLabel() throws ServerException {
        return theObject.getLabel();
    }

    public String GetObjectPID() throws ServerException {
        return theObject.getPid();
    }

    public String GetObjectState() throws ServerException {
        return theObject.getState();
    }

    public String getContentModelId() throws ServerException {
        return theObject.getContentModelId();
    }

    public Date getCreateDate() throws ServerException {
        return theObject.getCreateDate();
    }

    public String getFedoraObjectType() throws ServerException {
        int t = theObject.getFedoraObjectType();
        if (t == DigitalObject.FEDORA_OBJECT) {
            return "O";
        } else {
            if (t == DigitalObject.FEDORA_BMECH_OBJECT) {
                return "M";
            } else {
                return "D";
            }
        }
    }

    public Date getLastModDate() throws ServerException {
        return theObject.getLastModDate();
    }

    public String getOwnerId() throws ServerException {
        return theObject.getOwnerId();
    }

    /** For now, an empty return satisfies our testing needs. */
    public Disseminator[] GetDisseminators(Date versDateTime, String state) throws ServerException {
        return new Disseminator[0];
    }

    public InputStream ExportObject(String format, String exportContext) throws ServerException {
        throw new RuntimeException("MockDOReader.ExportObject() not implemented.");
    }

    public List<AuditRecord> getAuditRecords() throws ServerException {
        throw new RuntimeException("MockDOReader.getAuditRecords() not implemented.");
    }

    public String[] GetBehaviorDefs(Date versDateTime) throws ServerException {
        throw new RuntimeException("MockDOReader.GetBehaviorDefs() not implemented.");
    }

    public Datastream getDatastream(String datastreamID, String versionID) throws ServerException {
        throw new RuntimeException("MockDOReader.getDatastream() not implemented.");
    }

    public Datastream[] GetDatastreams(Date versDateTime, String state) throws ServerException {
        throw new RuntimeException("MockDOReader.GetDatastreams() not implemented.");
    }

    public Date[] getDatastreamVersions(String datastreamID) throws ServerException {
        throw new RuntimeException("MockDOReader.getDatastreamVersions() not implemented.");
    }

    public DisseminationBindingInfo[] getDisseminationBindingInfo(String defPID, String methodName, Date versDateTime) throws ServerException {
        throw new RuntimeException("MockDOReader.getDisseminationBindingInfo() not implemented.");
    }

    public Disseminator GetDisseminator(String disseminatorID, Date versDateTime) throws ServerException {
        throw new RuntimeException("MockDOReader.GetDisseminator() not implemented.");
    }

    public Date[] getDisseminatorVersions(String dissID) throws ServerException {
        throw new RuntimeException("MockDOReader.getDisseminatorVersions() not implemented.");
    }

    public DSBindingMapAugmented[] GetDSBindingMaps(Date versDateTime) throws ServerException {
        throw new RuntimeException("MockDOReader.GetDSBindingMaps() not implemented.");
    }

    public String[] getObjectHistory(String PID) throws ServerException {
        throw new RuntimeException("MockDOReader.getObjectHistory() not implemented.");
    }

    public MethodParmDef[] getObjectMethodParms(String defPID, String methodName, Date versDateTime) throws ServerException {
        throw new RuntimeException("MockDOReader.getObjectMethodParms() not implemented.");
    }

    public InputStream GetObjectXML() throws ServerException {
        throw new RuntimeException("MockDOReader.GetObjectXML() not implemented.");
    }

    public String[] ListDatastreamIDs(String state) throws ServerException {
        throw new RuntimeException("MockDOReader.ListDatastreamIDs() not implemented.");
    }

    public String[] ListDisseminatorIDs(String state) throws ServerException {
        throw new RuntimeException("MockDOReader.ListDisseminatorIDs() not implemented.");
    }

    public ObjectMethodsDef[] listMethods(Date versDateTime) throws ServerException {
        throw new RuntimeException("MockDOReader.listMethods() not implemented.");
    }

    public MethodDef[] listMethods(String defPID, Date versDateTime) throws ServerException {
        throw new RuntimeException("MockDOReader.listMethods() not implemented.");
    }
}
