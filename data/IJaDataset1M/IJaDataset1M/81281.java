package de.icehorsetools.iceoffice.plugin.participant;

import org.ugat.wiser.componentHandler.*;
import org.ugat.wiser.language.Lang;
import org.ugat.wiser.plugin.AUnPluginHandler;

public class ParticipantPl extends AUnPluginHandler {

    public void setComponents() {
        this.addComponentHandler(new UnObjLookupHandler("person", this.findComponentByName("oPerson"), AUnComponentHandler.NULLABLE, "tPersonSelect", Lang.deref("%#LANG_participant", "person_lookup"), 600, 450));
        this.addComponentHandler(new UnInputTextHandler("personId", this.findComponentByName("iPersonId"), 0, 12));
        this.addComponentHandler(new UnObjLookupHandler("horse", this.findComponentByName("oHorse"), AUnComponentHandler.NULLABLE, "tHorseSelect", Lang.deref("%#LANG_participant", "horse_lookup"), 600, 450));
        this.addComponentHandler(new UnInputTextHandler("horseId", this.findComponentByName("iHorseId"), 0, 12));
        this.addComponentHandler(new UnInputTextHandler("clazz", this.findComponentByName("iClazz"), 0, 50));
        this.addComponentHandler(new UnInputTextHandler("club", this.findComponentByName("iClub"), 0, 100));
        this.addComponentHandler(new UnTextBoxHandler("comments", this.findComponentByName("tbComments"), AUnComponentHandler.NULLABLE));
        this.addComponentHandler(new UnInputTextHandler("sta", this.findComponentByName("iSta"), 0, 3));
        this.addComponentHandler(new UnInputTextHandler("stable", this.findComponentByName("iStable"), 0, 30));
        this.addComponentHandler(new UnInputIntHandler("status", this.findComponentByName("iStatus"), AUnComponentHandler.NULLABLE));
        this.addComponentHandler(new UnInputTextHandler("team", this.findComponentByName("iTeam"), 0, 100));
        this.addComponentHandler(new UnInputDoubleHandler("nenngeld", this.findComponentByName("iNenngeld"), AUnComponentHandler.NULLABLE));
        this.addComponentHandler(new UnInputDoubleHandler("startgeld", this.findComponentByName("iStartgeld"), AUnComponentHandler.NULLABLE));
        this.addComponentHandler(new UnInputDoubleHandler("stallgeld", this.findComponentByName("iStallgeld"), AUnComponentHandler.NULLABLE));
        this.addComponentHandler(new UnInputDoubleHandler("helferfonds", this.findComponentByName("iHelferfonds"), AUnComponentHandler.NULLABLE));
        this.addComponentHandler(new UnInputDoubleHandler("programmheft", this.findComponentByName("iProgrammheft"), AUnComponentHandler.NULLABLE));
        this.addComponentHandler(new UnInputDoubleHandler("sonstiges", this.findComponentByName("iSonstiges"), AUnComponentHandler.NULLABLE));
        this.addComponentHandler(new UnInputDoubleHandler("extra", this.findComponentByName("iExtra"), AUnComponentHandler.NULLABLE));
        this.addComponentHandler(new UnInputDoubleHandler("summe", this.findComponentByName("iSumme"), AUnComponentHandler.NULLABLE));
        this.addComponentHandler(new UnInputDoubleHandler("perscheck", this.findComponentByName("iPerscheck"), AUnComponentHandler.NULLABLE));
        this.addComponentHandler(new UnInputDoubleHandler("perbar", this.findComponentByName("iPerbar"), AUnComponentHandler.NULLABLE));
        this.addComponentHandler(new UnInputDoubleHandler("perueberweisung", this.findComponentByName("iPerueberweisung"), AUnComponentHandler.NULLABLE));
        this.addComponentHandler(new UnInputDoubleHandler("rueckerstattet", this.findComponentByName("iRueckerstattet"), AUnComponentHandler.NULLABLE));
        this.addComponentHandler(new UnInputTextHandler("scheknr", this.findComponentByName("iScheknr"), 0, 50));
        this.addComponentHandler(new UnInputTextHandler("extras", this.findComponentByName("iExtras"), 0, 50));
    }
}
