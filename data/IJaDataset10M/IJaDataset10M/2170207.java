package org.plazmaforge.studio.dbdesigner.model;

import java.util.List;
import org.plazmaforge.studio.dbdesigner.dbmodel.DBMColumn;
import org.plazmaforge.studio.dbdesigner.dbmodel.DBMTable;
import org.plazmaforge.studio.dbdesigner.dbmodel.DBMForeignKey;

public interface IModelService {

    public boolean isIgnoreCase();

    DBMTable createDBTable(ERTableNode tableNode);

    DBMTable createDBView(ERViewNode viewNode);

    DBMForeignKey createDBForeignKey(ERRelationship erRelationship);

    DBMTable addDBTable(ERTableNode tableNode);

    DBMTable addDBView(ERViewNode viewNode);

    DBMForeignKey addDBForeignKey(ERRelationship erRelationship);

    void addDBTable(DBProject project, DBMTable dbTable);

    void addDBView(DBProject project, DBMTable dbView);

    void addDBForeignKey(DBProject project, DBMForeignKey dbForeignKey);

    void removeDBTable(DBProject project, DBMTable dbTable);

    void removeDBView(DBProject project, DBMTable dbView);

    void removeDBForeignKey(DBProject project, DBMForeignKey dbForeignKey);

    DBMTable findDBTable(ERTableNode tableNode);

    DBMTable findDBView(ERViewNode viewNode);

    DBMForeignKey findDBForeignKey(ERRelationship erRelationship);

    DBMColumn findDBColumn(ERColumn column);

    void populateDBTable(ERTableNode tableNode, DBMTable dbTable);

    void populateDBTableColumn(ERColumn erColumn, DBMColumn dbColumn);

    void populateDBForeignKey(DBMForeignKey dbForeignKey, ERRelationship erRelationship);

    void updateERTable(ERTableNode tableNode, DBMTable dbTable);

    void updateERView(ERViewNode viewNode, DBMTable dbTable);

    void updateERRelationship(ERRelationship erRelationship, DBMForeignKey dbForeignKey, List<ERColumn> oldPkColumns, List<ERColumn> oldFkColumns);

    List<ERTableNode> getERTables(ERDiagram diagram);

    List<ERViewNode> getERViews(ERDiagram diagram);

    List<ERRelationship> getERRelationships(ERDiagram diagram);

    List<ERRelationship> getERRelationships(ERTableNode tableNode);

    List<DBMTable> getDBTables(ERDiagram diagram);

    List<DBMTable> getDBViews(ERDiagram diagram);

    boolean equalsName(String name1, String name2);

    List<ERTableNode> findERTables(DBProject project, DBMTable table);

    List<ERViewNode> findERViews(DBProject project, DBMTable table);

    List<ERRelationship> findERRelationships(DBProject project, DBMForeignKey foreignKey);

    ERDiagram[] getDiagrams(ERTableNode[] tableNodes);

    ERDiagram[] getDiagrams(ERViewNode[] viewNodes);
}
