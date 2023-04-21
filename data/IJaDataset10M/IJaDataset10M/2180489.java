package calclipse.caldron.gui.graph.actions;

import java.awt.event.ActionEvent;
import javax.swing.Icon;
import calclipse.Resource;
import calclipse.caldron.gui.graph.features.ZoomFeature;
import calclipse.caldron.gui.locale.LocalizedAction;

/**
 * Zooms in.
 * @author T. Sommerland
 */
public class ZoomInAction extends LocalizedAction {

    private static final long serialVersionUID = 1L;

    private final ZoomFeature feature;

    private final double factor;

    private final int steps;

    private final int delay;

    public ZoomInAction(final ZoomFeature feature, final double factor, final int steps, final int delay) {
        super("Zoom in");
        this.feature = feature;
        this.factor = factor;
        this.steps = steps;
        this.delay = delay;
    }

    @Override
    public void actionPerformed(final ActionEvent evt) {
        feature.zoom(factor, steps, delay);
    }

    @Resource("calclipse.caldron.gui.graph.actions.ZoomInAction.accelerator")
    @Override
    public void setAccelerator(final String keyStroke) {
        super.setAccelerator(keyStroke);
    }

    @Resource("calclipse.caldron.gui.graph.actions.ZoomInAction.largeIcon")
    @Override
    public void setLargeIcon(final Icon largeIcon) {
        super.setLargeIcon(largeIcon);
    }

    @Resource("calclipse.caldron.gui.graph.actions.ZoomInAction.longDescription")
    @Override
    public void setLongDescription(final String longDescription) {
        super.setLongDescription(longDescription);
    }

    @Resource("calclipse.caldron.gui.graph.actions.ZoomInAction.mnemonic")
    @Override
    public void setMnemonic(final String keyStroke) {
        super.setMnemonic(keyStroke);
    }

    @Resource("calclipse.caldron.gui.graph.actions.ZoomInAction.name")
    @Override
    public void setName(final String name) {
        super.setName(name);
    }

    @Resource("calclipse.caldron.gui.graph.actions.ZoomInAction.shortDescription")
    @Override
    public void setShortDescription(final String shortDescription) {
        super.setShortDescription(shortDescription);
    }

    @Resource("calclipse.caldron.gui.graph.actions.ZoomInAction.smallIcon")
    @Override
    public void setSmallIcon(final Icon smallIcon) {
        super.setSmallIcon(smallIcon);
    }
}
