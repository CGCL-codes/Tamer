package edu.uiuc.ncsa.security.core.util;

import edu.uiuc.ncsa.security.core.Identifiable;
import edu.uiuc.ncsa.security.core.Identifier;
import javax.inject.Provider;

/**
 * Use this to create various identifiable things, i.e., objects that have globally unique
 * ids.
 * <p>Created by Jeff Gaynor<br>
 * on 4/3/12 at  2:53 PM
 */
public abstract class IdentifiableProvider<V extends Identifiable> implements Provider<V> {

    protected Provider<Identifier> idProvider;

    protected IdentifiableProvider(Provider<Identifier> idProvider) {
        this.idProvider = idProvider;
    }
}
