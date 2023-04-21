package negocios;

import negocios.excecoes.EntradaInvalidaException;

/**
 * Classe que herda da classe UsuarioAbstrato. Na versao atual, um UsuarioComum
 * nao difere do UsuarioAbstrato
 * 
 * @author Jonathan Brilhante
 * @author Jose Rafael
 * @author Nata Venancio
 * @author Renato Almeida
 * 
 * @version 2.0
 */
public class UsuarioComum extends UsuarioAbstrato {

    /**
	 * Cria um usuario comum a partir dos dados obrigatorios
	 * 
	 * @param login
	 *            o login do usuario comum
	 * @param nome
	 *            o nome do usuario comum
	 * @param email
	 *            o email do usuario comum
	 * @param senha
	 *            a senha do usuario comum
	 * @param telefone
	 *            o telefone do usuario comum
	 * @throws EntradaInvalidaException
	 *             caso algum dos dados obrigatorios esteja faltando ou sejam
	 *             invalidos
	 */
    public UsuarioComum(String login, String nome, String email, String senha, String telefone) throws EntradaInvalidaException {
        super(login, nome, email, senha, telefone);
    }
}
