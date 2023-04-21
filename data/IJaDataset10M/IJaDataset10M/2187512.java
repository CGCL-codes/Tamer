package jmcf.core;

/**
 *
 * @author Mauro Dragone
 */
public interface IBindingEventSource extends IEventSource {

    IBindingEventListener getSource();
}
