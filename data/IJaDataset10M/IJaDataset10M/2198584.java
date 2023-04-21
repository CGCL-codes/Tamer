package net.rptools.maptool.client.macro.impl;

import java.util.HashSet;
import java.util.Set;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.MapToolMacroContext;
import net.rptools.maptool.client.macro.Macro;
import net.rptools.maptool.client.macro.MacroContext;
import net.rptools.maptool.client.macro.MacroDefinition;
import net.rptools.maptool.client.ui.token.LightDialog;
import net.rptools.maptool.language.I18N;
import net.rptools.maptool.model.GUID;
import net.rptools.maptool.model.Token;
import net.rptools.maptool.model.Zone;

/**
 * Set the token state on a named macro
 * 
 * @author jgorrell
 * @version $Revision$ $Date$ $Author$
 */
@MacroDefinition(name = "settokenstate", aliases = { "sts" }, description = "settokenstate.desc")
public class SetTokenStateMacro implements Macro {

    /**
	 * The element that contains the token name
	 */
    public static final int TOKEN = 0;

    /**
	 * The element that contains the state name
	 */
    public static final int STATE = 1;

    /**
	 * The element that contains the true/false value
	 */
    public static final int VALUE = 2;

    /**
	 * @see net.rptools.maptool.client.macro.Macro#execute(java.lang.String)
	 */
    public void execute(MacroContext context, String aMacro, MapToolMacroContext executionContext) {
        Set<GUID> selectedTokenSet;
        String stateName;
        String value;
        if (aMacro.length() == 0) {
            MapTool.addLocalMessage(I18N.getText("settokenstate.param"));
            return;
        }
        String[] args = aMacro.trim().split("\\s");
        if (args.length < 2) {
            selectedTokenSet = MapTool.getFrame().getCurrentZoneRenderer().getSelectedTokenSet();
            if (selectedTokenSet.size() == 0) {
                MapTool.addLocalMessage(I18N.getText("settokenstate.param"));
                return;
            }
            stateName = args[0];
            value = null;
        } else {
            Zone zone = MapTool.getFrame().getCurrentZoneRenderer().getZone();
            Token token = zone.getTokenByName(args[0]);
            if (!MapTool.getPlayer().isGM() && (!zone.isTokenVisible(token) || token.getLayer() == Zone.Layer.GM)) {
                token = null;
            }
            if (token == null) {
                selectedTokenSet = MapTool.getFrame().getCurrentZoneRenderer().getSelectedTokenSet();
                if (selectedTokenSet.size() == 0) {
                    MapTool.addLocalMessage(I18N.getText("settokenstate.param"));
                    return;
                } else {
                    stateName = args[0];
                    value = args[1];
                }
            } else {
                selectedTokenSet = new HashSet<GUID>();
                selectedTokenSet.add(token.getId());
                stateName = args[1];
                if (args.length > 2) {
                    value = args[2];
                } else {
                    value = null;
                }
            }
        }
        String state = getState(stateName);
        if (state == null) {
            MapTool.addLocalMessage(I18N.getText("settokenstate.unknownState", stateName));
            return;
        }
        if (MapTool.getCampaign().getTokenStatesMap().containsKey(state)) {
            for (GUID tokenId : selectedTokenSet) {
                Token tok = MapTool.getFrame().getCurrentZoneRenderer().getZone().getToken(tokenId);
                handleBooleanValue(tok, state, value);
            }
        } else if (state.equals("light")) {
            for (GUID tokenId : selectedTokenSet) {
                Token tok = MapTool.getFrame().getCurrentZoneRenderer().getZone().getToken(tokenId);
                LightDialog.show(tok, state);
            }
        }
    }

    /**
	 * Handle setting a boolean value.
	 * 
	 * @param token The token having its state modified
	 * @param state The state being set.
	 * @param value The value to set, or <code>null</code> to toggle value.
	 */
    private void handleBooleanValue(Token token, String state, String value) {
        Object baseValue = token.getState(state);
        assert baseValue == null || baseValue instanceof Boolean : "The current value of token sate '" + state + "' is not a Boolean value but a " + baseValue.getClass().getName();
        Boolean oldValue = (Boolean) baseValue;
        Boolean newValue;
        if (value == null) {
            if (oldValue == null || oldValue.equals(Boolean.FALSE)) {
                newValue = Boolean.TRUE;
            } else {
                newValue = Boolean.FALSE;
            }
        } else {
            newValue = Boolean.valueOf(value);
        }
        token.setState(state, newValue);
        MapTool.serverCommand().putToken(MapTool.getFrame().getCurrentZoneRenderer().getZone().getId(), token);
        if (newValue.booleanValue()) {
            MapTool.addLocalMessage(I18N.getText("settokenstate.marked", token.getName(), state));
        } else {
            MapTool.addLocalMessage(I18N.getText("settokenstate.unmarked", token.getName(), state));
        }
    }

    /**
	 * Find a state name by ignoring case
	 * 
	 * @param state Name entered on command line
	 * @return The valid state name w/ correct case or <code>null</code> if
	 * no state with the passed name could be found.
	 */
    public String getState(String state) {
        if (MapTool.getCampaign().getTokenStatesMap().get(state) != null || "light".equals(state)) return state;
        String newState = null;
        for (String name : MapTool.getCampaign().getTokenStatesMap().keySet()) {
            if (name.equalsIgnoreCase(state)) {
                if (newState != null) {
                    MapTool.addLocalMessage("The name '" + state + "' can be the state " + newState + " or " + name + ".");
                    return null;
                }
                newState = name;
            }
        }
        return newState;
    }
}
