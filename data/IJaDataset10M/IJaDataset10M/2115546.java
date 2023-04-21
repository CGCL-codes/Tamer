package de.cue4net.eventservice.tests.model;

import junit.framework.Assert;
import de.cue4net.eventservice.model.location.locationservice.LocationServiceType;

/**
 * @author Keino Uelze
 * @version $Id: LocationServiceTypeDAOTest.java,v 1.4 2008-06-05 11:00:06 keino Exp $
 */
public class LocationServiceTypeDAOTest extends AbstractEventserviceTest {

    public void testLocationServiceTypeDAO() {
        LocationServiceType locationServiceType = new LocationServiceType();
        LocationServiceType locationServiceTypePersistent = null;
        locationServiceType.setName("functionTestName");
        locationServiceType.setDescription("function description text");
        locationServiceType.setFeatureReference("");
        Assert.assertTrue(locationServiceTypeDAO.createOrUpdateLocationServiceType(locationServiceType));
        locationServiceTypePersistent = locationServiceTypeDAO.getLocationServiceTypeByID(locationServiceType.getId());
        Assert.assertTrue(locationServiceTypePersistent.equals(locationServiceType));
        Assert.assertTrue(locationServiceTypeDAO.getLocationServiceTypeByName("functionTestName").equals(locationServiceType));
    }
}
