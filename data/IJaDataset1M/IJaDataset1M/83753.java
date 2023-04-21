package org.jcrpg.world.ai.humanoid.group.human.member;

import org.jcrpg.world.ai.EntityDescription;
import org.jcrpg.world.ai.audio.AudioDescription;

public class HumanFemaleHousewife extends HumanBaseMember {

    public HumanFemaleHousewife(String visibleTypeId, AudioDescription audioDescription) {
        super(visibleTypeId, audioDescription);
        genderType = EntityDescription.GENDER_FEMALE;
    }
}
