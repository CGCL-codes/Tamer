package net.sourceforge.traffiscope.entity.jpa;

public class ToplinkSessionBeanFactory extends JPASessionBeanFactory {

    private static final String PERSISTENCE_UNIT = "traffiscopeDerbyToplink";

    public ToplinkSessionBeanFactory() {
        super(PERSISTENCE_UNIT);
    }
}
