package com.google.gwt.user.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CommonResources;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.ImportedWithPrefix;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;

/**
 * A vertical scrollbar implemented using the browsers native scrollbar.
 */
public class NativeVerticalScrollbar extends AbstractNativeScrollbar implements VerticalScrollbar {

    interface NativeVerticalScrollbarUiBinder extends UiBinder<Element, NativeVerticalScrollbar> {
    }

    /**
   * A ClientBundle of resources used by this widget.
   */
    public interface Resources extends ClientBundle {

        /**
     * The styles used in this widget.
     */
        @Source(Style.DEFAULT_CSS)
        Style nativeVerticalScrollbarStyle();
    }

    /**
   * A variation of {@link Resources} that renders the scrollbar
   * semi-transparent until it is hovered.
   */
    public interface ResourcesTransparant extends Resources {

        /**
     * The styles used in this widget.
     */
        @Source(StyleTransparant.DEFAULT_CSS)
        Style nativeVerticalScrollbarStyle();
    }

    /**
   * Styles used by this widget.
   */
    @ImportedWithPrefix("gwt-NativeVerticalScrollbar")
    public interface Style extends CssResource {

        /**
     * The path to the default CSS styles used by this resource.
     */
        String DEFAULT_CSS = "com/google/gwt/user/client/ui/NativeVerticalScrollbar.css";

        /**
     * Applied to the scrollbar.
     */
        String nativeVerticalScrollbar();
    }

    /**
   * A variation of {@link Style} that renders the scrollbar semi-transparent
   * until it is hovered.
   */
    public interface StyleTransparant extends Style {

        /**
     * The path to the default CSS styles used by this resource.
     */
        String DEFAULT_CSS = "com/google/gwt/user/client/ui/NativeVerticalScrollbarTransparent.css";
    }

    private static Resources DEFAULT_RESOURCES;

    private static NativeVerticalScrollbarUiBinder uiBinder = GWT.create(NativeVerticalScrollbarUiBinder.class);

    /**
   * Get the default resources for this widget.
   */
    private static Resources getDefaultResources() {
        if (DEFAULT_RESOURCES == null) {
            DEFAULT_RESOURCES = GWT.create(Resources.class);
        }
        return DEFAULT_RESOURCES;
    }

    /**
   * The div inside the scrollable div that forces scrollbars to appear.
   */
    @UiField
    Element contentDiv;

    /**
   * The scrollable div used to create a scrollbar.
   */
    @UiField
    Element scrollable;

    /**
   * Construct a new {@link NativeVerticalScrollbar}.
   */
    public NativeVerticalScrollbar() {
        this(getDefaultResources());
    }

    /**
   * Construct a new {@link NativeVerticalScrollbar}.
   * 
   * @param resources the resources used by this widget
   */
    public NativeVerticalScrollbar(Resources resources) {
        setElement(uiBinder.createAndBindUi(this));
        getElement().addClassName(CommonResources.getInlineBlockStyle());
        setWidth(getNativeWidth() + "px");
        Style style = resources.nativeVerticalScrollbarStyle();
        style.ensureInjected();
        getScrollableElement().addClassName(style.nativeVerticalScrollbar());
        ScrollImpl.get().initialize(scrollable, contentDiv);
    }

    public int getMaximumVerticalScrollPosition() {
        return getScrollableElement().getScrollHeight() - getElement().getClientHeight();
    }

    public int getMinimumVerticalScrollPosition() {
        return 0;
    }

    /**
   * Get the height in pixels of the scrollable content that the scrollbar
   * controls.
   * 
   * <p>
   * This is not the same as the maximum scroll top position. The maximum scroll
   * position equals the <code>scrollHeight- offsetHeight</code>;
   * 
   * @return the scroll height
   * @see #setScrollHeight(int)
   */
    public int getScrollHeight() {
        return contentDiv.getOffsetHeight();
    }

    public int getVerticalScrollPosition() {
        return getScrollableElement().getScrollTop();
    }

    /**
   * Set the height in pixels of the scrollable content that the scrollbar
   * controls.
   * 
   * <p>
   * This is not the same as the maximum scroll top position. The maximum scroll
   * position equals the <code>scrollHeight- offsetHeight</code>;
   * 
   * @param height the size height pixels
   */
    public void setScrollHeight(int height) {
        contentDiv.getStyle().setHeight(height, Unit.PX);
    }

    public void setVerticalScrollPosition(int position) {
        getScrollableElement().setScrollTop(position);
    }

    /**
   * Get the width of the scrollbar.
   * 
   * @return the width of the scrollbar in pixels
   */
    protected int getNativeWidth() {
        return getNativeScrollbarWidth();
    }

    @Override
    protected Element getScrollableElement() {
        return scrollable;
    }
}
