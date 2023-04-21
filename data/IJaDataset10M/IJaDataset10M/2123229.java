package org.linkedgeodata.osm.mapping;

import java.util.Collection;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import com.hp.hpl.jena.rdf.model.Model;

/**
 * Interface for mapping tags to triples.
 * 
 * @author raven
 *
 */
public interface ITagMapper {

    /**
	 * FIXME This method might be only a convenience method, which uses
	 * the lookup method. In that case it shouldn't be part of the interface
	 * but rather a static utility function.
	 * 
	 * 
	 * @param subject
	 * @param tag
	 * @param model
	 * @return
	 */
    Model map(String subject, Tag tag, Model model);

    Collection<? extends IOneOneTagMapper> lookup(String k, String v);

    Collection<? extends IOneOneTagMapper> getAllMappers();
}
