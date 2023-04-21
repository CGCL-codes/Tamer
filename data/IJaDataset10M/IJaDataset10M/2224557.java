package org.gwtoolbox.widget.client.panel.layout.tab;

import com.google.gwt.animation.client.Animation;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style;
import com.google.gwt.event.logical.shared.*;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.*;
import com.google.gwt.user.client.ui.RequiresResize;
import com.google.gwt.user.client.ui.Widget;
import org.gwtoolbox.commons.ui.client.event.HoverHandler;
import org.gwtoolbox.commons.ui.client.event.ContextMenuHandler;
import org.gwtoolbox.commons.ui.client.event.EventUtils;
import org.gwtoolbox.commons.ui.client.event.Handler;
import org.gwtoolbox.widget.client.menu.MenuBuilder;
import org.gwtoolbox.widget.client.menu.MenuPopup;
import org.gwtoolbox.widget.client.panel.LayoutUtils;
import org.gwtoolbox.widget.client.popup.Popup;
import java.util.*;
import static org.gwtoolbox.commons.ui.client.event.EventUtils.addContextMenuListener;
import static org.gwtoolbox.commons.ui.client.event.EventUtils.addOnClickHandler;
import static org.gwtoolbox.widget.client.panel.LayoutUtils.preventBoxStyles;

/**
 * The top bar of the TabLayout where all the tabs are desplayed and where the user can interact with them. This bar is
 * more than just a simple container for tab elements, it also provides scrolling for tabs once they effect out of space.
 * There is no real reason for the user to directly interact with this class as it's only purpose is to serve the
 * {@code TabLayout}.
 *
 * @author Uri Boness
 */
public class TabBar extends Widget implements RequiresResize, HasSelectionHandlers<TabBar.Tab>, HasCloseHandlers<TabBar.Tab> {

    private static final int SCROLL_BUTTON_WIDTH = 18;

    private static final int TAB_HORIZONTAL_MARGIN = 2;

    private static final int SCROLL_JUMP = 100;

    private Impl impl = GWT.create(Impl.class);

    private Element container;

    private Element leftScrollButton;

    private Element tabsViewportElement;

    private Element tabsElement;

    private Element rightScrollButton;

    private List<Tab> tabs = new ArrayList<Tab>();

    private Map<String, Tab> tabById = new HashMap<String, Tab>();

    private Tab selectedTab;

    private ScrollStrategy scrollStrategy = new StaticScrollStrategy(SCROLL_JUMP);

    private boolean showDefaultContextMenu;

