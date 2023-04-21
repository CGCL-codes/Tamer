package br.com.sgac.controle;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;
import br.com.sgac.entidades.Usuario;
import br.com.sgac.util.HibernateUtil;

public class UsuarioControle {

    private Usuario usuario = new Usuario();

    public List validaLogin(String login, String senha) {
        Session s = HibernateUtil.getSessionFactory();
        List<Object> retorno = new ArrayList<Object>();
        Query q = s.createQuery("FROM Usuario where login = ? and senha = ?");
        q.setString(0, login);
        q.setString(1, senha);
        this.usuario = (Usuario) q.uniqueResult();
        retorno.add(usuario);
        retorno.add(new Date());
        return retorno;
    }

    public boolean existeUsuario(String login) {
        Session s = HibernateUtil.getSessionFactory();
        Usuario usuario = null;
        Query q = s.createQuery("from Usuario where login = ? ").setString(0, login);
        usuario = (Usuario) q.uniqueResult();
        s.close();
        if (usuario == null) {
            return false;
        } else {
            return true;
        }
    }

    public String gravarUsuario(Usuario usuario) {
        Session s = HibernateUtil.getSessionFactory();
        if (existeUsuario(usuario.getLogin()) == true) {
            return "Este nome de usuário já existe";
        } else {
            try {
                Transaction t = s.beginTransaction();
                s.saveOrUpdate(usuario);
                t.commit();
                return "sucesso";
            } catch (HibernateException e) {
                return "falha";
            } finally {
                s.close();
            }
        }
    }

    public String atualizarUsuario(Usuario usuario) {
        Session s = HibernateUtil.getSessionFactory();
        try {
            Transaction t = s.beginTransaction();
            s.update(usuario);
            t.commit();
            return "sucesso";
        } catch (HibernateException e) {
            return "falha";
        } finally {
            s.close();
        }
    }

    public List getListUsuarioPorLogin(String usuario) {
        Session s = HibernateUtil.getSessionFactory();
        Query q = s.createQuery("from Usuario where login like ?");
        q.setString(0, "%" + usuario + "%");
        return q.list();
    }

    public String excluirUsuario(Usuario usuario) {
        Session s = HibernateUtil.getSessionFactory();
        try {
            Transaction t = s.beginTransaction();
            s.delete(usuario);
            t.commit();
            return "sucesso";
        } catch (HibernateException e) {
            return "falha";
        } finally {
            s.close();
        }
    }

    public static void main(String[] args) {
        UsuarioControle uc = new UsuarioControle();
        List l = uc.getListUsuarioPorLogin("wagner");
        Usuario u = (Usuario) l.get(0);
        System.out.println(u.getId() + u.getLogin() + u.getSenha());
        u.setSenha("21a");
        System.out.println(uc.atualizarUsuario(u));
        List l2 = uc.getListUsuarioPorLogin("wagner");
        Usuario u2 = (Usuario) l2.get(0);
        System.out.println(u2.getId() + u2.getLogin() + u2.getSenha());
    }
}
