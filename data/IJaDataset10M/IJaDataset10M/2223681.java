package com.powers.wsexplorer.gui;

import org.eclipse.swt.graphics.RGB;

public class Options {

    public boolean ignoreHostCertificates;

    public static final String IGNORE_HOST_CERTIFICATS_KEY = "ignore.host.certificates";

    public static final RGB DefaultRGB = new RGB(0, 0, 0);

    public static final RGB ValueRGB = new RGB(42, 0, 255);

    public static final RGB TagRGB = new RGB(63, 127, 127);

    public static final RGB AttributeRGB = new RGB(102, 0, 102);

    public static final RGB ScriptRGB = new RGB(136, 0, 0);

    public static final RGB CommentRGB = new RGB(102, 102, 0);

    public static final RGB ErrorRGB = new RGB(255, 0, 0);

    public static final RGB TagValueRGB = DefaultRGB;
}
