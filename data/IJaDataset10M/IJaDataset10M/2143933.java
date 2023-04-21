package com.pcmsolutions.device.EMU.E4;

import com.pcmsolutions.device.EMU.E4.preset.ContextEditablePreset;
import com.pcmsolutions.device.EMU.E4.preset.ReadablePreset;
import com.pcmsolutions.system.paths.LogicalPath;

/**
 * User: paulmeehan
 * Date: 21-Jan-2004
 * Time: 11:42:55
 */
public class PathFactory {

    private static final String PATH_ELEMENT_DEVICE = "e4device";

    private static final String PATH_ELEMENT_PRESETS = "presets";

    private static final String PATH_ELEMENT_PRESET_USER = "user";

    private static final String PATH_ELEMENT_PROPERTIES = "properties";

    private static final String PATH_ELEMENT_SAMPLES = "samples";

    private static final String PATH_ELEMENT_MULTIMODE = "multimode";

    private static final String PATH_ELEMENT_MASTER = "master";

    private static final String PATH_ELEMENT_ZONES = "zones";

    private static final String PATH_ELEMENT_VOICES = "voices";

    private static final String PATH_ELEMENT_GROUPS = "groups";

    private static final String PATH_ELEMENT_VOICE_SECTIONS = "sections";

    private static final String PATH_ELEMENT_LINKS = "links";

    private static final String PATH_ELEMENT_PARAMETERS = "parameters";

    private static final String PATH_ELEMENT_PIANO = "piano";

    public static LogicalPath providePianoPath(DeviceContext dc) {
        return new LogicalPath(dc.getSystemEntryPoint(), new Object[] { PATH_ELEMENT_DEVICE, dc.getStaticName(), PATH_ELEMENT_PIANO });
    }

    public static LogicalPath provideDevicePath(DeviceContext dc) {
        return new LogicalPath(dc.getSystemEntryPoint(), new Object[] { PATH_ELEMENT_DEVICE, dc.getStaticName() });
    }

    public static LogicalPath providePropertiesPath(DeviceContext dc) {
        return new LogicalPath(dc.getSystemEntryPoint(), new Object[] { PATH_ELEMENT_DEVICE, dc.getStaticName(), PATH_ELEMENT_PROPERTIES });
    }

    public static LogicalPath providePresetsPath(DeviceContext dc) {
        return new LogicalPath(dc.getSystemEntryPoint(), new Object[] { PATH_ELEMENT_DEVICE, dc.getStaticName(), PATH_ELEMENT_PRESETS });
    }

    public static LogicalPath provideSamplesPath(DeviceContext dc) {
        return new LogicalPath(dc.getSystemEntryPoint(), new Object[] { PATH_ELEMENT_DEVICE, dc.getStaticName(), PATH_ELEMENT_SAMPLES });
    }

    public static LogicalPath provideMultiModePath(DeviceContext dc) {
        return new LogicalPath(dc.getSystemEntryPoint(), new Object[] { PATH_ELEMENT_DEVICE, dc.getStaticName(), PATH_ELEMENT_MULTIMODE });
    }

    public static LogicalPath provideMasterPath(DeviceContext dc) {
        return new LogicalPath(dc.getSystemEntryPoint(), new Object[] { PATH_ELEMENT_DEVICE, dc.getStaticName(), PATH_ELEMENT_MASTER });
    }

    public static LogicalPath providePresetPath(ReadablePreset preset) {
        return providePresetPaths(new ReadablePreset[] { preset })[0];
    }

    public static LogicalPath providePresetUserPath(ReadablePreset preset) {
        return providePresetUserPaths(new ReadablePreset[] { preset })[0];
    }

    public static LogicalPath[] providePresetPaths(ReadablePreset[] presets) {
        LogicalPath[] paths = new LogicalPath[presets.length];
        for (int i = 0; i < presets.length; i++) paths[i] = providePresetsPath(presets[i].getDeviceContext()).append(presets[i].getIndex());
        return paths;
    }

