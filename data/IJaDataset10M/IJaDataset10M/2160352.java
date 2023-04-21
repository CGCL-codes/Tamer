package ca.sqlpower.architect.example;

import java.awt.Point;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.DatabaseMetaData;
import java.sql.Types;
import javax.swing.SwingUtilities;
import ca.sqlpower.architect.swingui.ArchitectSwingSession;
import ca.sqlpower.architect.swingui.ArchitectSwingSessionContext;
import ca.sqlpower.architect.swingui.ArchitectSwingSessionContextImpl;
import ca.sqlpower.architect.swingui.PlayPen;
import ca.sqlpower.architect.swingui.Relationship;
import ca.sqlpower.architect.swingui.TablePane;
import ca.sqlpower.sqlobject.SQLColumn;
import ca.sqlpower.sqlobject.SQLDatabase;
import ca.sqlpower.sqlobject.SQLIndex;
import ca.sqlpower.sqlobject.SQLObjectException;
import ca.sqlpower.sqlobject.SQLRelationship;
import ca.sqlpower.sqlobject.SQLTable;
import ca.sqlpower.sqlobject.SQLIndex.AscendDescend;

/**
 * A simple class with a main method that demonstrates how to create all the
 * various types of SQL Object from scratch, assemble them into a playpen
 * database, then save them out as a new project.
 * 
 * @author Jonathan Fuerth
 */
public class ProjectCreator implements Runnable {

    private ArchitectSwingSessionContext sessionContext;

    private ArchitectSwingSession session;

    /**
     * Entry point for this demo. Call with "java ca.sqlpower.architect.example.ProjectCreator".
     * 
     * @param args Ignored.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new ProjectCreator());
    }

    /**
     * This method is expected to be invoked in the AWT event dispatch thread.
     * Calling the {@link #main(String[])} method accomplishes this.
     */
    public void run() {
        try {
            sessionContext = new ArchitectSwingSessionContextImpl();
            session = sessionContext.createSession();
            createSampleProject();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            session.close();
        }
    }

    /**
     * This is the interesting part that shows you how to use the Architect's
     * API to create a project and save it.
     * 
     * @throws SQLObjectException not expected to happen in this scenario
     * @throws IOException if saving to the project file fails
     */
    void createSampleProject() throws SQLObjectException, IOException {
        SQLDatabase ppdb = session.getTargetDatabase();
        SQLTable person = new SQLTable(ppdb, "person", "This table represents people", "TABLE", true);
        ppdb.addChild(person);
        SQLTable address = new SQLTable(ppdb, "address", "This table represents places where people live or work", "TABLE", true);
        ppdb.addChild(address);
        SQLColumn col;
        col = new SQLColumn(person, "person_id", Types.INTEGER, 10, 0);
        person.addColumn(col);
        col.setNullable(DatabaseMetaData.columnNoNulls);
        person.addToPK(col);
        col = new SQLColumn(person, "name", Types.VARCHAR, 100, 0);
        person.addColumn(col);
        col = new SQLColumn(address, "address_id", Types.INTEGER, 10, 0);
        address.addColumn(col);
        col.setNullable(DatabaseMetaData.columnNoNulls);
        person.addToPK(col);
        SQLRelationship rel = new SQLRelationship();
        rel.setName("person_address_fk");
        rel.attachRelationship(person, address, true);
        col = new SQLColumn(address, "street_address", Types.VARCHAR, 100, 0);
        address.addColumn(col);
        col = new SQLColumn(address, "city", Types.VARCHAR, 100, 0);
        address.addColumn(col);
        SQLIndex idx = new SQLIndex();
        idx.setName("address_city_idx");
        idx.addIndexColumn(col, AscendDescend.UNSPECIFIED);
        address.addIndex(idx);
        col = new SQLColumn(address, "province", Types.VARCHAR, 100, 0);
        address.addColumn(col);
        PlayPen pp = session.getPlayPen();
        int x = 10;
        int y = 10;
        for (SQLTable table : ppdb.getTables()) {
            TablePane tp = new TablePane(table, pp.getContentPane());
            pp.addTablePane(tp, new Point(x, y));
            x += tp.getPreferredSize().width + 10;
        }
        for (SQLRelationship sr : ppdb.getRelationships()) {
            Relationship r = new Relationship(sr, pp.getContentPane());
            pp.addRelationship(r);
        }
        File file = new File("project_creator_output.architect");
        FileOutputStream out = new FileOutputStream(file);
        try {
            session.getProjectLoader().save(out, "utf-8");
            System.out.println("Saved example project to " + file.getAbsolutePath());
        } finally {
            out.close();
        }
    }
}
