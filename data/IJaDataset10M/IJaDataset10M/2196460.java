package android.content.pm.cts;

import dalvik.annotation.TestLevel;
import dalvik.annotation.TestTargetClass;
import dalvik.annotation.TestTargetNew;
import dalvik.annotation.TestTargets;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.content.pm.ServiceInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Parcel;
import android.test.AndroidTestCase;

@TestTargetClass(ServiceInfo.class)
public class ServiceInfoTest extends AndroidTestCase {

    private static final String PACKAGE_NAME = "com.android.cts.stub";

    private static final String SERVICE_NAME = "android.content.pm.cts.TestPmService";

    @TestTargets({ @TestTargetNew(level = TestLevel.COMPLETE, notes = "Test describeContents", method = "describeContents", args = {  }), @TestTargetNew(level = TestLevel.COMPLETE, notes = "Test constructors", method = "ServiceInfo", args = {  }), @TestTargetNew(level = TestLevel.COMPLETE, notes = "Test constructors", method = "ServiceInfo", args = { android.content.pm.ServiceInfo.class }), @TestTargetNew(level = TestLevel.COMPLETE, notes = "Test toString", method = "toString", args = {  }), @TestTargetNew(level = TestLevel.COMPLETE, notes = "Test writeToParcel", method = "writeToParcel", args = { android.os.Parcel.class, int.class }) })
    public void testServiceInfo() throws NameNotFoundException {
        PackageManager pm = getContext().getPackageManager();
        ComponentName componentName = new ComponentName(PACKAGE_NAME, SERVICE_NAME);
        Parcel p = Parcel.obtain();
        new ServiceInfo();
        ServiceInfo serviceInfo = pm.getServiceInfo(componentName, 0);
        ServiceInfo infoFromExisted = new ServiceInfo(serviceInfo);
        checkInfoSame(serviceInfo, infoFromExisted);
        assertNotNull(serviceInfo.toString());
        assertEquals(0, serviceInfo.describeContents());
        serviceInfo.writeToParcel(p, 0);
        p.setDataPosition(0);
        ServiceInfo infoFromParcel = ServiceInfo.CREATOR.createFromParcel(p);
        checkInfoSame(serviceInfo, infoFromParcel);
    }

    private void checkInfoSame(ServiceInfo expected, ServiceInfo actual) {
        assertEquals(expected.name, actual.name);
        assertEquals(expected.permission, actual.permission);
    }
}
