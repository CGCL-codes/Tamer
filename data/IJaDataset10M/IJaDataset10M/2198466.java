package ru.st0ke.groundsquirrel.view;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CaretEvent;
import org.eclipse.swt.custom.CaretListener;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Caret;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.part.ViewPart;
import ru.st0ke.groundsquirrel.Activator;
import ru.st0ke.groundsquirrel.preferences.PreferenceConstants;

public class ConsoleView extends ViewPart implements IPropertyChangeListener {

    public static final String ID = "ru.st0ke.groundsquirrel.consoleView";

    private StyledText textArea;

    private String tempLine = "";

    private String lastLine = null;

    private final int margin = 5;

    public ConsoleView() {
    }

    @Override
    public void createPartControl(Composite parent) {
        textArea = new StyledText(parent, SWT.MULTI | SWT.V_SCROLL | SWT.WRAP | SWT.READ_ONLY);
        String fontName = Activator.getDefault().getPreferenceStore().getString(PreferenceConstants.consoleFontName);
        int fontSize = Activator.getDefault().getPreferenceStore().getInt(PreferenceConstants.consoleFontSize);
        boolean fontMiscFixed = Activator.getDefault().getPreferenceStore().getBoolean(PreferenceConstants.consoleFontUseBuiltIn);
        if (!fontMiscFixed || !Display.getCurrent().loadFont("fonts/Misc-Fixed.ttf")) {
            textArea.setFont(new Font(null, fontName, fontSize, SWT.NORMAL));
        } else {
            textArea.setFont(new Font(null, "Fixed Medium", fontSize, SWT.NORMAL));
        }
        textArea.setTabs(Activator.getDefault().getPreferenceStore().getInt(PreferenceConstants.consoleTabSize));
        textArea.setForeground(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
        textArea.setBackground(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
        Caret caret = new Caret(textArea, SWT.NONE);
        caret.setBounds(0, 0, fontSize, fontSize + fontSize / 2);
        textArea.setCaret(caret);
        textArea.setMargins(margin, margin, margin, margin);
        textArea.setWordWrap(true);
        textArea.setText("Ground Squirrel");
        textArea.addCaretListener(new CaretListener() {

            @Override
            public void caretMoved(CaretEvent event) {
                textArea.setCaretOffset(textArea.getText().length());
            }
        });
        textArea.addModifyListener(new ModifyListener() {

            @Override
            public void modifyText(ModifyEvent e) {
                textArea.setCaretOffset(textArea.getText().length());
                int visibledLines = (textArea.getSize().y - margin * 2) / textArea.getLineHeight();
                textArea.setTopIndex(textArea.getLineCount() >= visibledLines ? (textArea.getLineCount() - visibledLines) : 0);
            }
        });
        textArea.addKeyListener(new KeyListener() {

            @Override
            public void keyPressed(KeyEvent e) {
                if (!e.doit) {
                    return;
                }
                if (e.keyCode == 13) {
                    if (lastLine == null) {
                        lastLine = tempLine + "\n";
                    }
                    if (tempLine.length() > 0) {
                        tempLine = "";
                    }
                    textArea.append("\n");
                } else if (e.keyCode == 8 || e.keyCode == 127) {
                    if (tempLine.length() > 0) {
                        tempLine = tempLine.substring(0, tempLine.length() - 1);
                        textArea.setText(textArea.getTextRange(0, textArea.getText().length() - 1));
                    }
                } else {
                    if (e.keyCode == SWT.SHIFT) {
                    } else if (Character.isDefined(e.character)) {
                        tempLine += String.valueOf(e.character);
                        textArea.append(String.valueOf(e.character));
                    }
                }
                textArea.setCaretOffset(textArea.getText().length());
                int visibledLines = (textArea.getSize().y - margin * 2) / textArea.getLineHeight();
                textArea.setTopIndex(textArea.getLineCount() >= visibledLines ? (textArea.getLineCount() - visibledLines) : 0);
            }

            @Override
            public void keyReleased(KeyEvent e) {
            }
        });
        Activator.getDefault().getPreferenceStore().addPropertyChangeListener(this);
        setPartName("Console");
    }

    @Override
    public void setFocus() {
    }

    public void setText(String text) {
        textArea.setText("");
        textArea.append(text);
        textArea.setCaretOffset(textArea.getText().length());
    }

    public void append(String text) {
        textArea.append(text);
        textArea.setCaretOffset(textArea.getText().length());
    }

    public void clear() {
        tempLine = "";
        textArea.setText("");
        lastLine = null;
    }

    @Override
    public void dispose() {
        Activator.getDefault().getPreferenceStore().removePropertyChangeListener(this);
        super.dispose();
    }

    public String getLastLine() {
        return lastLine;
    }

    public void clearLastLine() {
        lastLine = null;
    }

    @Override
    public void propertyChange(PropertyChangeEvent event) {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        if (event.getProperty().equals(PreferenceConstants.consoleTabSize)) {
            textArea.setTabs((Integer) event.getNewValue());
        } else if (event.getProperty().equals(PreferenceConstants.consoleFontUseBuiltIn) || event.getProperty().equals(PreferenceConstants.consoleFontSize) || event.getProperty().equals(PreferenceConstants.consoleFontName)) {
            String fontName = store.getString(PreferenceConstants.consoleFontName);
            int fontSize = store.getInt(PreferenceConstants.consoleFontSize);
            Font font = textArea.getFont();
            if (store.getBoolean(PreferenceConstants.consoleFontUseBuiltIn) && Display.getCurrent().loadFont("fonts/Misc-Fixed.ttf")) {
                textArea.setFont(new Font(null, "Fixed Medium", fontSize, SWT.NORMAL));
            } else {
                textArea.setFont(new Font(null, fontName, fontSize, SWT.NORMAL));
                if (store.getBoolean(PreferenceConstants.consoleFontUseBuiltIn)) {
                    store.setValue(PreferenceConstants.consoleFontUseBuiltIn, false);
                }
            }
            font.dispose();
        }
    }
}
