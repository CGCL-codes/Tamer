package com.justinrmiller.rpg;

import com.justinrmiller.rpg.ui.*;
import com.justinrmiller.rpg.model.*;
import com.google.inject.AbstractModule;

public class BaseModule extends AbstractModule {

    public void configure() {
        bind(UserInterface.class).to(SnazzyUI.class);
        bind(World.class).to(SimpleWorld.class);
    }
}