    /**
     * Constructs a new TabBar. This method is kept package private as it only serves the {@link TabLayout} class.
     */
    TabBar() {
        impl.init(this);
        Element main = preventBoxStyles(DOM.createDiv());
        setElement(main);
        container = DOM.createDiv();
        container.getStyle().setPosition(Style.Position.RELATIVE);
        DOM.setStyleAttribute(container, "height", impl.getContainerHeight());
        DOM.setStyleAttribute(container, "borderBottomWidth", "1px");
        DOM.setStyleAttribute(container, "borderBottomStyle", "solid");
        DOM.setStyleAttribute(container, "overflow", "hidden");
        DOM.setStyleAttribute(container, "margin", "0");
        DOM.setStyleAttribute(container, "padding", "0 0 2px 0");
        DOM.setStyleAttribute(container, "position", "relative");
        DOM.setStyleAttribute(container, "left", "0");
        DOM.setStyleAttribute(container, "top", "0");
        container.setClassName("SimpleTabBar");
        main.appendChild(container);
        leftScrollButton = DOM.createDiv();
        leftScrollButton.setClassName("tabs-left-scroll-button");
        DOM.appendChild(container, leftScrollButton);
        setVisible(leftScrollButton, false);
        DOM.setStyleAttribute(leftScrollButton, "position", "absolute");
        DOM.setStyleAttribute(leftScrollButton, "left", "0");
        DOM.setStyleAttribute(leftScrollButton, "top", "0");
        DOM.setStyleAttribute(leftScrollButton, "width", "18px");
        DOM.setStyleAttribute(leftScrollButton, "height", "23px");
        DOM.setStyleAttribute(leftScrollButton, "borderBottomWidth", "1px");
        DOM.setStyleAttribute(leftScrollButton, "borderBottomStyle", "solid");
        DOM.setStyleAttribute(leftScrollButton, "cursor", "pointer");
        addOnClickHandler(leftScrollButton, new Handler<Event>() {

            public void handle(Event event) {
                scrollLeft();
            }
        });
        rightScrollButton = DOM.createDiv();
        rightScrollButton.setClassName("tabs-right-scroll-button");
        DOM.appendChild(container, rightScrollButton);
        setVisible(rightScrollButton, false);
        DOM.setStyleAttribute(rightScrollButton, "position", "absolute");
        DOM.setStyleAttribute(rightScrollButton, "top", "0");
        DOM.setStyleAttribute(rightScrollButton, "width", "18px");
        DOM.setStyleAttribute(rightScrollButton, "height", "23px");
        DOM.setStyleAttribute(rightScrollButton, "borderBottomWidth", "1px");
        DOM.setStyleAttribute(rightScrollButton, "borderBottomStyle", "solid");
        DOM.setStyleAttribute(rightScrollButton, "cursor", "pointer");
        addOnClickHandler(rightScrollButton, new Handler<Event>() {

            public void handle(Event event) {
                scrollRight();
            }
        });
        tabsViewportElement = DOM.createDiv();
        tabsViewportElement.setClassName("tabs-viewport");
        DOM.setStyleAttribute(tabsViewportElement, "position", "absolute");
        DOM.setStyleAttribute(tabsViewportElement, "left", "0px");
        DOM.setStyleAttribute(tabsViewportElement, "height", impl.getTabsViewportHeight());
        DOM.setStyleAttribute(tabsViewportElement, "overflow", "hidden");
        DOM.setStyleAttribute(tabsViewportElement, "padding", "2px 0 0 0");
        DOM.appendChild(container, tabsViewportElement);
        tabsElement = DOM.createElement("ul");
        tabsElement.setClassName("tabs");
        DOM.appendChild(tabsViewportElement, tabsElement);
        DOM.setStyleAttribute(tabsElement, "display", "block");
        DOM.setStyleAttribute(tabsElement, "position", "relative");
        DOM.setStyleAttribute(tabsElement, "width", "4000px");
        DOM.setStyleAttribute(tabsElement, "height", "20px");
        DOM.setStyleAttribute(tabsElement, "borderBottomWidth", "1px");
        DOM.setStyleAttribute(tabsElement, "borderBottomStyle", "solid");
        DOM.setStyleAttribute(tabsElement, "listStyleImage", "none");
        DOM.setStyleAttribute(tabsElement, "listStylePosition", "outside");
        DOM.setStyleAttribute(tabsElement, "listStyleType", "none");
        DOM.setStyleAttribute(tabsElement, "margin", "0");
        DOM.setStyleAttribute(tabsElement, "padding", "1px 0 0 0");
    }

    /**
     * Adds a new tab to this bar with the given text. Since every tab be associated with a unique id, the text here
     * will also serve as the id.
     *
     * @param text The text of the tab.
     */
    public void add(String text) {
        add(text, false);
    }

    /**
     * Adds a new tab to this bar with the given id and text.
     *
     * @param id The id of the tab.
     * @param text The text of the tab.
     */
    public void add(String id, String text) {
        add(id, text, false);
    }

    /**
     * Adds a new tab to this bar with the given text. The text also serves as the unique id of the new tab.
     *
     * @param text The text of the tab.
     * @param closable Indicates whether the tab can be closed by the user. When {@code true} a small close button will
     * appear on it where the user can click and close it.
     */
    public void add(String text, boolean closable) {
        add(text, text, closable);
    }

    /**
     * Adds a new tab to this bar with the given id and text.
     *
     * @param id The id of the tab.
     * @param text The text of the tab.
     * @param closable Indicates whether the tab can be closed by the user. When {@code true} a small close button will
     * appear on it where the user can click and close it.
     */
    public void add(String id, String text, boolean closable) {
        TabSpec spec = new TabSpec(id).setText(text).setClosable(closable);
        addTab(spec);
    }

    /**
     * Returns the index of the tab that is associated with the given id.
     *
     * @param id The id of the tab.
     * @return The index (zero-based) of the identified tab, or {@code -1} if no tab is associated with the given id.
     */
    public int getTabIndex(String id) {
        Tab tab = tabById.get(id);
        return tabs.indexOf(tab);
    }

