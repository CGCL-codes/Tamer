package org.xptools.xpairports.parser;

import org.xptools.xpairports.model.Ramp;

public class RampParserState extends SingleLineParserState {

    private Ramp _ramp;

    public RampParserState(Parser parser) {
        super(parser);
    }

    @Override
    void activate() {
        super.activate();
        if (_parent instanceof AirportState) {
            AirportState pstate = (AirportState) _parent;
            _ramp = pstate.newRamp();
        } else throw new IllegalArgumentException("parent is unexpected type");
    }

    @Override
    protected int getLineCode() {
        return LineCodes.RAMP_LINE_CODE;
    }

    public void setPosition(String lat, String longitude) {
        _ramp.setPosition(lat, longitude);
    }

    public void setHeading(String heading) {
        _ramp.setHeading(heading);
    }

    public void setName(String name) {
        _ramp.setName(name);
    }
}
