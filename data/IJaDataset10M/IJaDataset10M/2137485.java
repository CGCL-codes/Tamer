package org.humboldt.cassia.zk.render;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import org.humboldt.cassia.core.jdo.Conjunto;
import org.humboldt.cassia.core.jdo.PerfilDocumentacion;
import org.humboldt.cassia.core.jdo.Usuario;
import org.zkoss.zul.Label;
import org.zkoss.zul.Row;
import org.zkoss.zul.RowRenderer;

public class BusquedaConjuntosWSGridRenderer implements RowRenderer {

    private PerfilDocumentacion perfilBusqueda;

    private Collection<String> atributos;

    private HashMap<String, Usuario> usuarios;

    public void render(Row row, Object data) throws Exception {
        if (data instanceof Conjunto) {
            Conjunto conjunto = (Conjunto) data;
            new Label(conjunto.getNombre()).setParent(row);
            if (usuarios.get(conjunto.getUsuario()) != null) {
                new Label(usuarios.get(conjunto.getUsuario()).getNombres()).setParent(row);
            } else {
                new Label("").setParent(row);
            }
        } else if (data instanceof String) {
            new Label((String) data).setParent(row);
        } else {
            System.out.println("PASO");
        }
    }

    /**
	 * @return the perfilBusqueda
	 */
    public PerfilDocumentacion getPerfilBusqueda() {
        return perfilBusqueda;
    }

    /**
	 * @param perfilBusqueda the perfilBusqueda to set
	 */
    public void setPerfilBusqueda(PerfilDocumentacion perfilBusqueda) {
        this.perfilBusqueda = perfilBusqueda;
    }

    /**
	 * @return the atributos
	 */
    public Collection<String> getAtributos() {
        return atributos;
    }

    /**
	 * @param atributos the atributos to set
	 */
    public void setAtributos(Collection<String> atributos) {
        this.atributos = atributos;
    }

    /**
	 * @param usuarios the usuarios to set
	 */
    public void setUsuarios(Collection<Usuario> usuarios) {
        this.usuarios = new HashMap<String, Usuario>();
        for (Iterator itUsuarios = usuarios.iterator(); itUsuarios.hasNext(); ) {
            Usuario usuario = (Usuario) itUsuarios.next();
            this.usuarios.put(usuario.getLogin(), usuario);
        }
    }
}
