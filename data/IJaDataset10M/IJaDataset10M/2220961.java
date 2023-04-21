package it.unisannio.rcost.callgraphanalyzer.dbmanagers;

import it.unisannio.rcost.callgraphanalyzer.Aspect;
import it.unisannio.rcost.callgraphanalyzer.Pointcut;
import it.unisannio.rcost.callgraphanalyzer.dbmanagers.dao.PointcutDAO;
import it.unisannio.rcost.callgraphanalyzer.dbmanagers.hsql.PointcutHSQL;

public class PointcutFacade implements PointcutDAO {

    private PointcutDAO pointcutDAO = null;

    public static final PointcutFacade facade = new PointcutFacade();

    private PointcutFacade() {
        pointcutDAO = new PointcutHSQL();
    }

    public Pointcut fillAllAttribute(String projectName, Pointcut emptyElement) {
        return pointcutDAO.fillAllAttribute(projectName, emptyElement);
    }

    public Pointcut[] getPointcutsByAspect(String projectName, Aspect owner) {
        return pointcutDAO.getPointcutsByAspect(projectName, owner);
    }
}
