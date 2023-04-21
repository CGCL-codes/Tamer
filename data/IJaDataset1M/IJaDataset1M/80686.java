package jalk;

/**
 * @author tmfelser
 *
 */
public class TcpMessageUpdate implements JalkTcpMessage, java.io.Serializable {

    Identification ident = null;

    TcpMessageUpdate() {
    }

    TcpMessageUpdate(Identification id) {
        setId(id);
    }

    public final void setId(Identification id) {
        this.ident = id;
    }

    public final int getId() {
        return ident.getId();
    }

    public final Identification getIdent() {
        return ident;
    }

    public final short getType() {
        return TYPE_UPDATE;
    }

    public final String toString() {
        return "UPDATE message Identification = " + ident.toString();
    }
}
