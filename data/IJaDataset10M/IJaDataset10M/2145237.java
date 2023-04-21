package com.pcmsolutions.device.EMU.E4.zcommands;

import com.pcmsolutions.system.ZCommand;
import com.pcmsolutions.system.Zoeos;
import java.util.*;

/**
 * User: paulmeehan
 * Date: 02-May-2004
 * Time: 15:54:29
 */
public class ZCommandRegistry {

    private static final Hashtable classMap = new Hashtable();

    private static final Hashtable classStringMap = new Hashtable();

    static {
        makeClassStringMap();
        makeClassMap();
    }

    private static void makeClassStringMap() {
        classStringMap.put(E4DeviceZCommandMarker.class, "com.pcmsolutions.device.EMU.E4.zcommands.device.DeviceShowConfigurationZC;com.pcmsolutions.device.EMU.E4.zcommands.device.RenameDeviceZC;com.pcmsolutions.device.EMU.E4.zcommands.device.EraseBankZC;com.pcmsolutions.device.EMU.E4.zcommands.device.RefreshBankZC;com.pcmsolutions.device.EMU.E4.zcommands.device.TakeDeviceWorkspaceSnapshotZC");
        classStringMap.put(E4ContextBasicEditablePresetZCommandMarker.class, "com.pcmsolutions.device.EMU.E4.zcommands.preset.ErasePresetZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.RenamePresetZC;com.pcmsolutions.device.EMU.E4.zcommands.preset.SpecialPresetNamingZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.RenamePresetAllZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.AutoNamePresetUserSamplesZC");
        classStringMap.put(E4ContextBasicEditableSampleZCommandMarker.class, "com.pcmsolutions.device.EMU.E4.zcommands.sample.EraseSampleZMTC;com.pcmsolutions.device.EMU.E4.zcommands.sample.RenameSampleZC;com.pcmsolutions.device.EMU.E4.zcommands.sample.RenameSampleAllZMTC;com.pcmsolutions.device.EMU.E4.zcommands.sample.SpecialSampleNamingZMTC");
        classStringMap.put(E4ContextEditablePresetZCommandMarker.class, "com.pcmsolutions.device.EMU.E4.zcommands.preset.PurgePresetZonesZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.ErasePresetSamplesZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.PurgePresetLinksZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.PurgeEmptySamplesZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.NewPresetZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.NewPresetVoicesZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.NewPresetLinksZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.OffsetPresetLinksZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.OffsetPresetSamplesZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.MatchOriginalKeysToSampleNameZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.SortPresetZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.AutoMapPresetZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.AssertRemotePresetZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.LoadPresetPackageZMTC");
        classStringMap.put(E4ContextEditableSampleZCommandMarker.class, "com.pcmsolutions.device.EMU.E4.zcommands.sample.CopyContextSampleZC;com.pcmsolutions.device.EMU.E4.zcommands.sample.CopyRangeContextSampleZC;com.pcmsolutions.device.EMU.E4.zcommands.sample.CopyBlockContextSamplesZMTC;com.pcmsolutions.device.EMU.E4.zcommands.sample.SaveContextSamplesZMTC;com.pcmsolutions.device.EMU.E4.zcommands.sample.LoadContextSamplesZMTC;com.pcmsolutions.device.EMU.E4.zcommands.sample.NewSamplePackageZMTC;com.pcmsolutions.device.EMU.E4.zcommands.sample.LoadSamplePackageZMTC");
        classStringMap.put(E4ContextReadablePresetZCommandMarker.class, "com.pcmsolutions.device.EMU.E4.zcommands.preset.CopyContextPresetZC;com.pcmsolutions.device.EMU.E4.zcommands.preset.CopyRangeContextPresetZC;com.pcmsolutions.device.EMU.E4.zcommands.preset.CopyBlockContextPresetsZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.CopyDeepContextPresetZMTC;");
        classStringMap.put(E4ContextReadableSampleZCommandMarker.class, "");
        classStringMap.put(E4EditableParameterModelZCommandMarker.class, "com.pcmsolutions.device.EMU.E4.zcommands.parameter.ParameterDefaultZMTC;com.pcmsolutions.device.EMU.E4.zcommands.parameter.ParameterMinimizeZMTC;com.pcmsolutions.device.EMU.E4.zcommands.parameter.ParameterMaximizeZMTC;com.pcmsolutions.device.EMU.E4.zcommands.parameter.ParameterUnitStepFromCurrentZMTC;com.pcmsolutions.device.EMU.E4.zcommands.parameter.ParameterJumpZMTC;com.pcmsolutions.device.EMU.E4.zcommands.parameter.ParameterRotateZMTC;com.pcmsolutions.device.EMU.E4.zcommands.parameter.ParameterReverseZMTC;com.pcmsolutions.device.EMU.E4.zcommands.parameter.ParameterRandomizeZMTC;com.pcmsolutions.device.EMU.E4.zcommands.parameter.ParameterPercentilesZMTC;com.pcmsolutions.device.EMU.E4.zcommands.parameter.ParameterLinearFadeZMTC;com.pcmsolutions.device.EMU.E4.zcommands.parameter.ParameterRepeatZMTC;com.pcmsolutions.device.EMU.E4.zcommands.parameter.ParameterUnitStepFromFirstZMTC;com.pcmsolutions.device.EMU.E4.zcommands.parameter.ParameterScaleCurrentZMTC;com.pcmsolutions.device.EMU.E4.zcommands.parameter.ParameterReflectionZMTC;com.pcmsolutions.device.EMU.E4.zcommands.parameter.ParameterPositionalReflectionZMTC");
        classStringMap.put(E4EditableVoiceZCommandMarker.class, "com.pcmsolutions.device.EMU.E4.zcommands.preset.EditVoiceZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.EditVoiceAsGroupZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.CopyVoiceZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.DeleteVoiceZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.NewVoiceZoneZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.ExpandCombineVoiceZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.SplitVoiceZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.MatchVoiceKeyToSampleNameZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.AutoMapVoiceGroupZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.ChromaticMapVoiceGroupZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.WhiteMapVoiceGroupZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.BlackMapVoiceGroupZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.SpanMapVoiceGroupZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.AutoMapVoicesZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.ChromaticMapVoicesZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.WhiteMapVoicesZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.BlackMapVoicesZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.SpanMapVoicesZMTC");
        classStringMap.put(E4EditableZoneZCommandMarker.class, "com.pcmsolutions.device.EMU.E4.zcommands.preset.RemoveZoneZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.MatchZoneKeyToSampleNameZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.AutoMapZonesZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.ChromaticMapZonesZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.WhiteMapZonesZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.BlackMapZonesZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.SpanMapZonesZMTC");
        classStringMap.put(E4EditableLinkZCommandMarker.class, "com.pcmsolutions.device.EMU.E4.zcommands.preset.RemoveLinkZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.CopyLinkZMTC;");
        classStringMap.put(E4MasterContextZCommandMarker.class, "com.pcmsolutions.device.EMU.E4.zcommands.device.RefreshMasterContextZC;");
        classStringMap.put(E4MultiModeChannelZCommandMarker.class, "");
        classStringMap.put(E4MultiModeContextZCommandMarker.class, "com.pcmsolutions.device.EMU.E4.zcommands.device.RefreshMultiModeContextZC;");
        classStringMap.put(E4ReadableParameterModelZCommandMarker.class, "");
        classStringMap.put(E4ReadablePresetZCommandMarker.class, "com.pcmsolutions.device.EMU.E4.zcommands.preset.RefreshPresetZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.OpenPresetZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.ClosePresetZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.AuditionPresetZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.SendPresetToMultiModeZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.NewPresetPackageZMTC;");
        classStringMap.put(E4ReadableSampleZCommandMarker.class, "com.pcmsolutions.device.EMU.E4.zcommands.sample.RefreshSampleZMTC;com.pcmsolutions.device.EMU.E4.zcommands.sample.AuditionSamplesZMTC");
        classStringMap.put(E4ReadableVoiceZCommandMarker.class, "com.pcmsolutions.device.EMU.E4.zcommands.preset.ViewVoiceZMTC;com.pcmsolutions.device.EMU.E4.zcommands.preset.AuditionVoicesZMTC;");
        classStringMap.put(E4ReadableZoneZCommandMarker.class, "com.pcmsolutions.device.EMU.E4.zcommands.preset.AuditionZonesZMTC;");
    }

