package org.gwt.advanced.client.ui.widget;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.*;
import com.google.gwt.user.client.ui.*;
import org.gwt.advanced.client.datamodel.Pageable;
import org.gwt.advanced.client.ui.AdvancedWidget;
import org.gwt.advanced.client.ui.PagerListener;
import org.gwt.advanced.client.ui.resources.PagerConstants;
import org.gwt.advanced.client.ui.widget.theme.ThemeImage;

/**
 * This is pager widget implementation.<p>
 * It can be used not only by the grid, but also by other widgets where paging feature is required.
 * These widgets must implement the {@link PagerListener} interface.
 *
 * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
 * @since 1.0.0
 */
public class Pager extends SimplePanel implements AdvancedWidget {

    /** widget resource bundle */
    private static final PagerConstants RESOURCE = (PagerConstants) GWT.create(PagerConstants.class);

    /** integer number pattern */
    protected static final String INTEGER_PATTERN = "([+-]?[0-9]+)|([0-9]*)";

    /** this flag defines whether the arrows must be displayed */
    private boolean arrowsVisible = true;

    /** pager table */
    private FlexTable table = new FlexTable();

    /** pageable data model */
    private Pageable model;

    /** left arrow click handler */
    private ClickHandler leftClickHandler;

    /** right arrow click handler */
    private ClickHandler rightClickHandler;

    /** left arrow image */
    private Image left = new ThemeImage();

    /** right arrow image */
    private Image right = new ThemeImage();

    /** a grid panel */
    private GridPanel gridPanel;

    /** a page number box */
    private TextBox pageNumber;

    /** submit button image */
    private Image submit;

    /** a flag that means whether the page number box must be displayed */
    private boolean pageNumberBoxDisplayed;

    /** a flag that means whether the total page count label must be displayed */
    private boolean totalCountDisplayed;

    /**
     * Creates an instance of this class and initialie core elements.
     */
    public Pager() {
        add(table);
    }

    /**
     * Getter for property 'model'.
     *
     * @return Value for property 'model'.
     */
    public Pageable getModel() {
        return model;
    }

    /**
     * Setter for property 'model'.
     *
     * @param model Value to set for property 'model'.
     */
    public void setModel(Pageable model) {
        this.model = model;
    }

    /**
     * Getter for property 'arrowsVisible'.
     *
     * @return Value for property 'arrowsVisible'.
     */
    public boolean isArrowsVisible() {
        return arrowsVisible;
    }

    /**
     * Setter for property 'arrowsVisible'.
     *
     * @param arrowsVisible Value to set for property 'arrowsVisible'.
     */
    public void setArrowsVisible(boolean arrowsVisible) {
        this.arrowsVisible = arrowsVisible;
    }

    /**
     * Invoke this method to displayActive the pager.
     * @see org.gwt.advanced.client.ui.AdvancedWidget#display()
     */
    public void display() {
        setStyleName("advanced-Pager");
        if (getModel() != null) {
            if (table.getRowCount() > 0) {
                while (table.getCellCount(0) > 0) {
                    table.getColumnFormatter().setWidth(table.getCellCount(0) - 1, "1%");
                    table.removeCell(0, table.getCellCount(0) - 1);
                }
            }
            addArrows();
            addLinks();
            addPageNumberBox();
            addTotalCountLabel();
        }
    }

    /**
     * Getter for property 'gridPanel'.
     *
     * @return Value for property 'gridPanel'.
     */
    public GridPanel getGridPanel() {
        if (gridPanel == null) gridPanel = new GridPanel();
        return gridPanel;
    }

    /**
     * Setter for property 'gridPanel'.
     *
     * @param gridPanel Value to set for property 'gridPanel'.
     */
    public void setGridPanel(GridPanel gridPanel) {
        this.gridPanel = gridPanel;
    }

    /**
     * Getter for property 'pageNumberBoxDisplayed'.
     *
     * @return Value for property 'pageNumberBoxDisplayed'.
     */
    public boolean isPageNumberBoxDisplayed() {
        return pageNumberBoxDisplayed;
    }

    /**
     * Setter for property 'pageNumberBoxDisplayed'.
     *
     * @param pageNumberBoxDisplayed Value to set for property 'pageNumberBoxDisplayed'.
     */
    public void setPageNumberBoxDisplayed(boolean pageNumberBoxDisplayed) {
        this.pageNumberBoxDisplayed = pageNumberBoxDisplayed;
    }

