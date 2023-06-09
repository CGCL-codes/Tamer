package javax.microedition.lcdui;

import java.util.ArrayList;

/**
 * 
 * @author Andre Nijholt
 */
public class ChoiceGroup extends Item implements Choice {

    protected int choiceType;

    protected ArrayList<ChoiceItem> choiceItems;

    /** Scrolling administration */
    private int scrollFirst = 0;

    private int scrollCurr = 0;

    private int scrollLast = 0;

    private boolean scrollWrap = true;

    public ChoiceGroup(String label, int choiceType) {
        if (choiceType == Choice.IMPLICIT) {
            throw new IllegalArgumentException();
        }
        this.label = label;
        this.choiceType = choiceType;
        choiceItems = new ArrayList<ChoiceItem>();
        interactive = true;
        if (label != null) {
            minWidth = (label.length() * Display.CHAR_WIDTH);
            minHeight = ((choiceType == Choice.POPUP) ? 1 : 2) * Display.CHAR_HEIGHT;
        } else {
            minWidth = Display.CHAR_WIDTH;
            minHeight = Display.CHAR_HEIGHT;
        }
    }

    public ChoiceGroup(String label, int choiceType, String[] stringElements, Image[] imageElements) {
        if (choiceType == Choice.IMPLICIT) {
            throw new IllegalArgumentException();
        }
        this.label = label;
        this.choiceType = choiceType;
        interactive = true;
        if (label != null) {
            minWidth = (label.length() * Display.CHAR_WIDTH);
            minHeight = 2 * Display.CHAR_HEIGHT;
        } else {
            minWidth = Display.CHAR_WIDTH;
            minHeight = Display.CHAR_HEIGHT;
        }
        choiceItems = new ArrayList<ChoiceItem>(stringElements.length);
        for (int i = 0; i < stringElements.length; i++) {
            choiceItems.add(new ChoiceItem(((stringElements != null) && (stringElements.length < i)) ? stringElements[i] : null, ((imageElements != null) && (imageElements.length < i)) ? imageElements[i] : null));
            int itemWidth = (((stringElements != null) && (stringElements.length < i)) ? stringElements[i].length() * Display.CHAR_WIDTH : 0) + (((imageElements != null) && (imageElements.length < i)) ? imageElements[i].getWidth() : 0);
            if (itemWidth > minWidth) {
                minWidth = itemWidth;
            }
            scrollLast++;
        }
    }

    public int append(String stringPart, Image imagePart) {
        choiceItems.add(new ChoiceItem(stringPart, imagePart));
        int itemWidth = ((stringPart != null) ? stringPart.length() * Display.CHAR_WIDTH : 0) + ((imagePart != null) ? imagePart.getWidth() : 0);
        if (itemWidth > minWidth) {
            minWidth = itemWidth;
        }
        scrollLast++;
        return choiceItems.size();
    }

    public void delete(int elementNum) {
        scrollLast--;
        choiceItems.remove(elementNum);
    }

    public void deleteAll() {
        scrollLast = 0;
        choiceItems.clear();
    }

    public Image getImage(int elementNum) {
        return choiceItems.get(elementNum).img;
    }

    public int getSelectedFlags(boolean[] selectedArray_return) {
        selectedArray_return = new boolean[choiceItems.size()];
        for (int i = 0; i < selectedArray_return.length; i++) {
            selectedArray_return[i] = choiceItems.get(i).selected;
        }
        return selectedArray_return.length;
    }

    public int getSelectedIndex() {
        for (int i = 0; i < choiceItems.size(); i++) {
            if (choiceItems.get(i).selected) {
                return i;
            }
        }
        return -1;
    }

    public String getString(int elementNum) {
        return choiceItems.get(elementNum).str;
    }

    public void insert(int elementNum, String stringPart, Image imagePart) {
        choiceItems.add(elementNum, new ChoiceItem(stringPart, imagePart));
    }

