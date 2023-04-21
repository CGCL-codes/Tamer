package model.dao;

import model.dao.hibernate.HibernateDAO;
import model.dao.hibernate.HibernateUtil;
import model.valueObject.PedidomercadoVO;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * <strong>PedidomercadoDao</strong>
 * <p>
 * Objeto de Acesso a Fonte de Dados da entidade Pedidomercado.
 * <br>
 * <strong>Hist�rico de Vers�es</strong>
 * <br>
 * 1.0 - Cria��o da classe
 *
 * @author <a href="mailto:raphaufrj@gmail.com">Raphael Rodrigues</a>
 * @author <a href="mailto:pvkelecom@gmail.com">Patrick Kelecom</a>
 * <br>
 * @version 1.0 - 30/11/2009 09:51 : X-MDA
 */
public class PedidomercadoDao extends HibernateDAO {

    public PedidomercadoDao() {
        super(PedidomercadoVO.class);
    }

    public void createPedidoMercado() {
    }

    public void buscarPedidoMercado() {
    }

    public void destroyPedidoMercado() {
    }

    public void inserirPedidoMercado(PedidomercadoVO pedidoMercado) {
        Session session = HibernateUtil.getSession();
        try {
            Transaction tx = session.beginTransaction();
            session.save(pedidoMercado);
            System.out.println("Successfully data insert in database");
            tx.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            session.close();
        }
    }
}
