package tm.displayEngine.tmHigraph;

import java.awt.Component;
import tm.backtrack.BTTimeManager;
import higraph.view.ComponentView;
import higraph.view.HigraphView;
import higraph.view.ViewFactory;

public class ViewFactoryTM extends ViewFactory<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM> {

    public ViewFactoryTM(BTTimeManager tm) {
        super(tm);
    }

    @Override
    public HigraphViewTM makeHigraphView(HigraphTM theGraph, Component display) {
        return new HigraphViewTM(this, theGraph, display, timeMan);
    }

    @Override
    public NodeViewTM makeNodeView(HigraphView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM> hgv, NodeTM node) {
        NodeViewTM nv = new NodeViewTM(hgv, node, timeMan);
        HigraphViewTM hgvTM = (HigraphViewTM) hgv;
        LabelTM nameLabel;
        nameLabel = new LabelTM("name", hgvTM.getDefaultNodeNamePosition(), nv, timeMan);
        nameLabel.setColor(hgvTM.getDefaultNodeNameColor());
        nv.addLabel(nameLabel);
        nameLabel.setTheLabel(node.getPayload().getName());
        nameLabel.setShow(hgvTM.getDefaultNodeNameShow());
        LabelTM valueLabel;
        valueLabel = new LabelTM("value", hgvTM.getDefaultNodeValuePosition(), nv, timeMan);
        valueLabel.setColor(hgvTM.getDefaultNodeValueColor());
        nv.addLabel(valueLabel);
        valueLabel.setTheLabel(node.getPayload().getValue());
        valueLabel.setShow(hgvTM.getDefaultNodeValueShow());
        return nv;
    }

    @Override
    public LabelTM makeLabel(ComponentView<NodePayloadTM, EdgePayloadTM, HigraphTM, WholeGraphTM, SubgraphTM, NodeTM, EdgeTM> cv, String id, int position) {
        return new LabelTM(id, position, cv, timeMan);
    }
}
