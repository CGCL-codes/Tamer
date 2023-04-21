package net.infordata.ifw2.web.grds;

import net.infordata.ifw2.web.view.RendererContext;

public class ProportionalGridResizers {

    /**
   * @param minPageSize - the page size expected when referencedMinBrowserHeight
   * @param maxPageSize - the page size expected when referencedMaxBrowserHeight
   * @param referencedMinBrowserHeight
   * @param referencedMaxBrowserHeight
   */
    public static final IGridVertResizer getVertResizer(final int minPageSize, final int maxPageSize, final int referencedMinBrowserHeight, final int referencedMaxBrowserHeight) {
        final double rp = (double) (referencedMaxBrowserHeight - referencedMinBrowserHeight) / (maxPageSize - minPageSize);
        return new IGridVertResizer() {

            private static final long serialVersionUID = 1L;

            @Override
            public int getPageSize() {
                int browserHeight = RendererContext.get().getBrowserHeight();
                final int pgSize = minPageSize + (int) ((browserHeight - referencedMinBrowserHeight) / rp);
                int pageSize = Math.min(maxPageSize, (Math.max(minPageSize, pgSize)));
                return pageSize;
            }
        };
    }

    /**
   * @param minDispUnits - display units when referencedMinBrowserWidth
   * @param maxDispUnits - display units when referencedMaxBrowserWidth
   * @param referencedMinBrowserWidth
   * @param referencedMaxBrowserWidth
   */
    public static final IGridHorzResizer getHorzResizer(final int minDispUnits, final int maxDispUnits, final int referencedMinBrowserWidth, final int referencedMaxBrowserWidth) {
        final double rp = (double) (referencedMaxBrowserWidth - referencedMinBrowserWidth) / (maxDispUnits - minDispUnits);
        return new IGridHorzResizer() {

            private static final long serialVersionUID = 1L;

            @Override
            public int getDisplayedUnits() {
                int browserWidth = RendererContext.get().getBrowserWidth();
                final int du = minDispUnits + (int) ((browserWidth - referencedMinBrowserWidth) / rp);
                int dispUnits = Math.min(maxDispUnits, (Math.max(minDispUnits, du)));
                return dispUnits;
            }

            @Override
            public int adaptMaxDisplayedTextLength(int maxDisplayedTextLength) {
                int dispUnits = getDisplayedUnits();
                return (dispUnits * maxDisplayedTextLength) / minDispUnits;
            }
        };
    }
}
