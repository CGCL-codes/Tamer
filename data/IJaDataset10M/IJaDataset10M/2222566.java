package br.com.wepa.webapps.orca.logica.persistencia.hibernate;

import br.com.wepa.webapps.orca.logica.persistencia.ConstrutoraDAO;
import br.com.wepa.webapps.orca.logica.persistencia.CredencialDAO;
import br.com.wepa.webapps.orca.logica.persistencia.DAOFactory;
import br.com.wepa.webapps.orca.logica.persistencia.EspecificacaoprodutoDAO;
import br.com.wepa.webapps.orca.logica.persistencia.FornecedorDAO;
import br.com.wepa.webapps.orca.logica.persistencia.FuncaoDAO;
import br.com.wepa.webapps.orca.logica.persistencia.GenericDAO;
import br.com.wepa.webapps.orca.logica.persistencia.GrupoDAO;
import br.com.wepa.webapps.orca.logica.persistencia.OrcamentoDAO;
import br.com.wepa.webapps.orca.logica.persistencia.ProdutoDAO;
import br.com.wepa.webapps.orca.logica.persistencia.SubgrupoDAO;
import br.com.wepa.webapps.orca.logica.persistencia.UfDAO;
import br.com.wepa.webapps.orca.logica.persistencia.UsuarioDAO;

public class HibernateDAOFactory extends DAOFactory {

    public UsuarioDAO getUsuarioDAO() {
        return (UsuarioDAO) instantiateHibernateDAO(UsuarioHibernateDAO.class);
    }

    public FornecedorDAO getFornecedorDAO() {
        return (FornecedorDAO) instantiateHibernateDAO(FornecedorHibernateDAO.class);
    }

    public OrcamentoDAO getOrcamentoDAO() {
        return (OrcamentoDAO) instantiateHibernateDAO(OrcamentoHibernateDAO.class);
    }

    public EspecificacaoprodutoDAO getEspecificacaoprodutoDAO() {
        return (EspecificacaoprodutoDAO) instantiateHibernateDAO(EspecificacaoprodutoHibernateDAO.class);
    }

    public ProdutoDAO getProdutoDAO() {
        return (ProdutoDAO) instantiateHibernateDAO(ProdutoHibernateDAO.class);
    }

    public CredencialDAO getCredencialDAO() {
        return (CredencialDAO) instantiateHibernateDAO(CredencialHibernateDAO.class);
    }

    public FuncaoDAO getFuncaoDAO() {
        return (FuncaoDAO) instantiateHibernateDAO(FuncaoHibernateDAO.class);
    }

    public ConstrutoraDAO getConstrutoraDAO() {
        return (ConstrutoraDAO) instantiateHibernateDAO(ConstrutoraHibernateDAO.class);
    }

    public GrupoDAO getGrupoDAO() {
        return (GrupoDAO) instantiateHibernateDAO(GrupoHibernateDAO.class);
    }

    public SubgrupoDAO getSubgrupoDAO() {
        return (SubgrupoDAO) instantiateHibernateDAO(SubgrupoHibernateDAO.class);
    }

    public UfDAO getUfDAO() {
        return (UfDAO) instantiateHibernateDAO(UfHibernateDAO.class);
    }

    /**
     * Returns the DAO for a class
     */
    @Override
    public GenericDAO getDAO(Class daoClass) {
        try {
            GenericDAO dao = null;
            if (UsuarioDAO.class.equals(daoClass)) {
                dao = getUsuarioDAO();
            } else if (FornecedorDAO.class.equals(daoClass)) {
                dao = getFornecedorDAO();
            } else if (OrcamentoDAO.class.equals(daoClass)) {
                dao = getOrcamentoDAO();
            } else if (EspecificacaoprodutoDAO.class.equals(daoClass)) {
                dao = getEspecificacaoprodutoDAO();
            } else if (ProdutoDAO.class.equals(daoClass)) {
                dao = getProdutoDAO();
            } else if (CredencialDAO.class.equals(daoClass)) {
                dao = getCredencialDAO();
            } else if (FuncaoDAO.class.equals(daoClass)) {
                dao = getFuncaoDAO();
            } else if (ConstrutoraDAO.class.equals(daoClass)) {
                dao = getConstrutoraDAO();
            } else if (GrupoDAO.class.equals(daoClass)) {
                dao = getGrupoDAO();
            } else if (SubgrupoDAO.class.equals(daoClass)) {
                dao = getSubgrupoDAO();
            } else if (UfDAO.class.equals(daoClass)) {
                dao = getUfDAO();
            } else {
                dao = instantiateHibernateDAO(daoClass);
            }
            return dao;
        } catch (Exception ex) {
            throw new RuntimeException("Can not instantiate DAO: " + daoClass, ex);
        }
    }

    private GenericHibernateDAO instantiateHibernateDAO(Class daoClass) {
        try {
            GenericHibernateDAO dao = (GenericHibernateDAO) daoClass.newInstance();
            return dao;
        } catch (Exception ex) {
            throw new RuntimeException("Can not instantiate GenericHibernateDAO: " + daoClass, ex);
        }
    }
}
