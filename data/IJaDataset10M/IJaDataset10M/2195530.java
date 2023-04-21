package com.google.gdt.eclipse.designer.model.widgets.live;

import com.google.gdt.eclipse.designer.model.widgets.WidgetInfo;
import com.google.gdt.eclipse.designer.model.widgets.panels.RootPanelInfo;
import com.google.gdt.eclipse.designer.model.widgets.support.UIObjectUtils;
import org.eclipse.wb.core.model.AbstractComponentInfo;
import org.eclipse.wb.core.model.JavaInfo;
import org.eclipse.wb.core.model.broadcast.JavaInfoSetObjectAfter;
import org.eclipse.wb.draw2d.geometry.Dimension;
import org.eclipse.wb.draw2d.geometry.Point;
import org.eclipse.wb.draw2d.geometry.Rectangle;
import org.eclipse.wb.internal.core.model.JavaInfoUtils;
import org.eclipse.wb.internal.core.model.clipboard.ComponentInfoMemento;
import org.eclipse.wb.internal.core.model.creation.CreationSupport;
import org.eclipse.wb.internal.core.model.creation.ILiveCreationSupport;
import org.eclipse.wb.internal.core.model.util.live.AbstractLiveManager;
import org.eclipse.wb.internal.core.model.util.live.ILiveCacheEntry;
import org.eclipse.wb.internal.core.utils.check.Assert;
import org.eclipse.wb.internal.core.utils.execution.ExecutionUtils;
import org.eclipse.wb.internal.core.utils.execution.RunnableEx;
import org.eclipse.wb.internal.core.utils.ui.UiUtils;
import org.eclipse.swt.graphics.Image;
import org.apache.commons.lang.StringUtils;

/**
 * Default live components manager for GWT.
 * 
 * @author scheglov_ke
 * @coverage gwt.model
 */
public class GwtLiveManager extends AbstractLiveManager {

    protected boolean m_shouldSetSize = false;

    private final UIObjectUtils m_utils;

    public GwtLiveManager(WidgetInfo widget) {
        super(widget);
        m_utils = widget.getState().getUIObjectUtils();
    }

    @Override
    protected WidgetInfo createLiveComponent() throws Exception {
        m_utils.setLiveManager(true);
        RootPanelInfo panel = (RootPanelInfo) parse(new String[] { "  com.google.gwt.user.client.ui.RootPanel __wbp_panel = com.google.gwt.user.client.ui.RootPanel.get();", "  __wbp_panel.setPixelSize(800, 600);" });
        final WidgetInfo widget = createClone();
        widget.addBroadcastListener(new JavaInfoSetObjectAfter() {

            public void invoke(JavaInfo target, Object o) throws Exception {
                if (target == widget) {
                    widget.removeBroadcastListener(this);
                    m_utils.setLiveWidget(o);
                }
            }
        });
        panel.command_CREATE2(widget, null);
        panel.command_BOUNDS(widget, new Point(100, 100), null);
        panel.getPropertyByTitle("title").setValue(null);
        {
            String width = getForcedWidth();
            String height = getForcedHeight();
            if (hasForcedSizePart(width) && hasForcedSizePart(height)) {
                m_shouldSetSize = true;
                widget.getSizeSupport().setSize(width, height);
            }
        }
        return widget;
    }

    @Override
    protected void cleanupLiveComponent(AbstractComponentInfo liveComponentInfo) throws Exception {
        super.cleanupLiveComponent(liveComponentInfo);
        m_utils.setLiveWidget(null);
        m_utils.setLiveManager(false);
    }

    @Override
    protected ILiveCacheEntry createComponentCacheEntry(AbstractComponentInfo component) {
        WidgetInfo widget = (WidgetInfo) component;
        setDefaultSizeIfEmpty(widget);
        GwtLiveCacheEntry entry = new GwtLiveCacheEntry();
        entry.setImage(getWidgetImage(widget));
        entry.shouldSetSize(m_shouldSetSize);
        return entry;
    }

    /**
   * If widget has no size and no forced size (i.e. most probably custom component), then set some
   * reasonable default size.
   */
    private void setDefaultSizeIfEmpty(final WidgetInfo widget) {
        Rectangle bounds = widget.getBounds();
        if (bounds.width == 0 || bounds.height == 0) {
            m_shouldSetSize = true;
            ExecutionUtils.run(widget, new RunnableEx() {

                public void run() throws Exception {
                    widget.getSizeSupport().setSize(100, 100);
                }
            });
        }
    }

