package com.planet_ink.coffee_mud.Abilities.Druid;

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
import java.util.*;

@SuppressWarnings("unchecked")
public class Chant_Grapevine extends Chant {

    public String ID() {
        return "Chant_Grapevine";
    }

    public String name() {
        return "Grapevine";
    }

    public int classificationCode() {
        return Ability.ACODE_CHANT | Ability.DOMAIN_PLANTCONTROL;
    }

    public int abstractQuality() {
        return Ability.QUALITY_INDIFFERENT;
    }

    protected int canAffectCode() {
        return Ability.CAN_MOBS | Ability.CAN_ITEMS;
    }

    protected int canTargetCode() {
        return 0;
    }

    Vector myChants = new Vector();

    public void executeMsg(Environmental myHost, CMMsg msg) {
        super.executeMsg(myHost, msg);
        if ((affected instanceof Item) && (((Item) affected).owner() instanceof Room) && (((Room) ((Item) affected).owner()).isContent((Item) affected)) && (msg.sourceMinor() == CMMsg.TYP_SPEAK) && (invoker != null) && (invoker.location() != ((Room) ((Item) affected).owner())) && (msg.othersMessage() != null)) invoker.executeMsg(invoker, msg);
    }

    public void unInvoke() {
        if ((affected instanceof MOB) && (myChants != null)) {
            Vector V = myChants;
            myChants = null;
            for (int i = 0; i < V.size(); i++) {
                Ability A = (Ability) V.elementAt(i);
                if ((A.affecting() != null) && (A.ID().equals(ID())) && (A.affecting() instanceof Item)) {
                    Item I = (Item) A.affecting();
                    I.delEffect(A);
                }
            }
        }
        super.unInvoke();
    }

    public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel) {
        if ((mob.fetchEffect(ID()) != null) || (mob.fetchEffect("Chant_TapGrapevine") != null)) {
            mob.tell("You are already listening through a grapevine.");
            return false;
        }
        Vector myRooms = Druid_MyPlants.myPlantRooms(mob);
        if ((myRooms == null) || (myRooms.size() == 0)) {
            mob.tell("There doesn't appear to be any of your plants around to listen through.");
            return false;
        }
        Item myPlant = Druid_MyPlants.myPlant(mob.location(), mob, 0);
        if ((!auto) && (myPlant == null)) {
            mob.tell("You must be in the same room as one of your plants to initiate this chant.");
            return false;
        }
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        boolean success = proficiencyCheck(mob, 0, auto);
        if (success) {
            CMMsg msg = CMClass.getMsg(mob, myPlant, this, verbalCastCode(mob, myPlant, auto), auto ? "" : "^S<S-NAME> chant(s) to <T-NAMESELF> and listen(s) carefully to <T-HIM-HER>!^?");
            if (mob.location().okMessage(mob, msg)) {
                mob.location().send(mob, msg);
                myChants = new Vector();
                beneficialAffect(mob, mob, asLevel, 0);
                Chant_Grapevine C = (Chant_Grapevine) mob.fetchEffect(ID());
                if (C == null) return false;
                for (int i = 0; i < myRooms.size(); i++) {
                    Room R = (Room) myRooms.elementAt(i);
                    int ii = 0;
                    myPlant = Druid_MyPlants.myPlant(R, mob, ii);
                    while (myPlant != null) {
                        Ability A = myPlant.fetchEffect(ID());
                        if (A != null) myPlant.delEffect(A);
                        myPlant.addNonUninvokableEffect((Ability) C.copyOf());
                        A = myPlant.fetchEffect(ID());
                        if (A != null) myChants.addElement(A);
                        ii++;
                        myPlant = Druid_MyPlants.myPlant(R, mob, ii);
                    }
                }
                C.myChants = (Vector) myChants.clone();
                myChants = new Vector();
            }
        } else beneficialVisualFizzle(mob, myPlant, "<S-NAME> chant(s) to <T-NAMESELF>, but nothing happens.");
        return success;
    }
}