    /**
     * Returns the id of the tab which is located on the given index (zero-based).
     *
     * @param index The index of the tab.
     * @return The ide of the tab which is located on the given index.
     * @throws IndexOutOfBoundsException when the given index is outside of the bounds of the tab list.
     */
    public String getTabId(int index) {
        Tab tab = tabs.get(index);
        return tab.id;
    }

    /**
     * Adds a new tab to this bar. For more details on the characteristics of the newly added tab, check out the
     * the documentation of {@link TabSpec}. The new tab will be will be added to the end of the tab list.
     *
     * @param tabSpec The specification for the newly added tab.
     */
    public void addTab(TabSpec tabSpec) {
        insertTab(tabSpec, getTabCount());
    }

    /**
     * Adds a new tab to this bar. The new tab will be based on the given tab specification and will be inserted in the
     * specified index.
     *
     * @param tabSpec The tab specification base on which the tab will be created.
     * @param index The index in which the tab should be added.
     */
    public void insertTab(TabSpec tabSpec, int index) {
        assert tabSpec.getText() != null;
        final Element li = DOM.createElement("li");
        li.setClassName("tab");
        Element closeButtonElement = null;
        if (tabSpec.isClosable()) {
            closeButtonElement = DOM.createDiv();
            closeButtonElement.setClassName("tab-close-button");
            closeButtonElement.setInnerHTML("&nbsp;");
            DOM.appendChild(li, closeButtonElement);
        }
        Element leftDiv = DOM.createDiv();
        leftDiv.setClassName("tab-left");
        DOM.appendChild(li, leftDiv);
        Element rightDiv = DOM.createDiv();
        rightDiv.setClassName("tab-right");
        DOM.appendChild(leftDiv, rightDiv);
        Element centerSpan = DOM.createSpan();
        centerSpan.setClassName("tab-center");
        Integer width = tabSpec.getWidth();
        if (width == null) {
            width = impl.getTextWidth(tabSpec.getText(), "tab-text-measurement");
        }
        DOM.setStyleAttribute(centerSpan, "width", width + "px");
        DOM.appendChild(rightDiv, centerSpan);
        Element textSpan = DOM.createSpan();
        textSpan.setClassName("tab-text");
        textSpan.setInnerText(tabSpec.getText());
        DOM.appendChild(centerSpan, textSpan);
        final Tab tab = new Tab(tabSpec, li, textSpan, closeButtonElement);
        if (index > -1) {
            tabs.add(index, tab);
            tab.setIndex(index);
        } else {
            tabs.add(tab);
            tab.setIndex(tabs.size() - 1);
        }
        tabById.put(tabSpec.getId(), tab);
        if (index + 1 > tabsElement.getChildCount()) {
            tabsElement.appendChild(li);
        } else {
            tabsElement.insertBefore(li, tabsElement.getChild(index));
        }
        if (isAttached()) {
            impl.layout();
        }
    }

    /**
     * Adds tab selection handler to this bar. Will be notified whenever a tab is un/selected. When a tab is
     * unselected, the selection handler will hold {@code null} as the selected value.
     *
     * @param tabSelectionHandler The selection hadler to register.
     * @return The associated handler reigstration.
     */
    public HandlerRegistration addSelectionHandler(SelectionHandler<Tab> tabSelectionHandler) {
        return addHandler(tabSelectionHandler, SelectionEvent.getType());
    }

    /**
     * Adds a tab close handler to this bar. Will be notified whenever a tab is closed by the user (by clicking on the
     * close button of the tab if available).
     *
     * @param tabCloseHandler The close handler to register.
     * @return The associated handler registration.
     */
    public HandlerRegistration addCloseHandler(CloseHandler<Tab> tabCloseHandler) {
        return addHandler(tabCloseHandler, CloseEvent.getType());
    }

    /**
     * Returns the currently selected tab, or {@code null} if no tab is currently selected.
     *
     * @return The currently selected tab, or {@code null} if no tab is currently selected.
     */
    public Tab getSelectedTab() {
        return selectedTab;
    }

    /**
     * Returns the index of the currently selected tab, or {@code -1} if no tab is currently selected.
     *
     * @return The index of the currently selected tab, or {@code -1} if no tab is currently selected.
     */
    public int getSelectedTabIndex() {
        return tabs.indexOf(selectedTab);
    }

    /**
     * Selects the tab that is associated with the given id.
     *
     * @param id The id of the tab to select.
     */
    public void selectTab(String id) {
        Tab tab = tabById.get(id);
        if (tab != null) {
            tab.select();
        }
    }

