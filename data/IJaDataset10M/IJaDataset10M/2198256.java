package com.trohko.jfsim.aircraft.sys.electrical.impl;

import com.trohko.jfsim.aircraft.sys.electrical.ElectricalBus;
import com.trohko.jfsim.aircraft.sys.electrical.ElectricalDrain;
import com.trohko.jfsim.aircraft.sys.electrical.ElectricalSource;
import com.trohko.jfsim.core.AbstractElement;
import java.util.List;

public class ElectricalSystem extends AbstractElement {

    private List<ElectricalSource> sources;

    private List<ElectricalDrain> drains;

    private List<ElectricalBus> buses;

    protected ElectricalSystem(String name) {
        super(name);
    }
}
