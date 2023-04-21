package com.planet_ink.coffee_mud.Abilities.Prayers;

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
public class Prayer_Weather extends Prayer {

    public String ID() {
        return "Prayer_Weather";
    }

    public String name() {
        return "Change Weather";
    }

    protected int canAffectCode() {
        return 0;
    }

    public int classificationCode() {
        return Ability.ACODE_PRAYER | Ability.DOMAIN_CREATION;
    }

    public long flags() {
        return Ability.FLAG_HOLY | Ability.FLAG_UNHOLY;
    }

    protected int canTargetCode() {
        return 0;
    }

    public int abstractQuality() {
        return Ability.QUALITY_INDIFFERENT;
    }

    public boolean invoke(MOB mob, Vector commands, Environmental givenTarget, boolean auto, int asLevel) {
        if (!super.invoke(mob, commands, givenTarget, auto, asLevel)) return false;
        int size = mob.location().getArea().numberOfProperIDedRooms();
        size = size - ((mob.envStats().level() + (2 * super.getXLEVELLevel(mob))) * 20);
        if (size < 0) size = 0;
        boolean success = proficiencyCheck(mob, -size, auto);
        if (success) {
            CMMsg msg = CMClass.getMsg(mob, null, this, verbalCastCode(mob, null, auto), auto ? "The sky changes color!" : "^S<S-NAME> " + prayWord(mob) + " for a change in weather!^?");
            if (mob.location().okMessage(mob, msg)) {
                int switcher = CMLib.dice().roll(1, 3, 0);
                mob.location().send(mob, msg);
                switch(mob.location().getArea().getClimateObj().weatherType(mob.location())) {
                    case Climate.WEATHER_BLIZZARD:
                        if (switcher == 1) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_BLIZZARD); else if (switcher == 2) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_BLIZZARD); else mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_SNOW);
                        break;
                    case Climate.WEATHER_CLEAR:
                        if (switcher == 1) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_WINDY); else if (switcher == 2) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_RAIN); else mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_CLOUDY);
                        break;
                    case Climate.WEATHER_CLOUDY:
                        if (switcher == 1) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_WINDY); else if (switcher == 2) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_RAIN); else mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_CLEAR);
                        break;
                    case Climate.WEATHER_DROUGHT:
                        if (switcher == 1) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_DUSTSTORM); else if (switcher == 2) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_HEAT_WAVE); else mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_CLEAR);
                        break;
                    case Climate.WEATHER_DUSTSTORM:
                        if (switcher == 1) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_DUSTSTORM); else if (switcher == 2) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_CLOUDY); else mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_CLEAR);
                        break;
                    case Climate.WEATHER_HAIL:
                        if (switcher == 1) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_HAIL); else if (switcher == 2) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_SLEET); else mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_CLOUDY);
                        break;
                    case Climate.WEATHER_HEAT_WAVE:
                        if (switcher == 1) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_DUSTSTORM); else if (switcher == 2) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_RAIN); else mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_CLEAR);
                        break;
                    case Climate.WEATHER_RAIN:
                        if (switcher == 1) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_THUNDERSTORM); else if (switcher == 2) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_THUNDERSTORM); else mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_CLOUDY);
                        break;
                    case Climate.WEATHER_SLEET:
                        if (switcher == 1) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_SLEET); else if (switcher == 2) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_SLEET); else mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_CLOUDY);
                        break;
                    case Climate.WEATHER_SNOW:
                        if (switcher == 1) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_BLIZZARD); else if (switcher == 2) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_SLEET); else mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_CLOUDY);
                        break;
                    case Climate.WEATHER_THUNDERSTORM:
                        if (switcher == 1) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_THUNDERSTORM); else if (switcher == 2) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_THUNDERSTORM); else mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_RAIN);
                        break;
                    case Climate.WEATHER_WINDY:
                        if (switcher == 1) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_WINDY); else if (switcher == 2) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_THUNDERSTORM); else mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_CLEAR);
                        break;
                    case Climate.WEATHER_WINTER_COLD:
                        if (switcher == 1) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_WINDY); else if (switcher == 2) mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_SNOW); else mob.location().getArea().getClimateObj().setNextWeatherType(Climate.WEATHER_CLEAR);
                        break;
                    default:
                        break;
                }
                mob.location().getArea().getClimateObj().forceWeatherTick(mob.location().getArea());
            }
        } else beneficialVisualFizzle(mob, null, "<S-NAME> " + prayWord(mob) + ", but nothing happens.");
        return success;
    }
}
