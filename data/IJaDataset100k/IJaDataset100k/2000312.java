package vrml;

import vrml.node.*;

public class Browser {

    vrml.external.Browser browser;

    public Browser(vrml.external.Browser browser) {
        this.browser = browser;
    }

    public String toString() {
        return "VRSpace VRML Browser wrapper";
    }

    public String getName() {
        return browser.getName();
    }

    public String getVersion() {
        return browser.getVersion();
    }

    public float getCurrentSpeed() {
        return browser.getCurrentSpeed();
    }

    public float getCurrentFrameRate() {
        return browser.getCurrentFrameRate();
    }

    public String getWorldURL() {
        return browser.getWorldURL();
    }

    public void replaceWorld(BaseNode[] nodes) {
        vrml.external.Node[] tmp = new vrml.external.Node[nodes.length];
        for (int i = 0; i < tmp.length; i++) {
            tmp[i] = nodes[i].node;
        }
        browser.replaceWorld(tmp);
    }

    public BaseNode[] createVrmlFromString(String vrmlSyntax) throws InvalidVRMLSyntaxException {
        BaseNode[] ret = null;
        try {
            vrml.external.Node[] nodes = browser.createVrmlFromString(vrmlSyntax);
            ret = new BaseNode[nodes.length];
            for (int i = 0; i < nodes.length; i++) {
                if ("Script".equals(nodes[i].getType())) {
                    ret[i] = new Script(this, nodes[i]);
                } else {
                    ret[i] = new Node(this, nodes[i]);
                }
            }
        } catch (Throwable t) {
            throw new InvalidVRMLSyntaxException(t.toString());
        }
        return ret;
    }

    public void createVrmlFromURL(String[] url, BaseNode node, String event) throws InvalidVRMLSyntaxException {
        try {
            browser.createVrmlFromURL(url, node.node, event);
        } catch (Throwable t) {
            throw new InvalidVRMLSyntaxException(t.toString());
        }
    }

    public void addRoute(BaseNode fromNode, String fromEventOut, BaseNode toNode, String toEventIn) {
        browser.addRoute(fromNode.node, fromEventOut, toNode.node, toEventIn);
    }

    public void deleteRoute(BaseNode fromNode, String fromEventOut, BaseNode toNode, String toEventIn) {
        browser.deleteRoute(fromNode.node, fromEventOut, toNode.node, toEventIn);
    }

    public void loadURL(String[] url, String[] parameter) throws InvalidVRMLSyntaxException {
        try {
            browser.loadURL(url, parameter);
        } catch (Throwable t) {
            throw new InvalidVRMLSyntaxException(t.toString());
        }
    }

    public void setDescription(String description) {
        browser.setDescription(description);
    }
}
