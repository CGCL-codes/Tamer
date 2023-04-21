package it.unisannio.rcost.callgraphanalyzer.util;

import it.unisannio.rcost.callgraphanalyzer.Aspect;
import it.unisannio.rcost.callgraphanalyzer.Interface;
import it.unisannio.rcost.callgraphanalyzer.Node;
import it.unisannio.rcost.callgraphanalyzer.NodeLeaf;
import it.unisannio.rcost.callgraphanalyzer.Package;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.jface.viewers.ISelection;

public class Criteria implements ISelection {

    public enum GraphType {

        FULL_PACKAGE, FULL_CLASS, FULL_METHOD, PARTIAL_PACKAGE, PARTIAL_CLASS, PARTIAL_METHOD
    }

    public enum PathOptions {

        ALL_PATH, ONLY_OBJECT, ONLY_ASPECT
    }

    private String projectName = null;

    private GraphType graphType = GraphType.FULL_PACKAGE;

    private NodeLeaf[] sources = null;

    private Node target = null;

    private Node[] lstIntermediary = null;

    private boolean isOrdered = false;

    private PathOptions pathOptions = PathOptions.ALL_PATH;

    private Node selectedSource = null;

    private boolean isEmpty = true;

    public GraphType getGraphType() {
        return graphType;
    }

    public void setGraphType(GraphType graphType) {
        this.graphType = graphType;
    }

    public boolean isOrdered() {
        return isOrdered;
    }

    public void setOrdered(boolean isOrdered) {
        this.isOrdered = isOrdered;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
	 * 
	 * @return Nodo sorgente
	 */
    public NodeLeaf[] getSources() {
        return sources;
    }

    public void setSource(Node source) {
        this.selectedSource = source;
        if (selectedSource != null) setSources(new Node[] { source });
    }

    public Node getSource() {
        return this.selectedSource;
    }

    public void setSources(Node[] sources) {
        List<NodeLeaf> leafs = new ArrayList<NodeLeaf>();
        for (int i = 0; i < sources.length; i++) {
            leafs.addAll(getFlattenNodeLeafs(sources[i]));
        }
        this.sources = leafs.toArray(new NodeLeaf[leafs.size()]);
    }

    private List<NodeLeaf> getFlattenNodeLeafs(Node node) {
        List<NodeLeaf> leafs = new ArrayList<NodeLeaf>();
        if (node instanceof Interface) {
            leafs.addAll(((Interface) node).getMethodsList());
            if (node instanceof Aspect) {
                leafs.addAll(((Aspect) node).getAdvicesList());
            }
        } else if (node instanceof Package) {
            List<Interface> inner = ((Package) node).getInnerModulesList();
            for (Interface interface_ : inner) {
                leafs.addAll(getFlattenNodeLeafs(interface_));
            }
            List<Package> innerPackage = ((Package) node).getPackagesList();
            for (Package pack : innerPackage) {
                leafs.addAll(getFlattenNodeLeafs(pack));
            }
        } else if (node instanceof NodeLeaf) leafs.add((NodeLeaf) node);
        return leafs;
    }

    /**
	 * 
	 * @return Nodo destinazione
	 */
    public Node getTarget() {
        return target;
    }

    public void setTarget(Node target) {
        this.target = target;
    }

    public Node[] getLstIntermediary() {
        return lstIntermediary;
    }

    public void setLstIntermediary(Node[] lstIntermediary) {
        this.lstIntermediary = lstIntermediary;
    }

    public PathOptions getPathOptions() {
        return pathOptions;
    }

    /**
	 * Indica il tipo di percorso che verr� visualizzato. Questo attributo ha
	 * senso solo se GraphType � Partial
	 * 
	 * @param partialOptions
	 */
    public void setPathOptions(PathOptions pathOptions) {
        this.pathOptions = pathOptions;
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public void setEmpty(boolean isEmpty) {
        this.isEmpty = isEmpty;
    }

    public Criteria clone() {
        Criteria newCriteria = new Criteria();
        newCriteria.setEmpty(false);
        newCriteria.setGraphType(this.getGraphType());
        newCriteria.setLstIntermediary(this.getLstIntermediary());
        newCriteria.setOrdered(this.isOrdered());
        newCriteria.setPathOptions(this.getPathOptions());
        newCriteria.setProjectName(this.getProjectName());
        newCriteria.sources = (this.getSources() != null) ? this.getSources().clone() : null;
        newCriteria.selectedSource = this.getSource();
        newCriteria.setTarget(this.getTarget());
        return newCriteria;
    }
}
