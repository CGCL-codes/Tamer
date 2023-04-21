package com.g2d.display.ui;

import com.g2d.display.ui.layout.FillContainerLayout;

public class PageSelectPan extends PageSelect<com.g2d.display.ui.PageSelectPan.Page> {

    public PageSelectPan() {
        super();
    }

    @Override
    protected Page createPage(String text) {
        return new Page(text);
    }

    public static class Page extends com.g2d.display.ui.PageSelect.Page {

        Pan pan = new Pan();

        public Page(String headText) {
            super(headText);
            pan.setContainerLayout(new FillContainerLayout());
        }

        public Pan getPan() {
            return pan;
        }

        @Override
        protected UIComponent getPageView() {
            return pan;
        }
    }
}
