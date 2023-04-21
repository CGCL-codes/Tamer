package it.unisannio.rcost.callgraphanalyzer.dbmanagers.dao;

import it.unisannio.rcost.callgraphanalyzer.Field;
import it.unisannio.rcost.callgraphanalyzer.Interface;
import it.unisannio.rcost.callgraphanalyzer.dbmanagers.DAOInteface;

public interface FieldDAO extends DAOInteface<Field> {

    public Field[] getFieldsByModule(String projectName, Interface owner);

    public Field getFieldByNameAndModule(String projectName, String name, Interface owner);
}
