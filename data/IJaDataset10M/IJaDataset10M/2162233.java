package it.unisannio.rcost.callgraphanalyzer.dbmanagers;

import it.unisannio.rcost.callgraphanalyzer.Field;
import it.unisannio.rcost.callgraphanalyzer.Interface;
import it.unisannio.rcost.callgraphanalyzer.Method;
import it.unisannio.rcost.callgraphanalyzer.Package;
import it.unisannio.rcost.callgraphanalyzer.dbmanagers.dao.InterfaceDAO;
import it.unisannio.rcost.callgraphanalyzer.dbmanagers.hsql.InterfaceHSQL;

public class InterfaceFacade implements InterfaceDAO {

    private InterfaceDAO interfaceDAO = null;

    public static final InterfaceFacade facade = new InterfaceFacade();

    private InterfaceFacade() {
        interfaceDAO = new InterfaceHSQL();
    }

    public Interface fillAllAttribute(String projectName, Interface emptyElement) {
        return interfaceDAO.fillAllAttribute(projectName, emptyElement);
    }

    public Field[] getFieldsByModule(String projectName, Interface owner) {
        return interfaceDAO.getFieldsByModule(projectName, owner);
    }

    public Method[] getMethodsByModule(String projectName, Interface owner) {
        return interfaceDAO.getMethodsByModule(projectName, owner);
    }

    public Interface[] getInnerModules(String projectName, Interface owner) {
        return interfaceDAO.getInnerModules(projectName, owner);
    }

    public Interface[] getInterfacesByPackage(String projectName, Package owner) {
        return interfaceDAO.getInterfacesByPackage(projectName, owner);
    }

    public Interface[] getModulesByPackageAndType(String projectName, Package owner, String type) {
        return interfaceDAO.getModulesByPackageAndType(projectName, owner, type);
    }

    public Interface[] getModulesByPackage(String projectName, Package owner) {
        return interfaceDAO.getModulesByPackage(projectName, owner);
    }

    public Interface getModuleByNameAndPackage(String projectName, String name, String packageName) {
        return interfaceDAO.getModuleByNameAndPackage(projectName, name, packageName);
    }
}
