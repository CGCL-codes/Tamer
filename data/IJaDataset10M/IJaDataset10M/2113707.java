package com.google.code.wifimaps.web;

import com.google.code.wifimaps.entidade.Usuario;

/**
 * Um usuario 'ajustado' para a p�gina JSP.
 * 
 * @author Marco Tulio
 * @author Tesso Martins
 *
 */
public class UsuarioBean extends Usuario {

    /** 'Controle de vers�o' da classe serializavel. 
	 *  Este n�o � um atributo de classe obrigat�rio.
	 */
    static final long serialVersionUID = 1L;

    /** Verifica se a string � vazia.
	 * @param str String a ser testada.
	 * @return Verdadeiro se a string � vazia.
	 */
    private boolean ehVazia(String str) {
        return (str == null || (str.trim().equals("")));
    }

    /**
	 * Clona o usuario informado como um UsuarioBean.
	 * @param u Usuario a ser 'clonada'.
	 * @return Usuario como UsuarioBean.
	 */
    public static UsuarioBean usuarioComoBean(UsuarioBean u) {
        if (u == null) {
            return null;
        }
        UsuarioBean usuarioBean = new UsuarioBean();
        usuarioBean.codigoUsuario = u.getCodigo();
        usuarioBean.loginUsuario = u.getLogin();
        usuarioBean.senhaUsuario = u.getSenha();
        return usuarioBean;
    }

    /**
	 * Clona a usuarioBean com um Usuario.
	 * @param usuarioBean UsuarioBean a ser clonado.
	 * @return Novo Usuario.
	 */
    public static Usuario beanComoUsuario(UsuarioBean usuarioBean) {
        if (usuarioBean == null) {
            return null;
        }
        Usuario usuario = new Usuario();
        usuario.setCodigo(usuarioBean.getCodigo());
        usuario.setLogin(usuarioBean.getLogin());
        usuario.setSenha(usuarioBean.getSenha());
        return usuario;
    }
}
