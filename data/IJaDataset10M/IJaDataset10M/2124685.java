package com.gwtaf.portal.client.frame;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.Event.NativePreviewHandler;
import com.google.gwt.user.client.ui.Widget;
import com.gwtaf.core.client.layout.FlowLayout;
import com.gwtaf.core.client.layout.FlowLayoutData;
import com.gwtaf.core.client.layout.LayoutPanel;
import com.gwtaf.core.client.layout.SplitFlowLayout;
import com.gwtaf.core.client.widget.SwitchPanel;
import com.gwtaf.core.shared.util.Util;
import com.gwtaf.portal.client.IPortalListener;
import com.gwtaf.portal.client.document.DocumentManager;
import com.gwtaf.portal.client.document.IDocument;
import com.gwtaf.portal.client.layout.DocumentLayout;
import com.gwtaf.portal.client.layout.IPortalLayout;
import com.gwtaf.portal.client.layout.PartLayout;
import com.gwtaf.portal.client.layout.PortalLayout;
import com.gwtaf.portal.client.layout.PortletStackLayout;
import com.gwtaf.portal.client.layout.PortletStackLayout.PortletInfo;
import com.gwtaf.portal.client.part.IPortalPart;
import com.gwtaf.portal.client.part.IPortalPartContext;
import com.gwtaf.portal.client.part.IPortalPartDescriptor;
import com.gwtaf.portal.client.part.PortalPartRegistry;
import com.gwtaf.portal.client.portlet.IPortlet;
import com.gwtaf.portal.client.portlet.PortletManager;
import com.gwtaf.portal.client.portlet.PortletRegistry;
import com.gwtaf.portal.client.view.IPortalViewStack;
import com.gwtaf.portal.client.view.IPortalViewStackFactory;

public class PortalFrame implements IPortalFrame {

    private final IDocumentManager documentManager;

    private final IPortletManager portletManager;

    private final SwitchPanel root = new SwitchPanel();

    private final Map<String, IPortalPart> partMap = new HashMap<String, IPortalPart>();

    private PortalFrameLayouter layout;

    private IPortalPart lastPart;

    private IPortalPart maximizedPart;

    private LayoutPanel originalLayout;

    private int originalPos;

    private final IPortalViewStackFactory viewStackFactory;

    private final class PPC implements IPortalPartContext {

        private final IPortalPart part;

        private STATE state = STATE.NORMAL;

        public PPC(IPortalPart part) {
            this.part = part;
        }

        @Override
        public void close() {
            closePart(part);
        }

        @Override
        public STATE getViewState() {
            return state;
        }

        @Override
        public void setViewState(STATE state) {
            if (!this.state.equals(state)) {
                switch(state) {
                    case MAXIMIZED:
                        if (maximizedPart != null) return;
                        if (this.state == STATE.NORMAL) {
                            maximizedPart = part;
                            originalLayout = (LayoutPanel) part.getWidget().getParent();
                            originalPos = originalLayout.getWidgetIndex(part.getWidget());
                            root.add(part.getWidget());
                            root.showWidget(1);
                            this.state = STATE.MAXIMIZED;
                            part.onChangeViewState();
                        } else if (this.state == STATE.MINIMIZED) ;
                        break;
                    case MINIMIZED:
                        if (maximizedPart != null) return;
                        if (this.state == STATE.NORMAL) {
                        } else if (this.state == STATE.MAXIMIZED) ;
                        break;
                    case NORMAL:
                        if (this.state == STATE.MAXIMIZED) {
                            originalLayout.insert(part.getWidget(), originalPos);
                            originalLayout = null;
                            maximizedPart = null;
                            root.showWidget(0);
                            this.state = STATE.NORMAL;
                            part.onChangeViewState();
                        } else if (this.state == STATE.MINIMIZED) ;
                        break;
                }
            }
        }
    }

    public PortalFrame(IPortalViewStackFactory stackFactory, List<IPortalListener> listeners) {
        this.viewStackFactory = stackFactory;
        this.documentManager = new DocumentManager(listeners);
        this.portletManager = new PortletManager(this, listeners);
        root.sinkEvents(Event.FOCUSEVENTS);
        Event.addNativePreviewHandler(new NativePreviewHandler() {

            boolean goLock = true;

            @Override
            public void onPreviewNativeEvent(NativePreviewEvent event) {
                if (goLock && (event.getTypeInt() & Event.BUTTON_LEFT | event.getTypeInt() & Event.ONFOCUS) != 0) {
                    try {
                        goLock = false;
                        EventTarget target = event.getNativeEvent().getEventTarget();
                        if (!Element.is(target)) return;
                        Element e = Element.as(target);
                        for (IPortalPart part : partMap.values()) {
                            if (part.getWidget().getElement().isOrHasChild(e)) {
                                if (!Util.iseq(part, lastPart)) {
                                    lastPart = part;
                                    if (part != null) part.setActive();
                                }
                                break;
                            }
                        }
                    } catch (Exception e) {
                        GWT.log(e.getMessage(), e);
                    } finally {
                        goLock = true;
                    }
                }
            }
        });
    }

    @Override
    public Widget getWidget() {
        return root;
    }

