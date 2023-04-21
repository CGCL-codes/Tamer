package javax.microedition.lcdui;

import com.sun.midp.log.Logging;
import com.sun.midp.log.LogChannels;
import com.sun.midp.configurator.Constants;

/**
 * Layout management class for <code>Form</code>.
 * See DisplayableLF.java for naming convention.
 */
class LayoutManager {

    /**
     * Singleton design pattern. Obtain access using instance() method.
     */
    LayoutManager() {
        sizingBox = new int[3];
    }

    /**
     * Do layout.
     * SYNC NOTE: caller must hold LCDUILock around a call to this method
     *
     * @param layoutMode one of <code>FULL_LAYOUT</code> or 
     *                   <code>UPDATE_LAYOUT</code>
     * @param numOfLFs number of elements in the calling form
     * @param itemLFs reference to the items array of the calling form
     * @param inp_viewportWidth width of the screen area available for the form
     * @param inp_viewportHeight height of the screen area available 
     * for the form
     * @param viewable area needed for the content of the form
     */
    void lLayout(int layoutMode, ItemLFImpl[] itemLFs, int numOfLFs, int inp_viewportWidth, int inp_viewportHeight, int[] viewable) {
        viewportWidth = inp_viewportWidth;
        viewportHeight = inp_viewportHeight;
        if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
            Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "\n<<<<<<<<<< Doing " + (layoutMode == FULL_LAYOUT ? "FULL_LAYOUT" : "UPDATE_LAYOUT") + "... >>>>>>>>>>");
        }
        if (layoutMode == FULL_LAYOUT) {
            updateBlock(0, 0, true, itemLFs, numOfLFs, viewable);
        } else {
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "UPDATE_LAYOUT - START");
            }
            int anchorIndex = 0;
            int newLineIndex = 0;
            for (int index = 0; index < numOfLFs; index++) {
                if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                    Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "\n[" + itemLFs[index] + "]" + "BEFORE: index: " + index + "\t[" + itemLFs[index].bounds[X] + "," + itemLFs[index].bounds[Y] + "," + itemLFs[index].bounds[WIDTH] + "," + itemLFs[index].bounds[HEIGHT] + "]\t newLine?" + itemLFs[index].isNewLine + " lineHeight=" + itemLFs[index].rowHeight + "\t actualBoundsInvalid[" + itemLFs[index].actualBoundsInvalid[X] + "," + itemLFs[index].actualBoundsInvalid[Y] + "," + itemLFs[index].actualBoundsInvalid[WIDTH] + "," + itemLFs[index].actualBoundsInvalid[HEIGHT] + "]\t ** viewable: " + index + "\t[" + viewportWidth + "," + viewable[HEIGHT] + "]");
                }
                if (itemLFs[index].actualBoundsInvalid[WIDTH] || itemLFs[index].actualBoundsInvalid[X]) {
                    if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                        Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "> WIDTH or X is invalid!");
                    }
                    index = updateBlock(anchorIndex, index, false, itemLFs, numOfLFs, viewable);
                    anchorIndex = index + 1;
                } else if (itemLFs[index].actualBoundsInvalid[HEIGHT]) {
                    int h = itemLFs[index].bounds[HEIGHT];
                    int ph = itemLFs[index].lGetAdornedPreferredHeight(itemLFs[index].bounds[WIDTH]);
                    if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                        Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "> HEIGHT is invalid  from:" + h + " to:" + ph);
                    }
                    if (h != ph) {
                        itemLFs[index].lSetSize(itemLFs[index].bounds[WIDTH], ph);
                        itemLFs[index].rowHeight += (ph - h);
                        itemLFs[index].actualBoundsInvalid[HEIGHT] = false;
                        if (numOfLFs > index + 1) {
                            itemLFs[index + 1].actualBoundsInvalid[Y] = true;
                            updateVertically(index + 1, itemLFs, numOfLFs, viewable);
                        } else {
                            viewable[HEIGHT] += (ph - h);
                        }
                    }
                } else if (itemLFs[index].actualBoundsInvalid[Y]) {
                    if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                        Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "> *only* Y is invalid for #" + index);
                    }
                    updateVertically(index, itemLFs, numOfLFs, viewable);
                    if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                        Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "> Y - done");
                    }
                } else {
                    if (itemLFs[index].isNewLine) {
                        if (itemLFs[index].equateNLB() || ((index > 0) && (itemLFs[index - 1].equateNLA()))) {
                            anchorIndex = index;
                            newLineIndex = index;
                        } else {
                            anchorIndex = newLineIndex;
                            newLineIndex = index;
                        }
                    }
                }
                if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                    Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "AFTER: index: " + index + "\t[" + itemLFs[index].bounds[X] + "," + itemLFs[index].bounds[Y] + "," + itemLFs[index].bounds[WIDTH] + "," + itemLFs[index].bounds[HEIGHT] + "]\t newLine?" + itemLFs[index].isNewLine + " lineHeight=" + itemLFs[index].rowHeight + "\t actualBoundsInvalid[" + itemLFs[index].actualBoundsInvalid[X] + "," + itemLFs[index].actualBoundsInvalid[Y] + "," + itemLFs[index].actualBoundsInvalid[WIDTH] + "," + itemLFs[index].actualBoundsInvalid[HEIGHT] + "]\t ** viewable: " + index + "\t[" + viewportWidth + "," + viewable[HEIGHT] + "]");
                }
            }
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "UPDATE_LAYOUT - DONE");
            }
        }
        if (numOfLFs == 0) {
            viewable[HEIGHT] = 0;
        }
    }

    /**
     * Used both to do a full layout or just update a layout.
     *
     * assumptions: startIndex<=invalidIndex
     * 
     * @param startIndex The index to start the layout. Should start a row.
     * @param invalidIndex The index causing the re-layout, should
     *        be equal or greater than startIndex
     * @param fullLayout if <code>true</code>, does a full layout and ignores
     *                   the rest of the parameters sent to this method.
     * @param itemLFs reference to the items array of the calling form
     * @param numOfLFs number of elements in the calling form
     * @param viewable area needed for the content of the form
     *
     * @return the index of the last <code>Item</code> laid out
     */
    private int updateBlock(int startIndex, int invalidIndex, boolean fullLayout, ItemLFImpl[] itemLFs, int numOfLFs, int[] viewable) {
        if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
            Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "\n - updateBlock(START=" + startIndex + ", INVALID=" + invalidIndex + ", Full Layout=" + fullLayout + ") {");
        }
        int oldWidth = viewable[WIDTH];
        int oldHeight = viewable[HEIGHT];
        if (numOfLFs == 0) {
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, " we don't have any Items, just return }");
            }
            return 0;
        }
        int rowStart;
        if (fullLayout) {
            rowStart = 0;
        } else {
            rowStart = startIndex;
        }
        sizingBox[X] = 0;
        sizingBox[Y] = 0;
        sizingBox[WIDTH] = viewportWidth;
        viewable[WIDTH] = viewportWidth;
        if (fullLayout) {
            viewable[HEIGHT] = 0;
        } else if (numOfLFs > 1 && startIndex > 0) {
            sizingBox[Y] = itemLFs[startIndex - 1].bounds[Y] + itemLFs[startIndex - 1].rowHeight;
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "sizingBox[Y]=" + sizingBox[Y]);
            }
        }
        int lineHeight = 0;
        int pW, pH;
        int curAlignment = Item.LAYOUT_LEFT;
        for (int index = startIndex; index < numOfLFs; index++) {
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "..\n\tFOR LOOP: startIndex=" + startIndex + " index=[" + index + "] invalidIndex=" + invalidIndex);
            }
            if (itemLFs[index].shouldHShrink()) {
                pW = itemLFs[index].lGetAdornedMinimumWidth();
            } else {
                if (itemLFs[index].lGetLockedWidth() != -1) {
                    pW = itemLFs[index].lGetLockedWidth();
                } else {
                    pW = itemLFs[index].lGetAdornedPreferredWidth(itemLFs[index].lGetLockedHeight());
                    if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                        Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, " no shrink - locked w - pW=" + pW + " viewable[width]=" + viewable[WIDTH]);
                    }
                }
            }
            if (!Constants.SCROLLS_HORIZONTAL && (pW > viewable[WIDTH])) {
                pW = viewable[WIDTH];
            }
            boolean newLine = (index > 0 && itemLFs[index - 1].equateNLA() || itemLFs[index].equateNLB() || pW > sizingBox[WIDTH]);
            if (isImplicitLineBreak(curAlignment, index, itemLFs)) {
                curAlignment = itemLFs[index].getLayout() & LAYOUT_HMASK;
                newLine = true;
            }
            if (newLine && (lineHeight > 0)) {
                if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                    Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "   --new line--");
                }
                try {
                    boolean wasNewLine = itemLFs[index].isNewLine;
                    itemLFs[index].isNewLine = true;
                    lineHeight = layoutRowHorizontal(rowStart, index - 1, sizingBox[WIDTH], lineHeight, itemLFs);
                    layoutRowVertical(rowStart, index - 1, lineHeight, itemLFs, numOfLFs);
                    if (fullLayout) {
                        if (numOfLFs > 1) {
                            viewable[HEIGHT] += lineHeight;
                        } else {
                            viewable[HEIGHT] += lineHeight + 1;
                        }
                    } else {
                        if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                            Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "** 1 ** row height=" + lineHeight);
                        }
                        if (wasNewLine && index > invalidIndex) {
                            itemLFs[index].actualBoundsInvalid[Y] = true;
                            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                                Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, " returning index-1 and " + "marking next Y as invalid. }");
                            }
                            return (index - 1);
                        }
                        if (!wasNewLine) {
                            itemLFs[index].actualBoundsInvalid[X] = true;
                        }
                    }
                } catch (Throwable t) {
                    Display.handleThrowable(t);
                }
                sizingBox[X] = 0;
                if (fullLayout) {
                    sizingBox[Y] = viewable[HEIGHT];
                } else {
                    sizingBox[Y] = itemLFs[index - 1].bounds[Y] + itemLFs[index - 1].rowHeight;
                    if (numOfLFs <= 1) {
                        sizingBox[Y] += 1;
                    }
                    if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                        Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "** 2 **   sizingBox[Y]=" + sizingBox[Y]);
                    }
                }
                sizingBox[WIDTH] = viewportWidth;
                lineHeight = 0;
                rowStart = index;
                itemLFs[index].isNewLine = true;
                if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                    Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "  (new line end)");
                }
            } else {
                if (index == 0 || newLine) {
                    itemLFs[index].isNewLine = true;
                } else {
                    if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                        Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "** " + index + " is not a new line **");
                    }
                    itemLFs[index].isNewLine = false;
                }
            }
            pH = getItemHeight(index, pW, itemLFs);
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, " updateBlock.. pH = " + pH);
            }
            if (oldWidth != viewportWidth || oldHeight != viewportHeight || itemLFs[index].bounds[WIDTH] != pW || itemLFs[index].bounds[HEIGHT] != pH) {
                itemLFs[index].sizeChanged = true;
            }
            if (!fullLayout && (index > invalidIndex)) {
                if (itemLFs[index].equateNLB() || ((index > 0) && (itemLFs[index - 1].equateNLA()))) {
                    if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                        Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "** stop layout, explicit lb **\n}");
                    }
                    itemLFs[index].actualBoundsInvalid[Y] = true;
                    return (index - 1);
                } else if (itemLFs[index].bounds[X] == sizingBox[X] && itemLFs[index].bounds[WIDTH] == pW && itemLFs[index].bounds[HEIGHT] == pH) {
                    if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                        Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "\n** no need to layout **\n}");
                    }
                    itemLFs[index].actualBoundsInvalid[X] = false;
                    itemLFs[index].actualBoundsInvalid[Y] = true;
                    itemLFs[index].actualBoundsInvalid[WIDTH] = false;
                    itemLFs[index].actualBoundsInvalid[HEIGHT] = false;
                    return (index - 1);
                }
            }
            itemLFs[index].lSetSize(pW, pH);
            itemLFs[index].lSetLocation(sizingBox[X], sizingBox[Y]);
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "[F] index (" + index + " lineHeight == " + lineHeight + ") set height to:" + pH);
            }
            itemLFs[index].actualBoundsInvalid[X] = false;
            itemLFs[index].actualBoundsInvalid[Y] = false;
            itemLFs[index].actualBoundsInvalid[WIDTH] = false;
            itemLFs[index].actualBoundsInvalid[HEIGHT] = false;
            if (pH > lineHeight) {
                lineHeight = pH;
            }
            if (pW > 0) {
                sizingBox[WIDTH] -= pW;
                if (sizingBox[WIDTH] < 0) {
                    sizingBox[WIDTH] = 0;
                }
                sizingBox[X] += pW;
            }
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "\t\tindex: " + index + "\t[" + itemLFs[index].bounds[X] + "," + itemLFs[index].bounds[Y] + "," + itemLFs[index].bounds[WIDTH] + "," + itemLFs[index].bounds[HEIGHT] + "]");
            }
        }
        try {
            int oldRowHeight = itemLFs[rowStart].rowHeight;
            lineHeight = layoutRowHorizontal(rowStart, numOfLFs - 1, sizingBox[WIDTH], lineHeight, itemLFs);
            int rowY = itemLFs[rowStart].bounds[Y];
            layoutRowVertical(rowStart, numOfLFs - 1, lineHeight, itemLFs, numOfLFs);
            viewable[HEIGHT] = rowY + lineHeight;
        } catch (Throwable t) {
            Display.handleThrowable(t);
        }
        if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
            Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, " returning invalidIndex:" + invalidIndex + " }");
        }
        return invalidIndex;
    }

    /**
     * Calculating how many pixels should the <pre>startIndex<> item move up
     * or down, and loop from this item until the end, adding the delta
     * to all these items.
     * We know where this startIndex should be, we know where it is
     * now, so we can know how much to move everything.
     *
     * The viewable height is updated accordingly.
     *
     * @param startIndex the index of the first item that should move
     *                   up or down. It should be first in its row, 
     *                   and the Item before it should be laid out 
     *                   correctly, with rowHeight set up.
     * @param itemLFs reference to the items array of the calling form
     * @param numOfLFs number of elements in the calling form
     * @param viewable area needed for the content of the form
     */
    private void updateVertically(int startIndex, ItemLFImpl[] itemLFs, int numOfLFs, int[] viewable) {
        if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
            Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "### in updateVertically for #" + startIndex + ".\t");
        }
        int deltaY = 0;
        int newY = 0;
        if (startIndex == 0) {
            newY = 0;
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "newY=" + newY);
            }
        } else {
            newY = itemLFs[startIndex - 1].bounds[Y] + itemLFs[startIndex - 1].rowHeight;
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, " itemLFs[si-1].bounds[Y]=" + itemLFs[startIndex - 1].bounds[Y] + " itemLFs[si-1].rowHeight=" + itemLFs[startIndex - 1].rowHeight);
            }
        }
        if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
            Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, ">>> CustomItemLFImpl -- lRepaint()" + " itemLFs[si].bounds[Y]=" + itemLFs[startIndex].bounds[Y] + " newY=" + newY);
        }
        deltaY = newY - itemLFs[startIndex].bounds[Y];
        if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
            Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, " delta= " + deltaY);
        }
        if (deltaY == 0) {
            itemLFs[startIndex].actualBoundsInvalid[Y] = false;
            return;
        }
        for (int i = startIndex; i < numOfLFs; i++) {
            itemLFs[i].lMove(0, deltaY);
        }
        itemLFs[startIndex].actualBoundsInvalid[Y] = false;
        viewable[HEIGHT] += deltaY;
    }

    /**
     * After the contents of a row have been determined, layout the
     * items on that row, taking into account the individual items'
     * horizontally oriented layout directives.
     *
     * @param rowStart the index of the first row element
     * @param rowEnd the index of the last row element
     * @param hSpace the amount of empty space in pixels in this row before 
     *               inflation
     * @param rowHeight the old row height
     * @param itemLFs reference to the items array of the calling form
     *
     * @return the new rowHeight for this row after all of the inflations
     */
    private int layoutRowHorizontal(int rowStart, int rowEnd, int hSpace, int rowHeight, ItemLFImpl[] itemLFs) {
        if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
            Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "[F] layoutRowHorizontal -- rowStart=" + rowStart + " rowEnd=" + rowEnd + " hSpace=" + hSpace + " rowHeight=" + rowHeight);
        }
        hSpace = inflateHShrinkables(rowStart, rowEnd, hSpace, itemLFs);
        hSpace = inflateHExpandables(rowStart, rowEnd, hSpace, itemLFs);
        rowHeight = 0;
        for (int i = rowStart; i <= rowEnd; i++) {
            if (rowHeight < itemLFs[i].bounds[HEIGHT]) {
                rowHeight = itemLFs[i].bounds[HEIGHT];
            }
        }
        if (hSpace == 0) {
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "[F] layoutRowHorizontal -- done -- " + "(hSpace == 0) -- returning " + rowHeight);
            }
            return rowHeight;
        }
        int curAlignment = getCurHorAlignment(itemLFs, rowStart);
        switch(curAlignment) {
            case Item.LAYOUT_CENTER:
                hSpace = hSpace / 2;
            case Item.LAYOUT_RIGHT:
                for (; rowStart <= rowEnd; rowStart++) {
                    itemLFs[rowEnd].lMove(hSpace, 0);
                }
                break;
            case Item.LAYOUT_LEFT:
            default:
                break;
        }
        if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
            Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "[F] layoutRowHorizontal -- done " + "-- returning " + rowHeight);
        }
        return rowHeight;
    }

    /**
     * Gets the current horizontal alignment of the item. If Item's
     * horizontal layout bits are not set its current horizontal
     * alignment is the same as of the previous Item.
     * 
     * @param itemLFs reference to the items array of the calling form
     * @param index the index of an item in the itemLFs array which 
     *        current horizontal alignment needs to be found out
     * @return currentl horizontal alignment of an Item with passed in 
     *         index
     */
    private int getCurHorAlignment(ItemLFImpl[] itemLFs, int index) {
        for (int hAlign, i = index; i >= 0; i--) {
            hAlign = itemLFs[i].getLayout() & LAYOUT_HMASK;
            if (hAlign == 0) {
                continue;
            }
            return hAlign;
        }
        return Item.LAYOUT_LEFT;
    }

    /**
     * Inflate all the horizontally 'shrinkable' items on a row.
     *
     * @param rowStart the index of the first row element
     * @param rowEnd the index of the last row element
     * @param space the amount of empty space left in pixels in this row
     * @param itemLFs reference to the items array of the calling form
     *
     * @return the amount of empty space on this row after shinkage
     */
    private int inflateHShrinkables(int rowStart, int rowEnd, int space, ItemLFImpl[] itemLFs) {
        if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
            Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "[F] inflateHShrinkables -- rowStart=" + rowStart + " rowEnd=" + rowEnd + " space=" + space);
        }
        if (space == 0) {
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "[F] inflateHShrinkables -- returning " + "(space == 0)");
            }
            return 0;
        }
        int baseline = Integer.MAX_VALUE;
        int pW, prop = 0;
        for (int i = rowStart; i <= rowEnd; i++) {
            if (itemLFs[i].shouldHShrink()) {
                pW = itemLFs[i].lGetLockedWidth();
                if (pW == -1) {
                    pW = itemLFs[i].lGetAdornedPreferredWidth(itemLFs[i].lGetLockedHeight());
                }
                prop = pW - itemLFs[i].lGetAdornedMinimumWidth();
                if (prop > 0 && prop < baseline) {
                    baseline = prop;
                }
            }
        }
        if (baseline == Integer.MAX_VALUE) {
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "[F] inflateHShrinkables -- returning " + "(baseline == Integer.MAX_VALUE) space == " + space);
            }
            return space;
        }
        prop = 0;
        for (int i = rowStart; i <= rowEnd; i++) {
            if (itemLFs[i].shouldHShrink()) {
                pW = itemLFs[i].lGetLockedWidth();
                if (pW == -1) {
                    pW = itemLFs[i].lGetAdornedPreferredWidth(itemLFs[i].lGetLockedHeight());
                }
                prop += ((pW - itemLFs[i].lGetAdornedMinimumWidth()) / baseline);
            }
        }
        if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
            Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "[F] inflateHShrinkables -- prop == " + prop);
        }
        int adder = space / prop;
        for (int i = rowStart; i <= rowEnd; i++) {
            if (itemLFs[i].shouldHShrink()) {
                if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                    Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "### item " + i + " before shrinking is:" + itemLFs[i].bounds[WIDTH]);
                }
                pW = itemLFs[i].lGetLockedWidth();
                if (pW == -1) {
                    pW = itemLFs[i].lGetAdornedPreferredWidth(itemLFs[i].lGetLockedHeight());
                }
                space = pW - itemLFs[i].lGetAdornedMinimumWidth();
                prop = adder * (space / baseline);
                if (space > prop) {
                    space = prop;
                }
                itemLFs[i].lSetSize(itemLFs[i].bounds[WIDTH] + space, getItemHeight(i, itemLFs[i].bounds[WIDTH] + space, itemLFs));
                for (int j = i + 1; j <= rowEnd; j++) {
                    itemLFs[j].lMove(space, 0);
                }
                if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                    Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "### item " + i + " shrank to:" + itemLFs[i].bounds[WIDTH]);
                }
            }
        }
        space = viewportWidth - (itemLFs[rowEnd].bounds[X] + itemLFs[rowEnd].bounds[WIDTH]);
        if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
            Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "[F] inflateHShrinkables -- " + "returning (end). space == " + space);
        }
        return space;
    }

    /**
     * Inflate all the horizontally 'expandable' items on a row.
     *
     * @param rowStart the index of the first row element
     * @param rowEnd the index of the last row element
     * @param space the amount of empty space on this row
     * @param itemLFs reference to the items array of the calling form
     *
     * @return the amount of empty space after expansion
     */
    private int inflateHExpandables(int rowStart, int rowEnd, int space, ItemLFImpl[] itemLFs) {
        if (space == 0) {
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "[F] inflateHExpandables -- " + "returning (space == 0)");
            }
            return 0;
        }
        int numExp = 0;
        for (int i = rowStart; i <= rowEnd; i++) {
            if (itemLFs[i].shouldHExpand()) {
                numExp++;
            }
        }
        if (numExp == 0 || space < numExp) {
            if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "[F] inflateHExpandables -- returning " + "(numExp == 0 || space < numExp) space = " + space);
            }
            return space;
        }
        space = space / numExp;
        for (int i = rowStart; i <= rowEnd; i++) {
            if (itemLFs[i].shouldHExpand()) {
                itemLFs[i].lSetSize(itemLFs[i].bounds[WIDTH] + space, getItemHeight(i, itemLFs[i].bounds[WIDTH] + space, itemLFs));
                for (int j = i + 1; j <= rowEnd; j++) {
                    itemLFs[j].lMove(space, 0);
                }
            }
        }
        space = viewportWidth - (itemLFs[rowEnd].bounds[X] + itemLFs[rowEnd].bounds[WIDTH]);
        if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
            Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "[F] inflateHExpandables -- " + "returning (end) space = " + space);
        }
        return space;
    }

    /**
     * After the contents of a row have been determined, layout the
     * items on that row, taking into account the individual items'
     * vertically oriented layout directives.
     *
     * @param rowStart the index of the first row element
     * @param rowEnd the index of the last row element
     * @param itemLFs reference to the items array of the calling form
     * @param lineHeight the overall height in pixels of the line
     * @param numOfLFs number of elements in the calling form
     */
    private void layoutRowVertical(int rowStart, int rowEnd, int lineHeight, ItemLFImpl[] itemLFs, int numOfLFs) {
        int space = 0;
        int pH = 0;
        for (int i = rowStart; i <= rowEnd; i++) {
            itemLFs[i].rowHeight = lineHeight;
            if (itemLFs[i].shouldVExpand()) {
                itemLFs[i].lSetSize(itemLFs[i].bounds[WIDTH], lineHeight);
            } else if (itemLFs[i].shouldVShrink()) {
                pH = itemLFs[i].lGetLockedHeight();
                if (pH == -1) {
                    pH = itemLFs[i].lGetAdornedPreferredHeight(itemLFs[i].bounds[WIDTH]);
                }
                if (pH > lineHeight) {
                    pH = lineHeight;
                }
                itemLFs[i].lSetSize(itemLFs[i].bounds[WIDTH], pH);
            }
            switch(itemLFs[i].getLayout() & LAYOUT_VMASK) {
                case Item.LAYOUT_VCENTER:
                    space = lineHeight - itemLFs[i].bounds[HEIGHT];
                    if (space > 0) {
                        itemLFs[i].lMove(0, space / 2);
                    }
                    break;
                case Item.LAYOUT_TOP:
                    break;
                case Item.LAYOUT_BOTTOM:
                default:
                    space = lineHeight - itemLFs[i].bounds[HEIGHT];
                    if (space > 0) {
                        itemLFs[i].lMove(0, space);
                    }
                    if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
                        Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "Default V layout -- space = " + space + " itemLFs[i].Y = " + itemLFs[i].bounds[Y]);
                    }
            }
        }
        if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
            Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, "[F] layoutRowVertical -- done");
        }
    }

    /**
     * This method checks if we need a new line due to change in 
     * horizontal alignment. If horizontal alignment is not set on 
     * <code>Item</code> with index thisItem then current horizontal 
     * alignment is not changed and no row break is needed.
     *
     * @param curAlignment current horizontal alignment until this Item
     * @param thisItem index of the <code>Item</code> from which to start 
     *                 the scan
     * @param itemLFs reference to the items array of the calling form
     *
     * @return <code>true</code> if a new line is needed
     */
    private boolean isImplicitLineBreak(int curAlignment, int thisItem, ItemLFImpl[] itemLFs) {
        if (thisItem == 0) {
            return false;
        }
        int hAlign = itemLFs[thisItem].getLayout() & LAYOUT_HMASK;
        if (hAlign == 0) {
            return false;
        }
        return (hAlign != curAlignment);
    }

    /**
     * Get item's height based on the width.
     *
     * @param index the index of the item which height is being calculated 
     * @param pW the width set for the item
     * @param itemLFs reference to the items array of the calling form
     *
     * @return the height of the item
     */
    private int getItemHeight(int index, int pW, ItemLFImpl[] itemLFs) {
        int pH;
        if (itemLFs[index].shouldVShrink()) {
            pH = itemLFs[index].lGetAdornedMinimumHeight();
        } else {
            pH = itemLFs[index].lGetLockedHeight();
            if (pH == -1) {
                pH = itemLFs[index].lGetAdornedPreferredHeight(pW);
            }
        }
        if (!Constants.SCROLLS_VERTICAL && pH > viewportHeight) {
            pH = viewportHeight;
        }
        if (Logging.REPORT_LEVEL <= Logging.INFORMATION) {
            Logging.report(Logging.INFORMATION, LogChannels.LC_HIGHUI_FORM_LAYOUT, " LayoutManager- getItemHeight(" + index + "," + pW + ") returns " + pH);
        }
        return pH;
    }

    /**
     * Singleton design pattern: obtain access to the single instance of
     * this class using this method.
     *
     * @return Single instance of LayoutManager
     */
    static LayoutManager instance() {
        return singleInstance;
    }

    /** 
     * A bit mask to capture the horizontal layout directive of an item.
     */
    static final int LAYOUT_HMASK = 0x03;

    /** 
     * A bit mask to capture the vertical layout directive of an item. 
     */
    static final int LAYOUT_VMASK = 0x30;

    /**
     * 'sizingBox' is a [x,y,w,h] array used for dynamic sizing of 
     * <code>Item</code>s during the layout. It starts with the size of the 
     * viewport, but can shrink or grow according to the <code>Item</code> 
     * it tries to lay out.
     *
     * It is used by layoutBlock and layoutRow.
     */
    private int[] sizingBox;

    /** Do a full layout. */
    static final int FULL_LAYOUT = -1;

    /** Only update layout. */
    static final int UPDATE_LAYOUT = -2;

    /**
     * Single instance of the LayoutManager class.
     */
    static LayoutManager singleInstance = new LayoutManager();

    /** Used as an index into the viewport[], for the x origin. */
    static final int X = DisplayableLFImpl.X;

    /** Used as an index into the viewport[], for the y origin. */
    static final int Y = DisplayableLFImpl.Y;

    /** Used as an index into the viewport[], for the width. */
    static final int WIDTH = DisplayableLFImpl.WIDTH;

    /** Used as an index into the viewport[], for the height. */
    static final int HEIGHT = DisplayableLFImpl.HEIGHT;

    /** Width of viewport, as passed to layout(). */
    int viewportWidth = 0;

    /** Height of viewport, as passed to layout(). */
    int viewportHeight = 0;
}
