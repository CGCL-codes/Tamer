package net.sourceforge.jabm.init;

import net.sourceforge.jabm.Simulation;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public interface SimulationInitialiser {

    public void initialise(ConfigurableListableBeanFactory beanFactory, Simulation simulation);
}