    /**
     * Returns the number of tabs in this bar.
     *
     * @return The number of tabs in this bar.
     */
    public int getTabCount() {
        return tabs.size();
    }

    /**
     * Sets whether the tab that is associated with the given id is enabled/disabled.
     *
     * @param id The id of the tab.
     * @param enabled When {@code true} The tab that is associated with the give id will be enabled, otherwise it will
     * be disabled.
     */
    public void setTabEnabled(String id, boolean enabled) {
        tabById.get(id).setEnabled(enabled);
    }

    /**
     * Indicates whether the tab that is associated with the given id is enabled.
     *
     * @param id The id of the tab.
     * @return Whether the tab that is associated with the given id is enabled.
     */
    public boolean isTabEnabled(String id) {
        return tabById.get(id).isEnabled();
    }

    public void setTabHighlighted(String id, boolean highlight) {
        tabById.get(id).setHighlight(highlight);
    }

    public void renameTab(String id, String newName) {
        tabById.get(id).setText(newName);
    }

    /**
     * Removes and returns the tab that is associated with the given id. If no tab is associated with the given id, no
     * action will be taken.
     *
     * @param id The id of the tab that will be removed.
     * @return The removed tab.
     */
    public Tab remove(String id) {
        Tab tab = tabById.remove(id);
        if (tab == null) {
            return null;
        }
        int index = tabs.indexOf(tab);
        tabs.remove(tab);
        boolean wasSelected = selectedTab != null && selectedTab == tab;
        if (wasSelected) {
            selectedTab.unselect();
        }
        DOM.removeChild(tabsElement, tab.tabElement);
        impl.layout();
        if (wasSelected) {
            selectNextTab(index);
        }
        return tab;
    }

    /**
     * Clears all the tabs from this bar.
     */
    public void clear() {
        tabById.clear();
        tabs.clear();
        selectedTab = null;
        tabsElement.setInnerHTML("");
    }

    /**
     * @see {@link com.google.gwt.user.client.ui.RequiresResize#onResize()}
     */
    public void onResize() {
        impl.layout();
    }

    @Override
    protected void onLoad() {
        impl.onAttach();
    }

    @Override
    protected void onUnload() {
        impl.onDetach();
    }

    /**
     * Defines the size of the scroll jump when the user clicks on one of the scroll handles.
     *
     * @param jump The scroll jump.
     */
    public void setScrollJump(int jump) {
        scrollStrategy = new StaticScrollStrategy(jump);
    }

    /**
     * Sets the scroll jump to equal the smallest tab on the bar. This will make sure that the scroll jumps are not
     * big enough for some tabs to prevent situations where during a jump a tab is "kicked-off" from the bar view port.
     */
    public void setScrollJumpToSmallestTab() {
        scrollStrategy = new SmallestTabScrollStrategy();
        if (isAttached()) {
            scrollStrategy.update(this);
        }
    }

    /**
     * Sets the scroll jump to equal the largest tab on the bar. This will can be used to prevent really small and
     * annoying jumps.
     */
    public void setScrollJumpToLargestTab() {
        scrollStrategy = new LargestTabScrollStrategy();
        if (isAttached()) {
            scrollStrategy.update(this);
        }
    }

    /**
     * Returns whether tabs on this bar has the default context menu.
     *
     * @return Wether tabs on this bar has the default context menu.
     * @see #setShowDefaultContextMenu(boolean)
     */
    public boolean isShowDefaultContextMenu() {
        return showDefaultContextMenu;
    }

    /**
     * Sets whether the tabs on this bar have the default context menu. The default context menu will contain the following
     * menu items:
     * <ul>
     *  <li>Select - will select the tab. This will only be enabled if the tab is enabled (that is,
     *              {@link #isTabEnabled(String)} returns {@code true}</li>
     *  <li>Close - will close the tab. This will only be enabled if the tab is closable (see {@link TabSpec#setClosable(boolean)}</li>
     *  <li>Close Others - Will always be enabled and will close all other tabs that are closable.</li>
     * </ul>
     *
     * The default context menu items will always show on the top. If the tab also has a custom menu builder, the menu
     * builder will add it's items to the context menu under the default menu items (separated by a separator).
     *
     * @param showDefaultContextMenu Whether the tabs on this bar have the default context menu.
     */
    public void setShowDefaultContextMenu(boolean showDefaultContextMenu) {
        this.showDefaultContextMenu = showDefaultContextMenu;
    }

