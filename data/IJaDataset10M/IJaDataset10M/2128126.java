package ch.jester.common.components;

import ch.jester.commonservices.api.logging.ILoggerFactory;

/**
 * Component mit DependencyInjection für eine ILoggerFactory.<br>
 * bindLoggerFactory bindet nicht die Factory per se, sondern holt sich einfach nur einen Logger
 * 
 * @param <T>
 */
public class InjectedLogFactoryComponentAdapter<T> extends ComponentAdapter<T> {

    /**ILoggerFactory binding
	 * @param eine LoggerFactory pFactory
	 */
    public void bindLoggerFactory(ILoggerFactory pFactory) {
        setLogger(pFactory.getLogger(InjectedLogFactoryComponentAdapter.this.getClass()));
        getLogger().info(getStartMessage());
    }

    /**Unbinding.
	 * Hier geschieht nichts. Wir wollen der Klasse ja nicht ihren Logger klauen.
	 * @param eine LoggerFactory pFactory
	 */
    public void unbindLoggerFactory(ILoggerFactory pFactory) {
    }

    /**
	 * Eine vordefinierte Start Message
	 * @return die Message
	 */
    public String getStartMessage() {
        return "*** Component " + InjectedLogFactoryComponentAdapter.this.getClass().getSimpleName() + " started";
    }
}
