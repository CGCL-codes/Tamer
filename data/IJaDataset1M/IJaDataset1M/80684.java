package prisms.arch.event;

/**
 * A {@link PrismsEventListener} that can be configured using {@link prisms.arch.AppConfig}. An
 * event listener that is configured from XML must generally be told what session it is in since the
 * {@link PrismsEventListener} interface assumes that it already knows.
 */
public interface ConfiguredPEL extends PrismsEventListener {

    /**
	 * Sets this listener's session so it can perform its action properly
	 * 
	 * @param session The session that this listener is listening to
	 * @param config The configuration to configure this event listener
	 */
    void configure(prisms.arch.PrismsSession session, prisms.arch.PrismsConfig config);
}
