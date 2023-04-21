package ejb;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpa.Usuario;

@Stateless
@LocalBean
public class UsuarioFacade extends AbstractFacade<Usuario> {

    @PersistenceContext
    private EntityManager em;

    public UsuarioFacade() {
        super(Usuario.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public Usuario obterPorNome(String nome) {
        Usuario usuario = null;
        try {
            CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
            CriteriaQuery<Usuario> cq = cb.createQuery(Usuario.class);
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.where(cb.equal(rt.get("nome"), nome));
            usuario = getEntityManager().createQuery(cq).getSingleResult();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (usuario == null) {
            System.out.println("Veio Nulo!!");
        }
        return usuario;
    }

    public String gerarHash(String frase) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(frase.getBytes());
            byte[] bytes = md.digest();
            StringBuilder s = new StringBuilder(0);
            for (int i = 0; i < bytes.length; i++) {
                int parteAlta = ((bytes[i] >> 4) & 0xf) << 4;
                int parteBaixa = bytes[i] & 0xf;
                if (parteAlta == 0) {
                    s.append('0');
                }
                s.append(Integer.toHexString(parteAlta | parteBaixa));
            }
            return s.toString();
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
