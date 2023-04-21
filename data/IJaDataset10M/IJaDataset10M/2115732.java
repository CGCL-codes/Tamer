package org.jcrpg.world.ai.audio.desc;

import org.jcrpg.world.ai.audio.AudioDescription;

public class MaleEvil1 extends AudioDescription {

    public MaleEvil1() {
        formattedName = "Evil";
        String base = "humanoid/male_evil1/evil-";
        ATTACK = new String[] { base + "attackshout1", base + "attackshout2" };
        PAIN = new String[] { base + "pain1", base + "pain2" };
        JOY = new String[] { base + "healjoy1", base + "healjoy2" };
        DEATH = new String[] { base + "death1", base + "death2" };
        ENCOUNTER = new String[] { base + "greeting1", base + "greeting2" };
        DANGER = new String[] { base + "dangersensing1", base + "dangersensing2" };
        LEVELING = new String[] { base + "leveling1", base + "leveling2" };
        TIRED = new String[] { base + "tired1", base + "tired2" };
        BRUISED = new String[] { base + "needheal1", base + "needheal2" };
        BRUISED_MORALE = new String[] { base + "sad1", base + "sad2" };
        BRUISED_SANITY = new String[] { base + "crazy1", base + "crazy2" };
        BRUISED_STAMINA = new String[] { base + "tired1", base + "tired2" };
        pitchModifier = 0.85f;
    }
}
