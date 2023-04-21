package de.kugihan.dictionaryformids.hmi_j2me.lcdui_extension;

import javax.microedition.lcdui.CustomItem;
import javax.microedition.lcdui.Font;
import javax.microedition.lcdui.Graphics;
import de.kugihan.dictionaryformids.dataaccess.content.FontStyle;
import de.kugihan.dictionaryformids.dataaccess.content.PredefinedContent;
import de.kugihan.dictionaryformids.dataaccess.content.RGBColour;
import de.kugihan.dictionaryformids.dataaccess.content.SelectionMode;
import de.kugihan.dictionaryformids.general.Util;
import de.kugihan.dictionaryformids.hmi_common.content.StringColourItemText;
import de.kugihan.dictionaryformids.hmi_common.content.StringColourItemTextPart;
import de.kugihan.dictionaryformids.hmi_j2me.DictionarySettings;
import de.kugihan.dictionaryformids.hmi_j2me.mainform.MainForm;
import javax.microedition.lcdui.*;

public class StringColourItem extends CustomItem {

    private int width;

    private int height;

    private int rows;

    private int fontSize;

    private StringColourItemText stringColourItemText;

    private String[] itemTextWrap;

    private int[] itemLeft;

    private int nItems;

    private char newLineChar = '\n';

    private String newLineStr = "\n";

    private RGBColour backgroundColour;

    private boolean isUseBackgroundColour;

    private int systemBackgroundColour;

    MainForm applicationMainForm;

    Font defaultFont;

    private String selectedWord;

    private char selectChar = '║';

    boolean isSelectable;

    private int selectionColour = 0x00ffffff;

    private int selectionBackColour = 0x000000ff;

    private int firstSelectableItem;

    private int lastSelectableItem;

    private int currSelectItem;

    private int indexOfTranslationItem;

    String punctuationChars = "(){}[]<>+=',;:\\\n\t|&@$*?/!%#\" ";

    public StringColourItem(StringColourItemText stringColourItemTextParam, int screenWidthParam, int fontSizeParam, int indexOfItem, MainForm applicationMainFormParam) {
        super(null);
        applicationMainForm = applicationMainFormParam;
        indexOfTranslationItem = indexOfItem;
        stringColourItemText = stringColourItemTextParam;
        fontSize = fontSizeParam;
        width = screenWidthParam;
        nItems = stringColourItemText.size();
        itemTextWrap = new String[nItems];
        itemLeft = new int[nItems];
        isUseBackgroundColour = DictionarySettings.isUseBackgroundColour();
        if (isUseBackgroundColour) backgroundColour = DictionarySettings.getBackgroundColour();
        systemBackgroundColour = applicationMainForm.systemBackgroundColour;
        initValue();
        defaultFont = Font.getFont(Font.getDefaultFont().getFace(), Font.STYLE_PLAIN, fontSize);
        height = defaultFont.getHeight() * getRows();
    }

    public int getMinContentWidth() {
        return width;
    }

    public int getMinContentHeight() {
        return height;
    }

    public int getPrefContentWidth(int width) {
        return getMinContentWidth();
    }

    public int getPrefContentHeight(int height) {
        return getMinContentHeight();
    }

