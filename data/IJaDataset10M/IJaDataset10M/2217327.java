package br.com.felix.fwt.svg.shapes;

import br.com.felix.fwt.svg.SVGBase;
import br.com.felix.fwt.svg.SVGBuilder;
import br.com.felix.fwt.svg.SVGColor;

public class SVGEllipse extends SVGBase<SVGEllipse> {

    private int rx, ry;

    private SVGColor fill;

    @Override
    public void toSVG(SVGBuilder svgBuilder) {
        svgBuilder.createElement("ellipse").attribute("cx", x).attribute("cy", y).attribute("rx", rx).attribute("ry", ry).attribute("fill", fill).endElementNoBody();
    }

    public SVGEllipse rx(int rx) {
        this.rx = rx;
        return this;
    }

    public SVGEllipse ry(int ry) {
        this.ry = ry;
        return this;
    }
}
