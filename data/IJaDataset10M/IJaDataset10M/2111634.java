package com.gtt.pattern.structural.composite;

/**
 * Client
 * 
 * @author 高甜甜(gao12581@sina.com)
 * @date 2011-3-31
 * 
 */
public class Client {

    public static void main(String[] args) {
        Client c = new Client();
        Component component = c.setupTree();
        component.dumpContents();
    }

    private Component setupTree() {
        Composite root = new Composite("root-composite");
        Composite parent;
        Composite elem;
        Leaf leaf;
        parent = root;
        elem = new Composite("first level - first sibling - composite");
        parent.add(elem);
        leaf = new Leaf("first level - second sibling - leaf");
        parent.add(leaf);
        parent = elem;
        elem = new Composite("second level - first sibling - composite");
        parent.add(elem);
        elem = new Composite("second level - second sibling - composite");
        parent.add(elem);
        parent = elem;
        leaf = new Leaf("third level - first sibling - leaf");
        parent.add(leaf);
        leaf = new Leaf("third level - second sibling - leaf");
        parent.add(leaf);
        elem = new Composite("third level - third sibling - composite");
        parent.add(elem);
        return root;
    }
}
