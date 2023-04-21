package br.ufmg.saotome.arangi.model.dao;

import br.ufmg.saotome.arangi.commons.BasicException;
import br.ufmg.saotome.arangi.commons.ClasspathEntityResolver;

/**
 * 
 * @author Cesar Correia
 *
 */
public interface IConfiguration {

    public void setEntityResolver(ClasspathEntityResolver entityResolver);

    public void configure() throws BasicException;

    public void buildSessionFactory() throws BasicException;

    public void reBuildSessionFactory() throws BasicException;

    public Object getSessionFactory();

    public Object getUnderlyingConfiguration();

    public void reconfigure() throws BasicException;

    public String getDialect();

    public void setDialect(String dialect);

    public void setDialectByJDBCDriver(String driver);
}