    /**
     * Method isTotalCountDisplayed returns the totalCountDisplayed of this Pager object.
     *
     * @return the totalCountDisplayed (type boolean) of this Pager object.
     */
    public boolean isTotalCountDisplayed() {
        return totalCountDisplayed;
    }

    /**
     * Method setTotalCountDisplayed sets the totalCountDisplayed of this Pager object.
     *
     * @param totalCountDisplayed the totalCountDisplayed of this Pager object.
     *
     */
    public void setTotalCountDisplayed(boolean totalCountDisplayed) {
        this.totalCountDisplayed = totalCountDisplayed;
    }

    /**
     * Getter for property 'pageNumber'.
     *
     * @return Value for property 'pageNumber'.
     */
    protected TextBox getPageNumber() {
        if (pageNumber == null) {
            pageNumber = new TextBox();
            PageBoxHandler boxHandler = new PageBoxHandler();
            pageNumber.addFocusHandler(boxHandler);
            pageNumber.addBlurHandler(boxHandler);
        }
        return pageNumber;
    }

    /**
     * Getter for property 'submit'.
     *
     * @return Value for property 'submit'.
     */
    public Image getSubmit() {
        if (submit == null) {
            submit = new ThemeImage();
            submit.setUrl("right.gif");
            submit.setTitle(RESOURCE.getJumpTo());
        }
        return submit;
    }

    /**
     * This method adds a total page count label into the pager if the {@link #isTotalCountDisplayed()} method
     * returns <code>true</code>.<p/>
     * Otherwise it displays an empty cell.
     */
    protected void addTotalCountLabel() {
        int column = table.getCellCount(0);
        if (isTotalCountDisplayed()) {
            Label label = new Label();
            label.setText(RESOURCE.getTotalCount(String.valueOf(getModel().getTotalPagesNumber())));
            label.setStyleName("label");
            table.setWidget(0, column, label);
        } else {
            table.setText(0, column, "");
        }
        table.getCellFormatter().setWidth(0, column, "100%");
    }

