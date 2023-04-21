package br.com.tiagoramos.financeiro.negocio;

import java.io.Serializable;
import br.com.tiagoramos.financeiro.dao.UsuarioDAO;
import br.com.tiagoramos.financeiro.exception.LoginException;
import br.com.tiagoramos.financeiro.exception.PersistenciaException;
import br.com.tiagoramos.financeiro.model.Usuario;
import br.com.tiagoramos.financeiro.utils.Criptografia;

public class LoginBO implements Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 7426629812931780004L;

    /**Objeto da classe LoginDAO usado para comunicar-se com a camada de 
	 * persistencia.*/
    private UsuarioDAO usuarioDAO;

    /**Instancia o objeto desta classe.*/
    public LoginBO() {
        super();
        usuarioDAO = new UsuarioDAO();
    }

    /**
	 * Faz a validacao do login e senha de acordo com a matriz de requisitos.
	 * 
	 * IMPORTANTE: Sempre que chamar este matodo consultar o valor do atributo
	 * avisoPrevioExpiracao para saber se deve ou nao exibir aviso de que a senha
	 * vai expirar.
	 * @throws PersistenciaException 
	 * @throws br.com.tiagoramos.financeiro.exception.PersistenciaException 
	 * @throws PersistenciaException LoginException
	 * */
    public Usuario validarLogin(String login, String senha) throws LoginException, PersistenciaException {
        Usuario usuario = usuarioDAO.buscaUsuario(login);
        if (usuario.getSenha().equals(Criptografia.md5(senha))) {
            return usuario;
        } else {
            throw new LoginException(LoginException.USUARIO_INVALIDO);
        }
    }
}
