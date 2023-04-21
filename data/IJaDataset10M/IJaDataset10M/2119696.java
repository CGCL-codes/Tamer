package org.xptools.xpairports.parser;

import org.xptools.xpairports.model.Tower;

public class ToweParserState extends SingleLineParserState {

    private Tower _tower;

    public ToweParserState(Parser parser) {
        super(parser);
    }

    @Override
    void activate() {
        super.activate();
        if (_parent instanceof AirportState) {
            AirportState pstate = (AirportState) _parent;
            _tower = pstate.newTower();
        } else throw new IllegalArgumentException("parent is unexpected type");
    }

    @Override
    protected int getLineCode() {
        return LineCodes.TOWER_LINE_CODE;
    }

    public void setHeight(String height) {
        _tower.setHeight(height);
    }

    public void setPosition(String lat, String longitude) {
        _tower.setPosition(lat, longitude);
    }

    public void setDraw(boolean draw) {
        _tower.setDraw(draw);
    }

    public void setName(String name) {
        _tower.setName(name);
    }
}
