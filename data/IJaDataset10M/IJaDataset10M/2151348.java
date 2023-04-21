package jsynoptic.plugins.java3d.tree;

import java.awt.event.ActionEvent;
import javax.media.j3d.AmbientLight;
import javax.media.j3d.DirectionalLight;
import javax.media.j3d.Light;
import javax.media.j3d.PointLight;
import javax.media.j3d.SpotLight;

/**
 * A Node for java3d lights : Ambient Directional Point and Spot
 */
public class LightNode extends SceneGraphTreeNode {

    /**
     * Register nodes resources and actions
     */
    static void loadResources() {
        addResources("javax.media.j3d.AmbientLight", "Ambient");
        addResources("javax.media.j3d.DirectionalLight", "Directional");
        addResources("javax.media.j3d.PointLight", "Point");
        addResources("javax.media.j3d.SpotLight", "Spot");
        addActions("javax.media.j3d.Light");
        addActions("AddLight", AmbientAction.class, DirectionalAction.class, PointAction.class, SpotAction.class);
    }

    public LightNode(Tree tree, Object graphObject, boolean getChildren) {
        super(tree, graphObject, getChildren);
    }

    static class LightAction extends AbstractNodeAction {

        protected final Class<? extends Light> _lightClass;

        protected LightAction(Class<? extends Light> lightClass) {
            _lightClass = lightClass;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            Light n = null;
            try {
                n = _lightClass.newInstance();
            } catch (InstantiationException e1) {
                throw new RuntimeException("Invalid Light class : " + _lightClass.getName(), e1);
            } catch (IllegalAccessException e1) {
                throw new RuntimeException("Invalid Light class : " + _lightClass.getName(), e1);
            }
            GroupNode gn = (GroupNode) getNode();
            gn.addChild(n);
            getNode().refresh();
        }
    }

    static class AmbientAction extends LightAction {

        public AmbientAction() {
            super(AmbientLight.class);
        }
    }

    static class DirectionalAction extends LightAction {

        public DirectionalAction() {
            super(DirectionalLight.class);
        }
    }

    static class PointAction extends LightAction {

        public PointAction() {
            super(PointLight.class);
        }
    }

    static class SpotAction extends LightAction {

        public SpotAction() {
            super(SpotLight.class);
        }
    }
}
