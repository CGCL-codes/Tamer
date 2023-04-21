package org.jtomtom.junit;

import static junit.framework.Assert.*;
import java.io.File;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jtomtom.device.TomtomDeviceFinder;
import org.jtomtom.device.providers.CarminatFilesProvider;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestTomtomDeviceFinder {

    @BeforeClass
    public static void initLogger() {
        if (!Logger.getRootLogger().getAllAppenders().hasMoreElements()) BasicConfigurator.configure();
        Logger.getRootLogger().setLevel(Level.DEBUG);
    }

    @Test
    public void testFindMountPoint() {
        File ttMoundPoint = TomtomDeviceFinder.findMountPoint();
        assertNotNull(ttMoundPoint);
        assertTrue(ttMoundPoint.exists());
        assertTrue(new File(ttMoundPoint, "ttgo.bif").exists() || new File(ttMoundPoint, CarminatFilesProvider.DIR_CARMINAT_LOOPBACK + File.separator + CarminatFilesProvider.FILE_CARMINAT_LOOPBACK).exists());
    }
}
