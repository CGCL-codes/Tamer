package rollmadness.logic.triggers;

import com.jme3.audio.AudioNode;
import com.jme3.audio.AudioRenderer;
import com.jme3.scene.Spatial;
import jme3clogic.Trigger;
import jme3clogic.basic.Threshold;
import jme3clogic.basic.conditions.SpatialsAreNear;
import jme3clogic.basic.reactions.DetachSpatial;
import jme3clogic.basic.reactions.IncreaseNumericField;
import jme3clogic.basic.reactions.PlaySound;
import jme3clogic.basic.reactions.RemoveTrigger;
import rollmadness.stages.PlayerHud;

public class PickUpAmmo extends Trigger {

    public PickUpAmmo(Spatial player, Spatial ammo, PlayerHud hud, AudioRenderer ren, AudioNode sound) {
        condition = new SpatialsAreNear(player, ammo, new Threshold(4f));
        reaction = new PlaySound(sound, ren).and(new RemoveTrigger(this)).and(new DetachSpatial(ammo)).and(new IncreaseNumericField(hud, PlayerHud.AMMO, 10));
    }
}
