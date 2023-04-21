package org.openeccos.peers;

import java.io.IOException;
import nextapp.echo2.app.Component;
import nextapp.echo2.app.Extent;
import nextapp.echo2.app.ImageReference;
import nextapp.echo2.app.update.ServerComponentUpdate;
import nextapp.echo2.webcontainer.ActionProcessor;
import nextapp.echo2.webcontainer.ComponentSynchronizePeer;
import nextapp.echo2.webcontainer.ContainerInstance;
import nextapp.echo2.webcontainer.PropertyUpdateProcessor;
import nextapp.echo2.webcontainer.RenderContext;
import nextapp.echo2.webcontainer.SynchronizePeerFactory;
import nextapp.echo2.webcontainer.image.ImageRenderSupport;
import nextapp.echo2.webcontainer.image.ImageTools;
import nextapp.echo2.webcontainer.propertyrender.ExtentRender;
import nextapp.echo2.webrender.Connection;
import nextapp.echo2.webrender.ContentType;
import nextapp.echo2.webrender.ServerMessage;
import nextapp.echo2.webrender.Service;
import nextapp.echo2.webrender.WebRenderServlet;
import nextapp.echo2.webrender.servermessage.DomUpdate;
import nextapp.echo2.webrender.service.JavaScriptService;
import org.openeccos.util.PDUtil;
import org.openeccos.widgets.PDShortcut;
import org.w3c.dom.Element;

/**
 *
 * @author cgspinner@web.de
 */
public class PDShortcutPeer implements ActionProcessor, ImageRenderSupport, PropertyUpdateProcessor, ComponentSynchronizePeer {

    private static final String IMAGE_ID_ICON = "icon";

    private static final ImageReference DEFAULT_CLOSE_ICON = PDUtil.getImage("/img/notice.gif");

    private static final String BLANK_HTML_STRING = "<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" " + "\"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" + "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><title></title><body></body></html>";

    private static final Service BLANK_HTML_SERVICE = new Service() {

        public String getId() {
            return "Echo.PDShortcut.IFrame";
        }

        public int getVersion() {
            return 0;
        }

        public void service(Connection conn) throws IOException {
            conn.setContentType(ContentType.TEXT_HTML);
            conn.getWriter().write(BLANK_HTML_STRING);
        }
    };

    public static final Service WINDOW_PANE_SERVICE = JavaScriptService.forResource("Echo.PDShortcut", "/org/openeccos/js/PDShortcut.js");

    static {
        WebRenderServlet.getServiceRegistry().add(WINDOW_PANE_SERVICE);
    }

    private static void renderPixelProperty(PDShortcut windowPane, String propertyName, Element element, String attributeName) {
        String pixelValue;
        Extent extent = (Extent) windowPane.getRenderProperty(propertyName);
        if (extent != null) {
            pixelValue = ExtentRender.renderCssAttributePixelValue(extent);
            if (pixelValue != null) {
                element.setAttribute(attributeName, pixelValue);
            }
        }
    }

    public PDShortcutPeer() {
        super();
    }

    public String getContainerId(Component child) {
        return ContainerInstance.getElementId(child.getParent()) + "_content";
    }

    public ImageReference getImage(Component component, String imageId) {
        if (IMAGE_ID_ICON.equals(imageId)) {
            return (ImageReference) component.getRenderProperty(PDShortcut.PROPERTY_ICON, DEFAULT_CLOSE_ICON);
        }
        return null;
    }

    public void processAction(ContainerInstance ci, Component component, Element actionElement) {
        ci.getUpdateManager().getClientUpdateManager().setComponentAction(component, PDShortcut.PROPERTY_ACTION_EVENT, null);
    }

    public void processPropertyUpdate(ContainerInstance ci, Component component, Element propertyElement) {
        String propertyName = propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_NAME);
        if (PDShortcut.PROPERTY_POSITION_X.equals(propertyName)) {
            ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, PDShortcut.PROPERTY_POSITION_X, ExtentRender.toExtent(propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_VALUE)));
        } else if (PDShortcut.PROPERTY_POSITION_Y.equals(propertyName)) {
            ci.getUpdateManager().getClientUpdateManager().setComponentProperty(component, PDShortcut.PROPERTY_POSITION_Y, ExtentRender.toExtent(propertyElement.getAttribute(PropertyUpdateProcessor.PROPERTY_VALUE)));
        }
    }

    public void renderAdd(RenderContext rc, ServerComponentUpdate update, String targetId, Component component) {
        ServerMessage serverMessage = rc.getServerMessage();
        serverMessage.addLibrary(WINDOW_PANE_SERVICE.getId());
        PDShortcut windowPane = (PDShortcut) component;
        renderInitDirective(rc, windowPane, targetId);
        Component[] children = windowPane.getVisibleComponents();
        if (children.length != 0) {
            ComponentSynchronizePeer syncPeer = SynchronizePeerFactory.getPeerForComponent(children[0].getClass());
            syncPeer.renderAdd(rc, update, getContainerId(children[0]), children[0]);
        }
    }

    public void renderDispose(RenderContext rc, ServerComponentUpdate update, Component component) {
        ServerMessage serverMessage = rc.getServerMessage();
        serverMessage.addLibrary(WINDOW_PANE_SERVICE.getId());
        renderDisposeDirective(rc, (PDShortcut) component);
    }

    private void renderDisposeDirective(RenderContext rc, PDShortcut windowPane) {
        String elementId = ContainerInstance.getElementId(windowPane);
        ServerMessage serverMessage = rc.getServerMessage();
        Element initElement = serverMessage.appendPartDirective(ServerMessage.GROUP_ID_PREREMOVE, "PDShortcut.MessageProcessor", "dispose");
        initElement.setAttribute("eid", elementId);
    }

    private void renderInitDirective(RenderContext rc, PDShortcut windowPane, String targetId) {
        String elementId = ContainerInstance.getElementId(windowPane);
        ServerMessage serverMessage = rc.getServerMessage();
        Element partElement = serverMessage.addPart(ServerMessage.GROUP_ID_UPDATE, "PDShortcut.MessageProcessor");
        Element initElement = serverMessage.getDocument().createElement("init");
        initElement.setAttribute("container-eid", targetId);
        initElement.setAttribute("eid", elementId);
        renderPixelProperty(windowPane, PDShortcut.PROPERTY_POSITION_X, initElement, "position-x");
        renderPixelProperty(windowPane, PDShortcut.PROPERTY_POSITION_Y, initElement, "position-y");
        String title = (String) windowPane.getRenderProperty(PDShortcut.PROPERTY_TITLE);
        initElement.setAttribute("title", title);
        if (getImage(windowPane, IMAGE_ID_ICON) != null) {
            initElement.setAttribute("icon", ImageTools.getUri(rc, this, windowPane, IMAGE_ID_ICON));
        }
        partElement.appendChild(initElement);
    }

    /**
	 * @see nextapp.echo2.webcontainer.ComponentSynchronizePeer#renderUpdate(nextapp.echo2.webcontainer.RenderContext,
	 *      nextapp.echo2.app.update.ServerComponentUpdate, java.lang.String)
	 */
    public boolean renderUpdate(RenderContext rc, ServerComponentUpdate update, String targetId) {
        renderDisposeDirective(rc, (PDShortcut) update.getParent());
        DomUpdate.renderElementRemove(rc.getServerMessage(), ContainerInstance.getElementId(update.getParent()));
        renderAdd(rc, update, targetId, update.getParent());
        return true;
    }
}
