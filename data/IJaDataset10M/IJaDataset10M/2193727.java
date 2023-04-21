package com.trohko.jfsim.aircraft.sys.electrical;

import com.trohko.jfsim.aircraft.sys.electrical.impl.Battery;

public class BatteryC172 extends Battery {

    protected BatteryC172() {
        super("C172 battery", 12, 20, 25);
    }
}
