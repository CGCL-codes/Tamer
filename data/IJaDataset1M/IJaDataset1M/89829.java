package abbot.tester.swt;

import junit.framework.Assert;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Shell;
import abbot.finder.swt.MultipleWidgetsFoundException;
import abbot.finder.swt.WidgetNotFoundException;
import abbot.script.swt.InstrumentedButtonReference;
import abbot.swt.DefaultWidgetFinder;
import abbot.script.swt.WidgetReference;

/**
 * Provides widget-specific actions, assertions, and getter methods for
 * widgets of type Button.
 */
public class ButtonTester extends ControlTester {

    public static final String copyright = "Licensed Materials	-- Property of IBM\n" + "(c) Copyright International Business Machines Corporation, 2003\nUS Government " + "Users Restricted Rights - Use, duplication or disclosure restricted by GSA " + "ADP Schedule Contract with IBM Corp.";

    int alignment;

    Image image;

    boolean selection;

    String text;

    /**
	 * Sets the above properties to their current values for the given widget. 
	 * NOTE: This should be called in a block of code synchronized on this
	 * tester.
	 */
    protected synchronized void getProperties(final Button b) {
        super.getProperties(b);
        Robot.syncExec(b.getDisplay(), this, new Runnable() {

            public void run() {
                alignment = b.getAlignment();
                image = b.getImage();
                selection = b.getSelection();
                text = b.getText();
            }
        });
    }

    public synchronized int getAlignment(Button b) {
        getProperties(b);
        return alignment;
    }

    public synchronized Image getImage(Button b) {
        getProperties(b);
        return image;
    }

    public synchronized boolean getSelection(Button b) {
        getProperties(b);
        return selection;
    }

    public synchronized String getText(Button b) {
        getProperties(b);
        return text;
    }

    /**
	 * Factory method.
	 */
    public static ButtonTester getButtonTester() {
        return (ButtonTester) (getTester(Button.class));
    }

    /**
	 * Get an instrumented <code>Button</code> from its <code>id</code> 
	 * Because we instrumented it, we assume it not only can be found,
	 * but is unique, so we don't even try to catch the *Found exceptions.
	 * CONTRACT: instrumented <code>Button</code> must be unique and findable with param.
	 */
    public static Button getInstrumentedButton(String id) {
        return getInstrumentedButton(id, null);
    }

    /**
	 * Get an instrumented <code>Button</code> from its <code>id</code>
	 * and the <code>title</code> of its shell (e.g. of the wizard
	 * containing it). 
	 * Because we instrumented it, we assume it not only can be found,
	 * but is unique, so we don't even try to catch the *Found exceptions.
	 * CONTRACT: instrumented <code>Button</code> must be unique and findable with param.
	 */
    public static Button getInstrumentedButton(String id, String title) {
        return getInstrumentedButton(id, title, null);
    }

    /**
	 * Get an instrumented <code>Button</code> from its 
	 * <ol>
	 * <li><code>id</code></li>
	 * <li><code>title</code> of its shell (e.g. of the wizard containing it)</li>
	 * <li><code>text</code> that it contains (<code>""</code> if none)</li>
	 * </ol>
	 * Because we instrumented it, we assume it not only can be found,
	 * but is unique, so we don't even try to catch the *Found exceptions.
	 * CONTRACT: instrumented <code>Button</code> must be unique and findable with param.
	 */
    public static Button getInstrumentedButton(String id, String title, String text) {
        return getInstrumentedButton(id, title, text, null);
    }

    /**
	 * Get an instrumented <code>Button</code> from its 
	 * <ol>
	 * <li><code>id</code></li>
	 * <li><code>title</code> of its shell (e.g. of the wizard containing it)</li>
	 * <li><code>text</code> that it contains (<code>""</code> if none)</li>
	 * <li><code>shell</code> that contains it</li>
	 * </ol>
	 * Because we instrumented it, we assume it not only can be found,
	 * but is unique, so we don't even try to catch the *Found exceptions.
	 * CONTRACT: instrumented <code>Button</code> must be unique and findable with param.
	 */
    public static Button getInstrumentedButton(String id, String title, String text, Shell shell) {
        WidgetReference ref = new InstrumentedButtonReference(id, null, title, text);
        Button t = null;
        try {
            if (shell == null) {
                t = DefaultWidgetFinder.findButton(ref);
            } else {
                t = DefaultWidgetFinder.findButtonInShell(ref, shell);
            }
        } catch (WidgetNotFoundException nf) {
            Assert.fail("no instrumented Button \"" + id + "\" found");
        } catch (MultipleWidgetsFoundException mf) {
            Assert.fail("many instrumented Buttons \"" + id + "\" found");
        }
        Assert.assertNotNull("ERROR: null Button", t);
        return t;
    }
}
