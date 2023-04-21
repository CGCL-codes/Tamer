package org.jfonia.view.symbols;

import org.jfonia.assets.fonts.Fughetta;
import org.jfonia.connect5.basics.MutableValueNode;
import org.jfonia.connect5.basics.ValueNode;
import org.jfonia.constants.SymbolConstants;

/**
 *
 * @author Rik Bauwens
 */
public class DotSymbol extends PrimitiveStaffSymbol {

    public DotSymbol(ValueNode<Double> frameX, ValueNode<Double> staffY, MutableValueNode<Double> staffBase, MutableValueNode<Double> postStaffBase, ValueNode<Double> xNode, MutableValueNode<Double> widthNode, MutableValueNode<Integer> rankNode) {
        super(frameX, staffY, staffBase, postStaffBase, xNode, widthNode, rankNode);
        init();
    }

    private void init() {
        textGlyph = Fughetta.getGlyph(SymbolConstants.DOT);
        textGlyph.setSize(SymbolConstants.SYMBOL_SIZE);
        updateNodes();
    }
}
