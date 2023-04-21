package barsuift.simLife.j2d.panel;

import org.fest.assertions.Delta;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import barsuift.simLife.PercentHelper;
import barsuift.simLife.environment.SunUpdateCode;
import barsuift.simLife.j3d.environment.MockSun3D;
import static org.fest.assertions.Assertions.assertThat;

public class SunBrightnessPanelTest {

    private MockSun3D mockSun3D;

    private SunBrightnessPanel display;

    @BeforeMethod
    protected void setUp() {
        mockSun3D = new MockSun3D();
        display = new SunBrightnessPanel(mockSun3D);
    }

    @AfterMethod
    protected void tearDown() {
        mockSun3D = null;
        display = null;
    }

    @Test
    public void testInit() {
        assertThat(display.getLabel().getText()).isEqualTo("Sun brightness (100.00%)");
        assertThat(PercentHelper.getDecimalValue(display.getSlider().getValue()).floatValue()).isEqualTo(mockSun3D.getBrightness().floatValue(), Delta.delta(0.0050001));
        mockSun3D.setBrightness(PercentHelper.getDecimalValue(90));
        display = new SunBrightnessPanel(mockSun3D);
        assertThat(display.getLabel().getText()).isEqualTo("Sun brightness (90.00%)");
        assertThat(PercentHelper.getDecimalValue(display.getSlider().getValue())).isEqualTo(mockSun3D.getBrightness());
        mockSun3D.setBrightness(PercentHelper.getDecimalValue(80));
        display = new SunBrightnessPanel(mockSun3D);
        assertThat(display.getLabel().getText()).isEqualTo("Sun brightness (80.00%)");
        assertThat(PercentHelper.getDecimalValue(display.getSlider().getValue())).isEqualTo(mockSun3D.getBrightness());
        mockSun3D.setBrightness(PercentHelper.getDecimalValue(100));
        display = new SunBrightnessPanel(mockSun3D);
        assertThat(display.getLabel().getText()).isEqualTo("Sun brightness (100.00%)");
        assertThat(PercentHelper.getDecimalValue(display.getSlider().getValue())).isEqualTo(mockSun3D.getBrightness());
    }

    @Test
    public void testUpdate() {
        assertThat(display.getLabel().getText()).isEqualTo("Sun brightness (100.00%)");
        assertThat(PercentHelper.getDecimalValue(display.getSlider().getValue()).floatValue()).isEqualTo(mockSun3D.getBrightness().floatValue(), Delta.delta(0.0050001));
        mockSun3D.setBrightness(PercentHelper.getDecimalValue(90));
        display.update(mockSun3D, SunUpdateCode.BRIGHTNESS);
        assertThat(display.getLabel().getText()).isEqualTo("Sun brightness (90.00%)");
        assertThat(PercentHelper.getDecimalValue(display.getSlider().getValue())).isEqualTo(mockSun3D.getBrightness());
        mockSun3D.setBrightness(PercentHelper.getDecimalValue(90));
        display.update(mockSun3D, SunUpdateCode.BRIGHTNESS);
        assertThat(display.getLabel().getText()).isEqualTo("Sun brightness (90.00%)");
        assertThat(PercentHelper.getDecimalValue(display.getSlider().getValue())).isEqualTo(mockSun3D.getBrightness());
        mockSun3D.setBrightness(PercentHelper.getDecimalValue(100));
        display.update(mockSun3D, SunUpdateCode.BRIGHTNESS);
        assertThat(display.getLabel().getText()).isEqualTo("Sun brightness (100.00%)");
        assertThat(PercentHelper.getDecimalValue(display.getSlider().getValue())).isEqualTo(mockSun3D.getBrightness());
    }
}
