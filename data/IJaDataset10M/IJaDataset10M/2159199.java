package org.kalypso.nofdpidss.hydraulic.computation.view.diagram.chart;

import java.util.ArrayList;
import java.util.List;

public class CsnDiagramWrapper implements ICsnDiagramWrapper {

    List<ICsnDiagramVariant> m_variants = new ArrayList<ICsnDiagramVariant>();

    private final String m_node;

    public CsnDiagramWrapper(final String node) {
        m_node = node;
    }

    public void addVariant(final ICsnDiagramVariant variant) {
        m_variants.add(variant);
    }

    public ICsnDiagramVariant[] getVariants() {
        return m_variants.toArray(new ICsnDiagramVariant[] {});
    }

    public void addVariant(final IDiagramVariant cv) {
        addVariant((ICsnDiagramVariant) cv);
    }

    public String getNodeName() {
        return m_node;
    }
}
