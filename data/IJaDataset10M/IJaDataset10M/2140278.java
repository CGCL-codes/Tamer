package com.msli.test.graphic.math;

import com.msli.core.event.UpdateNotice;
import com.msli.core.exception.UnmodifiableStateException;
import com.msli.graphic.math.MathObject;
import com.msli.graphic.math.MathUtils;
import com.msli.graphic.math.Pose3;
import com.msli.graphic.math.Ray3;
import com.msli.graphic.math.Tuple3;
import com.msli.graphic.math.UnitVector3;
import com.msli.test.TestUtils;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.JUnitCore;

public class TestPose3 {

    protected Pose3 getObjA() {
        return _objA;
    }

    protected Pose3 getObjB() {
        return _objB;
    }

    @Before
    public void setUp() throws Exception {
        _objA = new Pose3.Impl();
        _objB = new Pose3.Impl();
    }

    @After
    public void tearDown() throws Exception {
        _objA = null;
        _objB = null;
    }

    @Test
    public void constructEmpty() {
        Pose3 obj = new Pose3.Impl();
        MathTestUtils.checkEqualValues(MathUtils.DEFAULT_POSITION3, MathUtils.MINUS_Z_DIRECTION3, MathUtils.PLUS_Y_DIRECTION3, obj);
    }

    @Test
    public void constructDiscrete() {
        Tuple3 pos = new Tuple3.Impl(-1.0, 2.0, -3.0);
        Tuple3 lad = new Tuple3.Impl(.5774, -.5774, .5774);
        Tuple3 lud = new Tuple3.Impl(MathUtils.Y_AXIS3);
        Ray3 ray = new Ray3.Impl(pos, lad);
        Pose3 obj;
        obj = new Pose3.Impl(pos, lad, lud);
        MathTestUtils.checkEqualValues(ray, lud, obj);
        obj = new Pose3.Impl(ray, lud);
        MathTestUtils.checkEqualValues(ray, lud, obj);
    }

    @Test
    public void constructObject() {
        Tuple3 pos = new Tuple3.Impl(-1.0, 2.0, -3.0);
        Tuple3 lad = new Tuple3.Impl(.5774, -.5774, .5774);
        Tuple3 lud = new Tuple3.Impl(MathUtils.Y_AXIS3);
        Ray3 ray = new Ray3.Impl(pos, lad);
        getObjA().set(pos, lad, lud);
        Pose3 obj = new Pose3.Impl(getObjA());
        MathTestUtils.checkEqualValues(getObjA(), obj);
        MathTestUtils.checkEqualValues(ray, lud, obj);
    }

    @Test
    public void setDiscreteWhole() {
        Tuple3 pos = new Tuple3.Impl(-1.0, 2.0, -3.0);
        Tuple3 lad = new Tuple3.Impl(.5774, -.5774, .5774);
        Tuple3 lud = new Tuple3.Impl(MathUtils.Y_AXIS3);
        Ray3 ray = new Ray3.Impl(pos, lad);
        getObjA().set(pos, lad, lud);
        MathTestUtils.checkEqualValues(pos, lad, lud, getObjA());
        getObjA().set(ray, lud);
        MathTestUtils.checkEqualValues(pos, lad, lud, getObjA());
    }

    @Test
    public void setDiscretePLL() {
        Tuple3 pos = new Tuple3.Impl(-1.0, 2.0, -3.0);
        Tuple3 lad = new Tuple3.Impl(.5774, -.5774, .5774);
        Tuple3 lud = new Tuple3.Impl(MathUtils.Y_AXIS3);
        Ray3 ray = new Ray3.Impl(pos, lad);
        getObjA().setLookFromPosition(pos);
        getObjA().setLookAtDirection(lad);
        getObjA().setLookUpDirection(lud);
        MathTestUtils.checkEqualValues(ray, lud, getObjA());
    }

