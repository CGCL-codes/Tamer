package co.fxl.gui.table.scroll.impl;

import java.util.LinkedList;
import java.util.List;
import co.fxl.gui.api.IClickable;
import co.fxl.gui.api.IClickable.IClickListener;
import co.fxl.gui.api.IContainer;
import co.fxl.gui.api.IGridPanel.IGridCell;
import co.fxl.gui.impl.CallbackTemplate;
import co.fxl.gui.impl.Constants;
import co.fxl.gui.impl.IToolbar;
import co.fxl.gui.impl.ToolbarImpl;
import co.fxl.gui.table.api.ISelection.IMultiSelection.IChangeListener;
import co.fxl.gui.table.api.ISelection.ISingleSelection.ISelectionListener;
import co.fxl.gui.table.scroll.api.IRows;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IButtonPanelDecorator;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.ICommandButtons;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IDecorator;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IMoveRowListener;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IMultiRowListener;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IRowListener;
import co.fxl.gui.table.scroll.api.IScrollTableWidget.IScrollTableClickListener;

public class CommandButtonsImpl implements ICommandButtons<Object>, IButtonPanelDecorator, IChangeListener<Object> {

    class Edit {

        private int previousEdit = -1;

        Edit() {
            edit = clickable(panel.add(), "Edit", true);
            edit.addClickListener(new IClickListener() {

                @Override
                public void onClick() {
                    if (previousEdit != -1) {
                        widget.editable(previousEdit, false);
                    }
                    previousEdit = selectionIndex();
                    widget.editable(previousEdit, true);
                    clickable(edit, "Save");
                }
            });
            edit.clickable(!widget.preselectedList.isEmpty());
            widget.selection().single().addSelectionListener(new ISelectionListener<Object>() {

                @Override
                public void onSelection(int index, Object selection) {
                    if (previousEdit != -1) {
                        widget.editable(previousEdit, false);
                        clickable(edit, "Edit");
                    }
                }
            });
        }
    }

    private int selectionIndex() {
        if (selectionList.isEmpty()) return -1;
        return widget.rows.find(lastSelected());
    }

    private Object lastSelected() {
        if (selectionList.isEmpty()) return null;
        return selectionList.get(selectionList.size() - 1);
    }

    private final class Update implements IClickListener {

        private IRowListener<IRows<Object>> l;

        private boolean deleteSelection;

        private IMultiRowListener<IRows<Object>> l2;

        Update(IRowListener<IRows<Object>> l) {
            this(l, false);
        }

        Update(IMultiRowListener<IRows<Object>> l2, boolean deleteSelection) {
            this.l2 = l2;
            this.deleteSelection = deleteSelection;
        }

        Update(IRowListener<IRows<Object>> l, boolean deleteSelection) {
            this.l = l;
            this.deleteSelection = deleteSelection;
        }

        @Override
        public void onClick() {
            CallbackTemplate<IRows<Object>> callback = new CallbackTemplate<IRows<Object>>() {

                @Override
                public void onSuccess(IRows<Object> result) {
                    execute(result);
                    if (deleteSelection) {
                        widget.selection().clear();
                    }
                    updateButtons();
                }
            };
            if (l != null) {
                Object s = lastSelected();
                int i = selectionIndex();
                l.onClick(s, i, callback);
            } else if (l2 != null) {
                l2.onClick(selectionList, callback);
            } else {
                callback.onSuccess(null);
            }
        }

        private void execute(IRows<Object> result) {
            widget.visible(result);
        }
    }

    private final class Move implements IClickListener {

        private IMoveRowListener<IRows<Object>> l;

        private int inc;

        Move(IMoveRowListener<IRows<Object>> l, int inc) {
            this.l = l;
            this.inc = inc;
        }

        @Override
        public void onClick() {
            int index = selectionIndex();
            int secondIndex;
            if (inc == Integer.MIN_VALUE) secondIndex = 0; else if (inc == Integer.MAX_VALUE) secondIndex = widget.rows.size() - 1; else secondIndex = index + inc;
            if (l != null) l.onClick(index, lastSelected(), inc == Integer.MAX_VALUE || inc == Integer.MIN_VALUE, new CallbackTemplate<IRows<Object>>() {

                @Override
                public void onSuccess(IRows<Object> result) {
                    assert result != null;
                    widget.rows(result);
                    widget.update();
                }
            }); else {
                widget.rows.swap(index, secondIndex);
                widget.notifySelection(secondIndex, lastSelected());
                widget.update();
                updateButtons();
            }
        }
    }