    private void scrollLeft() {
        int left = tabsElement.getOffsetLeft();
        int targetLeft = 0;
        if (left <= -scrollStrategy.getScrollJump()) {
            targetLeft = left + scrollStrategy.getScrollJump();
        }
        new SlideLeftAnimation(left, targetLeft, tabsElement).start();
    }

    private void scrollRight() {
        Element lastTabElement = tabs.get(tabs.size() - 1).tabElement;
        int lastTabRight = lastTabElement.getAbsoluteLeft() + lastTabElement.getOffsetWidth();
        int tabsElementRight = tabsViewportElement.getAbsoluteLeft() + tabsViewportElement.getOffsetWidth();
        int delta = lastTabRight - tabsElementRight;
        int left = tabsElement.getOffsetLeft();
        int targetLeft;
        if (delta < scrollStrategy.getScrollJump()) {
            targetLeft = tabsElement.getOffsetLeft() - delta - TAB_HORIZONTAL_MARGIN;
        } else {
            targetLeft = tabsElement.getOffsetLeft() - scrollStrategy.getScrollJump();
        }
        new SlideLeftAnimation(left, targetLeft, tabsElement).start();
    }

    /**
     * Selects the next tab after the tab in the given index was closed.
     *
     * @param index The index of the tab that was closed.
     */
    void selectNextTab(int index) {
        int originalIndex = index;
        int tabCount = getTabCount();
        if (tabCount == 0) {
            return;
        }
        if (tabCount <= index) {
            index = tabCount - 1;
        }
        String id = null;
        while (index < tabCount) {
            id = getTabId(index);
            if (isTabEnabled(id)) {
                break;
            }
            index++;
        }
        if (isTabEnabled(id)) {
            selectTab(id);
            return;
        }
        index = originalIndex;
        while (index >= 0) {
            id = getTabId(index);
            if (isTabEnabled(id)) {
                selectTab(id);
            }
            index--;
        }
    }

    private class SlideLeftAnimation extends Animation {

        private int from;

        private int to;

        private Element element;

        private final int delta;

        private SlideLeftAnimation(int from, int to, Element element) {
            this.from = from;
            this.to = to;
            this.element = element;
            delta = to - from;
        }

        protected void onUpdate(double progress) {
            if (progress == 1.0) {
                element.getStyle().setLeft(to, Style.Unit.PX);
                return;
            }
            int interval = (int) (progress * delta);
            element.getStyle().setLeft(from + interval, Style.Unit.PX);
        }

        public void start() {
            run(300);
        }
    }

    private static class Impl {

        protected TabBar bar;

        public void init(TabBar panel) {
            this.bar = panel;
        }

        public String getContainerHeight() {
            return "25px";
        }

        public String getTabsViewportHeight() {
            return "22px";
        }

        public void onAttach() {
            layout();
            DeferredCommand.addCommand(new Command() {

                public void execute() {
                    layout();
                }
            });
        }

        public void onDetach() {
        }

        public void layout() {
            bar.setVisible(true);
            int containerHeight = bar.container.getOffsetHeight();
            if (containerHeight == 0) {
                return;
            }
            if (bar.tabs.isEmpty()) {
                bar.tabsViewportElement.getStyle().setLeft(0, Style.Unit.PX);
                bar.tabsViewportElement.getStyle().setWidth(100, Style.Unit.PCT);
                setVisible(bar.leftScrollButton, false);
                setVisible(bar.rightScrollButton, false);
                return;
            }
            bar.tabsElement.getStyle().setLeft(0, Style.Unit.PX);
            Element lastTabElement = bar.tabs.get(bar.tabs.size() - 1).tabElement;
            int lastTabLeft = lastTabElement.getAbsoluteLeft() - bar.tabsElement.getAbsoluteLeft();
            int tabsWidth = lastTabLeft + lastTabElement.getOffsetWidth();
            int containerWidth = bar.container.getOffsetWidth();
            if (tabsWidth > containerWidth) {
                bar.tabsViewportElement.getStyle().setLeft(SCROLL_BUTTON_WIDTH, Style.Unit.PX);
                bar.tabsViewportElement.getStyle().setWidth(containerWidth - 2 * SCROLL_BUTTON_WIDTH, Style.Unit.PX);
                setVisible(bar.leftScrollButton, true);
                bar.rightScrollButton.getStyle().setLeft(containerWidth - SCROLL_BUTTON_WIDTH, Style.Unit.PX);
                setVisible(bar.rightScrollButton, true);
            } else {
                bar.tabsViewportElement.getStyle().setLeft(0, Style.Unit.PX);
                bar.tabsViewportElement.getStyle().setWidth(100, Style.Unit.PCT);
                setVisible(bar.leftScrollButton, false);
                setVisible(bar.rightScrollButton, false);
            }
            bar.scrollStrategy.update(bar);
        }

