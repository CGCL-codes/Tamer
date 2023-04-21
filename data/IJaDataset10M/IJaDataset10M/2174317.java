package de.simpleworks.jmeter.visualizer.rstatd.graph.configuration;

import java.awt.Color;
import de.simpleworks.jmeter.visualizer.rstatd.graph.state.StateCpu;

/**
 * @author Marcin Brzoza
 * @since Mar 15, 2008
 */
public class CpuStateGetterIdle implements ICpuStateGetter {

    public CpuStateGetterIdle() {
        super();
    }

    public Color getColor(final CpuStateColors _cpuStateColors) {
        return _cpuStateColors.getColorCpuIdle();
    }

    public int get(final StateCpu _state) {
        return _state.getIdle();
    }
}
