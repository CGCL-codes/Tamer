package edu.uah.eduardomoriana.qaopmvc.patterns.creation.abstractfactory;

import edu.uah.eduardomoriana.qaopmvc.patterns.creation.builder.OldComputerDirector;

public abstract class OldComputerBuilderAbstractFactory {

    public static OldComputerBuilderAbstractFactory getFactory() {
        return new SpectrumConcreteFactory();
    }

    public abstract OldComputerDirector getComputerDirector();
}