        public int getTextWidth(String text, String className) {
            return LayoutUtils.getTextWidth(text, className);
        }
    }

    private static class ImplIE6 extends Impl {

        @Override
        public String getContainerHeight() {
            return "27px";
        }

        @Override
        public String getTabsViewportHeight() {
            return "23px";
        }

        public void onAttach() {
            addResizeListener(bar.container);
            layout();
        }

        public void onDetach() {
            DOM.setElementProperty(bar.container, "onresize", null);
        }

        private native void addResizeListener(Element container);

        @Override
        public int getTextWidth(String text, String className) {
            int width = super.getTextWidth(text, className);
            width += 26;
            return width;
        }
    }

    public class Tab {

        private String id;

        private String text;

        private int index;

        private Element tabElement;

        private Element textElement;

        private Element closeButtonElement;

        private boolean enabled = true;

        private TabSpec tabSpec;

        private boolean highlighted = false;

        private Tab(final TabSpec tabSpec, Element tabElement, Element textElement, final Element closeButtonElement) {
            this.id = tabSpec.getId();
            this.text = tabSpec.getText();
            this.tabElement = tabElement;
            this.textElement = textElement;
            this.closeButtonElement = closeButtonElement;
            this.tabSpec = tabSpec;
            addOnClickHandler(tabElement, new Handler<Event>() {

                public void handle(Event event) {
                    handleOnClick();
                }
            });
            EventUtils.addMouseHoverHandler(tabElement, new HoverHandler() {

                public void onMouseOver(Event event) {
                    handleMouseOver();
                }

                public void onMouseOut(Event event) {
                    handleMouseOut();
                }
            });
            if (closeButtonElement != null) {
                addOnClickHandler(closeButtonElement, new Handler<Event>() {

                    public void handle(Event event) {
                        handleOnClose();
                        DOM.eventCancelBubble(event, true);
                    }
                });
                EventUtils.addMouseHoverHandler(closeButtonElement, new HoverHandler() {

                    public void onMouseOver(Event event) {
                        setStyleName(closeButtonElement, "tab-close-button-hover", true);
                    }

                    public void onMouseOut(Event event) {
                        setStyleName(closeButtonElement, "tab-close-button-hover", false);
                    }
                });
            }
            final MenuBuilder menuBuilder = tabSpec.getMenuBuilder();
            if (menuBuilder != null || showDefaultContextMenu) {
                ContextMenuHandler handler = new ContextMenuHandler() {

                    public void onContextMenu(final com.google.gwt.dom.client.Element element, final int x, final int y) {
                        final MenuPopup menu = new MenuPopup(true);
                        if (showDefaultContextMenu) {
                            if (!isSelected()) {
                                menu.addItem("Select", new Command() {

                                    public void execute() {
                                        select();
                                    }
                                }).setEnabled(enabled);
                            }
                            if (closeButtonElement != null) {
                                menu.addItem("Close", new Command() {

                                    public void execute() {
                                        handleOnClose();
                                    }
                                }).setEnabled(enabled);
                            }
                            menu.addItem("Close Others", new Command() {

                                public void execute() {
                                    handleCloseOthers();
                                }
                            }).setEnabled(enabled);
                        }
                        if (menuBuilder != null) {
                            if (showDefaultContextMenu) {
                                menu.addSeparator();
                            }
                            menuBuilder.build(menu.getMenu());
                        }
                        menu.setPopupPositionAndShow(new Popup.PositionCallback() {

                            public void setPosition(int offsetWidth, int offsetHeight) {
                                menu.setPopupPosition(element.getAbsoluteLeft() + x, element.getAbsoluteTop() + y);
                            }
                        });
                    }
                };
                addContextMenuListener(tabElement, handler);
            }
        }

