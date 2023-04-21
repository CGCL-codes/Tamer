package br.ufmg.ubicomp.droidguide.util;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.Persistence;
import org.hibernate.HibernateException;

public class HibernateUtil {

    private boolean criarEnttyManagerFactory = false;

    private EntityManagerFactory emf;

    private EntityManager em;

    protected static HibernateUtil instancia;

    public EntityManagerFactory getEmf() {
        return emf;
    }

    /**
	 * Verifica se a conex�o com o pool do banco de teste j� foi feita. Se n�o
	 * foi feita, conecta com o pool do banco de dados de teste para o arquivo
	 * de configura��es fornecido.
	 * 
	 * @param hibernateCFG
	 * @throws HibernateException
	 */
    public void criarEntityManagerFactory() throws HibernateException {
        if (!criarEnttyManagerFactory) {
            synchronized (HibernateUtil.class) {
                if (!criarEnttyManagerFactory) {
                    emf = Persistence.createEntityManagerFactory("hibernate-jpa");
                    criarEnttyManagerFactory = true;
                }
            }
        }
    }

    public void destruirEntityManagerFactory() throws HibernateException {
        if (criarEnttyManagerFactory) {
            synchronized (HibernateUtil.class) {
                if (criarEnttyManagerFactory) {
                    emf.close();
                    emf = null;
                    criarEnttyManagerFactory = false;
                }
            }
        }
    }

    public EntityManager openEntitiyManager() {
        if (em == null) {
            em = getEmf().createEntityManager();
            em.setFlushMode(FlushModeType.COMMIT);
        }
        return em;
    }

    public EntityManager closeEntitiyManager() {
        if (em != null) {
            em.close();
            em = null;
        }
        return em;
    }
}
