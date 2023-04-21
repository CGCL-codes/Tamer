package android.content.pm.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;
import android.content.pm.ConfigurationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Parcel;
import android.test.AndroidTestCase;

@TestTargetClass(ConfigurationInfo.class)
public class ConfigurationInfoTest extends AndroidTestCase {

    private static final String APK_ROUTE = "/data/app/com.android.cts.stub.apk";

    @TestTargets({ @TestTargetNew(level = TestLevel.COMPLETE, notes = "Test constructors", method = "ConfigurationInfo", args = {  }), @TestTargetNew(level = TestLevel.COMPLETE, notes = "Test constructors", method = "ConfigurationInfo", args = { android.content.pm.ConfigurationInfo.class }), @TestTargetNew(level = TestLevel.COMPLETE, notes = "Test toString", method = "toString", args = {  }), @TestTargetNew(level = TestLevel.COMPLETE, notes = "Test writeToParcel", method = "writeToParcel", args = { android.os.Parcel.class, int.class }), @TestTargetNew(level = TestLevel.COMPLETE, notes = "Test describeContents", method = "describeContents", args = {  }) })
    public void testConfigPreferences() {
        PackageManager pm = getContext().getPackageManager();
        new ConfigurationInfo();
        PackageInfo pkgInfo = pm.getPackageArchiveInfo(APK_ROUTE, PackageManager.GET_CONFIGURATIONS);
        ConfigurationInfo[] configInfoArray = pkgInfo.configPreferences;
        assertTrue(configInfoArray.length > 0);
        ConfigurationInfo configInfo = configInfoArray[0];
        ConfigurationInfo infoFromExisted = new ConfigurationInfo(configInfo);
        checkInfoSame(configInfo, infoFromExisted);
        assertEquals(0, configInfo.describeContents());
        assertNotNull(configInfo.toString());
        Parcel p = Parcel.obtain();
        configInfo.writeToParcel(p, 0);
        p.setDataPosition(0);
        ConfigurationInfo infoFromParcel = ConfigurationInfo.CREATOR.createFromParcel(p);
        checkInfoSame(configInfo, infoFromParcel);
        p.recycle();
    }

    private void checkInfoSame(ConfigurationInfo expected, ConfigurationInfo actual) {
        assertEquals(expected.reqKeyboardType, actual.reqKeyboardType);
        assertEquals(expected.reqTouchScreen, actual.reqTouchScreen);
        assertEquals(expected.reqInputFeatures, actual.reqInputFeatures);
        assertEquals(expected.reqNavigation, actual.reqNavigation);
    }
}
