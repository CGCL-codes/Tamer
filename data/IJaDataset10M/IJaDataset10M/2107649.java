package uk.org.toot.synth.modules.mixer;

public interface ModulationMixerVariables {

    int getCount();

    float getDepth(int n);

    float[] getDepths();
}
