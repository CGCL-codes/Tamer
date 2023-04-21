package com.bluebrim.base.shared;

/**
 * NOTE: This interface could be called CoDomainContext. It should however
 * reside in the "base" package.
 *
 * General context interface for additional state that cannot be obtained
 * from a domain object. UI:s requiring such state should implement
 * CoContextAcceptingUI and also have a constructor with the parameters
 * (CoObjectIF, CoUIContext) in that order.
 *
 * This interface will most often be implemented by a subclass to
 * CoDomainUserInterface for use by it's sub-UI:s. In some other cases,
 * most notably when creating bookmarks, it may be necessary to create
 * an instance of a CoGenericUIContext or similar.
 *
 * It is not recommended that any reference to a CoUIContext implementor be
 * cast to (or typed to) anything other than CoUIContext when used to obtain
 * said state. Doing so would prevent the ability to replace the context with
 * any other implementation such as when creating/opening a bookmark. Note
 * that this may in very rare circumstances require a UI to hold two differently
 * typed references that may refer to the same object.
 *
 * Typical usage for UI:s will be to read from the context when receiving a new
 * domain object and if neccessary storing the result in internal variables.
 * Although it is permissable to read from the context during normal operation of
 * the UI, reading the context as part of cleaning up from an old domain object
 * (before receiving a new domain object or null) is discouraged. This is because
 * the context then may contain state for the next domain object, but this is not
 * guaranteed. It is better then (as metioned above) to store state required at
 * this point in internal variables.
 *
 * @author Markus Persson 2000-10-02
 */
public interface CoUIContext {

    String INDEXED_ORDER = "indexed_order";

    String FILTER_UI = "filter_ui";

    /**
 * Returns the state given by this context for the given key.
 *
 * The key used is typically the factory key or any similar key
 * that is suitably <b>well defined</b>. Note that many factory
 * keys, being factory keys, do not represent an abstract super-
 * class but a concrete subclass to narrow to be useful here.
 *
 * For such cases or if the class itself is so generic that it
 * doesn't specify the usage well enough (like String) and if
 * there is no reason for such a key other then to be used here,
 * the key could be defined here in this interface.
 */
    public Object getStateFor(String key);
}
