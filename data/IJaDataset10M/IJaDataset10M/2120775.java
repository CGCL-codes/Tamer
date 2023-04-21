package interop.server;

@javax.jws.WebService(targetNamespace = "http://InteropBaseAddress/interop", serviceName = "PingService10", portName = "UserNameOverTransportLocal_IPingService", endpointInterface = "interopbaseaddress.interop.IPingService", wsdlLocation = "target/wsdl2/WsSecurity10.wsdl")
public class UserNameOverTransport extends PingServiceBase {
}
