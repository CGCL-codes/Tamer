package br.com.felix.fwt.svg.shapes;

import br.com.felix.fwt.svg.SVGBase;
import br.com.felix.fwt.svg.SVGBuilder;
import br.com.felix.fwt.svg.SVGColor;

public class SVGLine extends SVGBase<SVGLine> {

    private int x2, y2;

    private SVGColor stroke;

    private Integer strokeWidth;

    @Override
    public void toSVG(SVGBuilder svgBuilder) {
        svgBuilder.createElement("line").attribute("x1", x).attribute("y1", y).attribute("x2", x2).attribute("y2", y2).attribute("stroke", stroke).attribute("stroke-width", strokeWidth).endElementNoBody();
    }

    public SVGLine x2(int x2) {
        this.x2 = x2;
        return this;
    }

    public SVGLine y2(int y2) {
        this.y2 = y2;
        return this;
    }

    public SVGLine stroke(SVGColor stroke) {
        this.stroke = stroke;
        return this;
    }

    public SVGLine strokeWidth(int strokeWidth) {
        this.strokeWidth = strokeWidth;
        return this;
    }
}
