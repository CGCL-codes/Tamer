package com.siemens.ct.exi.grammar;

import com.siemens.ct.exi.grammar.event.Event;
import com.siemens.ct.exi.grammar.rule.Rule;

/**
 * 
 * @author Daniel.Peintner.EXT@siemens.com
 * @author Joerg.Heuer@siemens.com
 * 
 * @version 0.8
 */
public class SchemaInformedEventInformation extends EventInformation {

    private static final long serialVersionUID = -7301991368895524208L;

    public SchemaInformedEventInformation(Rule next, Event event, int eventCode) {
        super(next, event, eventCode);
    }

    @Override
    public int getEventCode() {
        return eventCode;
    }
}
