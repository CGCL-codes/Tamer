package com.cell.gfx.gui;

import java.util.Vector;

/**
 * @author Waza
 *	在构造完Page时不能直接selectPage，必须先执行initComplete
 */
public class GroupPageSelect extends Group {

    public static UIRect HeadSelectedUI = UIRect.createBlankRect();

    public static UIRect HeadUnselectedUI = UIRect.createBlankRect();

    public static UIRect BackgroundUI = UIRect.createBlankRect();

    public class Page {

        private int Index;

        public final LabelBar Head;

        public final Vector<Item> Items = new Vector<Item>();

        public Page(String head) {
            Index = Pages.size();
            Head = new LabelBar(head, 100, 20);
            Head.CanFocus = true;
            Head.UserRect = HeadUnselectedUI;
            OwnerForm.appendItem(Head, X + Index * 100, Y);
            Pages.add(this);
        }

        public int getIndex() {
            return Index;
        }
    }

    public final int X;

    public final int Y;

    public final int W;

    public final int H;

    public final LabelBar Page;

    private Vector<Page> Pages = new Vector<Page>();

    private int SelectedPageIndex = -1;

    public GroupPageSelect(Form form, int x, int y, int w, int h) {
        X = x;
        Y = y;
        W = w;
        H = h;
        OwnerForm = form;
        Page = new LabelBar("", W, H - 20);
        OwnerForm.appendItem(Page, X, Y + 20);
        Page.UserRect = BackgroundUI;
        OwnerForm.appendGroup(this);
    }

    public int addPage(String text) {
        return new Page(text).Index;
    }

    public void clearPage(int pageIndex) {
        Page p = Pages.get(pageIndex);
        OwnerForm.deleteItems(p.Items);
        p.Items.clear();
    }

    public int addItem(int pageIndex, Item item, int x, int y) {
        Page p = Pages.get(pageIndex);
        item.X = Page.X + Page.UserRect.BorderSize + x;
        item.Y = Page.Y + Page.UserRect.BorderSize + y;
        p.Items.add(item);
        return p.Items.size() - 1;
    }

    public int addItem(int pageIndex, Item item) {
        Page p = Pages.get(pageIndex);
        item.X = Page.X + Page.UserRect.BorderSize + item.X;
        item.Y = Page.Y + Page.UserRect.BorderSize + item.Y;
        p.Items.add(item);
        return p.Items.size() - 1;
    }

    public int removeItem(int pageIndex, Item item) {
        Page p = Pages.get(pageIndex);
        p.Items.remove(item);
        return p.Items.size() - 1;
    }

    public int getItemX(int pageIndex, Item item) {
        Page p = Pages.get(pageIndex);
        return item.X - Page.X - Page.UserRect.BorderSize;
    }

    public int getItemY(int pageIndex, Item item) {
        Page p = Pages.get(pageIndex);
        return item.Y - Page.Y - Page.UserRect.BorderSize;
    }

    public int getPageWidth() {
        return Page.getWidth() - Page.UserRect.BorderSize * 2;
    }

    public int getPageHeight() {
        return Page.getHeight() - Page.UserRect.BorderSize * 2;
    }

    public int addDataList(int pageIndex, DataList datalist) {
        Page p = Pages.get(pageIndex);
        {
            p.Items.add(datalist.BackGround);
            p.Items.add(datalist.Title);
            p.Items.add(datalist.HeadStrip);
            p.Items.add(datalist.BeginPage);
            p.Items.add(datalist.NextPage);
            p.Items.add(datalist.Page);
            p.Items.add(datalist.PrewPage);
            p.Items.add(datalist.EndPage);
            for (Item item : datalist.GridColumnHeads) {
                p.Items.add(item);
            }
            for (Item[] items : datalist.GridRows) {
                for (Item item : items) {
                    p.Items.add(item);
                }
            }
        }
        return p.Items.size() - 1;
    }

    public int getSelectedPageIndex() {
        return SelectedPageIndex;
    }

    public void selectPage(int pageIndex) {
        if (SelectedPageIndex != pageIndex) {
            if (SelectedPageIndex >= 0 && SelectedPageIndex < Pages.size()) {
                Page p = Pages.get(SelectedPageIndex);
                p.Head.UserRect = HeadUnselectedUI;
                OwnerForm.deleteItems(p.Items);
            }
            SelectedPageIndex = pageIndex;
            if (SelectedPageIndex >= 0 && SelectedPageIndex < Pages.size()) {
                Page p = Pages.get(SelectedPageIndex);
                p.Head.UserRect = HeadSelectedUI;
                OwnerForm.appendItems(p.Items);
            }
        }
    }

    public void setPageHeadText(int index, String text) {
        Pages.elementAt(index).Head.setText(text);
    }

    public void setPageHeadsText(String[] texts) {
        for (Page p : Pages) {
            if (p.Index < texts.length) {
                p.Head.setText(texts[p.Index]);
            }
        }
    }

    public void setPageHeadUIRect(UIRect selectedUI, UIRect unselectedUI) {
        HeadUnselectedUI = unselectedUI;
        HeadSelectedUI = selectedUI;
        for (Page p : Pages) {
            if (p.Index == SelectedPageIndex) {
                p.Head.UserRect = HeadSelectedUI;
            } else {
                p.Head.UserRect = HeadUnselectedUI;
            }
        }
    }

    public void setPageBackGroundUIRect(UIRect uir) {
        BackgroundUI = uir;
        Page.UserRect = BackgroundUI;
    }

    public void refreshCurrentPage() {
        if (SelectedPageIndex >= 0 && SelectedPageIndex < Pages.size()) {
            Page p = Pages.get(SelectedPageIndex);
            OwnerForm.deleteItems(p.Items);
            OwnerForm.appendItems(p.Items);
        }
    }

    protected boolean itemAction(Command command, Item item, Group group) {
        if (group == this) {
            for (Page p : Pages) {
                if (p.Head == item) {
                    selectPage(p.Index);
                    return true;
                }
            }
        }
        return false;
    }

    public void initComplete() {
        if (SelectedPageIndex < 0) {
            for (int i = Pages.size() - 1; i >= 0; i--) {
                selectPage(i);
            }
        }
    }

    protected void update(Form form) {
        initComplete();
    }
}
