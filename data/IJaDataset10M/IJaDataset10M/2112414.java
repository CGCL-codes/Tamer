package br.org.rfccj.siap.web.struts2.actions;

import java.util.Collection;
import java.util.Vector;
import br.org.rfccj.siap.Perfil;

public class PerfilListAction extends ListAction<Perfil> {

    private static final long serialVersionUID = 1L;

    private Collection<Perfil> lista;

    public Collection<Perfil> getLista() {
        return copyCurrentPageFrom(new Vector<Perfil>(lista));
    }

    public String execute() throws Exception {
        lista = contexto().perfis().ordenadosPorNome();
        createPageNumbers(lista.size());
        return SUCCESS;
    }
}
