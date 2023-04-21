package org.openscience.cdk.renderer.visitor;

import org.openscience.cdk.renderer.element.AtomSymbol;
import org.openscience.cdk.renderer.element.BondSymbol;
import org.openscience.cdk.renderer.element.IRenderingElement;
import org.openscience.cdk.renderer.element.SymbolGroup;
import org.openscience.cdk.renderer.element.IRenderingVisitor;

public class DistanceSearchVisitor implements IRenderingVisitor {

    private int x;

    private int y;

    private int searchRadiusSQ;

    private int closestDistanceSQ;

    public IRenderingElement bestHit;

    public DistanceSearchVisitor(int x, int y, int searchRadius) {
        this.x = x;
        this.y = y;
        this.searchRadiusSQ = searchRadius * searchRadius;
        this.bestHit = null;
        this.closestDistanceSQ = -1;
    }

    private void check(IRenderingElement element) {
        int x2 = element.getCenterX();
        int y2 = element.getCenterY();
        int dSQ = (this.x - x2) * (this.x - x2) + (this.y - y2) * (this.y - y2);
        if (dSQ < this.searchRadiusSQ && (this.closestDistanceSQ == -1 || dSQ < this.closestDistanceSQ)) {
            this.bestHit = element;
            this.closestDistanceSQ = dSQ;
        }
    }

    public void visitSymbolGroup(SymbolGroup element) {
        element.visitChildren(this);
    }

    public void visitAtomSymbol(AtomSymbol element) {
        this.check(element);
    }

    public void visitBondSymbol(BondSymbol element) {
        this.check(element);
    }
}
