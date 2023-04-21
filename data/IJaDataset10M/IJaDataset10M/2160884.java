package com.mattpatey.modularsynpat.modules;

import com.mattpatey.modularsynpat.modules.components.JackListener;
import com.mattpatey.modularsynpat.modules.components.ModuleComponent;
import processing.core.PApplet;

public interface Module {

    public void addComponent(final ModuleComponent component);

    public void draw(PApplet p);

    public void addJackListener(JackListener l);

    public int getX();

    public int getY();
}
