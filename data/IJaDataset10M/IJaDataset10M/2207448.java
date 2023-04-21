package org.wikimodel.wem.util;

import java.util.ArrayList;
import java.util.List;
import junit.framework.TestCase;
import org.wikimodel.wem.util.TreeBuilder.IPos;

/**
 * @author MikhailKotelnikov
 */
public class ListBuilderTest extends TestCase {

    static class CharPos implements TreeBuilder.IPos {

        private char fCh;

        private int fPos;

        public CharPos(char ch, int pos) {
            fPos = pos;
            fCh = ch;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj == this) return true;
            if (!(obj instanceof CharPos)) return false;
            CharPos pos = (CharPos) obj;
            return equalsData(pos) && pos.fPos == fPos;
        }

        public boolean equalsData(IPos pos) {
            return ((CharPos) pos).fCh == fCh;
        }

        public int getPos() {
            return fPos;
        }
    }

    /**
     * @param name
     */
    public ListBuilderTest(String name) {
        super(name);
    }

    private List<IPos> getCharPositions(String s) {
        List<IPos> list = new ArrayList<IPos>();
        char[] array = s.toCharArray();
        for (int i = 0; i < array.length; i++) {
            char ch = array[i];
            if (!Character.isSpaceChar(ch)) list.add(new CharPos(ch, i));
        }
        return list;
    }

    public void testTwo() throws Exception {
        testTwo("a\n a\n a\n a", "<A><a>" + "<A>" + "<a></a>" + "<a></a>" + "<a></a>" + "</A>" + "</a></A>");
        testTwo("a\n b\n b\n b", "<A><a>" + "<B>" + "<b></b>" + "<b></b>" + "<b></b>" + "</B>" + "</a></A>");
        testTwo("a\n b\n b\n b\na", "<A>" + "<a>" + "<B>" + "<b></b>" + "<b></b>" + "<b></b>" + "</B>" + "</a>" + "<a></a>" + "</A>");
        testTwo("a\nab\nabc\nabcd", "<A><a>" + "<B><b>" + "<C><c>" + "<D><d></d></D>" + "</c></C>" + "</b></B>" + "</a></A>");
        testTwo("a\nab\nabc", "<A><a>" + "<B><b>" + "<C><c></c></C>" + "</b></B>" + "</a></A>");
        testTwo("" + "            a\n" + "       a   b\n" + "  a c\n" + " a c  d\n" + " e" + "" + "" + "" + "" + "", "<A><a>" + "<B><b></b></B>" + "<C><c>" + "<D><d></d></D>" + "</c></C>" + "</a></A>" + "<E><e></e></E>");
        testTwo("a\n b", "<A><a><B><b></b></B></a></A>");
        testTwo("a\nab", "<A><a><B><b></b></B></a></A>");
        testTwo("a\n         ", "<A><a></a></A>");
        testTwo(" ", "");
        testTwo("", "");
        testTwo("              ", "");
        testTwo("         a", "<A><a></a></A>");
        testTwo("     a     \n         ", "<A><a></a></A>");
        testTwo("" + " a\n" + "  b\n" + "  c\n" + "   d\n" + " e" + "" + "" + "" + "" + "", "<A><a>" + "<B><b></b></B>" + "<C><c>" + "<D><d></d></D>" + "</c></C>" + "</a></A>" + "<E><e></e></E>");
        testTwo("" + "a\n" + "ab\n" + "ac\n" + "acd\n" + "e" + "" + "" + "" + "" + "", "<A><a>" + "<B><b></b></B>" + "<C><c>" + "<D><d></d></D>" + "</c></C>" + "</a></A>" + "<E><e></e></E>");
        testTwo("" + "            a\n" + "       a   b\n" + "  a c\n" + " a c  d\n" + " e" + "" + "" + "" + "" + "", "<A><a>" + "<B><b></b></B>" + "<C><c>" + "<D><d></d></D>" + "</c></C>" + "</a></A>" + "<E><e></e></E>");
        testTwo("" + "    a\n" + "  b\n" + " c\n" + "cd\n" + "", "" + "<A><a></a></A>" + "<B><b></b></B>" + "<C><c>" + "<D><d></d></D>" + "</c></C>");
    }

    private void testTwo(String string, String control) {
        final StringBuffer buf = new StringBuffer();
        TreeBuilder builder = new TreeBuilder(new TreeBuilder.ITreeListener() {

            private void closeTag(char ch) {
                buf.append("</").append(ch).append(">");
            }

            public void onBeginRow(IPos n) {
                CharPos p = (CharPos) n;
                char ch = p.fCh;
                openTag(ch);
            }

            public void onBeginTree(IPos n) {
                CharPos p = (CharPos) n;
                char ch = p.fCh;
                openTag(Character.toUpperCase(ch));
            }

            public void onEndRow(IPos n) {
                CharPos p = (CharPos) n;
                char ch = p.fCh;
                closeTag(ch);
            }

            public void onEndTree(IPos n) {
                CharPos p = (CharPos) n;
                char ch = p.fCh;
                closeTag(Character.toUpperCase(ch));
            }

            private void openTag(char str) {
                buf.append("<").append(str).append(">");
            }
        });
        String[] lines = string.split("\n");
        for (String s : lines) {
            List<IPos> row = getCharPositions(s);
            builder.align(row);
        }
        List<IPos> empty = new ArrayList<IPos>();
        builder.align(empty);
        assertEquals(control, buf.toString());
    }
}
