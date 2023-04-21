package org.expasy.jpl.core.mol.monomer.aa;

import org.expasy.jpl.commons.collection.symbol.AlphabetImpl;
import org.expasy.jpl.commons.collection.symbol.Symbol;
import org.expasy.jpl.commons.collection.tree.TreeNode;
import org.expasy.jpl.commons.collection.tree.TreeView;
import org.expasy.jpl.commons.collection.tree.TreeNodeImpl;
import org.junit.Before;
import org.junit.Test;

public class JPLAAAlphabetTest {

    private AlphabetImpl<AminoAcid> alphabet = AlphabetImpl.newInstance(AASymbol.getSymbolType());

    /**
	 * @throws java.lang.Exception
	 */
    @Before
    public void setUp() throws Exception {
        TreeNode<AASymbol> tree = createAASymbols();
        for (TreeView<AASymbol> node : tree.getNodes()) {
            alphabet.registerSymbolNode(node);
        }
    }

    @Test
    public void displaySymbols() {
        System.out.println(alphabet);
    }

    @Test
    public void displayAlanineSymbol() {
        Symbol<? extends AminoAcid> alanine = alphabet.lookupSymbolNode('A').getData();
        System.out.println(alanine);
    }

    @Test
    public void displayBSymbols() {
        TreeView<? extends Symbol<? extends AminoAcid>> aspAndasnNode = alphabet.lookupSymbolNode('B');
        System.out.println(aspAndasnNode.getLeaves());
        Symbol<? extends AminoAcid> aspAndasn = aspAndasnNode.getData();
        System.out.println(aspAndasn);
    }

    @Test
    public void displayAAString() {
        String seq = "MQRSTATGCFKLBOPZKXUJIL";
        StringBuilder sb = new StringBuilder();
        for (char c : seq.toCharArray()) {
            TreeView<? extends Symbol<? extends AminoAcid>> symNode = alphabet.lookupSymbolNode(c);
            if (!symNode.isLeave()) {
                sb.append("?");
            } else {
                sb.append(symNode.getData().getName());
            }
        }
        System.out.println(sb);
    }

    public static TreeNode<AASymbol> createAASymbols() {
        TreeNode<AASymbol> tree;
        TreeNode<AASymbol> bTree;
        TreeNode<AASymbol> zTree;
        tree = TreeNodeImpl.newRoot(AASymbol.newInstance('X'));
        bTree = TreeNodeImpl.newNode(AASymbol.newInstance('B'), tree);
        zTree = TreeNodeImpl.newNode(AASymbol.newInstance('Z'), tree);
        for (char c : "DN".toCharArray()) {
            TreeNodeImpl.newNode(AASymbol.newInstance(c), bTree);
        }
        for (char c : "EQ".toCharArray()) {
            TreeNodeImpl.newNode(AASymbol.newInstance(c), zTree);
        }
        for (char c : "ACFGHIJKLMOPRSTUVWY".toCharArray()) {
            TreeNodeImpl.newNode(AASymbol.newInstance(c), tree);
        }
        return tree;
    }
}
