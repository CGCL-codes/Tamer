package com.ecomponentes.formularios.usuario.bo;

import java.util.ArrayList;
import java.util.List;
import com.ecomponentes.dao.BeanUtilsBO;
import com.ecomponentes.formularios.contato.to.ContatoTO;
import com.ecomponentes.formularios.usuario.dao.UsuarioDAO;
import com.ecomponentes.formularios.usuario.to.UsuarioTO;
import com.ecomponentes.hibernate.usuario.TbUsuario;

/**
 * 
 * @author Enrique Campos Quaggio
 */
public class UsuarioBO {

    private UsuarioDAO dao;

    /** Construtor padr�o. */
    public UsuarioBO() {
        dao = new UsuarioDAO();
    }

    /**
	 * Utilizado para transformar uma List de objetos hibernate para um
	 * array de usuarioTO.
	 * @param lista Lista a se transformada.
	 * @return Array de usuarioTO.
	 */
    private UsuarioTO[] carregarUsuarios(List lista) {
        UsuarioTO[] to = new UsuarioTO[lista.size()];
        for (int ct = 0; ct < lista.size(); ++ct) {
            to[ct] = new UsuarioTO();
            try {
                BeanUtilsBO.copyPropertiesUsuario(to[ct], (TbUsuario) lista.get(ct));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return to;
    }

    /**
	 * Utilizado para carregar todos os regsitro do banco de dados.
	 * @return Array de SosUsuarioTO populado.
	 */
    public UsuarioTO[] selecionarTodos() {
        List lista = dao.selecionarTodos();
        return carregarUsuarios(lista);
    }

    /**
	 * Utilizado por validar um usu�rio.
	 * @param to Usuario a ser validade.
	 * @return True caso existir, sen�o false.
	 */
    public UsuarioTO validarLogin(UsuarioTO to) {
        List lista = dao.localizarLogin(to.login);
        if (lista.size() > 0) {
            UsuarioTO[] tos = carregarUsuarios(lista);
            for (int ct = 0; ct < tos.length; ++ct) {
                if (tos[ct].senha.equals(to.senha)) {
                    return tos[ct];
                }
            }
        }
        return null;
    }

    public UsuarioTO[] getUsuarios() {
        List lista = dao.selecionarUsuariosContato();
        UsuarioTO[] usuarioTO = new UsuarioTO[lista.size()];
        for (int ct = 0; ct < lista.size(); ++ct) {
            usuarioTO[ct] = new UsuarioTO();
            BeanUtilsBO.copyPropertiesUsuario(usuarioTO[ct], (TbUsuario) lista.get(ct));
        }
        return usuarioTO;
    }

    public UsuarioTO[] getUsuarios(String campo, String valor) {
        List lista = dao.selecionarUsuariosContato(campo, valor);
        UsuarioTO[] usuarioTO = new UsuarioTO[lista.size()];
        for (int ct = 0; ct < lista.size(); ++ct) {
            usuarioTO[ct] = new UsuarioTO();
            BeanUtilsBO.copyPropertiesUsuario(usuarioTO[ct], (TbUsuario) lista.get(ct));
        }
        return usuarioTO;
    }

    public UsuarioTO getUsuario(Integer id) {
        UsuarioTO usuarioTO = new UsuarioTO();
        try {
            BeanUtilsBO.copyPropertiesUsuario(usuarioTO, (TbUsuario) dao.selecionarUsuario(id));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return usuarioTO;
    }

    public void removeUsuario(Integer id) {
        TbUsuario usuario = new TbUsuario();
        try {
            BeanUtilsBO.copyPropertiesUsuario(usuario, getUsuario(id));
            dao.removeObject(usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void inserirAlterar(UsuarioTO to) {
        TbUsuario usuario = new TbUsuario();
        try {
            BeanUtilsBO.copyPropertiesUsuario(usuario, to);
            if (usuario.getIdUsuario().longValue() == 0L) {
                usuario.setIdUsuario(null);
            }
            dao.saveUpdateObject(usuario);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
	 * Utilizado para carregar todos os regsitro do banco de dados.
	 * @return Array de SosUsuarioTO populado.
	 */
    public ContatoTO[] selecionarContatosTodos() {
        List lista = dao.selecionarTodos();
        ContatoTO[] to = new ContatoTO[lista.size()];
        for (int ct = 0; ct < lista.size(); ++ct) {
            to[ct] = new ContatoTO();
            try {
                BeanUtilsBO.copyPropertiesContato(to[ct], ((TbUsuario) lista.get(ct)).getTbContato1());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return to;
    }

    /**
	 * Utilizado para carregar todos os regsitro do banco de dados.
	 * @return Array de SosUsuarioTO populado.
	 */
    public List selecionarContatosTodosList() {
        List lista = dao.selecionarTodos();
        List contatosList = new ArrayList();
        for (int ct = 0; ct < lista.size(); ++ct) {
            contatosList.add(((TbUsuario) lista.get(ct)).getTbContato1());
        }
        return contatosList;
    }
}
