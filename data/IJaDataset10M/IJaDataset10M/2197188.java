package net.teqlo.db.remote;

import net.teqlo.TeqloException;
import net.teqlo.bus.messages.security.Authenticate;
import net.teqlo.bus.messages.security.GetUserFqn;
import net.teqlo.bus.messages.security.RejectAlias;
import net.teqlo.db.impl.AbstractSecurity;

public class RemoteSecurity extends AbstractSecurity {

    public RemoteSecurity(RemoteXmlDatabase db) {
        super(db);
    }

    public String authenticate(String id, String pw) throws TeqloException {
        return (String) ((RemoteXmlDatabase) db).requestReply(new Authenticate(id, pw));
    }

    public void rejectAlias(String id, String pw) throws TeqloException {
        ((RemoteXmlDatabase) db).requestReply(new RejectAlias(id, pw));
    }

    public String getUserFqn(String id) throws TeqloException {
        return (String) ((RemoteXmlDatabase) db).requestReply(new GetUserFqn(id));
    }

    /**
     * Authenticates a user id and digested pw+onetimekey, returns teqlo user id
     */
    @SuppressWarnings("unused")
    public String authenticate(String id, String oneTimeKey, String digest, String digestAlgorithm) throws TeqloException {
        throw new TeqloException(this, id, null, "Secure authentication not supported yet");
    }
}
