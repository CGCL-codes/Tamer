package abbot.swt.junit.extensions;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.ImageLoader;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Widget;
import abbot.swt.display.DisplayTester;
import abbot.swt.display.DisplayUtil;
import abbot.swt.display.Result;
import abbot.swt.hierarchy.Hierarchy;
import abbot.swt.hierarchy.Visitor;
import abbot.swt.hierarchy.WidgetHierarchyImpl;
import abbot.swt.hierarchy.text.WidgetHierarchyPrinter;
import abbot.swt.internal.WidgetLocator;

public class SWTTestUtil {

    /**
	 * <p>
	 * Value ("<b>abbot.swt.error.Dumps</b>") is the name of a system property
	 * that specifies what information to dump if a test failure or error
	 * occurs. The property's value should be a comma-separated list of zero or
	 * more of "screenshot", "widgets" and "stacks". If no value is specified
	 * then "stacks,screenshot,widgets" is assumed. If the property is not
	 * defined then nothing is dumped.
	 * </p>
	 * <p>
	 * Dumps are created in the directory specified by abbot.swt.error.DumpsDir
	 * (or the current directory if abbot.swt.error.DumpsDir is not specified).
	 * Files names are as follows:
	 * <ul>
	 * <li>If "<b>stacks</b>" is present then a test failure or error will cause
	 * stack traces of all threads to be written to a file. The name of the file
	 * will be the name of the test + "_stacks.txt".</li>
	 * <li>If "<b>widgets</b>" is present then a test failure or error will
	 * cause the widget hierarchy to be written to a file. The name of the file
	 * will be the name of the test + "_widgets.txt".</li>
	 * <li>If "<b>screenshot</b>" is present then a test failure or error will
	 * cause a screenshot to be written to a file. The name of the file will be
	 * the name of the test + ".jpg".</li>
	 * </ul>
	 * </p>
	 * 
	 * @see #DUMP_DIR
	 */
    public static final String DUMP_TYPES = "abbot.swt.error.Dumps";

    /**
	 * <p>
	 * Value ("<b>abbot.swt.error.DumpsDir</b>") is the name of a system
	 * property that specifies a directory in which dumps are to be created if a
	 * test failure or error occurs. If not present then any dumps will be
	 * created in the current directory.
	 * </p>
	 * 
	 * @see #DUMP_TYPES
	 */
    public static final String DUMP_DIR = "abbot.swt.error.DumpsDir";

