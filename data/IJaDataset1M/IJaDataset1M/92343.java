package servicos.impl.usuario;

import javax.ejb.Stateless;
import javax.persistence.PersistenceException;
import org.jboss.annotation.ejb.LocalBinding;
import servicos.interfaces.usuario.IPerfil;
import constantes.EjbNames;
import listagem.ListagemInfo;
import entidades.usuario.Perfil;

@Stateless
@LocalBinding(jndiBinding = EjbNames.PERFIL)
public class PerfilImpl implements IPerfil {

    public void atualizarPerfil(Perfil perfil) throws PersistenceException {
    }

    public Perfil bucarPerfil(Long id) throws PersistenceException {
        return null;
    }

    public ListagemInfo buscarPerfils(String valor, ListagemInfo info_listagem) throws PersistenceException {
        return null;
    }

    public void salvarPerfil(Perfil perfil) throws PersistenceException {
    }
}
