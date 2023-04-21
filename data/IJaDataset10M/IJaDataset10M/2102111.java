package br.ufrgs.inf.biblioteca.business.implementation;

import br.gov.framework.demoiselle.core.layer.integration.Injection;
import br.ufrgs.inf.biblioteca.bean.Autor;
import br.ufrgs.inf.biblioteca.business.IAutorBC;
import br.ufrgs.inf.biblioteca.persistence.dao.IAutorDAO;
import br.ufrgs.inf.biblioteca.persistence.dao.IGenericDAO;

public class AutorBC extends GenericBC<Autor> implements IAutorBC {

    @Injection
    private IAutorDAO autorDAO;

    @Override
    public IGenericDAO<Autor> getDAO() {
        return autorDAO;
    }
}
