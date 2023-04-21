package org.javaongems.gwk.client;

import java.util.ArrayList;
import java.util.HashMap;
import org.javaongems.std.client.DivPanel;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.ComplexPanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.FocusPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.KeyboardListener;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class PagePanel extends Composite {

    public static interface Listener {

        public void onLeavePage(int idx, Widget from, Widget to);

        public boolean onBeforePageSelected(int idx, Widget target);

        public void onPageSelected(int idx, Widget target);
    }

    private DivPanel containerWidget = new DivPanel();

    private Element srchContainer = DOM.createDiv();

    private Element pageArea = DOM.createDiv();

    private Element pageTabs = DOM.createDiv();

    private Element pageCtxContainer = DOM.createDiv();

    private DeckPanel deck = new DeckPanel();

    private Widget searchPanel, pageCtx;

    private Bar bar;

    private Listener listener;

    public PagePanel() {
        initWidget(containerWidget);
        setStyleName("gwk-PagePanel");
        buildBaseFrame();
    }

    public void setSearchPanel(Widget w) {
        if (searchPanel == w) return;
        if (searchPanel != null) containerWidget.remove(searchPanel);
        if (w != null) containerWidget.addInto(w, srchContainer);
        searchPanel = w;
    }

    public Widget getSearchPanel() {
        return searchPanel;
    }

    public void add(Widget w, Widget ctx, String lbl, boolean asHtml) {
        bar.add(w, ctx, lbl, asHtml);
        if (bar.pages.size() == 1) selectPage(0);
    }

    public void remove(int idx) {
        Entry entry = (Entry) bar.pages.get(idx);
        entry.remove();
    }

    public void remove(Widget w) {
        Entry entry = (Entry) bar.viewEntries.get(w);
        entry.remove();
    }

    public void setPageContext(Widget w) {
        if (pageCtx == w) return;
        if (pageCtx != null) containerWidget.remove(pageCtx);
        if (w != null) containerWidget.addInto(w, pageCtxContainer);
        pageCtx = w;
    }

    public void selectPage(int idx) {
        Entry entry = (Entry) bar.pages.get(idx);
        entry.selectPageIfRequired();
    }

    public void selectPage(Widget page) {
        int idx = deck.getWidgetIndex(page);
        selectPage(idx);
    }

    public int getSelectedPageIndex() {
        return deck.getVisibleWidget();
    }

    public Widget getSelectedPage() {
        return deck.getWidget(getSelectedPageIndex());
    }

    public void setListener(Listener lsnr) {
        listener = lsnr;
    }

    public int getPageBodyOffsetHeight() {
        return deck.getOffsetHeight();
    }

    public int getPageBodyOffsetWidth() {
        return deck.getOffsetWidth();
    }

    protected void onAttach() {
        super.onAttach();
        bar.doAttach(true);
    }

    protected void onDetach() {
        super.onDetach();
        bar.doAttach(false);
    }

    private void buildBaseFrame() {
        Element container = containerWidget.getElement();
        DOM.appendChild(container, srchContainer);
        UIObject.setStyleName(srchContainer, "gwk-PagePanel-srch", true);
        DOM.appendChild(container, pageArea);
        UIObject.setStyleName(pageArea, "gwk-PagePanel-area", true);
        Element table = DOM.createTable();
        DOM.appendChild(pageArea, table);
        DOM.setAttribute(table, "cellPadding", "0");
        DOM.setAttribute(table, "cellSpacing", "0");
        Element tbody = DOM.createTBody();
        DOM.appendChild(table, tbody);
        Element tr = DOM.createTR();
        DOM.appendChild(tbody, tr);
        Element tdCnt = DOM.createTD();
        DOM.appendChild(tr, tdCnt);
        UIObject.setStyleName(tdCnt, "gwk-PagePanel-cnt", true);
        DOM.setAttribute(tdCnt, "vAlign", "top");
        containerWidget.addInto(deck, tdCnt);
        Element tdBar = DOM.createTD();
        DOM.appendChild(tr, tdBar);
        UIObject.setStyleName(tdBar, "gwk-PagePanel-bar", true);
        DOM.setAttribute(tdBar, "vAlign", "top");
        Element spacer = DOM.createDiv();
        DOM.appendChild(tdBar, spacer);
        UIObject.setStyleName(spacer, "gwk-PagePanel-bs", true);
        DOM.appendChild(tdBar, pageTabs);
        bar = new Bar(pageTabs);
        DOM.appendChild(tdBar, pageCtxContainer);
        UIObject.setStyleName(pageCtxContainer, "gwk-PagePanel-ctx", true);
        Element hr = DOM.createElement("hr");
        DOM.appendChild(pageCtxContainer, hr);
    }

    private class Bar extends ComplexPanel {

        private ArrayList pages = new ArrayList();

        private HashMap viewEntries = new HashMap();

        private Entry currentSelection;

        public Bar(Element e) {
            setElement(e);
        }

        public void add(Widget view, Widget sel, String label, boolean asHtml) {
            insert(view, sel, label, asHtml, pages.size());
        }

        public void insert(Widget view, Widget sel, String label, boolean asHtml, int before) {
            Entry entry = new Entry(view, sel, label, asHtml);
            insert(entry, getElement(), before);
            addEntry(entry);
        }

        protected void doAttach(boolean attach) {
            if (attach) onAttach(); else onDetach();
        }

        protected void addEntry(Entry entry) {
            pages.add(entry);
            Widget v = entry.getView();
            deck.add(v);
            viewEntries.put(v, entry);
        }

        protected void removeEntry(Entry entry) {
            remove(entry);
            pages.remove(entry);
            Widget v = entry.getView();
            viewEntries.remove(v);
            deck.remove(v);
        }

        public void remove(int idx) {
            Entry entry = (Entry) pages.get(idx);
            entry.remove();
            entry = getFirstSelectable();
            entry.select();
        }

        private Entry getFirstSelectable() {
            for (int i = 0; i < pages.size(); i++) {
                Entry entry = (Entry) pages.get(i);
                if (entry.isEnabled()) return entry;
            }
            return null;
        }
    }

    private class Entry extends ComplexPanel implements KeyboardListener {

        private FocusContainer focusContainer = new FocusContainer();

        private Element page;

        private Widget view, context;

        private boolean selected;

        public Entry(Widget vw, Widget ctx, String label, boolean asHtml) {
            view = vw;
            context = ctx;
            page = DOM.createDiv();
            setElement(page);
            setStyleName("gwk-PagePanel-page");
            focusContainer.setStyleName("gwk-PagePanel-pghnd");
            focusContainer.setTabIndex(0);
            add(focusContainer, page);
            focusContainer.addKeyboardListener(this);
            Widget w = asHtml ? new HTML(label) : new Label(label);
            focusContainer.getContainer().add(w);
            if (context != null) {
                focusContainer.getContainer().add(context);
                context.setVisible(false);
            }
            sinkEvents(Event.ONCLICK);
        }

        public void remove() {
            bar.removeEntry(this);
        }

        public boolean isSelected() {
            return selected;
        }

        public void onBrowserEvent(Event event) {
            int type = DOM.eventGetType(event);
            if (type == Event.ONCLICK) selectPageIfRequired();
        }

        public void selectPageIfRequired() {
            if (selected) return;
            int idx = bar.pages.indexOf(this);
            if (listener != null) {
                if (!listener.onBeforePageSelected(idx, view)) return;
            }
            if (bar.currentSelection != null) {
                if (listener != null) {
                    int fromIdx = bar.pages.indexOf(bar.currentSelection);
                    listener.onLeavePage(fromIdx, bar.currentSelection.view, view);
                }
                bar.currentSelection.deselect();
            }
            select();
            if (listener != null) listener.onPageSelected(idx, view);
        }

        public Element getPageElement() {
            return page;
        }

        public void select() {
            selected = true;
            bar.currentSelection = this;
            UIObject.setStyleName(focusContainer.getElement(), "gwk-PagePanel-pgsln", true);
            int index = bar.pages.indexOf(this);
            deck.showWidget(index);
            if (context != null) context.setVisible(true);
        }

        public void deselect() {
            selected = false;
            UIObject.setStyleName(focusContainer.getElement(), "gwk-PagePanel-pgsln", false);
            if (context != null) context.setVisible(false);
        }

        public boolean isEnabled() {
            return focusContainer.isEnabled();
        }

        public Widget getView() {
            return view;
        }

        public Widget getContext() {
            return context;
        }

        public void onKeyDown(Widget sender, char keyCode, int modifiers) {
        }

        public void onKeyPress(Widget sender, char keyCode, int modifiers) {
        }

        public void onKeyUp(Widget sender, char keyCode, int modifiers) {
            if (keyCode == (char) 32) selectPageIfRequired();
        }
    }

    private class FocusContainer extends FocusPanel {

        public FocusContainer() {
            super(new DivPanel());
        }

        public DivPanel getContainer() {
            return (DivPanel) getWidget();
        }

        public boolean isEnabled() {
            return !DOM.getBooleanAttribute(getElement(), "disabled");
        }

        public void setEnabled(boolean enabled) {
            DOM.setBooleanAttribute(getElement(), "disabled", !enabled);
        }
    }
}
