package pl.umk.webclient.impl.security;

import javax.security.auth.x500.X500Principal;
import javax.wsdl.Binding;
import javax.wsdl.Operation;
import javax.wsdl.PortType;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.XMLStreamWriter;
import pl.edu.icm.unicore.security.dsig.DSigException;
import pl.edu.icm.unicore.security.user.UserAssertion;
import com.intel.gpe.security.Credential;
import com.intel.gpe.util.stax.MessageWriterExcepion;
import com.intel.gpe.wsclient.Handler;
import com.intel.gpe.wsclient.HandlerException;
import com.intel.gpe.wsrfclient.handlers.HeaderUtil;

/**
 * 
 * The client-side handler that inserts Unicore User SAML
 * token into the SOAP header  
 * 
 * @author Krzysztof Benedyczak
 * @author Alexander Lukichev
 */
public class UserTokenHandler implements Handler {

    private UserAssertion user;

    /**
	 * (Re-)initialize the handler.
	 * 
	 * @param userDN - the principal of the user
     * @param consignorDN - the principal of the consignor
	 * @throws DSigException 
	 */
    public UserTokenHandler(X500Principal userDN, Credential consignor) throws DSigException {
        this.user = new UserAssertion(userDN.getName(), consignor.getCertificateChain()[0].getSubjectX500Principal().getName());
    }

    public void handle(XMLStreamReader in, XMLStreamWriter out, PortType portType, Binding binding, Operation operation) throws HandlerException {
        try {
            HeaderUtil.addHeader(in, out, user.getXML());
        } catch (MessageWriterExcepion e) {
            throw new HandlerException(e);
        }
    }
}
