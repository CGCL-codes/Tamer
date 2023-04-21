package org.unicolet.axl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created:
 * User: unicoletti
 * Date: 12:14:08 PM Oct 24, 2005
 */
public class ValueMapRenderer {

    Log log = LogFactory.getLog(getClass());

    public ValueMapRenderer() {
        ranges = new ArrayList();
        exacts = new ArrayList();
        others = new ArrayList();
    }

    private String lookupfield;

    private List ranges;

    private List exacts;

    private List others;

    private Layer layer;

    public Layer getLayer() {
        return layer;
    }

    public void setLayer(Layer layer) {
        this.layer = layer;
        if (!ranges.isEmpty()) {
            for (Iterator i = ranges.iterator(); i.hasNext(); ) {
                ((Range) i.next()).setLayer(layer);
            }
        }
        if (!exacts.isEmpty()) {
            for (Iterator i = exacts.iterator(); i.hasNext(); ) {
                ((Exact) i.next()).setLayer(layer);
            }
        }
        if (!others.isEmpty()) {
            for (Iterator i = others.iterator(); i.hasNext(); ) {
                ((Other) i.next()).setLayer(layer);
            }
        }
    }

    public String getLookupfield() {
        return lookupfield;
    }

    public void setLookupfield(String lookupfield) {
        this.lookupfield = lookupfield;
    }

    public List getRanges() {
        return ranges;
    }

    public void setRanges(List ranges) {
        this.ranges = ranges;
    }

    public void addRange(Range r) {
        r.setLayer(getLayer());
        ranges.add(r);
    }

    public List getExacts() {
        return exacts;
    }

    public void setExacts(List exacts) {
        this.exacts = exacts;
    }

    public List getOthers() {
        return others;
    }

    public void setOthers(List others) {
        this.others = others;
    }

    public void addOther(Other o) {
        o.setLayer(getLayer());
        others.add(o);
    }

    public void addExact(Exact e) {
        e.setLayer(getLayer());
        exacts.add(e);
    }

    public void merge(ValueMapRenderer vmr) {
        mergeSymbolsCollection(exacts, vmr.getExacts());
        mergeSymbolsCollection(ranges, vmr.getRanges());
        mergeSymbolsCollection(others, vmr.getOthers());
    }

    protected void mergeSymbolsCollection(List vmos, List vmo2add) {
        if (!vmos.isEmpty()) {
            ValueMapOperator vmo = null;
            for (int i = 0; i < vmo2add.size(); i++) {
                vmo = (ValueMapOperator) vmo2add.get(i);
                log.debug("Round " + i + ", considering vmo=" + vmo);
                if (vmos.contains(vmo)) {
                    ((ValueMapOperator) vmos.get(vmos.indexOf(vmo))).symbols.addAll(vmo.symbols);
                } else {
                    vmos.add(vmo);
                }
            }
        }
    }
}