    @Override
    public void applyLayout(PortalLayout layout) {
        if (layout == null) layout = createDefaultLayout();
        this.layout = new PortalFrameLayouter(layout);
        root.clear();
        portletManager.clear();
        documentManager.clear();
        createParts();
        for (PortletStackLayout sl : this.layout.getPortletLayoutList()) {
            for (PortletInfo pi : sl.getInfos()) {
                if (!pi.isSpaceHolder()) {
                    IPortlet portlet = PortletRegistry.get().create(pi.getId());
                    portletManager.addPortlet(portlet, sl.getId());
                }
            }
        }
    }

    private void createParts() {
        LayoutPanel portalPanel = new LayoutPanel();
        portalPanel.setLayout(new SplitFlowLayout(true));
        for (PartLayout pl : layout.getPartLayoutList()) {
            IPortalPart pp = createPart(pl);
            IPortalPartDescriptor descriptor = PortalPartRegistry.get().lookup(pl.getId());
            int minWidth = descriptor == null ? 64 : descriptor.getMinWidth();
            int minHeight = descriptor == null ? 64 : descriptor.getMinHeight();
            pp.getWidget().setLayoutData(new FlowLayoutData(minWidth, minHeight, pl.getRatio()));
            IPortalPart refPart = partMap.get(pl.getRefId());
            LayoutPanel target = getPanel(portalPanel, pl.getRefId());
            int refIdx = refPart == null ? 0 : target.getWidgetIndex(refPart.getWidget());
            if (pl.isHorizontal() && !((FlowLayout) target.getLayout()).isHorizontal()) {
                if (target.getWidgetCount() > 1) {
                    LayoutPanel sub = new LayoutPanel();
                    sub.setLayout(new SplitFlowLayout(true));
                    if (refPart.getWidget().getLayoutData() != null) {
                        FlowLayoutData layoutData = (FlowLayoutData) refPart.getWidget().getLayoutData();
                        FlowLayoutData copy = new FlowLayoutData(layoutData.getMinWidth(), layoutData.getMinHeight(), 1.0f);
                        sub.setLayoutData(layoutData);
                        refPart.getWidget().setLayoutData(copy);
                    }
                    sub.add(refPart.getWidget());
                    target.insert(sub, refIdx);
                    target = sub;
                } else target.setLayout(new SplitFlowLayout(true));
            } else if (pl.isVertical() && ((FlowLayout) target.getLayout()).isHorizontal()) {
                if (target.getWidgetCount() > 1) {
                    LayoutPanel sub = new LayoutPanel();
                    sub.setLayout(new SplitFlowLayout(false));
                    if (refPart.getWidget().getLayoutData() != null) {
                        FlowLayoutData layoutData = (FlowLayoutData) refPart.getWidget().getLayoutData();
                        FlowLayoutData copy = new FlowLayoutData(layoutData.getMinWidth(), layoutData.getMinHeight(), 1.0f);
                        sub.setLayoutData(layoutData);
                        refPart.getWidget().setLayoutData(copy);
                    }
                    target.insert(sub, refIdx);
                    sub.add(refPart.getWidget());
                    target = sub;
                } else target.setLayout(new SplitFlowLayout(false));
            }
            if (target.getWidgetCount() > 0) {
                switch(pl.getPos()) {
                    case TOP:
                    case LEFT:
                        target.insert(pp.getWidget(), refIdx);
                        break;
                    case BOTTOM:
                    case RIGHT:
                        target.insert(pp.getWidget(), refIdx + 1);
                        break;
                }
            } else target.add(pp.getWidget());
        }
        this.root.add(portalPanel);
        this.root.showWidget(0);
    }

    private LayoutPanel getPanel(LayoutPanel root, String refId) {
        if (IPortalLayout.PORTAL.equals(refId)) return root;
        IPortalPart refPart = partMap.get(refId);
        return (LayoutPanel) refPart.getWidget().getParent();
    }

    private IPortalPart createPart(PartLayout pl) {
        try {
            IPortalPart part;
            if (pl instanceof DocumentLayout) {
                IPortalViewStack<IDocument> stack = viewStackFactory.create(pl.getId());
                documentManager.addStack(stack);
                part = stack;
            } else if (pl instanceof PortletStackLayout) {
                IPortalViewStack<IPortlet> stack = viewStackFactory.create(pl.getId());
                portletManager.addStack(stack);
                part = stack;
            } else part = PortalPartRegistry.get().create(pl.getId());
            part.init(new PPC(part));
            partMap.put(part.getId(), part);
            return part;
        } catch (Exception e) {
            GWT.log("Failed to create portal part!", e);
            BrokenPortalPart part = new BrokenPortalPart(pl.getId(), e);
            part.init(new PPC(part));
            partMap.put(pl.getId(), part);
            return part;
        }
    }

    public void closePart(IPortalPart part) {
        part.getWidget().removeFromParent();
    }

    private PortalLayout createDefaultLayout() {
        PortalLayout defaultLayout = new PortalLayout();
        defaultLayout.showDocuments(true);
        return defaultLayout;
    }

    @Override
    public IDocumentManager getDocumentManager() {
        return documentManager;
    }

    @Override
    public IPortletManager getPortletManager() {
        return portletManager;
    }

    public IPortalPart getPart(String id) {
        return partMap.get(id);
    }

    public List<PartLayout> getPartLayoutList() {
        return layout.getPartLayoutList();
    }
}
