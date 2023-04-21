package ioports.core.templates;

import ioports.core.IOPort;
import ioports.core.Types;

public class TreeTemplate extends IOPort {

    protected IOPort tree;

    protected IOPort selector;

    protected TreeDetailTemplateValue treeDetailTemplateValue = new TreeDetailTemplateValue();

    public TreeTemplate(String pvName, String pvType) {
        super(pvName, pvType);
        init();
    }

    public TreeTemplate(String pvName, String pvType, Object pvValue) {
        super(pvName, pvType, pvValue);
        init();
    }

    public TreeTemplate(String pvName, String pvID, String pvType) {
        super(pvName, pvID, pvType);
        init();
    }

    public TreeTemplate(String pvName, String pvID, String pvType, Object pvValue) {
        super(pvName, pvID, pvType, pvValue);
        init();
    }

    private void init() {
        tree = new IOPort("Tree", Types.TREE);
        tree.setId("Tree");
        selector = new IOPort("Tree", Types.TREEOBJECT);
        selector.setId("TreeObject");
    }

    public IOPort getTree() {
        return tree;
    }

    public void setTree(IOPort pvTree) {
        tree = pvTree;
    }

    public IOPort getSelector() {
        return selector;
    }

    public void setSelector(IOPort pvSelector) {
        selector = pvSelector;
    }

    @Override
    public Object getValue() {
        return treeDetailTemplateValue;
    }

    @Override
    public void setValue(Object pvValue) {
        if (pvValue instanceof TreeDetailTemplateValue) {
            treeDetailTemplateValue = (TreeDetailTemplateValue) pvValue;
            tree.setValue(treeDetailTemplateValue.tree);
            selector.setValue(treeDetailTemplateValue.object);
        }
    }
}