    public static Link clickable(IContainer c, String string, boolean showLabel) {
        Link link = new Link();
        link = link.clickableLink(c, string).showLabel(showLabel);
        return link;
    }

    private static IClickable<?> clickable(IClickable<?> c, String string) {
        Link l = (Link) c;
        l.image.resource(string.toLowerCase() + ".png");
        l.label.text(string);
        return l;
    }

    private final IDecorator DEFAULT_DECORATOR = new IDecorator() {

        @Override
        public IClickable<?> decorate(IToolbar c) {
            String string = "Add";
            return clickable(c.add(), string, true);
        }
    };

    private static boolean ALIGN_END = Constants.get("CommandButtonsImpl.ALIGN_END", false);

    private ScrollTableWidgetImpl widget;

    private boolean listenOnAdd;

    private boolean listenOnRemove;

    private boolean listenOnMoveUp;

    private boolean listenOnMoveDown;

    private boolean listenOnShow;

    private IRowListener<IRows<Object>> listenOnAddListener;

    private IMultiRowListener<IRows<Object>> listenOnRemoveListener;

    private IMoveRowListener<IRows<Object>> listenOnMoveUpListener;

    private IMoveRowListener<IRows<Object>> listenOnMoveDownListener;

    private IRowListener<IRows<Object>> listenOnShowListener;

    List<Object> selectionList = new LinkedList<Object>();

    private IToolbar panel;

    private IClickable<?> imageUp;

    private IClickable<?> imageDown;

    private IClickable<?> remove;

    private IDecorator listenOnAddListenerDecorator = DEFAULT_DECORATOR;

    private IClickable<?> edit;

    private boolean listenOnEdit;

    private IRowListener<IRows<Object>> listenOnEditListener;

    private IClickable<?> imageUpMax;

    private IClickable<?> imageDownMax;

    private IClickable<?> show;

    CommandButtonsImpl(ScrollTableWidgetImpl widget) {
        this.widget = widget;
        widget.buttonPanel(this);
        selectionList(widget.preselectedList);
    }

    void setSpace(int i) {
        Link.SPACE = i;
    }

    @Override
    public ICommandButtons<Object> listenOnAdd(IDecorator dec, IRowListener<IRows<Object>> l) {
        listenOnAdd = true;
        listenOnAddListenerDecorator = dec;
        listenOnAddListener = l;
        return this;
    }

    @Override
    public ICommandButtons<Object> listenOnAdd(IRowListener<IRows<Object>> l) {
        listenOnAdd = true;
        listenOnAddListener = l;
        return this;
    }

    @Override
    public ICommandButtons<Object> listenOnRemove(IMultiRowListener<IRows<Object>> l) {
        listenOnRemove = true;
        listenOnRemoveListener = l;
        return this;
    }

    @Override
    public ICommandButtons<Object> listenOnMoveUp(IMoveRowListener<IRows<Object>> l) {
        listenOnMoveUp = true;
        listenOnMoveUpListener = l;
        return this;
    }

    @Override
    public ICommandButtons<Object> listenOnMoveDown(IMoveRowListener<IRows<Object>> l) {
        listenOnMoveDown = true;
        listenOnMoveDownListener = l;
        return this;
    }

    @Override
    public ICommandButtons<Object> listenOnShow(final IRowListener<IRows<Object>> l) {
        listenOnShow = true;
        listenOnShowListener = l;
        doubleClickListener(l);
        return this;
    }

    private void doubleClickListener(final IRowListener<IRows<Object>> l) {
        widget.addTableClickListener(new IScrollTableClickListener() {

            @Override
            public void onClick(Object identifier, int rowIndex) {
                l.onClick(identifier, rowIndex, new CallbackTemplate<IRows<Object>>() {

                    @Override
                    public void onSuccess(IRows<Object> result) {
                    }
                });
            }
        }).doubleClick();
    }

    @Override
    public ICommandButtons<Object> listenOnEdit(IRowListener<IRows<Object>> l) {
        listenOnEdit = true;
        listenOnEditListener = l;
        doubleClickListener(l);
        return this;
    }

