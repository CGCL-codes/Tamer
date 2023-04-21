package in980.rgms.persistence;

import in980.rgms.domain.Membro;
import in980.rgms.domain.TipoMembro;
import in980.rgms.domain.Usuario;
import in980.rgms.utils.HibernateUtils;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;

public class MembroDAO extends Dao<Membro> {

    @SuppressWarnings("unchecked")
    protected List<Membro> montaLista(Membro object, Session s) {
        List<Membro> listaRetorno = s.createQuery("from " + getTabela() + " t where upper(t.nome) like :nome ").setString("nome", "%" + object.getNome().toUpperCase() + "%").list();
        return listaRetorno;
    }

    @Override
    protected String getTabela() {
        return "Membro";
    }

    @SuppressWarnings("unchecked")
    @Override
    protected Class getTipoClasse() {
        return Membro.class;
    }

    @SuppressWarnings("unchecked")
    public List<Membro> buscaPorTipo(TipoMembro tipo) throws Exception {
        Session s = HibernateUtils.getSessionFactory().getCurrentSession();
        Query q = s.createQuery("from " + getTabela() + " t where t.tipo = :tipo ");
        q.setInteger("tipo", tipo.getId());
        List<Membro> listaRetorno = q.list();
        return listaRetorno;
    }

    @SuppressWarnings("unchecked")
    public List<Membro> buscaPorIDTurnAround(TipoMembro tipo) throws Exception {
        Session s = HibernateUtils.getSessionFactory().getCurrentSession();
        Query q = s.createQuery("from " + getTabela() + " t where t.ID = :ID ");
        q.setInteger("tipo", tipo.getId());
        List<Membro> listaRetorno = q.list();
        return listaRetorno;
    }

    public Membro buscaPeloUsuario(Usuario usuario) throws Exception {
        Session s = HibernateUtils.getSessionFactory().getCurrentSession();
        Query q = s.createQuery("from " + getTabela() + " t where t.ajc$interField$in980_rgms_domain_aspect_Autenticacao$usuario.ID = :usuario ");
        q.setInteger("usuario", usuario.getID());
        List<Membro> lista = q.list();
        if (lista.isEmpty()) return null;
        return lista.get(0);
    }
}
