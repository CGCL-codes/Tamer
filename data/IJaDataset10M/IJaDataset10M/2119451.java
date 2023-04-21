package ${app.AppPackageDots}.model.test;

import ${app.AppPackageDots}.model.Message;

import junit.framework.TestCase;

public class MessageTest extends TestCase {
    public MessageTest(String title) {
        super(title);
    }

    public void testTextAccessors() {
        Message obj = new Message();

        String expected = "VALUE";
        obj.setText(expected);

        String actual = obj.getText();
        assertEquals(expected, actual);
    }
}
