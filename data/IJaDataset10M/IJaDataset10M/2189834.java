package org.jfonia.view.symbols;

import org.jfonia.assets.fonts.Fughetta;
import org.jfonia.connect5.basics.MutableValueNode;
import org.jfonia.connect5.basics.Observer;
import org.jfonia.connect5.basics.ValueNode;
import org.jfonia.constants.SymbolConstants;
import org.jfonia.model.elements.Clef;
import org.jfonia.model.elements.Clef.Type;

/**
 * Symbol representing a musical clef 
 * 
 * @author Rik Bauwens
 *
 */
public class ClefSymbol extends PrimitiveStaffSymbol {

    public ClefSymbol(ValueNode<Double> frameX, ValueNode<Double> staffY, MutableValueNode<Double> staffBase, MutableValueNode<Double> postStaffBase, ValueNode<Double> xNode, MutableValueNode<Double> widthNode, Clef clef) {
        super(frameX, staffY, staffBase, postStaffBase, xNode, widthNode, clef.getStaffRankNode());
        init(clef.getTypeNode());
    }

    private void init(ValueNode<Type> clefNode) {
        setClef(clefNode.getValue());
        clefNode.addObserver(new Observer() {

            public void onNotify(Object source) {
                setClef(((ValueNode<Type>) source).getValue());
            }
        });
    }

    private void setClef(Type clefType) {
        if (clefType == Type.G) {
            textGlyph = Fughetta.getGlyph(SymbolConstants.G_CLEF);
        } else if (clefType == Type.F) {
            textGlyph = Fughetta.getGlyph(SymbolConstants.F_CLEF);
        } else if (clefType == Type.C) {
            textGlyph = Fughetta.getGlyph(SymbolConstants.C_CLEF);
        } else if (clefType == Type.Gminus8) {
            textGlyph = Fughetta.getGlyph(SymbolConstants.G_MINUS8_CLEF);
        } else if (clefType == Type.Gplus8) {
            textGlyph = Fughetta.getGlyph(SymbolConstants.G_PLUS8_CLEF);
        } else if (clefType == Type.Fminus8) {
            textGlyph = Fughetta.getGlyph(SymbolConstants.F_MINUS8_CLEF);
        } else if (clefType == Type.Fplus8) {
            textGlyph = Fughetta.getGlyph(SymbolConstants.F_PLUS8_CLEF);
        }
        textGlyph.setSize(SymbolConstants.SYMBOL_SIZE);
        updateNodes();
    }
}
