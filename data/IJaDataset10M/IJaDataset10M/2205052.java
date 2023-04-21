package pl.conx.action.chargen;

import webX.*;
import org.jdom.Element;
import pl.conx.*;
import java.util.Iterator;

public class AddSubskill extends Action {

    public Element execute() {
        webX.Session s = (webX.Session) getParent();
        s.setView("chargen/Skills");
        Skill skill = (Skill) (webX.loader.XmlLoader.get("hermes/skills").getChild("Skill[@id='" + getValue("skill/@id") + "']")).clone();
        skill.setValue("subskill", getValue("skill"));
        s.getChild("Player/Skills").addChild(skill);
        skill.setValue("1");
        ((Player) s.getChild("Player")).save();
        return s;
    }
}
