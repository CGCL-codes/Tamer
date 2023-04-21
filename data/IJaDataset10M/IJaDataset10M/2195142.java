package net.sourceforge.fluxion.pussycat.renderlets.impl;

import net.sourceforge.fluxion.pussycat.manager.PussycatRenderletManager;
import net.sourceforge.fluxion.pussycat.renderlets.AbstractRenderletProvider;
import net.sourceforge.fluxion.pussycat.renderlets.Renderlet;
import net.sourceforge.fluxion.pussycat.renderlets.RenderletManager;
import org.semanticweb.owl.model.OWLOntology;
import net.sourceforge.fluxion.spi.ServiceProvider;

/**
 * Makes OntologySummaryRenderlet discoverable by SPI. getRequiredClass() should return the
 * Class of the renderedObject type on the Renderlet.  makeRenderlet() should
 * instantiate a new OntologySummaryRenderlet and return it.
 */
@ServiceProvider
public class OntologySummaryRenderletProvider extends AbstractRenderletProvider {

    protected Class<?> getRequiredClass() {
        return OWLOntology.class;
    }

    protected Renderlet makeRenderlet(RenderletManager rm) {
        if (rm instanceof PussycatRenderletManager) {
            OntologySummaryRenderlet r = new OntologySummaryRenderlet();
            r.registerRenderletManager((PussycatRenderletManager) rm);
            return r;
        } else {
            return null;
        }
    }
}
