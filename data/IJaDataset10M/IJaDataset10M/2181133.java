package j3dworkbench.proxy;

import javax.media.j3d.Bounds;
import javax.media.j3d.Group;
import javax.media.j3d.Switch;

public class SwitchProxy extends NodeProxy {

    private static final long serialVersionUID = 1L;

    protected SwitchProxy() {
    }

    public static SwitchProxy createInstance() {
        return new SwitchProxy(new Switch());
    }

    public SwitchProxy(Switch switchNode) {
        super(switchNode);
        switchNode.setCapability(Switch.ALLOW_SWITCH_WRITE);
        switchNode.setCapability(Group.ALLOW_CHILDREN_WRITE);
        switchNode.setCapability(Group.ALLOW_CHILDREN_EXTEND);
    }

    @Override
    public boolean isEnabled() {
        return getJ3DNode().getWhichChild() == Switch.CHILD_ALL;
    }

    /**
	 * Do not inform children; children will be made live or not live
	 */
    @Override
    public void setEnabled(boolean enable) {
        if (isEnabled() == enable) {
            super.setEnabled(enable);
            return;
        }
        getJ3DNode().setWhichChild(enable ? Switch.CHILD_ALL : Switch.CHILD_NONE);
        super.setEnabled(enable);
    }

    @Override
    public Group getParentGroup() {
        return getJ3DNode();
    }

    @Override
    public Bounds getBounds() {
        return getTransformGroup().getBounds();
    }

    @Override
    public String getNodeTypeDescription() {
        return "When enabled, all child geometry is rendered, otherwise," + "it remains invisible";
    }

    @Override
    public Switch getJ3DNode() {
        return (Switch) node;
    }

    @Override
    protected void setCapabilities() {
    }
}
