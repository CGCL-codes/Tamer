package testes;

import util.SQLiteDialect;
import modelo.Usuario;
import java.util.Collection;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import org.junit.Test;

/**
	* @author fonini
*/
public class TesteUsuario {

    private EntityManager em;

    public TesteUsuario() {
        this.em = Persistence.createEntityManagerFactory("SigepiUP").createEntityManager();
    }

    @Test
    public void inserir() {
        Usuario u1 = new Usuario();
        u1.setNome("Felipe Antonio");
        u1.setLogin("felipe");
        u1.setSenha("123456");
        u1.setTipo("ADMIN");
        Usuario u2 = new Usuario();
        u2.setNome("Fernanda Magnello");
        u2.setLogin("fernanda.mag");
        u2.setSenha("654321");
        u2.setTipo("TECNICO");
        Usuario u3 = new Usuario();
        u3.setNome("Rubens Samara");
        u3.setLogin("rubens");
        u3.setSenha("rub123");
        u3.setTipo("USUARIO");
        em.getTransaction().begin();
        em.persist(u1);
        em.persist(u2);
        em.persist(u3);
        em.getTransaction().commit();
    }

    @Test
    public void listar() {
        Collection<Usuario> lista = em.createQuery("from Usuario").getResultList();
        for (Usuario p : lista) {
            System.out.println(p.getId() + " - " + p.getNome() + " - " + p.getLogin() + " - " + p.getSenha());
        }
    }
}
