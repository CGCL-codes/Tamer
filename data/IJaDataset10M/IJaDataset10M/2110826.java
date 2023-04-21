package fr.insatoulouse.uvmanager.grb.DataBase;

import java.util.ArrayList;
import java.util.Iterator;
import fr.insatoulouse.uvmanager.grb.uvmanager.Assignment;
import fr.insatoulouse.uvmanager.grb.uvmanager.AssignmentStatus;
import fr.insatoulouse.uvmanager.grb.uvmanager.LectureType;
import fr.insatoulouse.uvmanager.grb.uvmanager.UV;
import fr.insatoulouse.uvmanager.grb.uvmanager.UVStatus;
import junit.framework.TestCase;

/**
 * @author Alexander Remen
 * JUnit test case for the {@link Manager} class.
 */
public class ManagerTester extends TestCase {

    Manager mg = new Manager();

    public void testCreateTables() {
        mg.createTables();
    }

    public void testFillUpTables() {
        for (int i = 0; i < 10; i++) {
            int UVId = mg.addUV("English " + (i + 1), "Using English in a professional context linked to specialised field " + (i + 1), 4, 20, 10, 15, 40, 2, "Daniel Marre");
            System.out.println("UVId: " + UVId);
            int assignmentId = mg.addAssignment(1, "Exposito", 30, "CM");
            System.out.println("AssignmentId: " + assignmentId);
        }
    }

    public void testDelete() {
        mg.deleteUV(5);
        System.out.println("Deleted this UV entry:" + (5));
        mg.deleteAssignment(6);
        System.out.println("Deleted this ASSIGNMENT entry:" + (6));
    }

    public void testGet() {
        ArrayList<UV> uvs = mg.getPromo(4);
        Iterator<UV> it = uvs.iterator();
        while (it.hasNext()) {
            System.out.println(it.next());
        }
    }

    public void testGetUV() {
        System.out.println("************* getUV **********");
        UV uv = null;
        uv = mg.getUV(1);
        System.out.println(uv);
        uv = mg.getUV(120);
        if (uv != null) {
            fail("Did not return null");
        }
    }

    public void testUpdateUV() {
        System.out.println("******updateUV*****");
        UV uv = null;
        mg.updateUV(3, "FLE", "Francais langue �trang�re", 3, 10, 20, 30, 0, 1, "Philippe Fiore");
        uv = mg.getUV(3);
        System.out.println(uv);
        if (!uv.getDescription().equals("Francais langue �trang�re")) {
            fail("testUpdateUV not correct");
        }
    }

    public void testUpdateAssignment() {
        System.out.println("******updateAssignment*****");
        Assignment assignment = null;
        mg.updateAssignment(7, "Ernesto Exposito", 30, LectureType.BE);
        assignment = mg.getAssignment(7);
        System.out.println(assignment);
        if (!assignment.getTeacher().equals("Ernesto Exposito") && assignment.getType() != LectureType.BE) {
            fail("testUpdateUV not correct");
        }
    }

    public void testUpdateStatuses() {
        System.out.println("******updateUVStatus*******");
        UV uv = null;
        mg.updateUVStatus(4, UVStatus.acceptedCO);
        uv = mg.getUV(4);
        if (uv.getStatus() != UVStatus.acceptedCO) {
            fail("UVStatus not correct: " + uv.getStatus());
        }
        System.out.println("******updateAssignmentStatus*******");
        Assignment assignment = null;
        mg.updateAssignmentStatus(5, AssignmentStatus.refused);
        assignment = mg.getAssignment(5);
        if (assignment.getValidated() != AssignmentStatus.refused) {
            fail("AssignmentStatus not correct: " + assignment.getValidated());
        }
    }
}
