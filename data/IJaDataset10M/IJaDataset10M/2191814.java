package com.bbn.vessel.author.slideware;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import com.bbn.vessel.author.workspace.AuthoringTool;
import com.bbn.vessel.author.workspace.FileManager;
import com.bbn.vessel.author.workspace.Workspace;

public class SlidewareAuthoringTool extends AuthoringTool {

    private final String title;

    private final ImageIcon image;

    public SlidewareAuthoringTool(String title, ImageIcon image, Workspace w) {
        super(w, title, Integer.MAX_VALUE);
        this.title = title;
        this.image = image;
    }

    @Override
    public Component getComponent() {
        return new JLabel(image);
    }

    @Override
    public boolean isModified() {
        return false;
    }

    @Override
    public boolean wantsToLoad(FileManager fm) {
        return false;
    }

    @Override
    public String getName() {
        return title;
    }

    @Override
    public void load(FileManager fm) {
    }

    @Override
    public void save(FileManager fm, boolean autosave) {
    }
}
