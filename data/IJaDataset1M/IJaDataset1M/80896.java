package net.rptools.maptool.client.macro.impl;

import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.language.I18N;

public abstract class AbstractRollMacro extends AbstractMacro {

    protected String roll(String roll) {
        try {
            String text = roll + " => " + MapTool.getParser().expandRoll(roll);
            return text;
        } catch (Exception e) {
            MapTool.addLocalMessage("<b>" + I18N.getText("roll.general.unknown", roll) + "</b>");
            return null;
        }
    }
}