    public void setFontSize(int newSize) {
        initValue();
        fontSize = newSize;
        defaultFont = Font.getFont(Font.getDefaultFont().getFace(), Font.STYLE_PLAIN, fontSize);
        height = defaultFont.getHeight() * getRows();
        repaint();
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setWidth(int newWidth) {
        width = newWidth;
        initValue();
        height = defaultFont.getHeight() * getRows();
        repaint();
    }

    public StringColourItemText getColourItem() {
        return stringColourItemText;
    }

    public void clearSelectedWord() {
        itemTextWrap[currSelectItem] = ClearSelectChar(itemTextWrap[currSelectItem]);
        selectedWord = null;
        currSelectItem = -1;
        repaint();
    }

    public boolean isSelectableWord() {
        return isSelectable;
    }

    private int getRows() {
        rows = 1;
        int leftPos = 0;
        String text;
        for (int index = 0; index < nItems; index++) {
            leftPos = wrapText(index, leftPos);
        }
        return rows;
    }

    private int wrapText(int index, int leftPos) {
        Font curFont = setFont(stringColourItemText.getItemTextPart(index).getStyle());
        String itemText = itemTextWrap[index];
        boolean endOfLine = false;
        String wText = "";
        int nextLeftPos = 0;
        itemLeft[index] = leftPos;
        int pos = getBreakPos(itemText, curFont, width - leftPos);
        if (pos == 0) {
            if (itemText.startsWith(newLineStr)) itemText = itemText.substring(1);
            if (itemText.length() == 0) {
                endOfLine = true;
            }
            rows++;
            itemLeft[index] = 0;
        } else {
            wText = itemText.substring(0, pos);
            if (pos == itemText.length()) {
                endOfLine = true;
                nextLeftPos = leftPos + curFont.stringWidth(wText + " ");
            } else {
                itemText = itemText.substring(pos);
                rows++;
            }
        }
        while (!endOfLine) {
            if (itemText.startsWith(newLineStr)) itemText = itemText.substring(1);
            pos = getBreakPos(itemText, curFont, width);
            if (!wText.endsWith(newLineStr) && wText.length() > 0) wText += newLineChar;
            wText += itemText.substring(0, pos);
            if (pos == itemText.length()) {
                endOfLine = true;
                nextLeftPos = curFont.stringWidth(itemText + " ");
            } else {
                itemText = itemText.substring(pos);
                rows++;
            }
        }
        if (wText.endsWith(newLineStr)) {
            wText = wText.substring(0, wText.length() - 1);
            nextLeftPos = 0;
        }
        itemTextWrap[index] = wText;
        return nextLeftPos;
    }

    private int getBreakPos(String text, Font f, int w) {
        String s = new String(text);
        int pos = 0;
        if (s.startsWith(newLineStr)) {
            pos = 0;
        } else {
            pos = s.indexOf(newLineChar);
            if (pos != -1) {
                s = s.substring(0, pos);
            }
            if (f.stringWidth(s) <= w) {
                pos = s.length();
            } else {
                String punctuations = "+)}]><\\,;:-_&#!?% ";
                String s2 = "";
                pos = 0;
                int lastBreakPos = -1;
                do {
                    char c = s.charAt(pos);
                    s2 += c;
                    if (f.stringWidth(s2) > w) {
                        pos = lastBreakPos + 1;
                        break;
                    }
                    if (punctuations.indexOf(c) != -1) lastBreakPos = pos;
                    pos++;
                } while (pos < s.length());
                if (pos == 0) {
                    if (w == width) pos = w / f.stringWidth("w");
                }
            }
        }
        return pos;
    }

    private void initValue() {
        isSelectable = false;
        firstSelectableItem = -1;
        lastSelectableItem = -1;
        for (int index = 0; index < nItems; index++) {
            StringColourItemTextPart textPart = stringColourItemText.getItemTextPart(index);
            itemTextWrap[index] = textPart.getText();
            itemLeft[index] = -1;
            SelectionMode selectionMode = textPart.getSelectionMode();
            if (selectionMode == PredefinedContent.selectionModeAll || selectionMode == PredefinedContent.selectionModeSingle) {
                isSelectable = true;
                if (firstSelectableItem == -1) firstSelectableItem = index;
                lastSelectableItem = index;
            }
        }
        selectedWord = null;
        currSelectItem = -1;
    }

    protected void sizeChanged(int w, int h) {
        if (width != w) {
            applicationMainForm.updateStringColourItemWidth(indexOfTranslationItem, w, fontSize);
        }
    }

    public void paint(Graphics g, int w, int h) {
        Font currFont;
        RGBColour currRGBColor;
        FontStyle fontStyle;
        String rowText;
        String itemText;
        String selectedWordSplit;
        if (isUseBackgroundColour) g.setColor(backgroundColour.red, backgroundColour.green, backgroundColour.blue); else g.setColor(systemBackgroundColour);
        g.fillRect(0, 0, width, height);
        int row = -1;
        int firstRowPos = 0;
        int lastRowPos = 0;
        boolean firstRow = true;
        int leftPos;
        for (int index = 0; index < nItems; index++) {
            itemText = itemTextWrap[index];
            if (itemText.length() == 0) continue;
            fontStyle = stringColourItemText.getItemTextPart(index).getStyle();
            currRGBColor = stringColourItemText.getItemTextPart(index).getColour();
            g.setColor(currRGBColor.red, currRGBColor.green, currRGBColor.blue);
            currFont = setFont(fontStyle);
            g.setFont(currFont);
            firstRowPos = 0;
            firstRow = true;
            boolean contentSelection = false;
            do {
                lastRowPos = itemText.indexOf(newLineChar, firstRowPos);
                if (lastRowPos == -1) lastRowPos = itemText.length();
                rowText = itemText.substring(firstRowPos, lastRowPos);
                if (firstRow) {
                    leftPos = itemLeft[index];
                    if (leftPos == 0) row++;
                } else {
                    leftPos = 0;
                    row++;
                }
                if (selectedWord == null) g.drawString(rowText, leftPos, row * currFont.getHeight(), Graphics.LEFT | Graphics.TOP); else {
                    int selectPos = rowText.indexOf(selectChar);
                    if (selectPos == -1) {
                        if (!contentSelection) g.drawString(rowText, leftPos, row * currFont.getHeight(), Graphics.LEFT | Graphics.TOP); else {
                            g.setColor(selectionBackColour);
                            g.fillRect(leftPos, row * currFont.getHeight(), currFont.stringWidth(rowText), currFont.getHeight());
                            g.setColor(selectionColour);
                            g.drawString(rowText, leftPos, row * currFont.getHeight(), Graphics.LEFT | Graphics.TOP);
                            g.setColor(currRGBColor.red, currRGBColor.green, currRGBColor.blue);
                        }
                    } else {
                        int selectPos2 = rowText.indexOf(selectChar, selectPos + 1);
                        if (selectPos2 > 0) {
                            g.drawString(rowText.substring(0, selectPos), leftPos, row * currFont.getHeight(), Graphics.LEFT | Graphics.TOP);
                            leftPos += currFont.stringWidth(rowText.substring(0, selectPos));
                            g.setColor(selectionBackColour);
                            selectedWordSplit = rowText.substring(selectPos + 1, selectPos2);
                            g.fillRect(leftPos, row * currFont.getHeight(), currFont.stringWidth(selectedWordSplit), currFont.getHeight());
                            g.setColor(selectionColour);
                            g.drawString(selectedWordSplit, leftPos, row * currFont.getHeight(), Graphics.LEFT | Graphics.TOP);
                            leftPos += currFont.stringWidth(selectedWordSplit);
                            g.setColor(currRGBColor.red, currRGBColor.green, currRGBColor.blue);
                            g.drawString(rowText.substring(selectPos2 + 1), leftPos, row * currFont.getHeight(), Graphics.LEFT | Graphics.TOP);
                        } else {
                            if (contentSelection) {
                                g.setColor(selectionBackColour);
                                selectedWordSplit = rowText.substring(0, selectPos);
                                g.fillRect(leftPos, row * currFont.getHeight(), currFont.stringWidth(selectedWordSplit), currFont.getHeight());
                                g.setColor(selectionColour);
                                g.drawString(selectedWordSplit, leftPos, row * currFont.getHeight(), Graphics.LEFT | Graphics.TOP);
                                leftPos += currFont.stringWidth(selectedWordSplit);
                                g.setColor(currRGBColor.red, currRGBColor.green, currRGBColor.blue);
                                g.drawString(rowText.substring(selectPos + 1), leftPos, row * currFont.getHeight(), Graphics.LEFT | Graphics.TOP);
                                contentSelection = false;
                            } else {
                                g.drawString(rowText.substring(0, selectPos), leftPos, row * currFont.getHeight(), Graphics.LEFT | Graphics.TOP);
                                leftPos += currFont.stringWidth(rowText.substring(0, selectPos));
                                g.setColor(selectionBackColour);
                                selectedWordSplit = rowText.substring(selectPos + 1);
                                g.fillRect(leftPos, row * currFont.getHeight(), currFont.stringWidth(selectedWordSplit), currFont.getHeight());
                                g.setColor(selectionColour);
                                g.drawString(selectedWordSplit, leftPos, row * currFont.getHeight(), Graphics.LEFT | Graphics.TOP);
                                g.setColor(currRGBColor.red, currRGBColor.green, currRGBColor.blue);
                                contentSelection = true;
                            }
                        }
                    }
                }
                firstRow = false;
                firstRowPos = lastRowPos + 1;
            } while (lastRowPos < itemText.length());
        }
    }

    private Font setFont(FontStyle fontStyle) {
        Font newFont;
        if (fontStyle.style == FontStyle.underlined) {
            newFont = Font.getFont(Font.getDefaultFont().getFace(), Font.STYLE_UNDERLINED, fontSize);
        } else if (fontStyle.style == FontStyle.bold) {
            newFont = Font.getFont(Font.getDefaultFont().getFace(), Font.STYLE_BOLD, fontSize);
        } else if (fontStyle.style == FontStyle.italic) {
            newFont = Font.getFont(Font.getDefaultFont().getFace(), Font.STYLE_ITALIC, fontSize);
        } else {
            newFont = Font.getFont(Font.getDefaultFont().getFace(), Font.STYLE_PLAIN, fontSize);
        }
        return newFont;
    }

    public void keyPressed(int keyCode) {
        int keyTranslate = -5;
        int keySelectForward = 51;
        int keySelectBack = 49;
        if (keyCode == keyTranslate) {
            try {
                applicationMainForm.translateToBeTranslatedWordTextField(false);
            } catch (Throwable t) {
                Util.getUtil().log(t);
            }
        } else if (keyCode == keySelectForward) {
            selectNextWord();
        } else if (keyCode == keySelectBack) {
            selectPrevWord();
        }
    }

    public void selectNextWord() {
        if (!isSelectable) {
            applicationMainForm.setSelectedWordNextItem(indexOfTranslationItem);
            return;
        }
        StringColourItemTextPart textPart;
        SelectionMode selectionMode;
        String itemText = "";
        int lastSelPos = -1;
        int firstSelPos = -1;
        boolean isSelected = false;
        if (currSelectItem == -1) {
            currSelectItem = firstSelectableItem;
            itemText = itemTextWrap[currSelectItem];
            textPart = stringColourItemText.getItemTextPart(currSelectItem);
            selectionMode = textPart.getSelectionMode();
            if (selectionMode == PredefinedContent.selectionModeAll) {
                firstSelPos = 0;
                lastSelPos = itemText.length();
                isSelected = true;
            } else if (selectionMode == PredefinedContent.selectionModeSingle) {
                lastSelPos = getSelectBreakPos(itemText, 0, true);
                if (lastSelPos != -1) {
                    isSelected = true;
                    firstSelPos = 0;
                }
            }
        } else {
            textPart = stringColourItemText.getItemTextPart(currSelectItem);
            selectionMode = textPart.getSelectionMode();
            if (selectionMode == PredefinedContent.selectionModeSingle) {
                itemText = itemTextWrap[currSelectItem];
                firstSelPos = itemText.indexOf(selectChar);
                lastSelPos = itemText.indexOf(selectChar, firstSelPos + 1);
                if (lastSelPos < itemText.length() - 1) {
                    itemText = ClearSelectChar(itemText);
                    firstSelPos = lastSelPos - 1;
                    lastSelPos = getSelectBreakPos(itemText, firstSelPos + 1, true);
                    if (lastSelPos != -1) isSelected = true;
                }
            }
            if (!isSelected) {
                for (int n = currSelectItem + 1; n < nItems; n++) {
                    textPart = stringColourItemText.getItemTextPart(n);
                    selectionMode = textPart.getSelectionMode();
                    if (selectionMode == PredefinedContent.selectionModeSingle) {
                        itemText = itemTextWrap[n];
                        lastSelPos = getSelectBreakPos(itemText, 0, true);
                        if (lastSelPos != -1) {
                            itemTextWrap[currSelectItem] = ClearSelectChar(itemTextWrap[currSelectItem]);
                            currSelectItem = n;
                            firstSelPos = 0;
                            isSelected = true;
                            break;
                        }
                    } else if (selectionMode == PredefinedContent.selectionModeAll) {
                        itemText = itemTextWrap[n];
                        itemTextWrap[currSelectItem] = ClearSelectChar(itemTextWrap[currSelectItem]);
                        currSelectItem = n;
                        firstSelPos = 0;
                        lastSelPos = itemText.length();
                        isSelected = true;
                        break;
                    }
                }
            }
        }
        if (isSelected) {
            while (punctuationChars.indexOf(itemText.charAt(firstSelPos)) != -1) firstSelPos++;
            while (punctuationChars.indexOf(itemText.charAt(lastSelPos - 1)) != -1) lastSelPos--;
            selectedWord = itemText.substring(firstSelPos, lastSelPos);
            itemTextWrap[currSelectItem] = itemText.substring(0, firstSelPos) + selectChar + selectedWord + selectChar + itemText.substring(lastSelPos);
            applicationMainForm.setCurrentSelectedItem(indexOfTranslationItem, removeBreakChar(selectedWord), true);
            repaint();
        } else applicationMainForm.setSelectedWordNextItem(indexOfTranslationItem);
    }

    public void selectPrevWord() {
        if (!isSelectable) {
            applicationMainForm.setSelectedWordPrevItem(indexOfTranslationItem);
            return;
        }
        StringColourItemTextPart textPart;
        SelectionMode selectionMode;
        String itemText = "";
        int lastSelPos = -1;
        int firstSelPos = -1;
        boolean isSelected = false;
        if (currSelectItem == -1) {
            currSelectItem = lastSelectableItem;
            itemText = itemTextWrap[currSelectItem];
            textPart = stringColourItemText.getItemTextPart(currSelectItem);
            isSelected = true;
            lastSelPos = itemText.length();
            selectionMode = textPart.getSelectionMode();
            if (selectionMode == PredefinedContent.selectionModeSingle) {
                firstSelPos = getSelectBreakPos(itemText, itemText.length() - 1, false);
                if (firstSelPos != -1) firstSelPos = 0;
            } else if (selectionMode == PredefinedContent.selectionModeAll) firstSelPos = 0;
        } else {
            textPart = stringColourItemText.getItemTextPart(currSelectItem);
            selectionMode = textPart.getSelectionMode();
            if (selectionMode == PredefinedContent.selectionModeSingle) {
                itemText = itemTextWrap[currSelectItem];
                lastSelPos = itemText.indexOf(selectChar);
                if (lastSelPos > 0) {
                    itemText = ClearSelectChar(itemText);
                    firstSelPos = getSelectBreakPos(itemText, lastSelPos - 1, false);
                    if (firstSelPos != -1) isSelected = true;
                }
            }
            if (!isSelected) {
                for (int n = currSelectItem - 1; n > 0; n--) {
                    textPart = stringColourItemText.getItemTextPart(n);
                    selectionMode = textPart.getSelectionMode();
                    if (selectionMode == PredefinedContent.selectionModeSingle) {
                        itemText = itemTextWrap[n];
                        lastSelPos = itemText.length();
                        firstSelPos = getSelectBreakPos(itemText, lastSelPos - 1, false);
                        if (firstSelPos != -1) {
                            itemTextWrap[currSelectItem] = ClearSelectChar(itemTextWrap[currSelectItem]);
                            currSelectItem = n;
                            isSelected = true;
                            break;
                        }
                    } else if (selectionMode == PredefinedContent.selectionModeAll) {
                        itemText = itemTextWrap[n];
                        itemTextWrap[currSelectItem] = ClearSelectChar(itemTextWrap[currSelectItem]);
                        currSelectItem = n;
                        lastSelPos = itemText.length();
                        firstSelPos = 0;
                        isSelected = true;
                        break;
                    }
                }
            }
        }
        if (isSelected) {
            while (punctuationChars.indexOf(itemText.charAt(firstSelPos)) != -1) firstSelPos++;
            while (punctuationChars.indexOf(itemText.charAt(lastSelPos - 1)) != -1) lastSelPos--;
            selectedWord = itemText.substring(firstSelPos, lastSelPos);
            itemTextWrap[currSelectItem] = itemText.substring(0, firstSelPos) + selectChar + selectedWord + selectChar + itemText.substring(lastSelPos);
            applicationMainForm.setCurrentSelectedItem(indexOfTranslationItem, removeBreakChar(selectedWord), true);
            repaint();
        } else applicationMainForm.setSelectedWordPrevItem(indexOfTranslationItem);
    }

    private int getSelectBreakPos(String s, int beginPos, boolean forward) {
        String v = null;
        int returnPos = -1;
        if (forward) {
            for (int pos = beginPos; pos < s.length(); pos++) {
                char ch = s.charAt(pos);
                if (punctuationChars.indexOf(ch) != -1) {
                    if (v != null) {
                        returnPos = pos;
                        break;
                    }
                } else v += ch;
            }
            if (returnPos == -1 && v != null) returnPos = s.length();
        } else {
            for (int pos = beginPos; pos > 0; pos--) {
                char ch = s.charAt(pos);
                if (punctuationChars.indexOf(ch) != -1) {
                    if (v != null) {
                        returnPos = pos;
                        break;
                    }
                } else v += ch;
            }
            if (returnPos == -1 && v != null) returnPos = 0;
        }
        return returnPos;
    }

    private String ClearSelectChar(String s) {
        String returnStr = "";
        for (int pos = 0; pos < s.length(); pos++) {
            char ch = s.charAt(pos);
            if (ch != selectChar) returnStr += ch;
        }
        return returnStr;
    }

    public void pointerPressed(int x, int y) {
        if (currSelectItem != -1) itemTextWrap[currSelectItem] = ClearSelectChar(itemTextWrap[currSelectItem]);
        if (applicationMainForm.indexOfSelectedItem != -1 && applicationMainForm.indexOfSelectedItem != indexOfTranslationItem) applicationMainForm.setCurrentSelectedItem(-1, null, false);
        currSelectItem = -1;
        selectedWord = null;
        int pressedRow = y / defaultFont.getHeight();
        int rowIndex = -1;
        int itemIndex = 0;
        int nextBreakPos = -1;
        int lastBreakPos = 0;
        while (rowIndex < pressedRow) {
            lastBreakPos = nextBreakPos + 1;
            nextBreakPos = itemTextWrap[itemIndex].indexOf(newLineChar, lastBreakPos);
            if (nextBreakPos > -1) rowIndex++; else {
                if (rowIndex == pressedRow - 1) {
                    if (itemIndex < nItems - 1) {
                        if (itemLeft[itemIndex + 1] > x || itemLeft[itemIndex + 1] == 0) {
                            nextBreakPos = itemTextWrap[itemIndex].length();
                            rowIndex++;
                        } else {
                            itemIndex++;
                            nextBreakPos = -1;
                        }
                    } else {
                        nextBreakPos = itemTextWrap[itemIndex].length();
                        rowIndex++;
                    }
                } else {
                    itemIndex++;
                    if (itemLeft[itemIndex] == 0) rowIndex++;
                }
            }
        }
        StringColourItemTextPart textPart = stringColourItemText.getItemTextPart(itemIndex);
        SelectionMode selectMode = textPart.getSelectionMode();
        boolean isSelected = false;
        if (selectMode == PredefinedContent.selectionModeAll) {
            selectedWord = textPart.getText();
            itemTextWrap[itemIndex] = selectChar + itemTextWrap[itemIndex] + selectChar;
            currSelectItem = itemIndex;
            isSelected = true;
        } else if (selectMode == PredefinedContent.selectionModeSingle) {
            String selectedRowText = itemTextWrap[itemIndex].substring(lastBreakPos, nextBreakPos);
            Font itemFont = setFont(textPart.getStyle());
            int lastPos = 0;
            int nextPos = -1;
            boolean isOverXPoint = false;
            for (int pos = 0; pos < selectedRowText.length(); pos++) {
                char ch = selectedRowText.charAt(pos);
                if (punctuationChars.indexOf(ch) != -1) {
                    if (isOverXPoint) {
                        nextPos = pos;
                        break;
                    } else {
                        lastPos = pos + 1;
                        nextPos = -1;
                    }
                } else if (itemFont.stringWidth(selectedRowText.substring(0, pos + 1)) > x) isOverXPoint = true;
            }
            if (isOverXPoint) {
                if (nextPos == -1) nextPos = selectedRowText.length();
                if (lastPos < nextPos) {
                    nextBreakPos = lastBreakPos + nextPos;
                    lastBreakPos += lastPos;
                    selectedWord = itemTextWrap[itemIndex].substring(lastBreakPos, nextBreakPos);
                    itemTextWrap[itemIndex] = itemTextWrap[itemIndex].substring(0, lastBreakPos) + selectChar + selectedWord + selectChar + itemTextWrap[itemIndex].substring(nextBreakPos);
                    isSelected = true;
                    currSelectItem = itemIndex;
                }
            }
        }
        if (isSelected) {
            try {
                applicationMainForm.translateSelectedWord(removeBreakChar(selectedWord));
            } catch (Throwable t) {
                Util.getUtil().log(t);
            }
        }
    }

    private String removeBreakChar(String text) {
        int breakPos = text.indexOf(newLineChar);
        while (breakPos >= 0) {
            text = text.substring(0, breakPos) + text.substring(breakPos + 1);
            breakPos = text.indexOf(newLineChar);
        }
        return text;
    }
}
