package org.foafrealm.mfb.mediator.predefined;

import java.util.Collection;
import org.foafrealm.mfb.mediator.EnhanceCache;
import org.foafrealm.mfb.mediator.PredefinedEnhancer;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;

/**
 * This enhancer is used to make use of the SSCF information 
 * 
 * 
 * @author skruk
 *
 */
public class SscfEnhancer extends PredefinedEnhancer implements PredefinedRdfEnhancer {

    /**
	 * 
	 */
    public SscfEnhancer() {
        super();
    }

    public Model enhance(Model inputModel, Model outputModel, Collection<Property> properties, EnhanceCache enhanceCache, String currentProperty, String color) {
        return null;
    }
}