    private static void makeClassMap() {
        for (Iterator it = classStringMap.keySet().iterator(); it.hasNext(); ) {
            Class marker = (Class) it.next();
            String s = (String) classStringMap.get(marker);
            if (s != null) {
                Enumeration tok = new StringTokenizer(s, Zoeos.preferenceFieldSeperator);
                ArrayList classes = new ArrayList();
                while (tok.hasMoreElements()) {
                    String c = (String) tok.nextElement();
                    try {
                        Class cls = Class.forName(c);
                        if (marker.isAssignableFrom(cls) && ZCommand.class.isAssignableFrom(cls)) classes.add(cls); else System.out.println("Configured ZCommand " + c.toString() + "class not of the correct type.");
                    } catch (ClassNotFoundException e) {
                        System.out.println("Configured ZCommand " + c + " is not a valid class.");
                    }
                }
                classMap.put(marker, classes.toArray(new Class[classes.size()]));
            }
        }
    }

    public static String getCommandClassStrings(Class marker) {
        if (marker == null) return "";
        return (String) classStringMap.get(marker);
    }

    public static Class[] getCommandClasses(Class marker) {
        return getCommandClasses(new Class[] { marker });
    }

    public static void insert(ZCommand incoming, List<ZCommand> cmds) {
        for (ListIterator<ZCommand> i = cmds.listIterator(); i.hasNext(); ) if (incoming.overrides(i.next())) i.remove();
        cmds.add(incoming);
    }

    public static Class[] getCommandClasses(Class[] markers) {
        ArrayList<Class> cmdClasses = new ArrayList<Class>();
        for (int i = 0; i < markers.length; i++) {
            if (markers[i] == null) continue;
            Class[] classes = (Class[]) classMap.get(markers[i]);
            if (classes == null) continue;
            cmdClasses.addAll(Arrays.asList(classes));
        }
        return (Class[]) cmdClasses.toArray(new Class[cmdClasses.size()]);
    }
}
