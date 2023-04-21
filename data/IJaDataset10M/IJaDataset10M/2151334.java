package ali.compasstest;

import ali.compass.Compass;
import ali.point.Point;
import org.junit.Test;
import static ali.compass.Compass.getCompass;
import static ali.point.Point.point;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

/**
 * Created by IntelliJ IDEA.
 * User: aliolci
 * Date: 04/06/11
 * Time: 10:08
 * To change this template use File | Settings | File Templates.
 */
public class CompassTest {

    private Point center = point(40, 40);

    ;

    private Point east = point(60, 40);

    ;

    private Point north = point(40, 20);

    private Point south = point(40, 60);

    private Point southWest = point(20, 60);

    private Point northEast = point(60, 20);

    @Test
    public void shouldCompassReturnRightSignForTheGivenSourceAndDestinationPointsOO() {
        Compass compass = getCompass(center, center);
        assertThat(compass.getHorizontalDirectionMathSign(), is(0));
        assertThat(compass.getVerticalDirectionMathSign(), is(0));
    }

    @Test
    public void shouldCompassReturnRightSignForTheGivenSourceAndDestinationPointsOE() {
        Compass compass = getCompass(center, east);
        assertThat(compass.getHorizontalDirectionMathSign(), is(1));
    }

    @Test
    public void shouldCompassReturnRightSignForTheGivenSourceAndDestinationPointsOW() {
        Compass compass = getCompass(center, north);
        assertThat(compass.getVerticalDirectionMathSign(), is(-1));
    }

    @Test
    public void shouldCompassReturnRightSignForTheGivenSourceAndDestinationPointsOS() {
        Compass compass = getCompass(center, south);
        assertThat(compass.getVerticalDirectionMathSign(), is(1));
    }

    @Test
    public void shouldCompassReturnRightSignForTheGivenSourceAndDestinationPointsSW() {
        Compass compass = getCompass(center, southWest);
        assertThat(compass.getHorizontalDirectionMathSign(), is(-1));
        assertThat(compass.getVerticalDirectionMathSign(), is(1));
    }

    @Test
    public void shouldCompassReturnRightSignForTheGivenSourceAndDestinationPointsNE() {
        Compass compass = getCompass(center, northEast);
        assertThat(compass.getHorizontalDirectionMathSign(), is(1));
        assertThat(compass.getVerticalDirectionMathSign(), is(-1));
    }
}
