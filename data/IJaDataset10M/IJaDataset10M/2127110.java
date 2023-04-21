package com.jeantessier.dependency;

public class GraphSummarizer extends GraphCopier {

    private SelectionCriteria scopeCriteria;

    private SelectionCriteria filterCriteria;

    public GraphSummarizer(SelectionCriteria scopeCriteria, SelectionCriteria filterCriteria) {
        super(new SelectiveTraversalStrategy(scopeCriteria, filterCriteria));
        this.scopeCriteria = scopeCriteria;
        this.filterCriteria = filterCriteria;
    }

    protected boolean isInScope(PackageNode node) {
        return scopeCriteria.matchesPackageName(node.getName());
    }

    protected void preprocessPackageNode(PackageNode node) {
        if (scopeCriteria.isMatchingPackages()) {
            super.preprocessPackageNode(node);
        }
    }

    protected void postprocessPackageNode(PackageNode node) {
        if (scopeCriteria.isMatchingPackages()) {
            super.postprocessPackageNode(node);
        }
    }

    public void visitInboundPackageNode(PackageNode node) {
        if (getCurrentNode() != null && filterCriteria.matchesPackageName(node.getName())) {
            if (filterCriteria.isMatchingPackages()) {
                copy(getFilterFactory(), node).addDependency(getCurrentNode());
            }
        }
    }

    public void visitOutboundPackageNode(PackageNode node) {
        if (getCurrentNode() != null && filterCriteria.matchesPackageName(node.getName())) {
            if (filterCriteria.isMatchingPackages()) {
                getCurrentNode().addDependency(copy(getFilterFactory(), node));
            }
        }
    }

    protected boolean isInScope(ClassNode node) {
        return scopeCriteria.matchesClassName(node.getName());
    }

    protected void preprocessClassNode(ClassNode node) {
        if (scopeCriteria.isMatchingClasses()) {
            super.preprocessClassNode(node);
        }
    }

    protected void postprocessClassNode(ClassNode node) {
        if (scopeCriteria.isMatchingClasses()) {
            super.postprocessClassNode(node);
        }
    }

    public void visitInboundClassNode(ClassNode node) {
        if (getCurrentNode() != null && filterCriteria.matchesClassName(node.getName())) {
            if (filterCriteria.isMatchingClasses()) {
                copy(getFilterFactory(), node).addDependency(getCurrentNode());
            } else if (filterCriteria.isMatchingPackages()) {
                copy(getFilterFactory(), node.getPackageNode()).addDependency(getCurrentNode());
            }
        }
    }

    public void visitOutboundClassNode(ClassNode node) {
        if (getCurrentNode() != null && filterCriteria.matchesClassName(node.getName())) {
            if (filterCriteria.isMatchingClasses()) {
                getCurrentNode().addDependency(copy(getFilterFactory(), node));
            } else if (filterCriteria.isMatchingPackages()) {
                getCurrentNode().addDependency(copy(getFilterFactory(), node.getPackageNode()));
            }
        }
    }

    protected boolean isInScope(FeatureNode node) {
        return scopeCriteria.matchesFeatureName(node.getName());
    }

    protected void preprocessFeatureNode(FeatureNode node) {
        if (scopeCriteria.isMatchingFeatures()) {
            super.preprocessFeatureNode(node);
        }
    }

    protected void postprocessFeatureNode(FeatureNode node) {
        if (scopeCriteria.isMatchingFeatures()) {
            super.postprocessFeatureNode(node);
        }
    }

    public void visitInboundFeatureNode(FeatureNode node) {
        if (getCurrentNode() != null && filterCriteria.matchesFeatureName(node.getName())) {
            if (filterCriteria.isMatchingFeatures()) {
                copy(getFilterFactory(), node).addDependency(getCurrentNode());
            } else if (filterCriteria.isMatchingClasses()) {
                copy(getFilterFactory(), node.getClassNode()).addDependency(getCurrentNode());
            } else if (filterCriteria.isMatchingPackages()) {
                copy(getFilterFactory(), node.getClassNode().getPackageNode()).addDependency(getCurrentNode());
            }
        }
    }

    public void visitOutboundFeatureNode(FeatureNode node) {
        if (getCurrentNode() != null && filterCriteria.matchesFeatureName(node.getName())) {
            if (filterCriteria.isMatchingFeatures()) {
                getCurrentNode().addDependency(copy(getFilterFactory(), node));
            } else if (filterCriteria.isMatchingClasses()) {
                getCurrentNode().addDependency(copy(getFilterFactory(), node.getClassNode()));
            } else if (filterCriteria.isMatchingPackages()) {
                getCurrentNode().addDependency(copy(getFilterFactory(), node.getClassNode().getPackageNode()));
            }
        }
    }
}
