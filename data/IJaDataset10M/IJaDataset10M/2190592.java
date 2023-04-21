package canvas.view;

import canvas.view.persistence.XObjectScenePO;
import canvas.view.widget.XObjectScene;
import javax.swing.JComponent;
import javax.swing.JPanel;
import org.jdesktop.beansbinding.Converter;

/**
 *
 * @author Isaac
 */
public class ObjectSceneAndPOConverter extends Converter<XObjectScenePO, XObjectScene> {

    private JPanel jPanelImage;

    public ObjectSceneAndPOConverter(JPanel jPanelImage) {
        this.jPanelImage = jPanelImage;
    }

    @Override
    public XObjectScene convertForward(XObjectScenePO objectScenePO) {
        XObjectScene objectScene = objectScenePO.createXObjectScene();
        JComponent jComp = objectScene.createView();
        jPanelImage.removeAll();
        jPanelImage.add(jComp);
        jPanelImage.repaint();
        return objectScene;
    }

    @Override
    public XObjectScenePO convertReverse(XObjectScene objectScene) {
        return objectScene.complete();
    }
}
