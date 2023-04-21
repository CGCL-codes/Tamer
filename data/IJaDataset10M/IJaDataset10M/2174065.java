package net.sergetk.bitmapfont.editor.ui.controls;

import java.util.LinkedList;
import net.sergetk.bitmapfont.editor.font.BitmapChar;
import net.sergetk.bitmapfont.editor.font.BitmapFont;
import net.sergetk.bitmapfont.editor.font.CharacterSet;
import net.sergetk.bitmapfont.editor.font.event.BitmapFontListener;
import net.sergetk.bitmapfont.editor.font.event.CharacterChangeEvent;
import net.sergetk.bitmapfont.editor.ui.controls.event.CharacterIndexListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.ScrollBar;

/**
 * 
 * @author sergey tkachev
 *
 */
public class BitmapCharacterTable extends Canvas implements PaintListener, BitmapFontListener, MouseListener, Listener {

    private BitmapFont font;

    private CharacterSet selectedChars = new CharacterSet();

    private CharacterSet currentChar = new CharacterSet();

    private int cellWidth, cellHeight;

    private int numColumns;

    private int numRows;

    private int selectedItem = -1;

    private int yPos;

    private int maxCharacterWidth;

    private LinkedList<CharacterIndexListener> listeners = new LinkedList<CharacterIndexListener>();

    public BitmapCharacterTable(Composite parent) {
        super(parent, SWT.V_SCROLL);
        addPaintListener(this);
        addMouseListener(this);
        addListener(SWT.Resize, this);
        getVerticalBar().addListener(SWT.Selection, new Listener() {

            public void handleEvent(Event e) {
                int newY = getVerticalBar().getSelection();
                int dY = newY - yPos;
                Rectangle clientArea = getClientArea();
                int srcY = dY < 0 ? 0 : dY;
                int dstY = dY < 0 ? -dY : 0;
                scroll(0, dstY, 0, srcY, clientArea.width, clientArea.height - Math.abs(dY), true);
                yPos = newY;
            }
        });
    }

    public void setFont(BitmapFont font) {
        this.font = font;
        if (font != null) {
            this.font.addListener(this);
        }
        selectedItem = -1;
        resize();
        redraw();
    }

    public void paintControl(PaintEvent event) {
        if (font == null || font.getChars().size() == 0) return;
        GC gc = event.gc;
        int y = -yPos;
        int i = 0;
        int count = font.getChars().size();
        for (int row = 0; row < numRows; row++) {
            int x = 0;
            for (int col = 0; col < numColumns; col++) {
                BitmapChar c = font.getChar(i);
                boolean isCurrent = i == selectedItem;
                boolean isSelected = isCurrent || selectedChars.contains(c.getCode());
                Color bgColor, fgColor;
                if (isSelected) {
                    bgColor = getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION);
                    fgColor = getDisplay().getSystemColor(SWT.COLOR_LIST_SELECTION_TEXT);
                } else {
                    bgColor = getDisplay().getSystemColor(SWT.COLOR_LIST_BACKGROUND);
                    fgColor = getDisplay().getSystemColor(SWT.COLOR_LIST_FOREGROUND);
                }
                gc.setBackground(bgColor);
                gc.fillRectangle(x, y, cellWidth, cellHeight);
                gc.drawRectangle(x, y, cellWidth, cellHeight);
                gc.drawImage(c.getImage(bgColor, fgColor), x + (cellWidth - c.getWidth()) / 2, y + 2);
                x += cellWidth;
                i++;
                if (i >= count) break;
            }
            y += cellHeight;
            if (i >= count) break;
        }
    }

    public void characterChanged(CharacterChangeEvent event) {
        if (maxCharacterWidth != font.getMaxCharWidth()) {
            resize();
            redraw();
        } else {
            drawChar(event.index);
        }
    }

    private void drawChar(int index) {
        Rectangle charRect = getCharacterBounds(index);
        redraw(charRect.x, charRect.y, charRect.width, charRect.height, false);
    }

    private Rectangle getCharacterBounds(int index) {
        int x = index % numColumns * cellWidth;
        int y = index / numColumns * cellHeight - yPos;
        return new Rectangle(x, y, cellWidth, cellHeight);
    }

    public void fontChanged() {
        resize();
        int charsCount = font.getChars().size();
        if (selectedItem >= charsCount) {
            setSelectedItem(charsCount - 1);
        } else {
            currentChar.clear();
            currentChar.addChar(font.getChar(selectedItem).getCode());
            redraw();
        }
    }

    public void mouseDoubleClick(MouseEvent event) {
    }

    public void mouseDown(MouseEvent event) {
        if (font != null) {
            int index = getIndex(event.x, event.y);
            if (index >= 0 && index < font.getChars().size()) {
                setSelectedItem(index);
            }
        }
    }

    private void setSelectedItem(int index) {
        if (selectedItem != index) {
            setCharacterIndex(index);
            for (CharacterIndexListener listener : listeners) {
                listener.selectedItem(index);
            }
        }
    }

    private int getIndex(int x, int y) {
        int xIndex = x / cellWidth;
        int yIndex = (y + yPos) / cellHeight;
        if (xIndex < 0 || xIndex > numColumns || yIndex < 0 || yIndex > numRows) return -1;
        int index = yIndex * numColumns + xIndex;
        if (index >= font.getChars().size()) {
            return -1;
        } else {
            return index;
        }
    }

    public void mouseUp(MouseEvent event) {
    }

    public void addListener(CharacterIndexListener listener) {
        listeners.add(listener);
    }

    public void removeListener(CharacterIndexListener listener) {
        listeners.remove(listener);
    }

    public void handleEvent(Event event) {
        if (event.type == SWT.Resize) {
            resize();
        }
    }

    private void resize() {
        int tableHeight = 0;
        Rectangle clientArea = getClientArea();
        if (font != null) {
            int count = font.getChars().size();
            if (count == 0) return;
            maxCharacterWidth = font.getMaxCharWidth();
            cellWidth = maxCharacterWidth + 4;
            cellHeight = font.getHeight() + 4;
            numColumns = (clientArea.width - 1) / cellWidth;
            numRows = (count + numColumns - 1) / numColumns;
            tableHeight = numRows * cellHeight + 1;
        }
        int dh = tableHeight - clientArea.height;
        ScrollBar verticalBar = getVerticalBar();
        if (dh > 0) {
            yPos = Math.min(yPos, dh);
            verticalBar.setMinimum(0);
            verticalBar.setMaximum(dh + verticalBar.getThumb());
            verticalBar.setIncrement(1);
            verticalBar.setEnabled(true);
        } else {
            verticalBar.setEnabled(false);
            yPos = 0;
        }
    }

    public void setCharacterIndex(int index) {
        if (font != null) {
            if (index != this.selectedItem) {
                selectedItem = index;
                currentChar.clear();
                BitmapChar character = font.getChar(index);
                if (character != null) {
                    currentChar.addChar(font.getChar(index).getCode());
                }
                redraw();
            }
        } else {
            redraw();
        }
    }

    public void statusChanged() {
    }

    /**
	 * Returns selected characters set
	 * @return selected characters
	 */
    public CharacterSet getSelectedCharacters() {
        if (font == null) {
            return null;
        } else if (selectedChars.isEmpty()) {
            return currentChar;
        } else {
            return selectedChars;
        }
    }
}