    @Test
    public void setDiscreteRL() {
        Tuple3 pos = new Tuple3.Impl(-1.0, 2.0, -3.0);
        Tuple3 lad = new Tuple3.Impl(.5774, -.5774, .5774);
        Tuple3 lud = new Tuple3.Impl(MathUtils.Y_AXIS3);
        Ray3 ray = new Ray3.Impl(pos, lad);
        getObjA().setLookAtRay(ray);
        MathTestUtils.checkEqualValues(ray, lud, getObjA());
    }

    @Test
    public void getDiscrete() {
        Tuple3 pos = new Tuple3.Impl(-1.0, 2.0, -3.0);
        Tuple3 lad = new Tuple3.Impl(.5774, -.5774, .5774);
        Tuple3 lud = new Tuple3.Impl(MathUtils.Y_AXIS3);
        Ray3 ray = new Ray3.Impl(pos, lad);
        getObjA().set(pos, lad, lud);
        MathTestUtils.checkEqualValues(pos, getObjA().getLookFromPosition());
        MathTestUtils.checkEqualValues(lad, getObjA().getLookAtDirection());
        MathTestUtils.checkEqualValues(lud, getObjA().getLookUpDirection());
        MathTestUtils.checkEqualValues(ray, getObjA().getLookAtRay());
    }

    @Test
    public void getAxes() {
        Tuple3 pos = new Tuple3.Impl(-1.0, 2.0, -3.0);
        Tuple3 lad = new Tuple3.Impl(.5774, -.5774, .5774);
        Tuple3 lud = new Tuple3.Impl(MathUtils.Y_AXIS3);
        UnitVector3 expX = new UnitVector3.Impl(-.7071, 0.0, .7071);
        UnitVector3 expY = new UnitVector3.Impl(.4082, .8165, .4082);
        UnitVector3 expZ = new UnitVector3.Impl(-.5774, .5774, -.5774);
        getObjA().set(pos, lad, lud);
        MathTestUtils.checkEqualValues(expX, getObjA().getAxisX());
        MathTestUtils.checkEqualValues(expY, getObjA().getAxisY());
        MathTestUtils.checkEqualValues(expZ, getObjA().getAxisZ());
        Tuple3 tuple = new Tuple3.Impl();
        MathTestUtils.checkEqualValues(getObjA().getAxisZ(), tuple.cross(getObjA().getAxisX(), getObjA().getAxisY()));
        MathTestUtils.checkEqualValues(getObjA().getAxisX(), tuple.cross(getObjA().getAxisY(), getObjA().getAxisZ()));
        MathTestUtils.checkEqualValues(getObjA().getAxisY(), tuple.cross(getObjA().getAxisZ(), getObjA().getAxisX()));
    }

    @Test
    public void setObject() {
        Tuple3 pos = new Tuple3.Impl(-1.0, 2.0, -3.0);
        Tuple3 lad = new Tuple3.Impl(.5774, -.5774, .5774);
        Tuple3 lud = new Tuple3.Impl(MathUtils.Y_AXIS3);
        Ray3 ray = new Ray3.Impl(pos, lad);
        getObjB().set(pos, lad, lud);
        getObjA().set(getObjB());
        MathTestUtils.checkEqualValues(getObjB(), getObjA());
        MathTestUtils.checkEqualValues(ray, lud, getObjA());
    }

    @Test
    public void dupe() {
        Tuple3 pos = new Tuple3.Impl(-1.0, 2.0, -3.0);
        Tuple3 lad = new Tuple3.Impl(.5774, -.5774, .5774);
        Tuple3 lud = new Tuple3.Impl(MathUtils.Y_AXIS3);
        Ray3 ray = new Ray3.Impl(pos, lad);
        getObjA().set(pos, lad, lud);
        Pose3 dupe = getObjA().dupe();
        TestUtils.checkNotObjectEquals(getObjA(), dupe);
        MathTestUtils.checkEqualValues(ray, lud, getObjA());
    }