        public boolean isEnabled() {
            return enabled;
        }

        public boolean isSelected() {
            return this == selectedTab;
        }

        public String getId() {
            return id;
        }

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
            tabSpec.setText(text);
            Integer width = tabSpec.getWidth();
            if (width == null) {
                width = impl.getTextWidth(text, "tab-text-measurement");
            }
            Element centerSpan = textElement.getParentElement().cast();
            DOM.setStyleAttribute(centerSpan, "width", width + "px");
            textElement.setInnerText(text);
            impl.layout();
        }

        public int getIndex() {
            return index;
        }

        private void setIndex(int index) {
            this.index = index;
        }

        private void setEnabled(boolean enabled) {
            this.enabled = enabled;
            if (isSelected() && !enabled) {
                unselect();
            }
            markEnabled(enabled);
        }

        private void handleMouseOver() {
            if (!isSelected() && enabled) {
                if (highlighted) {
                    setStyleName(textElement, "tab-text-highlighted-hover", true);
                } else {
                    setStyleName(textElement, "tab-text-hover", true);
                }
            }
        }

        private void handleMouseOut() {
            if (!isSelected() && enabled) {
                setStyleName(textElement, "tab-text-highlighted-hover", false);
                setStyleName(textElement, "tab-text-hover", false);
            }
        }

        private void handleOnClick() {
            if (enabled) {
                select();
            }
        }

        private void handleOnClose() {
            if (enabled) {
                Tab tab = remove(id);
                CloseEvent.fire(TabBar.this, tab);
            }
        }

        private void handleCloseOthers() {
            Set<Tab> toBeClosed = new HashSet<Tab>();
            for (Tab tab : tabs) {
                if (tab != this && tab.isClosable()) {
                    toBeClosed.add(tab);
                }
            }
            for (Tab tab : toBeClosed) {
                tab.handleOnClose();
            }
        }

        public boolean isClosable() {
            return closeButtonElement != null;
        }

        public void select() {
            if (selectedTab != null) {
                selectedTab.markSelected(false);
            }
            selectedTab = this;
            selectedTab.markSelected(true);
            SelectionEvent.fire(TabBar.this, this);
        }

        public void unselect() {
            markSelected(false);
            selectedTab = null;
            SelectionEvent.fire(TabBar.this, null);
        }

        private void markSelected(boolean selected) {
            if (selected) {
                tabElement.setClassName("tab-selected");
            } else {
                tabElement.setClassName("tab");
            }
        }

        private void markEnabled(boolean enabled) {
            if (!enabled) {
                tabElement.setClassName("tab-disabled");
            } else {
                if (isSelected()) {
                    tabElement.setClassName("tab-selected");
                } else {
                    tabElement.setClassName("tab");
                }
            }
        }

        private void setHighlight(boolean highlight) {
            this.highlighted = highlight;
            if (highlight) {
                textElement.addClassName("tab-highlight");
            } else {
                textElement.removeClassName("tab-highlight");
            }
        }
    }

    private interface ScrollStrategy {

        int getScrollJump();

        void update(TabBar tabBar);
    }

    private class StaticScrollStrategy implements ScrollStrategy {

        private final int jump;

        private StaticScrollStrategy(int jump) {
            this.jump = jump;
        }

        public int getScrollJump() {
            return jump;
        }

        public void update(TabBar tabBar) {
        }
    }

    private class SmallestTabScrollStrategy implements ScrollStrategy {

        private int jump;

        private SmallestTabScrollStrategy() {
            update(TabBar.this);
        }

        public int getScrollJump() {
            return jump;
        }

        public void update(TabBar tabBar) {
            int width = 40000;
            for (Tab tab : tabBar.tabs) {
                int offsetWidth = tab.tabElement.getOffsetWidth();
                if (width > offsetWidth) {
                    width = offsetWidth;
                }
            }
            this.jump = width;
        }
    }

    private class LargestTabScrollStrategy implements ScrollStrategy {

        private int jump;

        private LargestTabScrollStrategy() {
            update(TabBar.this);
        }

        public int getScrollJump() {
            return jump;
        }

        public void update(TabBar tabBar) {
            int width = 0;
            for (Tab tab : tabBar.tabs) {
                int offsetWidth = tab.tabElement.getOffsetWidth();
                if (width < offsetWidth) {
                    width = offsetWidth;
                }
            }
            this.jump = width;
        }
    }
}
