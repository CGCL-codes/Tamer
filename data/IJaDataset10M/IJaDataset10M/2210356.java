package ch.unibe.inkml;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import ch.unibe.inkml.InkChannel.ChannelName;
import ch.unibe.inkml.util.ArrayTracePoint;

public class InkCanvasTransform extends InkUniqueElement {

    public static final String INKML_NAME = "canvasTransform";

    public static final String INKML_ATTR_INVERTIBLE = "invertible";

    public static final String ID_PREFIX = "ct";

    public static InkCanvasTransform getIdentityTransform(InkInk ink, String id, InkTraceFormat source, InkTraceFormat target) {
        if (!ink.getDefinitions().containsKey(id)) {
            try {
                InkCanvasTransform result;
                result = new InkCanvasTransform(ink, id);
                result.invertible = true;
                InkMapping m1 = new InkIdentityMapping(ink);
                result.setForewardMapping(m1);
                return result;
            } catch (InkMLComplianceException e) {
                throw new Error(e);
            }
        }
        return (InkCanvasTransform) ink.getDefinitions().get(id);
    }

    private boolean invertible = true;

    private InkMapping foreward, backward;

    public InkCanvasTransform(InkInk ink) {
        super(ink);
    }

    public InkCanvasTransform(InkInk ink, String id) throws InkMLComplianceException {
        super(ink, id);
    }

    public void setForewardMapping(InkMapping foreward) {
        this.foreward = foreward;
    }

    public void setBackwardMapping(InkMapping backward) {
        this.backward = backward;
    }

    @Override
    public void buildFromXMLNode(Element node) throws InkMLComplianceException {
        super.buildFromXMLNode(node);
        if (node.hasAttribute(INKML_ATTR_INVERTIBLE)) {
            this.invertible = Boolean.parseBoolean(node.getAttribute(INKML_ATTR_INVERTIBLE));
        }
        NodeList mappings = node.getElementsByTagName(InkMapping.INKML_NAME);
        if (mappings.getLength() == 0) {
            throw new InkMLComplianceException("A canvasTransform must contain a mapping");
        }
        InkMapping m = InkMapping.mappingFactory(getInk(), (Element) mappings.item(0));
        this.setForewardMapping(m);
        if (mappings.getLength() > 1) {
            m = InkMapping.mappingFactory(getInk(), (Element) mappings.item(1));
            this.setBackwardMapping(m);
        }
    }

    @Override
    public void exportToInkML(Element parent) throws InkMLComplianceException {
        Element t = parent.getOwnerDocument().createElement(INKML_NAME);
        parent.appendChild(t);
        super.exportToInkML(t);
        if (foreward.isInvertible()) {
            this.invertible = true;
        }
        if (!this.invertible) {
            t.setAttribute(INKML_ATTR_INVERTIBLE, "false");
        }
        this.foreward.exportToInkML(t);
        if (!this.invertible && this.backward != null) {
            this.backward.exportToInkML(t);
        }
    }

    public void flipAxis(InkTraceFormat sourceFormat, InkTraceFormat targetFormat) {
        foreward = InkMapping.flipAxis(foreward, sourceFormat, targetFormat);
        if (backward != null) {
            backward = InkMapping.flipAxis(backward, targetFormat, sourceFormat);
        }
    }

    public void invertAxis(InkTraceFormat sourceFormat, InkTraceFormat targetFormat, ChannelName axis) {
        foreward = InkMapping.invertAxis(foreward, sourceFormat, targetFormat, axis);
        if (backward != null) {
            backward = InkMapping.invertAxis(backward, targetFormat, sourceFormat, axis);
        }
    }

    public InkMapping getForwardMapping() {
        return this.foreward;
    }

    /**
     * @param sourcePoints
     * @param points
     * @param sourceFormat
     * @param targetFormat
     * @throws InkMLComplianceException 
     */
    public void transform(double[][] sourcePoints, double[][] points, InkTraceFormat sourceFormat, InkTraceFormat targetFormat) throws InkMLComplianceException {
        foreward.transform(sourcePoints, points, sourceFormat, targetFormat);
    }

    /**
     * @param d
     * @param sourceFormat
     * @param canvasTraceFormat
     * @return
     * @throws InkMLComplianceException 
     */
    public InkTracePoint transform(InkTracePoint sourcePoint, InkTraceFormat sourceFormat, InkTraceFormat targetFormat) throws InkMLComplianceException {
        double[][] src = new double[1][sourceFormat.getChannelCount()];
        int counter = 0;
        for (InkChannel channel : sourceFormat.getChannels()) {
            src[0][counter++] = sourcePoint.get(channel.getName());
        }
        double[][] trgt = new double[1][targetFormat.getChannelCount()];
        transform(src, trgt, sourceFormat, targetFormat);
        return new ArrayTracePoint(trgt[0], targetFormat);
    }

    /**
     * @param points
     * @param sourcePoints
     * @param canvasFormat
     * @param sourceFormat
     * @throws InkMLComplianceException 
     */
    public void backTransform(double[][] points, double[][] sourcePoints, InkTraceFormat canvasFormat, InkTraceFormat sourceFormat) throws InkMLComplianceException {
        if (foreward.isInvertible()) {
            foreward.backTransform(sourcePoints, points, canvasFormat, sourceFormat);
        } else if (backward != null) {
            backward.transform(sourcePoints, sourcePoints, canvasFormat, sourceFormat);
        } else {
            throw new UnsupportedOperationException("Backwards transformation is not given, and foreward transformation is not invertible.");
        }
    }
}
