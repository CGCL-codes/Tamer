package org.sf.net.sopf.model.core.model;

import java.util.Collection;

/**
 * @author architect
 * Subject to Gnu Public License GPLv3
 * In order for a layer to recieve input,
 * the first population of the layer must implement
 * IReciever.
 * In order for a layer to send output, the last population
 * must implement ISender.
 */
public interface ILayer {

    public Collection<IPopulation<? extends Component>> getPopulace();
}
