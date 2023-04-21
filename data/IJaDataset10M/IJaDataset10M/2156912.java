package to_do_o.gui.dialog;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.Shell;
import to_do_o.core.Constants;
import to_do_o.gui.IconProvider;

/**
 * 
 * @author Ruediger Gad
 * 
 */
public class AboutDialog extends AbstractGenericDialog {

    private Rectangle linkArea = new Rectangle(0, 0, 0, 0);

    private int indent;

    protected String linkText = "http://to-do-o.sourceforge.net";

    public AboutDialog(Shell parent) {
        super(parent);
        linkArea.width = 200;
        linkArea.height = 50;
    }

    public void open() {
        shell.setText("About");
        shell.setBackground(shell.getDisplay().getSystemColor(SWT.COLOR_WHITE));
        shell.addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent e) {
                GC gc = e.gc;
                Image img = IconProvider.getIconImage("/icons/to_do_o_logo_about_dialog.png");
                gc.drawImage(img, 0, 0);
                gc.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_BLACK));
                indent = getIndent();
                if (isShellLandscape()) {
                    String text = "To-Do-O - " + Constants.VERSION + "\n" + "A simple and free to-do list organizer\n" + "(Click to close.)";
                    int textWidth = getTextWidth(text, gc);
                    if (textWidth + indent > shell.getSize().x) {
                        indent = shell.getSize().x - textWidth - gc.getCharWidth(' ');
                    }
                    gc.drawText(text, indent, shell.getBounds().height / 5, SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER);
                } else {
                    gc.drawText("To-Do-O - " + Constants.VERSION + "\n" + "A simple and free\nto-do list organizer\n" + "(Click to close.)", indent, shell.getBounds().height / 5, SWT.DRAW_TRANSPARENT | SWT.DRAW_DELIMITER);
                }
                int linkTextWidth = getTextWidth(linkText, gc);
                if (linkTextWidth + indent > shell.getSize().x) {
                    indent = shell.getSize().x - linkTextWidth - gc.getCharWidth(' ');
                }
                linkArea.x = indent;
                linkArea.y = (shell.getBounds().height / 5) * 3;
                gc.setForeground(shell.getDisplay().getSystemColor(SWT.COLOR_DARK_BLUE));
                gc.drawText("http://to-do-o.sourceforge.net", linkArea.x, linkArea.y, SWT.DRAW_TRANSPARENT);
            }

            private int getTextWidth(String text, GC gc) {
                int max = 0;
                int current = 0;
                for (int i = 0; i < text.length(); i++) {
                    char c = text.charAt(i);
                    if (c != '\n') {
                        current += gc.getCharWidth(c);
                    } else {
                        max = Math.max(max, current);
                        current = 0;
                    }
                }
                return Math.max(max, current);
            }
        });
        shell.addMouseListener(new MouseListener() {

            public void mouseUp(MouseEvent arg0) {
            }

            public void mouseDown(MouseEvent e) {
                if (linkArea.contains(e.x, e.y)) {
                    linkClicked();
                } else {
                    shell.dispose();
                }
            }

            public void mouseDoubleClick(MouseEvent arg0) {
            }
        });
        shell.addControlListener(new ControlListener() {

            public void controlResized(ControlEvent arg0) {
                linkArea.x = indent;
                linkArea.y = (shell.getBounds().height / 5) * 3;
            }

            public void controlMoved(ControlEvent arg0) {
            }
        });
        layoutAndOpen();
    }

    private int getIndent() {
        return (isShellLandscape()) ? shell.getBounds().width / 2 : shell.getBounds().width / 5;
    }

    private boolean isShellLandscape() {
        return (shell.getSize().x > shell.getSize().y);
    }

    /**
     * Hook for implementing actions when the &quot;link&quot; is clicked.
     */
    protected void linkClicked() {
        shell.dispose();
    }
}
