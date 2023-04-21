package tools;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

/**
 * hebt Texte farbig hervor
 * @author Tim
 *
 */
public class HighlightSyntax {

    private HighlightSyntax() {
    }

    /**
	 * colors the keywords in 'content' that are listed in the HashMap 'map'
	 * 
	 * @param map			keywords and color
	 * @param constlength	the left gap of the element -> needed to overlap the existing keyword
	 * @param y				the Y-Position of the content
	 * @param g				Graphics instance
	 */
    @SuppressWarnings({ "unchecked" })
    public static void colorText(HashMap<Color, Vector<String>> map, String content, int constlength, int y, Graphics g) {
        Iterator<?> it;
        Vector<String> tmp = new Vector<String>();
        Object curr;
        it = map.entrySet().iterator();
        while (it.hasNext()) {
            curr = it.next();
            tmp = (Vector<String>) ((Map.Entry) curr).getValue();
            for (int i = 0; i < tmp.size(); ++i) {
                if (content.startsWith(tmp.elementAt(i) + " ") || content.startsWith(tmp.elementAt(i)) && content.length() == tmp.elementAt(i).length()) {
                    g.setColor((Color) ((Map.Entry) curr).getKey());
                    g.drawString(tmp.elementAt(i) + " ", constlength, y);
                    g.setColor(Color.black);
                }
                if (content.contains("" + tmp.elementAt(i) + "")) {
                    Vector<Point> points = subString(content, "" + tmp.elementAt(i) + "");
                    boolean justDigit = false;
                    for (int j = 0; j < tmp.elementAt(i).length(); ++j) {
                        if (!isSpecialKey(tmp.elementAt(i).charAt(j))) break;
                        justDigit = true;
                    }
                    if (!justDigit) for (int j = 0; j < points.size(); ++j) {
                        if ((points.elementAt(j).x - 1) < 0 || !isSpecialKey(content.charAt(points.elementAt(j).x - 1))) {
                            points.remove(j);
                            j--;
                            continue;
                        }
                        if ((points.elementAt(j).y) < content.length() && !isSpecialKey(content.charAt(points.elementAt(j).y))) {
                            points.remove(j);
                            j--;
                        }
                    }
                    for (int l = 0; l < points.size(); ++l) {
                        int startp = constlength + calculateLength(content.substring(0, points.elementAt(l).x), g);
                        g.setColor((Color) ((Map.Entry) curr).getKey());
                        g.drawString("" + tmp.elementAt(i) + "", startp, y);
                        g.setColor(Color.black);
                    }
                }
                if (tmp.elementAt(i).startsWith("<QUOTE") && tmp.elementAt(i).endsWith(">") && tmp.elementAt(i).length() == 8) {
                    char quote = tmp.elementAt(i).charAt(6);
                    int startp = -1;
                    int endp = -1;
                    for (int m = 0; m < content.length(); ++m) {
                        if (content.charAt(m) == quote) {
                            if (startp == -1) startp = m; else if (endp == -1) endp = m;
                            if (startp != -1 && endp != -1) {
                                g.setColor((Color) ((Map.Entry) curr).getKey());
                                g.drawString(content.substring(startp, endp + 1), constlength + calculateLength(content.substring(0, startp), g), y);
                                g.setColor(Color.black);
                                startp = endp = -1;
                            }
                        }
                    }
                } else if (tmp.elementAt(i).startsWith("<FROM") && tmp.elementAt(i).contains("TO") && tmp.elementAt(i).endsWith(">") && tmp.elementAt(i).length() == 10) {
                    char from = tmp.elementAt(i).charAt(5);
                    char to = tmp.elementAt(i).charAt(8);
                    for (int m = 0; m < content.length(); ++m) {
                        if (content.charAt(m) >= from && content.charAt(m) <= to) {
                            if (HighlightSyntax.fromToCheck(content, m, from, to)) {
                                g.setColor((Color) ((Map.Entry) curr).getKey());
                                g.drawString(content.substring(m, m + 1), constlength + calculateLength(content.substring(0, content.substring(0, m).length()), g), y);
                                g.setColor(Color.black);
                            }
                        }
                    }
                } else if (tmp.elementAt(i).startsWith("<QUOTEFROM") && tmp.elementAt(i).contains("TO") && tmp.elementAt(i).endsWith(">") && tmp.elementAt(i).length() == 15) {
                    char from = tmp.elementAt(i).charAt(10);
                    char to = tmp.elementAt(i).charAt(13);
                    int startp = -1;
                    int endp = -1;
                    for (int m = 0; m < content.length(); ++m) {
                        if (content.charAt(m) == (int) from) startp = m; else if (content.charAt(m) == (int) to) endp = m;
                        if (startp != -1 && endp != -1) {
                            g.setColor((Color) ((Map.Entry) curr).getKey());
                            g.drawString(content.substring(startp, endp + 1), constlength + calculateLength(content.substring(0, content.substring(0, startp).length()), g), y);
                            g.setColor(Color.black);
                            startp = -1;
                            endp = -1;
                        }
                    }
                }
            }
        }
    }