    @Test
    public void update() {
        Tuple3 pos = new Tuple3.Impl(-1.0, 2.0, -3.0);
        Tuple3 lad = new Tuple3.Impl(.5774, -.5774, .5774);
        Tuple3 lud = new Tuple3.Impl(MathUtils.Y_AXIS3);
        Ray3 ray = new Ray3.Impl(pos, lad);
        class MyObserver implements UpdateNotice.Observer<MathObject> {

            @Override
            public void handleUpdate(MathObject observable) {
                _result = (Pose3) observable;
            }

            public Pose3 getResult() {
                Pose3 result = _result;
                _result = null;
                return result;
            }

            private Pose3 _result = null;
        }
        MyObserver obs = new MyObserver();
        getObjA().addUpdateObserver(obs);
        getObjA().setLookFromPosition(pos);
        MathTestUtils.checkEqualValues(pos, obs.getResult().getLookFromPosition());
        getObjA().setLookAtDirection(lad);
        MathTestUtils.checkEqualValues(lad, obs.getResult().getLookAtDirection());
        getObjA().setLookUpDirection(lud);
        MathTestUtils.checkEqualValues(lud, obs.getResult().getLookUpDirection());
        getObjA().setLookAtRay(ray);
        MathTestUtils.checkEqualValues(ray, obs.getResult().getLookAtRay());
        getObjA().set(pos, lad, lud);
        MathTestUtils.checkEqualValues(pos, lad, lud, obs.getResult());
        getObjA().set(ray, lud);
        MathTestUtils.checkEqualValues(ray, lud, obs.getResult());
        getObjA().set(getObjB());
        MathTestUtils.checkEqualValues(getObjB(), obs.getResult());
    }

    @Test
    public void viewUnmod() {
        Tuple3 pos = new Tuple3.Impl(-1.0, 2.0, -3.0);
        Tuple3 lad = new Tuple3.Impl(.5774, -.5774, .5774);
        Tuple3 lud = new Tuple3.Impl(MathUtils.Y_AXIS3);
        Ray3 ray = new Ray3.Impl(pos, lad);
        Pose3 unmod = new Pose3.Unmod(getObjA());
        TestUtils.checkObjectEquals(unmod, getObjA());
        getObjA().set(pos, lad, lud);
        TestUtils.checkObjectEquals(unmod, getObjA());
        MathTestUtils.checkEqualValues(ray, lud, unmod);
    }

    @Test(expected = UnmodifiableStateException.class)
    public void setUnmod() {
        Tuple3 pos = new Tuple3.Impl(-1.0, 2.0, -3.0);
        Tuple3 lad = new Tuple3.Impl(.5774, -.5774, .5774);
        Tuple3 lud = new Tuple3.Impl(MathUtils.Y_AXIS3);
        Ray3 ray = new Ray3.Impl(pos, lad);
        Pose3 unmod = new Pose3.Unmod(getObjA());
        unmod.set(ray, lud);
    }

    @Test
    public void getUnmod() {
        Tuple3 pos = new Tuple3.Impl(-1.0, 2.0, -3.0);
        Tuple3 lad = new Tuple3.Impl(.5774, -.5774, .5774);
        Tuple3 lud = new Tuple3.Impl(MathUtils.Y_AXIS3);
        Ray3 ray = new Ray3.Impl(pos, lad);
        Pose3 unmod = new Pose3.Unmod(getObjA());
        getObjA().set(ray, lud);
        MathTestUtils.checkEqualValues(pos, unmod.getLookFromPosition());
        MathTestUtils.checkEqualValues(lad, unmod.getLookAtDirection());
        MathTestUtils.checkEqualValues(lud, unmod.getLookUpDirection());
        MathTestUtils.checkEqualValues(ray, unmod.getLookAtRay());
    }

    @Test(expected = UnmodifiableStateException.class)
    public void setObjectUnmod() {
        Pose3 unmod = new Pose3.Unmod(getObjA());
        unmod.set(getObjB());
    }

