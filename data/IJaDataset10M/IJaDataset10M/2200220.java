package tudresden.ocl20.pivot.tools.transformation.pivot2sql.test.tests;

import java.util.ArrayList;
import java.util.List;
import org.junit.BeforeClass;
import tudresden.ocl20.pivot.tools.transformation.impl.Tuple;
import tudresden.ocl20.pivot.tools.transformation.pivot2sql.test.tests.util.CWMTest;

public class Pivot2CWMtypedTest extends CWMTest {

    @BeforeClass
    public static void setUp_class() {
        modus = MODUS_TYPED;
    }

    /**
	 * 
	 * <p>
	 * Checks if a class mapped correctly.
	 * </p>
	 */
    public void testClass() {
        super.testClass();
        String view;
        tables.add("T_Person");
        views.add("OV_Person");
        view = "SELECT PK_Person\nFROM T_Person\n";
        table2PrimaryKey.put(tables.get(0), "PK_Person");
        view2queryexpression.put(views.get(0), view);
        checkCWM();
    }

    /**
	 * 
	 * <p>
	 * Checks if a property mapped correctly.
	 * </p>
	 */
    public void testProperty() {
        super.testProperty();
        List<Tuple<String, String>> attributes = new ArrayList<Tuple<String, String>>();
        String view;
        tables.add("T_Person");
        views.add("OV_Person");
        table2PrimaryKey.put(tables.get(0), "PK_Person");
        attributes.add(new Tuple<String, String>("firstName", "String"));
        attributes.add(new Tuple<String, String>("lastName", "String"));
        attributes.add(new Tuple<String, String>("birthDate", "Integer"));
        attributes.add(new Tuple<String, String>("age", "Integer"));
        attributes.add(new Tuple<String, String>("isMarried", "Boolean"));
        attributes.add(new Tuple<String, String>("salaries", "String"));
        view = "SELECT PK_Person,T_Person.age AS age,T_Person.birthDate AS birthDate,";
        view += "T_Person.firstName AS firstName,T_Person.isMarried AS isMarried,";
        view += "T_Person.lastName AS lastName,T_Person.salaries AS salaries";
        view += "\nFROM T_Person\n";
        table2properties.put(tables.get(0), attributes);
        view2queryexpression.put(views.get(0), view);
        checkCWM();
    }

    /**
	 * 
	 * <p>
	 * Checks if a inheritance mapped correctly.
	 * </p>
	 */
    public void testInheritance() {
        super.testInheritance();
        String view;
        List<Tuple<String, String>> attributes = new ArrayList<Tuple<String, String>>();
        tables.add("T_Person");
        views.add("OV_Person");
        table2PrimaryKey.put(tables.get(0), "PK_Person");
        attributes.add(new Tuple<String, String>("lastName", "String"));
        attributes.add(new Tuple<String, String>("type", "String"));
        attributes.add(new Tuple<String, String>("matNr", "Integer"));
        view = "SELECT PK_Person,T_Person.lastName AS lastName\nFROM T_Person\n";
        table2properties.put(tables.get(0), attributes);
        view2queryexpression.put(views.get(0), view);
        view = "SELECT PK_Person,T_Person.lastName AS lastName,T_Person.matNr AS matNr";
        view += "\nFROM T_Person\nWHERE type = \"Student\"";
        views.add("OV_Student");
        view2queryexpression.put(views.get(1), view);
        checkCWM();
    }

    /**
	 * 
	 * <p>
	 * Checks if a 1to1 relation mapped correctly.
	 * </p>
	 */
    public void testRelation1to1() {
        super.testRelation1to1();
        String view;
        List<String> foreignKeys1 = new ArrayList<String>();
        tables.add("T_Person");
        views.add("OV_Person");
        foreignKeys1.add("FK_currentPaper");
        view = "SELECT PK_Person,FK_currentPaper\nFROM T_Person\n";
        table2PrimaryKey.put(tables.get(0), "PK_Person");
        table2ForeignKey.put(tables.get(0), foreignKeys1);
        view2queryexpression.put(views.get(0), view);
        tables.add("T_Paper");
        views.add("OV_Paper");
        view = "SELECT PK_Paper\nFROM T_Paper\n";
        table2PrimaryKey.put(tables.get(1), "PK_Paper");
        view2queryexpression.put(views.get(1), view);
        checkCWM();
    }

    /**
	 * 
	 * <p>
	 * Checks if a 1toN relation mapped correctly.
	 * </p>
	 */
    public void testRelation1toN() {
        super.testRelation1toN();
        List<String> foreignKeys = new ArrayList<String>();
        String view;
        tables.add("T_Facility");
        views.add("OV_Facility");
        foreignKeys.add("FK_superFacility");
        view = "SELECT PK_Facility,FK_superFacility\nFROM T_Facility\n";
        table2PrimaryKey.put(tables.get(0), "PK_Facility");
        view2queryexpression.put(views.get(0), view);
        table2ForeignKey.put(tables.get(0), foreignKeys);
        checkCWM();
    }