    /**
     * This method adds a page number box and the submit button if the {@link #isPageNumberBoxDisplayed()} method
     * returns <code>true</code>.<p/>
     * Otherwise it does nothing.
     */
    protected void addPageNumberBox() {
        if (isPageNumberBoxDisplayed()) {
            int column = table.getCellCount(0);
            ToggleButton button = new ToggleButton(getSubmit());
            final Pager pager = this;
            button.addClickHandler(new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    setCurrentPageNumber(Integer.valueOf(getPageNumber().getText()) - 1);
                    getGridPanel().getMediator().firePageChangeEvent(pager, getModel().getCurrentPageNumber());
                    ((ToggleButton) event.getSource()).setDown(false);
                }
            });
            button.setStyleName("button");
            SimplePanel panel = new SimplePanel();
            panel.setWidget(getPageNumber());
            panel.setStyleName("page-number");
            Label label = new Label();
            label.setStyleName("label");
            label.setText(RESOURCE.getDisplayPage());
            table.setWidget(0, column, label);
            column = table.getCellCount(0);
            table.setWidget(0, column, panel);
            getPageNumber().setText(String.valueOf(getModel().getCurrentPageNumber() + 1));
            column = table.getCellCount(0);
            table.setWidget(0, column, button);
        }
    }

    /**
     * Setter for property 'currentPageNumber'.
     *
     * @param page Value to set for property 'currentPageNumber'.
     */
    protected void setCurrentPageNumber(int page) {
        Pageable pageable = getModel();
        if (page < pageable.getTotalPagesNumber()) pageable.setCurrentPageNumber(page);
        if (isPageNumberBoxDisplayed()) getPageNumber().setText(String.valueOf(page + 1));
        display();
    }

    /**
     * Getter for property 'leftClickHandler'.
     *
     * @return Value for property 'leftClickHandler'.
     */
    protected ClickHandler getLeftClickHandler() {
        if (leftClickHandler == null) {
            final Pager pager = this;
            leftClickHandler = new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    Pageable pageable = getModel();
                    int startPage = pageable.getStartPage() - pageable.getDisplayedPages();
                    if (startPage < 0) {
                        startPage = 0;
                    }
                    setCurrentPageNumber(startPage);
                    getGridPanel().getMediator().firePageChangeEvent(pager, startPage);
                    ((ToggleButton) event.getSource()).setDown(false);
                }
            };
        }
        return leftClickHandler;
    }

    /**
     * Getter for property 'rightClickHandler'.
     *
     * @return Value for property 'rightClickHandler'.
     */
    protected ClickHandler getRightClickHandler() {
        if (rightClickHandler == null) {
            final Pager pager = this;
            rightClickHandler = new ClickHandler() {

                @Override
                public void onClick(ClickEvent event) {
                    Pageable pageable = getModel();
                    int startPage = pageable.getStartPage() + pageable.getDisplayedPages();
                    if (startPage >= pageable.getTotalPagesNumber()) {
                        startPage = (pageable.getStartPage() - 1) * pageable.getPageSize() + 1;
                        if (startPage < 0) startPage = 0;
                    }
                    setCurrentPageNumber(startPage);
                    getGridPanel().getMediator().firePageChangeEvent(pager, startPage);
                    ((ToggleButton) event.getSource()).setDown(false);
                }
            };
        }
        return rightClickHandler;
    }

    /**
     * This method adds page links into the pager panel.
     */
    protected void addLinks() {
        final Pageable pageable = getModel();
        int count = 1;
        final Pager pager = this;
        for (int i = pageable.getStartPage(); i <= pageable.getEndPage(); i++) {
            if (pageable.getCurrentPageNumber() != i) {
                Anchor hyperlink = new Anchor(String.valueOf(i + 1), "javascript:void(0)");
                final int page = i;
                hyperlink.addClickHandler(new ClickHandler() {

                    @Override
                    public void onClick(ClickEvent event) {
                        setCurrentPageNumber(page);
                        getGridPanel().getMediator().firePageChangeEvent(pager, page);
                    }
                });
                table.setWidget(0, count++, hyperlink);
            } else {
                table.setText(0, count++, String.valueOf(i + 1));
            }
        }
    }

    /**
     * This method adds arrows into the pager panel.
     */
    protected void addArrows() {
        Pageable pageable = getModel();
        int pages = pageable.getEndPage() - pageable.getStartPage() + 2;
        table.setText(0, 0, "");
        table.setText(0, pages, "");
        if (isArrowsVisible()) {
            ToggleButton leftButton = new ToggleButton(left);
            leftButton.setStyleName("button");
            left.setTitle(RESOURCE.getPreviousPage());
            table.setWidget(0, 0, leftButton);
            ToggleButton rightButton = new ToggleButton(right);
            rightButton.setStyleName("button");
            right.setTitle(RESOURCE.getNextPage());
            table.setWidget(0, pages, rightButton);
            if (pageable.getCurrentPageNumber() >= pageable.getDisplayedPages()) {
                left.setUrl("left.gif");
                leftButton.addClickHandler(getLeftClickHandler());
            } else {
                left.setUrl("left-disabled.gif");
                leftButton.setEnabled(false);
            }
            if (pageable.getStartPage() + pageable.getDisplayedPages() <= pageable.getTotalPagesNumber()) {
                right.setUrl("right.gif");
                rightButton.addClickHandler(getRightClickHandler());
            } else {
                right.setUrl("right-disabled.gif");
                rightButton.setEnabled(false);
            }
        }
    }

    /**
     * This handler restores the previous value in the text box if the new one doesn't match restrictions required
     * for the page number values.
     *
     * @author <a href="mailto:sskladchikov@gmail.com">Sergey Skladchikov</a>
     */
    protected class PageBoxHandler implements FocusHandler, BlurHandler {

        /** previous page number */
        private int previousValue;

        @Override
        public void onBlur(BlurEvent event) {
            TextBox box = (TextBox) event.getSource();
            String text = box.getText();
            if (text != null && (!text.matches(INTEGER_PATTERN) || Integer.valueOf(text) < 1 || Integer.valueOf(text) > getModel().getTotalPagesNumber())) {
                box.setText(String.valueOf(this.previousValue));
            }
        }

        @Override
        public void onFocus(FocusEvent event) {
            TextBox box = (TextBox) event.getSource();
            String text = box.getText();
            if (text != null && text.length() > 0) this.previousValue = Integer.valueOf(text); else this.previousValue = 1;
        }
    }
}
