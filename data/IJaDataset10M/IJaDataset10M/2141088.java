package orm;

import java.io.Serializable;

public class Tcp_publicacion implements Serializable {

    public Tcp_publicacion() {
    }

    private void this_setOwner(Object owner, int key) {
        if (key == orm.ORMConstants.KEY_TCP_PUBLICACION_TCP_PERSONAPERS) {
            this.tcp_personapers = (orm.Tcp_persona) owner;
        }
    }

    org.orm.util.ORMAdapter _ormAdapter = new org.orm.util.AbstractORMAdapter() {

        public void setOwner(Object owner, int key) {
            this_setOwner(owner, key);
        }
    };

    private int pub_id;

    private int pub_monto;

    private String pub_descripcion;

    private orm.Tcp_persona tcp_personapers;

    private void setPub_id(int value) {
        this.pub_id = value;
    }

    public int getPub_id() {
        return pub_id;
    }

    public int getORMID() {
        return getPub_id();
    }

    public void setPub_monto(int value) {
        this.pub_monto = value;
    }

    public int getPub_monto() {
        return pub_monto;
    }

    public void setPub_descripcion(String value) {
        this.pub_descripcion = value;
    }

    public String getPub_descripcion() {
        return pub_descripcion;
    }

    public void setTcp_personapers(orm.Tcp_persona value) {
        if (tcp_personapers != null) {
            tcp_personapers.tcp_publicacion.remove(this);
        }
        if (value != null) {
            value.tcp_publicacion.add(this);
        }
    }

    public orm.Tcp_persona getTcp_personapers() {
        return tcp_personapers;
    }

    /**
	 * This method is for internal use only.
	 */
    public void setORM_Tcp_personapers(orm.Tcp_persona value) {
        this.tcp_personapers = value;
    }

    private orm.Tcp_persona getORM_Tcp_personapers() {
        return tcp_personapers;
    }

    public String toString() {
        return String.valueOf(getPub_id());
    }
}
