package com.neolab.crm.client.app.widgets.hierarchy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.DeferredCommand;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.neolab.crm.client.app.gin.Injector;

/**
 * @author Dusan
 */
public class SimpleMillerColumns<E> extends MillerColumnsWidget implements HasValueChangeHandlers<E> {

    public interface Model<E> {

        void search(E parent, int offset, int itemsPerPage, AsyncCallback<SearchResult<E>> callback);

        String getDisplayString(E item);

        String getHeaderString(E item);

        boolean isHeaderBold(E item);

        String getIcon(E item);

        boolean equalsTo(E item1, E item2);
    }

    public interface ToolTipAwareModel<E> extends Model<E> {

        String getToolTip(E item);
    }

    public static class SearchResult<E> implements Serializable {

        int total;

        List<E> items;

        public SearchResult(int total, List<E> items) {
            this.total = total;
            this.items = items;
        }
    }

    protected SelectableLabel<E> getElementWidget(E element, int column) {
        return new SelectableLabel<E>(element, column, model.getDisplayString(element), model.getIcon(element), this);
    }

    public static class SelectableLabel<E> extends HTML implements ClickHandler {

        private E element;

        private int column;

        private SimpleMillerColumns smc;

        private boolean selected;

        private SelectableLabel(E element, int column, String displayName, String icon, SimpleMillerColumns smc) {
            if (icon == null || icon.isEmpty()) {
                setText(displayName);
            } else {
                StringBuilder html = new StringBuilder(300);
                html.append("<div style='background:url(").append(icon).append(") no-repeat left center;padding-left:17px;'>");
                html.append(Utils.escapeHtml(displayName)).append("</div>");
                setHTML(html.toString());
            }
            setTitle(displayName);
            setStyleName("millerColumns-item");
            addStyleName("hand");
            this.element = element;
            this.column = column;
            this.smc = smc;
            addClickHandler(this);
            Utils.disableTextSelectInternal(getElement(), true);
        }

        @Override
        public void onClick(ClickEvent event) {
            if (!selected) {
                selected = true;
                smc.select(this);
            }
        }

        public void select() {
            selected = true;
            addStyleDependentName("selected");
        }

        public void deselect() {
            selected = false;
            removeStyleDependentName("selected");
        }
    }

    @Override
    public HandlerRegistration addValueChangeHandler(ValueChangeHandler<E> handler) {
        return addHandler(handler, ValueChangeEvent.getType());
    }

    public void setSelected(E selected) {
        if (this.selected != selected) {
            this.selected = selected;
            ValueChangeEvent.fire(this, selected);
        }
    }

    private static class HeaderWidget extends Composite {

        private FlexTable container = new FlexTable();

        private Label name = new Label();

