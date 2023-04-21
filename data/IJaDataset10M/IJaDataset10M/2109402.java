package com.googlecode.javacv.cpp;

import com.googlecode.javacpp.annotation.Platform;
import com.googlecode.javacpp.annotation.Properties;
import static com.googlecode.javacpp.Loader.*;
import static com.googlecode.javacv.cpp.avutil.*;

/**
 *
 * @author Samuel Audet
 */
@Properties({ @Platform(define = "__STDC_CONSTANT_MACROS", cinclude = "<libavdevice/avdevice.h>", includepath = genericIncludepath, linkpath = genericLinkpath, link = { "avdevice", "avformat", "avcodec", "avutil" }), @Platform(value = "windows", includepath = windowsIncludepath, linkpath = windowsLinkpath, preloadpath = windowsPreloadpath, preload = "avdevice-52"), @Platform(value = "android", includepath = androidIncludepath, linkpath = androidLinkpath) })
public class avdevice {

    static {
        load(avformat.class);
        load();
    }

    public static final int LIBAVDEVICE_VERSION_MAJOR = 52;

    public static final int LIBAVDEVICE_VERSION_MINOR = 2;

    public static final int LIBAVDEVICE_VERSION_MICRO = 0;

    public static final int LIBAVDEVICE_VERSION_INT = AV_VERSION_INT(LIBAVDEVICE_VERSION_MAJOR, LIBAVDEVICE_VERSION_MINOR, LIBAVDEVICE_VERSION_MICRO);

    public static final String LIBAVDEVICE_VERSION = AV_VERSION(LIBAVDEVICE_VERSION_MAJOR, LIBAVDEVICE_VERSION_MINOR, LIBAVDEVICE_VERSION_MICRO);

    public static final int LIBAVDEVICE_BUILD = LIBAVDEVICE_VERSION_INT;

    public static native int avdevice_version();

    public static native String avdevice_configuration();

    public static native String avdevice_license();

    public static native void avdevice_register_all();
}
