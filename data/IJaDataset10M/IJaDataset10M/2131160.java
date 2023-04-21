package bpmn.diagram.edit.parts.figures;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Dimension;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.gmf.runtime.draw2d.ui.render.RenderedImage;
import org.eclipse.gmf.runtime.draw2d.ui.render.factory.RenderedImageFactory;
import org.eclipse.gmf.runtime.draw2d.ui.render.figures.ScalableImageFigure;
import org.eclipse.gmf.runtime.notation.Node;
import org.osgi.framework.Bundle;
import bpmn.ANDGateway;
import bpmn.DataObject;
import bpmn.ORGateway;
import bpmn.XOREventGateway;
import bpmn.XORGateway;
import bpmn.diagram.part.BpmnDiagramEditorPlugin;
import bpmn.util.BpmnSwitch;

/**
 * @author jassuncao
 *
 */
public class SVGFigureFactory implements IImageFigureFactory {

    protected static final boolean ANTI_ALIAS = false;

    protected static final Dimension DEFAULT_FIGURE_SIZE = new Dimension(40, 40);

    private static final Path AND_GATEWAY_PATH = new Path("shapes/and_gateway.svg");

    private static final Path OR_GATEWAY_PATH = new Path("shapes/or_gateway.svg");

    private static final Path XOR_EVENT_GATEWAY_PATH = new Path("shapes/event_based_xor_gateway.svg");

    private static final Path XOR_GATEWAY_PATH = new Path("shapes/data_based_xor_gateway.svg");

    private static final Path END_EVENT_NONE_PATH = new Path("shapes/end_event_none.svg");

    private static final Path END_EVENT_MESSAGE_PATH = new Path("shapes/end_event_message.svg");

    private static final Path END_EVENT_CANCEL_PATH = new Path("shapes/end_event_cancel.svg");

    private static final Path END_EVENT_COMPENSATION_PATH = new Path("shapes/end_event_compensation.svg");

    private static final Path END_EVENT_LINK_PATH = new Path("shapes/end_event_link.svg");

    private static final Path END_EVENT_MULTIPLE_PATH = new Path("shapes/end_event_multiple.svg");

    private static final Path END_EVENT_TERMINATE_PATH = new Path("shapes/end_event_terminate.svg");

    private static final Path INTERMEDIATE_EVENT_NONE_PATH = new Path("shapes/intermediate_event_none.svg");

    private static final Path INTERMEDIATE_EVENT_MESSAGE_PATH = new Path("shapes/intermediate_event_message.svg");

    private static final Path INTERMEDIATE_EVENT_TIMER_PATH = new Path("shapes/intermediate_event_timer.svg");

    private static final Path INTERMEDIATE_EVENT_ERROR_PATH = new Path("shapes/intermediate_event_error.svg");

    private static final Path INTERMEDIATE_EVENT_CANCEL_PATH = new Path("shapes/intermediate_event_cancel.svg");

    private static final Path INTERMEDIATE_EVENT_COMPENSATION_PATH = new Path("shapes/intermediate_event_compensation.svg");

    private static final Path INTERMEDIATE_EVENT_RULE_PATH = new Path("shapes/intermediate_event_rule.svg");

    private static final Path INTERMEDIATE_EVENT_LINK_PATH = new Path("shapes/intermediate_event_link.svg");

    private static final Path INTERMEDIATE_EVENT_MULTIPLE_PATH = new Path("shapes/intermediate_event_multiple.svg");

    private static final Path START_EVENT_NONE_PATH = new Path("shapes/start_event_none.svg");

    private static final Path START_EVENT_MESSAGE_PATH = new Path("shapes/start_event_message.svg");

    private static final Path START_EVENT_TIMER_PATH = new Path("shapes/start_event_timer.svg");

    private static final Path START_EVENT_RULE_PATH = new Path("shapes/start_event_rule.svg");

    private static final Path START_EVENT_LINK_PATH = new Path("shapes/start_event_link.svg");

    private static final Path START_EVENT_MULTIPLE_PATH = new Path("shapes/start_event_multiple.svg");

    private static final Path DATA_OBJECT_PATH = new Path("shapes/data_object.svg");

    private Bundle bundle = null;

    private final Map<Path, URL> urlCache;

    public SVGFigureFactory() {
        bundle = BpmnDiagramEditorPlugin.getInstance().getBundle();
        urlCache = new HashMap<Path, URL>();
    }

    public IFigure createFigure(Object model) {
        EObject dataModel = ((Node) model).getElement();
        Path path = (Path) BPMNSWITCH.doSwitch(dataModel);
        if (null == path) return null;
        URL url = urlCache.get(path);
        if (url == null) {
            url = FileLocator.find(bundle, path, null);
            urlCache.put(path, url);
        }
        RenderedImage renderedImage = RenderedImageFactory.getInstance(url);
        return new ScalableImageFigure(renderedImage, true, true, ANTI_ALIAS);
    }

    public Dimension getFigureDefaultSize(Object model) {
        return DEFAULT_FIGURE_SIZE;
    }

    static final BpmnSwitch BPMNSWITCH = new BpmnSwitch() {

        @Override
        public Object caseANDGateway(ANDGateway object) {
            return AND_GATEWAY_PATH;
        }

        @Override
        public Object caseORGateway(ORGateway object) {
            return OR_GATEWAY_PATH;
        }

        @Override
        public Object caseXOREventGateway(XOREventGateway object) {
            return XOR_EVENT_GATEWAY_PATH;
        }

        @Override
        public Object caseXORGateway(XORGateway object) {
            return XOR_GATEWAY_PATH;
        }

        @Override
        public Object caseDataObject(DataObject object) {
            return DATA_OBJECT_PATH;
        }
    };
}
