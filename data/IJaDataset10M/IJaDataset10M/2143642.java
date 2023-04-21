package com.g2d.display.ui.text;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextHitInfo;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.CharacterIterator;
import java.text.AttributedCharacterIterator.Attribute;
import java.util.Map;
import java.util.Vector;
import com.cell.CMath;
import com.g2d.Tools;

public class MultiTextLayout {

    public static final char CHAR_CUT = 24;

    public static final char CHAR_PASTE = 22;

    public static final char CHAR_COPY = 3;

    public static final char CHAR_BACKSPACE = 8;

    public static final char CHAR_DELETE = 127;

    private int width;

    private int height;

    private int line_space = 2;

    private String text = "";

    private AttributedString attr_text;

    private Vector<TextLine> textlines = new Vector<TextLine>();

    /**是否为单行*/
    public final boolean is_single_line;

    /**是否只读*/
    public boolean is_read_only;

    /**是否显示光标*/
    public boolean is_show_caret = true;

    /**是否显示选择的区域*/
    public boolean is_show_select = true;

    private transient int render_timer;

    private transient float render_shadow_alpha = 0;

    private transient int render_shadow_color = 0;

    private transient boolean render_antialiasing = false;

    Rectangle caret_bounds = new Rectangle();

    int caret_line;

    int caret_start_line;

    int caret_end_line;

    int caret_position;

    int caret_start_position;

    int caret_end_position;

    TextHitInfo caret_start_hit;

    TextHitInfo caret_end_hit;

    Color selected_color = new Color(0x40ffffff, true);

    private int max_line_count = 1000;

    private TextChanges textChange;

    int set_caret_position = -1;

    public MultiTextLayout(boolean single) {
        is_single_line = single;
    }

    public MultiTextLayout() {
        this(false);
    }

    /**
	 * 将光标设置到x，y位置
	 * @param x
	 * @param y
	 */
    public synchronized void setCaret(int x, int y) {
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (!is_read_only) {
            caret_bounds = new Rectangle(x, y, 2, 10);
            caret_end_hit = null;
            caret_start_hit = null;
            caret_start_position = 0;
            caret_position = 0;
            if (!is_single_line && textlines.size() > 1) {
                for (TextLine line : textlines) {
                    if (caret_bounds.intersects(line.x, line.y, width, line.height)) {
                        testCaretStart(line, x, y);
                        break;
                    } else {
                        caret_start_position += line.getLayout().getCharacterCount();
                    }
                }
                if (caret_start_hit == null) {
                    testCaretStart(textlines.elementAt(textlines.size() - 1), x, y);
                }
            } else if (textlines.size() > 0) {
                testCaretStart(textlines.elementAt(0), x, y);
            }
        }
    }

    /**
	 * 尝试拖动光标到x，y位置
	 * @param x
	 * @param y
	 */
    public synchronized void dragCaret(int x, int y) {
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        if (!is_read_only) {
            caret_bounds = new Rectangle(x, y, 2, 10);
            caret_end_hit = null;
            caret_end_position = 0;
            caret_position = 0;
            if (!is_single_line && textlines.size() > 1) {
                for (TextLine line : textlines) {
                    if (caret_bounds.intersects(line.x, line.y, width, line.height)) {
                        testCaretEnd(line, x, y);
                        break;
                    } else {
                        caret_end_position += line.getLayout().getCharacterCount();
                    }
                }
                if (caret_end_hit == null) {
                    testCaretEnd(textlines.elementAt(textlines.size() - 1), x, y);
                }
            } else if (textlines.size() > 0) {
                testCaretEnd(textlines.elementAt(0), x, y);
            }
            testCaretSelected();
        }
    }

    /**
	 * 点击
	 * @param line
	 * @param x
	 * @param y
	 */
    private void testCaretStart(TextLine line, int x, int y) {
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        caret_start_hit = line.getLayout().hitTestChar(x - line.x + line.offsetx, y - line.y + line.offsety);
        caret_bounds = line.getLayout().getCaretShape(caret_start_hit).getBounds();
        caret_bounds.x = caret_bounds.x - 1;
        caret_bounds.y = line.y;
        caret_bounds.height = line.height;
        caret_bounds.width = 2;
        caret_start_position += caret_start_hit.getInsertionIndex();
        caret_start_line = line.line_index;
        caret_position = caret_start_position;
        caret_line = caret_start_line;
        if (caret_position > 0) {
            if (caret_start_hit.getInsertionIndex() > 0) {
                int pos = caret_position - 1;
                if (pos < text.length() && text.charAt(pos) == '\n') {
                    caret_position -= 1;
                }
            }
        }
    }

