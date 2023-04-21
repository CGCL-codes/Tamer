package coolkey.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.PaintListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Canvas;
import coolkey.CoolKey;
import coolkey.TestResults;

/**
 * Obszar na którym odbywa się przepisywanie.
 */
public class TypingArea {

    public static final Color COLOR_CORRECTION = new Color(GUI.display, 192, 0, 216);

    private final int MAX_LINES = 12;

    /**
	 * Maksymalna ilość przepisanych linii jaką widać na ekranie.
	 */
    private final int MAX_TYPING_LINES = 3;

    private final int LEFT_MARGIN = 15;

    private final int TOP_MARGIN_TEXT = 6;

    private final int TOP_MARGIN_WRITTEN = 27;

    private final int LINE_HEIGHT = 44;

    private Canvas canvas;

    public TypingArea() {
        canvas = new Canvas(GUI.shell, SWT.BORDER | SWT.DOUBLE_BUFFERED);
        canvas.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
        canvas.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent keyEvent) {
                pressKey(keyEvent.character);
            }

            @Override
            public void keyReleased(KeyEvent keyEvent) {
            }
        });
        canvas.addPaintListener(new PaintListener() {

            public void paintControl(PaintEvent pe) {
                GC gc = pe.gc;
                Point canvasSize = canvas.getSize();
                gc.setBackground(GUI.display.getSystemColor(SWT.COLOR_WHITE));
                gc.setForeground(GUI.display.getSystemColor(SWT.COLOR_BLACK));
                gc.setFont(new Font(GUI.display, "Courier New", 10, SWT.NORMAL));
                gc.fillRectangle(0, 0, canvasSize.x, canvasSize.y);
                int startLine = CoolKey.getCurrentTest().getWrittenLines().size() - MAX_TYPING_LINES;
                if (startLine < 0) {
                    startLine = 0;
                }
                int endLine = startLine + MAX_TYPING_LINES - 1;
                if (endLine > CoolKey.getCurrentTest().getWrittenLines().size() - 1) {
                    endLine = CoolKey.getCurrentTest().getWrittenLines().size() - 1;
                }
                int x = LEFT_MARGIN;
                int y = TOP_MARGIN_TEXT;
                for (int i = startLine; i <= endLine; i++) {
                    String line = CoolKey.getCurrentTest().getTextLines().get(i);
                    gc.drawString(line, x, y);
                    y += LINE_HEIGHT;
                }
                for (int i = endLine + 1; i < CoolKey.getCurrentTest().getTextLines().size() && i < endLine + 1 + MAX_LINES - 2 * (1 + endLine - startLine); i++) {
                    String line = CoolKey.getCurrentTest().getTextLines().get(i);
                    gc.drawString(line, x, y);
                    y += LINE_HEIGHT / 2;
                }
                gc.setForeground(GUI.display.getSystemColor(SWT.COLOR_BLUE));
                y = TOP_MARGIN_WRITTEN;
                for (int i = startLine; i <= endLine; i++) {
                    String line = CoolKey.getCurrentTest().getWrittenLines().get(i);
                    gc.drawString(line, x, y);
                    y += LINE_HEIGHT;
                }
                String lastLine = CoolKey.getCurrentTest().getWrittenLines().get(CoolKey.getCurrentTest().getWrittenLines().size() - 1);
                String cursor = "";
                for (int i = 0; i < lastLine.length(); i++) {
                    cursor += ' ';
                }
                cursor += '_';
                if (CoolKey.getCurrentTest().isMistakeMade()) {
                    gc.setForeground(GUI.display.getSystemColor(SWT.COLOR_RED));
                }
                y -= LINE_HEIGHT;
                gc.drawString(cursor, x, y, true);
                gc.setForeground(COLOR_CORRECTION);
                y = TOP_MARGIN_WRITTEN;
                for (int i = startLine; i <= endLine; i++) {
                    String line = CoolKey.getCurrentTest().getCorrections().get(i);
                    gc.drawString(line, x, y, true);
                    y += LINE_HEIGHT;
                }
                gc.setForeground(GUI.display.getSystemColor(SWT.COLOR_RED));
                y = TOP_MARGIN_WRITTEN;
                for (int i = startLine; i <= endLine; i++) {
                    String line = CoolKey.getCurrentTest().getMistakes().get(i);
                    gc.drawString(line, x, y, true);
                    y += LINE_HEIGHT;
                }
            }
        });
        canvas.redraw();
    }

    /**
	 * Obsługa zdarzenia polegającego na wpisaniu danego znaku.
	 *
	 * @param c Wpisywany znak.
	 */
    public void pressKey(char c) {
        if (!CoolKey.getCurrentTest().isFinished()) {
            if (c == '\r') {
                if (CoolKey.getCurrentTest().typeEnter()) {
                    if (CoolKey.isSoundAvailable() && CoolKey.getUser().getConfig().isSoundOn() && CoolKey.getCurrentTest().isStarted()) {
                        CoolKey.getSoundBank().TYPEWRITER.play();
                    }
                } else {
                    if (CoolKey.isSoundAvailable() && CoolKey.getUser().getConfig().isSoundOn() && CoolKey.getCurrentTest().isStarted()) {
                        CoolKey.getSoundBank().MISTAKE.play();
                    }
                }
            } else if (c == SWT.BS) {
                if (CoolKey.isSoundAvailable() && CoolKey.getUser().getConfig().isSoundOn() && CoolKey.getCurrentTest().isStarted()) {
                    CoolKey.getSoundBank().TYPEWRITER.play();
                }
                CoolKey.getCurrentTest().typeBackspace();
            } else if (!Character.isISOControl(c)) {
                if (!CoolKey.getCurrentTest().isStarted()) {
                    GUI.graphs.reset();
                }
                if (CoolKey.getCurrentTest().typeChar(c)) {
                    if (CoolKey.isSoundAvailable() && CoolKey.getUser().getConfig().isSoundOn()) {
                        CoolKey.getSoundBank().TYPEWRITER.play();
                    }
                } else {
                    if (CoolKey.isSoundAvailable() && CoolKey.getUser().getConfig().isSoundOn()) {
                        CoolKey.getSoundBank().MISTAKE.play();
                    }
                }
            }
            if (CoolKey.getCurrentTest().isFinished()) {
                TestResults finalResults = CoolKey.getCurrentTest().getResults();
                CoolKey.getUser().addResults(finalResults);
                GUI.graphs.addFinalResults(finalResults);
                GUI.graphs.refresh();
                new ResultsMessage(finalResults);
            }
            canvas.redraw();
            if (!Character.isISOControl(c) && CoolKey.getCurrentTest().isPaused()) {
                CoolKey.getCurrentTest().pauseUnpause();
            }
            GUI.buttonBar.refresh();
            GUI.keyboard.refresh();
        }
    }

    /**
	 * Wyświetl aktualną wersję tego elementu.
	 */
    public void refresh() {
        canvas.redraw();
    }

    /**
	 * Skup uwagę na tym obszarze, aby przechwytywać zdarzenia.
	 */
    public void setFocus() {
        canvas.setFocus();
    }
}
