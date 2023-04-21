package br.org.pizzaria.dao;

import br.org.pizzaria.dao.impl.ProdutoDAOImpl;

public class DAOFactory {

    private static DAOFactory daoFactory = null;

    private DAOFactory() {
    }

    public static DAOFactory getInstance() {
        if (daoFactory == null) {
            daoFactory = new DAOFactory();
        }
        return daoFactory;
    }

    public ProdutoDAO getProdutoDAO() {
        return new ProdutoDAOImpl();
    }
}
