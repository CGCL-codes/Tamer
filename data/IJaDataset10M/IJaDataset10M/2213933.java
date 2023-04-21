package br.edu.ufabc.gtrnp.helppo.business;

import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import br.edu.ufabc.gtrnp.helppo.persistence.entity.Administrador;
import br.edu.ufabc.gtrnp.helppo.persistence.entity.Usuario;

public class AdministradorBusiness {

    private static AdministradorBusiness business = new AdministradorBusiness();

    private AdministradorBusiness() {
    }

    public static AdministradorBusiness getInstance() {
        return business;
    }

    public void concedePermissaoAdministrador(Usuario usuario) {
        Administrador administrador = new Administrador();
        administrador.setUuid(usuario.getUUID());
        GenericDAO dao = new GenericDAO();
        dao.getCurrentSession().persist(administrador);
    }

    public void retiraPermissaoAdministrador(Usuario usuario) {
        Administrador administrador = new Administrador();
        administrador.setUuid(usuario.getUUID());
        GenericDAO dao = new GenericDAO();
        dao.getCurrentSession().delete(administrador);
    }

    public boolean isAdministrador(Usuario usuario) {
        GenericDAO dao = new GenericDAO();
        String sql = "FROM Administrador WHERE uuid =:a";
        Query query = dao.getCurrentSession().createQuery(sql);
        query.setString("a", usuario.getUUID());
        return query.uniqueResult() != null;
    }

    @SuppressWarnings("unchecked")
    public List<Usuario> recuperaAdministradores() {
        GenericDAO dao = new GenericDAO();
        String sql = "FROM Administrador";
        Query query = dao.getCurrentSession().createQuery(sql);
        List lista = query.list();
        List<Usuario> usuarios = new ArrayList<Usuario>();
        for (Object object : lista) {
            Administrador administrador = (Administrador) object;
            usuarios.add(UsuarioBusiness.getInstance().getUsuarioByUUID(administrador.getUuid()));
        }
        return usuarios;
    }
}
