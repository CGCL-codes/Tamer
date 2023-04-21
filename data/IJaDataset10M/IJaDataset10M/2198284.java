package lt.bsprendimai.ddesk;

import java.io.Serializable;

/**
 * See {@link ProxyBase}
 * @author Aleksandr Panzin (JAlexoid) alex@activelogic.eu
 */
public class ClientAccessProxy extends ProxyBase implements Serializable {

    /**
	 *
	 */
    private static final long serialVersionUID = -1131707028637627016L;

    private ClientAccessor clientAccessor;

    /** Creates a new instance of ClientAccessProxy */
    public ClientAccessProxy() {
    }

    public String getCompanyId() {
        if (clientAccessor.getCompanyId() != null) return clientAccessor.getCompanyId().toString(); else return "";
    }

    public void setCompanyId(String id) {
        try {
            clientAccessor.setCompanyId(new Integer(id.trim()));
        } catch (Exception ex) {
        }
    }

    public String getPersonId() {
        if (clientAccessor.getPersonId() != null) return clientAccessor.getPersonId().toString(); else return "";
    }

    public void setPersonId(String id) {
        try {
            if (id != null && id.equals("-1")) clientAccessor.setPersonId(null);
            clientAccessor.setPersonId(new Integer(id.trim()));
        } catch (Exception ex) {
        }
    }

    public ClientAccessor getClientAccessor() {
        return clientAccessor;
    }

    public void setClientAccessor(ClientAccessor clientAccessor) {
        this.clientAccessor = clientAccessor;
    }
}