    private static int calculateLength(String cnt, Graphics g) {
        FontMetrics fm = g.getFontMetrics();
        return fm.getStringBounds(cnt, g).getBounds().width;
    }

    private static Vector<Point> subString(String cnt, String sub) {
        Vector<Point> points = new Vector<Point>();
        for (int i = 0; i < cnt.length(); ++i) {
            boolean cmp_successful = true;
            try {
                for (int j = 0; j < sub.length(); ++j) {
                    if (cnt.charAt(i + j) != sub.charAt(j)) {
                        cmp_successful = false;
                        break;
                    }
                }
            } catch (StringIndexOutOfBoundsException e) {
                continue;
            }
            if (!cmp_successful) continue;
            Point p = new Point(i, i + sub.length());
            points.add(p);
        }
        return points;
    }

    /**
	 * returns the startposition of a word in 'content'
	 * @return
	 */
    private static int getWordStart(String content, int currentPos) {
        for (int i = currentPos; i > 0; --i) {
            if ((((int) content.charAt(i) > 32 && (int) content.charAt(i) < 48) || ((int) content.charAt(i) > 57 && (int) content.charAt(i) < 65) || ((int) content.charAt(i) > 90 && (int) content.charAt(i) < 97) || ((int) content.charAt(i) > 122)) || content.charAt(i) == ' ' || content.charAt(i) == '(' || content.charAt(i) == ')') {
                return i + 1;
            }
        }
        return 0;
    }

    /**
	 * returns the end posittion of a word in 'content'
	 * @return
	 */
    private static int getWordEnd(String content, int currentPos) {
        for (int i = currentPos; i < content.length(); ++i) {
            if ((((int) content.charAt(i) > 32 && (int) content.charAt(i) < 48) || ((int) content.charAt(i) > 57 && (int) content.charAt(i) < 65) || ((int) content.charAt(i) > 90 && (int) content.charAt(i) < 97) || ((int) content.charAt(i) > 122)) || content.charAt(i) == ' ' || content.charAt(i) == '(' || content.charAt(i) == ')') {
                return i - 1;
            }
        }
        return content.length() - 1;
    }

    /**
	 * checks weather a word contains just keywords or not
	 * 
	 * @param keywordFrom	the current from-keyword
	 * @param keywordTo		the current to-keyword
	 * @return false if another char has been found
	 */
    private static boolean fromToCheck(String content, int currentPos, char keywordFrom, char keywordTo) {
        int start = HighlightSyntax.getWordStart(content, currentPos);
        int end = HighlightSyntax.getWordEnd(content, currentPos);
        for (int i = start; i <= end; ++i) {
            if (content.charAt(i) < keywordFrom || content.charAt(i) > keywordTo) {
                return false;
            }
        }
        return true;
    }

    /**
	 * check for special chars like '(', ...
	 * 
	 * @param c
	 * @return
	 */
    public static boolean isSpecialKey(char c) {
        if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || (c == 'Ä' || c == 'Ö' || c == 'Ü' || c == 'ä' || c == 'ö' || c == 'ü')) {
            return false;
        }
        return true;
    }
}