    /**
	 * 
	 * <p>
	 * Checks if a Nto1 relation mapped correctly.
	 * </p>
	 */
    public void testRelationNto1() {
        super.testRelationNto1();
        List<String> foreignKeys = new ArrayList<String>();
        String view;
        tables.add("T_Facility");
        views.add("OV_Facility");
        foreignKeys.add("FK_superFacility");
        view = "SELECT PK_Facility,FK_superFacility\nFROM T_Facility\n";
        table2PrimaryKey.put(tables.get(0), "PK_Facility");
        table2ForeignKey.put(tables.get(0), foreignKeys);
        view2queryexpression.put(views.get(0), view);
        checkCWM();
    }

    /**
	 * 
	 * <p>
	 * Checks if a MtoN relation mapped correctly.
	 * </p>
	 */
    public void testRelationMtoN() {
        super.testRelationMtoN();
        List<String> foreignKeys = new ArrayList<String>();
        String view;
        tables.add("T_Person");
        views.add("OV_Person");
        view = "SELECT PK_Person\nFROM T_Person\n";
        table2PrimaryKey.put(tables.get(0), "PK_Person");
        view2queryexpression.put(views.get(0), view);
        tables.add("T_Paper");
        views.add("OV_Paper");
        view = "SELECT PK_Paper\nFROM T_Paper\n";
        table2PrimaryKey.put(tables.get(1), "PK_Paper");
        view2queryexpression.put(views.get(1), view);
        tables.add("ASS_author_papers");
        foreignKeys.add("FK_papers");
        foreignKeys.add("FK_author");
        table2ForeignKey.put(tables.get(2), foreignKeys);
        checkCWM();
    }

