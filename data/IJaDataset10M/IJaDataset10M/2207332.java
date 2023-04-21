package org.xmlvm.common;

import org.xmlvm.commondevice.subsystems.CommonDeviceAccelerometer;
import org.xmlvm.commondevice.subsystems.CommonDeviceDispatcher;
import org.xmlvm.commondevice.subsystems.CommonDeviceFileSystem;
import org.xmlvm.commondevice.subsystems.CommonDeviceFontFactory;
import org.xmlvm.commondevice.subsystems.CommonDevicePreferences;
import org.xmlvm.commondevice.subsystems.CommonDeviceProperties;
import org.xmlvm.commondevice.subsystems.CommonDeviceWidgetFactory;
import org.xmlvm.commondevice.subsystems.CommonDeviceWindow;
import android.hardware.SensorManager;

/**
 *
 */
public interface CommonDeviceAPI {

    public CommonDeviceFileSystem getFileSystem();

    public CommonDevicePreferences getPreferences();

    public CommonDeviceAccelerometer getAccelerometer(SensorManager sensorManager);

    public CommonDeviceProperties getProperties();

    public CommonDeviceWidgetFactory getWidgetFactory();

    public CommonDeviceDispatcher getDispatcher();

    public CommonDeviceWindow getWindow();

    public CommonDeviceFontFactory getFontFactory();
}
