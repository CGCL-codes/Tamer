package net.rptools.maptool.client.functions;

import java.awt.Dimension;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import net.rptools.maptool.client.MapTool;
import net.rptools.maptool.client.MapToolUtil;
import net.rptools.maptool.client.MapToolVariableResolver;
import net.rptools.maptool.client.ui.zone.ZoneRenderer;
import net.rptools.maptool.language.I18N;
import net.rptools.maptool.model.GUID;
import net.rptools.maptool.model.Grid;
import net.rptools.maptool.model.Token;
import net.rptools.maptool.model.TokenFootprint;
import net.rptools.maptool.model.Zone;
import net.rptools.parser.Parser;
import net.rptools.parser.ParserException;
import net.rptools.parser.function.AbstractFunction;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class TokenCopyDeleteFunctions extends AbstractFunction {

    private static final TokenCopyDeleteFunctions instance = new TokenCopyDeleteFunctions();

    private TokenCopyDeleteFunctions() {
        super(1, 4, "copyToken", "removeToken");
    }

    public static TokenCopyDeleteFunctions getInstance() {
        return instance;
    }

    @Override
    public Object childEvaluate(Parser parser, String functionName, List<Object> parameters) throws ParserException {
        if (!MapTool.getParser().isMacroTrusted()) {
            throw new ParserException(I18N.getText("macro.function.general.noPerm", functionName));
        }
        MapToolVariableResolver res = (MapToolVariableResolver) parser.getVariableResolver();
        if (functionName.equals("copyToken")) {
            return copyTokens(res, parameters);
        }
        if (functionName.equals("removeToken")) {
            return deleteToken(res, parameters);
        }
        throw new ParserException(I18N.getText("macro.function.general.unknownFunction", functionName));
    }

    private String deleteToken(MapToolVariableResolver res, List<Object> parameters) throws ParserException {
        Token token = FindTokenFunctions.findToken(parameters.get(0).toString(), null);
        if (token == null) {
            throw new ParserException("Can not find token " + parameters.get(0));
        }
        Zone zone = MapTool.getFrame().getCurrentZoneRenderer().getZone();
        MapTool.serverCommand().removeToken(zone.getId(), token.getId());
        return "Deleted token " + token.getId() + " (" + token.getName() + ")";
    }

    private Object copyTokens(MapToolVariableResolver res, List<Object> param) throws ParserException {
        if (param.size() < 1) {
            throw new ParserException(I18N.getText("macro.function.general.argumentTypeT", "copyToken", 1));
        }
        String zoneName = null;
        if (param.size() > 2) {
            zoneName = param.get(2).toString();
        }
        Token token = FindTokenFunctions.findToken(param.get(0).toString(), zoneName);
        if (token == null) {
            throw new ParserException(I18N.getText("macro.function.general.unknownToken", "copyToken", param.get(0)));
        }
        int numberCopies = 1;
        if (param.size() > 1) {
            if (!(param.get(1) instanceof BigDecimal)) {
                throw new ParserException(I18N.getText("macro.function.general.argumentTypeI", "copyToken", 2));
            }
            numberCopies = ((BigDecimal) param.get(1)).intValue();
        }
        JSONObject newVals = null;
        if (param.size() > 3) {
            Object o = JSONMacroFunctions.asJSON(param.get(3));
            if (!(o instanceof JSONObject)) {
                throw new ParserException(I18N.getText("macro.function.general.argumentTypeO", "copyToken", 3));
            }
            newVals = (JSONObject) o;
        }
        Zone zone = MapTool.getFrame().getCurrentZoneRenderer().getZone();
        List<String> newTokens = new ArrayList<String>(numberCopies);
        for (int i = 0; i < numberCopies; i++) {
            Token t = new Token(token);
            zone.putToken(t);
            setTokenValues(t, newVals, zone, res);
            MapTool.serverCommand().putToken(zone.getId(), t);
            newTokens.add(t.getId().toString());
        }
        MapTool.getFrame().getCurrentZoneRenderer().flushLight();
        if (numberCopies == 1) {
            return newTokens.get(0).toString();
        } else {
            return JSONArray.fromObject(newTokens);
        }
    }

    private void setTokenValues(Token token, JSONObject vals, Zone zone, MapToolVariableResolver res) throws ParserException {
        JSONObject newVals = JSONObject.fromObject(vals);
        newVals = (JSONObject) JSONMacroFunctions.getInstance().JSONEvaluate(res, newVals);
        if (newVals.containsKey("name")) {
            if (newVals.getString("name").equals("")) {
                throw new ParserException(I18N.getText("macro.function.tokenName.emptyTokenNameForbidden", "copyToken"));
            }
            token.setName(newVals.getString("name"));
        } else {
            if (token.getType() != Token.Type.PC) {
                token.setName(MapToolUtil.nextTokenId(zone, token));
            }
        }
        if (newVals.containsKey("label")) {
            token.setLabel(newVals.getString("label"));
        }
        if (newVals.containsKey("gmName")) {
            token.setGMName(newVals.getString("gmName"));
        }
        if (newVals.containsKey("layer")) {
            TokenPropertyFunctions.getInstance().setLayer(token, newVals.getString("layer"));
        }
        int x = token.getX();
        int y = token.getY();
        boolean tokenMoved = false;
        boolean useDistance = true;
        if (newVals.containsKey("x")) {
            x = newVals.getInt("x");
            tokenMoved = true;
        }
        if (newVals.containsKey("y")) {
            y = newVals.getInt("y");
            tokenMoved = true;
        }
        if (newVals.containsKey("useDistance")) {
            if (newVals.getInt("useDistance") == 0) {
                useDistance = false;
            }
        }
        if (tokenMoved) {
            TokenLocationFunctions.getInstance().moveToken(token, x, y, useDistance);
        }
        if (newVals.containsKey("facing")) {
            token.setFacing(newVals.getInt("facing"));
            MapTool.getFrame().getCurrentZoneRenderer().flushLight();
        }
        if (newVals.containsKey("size")) {
            String size = newVals.getString("size");
            ZoneRenderer renderer = MapTool.getFrame().getCurrentZoneRenderer();
            Grid grid = renderer.getZone().getGrid();
            for (TokenFootprint footprint : grid.getFootprints()) {
                if (token.isSnapToScale() && footprint.getName().equalsIgnoreCase(size)) {
                    token.setFootprint(grid, footprint);
                    token.setSnapToScale(true);
                }
            }
        }
    }
}
