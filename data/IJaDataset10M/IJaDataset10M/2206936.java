package de.hbrs.inf.atarrabi.test.model;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import de.hbrs.inf.atarrabi.model.SpatialCoverage;
import de.hbrs.inf.atarrabi.model.lov.Unit;

public class SpatialCoverageTest {

    private SpatialCoverage spatialCoverage = null;

    @BeforeClass
    public void beforeClass() {
        spatialCoverage = new SpatialCoverage();
    }

    @AfterClass
    public void afterClass() {
    }

    @Test
    public void testSetGetMinLat() {
        spatialCoverage.setMinLat(0d);
        assert 0 == spatialCoverage.getMinLat();
    }

    @Test
    public void testSetGetMaxLat() {
        spatialCoverage.setMaxLat(Double.MAX_VALUE);
        assert Double.MAX_VALUE == spatialCoverage.getMaxLat();
    }

    @Test
    public void testSetGetMinLon() {
        spatialCoverage.setMinLon(0d);
        assert 0 == spatialCoverage.getMinLon();
    }

    @Test
    public void testSetGetMaxLon() {
        spatialCoverage.setMaxLon(Double.MAX_VALUE);
        assert Double.MAX_VALUE == spatialCoverage.getMaxLon();
    }

    @Test
    public void testSetGetMinAltitude() {
        spatialCoverage.setMinAltitude(0d);
        assert 0 == spatialCoverage.getMinAltitude();
    }

    @Test
    public void testSetGetMaxAltitude() {
        spatialCoverage.setMaxAltitude(Double.MAX_VALUE);
        assert Double.MAX_VALUE == spatialCoverage.getMaxAltitude();
    }

    @Test
    public void testSetGetMinAltitudeUnit() {
        Unit minAltitudeUnit = new Unit();
        minAltitudeUnit.setName("acos");
        spatialCoverage.setMinAltitudeUnit(minAltitudeUnit);
        assert "acos" == spatialCoverage.getMinAltitudeUnit().getAcronym();
    }

    @Test
    public void testSetGetMaxAltitudeUnit() {
        Unit maxAltitudeUnit = new Unit();
        maxAltitudeUnit.setName("acos");
        spatialCoverage.setMaxAltitudeUnit(maxAltitudeUnit);
        assert "acos" == spatialCoverage.getMaxAltitudeUnit().getAcronym();
    }
}
