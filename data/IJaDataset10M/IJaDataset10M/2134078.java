package alltests;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import calclipse.core.disp.TrapModelSerializationTest;
import calclipse.lib.lcd.DigitSeparatorSerializationTest;
import calclipse.lib.lcd.DigitSerializationTest;
import calclipse.lib.lcd.LCDSerializationTest;
import calclipse.lib.lcd.PixelsSerializationTest;
import calclipse.lib.lcd.display.DefaultDisplayModelSerializationTest;
import calclipse.lib.lcd.display.DisplayScrollPaneSerializationTest;
import calclipse.lib.lcd.display.DisplaySerializationTest;
import calclipse.lib.math.mtrx.MatrixExceptionSerializationTest;
import calclipse.lib.math.rpn.RPNExceptionSerializationTest;
import calclipse.lib.math.rpn.ReturnExceptionSerializationTest;
import calclipse.lib.math.util.graph.GraphPanelSerializationTest;
import calclipse.lib.math.util.graph3d.GraphPanel3DSerializationTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({ DefaultDisplayModelSerializationTest.class, DigitSeparatorSerializationTest.class, DigitSerializationTest.class, DisplayScrollPaneSerializationTest.class, DisplaySerializationTest.class, GraphPanel3DSerializationTest.class, GraphPanelSerializationTest.class, LCDSerializationTest.class, MatrixExceptionSerializationTest.class, PixelsSerializationTest.class, ReturnExceptionSerializationTest.class, RPNExceptionSerializationTest.class, TrapModelSerializationTest.class })
public class AllBeanTests {

    public AllBeanTests() {
    }
}