    /**
	 * 松开
	 * @param line
	 * @param x
	 * @param y
	 */
    private void testCaretEnd(TextLine line, int x, int y) {
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        caret_end_hit = line.getLayout().hitTestChar(x - line.x + line.offsetx, y - line.y + line.offsety);
        caret_bounds = line.getLayout().getCaretShape(caret_end_hit).getBounds();
        caret_bounds.x = caret_bounds.x - 1;
        caret_bounds.y = line.y;
        caret_bounds.height = line.height;
        caret_bounds.width = 2;
        caret_end_position += caret_end_hit.getInsertionIndex();
        caret_end_line = line.line_index;
        caret_position = caret_end_position;
        caret_line = caret_end_line;
        if (caret_position > 0) {
            if (caret_end_hit.getInsertionIndex() > 0) {
                int pos = caret_position - 1;
                if (pos < text.length() && text.charAt(pos) == '\n') {
                    caret_position -= 1;
                }
            }
        }
    }

    private void testCaretSelected() {
        if (caret_start_hit != null && caret_end_hit != null) {
            try {
                int max = Math.max(caret_start_line, caret_end_line);
                int min = Math.min(caret_start_line, caret_end_line);
                for (int i = textlines.size() - 1; i >= 0; --i) {
                    TextLine line = textlines.get(i);
                    Rectangle rect = null;
                    if (i == min && i == max) {
                        rect = line.getLayout().getVisualHighlightShape(caret_start_hit, caret_end_hit).getBounds();
                    } else if (i == max && i == caret_end_line) {
                        rect = line.getLayout().getCaretShape(caret_end_hit).getBounds();
                        rect.width = rect.x;
                        rect.x = 0;
                    } else if (i == max && i == caret_start_line) {
                        rect = line.getLayout().getCaretShape(caret_start_hit).getBounds();
                        rect.width = rect.x;
                        rect.x = 0;
                    } else if (i == min && i == caret_end_line) {
                        rect = line.getLayout().getCaretShape(caret_end_hit).getBounds();
                        rect.width = width - rect.x;
                    } else if (i == min && i == caret_start_line) {
                        rect = line.getLayout().getCaretShape(caret_start_hit).getBounds();
                        rect.width = width - rect.x;
                    } else if (CMath.isInclude(i, min, max)) {
                        rect = new Rectangle(0, 0, width, line.height);
                    }
                    if (rect != null) {
                        line.selected = rect.getBounds();
                        line.selected.y = 0;
                        line.selected.height = line.height;
                    } else {
                        line.selected = null;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized int getCaretStartPosition() {
        return caret_start_position;
    }

    public synchronized int getCaretEndPosition() {
        return caret_end_position;
    }

    /**
	 * 
	 * @return 获得当前光标位置？
	 * @author yagamiya
	 */
    public synchronized int getCaretPosition() {
        return caret_position;
    }

    /**
	 * 移动光标位置
	 * @param d
	 */
    public synchronized void moveCaretPosition(int d) {
        setCaretPosition(getCaretPosition() + d);
    }

    public synchronized void moveCaretPositionEnd() {
        setCaretPosition(getText().length());
    }

    public synchronized void setCaretPosition(int position) {
        set_caret_position = position;
        set_caret_position = Math.max(set_caret_position, 0);
        set_caret_position = Math.min(set_caret_position, getText().length());
    }

    public synchronized String getSelectedText() {
        if (caret_start_hit != null && caret_end_hit != null) {
            int max = Math.max(caret_start_position, caret_end_position);
            int min = Math.min(caret_start_position, caret_end_position);
            try {
                max = Math.min(max, text.length());
                min = Math.max(min, 0);
                return text.substring(min, max);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    public synchronized int getCaretPosX() {
        if (text.length() > 0) {
            return caret_bounds.x;
        } else {
            return 0;
        }
    }

    public synchronized int getCaretPosY() {
        if (text.length() > 0) {
            return caret_bounds.y;
        } else {
            return 0;
        }
    }

    /**
	 * 坐标位置转换到文字位置
	 * @param x
	 * @param y
	 * @return
	 */
    public synchronized int pointToPosition(int x, int y) {
        if (x < 0) x = 0;
        if (y < 0) y = 0;
        int position = 0;
        for (TextLine line : textlines) {
            if (CMath.intersectRect2(line.x, line.y, width, line.height, x, y, 2, 10)) {
                x -= line.x;
                y -= line.y;
                TextHitInfo hit = line.getLayout().hitTestChar(x, y);
                position += hit.getCharIndex();
                break;
            } else {
                position += line.getLayout().getCharacterCount();
            }
        }
        position = Math.min(position, text.length());
        position = Math.max(position, 0);
        return position;
    }

    /**
	 * 得到该位置的文字属性，比如 attribute=TextAttribute.FOREGROUND, value=Color.BLUE，则返回该位置是否有颜色是蓝色的属性
	 * @param position 文本位置
	 * @param attribute 属性
	 * @param value 属性值
	 * @return
	 */
    public synchronized AttributedSegment getSegment(int position, Attribute attribute, Object value) {
        if (attr_text != null) {
            AttributedCharacterIterator it = attr_text.getIterator();
            return getSegment(it, position, attribute, value);
        }
        return null;
    }

    /**
	 * 得到该位置的文字属性
	 * @param position
	 * @param attribute
	 * @return
	 */
    public synchronized AttributedSegment getSegment(int position, Attribute attribute) {
        if (attr_text != null) {
            AttributedCharacterIterator it = attr_text.getIterator();
            it.setIndex(position);
            return getSegment(it, position, attribute, it.getAttribute(attribute));
        }
        return null;
    }

    private AttributedSegment getSegment(AttributedCharacterIterator it, int position, Attribute attribute, Object value) {
        try {
            it.setIndex(position);
            Object cur_value = it.getAttribute(attribute);
            if (value != null && value.equals(cur_value)) {
                String text_segment = "";
                for (char c = it.current(); c != CharacterIterator.DONE; c = it.next()) {
                    if (value.equals(it.getAttribute(attribute))) {
                        text_segment = text_segment + c;
                    }
                }
                it.setIndex(position);
                for (char c = it.previous(); c != CharacterIterator.DONE; c = it.previous()) {
                    if (value.equals(it.getAttribute(attribute))) {
                        text_segment = c + text_segment;
                    }
                }
                return new AttributedSegment(attribute, value, text_segment);
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
	 * @param c
	 * @return 增加多少文字，负数代表删除了多少文字
	 */
    public synchronized InsertInfo insertChar(char c) {
        if (is_read_only) {
            return null;
        }
        InsertInfo info = new InsertInfo(c);
        if (c == CHAR_CUT) {
            Tools.setClipboardText(getSelectedText());
            return doInstertChar(info, CHAR_DELETE);
        } else if (c == CHAR_COPY) {
            Tools.setClipboardText(getSelectedText());
        } else if (c == CHAR_PASTE) {
            String str = Tools.getClipboardText();
            if (is_single_line) {
                str = str.replaceAll("\n", "");
            }
            return doInstertText(info, str);
        } else if (c == CHAR_BACKSPACE) {
            return doInstertChar(info, CHAR_BACKSPACE);
        } else if (c == CHAR_DELETE) {
            return doInstertChar(info, CHAR_DELETE);
        } else if (c == '\t') {
            if (!is_single_line) {
                return doInstertText(info, "    ");
            }
        } else if (c == '\n') {
            if (!is_single_line) {
                return doInstertText(info, "\n");
            }
        } else if (c >= 32) {
            return doInstertText(info, c + "");
        }
        return null;
    }

    private InsertInfo doInstertChar(InsertInfo info, char inserted_char) {
        try {
            if (caret_start_hit != null && caret_end_hit != null && caret_start_position != caret_end_position) {
                int max = Math.max(caret_start_position, caret_end_position);
                int min = Math.min(caret_start_position, caret_end_position);
                switch(inserted_char) {
                    case CHAR_BACKSPACE:
                    case CHAR_DELETE:
                        deleteText(min, max);
                        caret_position = min;
                        info.length_change = min - max;
                        return info;
                }
            } else {
                switch(inserted_char) {
                    case CHAR_BACKSPACE:
                        if (caret_position > 0 && text.length() > 0) {
                            if (caret_position < text.length()) {
                                deleteText(caret_position - 1, caret_position);
                            } else {
                                deleteText(text.length() - 1, text.length());
                            }
                            caret_position -= 1;
                            info.length_change = -1;
                            return info;
                        }
                        break;
                    case CHAR_DELETE:
                        if (caret_position >= 0 && text.length() > 0) {
                            if (caret_position < text.length()) {
                                deleteText(caret_position, caret_position + 1);
                                info.length_change = -1;
                                return info;
                            }
                        }
                        break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            caret_end_hit = null;
        }
        return null;
    }

    private InsertInfo doInstertText(InsertInfo info, String inserted_text) {
        try {
            String text = getText();
            if (caret_start_hit != null && caret_end_hit != null && caret_start_position != caret_end_position) {
                int max = Math.max(caret_start_position, caret_end_position);
                int min = Math.min(caret_start_position, caret_end_position);
                if (max < text.length()) {
                    insertText(min, max, inserted_text);
                } else {
                    insertText(min, text.length(), inserted_text);
                }
                caret_position = min + inserted_text.length();
                info.inserted_text = inserted_text;
                info.length_change = inserted_text.length();
                return info;
            } else {
                if (caret_position < text.length()) {
                    insertText(caret_position, inserted_text);
                } else {
                    insertText(text.length(), inserted_text);
                }
                caret_position += inserted_text.length();
                info.inserted_text = inserted_text;
                info.length_change = inserted_text.length();
                return info;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            caret_end_hit = null;
        }
        return null;
    }

    /**
	 * 设置文本
	 * @param text
	 */
    public synchronized void setText(String text) {
        if (is_single_line) {
            text = text.replaceAll("\n", "");
        }
        if (textChange != null) {
            textChange.set(text);
        } else {
            textChange = new TextChanges(text);
        }
    }

    /**
	 * 设置高级文本
	 * @param atext
	 */
    public synchronized void setText(AttributedString atext) {
        if (textChange != null) {
            textChange.set(atext);
        } else {
            textChange = new TextChanges(atext);
        }
    }

    /**
	 * 在最后附加文字
	 * @param atext
	 */
    public synchronized void appendText(String text) {
        if (is_single_line) {
            text = text.replaceAll("\n", "");
        }
        if (textChange != null) {
            textChange.append(text);
        } else {
            textChange = new TextChanges(this.text + text);
        }
    }

    /**
	 * 在最后附加高级文字
	 * @param atext
	 */
    public synchronized void appendText(AttributedString atext) {
        if (textChange != null) {
            textChange.append(atext);
        } else {
            if (this.attr_text != null) {
                textChange = new TextChanges(Tools.linkAttributedString(this.attr_text, atext));
            } else {
                textChange = new TextChanges(atext);
            }
        }
    }

    /**
	 * 删除一段文字
	 * @param start
	 * @param end
	 */
    public synchronized void deleteText(int start, int end) {
        if (start == end) {
            return;
        }
        int min = Math.min(start, end);
        int max = Math.max(start, end);
        String get_text = getText();
        AttributedString get_atext = getAttributeText();
        if (get_atext != null) {
            if (textChange == null) {
                textChange = new TextChanges();
            }
            if (min <= 0 && max >= get_text.length()) {
                textChange.set("");
            } else if (min <= 0) {
                String text_right = get_text.substring(max, get_text.length());
                AttributedString atext_right = TextBuilder.subString(get_atext, max, get_text.length());
                textChange.set(text_right, atext_right);
            } else if (max >= get_text.length()) {
                String text_left = get_text.substring(0, min);
                AttributedString atext_left = TextBuilder.subString(get_atext, 0, min);
                textChange.set(text_left, atext_left);
            } else {
                String text_left = get_text.substring(0, min);
                String text_right = get_text.substring(max, get_text.length());
                AttributedString atext_left = TextBuilder.subString(get_atext, 0, min);
                AttributedString atext_right = TextBuilder.subString(get_atext, max, get_text.length());
                textChange.set(text_left.concat(text_right), TextBuilder.concat(atext_left, atext_right));
            }
        }
    }

    /**
	 * 在开始处插入文字
	 * @param start
	 * @param atext
	 */
    public synchronized void insertText(int start, AttributedString atext) {
        if (start < 0) {
            return;
        }
        String get_text = getText();
        AttributedString get_atext = getAttributeText();
        if (textChange == null) {
            textChange = new TextChanges();
        }
        if (get_text.length() == 0) {
            textChange.set(atext);
        } else if (start >= get_text.length()) {
            textChange.append(atext);
        } else if (get_atext != null) {
            String text_left = get_text.substring(0, start);
            String text_right = get_text.substring(start, get_text.length());
            AttributedString atext_left = TextBuilder.subString(get_atext, 0, start);
            AttributedString atext_right = TextBuilder.subString(get_atext, start, get_text.length());
            String add_text = TextBuilder.toString(atext);
            textChange.set(text_left.concat(add_text).concat(text_right), TextBuilder.concat(TextBuilder.concat(atext_left, atext), atext_right));
        }
    }

    /**
	 * 将此范围的文字替换成atext
	 * @param start
	 * @param end
	 * @param atext
	 */
    public synchronized void insertText(int start, int end, AttributedString atext) {
        if (start == end) {
            return;
        }
        int min = Math.min(start, end);
        int max = Math.max(start, end);
        String add_text = TextBuilder.toString(atext);
        String get_text = getText();
        AttributedString get_atext = getAttributeText();
        if (get_atext != null) {
            if (textChange == null) {
                textChange = new TextChanges();
            }
            if (min <= 0 && max >= get_text.length()) {
                textChange.set(atext);
            } else if (min <= 0) {
                String text_right = get_text.substring(max, get_text.length());
                AttributedString atext_right = TextBuilder.subString(get_atext, max, get_text.length());
                textChange.set(add_text.concat(text_right), TextBuilder.concat(atext, atext_right));
            } else if (max >= get_text.length()) {
                String text_left = get_text.substring(0, min);
                AttributedString atext_left = TextBuilder.subString(get_atext, 0, min);
                textChange.set(text_left.concat(add_text), TextBuilder.concat(atext_left, atext));
            } else {
                String text_left = get_text.substring(0, min);
                String text_right = get_text.substring(max, get_text.length());
                AttributedString atext_left = TextBuilder.subString(get_atext, 0, min);
                AttributedString atext_right = TextBuilder.subString(get_atext, max, get_text.length());
                textChange.set(text_left.concat(add_text).concat(text_right), TextBuilder.concat(TextBuilder.concat(atext_left, atext), atext_right));
            }
        }
    }

    /**
	 * 在开始处插入文字
	 * @param start
	 * @param atext
	 */
    public synchronized void insertText(int start, String text) {
        this.insertText(start, new AttributedString(text));
    }

    /**
	 * 将此范围的文字替换成atext
	 * @param start
	 * @param end
	 * @param atext
	 */
    public synchronized void insertText(int start, int end, String text) {
        this.insertText(start, end, new AttributedString(text));
    }

    public synchronized void putAttribute(Attribute attribute, Object value, int start, int end) {
        if (textChange == null) {
            textChange = new TextChanges();
        }
        if (textChange.atext != null) {
            textChange.force = true;
            textChange.atext.addAttribute(attribute, value, start, end);
        }
    }

    public synchronized void setWidth(int width) {
        if (this.width != width) {
            if (textChange != null) {
                textChange.setWidth(width);
            } else {
                textChange = new TextChanges();
                textChange.setWidth(width);
            }
        }
    }

    public synchronized void setSpace(int space) {
        if (this.line_space != space) {
            if (textChange != null) {
                textChange.setSpace(space);
            } else {
                textChange = new TextChanges();
                textChange.setSpace(space);
            }
        }
    }

    private synchronized AttributedString getAttributeText() {
        if (textChange != null) {
            return textChange.atext;
        }
        return attr_text;
    }

    public synchronized String getText() {
        if (textChange != null) {
            return textChange.text;
        }
        return text == null ? "" : text;
    }

    public synchronized int getWidth() {
        if (textChange != null) {
            return textChange.width;
        }
        return width;
    }

    public synchronized int getSpace() {
        if (textChange != null) {
            return textChange.space;
        }
        return line_space;
    }

    public synchronized int getHeight() {
        return height;
    }

    /**
	 * 获取分段后总的行数，在头一次渲染后有效
	 * @return
	 */
    public synchronized int getLineCount() {
        return textlines.size();
    }

    Dimension render_size = new Dimension();

    public synchronized Dimension drawText(Graphics2D g, int x, int y) {
        return this.drawText(g, x, y, 0, 0, width, height);
    }

    /**
	 * @param g
	 * @param x 绘制到g的位置
	 * @param y 绘制到g的位置
	 * @param sx 绘制文本的范围，是该Layout的内部坐标
	 * @param sy 绘制文本的范围，是该Layout的内部坐标
	 * @param sw 绘制文本的范围，是该Layout的内部坐标
	 * @param sh 绘制文本的范围，是该Layout的内部坐标
	 * @return
	 */
    public synchronized Dimension drawText(Graphics2D g, int x, int y, int sx, int sy, int sw, int sh) {
        return this.drawText(g, x, y, sx, sy, sw, sh, 0, 0, 0, 0);
    }

    /**
	 * @param g
	 * @param x 绘制到g的位置
	 * @param y 绘制到g的位置
	 * @param sx 绘制文本的范围，是该Layout的内部坐标
	 * @param sy 绘制文本的范围，是该Layout的内部坐标
	 * @param sw 绘制文本的范围，是该Layout的内部坐标
	 * @param sh 绘制文本的范围，是该Layout的内部坐标
	 * @param shadow_x 阴影偏移
	 * @param shadow_y 阴影偏移	 
	 * @param shadow_alpha 阴影透明度
	 * @param shadow_color 阴影颜色
	 * @return
	 */
    public synchronized Dimension drawText(Graphics2D g, int x, int y, int sx, int sy, int sw, int sh, int shadow_x, int shadow_y, float shadow_alpha, int shadow_color) {
        return drawText(g, Integer.MAX_VALUE, x, y, sx, sy, sw, sh, shadow_x, shadow_y, shadow_alpha, shadow_color);
    }

    /**
	 * @param g
	 * @param max_line 最多显示几行
	 * @param x 绘制到g的位置
	 * @param y 绘制到g的位置
	 * @param sx 绘制文本的范围，是该Layout的内部坐标
	 * @param sy 绘制文本的范围，是该Layout的内部坐标
	 * @param sw 绘制文本的范围，是该Layout的内部坐标
	 * @param sh 绘制文本的范围，是该Layout的内部坐标
	 * @param shadow_x 阴影偏移
	 * @param shadow_y 阴影偏移	 
	 * @param shadow_alpha 阴影透明度
	 * @param shadow_color 阴影颜色
	 * @return
	 */
    public synchronized Dimension drawText(Graphics2D g, int max_line, int x, int y, int sx, int sy, int sw, int sh, int shadow_x, int shadow_y, float shadow_alpha, int shadow_color) {
        x += 1;
        y += 1;
        sx -= 1;
        sy -= 1;
        sw += 2;
        sh += 2;
        tryChangeTextAndCaret(g);
        render_size.setSize(0, height);
        {
            g.translate(x, y);
            Shape prew_shape = g.getClip();
            try {
                Rectangle rect = new Rectangle(sx, sy, sw, sh);
                g.clip(rect);
                int rended_line = 0;
                for (TextLine line : textlines) {
                    if (rended_line < max_line) {
                        if (rect.intersects(line.x, line.y, line.width, line.height)) {
                            line.render(g, shadow_x, shadow_y, shadow_alpha, shadow_color);
                        }
                    }
                    render_size.width = Math.max(line.width, render_size.width);
                    rended_line++;
                }
                if (!is_read_only && is_show_caret) {
                    if (render_timer / 6 % 2 == 0 && caret_bounds != null) {
                        g.setColor(Color.WHITE);
                        if (text.length() > 0) {
                            g.fillRect(caret_bounds.x, caret_bounds.y, 2, caret_bounds.height);
                        } else {
                            g.fillRect(0, 0, 2, g.getFont().getSize());
                        }
                    }
                }
            } finally {
                g.setClip(prew_shape);
                g.translate(-x, -y);
            }
        }
        render_timer++;
        render_shadow_alpha = shadow_alpha;
        render_shadow_color = shadow_color;
        return render_size;
    }

    /**
	 * @param g
	 * @param max_line 最多显示几行
	 * @param x 绘制到g的位置
	 * @param y 绘制到g的位置
	 * @param sx 绘制文本的范围，是该Layout的内部坐标
	 * @param sy 绘制文本的范围，是该Layout的内部坐标
	 * @param sw 绘制文本的范围，是该Layout的内部坐标
	 * @param sh 绘制文本的范围，是该Layout的内部坐标
	 * @return
	 */
    public synchronized Dimension getDrawTextDimension(Graphics2D g, int x, int y, int sx, int sy, int sw, int sh) {
        x += 1;
        y += 1;
        sx -= 1;
        sy -= 1;
        sw += 2;
        sh += 2;
        tryChangeTextAndCaret(g);
        render_size.setSize(0, height);
        {
            for (TextLine line : textlines) {
                render_size.width = Math.max(line.width, render_size.width);
            }
        }
        return render_size;
    }

    private void tryChangeTextAndCaret(Graphics2D g) {
        if (textChange != null) {
            resetText(g, textChange);
            textChange = null;
            if (!is_read_only) {
                resetCaret();
            }
        } else if (set_caret_position >= 0) {
            if (!is_read_only) {
                caret_position = set_caret_position;
                caret_start_position = set_caret_position;
                caret_end_position = set_caret_position;
                set_caret_position = -1;
                resetCaret();
            }
        }
    }

    private void resetText(Graphics2D g, TextChanges change) {
        if (change.force || change.width != this.width || change.space != this.line_space || change.text.equals(this.text) == false) {
            this.text = change.text;
            this.attr_text = change.atext;
            this.width = change.width;
            this.height = 0;
            this.line_space = change.space;
            this.textlines.clear();
            this.render_antialiasing = false;
            if (text.length() > 0) {
                AttributedCharacterIterator it = change.atext.getIterator();
                attr_text = new AttributedString(text);
                int i = 0, e = 1;
                for (char c = it.first(); c != CharacterIterator.DONE; c = it.next()) {
                    Map<Attribute, Object> map = it.getAttributes();
                    Number size = (Number) map.get(TextAttribute.SIZE);
                    Font font = (Font) map.get(TextAttribute.FONT);
                    Integer anti = (Integer) map.get(com.g2d.display.ui.text.TextAttribute.ANTIALIASING);
                    if (anti != null && anti.intValue() == 1) {
                        this.render_antialiasing = true;
                    }
                    if (font == null) {
                        font = g.getFont();
                    }
                    if (size != null && size.intValue() != font.getSize()) {
                        font = new Font(font.getName(), 0, size.intValue());
                    }
                    attr_text.addAttribute(TextAttribute.FONT, font, i, e);
                    attr_text.addAttributes(map, i, e);
                    i++;
                    e++;
                }
                Object prew_rh = g.getRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING);
                try {
                    if (render_antialiasing) {
                        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    }
                    FontRenderContext frc = g.getFontRenderContext();
                    if (!is_single_line) {
                        LineBreakMeasurer textMeasurer = new LineBreakMeasurer(attr_text.getIterator(), frc);
                        while (textMeasurer.getPosition() >= 0 && textMeasurer.getPosition() < text.length()) {
                            TextLayout layout = null;
                            int limit = text.indexOf('\n', textMeasurer.getPosition());
                            if (limit >= textMeasurer.getPosition()) {
                                layout = textMeasurer.nextLayout(width, limit + 1, false);
                            } else {
                                layout = textMeasurer.nextLayout(width);
                            }
                            TextLine line = new TextLine(layout, textlines.size(), frc);
                            line.x = 0;
                            line.y = height;
                            height += line.height;
                            textlines.add(line);
                        }
                    } else {
                        TextLayout layout = new TextLayout(attr_text.getIterator(), g.getFontRenderContext());
                        TextLine line = new TextLine(layout, textlines.size(), frc);
                        line.x = 0;
                        line.y = height;
                        this.height += line.height;
                        this.width = line.width;
                        this.textlines.add(line);
                    }
                } finally {
                    g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, prew_rh);
                }
            }
        }
    }

    private void resetCaret() {
        int text_remain = text.length();
        for (int i = textlines.size() - 1; i >= 0; --i) {
            TextLine line = textlines.elementAt(i);
            text_remain -= line.getLayout().getCharacterCount();
            if (text_remain <= caret_position) {
                try {
                    int pos = Math.max(1, (caret_position - text_remain) + 1);
                    try {
                        caret_start_hit = line.getLayout().getNextLeftHit(pos);
                        caret_bounds = line.getLayout().getCaretShape(caret_start_hit).getBounds();
                        caret_bounds.x = caret_bounds.x - 1;
                    } catch (Exception e) {
                        caret_bounds.x = line.width + 1;
                    }
                    caret_bounds.y = line.y;
                    caret_bounds.height = line.height;
                    caret_bounds.width = 2;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                caret_start_line = line.line_index;
                caret_start_position = caret_position;
                break;
            }
        }
    }

    /**
	 * MultiTextLayout 中的一行数据
	 * @author WAZA
	 */
    private class TextLine {

        int x;

        int y;

        final int width;

        final int height;

        final int offsetx;

        final int offsety;

        final int line_index;

        final TextLayout text_layout;

        Rectangle selected = null;

        BufferedImage shadow_buffer = null;

        TextLine(TextLayout layout, int line, FontRenderContext frc) {
            this.text_layout = layout;
            this.line_index = line;
            this.offsetx = (int) 0;
            this.offsety = (int) layout.getAscent();
            this.width = (int) layout.getAdvance();
            this.height = (int) Math.max(10, layout.getAscent() + layout.getDescent() + line_space);
        }

        TextLayout getLayout() {
            return text_layout;
        }

        void render(Graphics2D g, int shadow_x, int shadow_y, float shadow_alpha, int shadow_color) {
            int tx = x;
            int ty = y;
            g.translate(tx, ty);
            try {
                if (is_show_select && selected != null) {
                    Color pc = g.getColor();
                    g.setColor(selected_color);
                    g.fill(selected);
                    g.setColor(pc);
                }
                drawShadow(g, shadow_x, shadow_y, shadow_alpha, shadow_color);
                text_layout.draw(g, offsetx, offsety);
            } finally {
                g.translate(-tx, -ty);
            }
        }

        private void drawShadow(Graphics2D g, int shadow_x, int shadow_y, float shadow_alpha, int shadow_color) {
            if (shadow_x != 0 && shadow_y != 0) {
                if (render_shadow_alpha != shadow_alpha && render_shadow_color != shadow_color) {
                    shadow_buffer = null;
                }
                if (shadow_buffer == null) {
                    this.shadow_buffer = Tools.createImage(width, height);
                    try {
                        Graphics2D g2d = shadow_buffer.createGraphics();
                        g2d.setColor(Color.BLACK);
                        g2d.setRenderingHints(g.getRenderingHints());
                        text_layout.draw(g2d, offsetx, offsety);
                        g2d.dispose();
                        this.shadow_buffer = Tools.toAlpha(shadow_buffer, shadow_alpha, shadow_color);
                    } catch (Throwable err) {
                    }
                }
                g.drawImage(shadow_buffer, shadow_x, shadow_y, null);
            } else {
                shadow_buffer = null;
            }
        }
    }

    /**
	 * 改变字符或尺寸的事件
	 * @author WAZA
	 */
    private class TextChanges {

        /** 强行刷新数据 */
        boolean force = false;

        String text = MultiTextLayout.this.text;

        AttributedString atext = MultiTextLayout.this.attr_text;

        int width = MultiTextLayout.this.width;

        int space = MultiTextLayout.this.line_space;

        TextChanges() {
        }

        TextChanges(String text) {
            set(text);
        }

        TextChanges(AttributedString atext) {
            set(atext);
        }

        void setWidth(int width) {
            this.width = width;
        }

        void setSpace(int space) {
            this.space = space;
        }

        void set(String text) {
            this.text = text;
            this.atext = new AttributedString(this.text);
        }

        void set(String text, AttributedString atext) {
            this.text = text;
            this.atext = atext;
        }

        void set(AttributedString atext) {
            this.atext = atext;
            this.text = Tools.toString(this.atext);
        }

        void append(String text) {
            this.text += text;
            this.atext = Tools.linkAttributedString(this.atext, new AttributedString(text));
        }

        void append(AttributedString atext) {
            this.atext = Tools.linkAttributedString(this.atext, atext);
            this.text = Tools.toString(this.atext);
        }
    }

    /**
	 * 一段有属性的字符串
	 * @author WAZA
	 */
    public static class AttributedSegment {

        public final Attribute attribute;

        public final Object attribute_value;

        public final String text;

        AttributedSegment(Attribute attribute, Object value, String text) {
            this.attribute = attribute;
            this.attribute_value = value;
            this.text = text;
        }

        @Override
        public String toString() {
            return "\"" + text + "\" : " + attribute + " : " + attribute_value;
        }
    }

    /**
	 * 插入字符后，变化结果
	 * @author WAZA
	 *
	 */
    public static class InsertInfo {

        /** 插入的原字符 */
        public final char src_char;

        /** 长度的变化，删减 */
        public int length_change;

        /** 如果长度增加，返回增加的字符 */
        public String inserted_text;

        public InsertInfo(char src) {
            this.src_char = src;
        }
    }
}
