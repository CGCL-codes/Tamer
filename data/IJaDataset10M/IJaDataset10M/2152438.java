package org.foafrealm.mfb.logic;

import java.util.Map;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * @author skruk
 *
 */
public class FacetValue extends Facet {

    protected Facet superFacet = null;

    /**
	 * 
	 */
    public FacetValue(Resource _class, Facet _facet, Facet _super) {
        super(_class, _facet.getMfbContext(), _super);
        this.superFacet = _facet;
        this.visible = false;
    }

    @Override
    public String toHTML(StringBuilder sb, Map<Facet, FacetValue> selection) {
        return this.toHTMLValues(sb, selection);
    }

    @Override
    public String getColor() {
        return this.getFacet().getColor();
    }

    @Override
    public Facet getFacet() {
        if (this.facet == null) {
            this.facet = this.superFacet.getFacet();
        }
        return this.facet;
    }

    public boolean isMorePrecise(FacetValue fv) {
        if (this.equals(fv)) {
            return true;
        }
        if ((this.superFacet != null && this.superFacet.equals(fv)) || this.getFacet().equals(fv)) {
            return false;
        }
        boolean result = false;
        for (FacetValue subfv : this.taxonomy) {
            result |= subfv.isMorePrecise(fv);
            if (result) {
                break;
            }
        }
        return result;
    }

    public boolean isLessPrecise(FacetValue fv) {
        if (this.equals(fv)) {
            return true;
        }
        if (this.superFacet == null || !(this.superFacet instanceof FacetValue)) {
            return false;
        }
        return ((FacetValue) this.superFacet).isLessPrecise(fv);
    }
}
