package com.hifiremote.jp1;

import java.awt.Color;
import java.util.Properties;

public class Activity extends Highlight {

    public Activity(Button button, Remote remote) {
        this.button = button;
        name = button.getName();
        setSegmentFlags(0xFF);
        activityGroups = new ActivityGroup[remote.getActivityButtonGroups().length];
        for (int i = 0; i < activityGroups.length; i++) {
            activityGroups[i] = new ActivityGroup(i, remote);
        }
    }

    public Activity(Properties props) {
        super(props);
        name = props.getProperty("Name");
        helpSegmentFlags = Integer.parseInt(props.getProperty("HelpSegmentFlags"));
        Hex hex = new Hex(props.getProperty("HelpSettings"));
        audioHelp = hex.getData()[0];
        videoHelp = hex.getData()[1];
        notes = props.getProperty("Notes");
        String temp = props.getProperty("GroupSettings");
        if (temp != null) {
            hex = new Hex(temp);
            activityGroups = new ActivityGroup[hex.length()];
            for (int index = 0; index < hex.length(); index++) {
                activityGroups[index] = new ActivityGroup(index, hex.getData()[index]);
                activityGroups[index].setNotes(props.getProperty("GroupNotes" + index));
            }
        }
    }

    public void set(Remote remote) {
        button = remote.getButton(name);
        for (ActivityGroup group : activityGroups) {
            group.set(remote);
        }
    }

    public ActivityGroup[] getActivityGroups() {
        return activityGroups;
    }

    public Button getButton() {
        return button;
    }

    public void setButton(Button button) {
        this.button = button;
    }

    public Macro getMacro() {
        return macro;
    }

    public void setMacro(Macro macro) {
        this.macro = macro;
    }

    public int getAudioHelp() {
        return audioHelp;
    }

    public void setAudioHelp(int audioHelp) {
        this.audioHelp = audioHelp;
    }

    public int getVideoHelp() {
        return videoHelp;
    }

    public void setVideoHelp(int videoHelp) {
        this.videoHelp = videoHelp;
    }

    @Override
    public void setHighlight(Color color) {
        super.setHighlight(color);
        if (macro != null) {
            macro.setHighlight(color);
        }
    }

    public Segment getHelpSegment() {
        return helpSegment;
    }

    public void setHelpSegment(Segment helpSegment) {
        this.helpSegment = helpSegment;
    }

    public int getHelpSegmentFlags() {
        return helpSegmentFlags;
    }

    public void setHelpSegmentFlags(int helpSegmentFlags) {
        this.helpSegmentFlags = helpSegmentFlags;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void store(PropertyWriter pw) {
        pw.print("Name", button.getName());
        pw.print("HelpSegmentFlags", helpSegmentFlags);
        Hex hex = new Hex(2);
        hex.set((short) audioHelp, 0);
        hex.set((short) videoHelp, 1);
        pw.print("HelpSettings", hex.toString());
        if (notes != null && !notes.trim().isEmpty()) {
            pw.print("Notes", notes);
        }
        if (activityGroups != null) {
            super.store(pw);
            hex = new Hex(activityGroups.length);
            for (ActivityGroup group : activityGroups) {
                hex.set((short) group.getDeviceIndex(), group.getIndex());
                String notes = group.getNotes();
                if (notes != null && !notes.trim().isEmpty()) {
                    pw.print("GroupNotes" + group.getIndex(), notes);
                }
            }
            pw.print("GroupSettings", hex.toString());
        }
    }

    private ActivityGroup[] activityGroups = null;

    private Button button = null;

    private String name = null;

    private Macro macro = null;

    private String notes = null;

    private int audioHelp = 0;

    private int videoHelp = 0;

    private int helpSegmentFlags = 0xFF;

    private Segment helpSegment = null;
}
