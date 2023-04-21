package appbaratoextreme.repositorio;

import appbaratoextreme.classesBasicas.Contrato;
import appbaratoextreme.util.Hibernate;
import appbaratoextreme.util.MethodsUtil;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author MarcosPaulo
 */
public class RepositorioContrato {

    private static RepositorioContrato repositorioContrato;

    /**
     * 
     * @return 
     */
    public static RepositorioContrato getRepositorioContrato() {
        if (repositorioContrato != null) {
            return repositorioContrato;
        }
        return new RepositorioContrato();
    }

    private RepositorioContrato() {
        MethodsUtil.systemOutInstanciandoInfo(RepositorioContrato.class);
    }

    /**
     * 
     * @param contrato
     * @throws Exception 
     */
    public void cadastrarContrato(final Contrato contrato) throws Exception {
        Session session = Hibernate.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            session.save(contrato);
            session.getTransaction().commit();
        } catch (HibernateException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new HibernateException("HIBERNATE Erro no Cadastrar Contrato: ", e);
        } catch (Exception e) {
            throw new Exception("GERAL Erro no Cadastrar Contrato: ", e);
        }
    }

    /**
     * 
     * @param contratos
     *          
     * @throws Exception 
     */
    public void deletarContrato(final Contrato... contratos) throws Exception {
        for (Contrato contrato : contratos) {
            Session session = Hibernate.getSessionFactory().getCurrentSession();
            try {
                session.beginTransaction();
                session.delete(contrato);
                session.getTransaction().commit();
            } catch (HibernateException e) {
                if (session != null) {
                    session.getTransaction().rollback();
                }
                throw new HibernateException("HIBERNATE Erro no Deletar Contrato: ", e);
            } catch (Exception e) {
                throw new Exception("GERAL Erro no Deletar Contrato: ", e);
            }
        }
    }

    public void atualizarContrato(final Contrato... contratos) throws Exception {
        for (Contrato contrato : contratos) {
            Session session = Hibernate.getSessionFactory().getCurrentSession();
            try {
                session.beginTransaction();
                session.update(contrato);
                session.getTransaction().commit();
            } catch (HibernateException e) {
                if (session != null) {
                    session.getTransaction().rollback();
                }
                throw new HibernateException("HIBERNATE Erro no Atualizar Contrato: ", e);
            } catch (Exception e) {
                throw new Exception("GERAL Erro no Atualizar Contrato: ", e);
            }
        }
    }

    /**
     * 
     * @param hql
     * @return
     * @throws Exception
     * @throws HibernateException 
     */
    public List<Contrato> listarContrato(final String hql) throws Exception, HibernateException {
        SessionFactory sessionFactory = Hibernate.getSessionFactory();
        Session session = sessionFactory.getCurrentSession();
        try {
            session.beginTransaction();
            List<Contrato> listContrato = session.createQuery(hql).list();
            session.getTransaction().commit();
            return listContrato;
        } catch (HibernateException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new HibernateException("HIBERNATE Erro no Listar Contrato: ", e);
        } catch (Exception e) {
            throw new Exception("GERAL Erro no Listar Contrato: ", e);
        }
    }

    /**
     * 
     * @return
     * @throws Exception
     * @throws HibernateException 
     */
    public List<Contrato> listarContrato() throws Exception, HibernateException {
        return listarContrato("from Contrato");
    }

    public Contrato procurarContrato(final Contrato contrato) throws Exception, HibernateException {
        Session session = Hibernate.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            Contrato returnlocador = (Contrato) session.get(Contrato.class, contrato);
            session.getTransaction().commit();
            return returnlocador;
        } catch (HibernateException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new HibernateException("HIBERNATE Erro no Procurar Contrato: ", e);
        } catch (Exception e) {
            throw new Exception("GERAL  Erro no Procurar Por ID Contrato: ", e);
        }
    }

    public Contrato procurarContratoId(final Integer id) throws Exception {
        Session session = Hibernate.getSessionFactory().getCurrentSession();
        try {
            session.beginTransaction();
            Contrato returnLocador = (Contrato) session.createQuery("from Contrato where codContrato =" + id);
            session.getTransaction().commit();
            return returnLocador;
        } catch (HibernateException e) {
            if (session != null) {
                session.getTransaction().rollback();
            }
            throw new HibernateException("HIBERNATE Erro no Procurar Por ID Contrato: ", e);
        } catch (Exception e) {
            throw new Exception("GERAL  Erro no Procurar Por ID Contrato: ", e);
        }
    }

    public static void main(String[] args) {
        RepositorioContrato repositorioContrato2 = RepositorioContrato.getRepositorioContrato();
        try {
            List<Contrato> listarContrato = repositorioContrato2.listarContrato();
            for (Contrato contrato : listarContrato) {
                System.out.println(contrato.getLocador().getNome());
            }
        } catch (HibernateException ex) {
            Logger.getLogger(RepositorioContrato.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(RepositorioContrato.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Finishied");
    }
}
