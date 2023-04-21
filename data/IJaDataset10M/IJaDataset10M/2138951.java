package project2;

import project2.scene.*;
import project2.scene.geom.*;
import project2.operations.*;
import project2.display.*;
import project2.scene.util.*;

public class DebugDeficiencyTree {

    static SuperSceneGraph ssg;

    static BasicOperations bo = new BasicOperations();

    static Display d;

    public static void main(String[] args) {
        ssg = bo.loadSceneFromFile(args[0]);
        DTNode head = new DeficiencyTree().perform((Polygon2D) ssg.get(0));
        createSuperSceneGraph(head, ssg);
        bo.saveSceneToFile(ssg, "deficiencyTree.scene");
        drawDT();
    }

    public static void createSuperSceneGraph(DTNode head, SuperSceneGraph ssg) {
        while (head.size() > 0) {
            DTNode tmp = head.removeFirst();
            ssg.add(tmp.getPoly());
            if (tmp.size() > 0) createSuperSceneGraph(tmp, ssg);
        }
    }

    public static void drawDT() {
        ssg = bo.loadSceneFromFile("deficiencyTree.scene");
        d = new Display();
        utilStack.push(new utilSuperSceneGraph(ssg, null, (long) 5, 1, 1));
    }
}
