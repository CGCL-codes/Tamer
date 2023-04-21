package org.primordion.user.app.climatechange.igm;

import org.primordion.xholon.base.IXholon;
import com.kitfox.svg.Text;
import com.kitfox.svg.Tspan;

/**
 * Store correspondences between Viewables and SVG elements.
 * 
 * @author ken
 * @deprecated
 */
public class SvgViewable extends Xhigm {

    private String xhcName = "";

    private IXholon xhNode = null;

    private String svgId = "";

    private Tspan svgNode = null;

    public void postConfigure() {
        IgmView igmView = ((IgmView) this.getParentNode());
        svgNode = (Tspan) (igmView).getDiagram().getElement(svgId);
        igmView.getViewables().visit(this);
        super.postConfigure();
    }

    public void act() {
        if (xhNode != null) {
            setText("(" + String.format("%.0f", xhNode.getVal()) + ")");
        }
        super.act();
    }

    public boolean visit(IXholon visitee) {
        if (visitee.getXhcName().equals(xhcName)) {
            xhNode = visitee;
            setText();
            return false;
        }
        return true;
    }

    protected void setText() {
        double val = xhNode.getVal();
        if (val == Double.NEGATIVE_INFINITY) {
            setText("");
        } else {
            setText("(" + String.format("%.0f", val) + ")");
        }
    }

    /**
	 * Set the text for an existing SVG tspan node, whose parent must be an SVG
	 * text node.
	 * @param text The text that the tspan node should be set to.
	 */
    protected void setText(String text) {
        if (svgNode == null) {
            return;
        }
        svgNode.setText(text);
        Text textEle = (Text) svgNode.getParent();
        try {
            textEle.rebuild();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int setAttributeVal(String attrName, String attrVal) {
        if ("xhcName".equals(attrName)) {
            xhcName = attrVal;
        } else if ("svgId".equals(attrName)) {
            svgId = attrVal;
        }
        return 0;
    }

    public String toString() {
        String outStr = getName();
        outStr += " xhcName:" + xhcName;
        outStr += " svgId:" + svgId;
        return outStr;
    }

    public String getXhcName() {
        return xhcName;
    }

    public void setXhcName(String xhcName) {
        this.xhcName = xhcName;
    }

    public IXholon getXhNode() {
        return xhNode;
    }

    public void setXhNode(IXholon xhNode) {
        this.xhNode = xhNode;
    }

    public String getSvgId() {
        return svgId;
    }

    public void setSvgId(String svgId) {
        this.svgId = svgId;
    }

    public Tspan getSvgNode() {
        return svgNode;
    }

    public void setSvgNode(Tspan svgNode) {
        this.svgNode = svgNode;
    }
}
