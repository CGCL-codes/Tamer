package visugraph.example;

import java.awt.Color;
import java.awt.FlowLayout;
import java.util.Iterator;
import java.util.Random;
import javax.swing.ButtonGroup;
import javax.swing.JComponent;
import javax.swing.JRadioButton;
import visugraph.algorithm.BFSIterator;
import visugraph.algorithm.DFSIterator;
import visugraph.data.Data;
import visugraph.data.DataUtils;
import visugraph.data.MapData;
import visugraph.generator.ErdosRenyiGenerator;
import visugraph.graph.Edge;
import visugraph.graph.Graph;
import visugraph.graph.Node;
import visugraph.graph.SimpleGraph;
import visugraph.gview.GraphComponent;
import visugraph.gview.GraphScrollPane;
import visugraph.gview.action.HighlightAction;
import visugraph.gview.action.KeyNavigatorAction;
import visugraph.gview.action.MoveViewAction;
import visugraph.gview.event.GraphComponentEvent;
import visugraph.gview.event.NodeAdapter;
import visugraph.gview.renderer.DefaultNodeRenderer;
import visugraph.gview.renderer.LabelPosition;
import visugraph.layout.FastFrLayout;
import visugraph.util.FactoryUtils;

/**
 * Illustre le fonctionnement des itérateurs de parcours en largeur et profondeur.
 */
public class IteratorExample implements Example {

    private GraphScrollPane panel;

    private boolean init;

    public IteratorExample() {
        this.panel = new GraphScrollPane();
    }

    public JComponent getComponent() {
        return this.panel;
    }

    public String getDescription() {
        return "Permet d'afficher l'ordre d'exploration des noeuds d'un graphe selon qu'il soit parcouru " + "en largeur ou en profondeur. L'ordre est affiché en tant que label des différents noeuds.\r\n" + "Cliquez sur un noeud pour afficher son ordre de parcours";
    }

    public String getTitle() {
        return "Parcours de graphes";
    }

    public boolean isInitialized() {
        return this.init;
    }

    public void initialize() {
        if (!this.init) {
            Graph<Node, Edge> graph = this.createGraph();
            GraphComponent<Node, Edge> component = this.createGui(graph);
            this.panel.setGraphComponent(component);
            this.init = true;
        }
    }

    private Graph<Node, Edge> createGraph() {
        Random rnd = new Random(0x07);
        ErdosRenyiGenerator<Node, Edge> generator = new ErdosRenyiGenerator<Node, Edge>(FactoryUtils.listFactory(new SimpleGraph<Node, Edge>(false)), Node.FACTORY, Edge.FACTORY);
        generator.setRandom(rnd);
        generator.setNbNodes(10);
        generator.setNbEdges(15);
        Graph<Node, Edge> graph = generator.generate();
        FastFrLayout<Node, Edge> layout = new FastFrLayout<Node, Edge>(graph, Node.POSITION_DATA);
        layout.setRandom(rnd);
        layout.start();
        return graph;
    }

    private GraphComponent<Node, Edge> createGui(final Graph<Node, Edge> graph) {
        final Data<Node, Integer> iterIndex = new MapData<Node, Integer>(graph.nbNodes());
        final GraphComponent<Node, Edge> component = new GraphComponent<Node, Edge>(graph, Node.POSITION_DATA);
        final JRadioButton dfsButton = new JRadioButton("Parcours en profondeur", true);
        final JRadioButton bfsButton = new JRadioButton("Parcours en largeur", false);
        ButtonGroup group = new ButtonGroup();
        group.add(dfsButton);
        group.add(bfsButton);
        component.addAction(new KeyNavigatorAction<Node, Edge>(component));
        component.addAction(new MoveViewAction<Node, Edge>(component));
        component.addAction(new HighlightAction<Node, Edge>(component));
        DefaultNodeRenderer<Node, Edge> nodeRenderer = new DefaultNodeRenderer<Node, Edge>(component);
        nodeRenderer.setLabelData(iterIndex);
        nodeRenderer.setLabelPositionData(DataUtils.<Node, LabelPosition>constData(LabelPosition.Center));
        nodeRenderer.setLabelPaintData(DataUtils.<Node, Color>constData(Color.WHITE));
        component.setNodeRenderer(nodeRenderer);
        component.setLayout(new FlowLayout(FlowLayout.CENTER));
        component.add(dfsButton);
        component.add(bfsButton);
        component.addNodeListener(new NodeAdapter<Node>() {

            public void nodeClicked(GraphComponentEvent<Node, ?> event) {
                resetDataIter(graph, iterIndex);
                fillDataIter(this.chooseIterator(event.getNode()), iterIndex);
                component.repaint();
            }

            private Iterator<Node> chooseIterator(Node node) {
                return dfsButton.isSelected() ? new DFSIterator<Node, Edge>(graph, node) : new BFSIterator<Node, Edge>(graph, node);
            }
        });
        return component;
    }

    private void resetDataIter(Graph<Node, Edge> graph, Data<Node, Integer> iterIndex) {
        for (Node node : graph) {
            iterIndex.set(node, null);
        }
    }

    private void fillDataIter(Iterator<Node> itNodes, Data<Node, Integer> iterIndex) {
        int curIdx = 0;
        while (itNodes.hasNext()) {
            iterIndex.set(itNodes.next(), curIdx++);
        }
    }
}
