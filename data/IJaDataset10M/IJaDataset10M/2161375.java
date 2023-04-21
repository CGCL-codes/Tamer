package br.com.rasoft.academix.persistence.dao;

import br.com.rasoft.academix.persistence.dao.hibernate.HibernateDAOFactory;

public abstract class DAOFactory {

    public static final int HIBERNATE = 2;

    public static DAOFactory getDAOFactory(int whichFactory) {
        switch(whichFactory) {
            case HIBERNATE:
                return new HibernateDAOFactory();
            default:
                return null;
        }
    }

    public abstract AlunoDAO getAlunoDAO();

    public abstract CepDAO getCepDAO();
}
