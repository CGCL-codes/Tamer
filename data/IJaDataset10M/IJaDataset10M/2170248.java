package org.jcrpg.world.ai.fauna.mammals.fox;

import org.jcrpg.threed.scene.model.Model;
import org.jcrpg.threed.scene.model.moving.MovingModel;
import org.jcrpg.threed.scene.moving.RenderedMovingUnit;
import org.jcrpg.world.ai.abs.behavior.Aggressive;
import org.jcrpg.world.ai.abs.skill.SkillInstance;
import org.jcrpg.world.ai.abs.skill.martial.BiteFight;
import org.jcrpg.world.ai.abs.skill.physical.outdoor.Tracking;
import org.jcrpg.world.ai.audio.AudioDescription;
import org.jcrpg.world.ai.body.MammalBody;
import org.jcrpg.world.ai.fauna.AnimalEntityDescription;
import org.jcrpg.world.ai.fauna.modifier.NormalAnimalFemale;
import org.jcrpg.world.ai.fauna.modifier.NormalAnimalMale;
import org.jcrpg.world.ai.fauna.modifier.WeakAnimalChild;
import org.jcrpg.world.climate.impl.continental.Continental;
import org.jcrpg.world.climate.impl.tropical.Tropical;
import org.jcrpg.world.place.geography.Plain;

public class FoxFamily extends AnimalEntityDescription {

    public static AudioDescription audio = new AudioDescription();

    static {
        audio.ENCOUNTER = new String[] { "redfox_1" };
    }

    public static NormalAnimalMale FOX_TYPE_MALE = new NormalAnimalMale("FOX_MALE", MammalBody.class, audio);

    public static NormalAnimalFemale FOX_TYPE_FEMALE = new NormalAnimalFemale("FOX_FEMALE", MammalBody.class, audio);

    public static WeakAnimalChild FOX_TYPE_CHILD = new WeakAnimalChild("FOX_CHILD", MammalBody.class, audio);

    public static MovingModel fox = new MovingModel("models/fauna/redfox.obj", null, null, null, false);

    public static RenderedMovingUnit fox_unit = new RenderedMovingUnit(new Model[] { fox });

    static {
    }

    @Override
    public String getEntityIconPic() {
        return "fox";
    }

    public FoxFamily() {
        climates.add(Tropical.class);
        climates.add(Continental.class);
        geographies.add(Plain.class);
        startingSkills.add(new SkillInstance(Tracking.class, 20));
        startingSkills.add(new SkillInstance(BiteFight.class, 10));
        behaviors.add(Aggressive.class);
        indoorDweller = true;
        genderType = GENDER_BOTH;
        addGroupingRuleMember(FOX_TYPE_MALE);
        addGroupingRuleMember(FOX_TYPE_FEMALE);
    }
}
