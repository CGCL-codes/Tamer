package fedora.server.storage;

import java.util.Date;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import org.xml.sax.InputSource;
import fedora.server.Context;
import fedora.server.errors.DatastreamNotFoundException;
import fedora.server.errors.MethodNotFoundException;
import fedora.server.errors.ObjectIntegrityException;
import fedora.server.errors.RepositoryConfigurationException;
import fedora.server.errors.ServerException;
import fedora.server.errors.StreamIOException;
import fedora.server.errors.GeneralException;
import fedora.server.errors.UnsupportedTranslationException;
import fedora.server.storage.translation.DOTranslator;
import fedora.server.storage.RepositoryReader;
import fedora.server.storage.types.BMechDSBindSpec;
import fedora.server.storage.types.DigitalObject;
import fedora.server.storage.types.MethodDef;
import fedora.server.storage.types.MethodParmDef;
import fedora.server.storage.types.MethodDefOperationBind;
import fedora.server.storage.service.ServiceMapper;

/**
 * A BMechReader based on a DigitalObject.
 *
 * @author cwilper@cs.cornell.edu
 * @version $Id: SimpleBMechReader.java 5220 2006-11-20 13:52:20Z cwilper $
 */
public class SimpleBMechReader extends SimpleServiceAwareReader implements BMechReader {

    private ServiceMapper serviceMapper;

    public SimpleBMechReader(Context context, RepositoryReader repoReader, DOTranslator translator, String exportFormat, String storageFormat, String encoding, InputStream serializedObject) throws ObjectIntegrityException, StreamIOException, UnsupportedTranslationException, ServerException {
        super(context, repoReader, translator, exportFormat, storageFormat, encoding, serializedObject);
        serviceMapper = new ServiceMapper(GetObjectPID());
    }

    /**
     * Alternate constructor for when a DigitalObject is already
     * available for some reason.
     */
    public SimpleBMechReader(Context context, RepositoryReader repoReader, DOTranslator translator, String exportFormat, String encoding, DigitalObject obj) {
        super(context, repoReader, translator, exportFormat, encoding, obj);
        serviceMapper = new ServiceMapper(GetObjectPID());
    }

    public MethodDef[] getServiceMethods(Date versDateTime) throws DatastreamNotFoundException, ObjectIntegrityException, RepositoryConfigurationException, GeneralException {
        return serviceMapper.getMethodDefs(new InputSource(new ByteArrayInputStream(getMethodMapDatastream(versDateTime).xmlContent)));
    }

    public MethodParmDef[] getServiceMethodParms(String methodName, Date versDateTime) throws MethodNotFoundException, ServerException {
        return getParms(getServiceMethods(versDateTime), methodName);
    }

    public MethodDefOperationBind[] getServiceMethodBindings(Date versDateTime) throws DatastreamNotFoundException, ObjectIntegrityException, RepositoryConfigurationException, GeneralException {
        return serviceMapper.getMethodDefBindings(new InputSource(new ByteArrayInputStream(getWSDLDatastream(versDateTime).xmlContent)), new InputSource(new ByteArrayInputStream(getMethodMapDatastream(versDateTime).xmlContent)));
    }

    public BMechDSBindSpec getServiceDSInputSpec(Date versDateTime) throws DatastreamNotFoundException, ObjectIntegrityException, RepositoryConfigurationException, GeneralException {
        return serviceMapper.getDSInputSpec(new InputSource(new ByteArrayInputStream(getDSInputSpecDatastream(versDateTime).xmlContent)));
    }

    public InputStream getServiceMethodsXML(Date versDateTime) throws DatastreamNotFoundException, ObjectIntegrityException {
        return new ByteArrayInputStream(getMethodMapDatastream(versDateTime).xmlContent);
    }

    /**
     * Get the parms out of a particular service method definition.
     * @param methods
     * @return
     */
    private MethodParmDef[] getParms(MethodDef[] methods, String methodName) throws MethodNotFoundException, ServerException {
        for (int i = 0; i < methods.length; i++) {
            if (methods[i].methodName.equalsIgnoreCase(methodName)) {
                return methods[i].methodParms;
            }
        }
        throw new MethodNotFoundException("[getParms] The behavior mechanism object, " + m_obj.getPid() + ", does not have a service method named '" + methodName);
    }
}