    public boolean isSelected(int elementNum) {
        return choiceItems.get(elementNum).selected;
    }

    public void set(int elementNum, String stringPart, Image imagePart) {
        choiceItems.set(elementNum, new ChoiceItem(stringPart, imagePart));
    }

    public void setScrollWrap(boolean scrollWrap) {
        this.scrollWrap = scrollWrap;
    }

    public void setSelectedFlags(boolean[] selectedArray) {
        for (int i = 0; i < choiceItems.size(); i++) {
            choiceItems.get(i).selected = selectedArray[i];
        }
    }

    public void setSelectedIndex(int elementNum, boolean selected) {
        if ((choiceType == Choice.MULTIPLE) || !selected) {
            choiceItems.get(elementNum).selected = selected;
        } else {
            for (int i = 0; i < choiceItems.size(); i++) {
                ChoiceItem li = choiceItems.get(i);
                li.selected = (i == elementNum);
            }
        }
    }

    public int size() {
        return choiceItems.size();
    }

    protected void keyPressed(int keyCode) {
        if (keyCode == Screen.KEY_RIGHT) {
            if (scrollWrap) {
                scrollCurr = (scrollCurr + 1) % choiceItems.size();
            } else if (scrollCurr < (choiceItems.size() - 1)) {
                scrollCurr++;
            }
            repaint();
        } else if (keyCode == Screen.KEY_LEFT) {
            if (scrollWrap) {
                scrollCurr = (scrollCurr == 0) ? (choiceItems.size() - 1) : (scrollCurr - 1);
            } else if (scrollCurr > 0) {
                scrollCurr--;
            }
            repaint();
        } else if (keyCode == Screen.KEY_BACK) {
        } else if (keyCode == Screen.KEY_ENTER) {
            ChoiceItem li = choiceItems.get(scrollCurr);
            setSelectedIndex(scrollCurr, !li.selected);
            notifyStateChanged();
        }
    }

    protected void paint(Graphics g, int x, int y, int w, int h, boolean selected) {
        if (label != null) {
            g.drawString(label, x, y, (selected && (choiceType == Choice.POPUP)));
            y += Display.CHAR_HEIGHT;
            h -= Display.CHAR_HEIGHT;
        }
        if (h <= 0) {
            return;
        }
        int scrollLines = h / Display.CHAR_HEIGHT;
        if (scrollCurr == 0) {
            scrollFirst = 0;
            scrollLast = scrollLines;
        } else if ((choiceItems.size() >= scrollLines) && (scrollCurr >= (choiceItems.size() - 1))) {
            scrollFirst = choiceItems.size() - scrollLines;
            scrollLast = choiceItems.size() - 1;
        } else if (scrollCurr >= scrollLast) {
            scrollFirst++;
            scrollLast++;
        } else if (scrollCurr < scrollFirst) {
            scrollFirst--;
            scrollLast--;
        }
        int xOffset = (choiceType == Choice.POPUP) ? x : (x + 2 * Display.CHAR_WIDTH);
        for (int i = scrollFirst; (i < choiceItems.size()) && (i <= scrollLast); i++) {
            ChoiceItem li = choiceItems.get(i);
            g.drawString(li.str, xOffset, y, (selected && (i == scrollCurr)));
            if ((choiceType == Choice.EXCLUSIVE) || (choiceType == Choice.MULTIPLE)) {
                if (li.selected) {
                    g.fillArc(x, y, 8, 8, 0, 360);
                } else {
                    g.drawArc(x, y, 8, 8, 0, 360);
                }
            }
            y += Display.CHAR_HEIGHT;
            h -= Display.CHAR_HEIGHT;
            if (h <= 0) {
                break;
            }
        }
    }

    private class ChoiceItem {

        String str;

        Image img;

        boolean selected;

        ChoiceItem(String stringPart, Image imagePart) {
            this.str = stringPart;
            this.img = imagePart;
            this.selected = false;
        }
    }
}
