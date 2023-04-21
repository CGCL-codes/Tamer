package com.google.gdt.eclipse.designer.smartgwt.model.widgets;

import com.google.gdt.eclipse.designer.smart.model.CanvasInfo;
import com.google.gdt.eclipse.designer.smartgwt.model.SmartGwtModelTest;
import static org.fest.assertions.Assertions.assertThat;

/**
 * Test for <code>com.smartgwt.client.widgets.Slider</code> widget.
 * 
 * @author scheglov_ke
 */
public class SliderTest extends SmartGwtModelTest {

    public void _test_exit() throws Exception {
        System.exit(0);
    }

    public void test_vertical() throws Exception {
        CanvasInfo canvas = parseJavaInfo("public class Test extends Canvas {", "  public Test() {", "    {", "      Slider slider = new Slider();", "      addChild(slider);", "    }", "  }", "}");
        canvas.refresh();
        CanvasInfo slider = getJavaInfoByName("slider");
        assertThat(slider.getBounds().height).isGreaterThan(200);
    }

    public void test_horizontal() throws Exception {
        CanvasInfo canvas = parseJavaInfo("public class Test extends Canvas {", "  public Test() {", "    {", "      Slider slider = new Slider();", "      slider.setVertical(false);", "      addChild(slider);", "    }", "  }", "}");
        canvas.refresh();
        CanvasInfo slider = getJavaInfoByName("slider");
        assertThat(slider.getBounds().width).isGreaterThan(200);
    }
}
