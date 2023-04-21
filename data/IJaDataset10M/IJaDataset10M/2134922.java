package com.jpmorrsn.fbp.test.networks;

import com.jpmorrsn.fbp.engine.Network;

public class TestRhythm extends Network {

    String description = "Still playing with this! ";

    @Override
    protected void define() {
        component("Rhythm1", com.jpmorrsn.fbp.test.components.Rhythm1.class);
        component("Rhythm2", com.jpmorrsn.fbp.test.components.Rhythm2.class);
        component("SoundMixer", com.jpmorrsn.fbp.components.SoundMixer.class);
        connect(component("Rhythm1"), port("OUT"), "SoundMixer.IN");
        initialize("4095", component("Rhythm1"), port("RANGE"));
        connect(component("Rhythm2"), port("OUT"), "SoundMixer.IN[1]");
        initialize("4095", component("Rhythm2"), port("RANGE"));
    }

    public static void main(final String[] argv) throws Exception {
        new TestRhythm().go();
    }
}