    public static LogicalPath[] providePresetUserPaths(ReadablePreset[] presets) {
        LogicalPath[] paths = new LogicalPath[presets.length];
        for (int i = 0; i < presets.length; i++) paths[i] = providePresetPath(presets[i]).append(PATH_ELEMENT_PRESET_USER);
        return paths;
    }

    public static LogicalPath provideVoicePath(ReadablePreset.ReadableVoice voice) {
        return provideVoicePaths(new ReadablePreset.ReadableVoice[] { voice })[0];
    }

    public static LogicalPath[] provideVoicePaths(ReadablePreset.ReadableVoice[] voices) {
        LogicalPath[] paths = new LogicalPath[voices.length];
        for (int i = 0; i < voices.length; i++) if (voices[i] instanceof ContextEditablePreset.EditableVoice && ((ContextEditablePreset.EditableVoice) voices[i]).getGroupMode()) paths[i] = provideGroupPath(voices[i]); else paths[i] = providePresetPath(voices[i].getPreset()).append(new Object[] { PATH_ELEMENT_VOICES, voices[i].getVoiceNumber() });
        return paths;
    }

    public static LogicalPath provideVoiceSectionPath(ReadablePreset.ReadableVoice voice, int sections) {
        return provideVoiceSectionPaths(new ReadablePreset.ReadableVoice[] { voice }, sections)[0];
    }

    public static LogicalPath[] provideVoiceSectionPaths(ReadablePreset.ReadableVoice[] voices, int sections) {
        LogicalPath[] paths = new LogicalPath[voices.length];
        for (int i = 0; i < voices.length; i++) if (voices[i] instanceof ContextEditablePreset.EditableVoice && ((ContextEditablePreset.EditableVoice) voices[i]).getGroupMode()) paths[i] = provideGroupSectionPath(voices[i], sections); else paths[i] = provideVoicePath(voices[i]).append(new Object[] { PATH_ELEMENT_VOICE_SECTIONS, VoiceSections.makeEnumeratedSectionString(sections) });
        return paths;
    }

    public static LogicalPath provideGroupPath(ReadablePreset.ReadableVoice voice) {
        LogicalPath path;
        path = providePresetPath(voice.getPreset()).append(new Object[] { PATH_ELEMENT_GROUPS, "G(v" + voice.getVoiceNumber() + ")" });
        return path;
    }

    public static LogicalPath provideGroupSectionPath(ReadablePreset.ReadableVoice voice, int sections) {
        LogicalPath path;
        path = provideGroupPath(voice).append(new Object[] { PATH_ELEMENT_VOICE_SECTIONS, VoiceSections.makeEnumeratedSectionString(sections) });
        return path;
    }

    public static LogicalPath provideLinkPath(ReadablePreset.ReadableLink link) {
        return provideLinkPaths(new ReadablePreset.ReadableLink[] { link })[0];
    }

    public static LogicalPath[] provideLinkPaths(ReadablePreset.ReadableLink[] links) {
        LogicalPath[] paths = new LogicalPath[links.length];
        for (int i = 0; i < links.length; i++) paths[i] = providePresetPath(links[i].getPreset()).append(new Object[] { PATH_ELEMENT_LINKS, links[i].getLinkNumber() });
        return paths;
    }

    public static LogicalPath provideZonePaths(ReadablePreset.ReadableVoice.ReadableZone zone) {
        return provideZonePaths(new ReadablePreset.ReadableVoice.ReadableZone[] { zone })[0];
    }

    public static LogicalPath[] provideZonePaths(ReadablePreset.ReadableVoice.ReadableZone[] zones) {
        LogicalPath[] paths = new LogicalPath[zones.length];
        for (int i = 0; i < zones.length; i++) paths[i] = provideVoicePath(zones[i].getVoice()).append(new Object[] { PATH_ELEMENT_ZONES, zones[i].getZoneNumber() });
        return paths;
    }
}
