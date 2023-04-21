package net.sourceforge.hibernateswt.widget.window;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swtbot.swt.finder.SWTBot;
import org.eclipse.swtbot.swt.finder.utils.SWTBotPreferences;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotButton;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotLabel;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotShell;
import org.eclipse.swtbot.swt.finder.widgets.SWTBotText;
import org.junit.BeforeClass;
import org.junit.Test;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;

/**
 * WindowSWTBotTest
 *
 * @author Martin Dale Lyness <martin.lyness@gmail.com>
 * @since Sep 8, 2010
 */
public class WindowSWTBotTest {

    private static Window shell;

    private static Window shell2;

    private static SWTBot bot;

    static {
        new Thread() {

            @Override
            public void run() {
                shell = new Window() {

                    private ColorScheme personalColorScheme;

                    private Label labelA;

                    private Text textA;

                    private Button buttonA;

                    @Override
                    public boolean beforeClose() {
                        return true;
                    }

                    @Override
                    protected void preInitGUI() {
                        Window.applyTitleTagPostfix(" [POSTFIX TITLE]");
                        GridLayout layout = new GridLayout();
                        layout.numColumns = 2;
                        setLayout(layout);
                        personalColorScheme = new ColorScheme("Personal Scheme");
                        personalColorScheme.mapWidgetBackgroundImage(this.getClass(), "splash1.jpg");
                        setLocalColorScheme(personalColorScheme);
                    }

                    @Override
                    protected void initGUI() {
                        labelA = new Label(this, SWT.BORDER);
                        labelA.setText("Label A:");
                        textA = new Text(this, SWT.BORDER);
                        textA.setText("fill in...");
                        buttonA = new Button(this, SWT.PUSH);
                        buttonA.setText("BuTToN");
                    }

                    @Override
                    protected void postInitGUI() {
                        pack();
                    }

                    @Override
                    protected void defineHotkeys() {
                    }

                    @Override
                    protected void applyWidgetActionListeners() {
                    }

                    @Override
                    protected void applyColorSchemeOverrides(ColorScheme scheme) {
                        Display.getDefault().syncExec(new Runnable() {

                            public void run() {
                                buttonA.setBackground(new Color(Display.getDefault(), 123, 123, 123));
                                buttonA.setForeground(new Color(Display.getDefault(), 213, 213, 213));
                            }
                        });
                    }

                    @Override
                    protected String getTitle() {
                        return "Test Window";
                    }
                };
                shell2 = new Window() {

                    private Label firstCloseAttemptedFailedLabel;

                    private boolean allowClose = false;

                    @Override
                    public boolean beforeClose() {
                        if (allowClose) return true;
                        allowClose = true;
                        return false;
                    }

                    @Override
                    protected void preInitGUI() {
                        setLayout(new FillLayout());
                    }

                    @Override
                    protected void initGUI() {
                        firstCloseAttemptedFailedLabel = new Label(this, SWT.NONE);
                        firstCloseAttemptedFailedLabel.setText("This window tests the close all windows feature on a failed window shutdown.");
                        firstCloseAttemptedFailedLabel.setSize(300, 300);
                    }

                    @Override
                    protected void postInitGUI() {
                    }

                    @Override
                    protected void defineHotkeys() {
                    }

                    @Override
                    protected void applyWidgetActionListeners() {
                    }

                    @Override
                    protected String getTitle() {
                        return "Fail Close";
                    }
                };
                shell.pack();
                shell.openBlocking();
            }
        }.start();
    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        ColorScheme colorScheme = new ColorScheme("Test scheme");
        colorScheme.setDefaultBackgroundColor(0, 0, 0);
        colorScheme.setDefaultForegroundColor(255, 255, 255);
        colorScheme.mapWidgetBackgroundColorSystemDefault(Text.class);
        colorScheme.mapWidgetForegroundColorSystemDefault(Text.class);
        Window.setGlobalColorScheme(colorScheme);
        bot = new SWTBot();
        SWTBotPreferences.PLAYBACK_DELAY = 10;
        SWTBotPreferences.TYPE_INTERVAL = 1000;
    }

    /**
     * Assure the rules for color scheme application are followed.
     *
     * Rules:
     * 1. Apply global backgrounds and foregrounds
     * 2. Apply global background images
     * 3. Override any local backgrounds and forgrounds defined
     * 4. Override any local background images defined
     * 5. Override any widget specific definitions found in the local applyColorSchemeOverrides() method
     *
     * NOTE: If a definition is not defined locally the global value should remain unchanged.
     * @throws Exception
     */
    @Test
    public void colorSchemeApplied() throws Exception {
        final SWTBotButton button = bot.button("BuTToN");
        assertEquals(123, button.backgroundColor().getGreen());
        assertEquals(213, button.foregroundColor().getGreen());
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                assertEquals(null, button.widget.getBackgroundImage());
            }
        });
        final SWTBotLabel label = bot.label("Label A:");
        assertEquals(0, label.backgroundColor().getBlue());
        assertEquals(255, label.foregroundColor().getBlue());
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                assertEquals(null, label.widget.getBackgroundImage());
            }
        });
        final SWTBotText text = bot.text(0);
        assertFalse(0 == text.backgroundColor().getBlue());
        assertFalse(255 == text.foregroundColor().getBlue());
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                assertEquals(null, text.widget.getBackgroundImage());
            }
        });
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                assertTrue("The main window didn't have it's background image set, returned null.", shell.getBackgroundImage() != null);
                assertEquals("The main window's foreground was modified or never set to the global color.", 255, shell.getForeground().getRed());
            }
        });
    }

    @Test
    public void testPostfixPrefixTitleApplies() {
        SWTBotShell[] shells = bot.shells();
        for (SWTBotShell shell : shells) {
            if (!shell.getText().trim().equals("")) {
                assertTrue(shell.getText().indexOf("[POSTFIX TITLE]") > 0);
            }
        }
    }

    @Test
    public void testCloseAllWindowsFeature() {
        Display.getDefault().syncExec(new Runnable() {

            public void run() {
                shell2.open();
                boolean allClosed = Window.closeAll();
                assertFalse(allClosed);
            }
        });
    }
}
