package net.sourceforge.c4jplugin.internal.annotationtypes;

import net.sourceforge.c4jplugin.C4JActivator;

public interface IC4JAnnotations {

    public static String TYPE_CLASS_INVARIANT = C4JActivator.PLUGIN_ID + ".annotationtypes.ClassInvariantAnnotation";

    public static String TYPE_CONTRACTED_CLASS_INVARIANT = C4JActivator.PLUGIN_ID + ".annotationtypes.ContractedClassInvariantAnnotation";

    public static String TYPE_CONTRACTED_METHOD = C4JActivator.PLUGIN_ID + ".annotationtypes.ContractedMethodAnnotation";

    public static String TYPE_METHOD_CONTRACT = C4JActivator.PLUGIN_ID + ".annotationtypes.MethodAnnotation";

    public static String TYPE_WARNING = C4JActivator.PLUGIN_ID + ".annotationtypes.WarningAnnotation";
}
