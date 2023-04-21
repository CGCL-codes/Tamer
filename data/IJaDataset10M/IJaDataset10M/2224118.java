package net.sf.lavalamp.device.impl;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.util.Calendar;
import net.sf.lavalamp.device.Blink;
import net.sf.lavalamp.device.Crash;
import net.sf.lavalamp.device.DeviceProperties;
import net.sf.lavalamp.device.OnError;
import net.sf.lavalamp.device.Period;
import net.sf.lavalamp.device.StatusQuo;
import net.sf.lavalamp.device.TimeOfDay;
import net.sf.lavalamp.device.TurnOff;
import net.sf.lavalamp.device.TurnOn;
import net.sf.lavalamp.site.Builds;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class TellStickTest {

    TellStick device;

    DeviceProperties properties;

    Calendar calendar;

    @Mock
    Runtime runtime;

    @Mock
    Builds builds;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        device = new TellStick();
        properties = new DeviceProperties();
        properties.setIdentity("ID");
        properties.setPath("PATH");
        device.setProperties(properties);
        calendar = Calendar.getInstance();
        calendar.set(2009, 1, 1, 4, 45);
    }

    @Test
    public void shouldTurnOn() throws IOException {
        device.setRuntime(runtime);
        device.turnOn();
        verify(runtime).exec(anyString());
    }

    @Test
    public void shouldTurnOff() throws IOException {
        device.setRuntime(runtime);
        device.turnOff();
        verify(runtime).exec(anyString());
    }

    @Test(expected = RuntimeException.class)
    public void shouldThrowRuntime() throws IOException {
        when(runtime.exec(anyString())).thenThrow(new IOException());
        device.setRuntime(runtime);
        device.turnOff();
    }

    @Test
    public void testOffString() {
        assertThat(device.offString(), is("PATH/tdtool.exe --off ID"));
    }

    @Test
    public void testOnString() {
        assertThat(device.onString(), is("PATH/tdtool.exe --on ID"));
    }

    @Test
    public void testOnStringWithSpace() {
        properties.setPath("C:/Program Files/Telldus");
        device.setProperties(properties);
        assertThat(device.onString(), is("C:/Program Files/Telldus/tdtool.exe --on ID"));
    }

    @Test
    public void shouldBeOnWhenSuccessful() {
        properties.setOnWhenSuccessful(true);
        device.setProperties(properties);
        assertThat(device.getActions().getSuccessAction(), instanceOf(TurnOn.class));
        assertThat(device.getActions().getFailureAction(), instanceOf(TurnOff.class));
    }

    @Test
    public void shouldBlink() {
        properties.setOnError(OnError.BLINK);
        device.setProperties(properties);
        assertThat(device.getActions().getErrorAction(), instanceOf(Blink.class));
    }

    @Test
    public void shouldCrash() {
        properties.setOnError(OnError.CRASH);
        device.setProperties(properties);
        assertThat(device.getActions().getErrorAction(), instanceOf(Crash.class));
    }

    @Test
    public void shouldRemainStatusQuo() {
        properties.setOnError(OnError.STATUSQUO);
        device.setProperties(properties);
        assertThat(device.getActions().getErrorAction(), instanceOf(StatusQuo.class));
    }

    @Test
    public void shouldBlinkByDefault() {
        device.setProperties(properties);
        assertThat(device.getActions().getErrorAction(), instanceOf(Blink.class));
    }

    @Test
    public void shouldNotBeOnWhenSuccessfulByDefault() {
        assertThat(device.getActions().getSuccessAction(), instanceOf(TurnOff.class));
    }

    @Test
    public void shouldBeAlwaysOff() {
        device.setAlwaysOffPeriod(new Period(new TimeOfDay(4, 30), new TimeOfDay(12, 30)));
        assertThat(device.isAlwaysOff(calendar), is(true));
    }

    @Test
    public void shouldntBeAlwaysOff() {
        device.setAlwaysOffPeriod(new Period(new TimeOfDay(0, 0), new TimeOfDay(0, 0)));
        assertThat(device.isAlwaysOff(calendar), is(false));
    }

    @Test
    public void shouldNotBeAlwaysOffWhenBefore() {
        device.setAlwaysOffPeriod(new Period(new TimeOfDay(5, 30), new TimeOfDay(12, 30)));
        assertThat(device.isAlwaysOff(calendar), is(false));
    }

    @Test
    public void shouldNotBeAlwaysOffWhenAfter() {
        device.setAlwaysOffPeriod(new Period(new TimeOfDay(4, 30), new TimeOfDay(4, 40)));
        assertThat(device.isAlwaysOff(calendar), is(false));
    }

    @Test
    public void shouldBeAlwaysOffAtNight() {
        device.setAlwaysOffPeriod(new Period(new TimeOfDay(22, 30), new TimeOfDay(5, 30)));
        assertThat(device.isAlwaysOff(calendar), is(true));
    }

    @Test
    public void shouldNotBeAlwaysOffInMorning() {
        device.setAlwaysOffPeriod(new Period(new TimeOfDay(22, 30), new TimeOfDay(4, 30)));
        assertThat(device.isAlwaysOff(calendar), is(false));
    }

    @Test
    public void testToString() {
        assertThat(device.toString(), is("TellStick ID "));
    }

    @Test
    public void shouldBeAlwaysOffInEvening() {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        device.setAlwaysOffPeriod(new Period(new TimeOfDay(22, 30), new TimeOfDay(04, 30)));
        assertThat(device.isAlwaysOff(calendar), is(true));
    }

    @Test
    public void shouldGetAndSetBuilds() {
        device.setBuilds(builds);
        assertThat(device.getBuilds(), notNullValue());
    }
}
