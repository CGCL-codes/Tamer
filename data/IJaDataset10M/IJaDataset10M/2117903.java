package br.ita.sgch.core;

public class HibernateDAOFactory implements DAOFactory {

    @Override
    public <T> DAO<T> createDAO(Class<T> clazz) {
        return new HibernateDAO<T>(clazz);
    }
}
