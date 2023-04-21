package consciouscode.bonsai.channels;

import consciouscode.junit.TestCase;

public class PropertyChannelTest extends TestCase {

    public static class Bean {

        public int getInt() {
            return myInt;
        }

        public void setInt(int value) {
            myInt = value;
        }

        public String getText() {
            return myText;
        }

        public void setText(String text) {
            myText = text;
        }

        private int myInt;

        private String myText;
    }

    @SuppressWarnings("hiding")
    public static final String AUTO_SUITES = "common,gui";

    public void testPushPull() {
        Bean bean = new Bean();
        PropertyChannel textChannel = new PropertyChannel(bean, "text");
        assertNull(textChannel.getValue());
        String newText = "hello";
        textChannel.setValue(newText);
        assertSame(newText, bean.getText());
        newText = "goodbye";
        textChannel.setValue(newText);
        assertSame(newText, bean.getText());
        PropertyChannel intChannel = new PropertyChannel(bean, "int");
        assertEquals(new Integer(0), intChannel.getValue());
        intChannel.setValue(new Integer(2003));
        assertEquals(2003, bean.getInt());
        try {
            intChannel.setValue(null);
            fail("Expected IllegalArgumentException or NullPointerException");
        } catch (NullPointerException e) {
        } catch (IllegalArgumentException e) {
        }
    }
}