    @Override
    public void decorate(IGridCell container) {
        widget.selection().multi().addChangeListener(this);
        panel = new ToolbarImpl(container);
        if (ALIGN_END) {
            panel.align().end();
            addRemove();
            addAdd();
        } else {
            addAdd();
            addRemove();
        }
        if (listenOnShow) {
            show = clickable(panel.add(), "Show", true);
            show.addClickListener(new Update(listenOnShowListener));
            show.clickable(!widget.preselectedList.isEmpty());
        }
        if (listenOnEdit) {
            edit = clickable(panel.add(), "Edit", true);
            final Update clickListener = new Update(listenOnEditListener);
            edit.addClickListener(clickListener);
            edit.clickable(!widget.preselectedList.isEmpty());
            widget.addTableClickListener(new IScrollTableClickListener() {

                @Override
                public void onClick(Object identifier, int rowIndex) {
                    widget.rows.selected(rowIndex);
                    selectionList.add(identifier);
                    clickListener.onClick();
                }
            }).doubleClick();
        }
        if (listenOnMoveUp) {
            imageUpMax = addMoveImage(ScrollTableWidgetImpl.ARROW_UP, listenOnMoveUpListener, Integer.MIN_VALUE);
            imageUp = addMoveImage(ScrollTableWidgetImpl.ARROW_UP, listenOnMoveUpListener, -1);
        }
        if (listenOnMoveDown) {
            imageDown = addMoveImage(ScrollTableWidgetImpl.ARROW_DOWN, listenOnMoveDownListener, 1);
            imageDownMax = addMoveImage(ScrollTableWidgetImpl.ARROW_DOWN, listenOnMoveDownListener, Integer.MAX_VALUE);
        }
        panel.adjustHeights();
    }

    void addRemove() {
        if (listenOnRemove) {
            remove = clickable(panel.add(), "Remove", true);
            remove.addClickListener(new Update(listenOnRemoveListener, true));
            remove.clickable(false);
        }
    }

    void addAdd() {
        if (listenOnAdd) {
            IClickable<?> image = listenOnAddListenerDecorator.decorate(panel);
            image.addClickListener(new Update(listenOnAddListener));
        }
    }

    private IClickable<?> addMoveImage(String resource, IMoveRowListener<IRows<Object>> listenOnMoveUpListener2, int i) {
        String res = null;
        if (resource.equals(ScrollTableWidgetImpl.ARROW_UP) && i == Integer.MIN_VALUE) res = "Top";
        if (resource.equals(ScrollTableWidgetImpl.ARROW_UP) && i == -1) res = "Up";
        if (resource.equals(ScrollTableWidgetImpl.ARROW_DOWN) && i == 1) res = "Down";
        if (resource.equals(ScrollTableWidgetImpl.ARROW_DOWN) && i == Integer.MAX_VALUE) res = "Bottom";
        IClickable<?> image = clickable(panel.add(), res, false);
        image.addClickListener(new Move(listenOnMoveUpListener2, i));
        boolean canClick = widget.preselectedList.size() == 1;
        if (canClick) {
            canClick &= i > 0 || widget.preselectedIndex() > 0;
            canClick &= i < 0 || widget.preselectedIndex() < widget.rows.size() - 1;
        }
        image.clickable(canClick);
        return image;
    }

    @Override
    public void onChange(List<Object> selection) {
        selectionList = selection;
        updateButtons();
    }

    void updateButtons() {
        int index = selectionIndex();
        if (imageUp != null) {
            boolean up = index > 0 && selectionList.size() == 1;
            imageUp.clickable(up);
            imageUpMax.clickable(up);
        }
        if (imageDown != null) {
            boolean c = index < widget.rows.size() - 1 && selectionList.size() == 1;
            imageDownMax.clickable(c);
            imageDown.clickable(c);
        }
        if (remove != null) {
            boolean c = selectionList.size() >= 1;
            if (c) for (Object o : selectionList) c &= widget.rows.deletable(widget.rows.find(o));
            remove.clickable(c);
        }
        if (show != null) {
            boolean c = lastSelected() != null && selectionList.size() == 1;
            show.clickable(c);
        }
        if (edit != null) {
            boolean c = lastSelected() != null && selectionList.size() == 1;
            edit.clickable(c);
        }
    }

    int getSelectionIndex() {
        return selectionList.isEmpty() ? -1 : widget.rows.find(lastSelected());
    }

    public void reset() {
        selectionList.clear();
    }

    void selectionList(List<Object> preselectedList) {
        if (preselectedList != null && !preselectedList.isEmpty() && widget.rows != null) {
            selectionList = new LinkedList<Object>();
            for (Object o : preselectedList) if (widget.rows.find(o) != -1) selectionList.add(o);
        }
    }
}