    static void checkDumps(Display display, String name) {
        String dumps = System.getProperty(DUMP_TYPES);
        if (dumps != null) {
            boolean dumpStacks = false;
            boolean dumpScreenshot = false;
            boolean dumpWidgets = false;
            dumps = dumps.trim();
            if (dumps.length() == 0) {
                dumpStacks = true;
                dumpScreenshot = true;
                dumpWidgets = true;
            } else {
                StringTokenizer tokenizer = new StringTokenizer(dumps, ",");
                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken().trim();
                    if (token.equalsIgnoreCase("stacks")) dumpStacks = true; else if (token.equalsIgnoreCase("screenshot")) dumpScreenshot = true; else if (token.equals("widgets")) dumpWidgets = true;
                }
            }
            String directory = null;
            if (dumpStacks || dumpScreenshot || dumpWidgets) {
                String filePrefix = name;
                directory = System.getProperty(DUMP_DIR);
                if (directory != null && !(directory.length() == 0)) filePrefix = directory + File.separatorChar + name;
                filePrefix = new File(filePrefix).getAbsolutePath();
                if (dumpStacks) dumpStacks(display, filePrefix + "_stacks.txt");
                if (dumpScreenshot) dumpScreenshot(display, filePrefix + ".jpg");
                if (dumpWidgets) dumpWidgets(display, filePrefix + "_widgets.txt");
            }
        }
    }

    private static void dumpScreenshot(Display display, String filename) {
        System.out.printf("\nWriting screenshot to %s\n", filename);
        DisplayTester displayTester = new DisplayTester(display);
        Image image = displayTester.capture();
        try {
            ImageLoader loader = new ImageLoader();
            loader.data = new ImageData[] { image.getImageData() };
            loader.save(filename, SWT.IMAGE_JPEG);
        } catch (Exception exception) {
            exception.printStackTrace();
        } finally {
            if (image != null) image.dispose();
        }
    }

    private static void dumpWidgets(Display display, String filename) {
        System.out.printf("\nWriting widget hierarchy to %s\n", filename);
        PrintStream stream = null;
        try {
            stream = new PrintStream(filename);
            Hierarchy<Widget> hierarchy = WidgetHierarchyImpl.getHierarchy(display);
            final WidgetHierarchyPrinter printer = new WidgetHierarchyPrinter(hierarchy, stream);
            display.syncExec(new Runnable() {

                public void run() {
                    printer.print();
                }
            });
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } finally {
            if (stream != null) stream.close();
        }
    }

    private static void dumpStacks(Display display, String filename) {
        System.out.printf("\nWriting stacks to %s\n", filename);
        PrintStream stream = null;
        try {
            stream = new PrintStream(filename);
            for (Map.Entry<Thread, StackTraceElement[]> entry : Thread.getAllStackTraces().entrySet()) {
                Thread thread = entry.getKey();
                StackTraceElement[] stack = entry.getValue();
                stream.printf("\n%s:", thread);
                for (int i = 0; i < stack.length; i++) stream.printf("\n\t" + stack[i]);
            }
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        } finally {
            if (stream != null) stream.close();
        }
    }

    static void checkThread(Display display) {
        if (!DisplayTester.isDisplayThread(display) && !UserThread.isOnUserThread()) throw new IllegalStateException("invalid thread");
    }

    static Shell[] getShells(final Display display) {
        if (DisplayTester.isDisplayThread(display)) return display.getShells();
        return DisplayUtil.syncExec(display, new Result<Shell[]>() {

            public Shell[] result() {
                return display.getShells();
            }
        });
    }

    static void closeOtherShells(final Display display, final Shell[] initialShells) {
        if (display != null && !display.isDisposed()) {
            if (DisplayTester.isDisplayThread(display)) {
                closeShells(display, initialShells);
            } else {
                display.syncExec(new Runnable() {

                    public void run() {
                        closeShells(display, initialShells);
                    }
                });
            }
        }
    }

    /**
	 * Holds some information about {@link Menu}s that were stuck open.
	 * 
	 * @see #closeStuckMenus(Display)
	 * @see #getStuckMenuInfos(Display)
	 */
    private static class MenuInfo {

        public String string;

        public boolean isBar;
    }

    /**
	 * Attempts to close any pop-up or drop-down Menus that are "stuck" open.
	 */
    static void closeStuckMenus(final Display display) {
        if (display != null && !display.isDisposed()) {
            Map<Menu, MenuInfo> infos = getStuckMenuInfos(display);
            for (Entry<Menu, MenuInfo> entry : infos.entrySet()) {
                MenuInfo info = entry.getValue();
                System.out.printf("Stuck menu (bar: %s): %s\n", info.isBar, info.string);
                esc(display);
                if (info.isBar) esc(display);
            }
        }
    }

    /**
	 * Generate ESC key press and release.
	 * 
	 * @see #closeStuckMenus(Display)
	 */
    static void esc(Display display) {
        Event event = new Event();
        event.type = SWT.KeyDown;
        event.character = SWT.ESC;
        display.post(event);
        event.type = SWT.KeyUp;
        display.post(event);
    }

    /**
	 * Gets a {@link Map} the keys of which are all drop-down and pop-up
	 * {@link Menu}s that are "stuck" open. The values are corresponding
	 * {@link MenuInfo}'s.
	 * 
	 * @see #closeStuckMenus(Display)
	 */
    private static Map<Menu, MenuInfo> getStuckMenuInfos(final Display display) {
        final Map<Menu, MenuInfo> infos = new HashMap<Menu, MenuInfo>();
        display.syncExec(new Runnable() {

            public void run() {
                WidgetHierarchyImpl.getHierarchy(display).accept(new Visitor<Widget>() {

                    public Result visit(Widget widget) {
                        if (!widget.isDisposed() && widget instanceof Menu) {
                            int style = widget.getStyle();
                            if ((style & (SWT.POP_UP | SWT.DROP_DOWN)) != 0) {
                                Rectangle bounds = WidgetLocator.getBounds(widget);
                                if (bounds != null && !bounds.isEmpty() && !infos.containsKey(widget)) {
                                    Menu menu = (Menu) widget;
                                    MenuInfo info = new MenuInfo();
                                    info.string = menu.toString();
                                    info.isBar = isBar(menu);
                                    infos.put(menu, info);
                                }
                            }
                        }
                        return Result.ok;
                    }
                });
            }
        });
        return infos;
    }

    /**
	 * @return true iff the {@link Menu} is, or is a descendent of, a menu bar.
	 * @see #getStuckMenuInfos(Display)
	 */
    static boolean isBar(Menu menu) {
        while (menu != null) {
            if ((menu.getStyle() & SWT.BAR) != 0) return true;
            menu = menu.getParentMenu();
        }
        return false;
    }

    private static void closeShells(Display display, Shell[] initialShells) {
        Shell[] remainingShells = display.getShells();
        for (int i = 0; i < remainingShells.length; i++) {
            Shell remainingShell = remainingShells[i];
            if (shouldClose(remainingShell, initialShells)) remainingShell.close();
        }
    }

    static boolean shouldClose(Shell remainingShell, Shell[] initialShells) {
        if (remainingShell.isDisposed()) return false;
        for (int j = 0; j < initialShells.length; j++) {
            if (remainingShell == initialShells[j]) return false;
        }
        return true;
    }
}
