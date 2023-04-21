package bg.tu_sofia.refg.imsqti.testitem;

import java.awt.Component;
import java.awt.Container;
import java.awt.GridLayout;
import java.util.List;
import java.util.Random;
import java.util.Vector;
import org.w3c.dom.Node;
import bg.tu_sofia.refg.imsqti.web.beans.ItemSessionBean;

public class ElementOrderInteraction extends Interaction {

    private static final long serialVersionUID = 1L;

    private final boolean shuffle;

    public ElementOrderInteraction(Node node) {
        super(node);
        shuffle = (node.getAttributes().getNamedItem(ATTR_RESPONSE_IDENTIFIER) != null) ? Boolean.parseBoolean(node.getAttributes().getNamedItem("shuffle").getNodeValue()) : false;
    }

    @Override
    public FeedBack getUserFeedback() {
        return null;
    }

    private Container panel;

    private final List<Component> fixedList = new Vector<Component>();

    private final Random rnd = new Random();

    @Override
    protected Component createGraphicContent(ItemSessionBean bean) {
        panel = getGraphicContent();
        int size = interactingElements.size();
        int i = 0;
        for (BodyElement e : this) {
            if (e instanceof Choice) {
                Choice c = (Choice) e;
                i = shuffle ? rnd.nextInt(size) : i++;
                if (panel.getComponent(i) != null) {
                    if (c.fixed) {
                        int j;
                        while ((j = rnd.nextInt(size)) == i) ;
                    }
                }
            }
        }
        panel.setLayout(new GridLayout(panel.getComponentCount(), 1));
        panel.validate();
        return this.panel;
    }

    @Override
    public void setUserFeedback(Object[] feedback) {
    }
}