    /**
   * @return the {@link Image} of {@link WidgetInfo} that is direct child of <code>RootPanel</code>
   *         .
   */
    private Image getWidgetImage(WidgetInfo widget) {
        Rectangle bounds = widget.getAbsoluteBounds();
        if (bounds.width == 0 || bounds.height == 0) {
            Dimension forcedSize = getForcedSize();
            return new Image(null, forcedSize.width, forcedSize.height);
        } else {
            RootPanelInfo rootPanel = (RootPanelInfo) widget.getRootJava();
            return UiUtils.getCroppedImage(rootPanel.getImage(), bounds.getSwtRectangle());
        }
    }

    @Override
    protected ILiveCacheEntry createComponentCacheEntryEx(Throwable e) {
        GwtLiveCacheEntry cacheEntry = new GwtLiveCacheEntry();
        {
            Image image = createImageForException(e);
            cacheEntry.setImage(image);
        }
        return cacheEntry;
    }

    private String getForcedWidth() {
        return getForcedSizePart("width");
    }

    private String getForcedHeight() {
        return getForcedSizePart("height");
    }

    private String getForcedSizePart(String name) {
        if (!useForcedSize()) {
            return null;
        }
        return m_component.getDescription().getParameter("liveComponent.forcedSize." + name);
    }

    /**
   * @return <code>true</code> if specified forced size should be used.
   */
    private boolean useForcedSize() {
        String script = m_component.getDescription().getParameter("liveComponent.forcedSize.use");
        if (StringUtils.isEmpty(script)) {
            return true;
        }
        return m_utils.evaluateScriptBoolean(script);
    }

    private static boolean hasForcedSizePart(String size) {
        return !StringUtils.isEmpty(size);
    }

    /**
   * @return the forced size from description.
   */
    private Dimension getForcedSize() {
        String width = getForcedWidth();
        String height = getForcedHeight();
        Assert.isTrue(hasForcedSizePart(width) && hasForcedSizePart(height), "Forced size was requested, but no liveComponent.forcedSize.width/height specified.");
        int w = parseSize(width);
        int h = parseSize(height);
        return new Dimension(w, h);
    }

    /**
   * @return the integer value of size {@link String}, that should be some integer like
   *         <code>100</code> or pixel value <code>100px</code> (preferable).
   */
    private static int parseSize(String size) {
        if (size.endsWith("px")) {
            size = size.replaceAll("px", "").trim();
        }
        return Integer.parseInt(size);
    }

    /**
   * @return the copy of "live" {@link Image} of component, must be disposed by caller.
   */
    public Image getImage() {
        {
            Image image = ComponentInfoMemento.getImage(m_component);
            if (image != null) {
                return image;
            }
        }
        return getEntry().getImage();
    }

    /**
   * See {@link WidgetInfo#shouldSetReasonableSize()}.
   */
    public boolean shouldSetSize() {
        return getEntry().shouldSetSize();
    }

    /**
   * Specifies "live" {@link WidgetInfo} properties externally, to avoid parsing (when
   * {@link CreationSupport} is not {@link ILiveCreationSupport}).
   */
    public static void setEntry(WidgetInfo widget, Image image, boolean shouldSetSize) {
        GwtLiveCacheEntry entry = new GwtLiveCacheEntry();
        entry.setImage(image);
        entry.shouldSetSize(shouldSetSize);
        widget.putArbitraryValue(GwtLiveCacheEntry.class, entry);
    }

    private GwtLiveCacheEntry getEntry() {
        {
            GwtLiveCacheEntry entry = (GwtLiveCacheEntry) m_component.getArbitraryValue(GwtLiveCacheEntry.class);
            if (entry != null) {
                return entry;
            }
        }
        if (JavaInfoUtils.hasTrueParameter(m_component, "liveComponent.no")) {
            Dimension forcedSize = getForcedSize();
            setEntry((WidgetInfo) m_component, new Image(null, forcedSize.width, forcedSize.height), false);
            return getEntry();
        }
        return (GwtLiveCacheEntry) getCachedEntry();
    }
}
