package br.ufpe.ankos.model.moluscosProtocolos;

import org.hibernate.Session;
import org.hibernate.Transaction;
import br.ufpe.ankos.dao.hibernate.HibernateUtil;
import br.ufpe.ankos.dao.hibernate.RepositorioGenerico;
import br.ufpe.ankos.model.protocolos.ErroAcessoRepositorioException;
import br.ufpe.ankos.model.protocolos.Protocolo;

public class RepositorioMoluscosProtocolosHibernate extends RepositorioGenerico<MoluscoProtocolo> implements IRepositorioMoluscosProtocolos {

    public MoluscoProtocolo recuperar(int codigoProtocolo, int codigoMolusco) throws ErroAcessoRepositorioException {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.currentSession();
            transaction = session.beginTransaction();
            MoluscoProtocolo moluscoProtocolo = (MoluscoProtocolo) session.createQuery("from MoluscoProtocolo mp where mp.protocolo.codigo = :codigoProtocolo and mp.molusco.codigo = :codigoMolusco").setInteger("codigoProtocolo", codigoProtocolo).setInteger("codigoMolusco", codigoMolusco).setMaxResults(1).uniqueResult();
            transaction.commit();
            return moluscoProtocolo;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new ErroAcessoRepositorioException(e.getMessage());
        } finally {
            HibernateUtil.closeSession();
        }
    }

    public void excluir(Protocolo protocolo) throws ErroAcessoRepositorioException {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.currentSession();
            transaction = session.beginTransaction();
            for (MoluscoProtocolo moluscoProtocolo : protocolo.getMoluscosProtocolos()) {
                session.delete(moluscoProtocolo);
            }
            transaction.commit();
            session.flush();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new ErroAcessoRepositorioException(e.getMessage());
        } finally {
            HibernateUtil.closeSession();
        }
    }

    public boolean existe(MoluscoProtocolo moluscoProtocolo) throws ErroAcessoRepositorioException {
        Session session = null;
        Transaction transaction = null;
        try {
            session = HibernateUtil.currentSession();
            transaction = session.beginTransaction();
            MoluscoProtocolo moluscoProtocoloBanco = (MoluscoProtocolo) session.createQuery("from MoluscoProtocolo mp " + "where mp.protocolo.codigo = :codigoProtocolo and " + "mp.molusco.codigo = :codigoMolusco").setInteger("codigoProtocolo", moluscoProtocolo.getProtocolo().getCodigo()).setInteger("codigoMolusco", moluscoProtocolo.getMolusco().getCodigo()).setMaxResults(1).uniqueResult();
            transaction.commit();
            if (moluscoProtocoloBanco == null) {
                return false;
            }
            return true;
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw new ErroAcessoRepositorioException(e.getMessage());
        } finally {
            HibernateUtil.closeSession();
        }
    }
}
