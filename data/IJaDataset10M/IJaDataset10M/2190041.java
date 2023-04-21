package com.planet_ink.coffee_mud.Items.Weapons;

import com.planet_ink.coffee_mud.core.interfaces.*;
import com.planet_ink.coffee_mud.core.*;
import com.planet_ink.coffee_mud.Abilities.interfaces.*;
import com.planet_ink.coffee_mud.Areas.interfaces.*;
import com.planet_ink.coffee_mud.Behaviors.interfaces.*;
import com.planet_ink.coffee_mud.CharClasses.interfaces.*;
import com.planet_ink.coffee_mud.Commands.interfaces.*;
import com.planet_ink.coffee_mud.Common.interfaces.*;
import com.planet_ink.coffee_mud.Exits.interfaces.*;
import com.planet_ink.coffee_mud.Items.interfaces.*;
import com.planet_ink.coffee_mud.Locales.interfaces.*;
import com.planet_ink.coffee_mud.MOBS.interfaces.*;
import com.planet_ink.coffee_mud.Races.interfaces.*;

public class FlamingSword extends Longsword {

    public String ID() {
        return "FlamingSword";
    }

    public FlamingSword() {
        super();
        setName("a fancy longsword");
        setDisplayText("a fancy longsword has been dropped on the ground.");
        setDescription("A one-handed sword with a very slight red tinge on the blade.");
        secretIdentity = "A Flaming Sword (Additional fire damage when you strike)";
        baseEnvStats().setAbility(0);
        baseEnvStats().setLevel(1);
        baseEnvStats().setWeight(4);
        baseEnvStats().setAttackAdjustment(0);
        baseEnvStats().setDamage(8);
        baseEnvStats().setDisposition(baseEnvStats().disposition() | EnvStats.IS_LIGHTSOURCE | EnvStats.IS_BONUS);
        baseGoldValue = 2500;
        recoverEnvStats();
        material = RawMaterial.RESOURCE_STEEL;
        weaponType = TYPE_SLASHING;
    }

    public void executeMsg(Environmental myHost, CMMsg msg) {
        super.executeMsg(myHost, msg);
        if ((msg.source().location() != null) && (msg.targetMinor() == CMMsg.TYP_DAMAGE) && ((msg.value()) > 0) && (msg.tool() == this) && (msg.target() instanceof MOB) && (!((MOB) msg.target()).amDead())) {
            CMMsg msg2 = CMClass.getMsg(msg.source(), msg.target(), new FlamingSword(), CMMsg.MSG_OK_ACTION, CMMsg.MSK_MALICIOUS_MOVE | CMMsg.TYP_FIRE, CMMsg.MSG_NOISYMOVEMENT, null);
            if (msg.source().location().okMessage(msg.source(), msg2)) {
                msg.source().location().send(msg.source(), msg2);
                if (msg2.value() <= 0) {
                    int flameDamage = (int) Math.round(Math.random() * 6);
                    flameDamage *= baseEnvStats().level();
                    CMLib.combat().postDamage(msg.source(), (MOB) msg.target(), null, flameDamage, CMMsg.TYP_FIRE, Weapon.TYPE_BURNING, name() + " <DAMAGE> <T-NAME>!");
                }
            }
        }
    }
}
