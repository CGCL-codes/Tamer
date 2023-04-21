package org.kalypso.nofdpidss.hydraulic.computation.view.diagram.chart;

import java.util.ArrayList;
import java.util.List;
import org.kalypso.nofdpidss.core.base.flow.network.implementation.PolderNodeHandler;

public class PolderNodeDiagramWrapper implements IPolderNodeDiagramWrapper {

    List<IPolderNodeDiagramVariant> m_variants = new ArrayList<IPolderNodeDiagramVariant>();

    private final PolderNodeHandler m_node;

    public PolderNodeDiagramWrapper(final PolderNodeHandler node) {
        m_node = node;
    }

    public void addVariant(final IPolderNodeDiagramVariant variant) {
        m_variants.add(variant);
    }

    public IPolderNodeDiagramVariant[] getVariants() {
        return m_variants.toArray(new IPolderNodeDiagramVariant[] {});
    }

    public void addVariant(final IDiagramVariant cv) {
        addVariant((IPolderNodeDiagramVariant) cv);
    }

    public String getNodeName() {
        return m_node.getName();
    }
}