    @Test
    public void dupeUnmod() {
        Tuple3 pos = new Tuple3.Impl(-1.0, 2.0, -3.0);
        Tuple3 lad = new Tuple3.Impl(.5774, -.5774, .5774);
        Tuple3 lud = new Tuple3.Impl(MathUtils.Y_AXIS3);
        Ray3 ray = new Ray3.Impl(pos, lad);
        Pose3 unmod = new Pose3.Unmod(getObjA());
        getObjA().set(pos, lad, lud);
        Pose3 dupe = unmod.dupe();
        TestUtils.checkObjectEquals(unmod, dupe);
        MathTestUtils.checkEqualValues(dupe, unmod);
        MathTestUtils.checkEqualValues(ray, lud, dupe);
    }

    @Test(expected = UnmodifiableStateException.class)
    public void dupeUnmodSet() {
        Tuple3 pos = new Tuple3.Impl(-1.0, 2.0, -3.0);
        Tuple3 lad = new Tuple3.Impl(.5774, -.5774, .5774);
        Tuple3 lud = new Tuple3.Impl(MathUtils.Y_AXIS3);
        Ray3 ray = new Ray3.Impl(pos, lad);
        Pose3 unmod = new Pose3.Unmod(getObjA());
        Pose3 dupe = unmod.dupe();
        dupe.set(ray, lud);
    }

    @Test
    public void superTest() {
        class SuperTest extends TestMathObject {

            @Override
            protected MathObject getObjA() {
                return getObjA();
            }

            @Override
            protected MathObject getObjB() {
                return getObjB();
            }
        }
        ;
        JUnitCore.runClasses(SuperTest.class);
    }

    @Test
    public void equalsValueTolerance() {
        Tuple3 pos = new Tuple3.Impl(-1.0, 2.0, -3.0);
        Tuple3 lad = new Tuple3.Impl(.5774, -.5774, .5774);
        Tuple3 lud = new Tuple3.Impl(MathUtils.Y_AXIS3);
        Ray3 ray = new Ray3.Impl(pos, lad);
        getObjA().set(ray, lud);
        getObjB().set(ray, lud);
        Assert.assertTrue(getObjA().equalsValue(getObjB(), 0.0));
        Assert.assertTrue(getObjB().equalsValue(getObjA(), 0.0));
        pos.add(.01);
        getObjA().setLookFromPosition(pos);
        Assert.assertTrue(getObjA().equalsValue(getObjB(), .1));
        Assert.assertTrue(getObjB().equalsValue(getObjA(), .1));
        Assert.assertTrue(!getObjA().equalsValue(getObjB(), .001));
        Assert.assertTrue(!getObjB().equalsValue(getObjA(), .001));
        getObjB().setLookFromPosition(pos);
        Assert.assertTrue(getObjA().equalsValue(getObjB(), 0.0));
        lad.add(-.01);
        getObjA().setLookAtDirection(lad);
        Assert.assertTrue(getObjA().equalsValue(getObjB(), .1));
        Assert.assertTrue(getObjB().equalsValue(getObjA(), .1));
        Assert.assertTrue(!getObjA().equalsValue(getObjB(), .001));
        Assert.assertTrue(!getObjB().equalsValue(getObjA(), .001));
        getObjB().setLookAtDirection(lad);
        Assert.assertTrue(getObjA().equalsValue(getObjB(), 0.0));
        lud.add(-.01);
        getObjA().setLookUpDirection(lud);
        Assert.assertTrue(getObjA().equalsValue(getObjB(), .1));
        Assert.assertTrue(getObjB().equalsValue(getObjA(), .1));
        Assert.assertTrue(!getObjA().equalsValue(getObjB(), .001));
        Assert.assertTrue(!getObjB().equalsValue(getObjA(), .001));
        getObjB().setLookUpDirection(lud);
        Assert.assertTrue(getObjA().equalsValue(getObjB(), 0.0));
    }

    private Pose3.Impl _objA;

    private Pose3.Impl _objB;
}