    /**
	 * 
	 * <p>
	 * Checks if a complex university example mapped correctly.
	 * </p>
	 */
    public void testComplexUniversity() {
        super.testComplexUniversity();
        List<Tuple<String, String>> attributes = new ArrayList<Tuple<String, String>>();
        List<String> foreignKeys = new ArrayList<String>();
        String view;
        tables.add("T_Person");
        views.add("OV_Person");
        attributes.add(new Tuple<String, String>("firstName", "String"));
        attributes.add(new Tuple<String, String>("lastName", "String"));
        attributes.add(new Tuple<String, String>("birthDate", "Date"));
        attributes.add(new Tuple<String, String>("age", "Integer"));
        attributes.add(new Tuple<String, String>("isMarried", "Boolean"));
        attributes.add(new Tuple<String, String>("salaries", "String"));
        attributes.add(new Tuple<String, String>("matNr", "Integer"));
        attributes.add(new Tuple<String, String>("matDate", "Date"));
        attributes.add(new Tuple<String, String>("soSecNr", "String"));
        attributes.add(new Tuple<String, String>("taxClass", "String"));
        attributes.add(new Tuple<String, String>("wage", "Integer"));
        attributes.add(new Tuple<String, String>("dissSubject", "String"));
        attributes.add(new Tuple<String, String>("type", "String"));
        foreignKeys.add("FK_supervisor");
        foreignKeys.add("FK_currentPaper");
        foreignKeys.add("FK_grade");
        foreignKeys.add("FK_theFacility");
        view = "SELECT PK_Person,T_Person.age AS age,T_Person.birthDate AS birthDate,";
        view += "T_Person.firstName AS firstName,T_Person.isMarried AS isMarried,";
        view += "T_Person.lastName AS lastName,T_Person.salaries AS salaries,";
        view += "FK_currentPaper,FK_grade,FK_supervisor,FK_theFacility";
        view += "\nFROM T_Person\n";
        table2properties.put(tables.get(0), attributes);
        table2ForeignKey.put(tables.get(0), foreignKeys);
        table2PrimaryKey.put(tables.get(0), "PK_Person");
        view2queryexpression.put(views.get(0), view);
        views.add("OV_Student");
        view = "SELECT PK_Person,T_Person.age AS age,T_Person.birthDate AS birthDate,";
        view += "T_Person.firstName AS firstName,T_Person.isMarried AS isMarried,";
        view += "T_Person.lastName AS lastName,T_Person.matDate AS matDate,";
        view += "T_Person.matNr AS matNr,T_Person.salaries AS salaries,";
        view += "FK_currentPaper,FK_grade,FK_supervisor,FK_theFacility";
        view += "\nFROM T_Person\nWHERE type = \"Student\"";
        view2queryexpression.put(views.get(1), view);
        views.add("OV_Employee");
        view = "SELECT PK_Person,T_Person.age AS age,T_Person.birthDate AS birthDate,";
        view += "T_Person.firstName AS firstName,T_Person.isMarried AS isMarried,";
        view += "T_Person.lastName AS lastName,T_Person.salaries AS salaries,";
        view += "T_Person.soSecNr AS soSecNr,T_Person.taxClass AS taxClass,";
        view += "T_Person.wage AS wage,FK_currentPaper,FK_grade,FK_supervisor,FK_theFacility";
        view += "\nFROM T_Person\nWHERE type = \"Employee\"";
        view2queryexpression.put(views.get(2), view);
        views.add("OV_PhDStudent");
        view = "SELECT PK_Person,T_Person.age AS age,T_Person.birthDate AS birthDate,";
        view += "T_Person.dissSubject AS dissSubject,T_Person.firstName AS firstName,";
        view += "T_Person.isMarried AS isMarried,T_Person.lastName AS lastName,";
        view += "T_Person.salaries AS salaries,T_Person.soSecNr AS soSecNr,";
        view += "T_Person.taxClass AS taxClass,T_Person.wage AS wage,";
        view += "FK_currentPaper,FK_grade,FK_supervisor,FK_theFacility";
        view += "\nFROM T_Person\nWHERE type = \"PhDStudent\"";
        view2queryexpression.put(views.get(3), view);
        attributes = new ArrayList<Tuple<String, String>>();
        foreignKeys = new ArrayList<String>();
        tables.add("T_Grade");
        views.add("OV_Grade");
        attributes.add(new Tuple<String, String>("name", "String"));
        attributes.add(new Tuple<String, String>("value", "Integer"));
        view = "SELECT PK_Grade,T_Grade.name AS name,T_Grade.value AS value";
        view += "\nFROM T_Grade\n";
        view2queryexpression.put(views.get(4), view);
        table2properties.put(tables.get(1), attributes);
        table2PrimaryKey.put(tables.get(1), "PK_Grade");
        attributes = new ArrayList<Tuple<String, String>>();
        foreignKeys = new ArrayList<String>();
        tables.add("T_Paper");
        views.add("OV_Paper");
        attributes.add(new Tuple<String, String>("title", "String"));
        attributes.add(new Tuple<String, String>("edition", "String"));
        attributes.add(new Tuple<String, String>("purpose", "String"));
        attributes.add(new Tuple<String, String>("category", "String"));
        attributes.add(new Tuple<String, String>("inProgress", "Boolean"));
        view = "SELECT PK_Paper,T_Paper.category AS category,T_Paper.edition AS edition,";
        view += "T_Paper.inProgress AS inProgress,T_Paper.purpose AS purpose,";
        view += "T_Paper.title AS title\nFROM T_Paper\n";
        view2queryexpression.put(views.get(5), view);
        table2properties.put(tables.get(2), attributes);
        table2PrimaryKey.put(tables.get(2), "PK_Paper");
        attributes = new ArrayList<Tuple<String, String>>();
        foreignKeys = new ArrayList<String>();
        tables.add("ASS_author_papers");
        foreignKeys.add("FK_author");
        foreignKeys.add("FK_papers");
        table2ForeignKey.put(tables.get(3), foreignKeys);
        foreignKeys = new ArrayList<String>();
        tables.add("T_Facility");
        views.add("OV_Facility");
        foreignKeys.add("FK_headOfFacility");
        foreignKeys.add("FK_superFacility");
        attributes.add(new Tuple<String, String>("name", "String"));
        attributes.add(new Tuple<String, String>("type", "String"));
        view = "SELECT PK_Facility,T_Facility.name AS name,";
        view += "FK_headOfFacility,FK_superFacility\nFROM T_Facility\n";
        view2queryexpression.put(views.get(6), view);
        table2properties.put(tables.get(4), attributes);
        table2ForeignKey.put(tables.get(4), foreignKeys);
        table2PrimaryKey.put(tables.get(4), "PK_Facility");
        views.add("OV_Faculty");
        view = "SELECT PK_Facility,T_Facility.name AS name,FK_headOfFacility,";
        view += "FK_superFacility\nFROM T_Facility\nWHERE type = \"Faculty\"";
        view2queryexpression.put(views.get(7), view);
        views.add("OV_Chair");
        view = "SELECT PK_Facility,T_Facility.name AS name,FK_headOfFacility,";
        view += "FK_superFacility\nFROM T_Facility\nWHERE type = \"Chair\"";
        view2queryexpression.put(views.get(8), view);
        views.add("OV_Institute");
        view = "SELECT PK_Facility,T_Facility.name AS name,FK_headOfFacility,";
        view += "FK_superFacility\nFROM T_Facility\nWHERE type = \"Institute\"";
        view2queryexpression.put(views.get(9), view);
        foreignKeys = new ArrayList<String>();
        tables.add("ASS_member_owner");
        foreignKeys.add("FK_member");
        foreignKeys.add("FK_owner");
        table2ForeignKey.put(tables.get(5), foreignKeys);
        checkCWM();
    }
}
