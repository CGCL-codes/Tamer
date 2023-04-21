package edu.cmu.km.core;

import java.util.ArrayList;
import java.util.List;
import edu.cmu.km.kernel.tree.TreeKernel;

public class Test {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Tree<String> tree1 = new Tree<String>();
        Node<String> root = new Node<String>("root1");
        Node<String> child1 = new Node<String>("c1");
        Node<String> child2 = new Node<String>("c2");
        Node<String> child3 = new Node<String>("c3");
        Node<String> child4 = new Node<String>("c4");
        Node<String> child5 = new Node<String>("c5");
        root.addChild(child1);
        root.addChild(child2);
        root.addChild(child3);
        child3.addChild(child4);
        child3.addChild(child5);
        tree1.setRootElement(root);
        List<Node<String>> tree1list = tree1.toList();
        System.out.println(tree1list);
        Tree<String> tree2 = new Tree<String>();
        Node<String> root2 = new Node<String>("root2");
        Node<String> child21 = new Node<String>("c1");
        Node<String> child22 = new Node<String>("c2");
        Node<String> child23 = new Node<String>("c3");
        Node<String> child24 = new Node<String>("c4");
        Node<String> child25 = new Node<String>("c5");
        root2.addChild(child21);
        root2.addChild(child22);
        root2.addChild(child23);
        child23.addChild(child24);
        child23.addChild(child25);
        tree2.setRootElement(root2);
        List<Node<String>> tree2list = tree2.toList();
        System.out.println(tree2list);
        Forest<String> forest = new Forest<String>();
        forest.addMemberTree(tree1);
        forest.addMemberTree(tree2);
        System.out.println(forest);
        TreeKernel<String> treekernel = new TreeKernel<String>();
        int kern = treekernel.corootedSubtreeKernel(tree1, tree2);
        System.out.println(kern);
        System.out.println("forest.SACCoTreeKernel():");
        kern = treekernel.SACCoTreeKernel(tree1, tree2, 10);
        System.out.println(kern);
    }
}