        private NavButton up = new NavButton(new Image("images/arrow_up.gif"), new Image("images/arrow_up.gif"), new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                previous();
            }
        });

        private NavButton down = new NavButton(new Image("images/arrow_down.gif"), new Image("images/arrow_down.gif"), new ClickHandler() {

            @Override
            public void onClick(ClickEvent event) {
                next();
            }
        });

        private SimpleMillerColumns millerColumns;

        private int column;

        private HeaderWidget(SimpleMillerColumns millerColumns, int column) {
            this.millerColumns = millerColumns;
            this.column = column;
            initWidget(container);
            setStyleName("millerColumns-headerWidget");
            DOM.setStyleAttribute(container.getElement(), "tableLayout", "fixed");
            container.setWidget(0, 0, name);
            container.setWidget(0, 1, up);
            container.setWidget(0, 2, down);
            container.setWidth("100%");
            container.getFlexCellFormatter().setWidth(0, 0, "100%");
            container.getFlexCellFormatter().setWidth(0, 1, "22px");
            container.getFlexCellFormatter().setWidth(0, 2, "22px");
            container.getFlexCellFormatter().setAlignment(0, 0, HasHorizontalAlignment.ALIGN_LEFT, HasVerticalAlignment.ALIGN_MIDDLE);
            container.getFlexCellFormatter().setAlignment(0, 1, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
            container.getFlexCellFormatter().setAlignment(0, 2, HasHorizontalAlignment.ALIGN_CENTER, HasVerticalAlignment.ALIGN_MIDDLE);
        }

        private void previous() {
            millerColumns.previousPage(column);
        }

        private void next() {
            millerColumns.nextPage(column);
        }

        public void setName(String title) {
            name.setText(title);
        }

        public void enableUpPage(boolean enable) {
            if (enable) {
                up.removeStyleName("hidden");
            } else {
                up.addStyleName("hidden");
            }
        }

        public void enableDownPage(boolean enable) {
            if (enable) {
                down.removeStyleName("hidden");
            } else {
                down.addStyleName("hidden");
            }
        }
    }

    private static class LevelStateHolder<E> {

        private E selected;

        private E col1Parent;

        private E col2Parent;

        private List<E> firstColumnElements = new ArrayList<E>();

        private List<E> secondColumnElements = new ArrayList<E>();

        private int col1Page;

        private int col2Page;

        private int col1Total;

        private int col2Total;

        private LevelStateHolder(E selected, E col1Parent, E col2Parent, List<E> firstColumnElements, List<E> secondColumnElements, int col1Page, int col2Page, int col1Total, int col2Total) {
            this.selected = selected;
            this.col1Parent = col1Parent;
            this.col2Parent = col2Parent;
            this.secondColumnElements.addAll(secondColumnElements);
            this.col2Page = col2Page;
            this.col2Total = col2Total;
            this.firstColumnElements.addAll(firstColumnElements);
            this.col1Page = col1Page;
            this.col1Total = col1Total;
        }
    }

    private static final int DEFAULT_ITEMS_PER_PAGE = 20;

    private Map<Integer, LevelStateHolder<E>> levelStateHolderList = new HashMap<Integer, LevelStateHolder<E>>();

    private List<E> firstColumnElements = new ArrayList<E>();

    private List<E> secondColumnElements = new ArrayList<E>();

    private Model<E> model;

    private E root;

    private SelectableLabel<E> selectedLabel;

    private int itemsPerPage = DEFAULT_ITEMS_PER_PAGE;

    private int level;

    private int col1Page;

    private int col2Page;

    private int col1Total;

    private int col2Total;

    private E col1Parent;

    private E col2Parent;

    private E selected;

    private HeaderWidget col1Header = new HeaderWidget(this, 0);

    private HeaderWidget col2Header = new HeaderWidget(this, 1);

    private HeaderWidget col1Footer = new HeaderWidget(this, 0);

    private HeaderWidget col2Footer = new HeaderWidget(this, 1);

    private boolean selectFirstItemOnInit = true;

    public SimpleMillerColumns() {
        this(DEFAULT_ITEMS_PER_PAGE);
    }

    public SimpleMillerColumns(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
        getBreadCrumbBar().setSimpleMillerColumns(this);
        miller.setHeight(this.itemsPerPage * 18 + "px");
        addColumn(col1Header, col1Footer);
    }

    public void show() {
        resetVariables();
        if (getNumberOfColumns() == 0) {
            addColumn(col1Header, col1Footer);
        }
        assert root != null && model != null;
        col1Parent = root;
        breadCrumbBar.add(col1Parent);
        loadingWidget.show();
        model.search(col1Parent, 0, itemsPerPage, new AsyncCallback<SearchResult<E>>() {

            @Override
            public void onFailure(Throwable caught) {
                loadingWidget.hide();
            }

            @Override
            public void onSuccess(SearchResult<E> result) {
                if (result != null && result.total > 0) {
                    addResultToColumn(result, firstColumnElements);
                    level = 1;
                    col1Total = result.total;
                    col1Header.setName(Injector.INSTANCE.getConstants().level() + " " + level);
                    final E toSelect = !selectFirstItemOnInit ? null : firstColumnElements.size() == 0 ? null : firstColumnElements.get(0);
                    addElements(0, firstColumnElements, toSelect);
                    DeferredCommand.addCommand(new Command() {

                        @Override
                        public void execute() {
                            if (toSelect != null) {
                                if (selectedLabel != null) {
                                    SelectableLabel<E> tmp = selectedLabel;
                                    selectedLabel = null;
                                    select(tmp);
                                }
                                setSelected(toSelect);
                            }
                        }
                    });
                    handlePagination();
                } else {
                    reset();
                }
                loadingWidget.hide();
            }
        });
    }

    private void handlePagination() {
        if (col1Page > 0) {
            col1Header.enableUpPage(true);
            col1Footer.enableUpPage(true);
        } else {
            col1Header.enableUpPage(false);
            col1Footer.enableUpPage(false);
        }
        if (getNumberOfColumns() > 1 && col2Page > 0) {
            col2Header.enableUpPage(true);
            col2Footer.enableUpPage(true);
        } else {
            col2Header.enableUpPage(false);
            col2Footer.enableUpPage(false);
        }
        if (col1Total > itemsPerPage * (col1Page + 1)) {
            col1Header.enableDownPage(true);
            col1Footer.enableDownPage(true);
        } else {
            col1Header.enableDownPage(false);
            col1Footer.enableDownPage(false);
        }
        if (getNumberOfColumns() > 1) {
            if (col2Total > itemsPerPage * (col2Page + 1)) {
                col2Header.enableDownPage(true);
                col2Footer.enableDownPage(true);
            } else {
                col2Header.enableDownPage(false);
                col2Footer.enableDownPage(false);
            }
        }
    }

    private void select(final SelectableLabel<E> label) {
        if (selectedLabel != label) {
            if (selectedLabel != null) {
                selectedLabel.deselect();
            }
            loadingWidget.show();
            model.search(label.element, 0, itemsPerPage, new AsyncCallback<SearchResult<E>>() {

                @Override
                public void onFailure(Throwable caught) {
                    loadingWidget.hide();
                }

                @Override
                public void onSuccess(SearchResult<E> result) {
                    if (label.column == 0) {
                        col2Parent = label.element;
                        setSelected(col2Parent);
                        breadCrumbBar.changeLastElement(label.element);
                        selectedLabel = label;
                        selectedLabel.select();
                        secondColumnElements.clear();
                        col2Total = result.total;
                        col2Page = 0;
                        if (result.items == null || result.items.isEmpty()) {
                            if (getNumberOfColumns() > 1) {
                                removeColumn(1);
                            }
                        } else {
                            addResultToColumn(result, secondColumnElements);
                            if (getNumberOfColumns() > 1) {
                                clearColumn(1);
                            } else {
                                col2Header.setName(Injector.INSTANCE.getConstants().level() + " " + (level + 1));
                                addColumn(col2Header, col2Footer);
                            }
                            addElements(1, secondColumnElements, null);
                        }
                    } else {
                        E selected = selectedLabel != null ? selectedLabel.element : null;
                        LevelStateHolder<E> level1 = new LevelStateHolder<E>(selected, col1Parent, col2Parent, firstColumnElements, secondColumnElements, col1Page, col2Page, col1Total, col2Total);
                        levelStateHolderList.put(level, level1);
                        level++;
                        col1Parent = col2Parent;
                        col2Parent = label.element;
                        SimpleMillerColumns.this.setSelected(label.element);
                        col1Header.setName("Level " + level);
                        firstColumnElements.clear();
                        firstColumnElements.addAll(secondColumnElements);
                        col1Page = col2Page;
                        col1Total = col2Total;
                        col2Page = 0;
                        col2Total = result.total;
                        secondColumnElements.clear();
                        if (result.items == null || result.items.isEmpty()) {
                            removeColumn(1);
                            col2Total = 0;
                        } else {
                            addResultToColumn(result, secondColumnElements);
                            clearColumn(1);
                            addElements(1, secondColumnElements, null);
                            col2Header.setName("Level " + (level + 1));
                        }
                        clearColumn(0);
                        addElements(0, firstColumnElements, label.element);
                        breadCrumbBar.add(label.element);
                    }
                    handlePagination();
                    loadingWidget.hide();
                }
            });
        }
    }

    public void showPath(List<E> path, E toSelect) {
        ArrayList<E> newPath = new ArrayList<E>();
        if (path != null) newPath.addAll(path);
        if (toSelect == null || newPath.contains(null)) {
            refresh();
            return;
        }
        newPath.add(toSelect);
        reset();
        addColumn(col1Header, col1Footer);
        this.level = newPath.size();
        if (newPath.size() > 1) {
            col1Parent = newPath.get(newPath.size() - 2);
        } else col1Parent = root;
        col2Parent = toSelect;
        setSelected(toSelect);
        for (int i = 0; i < newPath.size(); i++) {
            breadCrumbBar.add(newPath.get(i));
        }
        changeLevel(level, col1Parent, col2Parent);
    }

    public void changeLevel(final int level, E root1, E root2) {
        this.level = level;
        LevelStateHolder<E> levelStateHolder = levelStateHolderList.get(level);
        if (levelStateHolder != null) changeContent(level, levelStateHolder); else {
            assert root2 != null;
            if (root1 == null) root1 = root;
            col1Parent = root1;
            col2Parent = root2;
            col1Page = 0;
            col2Page = 0;
            col1Total = 0;
            col2Total = 0;
            setSelected(root2);
            loadingWidget.show();
            model.search(root1, 0, itemsPerPage, new AsyncCallback<SearchResult<E>>() {

                @Override
                public void onFailure(Throwable caught) {
                    loadingWidget.hide();
                }

                @Override
                public void onSuccess(SearchResult<E> result) {
                    if (result != null) {
                        addResultToColumn(result, firstColumnElements);
                        col1Total = result.total;
                        if (col2Parent != null) {
                            model.search(col2Parent, 0, itemsPerPage, new AsyncCallback<SearchResult<E>>() {

                                @Override
                                public void onFailure(Throwable caught) {
                                    LevelStateHolder<E> holder = new LevelStateHolder<E>(selected, col1Parent, col2Parent, firstColumnElements, secondColumnElements, col1Page, col2Page, col1Total, col2Total);
                                    changeContent(level, holder);
                                }

                                @Override
                                public void onSuccess(SearchResult<E> result) {
                                    if (result != null) {
                                        addResultToColumn(result, secondColumnElements);
                                        col2Total = result.total;
                                    }
                                    LevelStateHolder<E> holder = new LevelStateHolder<E>(selected, col1Parent, col2Parent, firstColumnElements, secondColumnElements, col1Page, col2Page, col1Total, col2Total);
                                    changeContent(level, holder);
                                }
                            });
                        } else {
                            LevelStateHolder<E> holder = new LevelStateHolder<E>(selected, col1Parent, col2Parent, firstColumnElements, secondColumnElements, col1Page, col2Page, col1Total, col2Total);
                            changeContent(level, holder);
                        }
                    } else {
                    }
                    loadingWidget.hide();
                }
            });
        }
    }

    private void changeContent(int level, LevelStateHolder<E> levelStateHolder) {
        levelStateHolderList.put(level, levelStateHolder);
        col1Parent = levelStateHolder.col1Parent;
        col2Parent = levelStateHolder.col2Parent;
        col1Page = levelStateHolder.col1Page;
        col2Page = levelStateHolder.col2Page;
        col1Total = levelStateHolder.col1Total;
        col2Total = levelStateHolder.col2Total;
        col1Header.setName("Level " + (level));
        firstColumnElements.clear();
        firstColumnElements.addAll(levelStateHolder.firstColumnElements);
        secondColumnElements.clear();
        secondColumnElements.addAll(levelStateHolder.secondColumnElements);
        if (secondColumnElements.isEmpty()) {
            if (getNumberOfColumns() > 1) {
                removeColumn(1);
            }
            col2Total = 0;
        } else {
            if (getNumberOfColumns() > 1) {
                clearColumn(1);
            } else {
                addColumn(col2Header, col2Footer);
            }
            addElements(1, secondColumnElements, null);
            col2Header.setName("Level " + (level + 1));
        }
        setSelected(levelStateHolder.selected);
        clearColumn(0);
        addElements(0, firstColumnElements, levelStateHolder.selected);
        handlePagination();
    }

    public void nextPage(final int column) {
        final int currentPage = column == 0 ? col1Page : col2Page;
        final E parent = column == 0 ? col1Parent : col2Parent;
        loadingWidget.show();
        model.search(parent, (currentPage + 1) * itemsPerPage, itemsPerPage, new AsyncCallback<SearchResult<E>>() {

            @Override
            public void onFailure(Throwable caught) {
                loadingWidget.hide();
            }

            @Override
            public void onSuccess(SearchResult<E> result) {
                if (result != null) {
                    newPageSetup(result, column, selected);
                    if (column == 0) col1Page++; else col2Page++;
                    handlePagination();
                }
                loadingWidget.hide();
            }
        });
    }

    public void previousPage(final int column) {
        final int currentPage = column == 0 ? col1Page : col2Page;
        final E parent = column == 0 ? col1Parent : col2Parent;
        loadingWidget.show();
        model.search(parent, (currentPage - 1) * itemsPerPage, itemsPerPage, new AsyncCallback<SearchResult<E>>() {

            @Override
            public void onFailure(Throwable caught) {
                loadingWidget.hide();
            }

            @Override
            public void onSuccess(SearchResult<E> result) {
                if (result != null) {
                    newPageSetup(result, column, selected);
                    if (column == 0) col1Page--; else col2Page--;
                    handlePagination();
                }
                loadingWidget.hide();
            }
        });
    }

    private void newPageSetup(SearchResult<E> result, int column, E parent) {
        List<E> columnElements = column == 0 ? firstColumnElements : secondColumnElements;
        columnElements.clear();
        addResultToColumn(result, columnElements);
        clearColumn(column);
        addElements(column, columnElements, parent);
    }

    private void addResultToColumn(SearchResult<E> result, List<E> columnElements) {
        columnElements.clear();
        if (result != null && result.items != null) {
            if (result.items.size() > itemsPerPage) {
                columnElements.addAll(result.items.subList(0, itemsPerPage));
            } else columnElements.addAll(result.items);
        }
    }

    private void addElements(int column, List<E> elements, E toSelect) {
        for (int i = 0; i < elements.size(); i++) {
            E e = elements.get(i);
            final SelectableLabel<E> label = getElementWidget(e, column);
            addRow(column, label);
            if (model.equalsTo(e, toSelect)) {
                selectedLabel = label;
                label.select();
            }
        }
    }

    public void setRoot(E root) {
        this.root = root;
    }

    public void setModel(Model<E> model) {
        this.model = model;
    }

    public String getElementHeaderString(E element) {
        return model.getHeaderString(element);
    }

    public E getSelected() {
        return selected;
    }

    public void refresh() {
        reset();
        show();
    }

    private void reset() {
        if (getNumberOfColumns() > 1) removeColumn(1);
        if (getNumberOfColumns() > 0) removeColumn(0);
        levelStateHolderList.clear();
        resetVariables();
        breadCrumbBar.reset();
    }

    private void resetVariables() {
        col1Page = 0;
        col2Page = 0;
        col1Parent = null;
        col2Parent = null;
        col1Total = 0;
        col2Total = 0;
        selected = null;
        selectedLabel = null;
        level = 0;
    }

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setElementId(String id) {
        super.setElementId(id);
    }

    public void selectFirstItemOnInit(boolean toSelect) {
        selectFirstItemOnInit = toSelect;
    }

    public boolean isElementHeaderBold(E item) {
        return model.isHeaderBold(item);
    }
}
