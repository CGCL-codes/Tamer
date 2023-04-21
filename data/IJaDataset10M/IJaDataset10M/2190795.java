package net.sf.planofattack.io;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import net.sf.planofattack.Plan;
import test.util.AssertUtil;
import test.util.EasyMockTestCase;
import test.util.TestMother;

public class FilePlanRendererTest extends EasyMockTestCase {

    public void test_render() throws Exception {
        Plan plan = new Plan();
        plan.addDrawable(TestMother.createOval(0, 0, 10, 100, true));
        plan.addDrawable(TestMother.createOval(10, 0, 10, 20, true));
        plan.addDrawable(TestMother.createOval(0, 20, 10, 10, true));
        plan.addDrawable(TestMother.createRectangle(0, 20, 100, 100, true));
        plan.addDrawable(TestMother.createRectangle(0, 20, 10, 10, true));
        Image planImage = new BufferedImage(800, 600, BufferedImage.TYPE_INT_ARGB);
        Graphics g = planImage.getGraphics();
        g.setClip(0, 0, 800, 600);
        FilePlanRenderer.instance().render(plan, g);
        assertNotNull(planImage);
        AssertUtil.assertImages(TestMother.getPlanImage(), planImage);
    }
}
