package netgest.bo.xwc.components.classic;

import javax.el.ValueExpression;
import netgest.bo.xwc.components.security.ComponentSecurityHandler;
import netgest.bo.xwc.components.security.SecurableComponent;
import netgest.bo.xwc.components.security.ViewerAccessPolicyBuilder;
import netgest.bo.xwc.framework.components.XUIOutput;

public abstract class ViewerOutputSecurityBase extends XUIOutput implements SecurableComponent {

    private ComponentSecurityHandler componentSecurityHandler = new ComponentSecurityHandler(this);

    public COMPONENT_TYPE getViewerSecurityComponentType() {
        return SecurableComponent.COMPONENT_TYPE.ATTRIBUTE;
    }

    public String getViewerSecurityId() {
        String viewerSecurityId = null;
        ValueExpression v = getValueExpression("value");
        if (v != null && v.getExpressionString().length() > 0) {
            viewerSecurityId = ViewerAccessPolicyBuilder.cleanElExpression(v.getExpressionString());
        }
        return viewerSecurityId;
    }

    public String getViewerSecurityLabel() {
        return getViewerSecurityComponentType().toString() + " " + getViewerSecurityId();
    }

    @Override
    public void preRender() {
        super.preRender();
        String viewerSecurityId = getInstanceId();
        if (viewerSecurityId != null) {
            setViewerSecurityPermissions("#{viewBean.viewerPermissions." + viewerSecurityId + "}");
        }
    }

    public boolean isContainer() {
        return false;
    }

    public String getChildViewers() {
        return null;
    }

    public Byte getEffectivePermission() {
        return componentSecurityHandler.getEffectivePermission();
    }

    public boolean getEffectivePermission(byte securityPermision) {
        return componentSecurityHandler.getEffectivePermission(securityPermision);
    }

    public byte getSecurityPermissions() {
        return componentSecurityHandler.getSecurityPermissions();
    }

    public byte getViewerSecurityPermissions() {
        return componentSecurityHandler.getViewerSecurityPermissions();
    }

    public void setSecurityPermissions(String expressionText) {
        componentSecurityHandler.setSecurityPermissions(expressionText);
    }

    public void setViewerSecurityPermissions(String expressionText) {
        componentSecurityHandler.setViewerSecurityPermissions(expressionText);
    }

    public String getInstanceId() {
        return componentSecurityHandler.getInstanceId();
    }

    public void setInstanceId(String instanceId) {
        componentSecurityHandler.setInstanceId(instanceId);
    }
}
