package test.frame2relex;

import static org.junit.Assert.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import frame.FrameBuilder;
import frame.FrameObject;
import frame2relex.Frame2RelexEngine;

public class Frame2RelexEngineTest {

    Frame2RelexEngine f2r;

    ArrayList<FrameObject[]> foList = new ArrayList<FrameObject[]>();

    @Before
    public void setUp() throws Exception {
        f2r = new Frame2RelexEngine();
        FrameBuilder fb = new FrameBuilder();
        String[] fra = new String[] { "^1_Entity:Entity(John,John)", "^1_Being_named:Entity(John)", "^1_Transitive_action:Agent(throw,John)", "^1_Cause_motion:Agent(throw,John)", "^1_Cause_motion:Theme(throw,ball)", "^1_Transitive_action:Patient(throw,ball)" };
        FrameObject[] fo = new FrameObject[fra.length];
        for (int i = 0; i < fra.length; i++) {
            fo[i] = fb.getFrame(fra[i]);
        }
        foList.add(fo);
    }

    @Test
    public final void testExecute() {
        HashSet<String> frame2relexout = null;
        for (FrameObject[] fos : foList) {
            frame2relexout = f2r.execute(fos);
            System.out.println("-------- Frame2Relex Output--------");
            for (String string : frame2relexout) {
                System.out.println(string);
            }
        }
        HashSet<String> exp = new HashSet<String>();
        exp.add("_obj(throw, ball)");
        exp.add("noun(John)");
        exp.add("_subj(throw, John)");
        exp.add("person(John)");
        Assert.assertEquals(exp.toString(), frame2relexout.toString());
    }
}
