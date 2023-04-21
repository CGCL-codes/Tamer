package br.com.beyondclick.model.repository;

import javax.ejb.EJB;
import org.apache.openejb.api.LocalClient;
import br.com.beyondclick.model.entity.Usuario;
import br.com.gentech.commons.model.repository.Repository;
import br.com.gentech.commons.model.repository.RepositoryDbUnitTest;

@LocalClient
public class UsuarioRepositoryTest extends RepositoryDbUnitTest<Usuario, String> {

    @EJB
    private Repository<Usuario, String> repo;

    @Override
    protected Repository<Usuario, String> getRepository() {
        return repo;
    }

    protected Usuario newEntity() {
        Usuario usuario = new Usuario();
        usuario.setNome("acdesouza");
        usuario.setSenha("senha");
        return usuario;
    }
}
