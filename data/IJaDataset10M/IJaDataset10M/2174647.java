package de.morknet.mrw.test;

import org.junit.Assert;
import org.junit.Test;
import de.morknet.mrw.UnknownDirectionCodeException;
import de.morknet.mrw.UnknownTurnStateException;
import de.morknet.mrw.base.DKW;
import de.morknet.mrw.base.DirectionCode;
import de.morknet.mrw.base.Gleisteil;

public class DkwTest extends SwitchTestBase {

    private final DKW dkw = (DKW) model.findMagneticPart("DKW 8");

    private final Gleisteil a = dkw.getA();

    private final Gleisteil b = dkw.getB();

    private final Gleisteil c = dkw.getC();

    private final Gleisteil d = dkw.getD();

    @Test
    public void turnDkw() {
        dkw.turn(batch, a, c);
        Assert.assertSame("Weichenlage falsch!", DirectionCode.ARC, dkw.getDirectionCode());
        dkw.turn(batch, a, d);
        Assert.assertSame("Weichenlage falsch!", DirectionCode.CROSS, dkw.getDirectionCode());
        dkw.turn(batch, b, c);
        Assert.assertSame("Weichenlage falsch!", DirectionCode.CROSS, dkw.getDirectionCode());
        dkw.turn(batch, b, d);
        Assert.assertSame("Weichenlage falsch!", DirectionCode.ARC, dkw.getDirectionCode());
        dkw.turn(batch, c, a);
        Assert.assertSame("Weichenlage falsch!", DirectionCode.ARC, dkw.getDirectionCode());
        dkw.turn(batch, c, b);
        Assert.assertSame("Weichenlage falsch!", DirectionCode.CROSS, dkw.getDirectionCode());
        dkw.turn(batch, d, a);
        Assert.assertSame("Weichenlage falsch!", DirectionCode.CROSS, dkw.getDirectionCode());
        dkw.turn(batch, d, b);
        Assert.assertSame("Weichenlage falsch!", DirectionCode.ARC, dkw.getDirectionCode());
    }

    @Test(expected = UnknownTurnStateException.class)
    public void turnDkwError() {
        dkw.turn(batch, dkw, dkw);
    }

    @Test(expected = UnknownTurnStateException.class)
    public void turnDkwErrorAA() {
        dkw.turn(batch, a, a);
    }

    @Test(expected = UnknownTurnStateException.class)
    public void turnDkwErrorAB() {
        dkw.turn(batch, a, b);
    }

    @Test(expected = UnknownTurnStateException.class)
    public void turnDkwErrorBA() {
        dkw.turn(batch, b, a);
    }

    @Test(expected = UnknownTurnStateException.class)
    public void turnDkwErrorBB() {
        dkw.turn(batch, b, b);
    }

    @Test(expected = UnknownTurnStateException.class)
    public void turnDkwErrorCC() {
        dkw.turn(batch, c, c);
    }

    @Test(expected = UnknownTurnStateException.class)
    public void turnDkwErrorCD() {
        dkw.turn(batch, c, d);
    }

    @Test(expected = UnknownTurnStateException.class)
    public void turnDkwErrorDC() {
        dkw.turn(batch, d, c);
    }

    @Test(expected = UnknownTurnStateException.class)
    public void turnDkwErrorDD() {
        dkw.turn(batch, d, d);
    }

    @Test
    public void dkwDirCode() {
        dkw.setDir(DirectionCode.ARC);
        Assert.assertSame("Weichenlage falsch!", DirectionCode.ARC, dkw.getDirectionCode());
        dkw.setDir(DirectionCode.CROSS);
        Assert.assertSame("Weichenlage falsch!", DirectionCode.CROSS, dkw.getDirectionCode());
    }

    @Test(expected = UnknownDirectionCodeException.class)
    public void dkwDirCodeLeftError() {
        dkw.setDir(DirectionCode.LEFT);
    }

    @Test(expected = UnknownDirectionCodeException.class)
    public void dkwDirCodeRightError() {
        dkw.setDir(DirectionCode.RIGHT);
    }
}
