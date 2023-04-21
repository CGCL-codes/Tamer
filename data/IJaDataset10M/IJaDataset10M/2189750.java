package g4mfs.stubs.service;

public interface MathFactoryServiceAddressing extends g4mfs.stubs.service.MathFactoryService {

    public g4mfs.stubs.MathFactoryPortType getMathFactoryPortTypePort(org.apache.axis.message.addressing.EndpointReferenceType reference) throws javax.xml.rpc.ServiceException;
}
