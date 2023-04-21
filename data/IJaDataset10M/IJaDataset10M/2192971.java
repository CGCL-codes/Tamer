package br.com.edawir.negocio.servico;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.com.edawir.integracao.dao.UserDAO;
import br.com.edawir.integracao.model.User;

/**
 * Classe de servi�os do/a Usu�rio
 * 
 * @author Grupo EDAWIR
 * @since 27/01/2012
 */
@Service
public class UsuarioServico extends ServicoGenerico<User> {

    @Autowired
    private UserDAO usuarioDAO;

    /**
	 * @return usuarioDAO o/a usuarioDAO
	 */
    @Override
    protected UserDAO getDAO() {
        return usuarioDAO;
    }

    /**
	 * M�todo que valida as regras de neg�cio
	 * 
	 * @param entidade o/a entidade
	 * @throws Exception tratamento de exce��o ou erro que possa ocorrer
	 */
    @Override
    protected void validarEntidade(User entidade) throws Exception {
    }
}
