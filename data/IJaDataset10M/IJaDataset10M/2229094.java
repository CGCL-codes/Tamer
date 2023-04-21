package com.art.anette.datamodel.dataobjects.managed;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import com.art.anette.datamodel.datacontrol.DBControl;
import com.art.anette.datamodel.dataobjects.simple.Employee;
import com.art.anette.datamodel.dataobjects.sqldata.DepartmentData;

/**
 * Die Klasse beschreibt eine Abteilung im Datenmodell.
 *
 * @author Alexander von Bremen-Kühne
 */
public class Department extends ManagedDataObject<Department, DepartmentData> {

    /**
     * Konstruktor der Klasse, das DataObject wird als VALID erzeugt.
     *
     * @param dbControl die DBControl, die über das DataObject wacht
     * @param rs        das ResultSet aus dem das DataObject erzeugt werden soll
     * @throws SQLException Fehlermeldung beim Auslesen des ResultSets
     */
    public Department(DBControl dbControl, ResultSet rs) throws SQLException {
        super(dbControl, rs.getLong("id"), rs.getLong("timestamp"), newInstance(DepartmentData.class, rs));
    }

    /**
     * Konstruktor der Klasse, das DataObject wird als VALID erzeugt.
     *
     * @param dbControl die DBControl, die über das DataObject wacht
     * @param id        die ID des DataObjects
     * @param timestamp der aktuelle Timestamp des DataObjects
     * @param data      die Daten des DataObjects
     */
    public Department(DBControl dbControl, long id, long timestamp, DepartmentData data) {
        super(dbControl, id, timestamp, data);
    }

    /**
     * Konstruktor der Klasse, das DataObject wird als NEW erzeugt.
     *
     * @param dbControl die DBControl, die über das DataObject wacht
     * @param id        die ID des DataObjects
     * @param data      die Daten des DataObjects
     */
    public Department(DBControl dbControl, long id, DepartmentData data) {
        super(dbControl, id, data);
    }

    /**
     * Konstruktor der Klasse, das DataObject wird als NEW erzeugt.
     *
     * @param dbControl die DBControl, die über das DataObject wacht
     * @param id        die ID des DataObjects
     */
    public Department(DBControl dbControl, long id) {
        super(dbControl, id);
        actual = new DepartmentData();
        modified = new DepartmentData();
    }

    @Override
    public void remove() {
        for (Project p : getProjects()) {
            p.remove();
        }
        super.remove();
        for (Employee e : getEmployees()) {
            e.remove();
        }
    }

    /**
     * Getter eines bestimmten Projects.
     *
     * @param id die ID des Projects
     * @return das Projects
     */
    public Project getProject(long id) {
        return dbControl.getProject(id);
    }

    /**
     * Getter aller Projects des Departments.
     *
     * @return eine Liste der Projects
     */
    public List<Project> getProjects() {
        return dbControl.getProjects(this);
    }

    @Override
    public String toString() {
        return actual.name;
    }

    /**
     * Liefert den Tabellennamen im SQL.
     *
     * @return der Tabellenname
     */
    @SuppressWarnings({ "SameReturnValue" })
    public static String getTableName() {
        return "Departments";
    }

    @Override
    public String getCurrentTableName() {
        return getTableName();
    }

    @Override
    public void informParentObservers() {
        dbControl.getCompany().informObservers();
    }
}
