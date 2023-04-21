package simtools.ui.test;

import simtools.ui.*;

/**
 * Test class for LogDialog, MessageWriter, StringsResourceBundle
 *
 * @see StringsResourceBundle
 * @see LogDialog
 *
 * @author Claude Cazenave
 *
 * @version 1.0 1999
 */
public class TestMessageDialog extends LogDialog {

    private DialogWriter _writer;

    /**
         * Creates a dialog with a title and strings resource
         * @param title the dialog title
         * @param resources the resources
         */
    public TestMessageDialog(String title, StringsResourceBundle resources) {
        super(title);
        _writer = new DialogWriter(resources);
    }

    /**
	 * Gets the current message writer
	 * @return the message writer
	 */
    public MessageWriter getWriter() {
        return _writer;
    }

    /**
	 * This class inherits from BasicMessageWriter to format the message
	 * and send the result to the dialog for display
	 */
    class DialogWriter extends BasicMessageWriter {

        /**
        	 * Creates it
        	 * @param resources the resources
        	 */
        public DialogWriter(StringsResourceBundle resources) {
            super(resources);
        }

        /**
        	 * Prints a message without argument
        	 * @param messageKey the message key
        	 * @return the message
        	 */
        public String print0args(String messageKey) {
            String res = super.print0args(messageKey);
            print(res + "\n");
            show();
            return res;
        }

        /**
        	 * Prints a message with one argument
        	 * @param messageKey the message key
        	 * @param arg1 the first argument
        	 * @return the message
        	 */
        public String print1args(String messageKey, Object arg1) {
            String res = super.print1args(messageKey, arg1);
            print(res + "\n");
            show();
            return res;
        }

        /**
         	 * Prints a message with two arguments
        	 * @param messageKey the message key
        	 * @param arg1 the first argument
        	 * @param arg2 the second argument
        	 * @return the message
        	 */
        public String print2args(String messageKey, Object arg1, Object arg2) {
            String res = super.print2args(messageKey, arg1, arg2);
            print(res + "\n");
            show();
            return res;
        }

        /**
        	 * Prints a message with n arguments
        	 * @param messageKey the message key
        	 * @param args an array of argument
        	 * @return the message
        	 */
        public String printNargs(String messageKey, Object[] args) {
            String res = super.printNargs(messageKey, args);
            print(res + "\n");
            show();
            return res;
        }
    }

    public static void main(String args[]) {
        TestMessageDialog f = null;
        StringsResourceBundle res = new StringsResourceBundle() {

            public String[][] getContents() {
                return contents;
            }

            final String[][] contents = { { "0", "message0" }, { "1", "message1 ", null }, { "2", "message2 ", null, null }, { "3", "message3 ", "one" }, { "4", "message4 ", null, " two" }, { "5", "message5 ", " one ", null }, { "6", "message6 ", " one ", " two" }, { "7", "message7 ", null, null, null }, { "8", "message8 ", null, null, " one" }, { "9", "message9 ", " one ", " two ", " three" } };
        };
        f = new TestMessageDialog("messages...", res);
        f.show();
        Object[] o = { "X", "Y", new Integer(3) };
        for (int i = 0; i < 40; i++) {
            try {
                Thread.sleep(100);
            } catch (Exception e) {
            }
            if (i < 10) {
                f.getWriter().print0args("" + i % 10);
            } else if (i < 20) {
                f.getWriter().print1args("" + i % 10, new Integer(i));
            } else if (i < 30) {
                f.getWriter().print2args("" + i % 10, new Integer(i), new Integer(2 * i));
            } else {
                f.getWriter().printNargs("" + i % 10, o);
            }
        }
    }
}
