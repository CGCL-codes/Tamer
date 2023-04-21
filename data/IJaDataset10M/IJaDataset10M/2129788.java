package superabbrevs;

import java.util.*;
import java.io.*;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.bsh.*;
import org.gjt.sp.jedit.bsh.ParseException;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.jedit.textarea.JEditTextArea;
import org.gjt.sp.jedit.textarea.Selection;
import org.gjt.sp.jedit.Buffer;
import org.gjt.sp.util.Log;
import superabbrevs.gui.AddAbbrevDialog;
import superabbrevs.template.*;
import superabbrevs.zencoding.html.*;
import superabbrevs.zencoding.ZenParser;
import javax.swing.JOptionPane;

/**************************************************************
 *            This class needs serious refactoring            *
 **************************************************************/
public class SuperAbbrevs {

    private static Hashtable modeAbbrevs = new Hashtable();

    /**
	 * Adds an abbreviation to the global abbreviation list.
	 * @param abbrev The abbreviation
	 * @param expansion The expansion
	 */
    public static void addGlobalAbbrev(String abbrev, String expansion) {
        addModeAbbrev("global", abbrev, expansion);
    }

    /**
	 * Adds a mode-specific abbrev.
	 * @param mode The edit mode
	 * @param abbrev The abbrev
	 * @param expansion The expansion
	 */
    public static void addModeAbbrev(String mode, String abbrev, String expansion) {
        Hashtable abbrevs = (Hashtable) modeAbbrevs.get(mode);
        if (abbrevs == null) {
            abbrevs = SuperAbbrevsIO.readModeFile(mode);
            if (abbrevs == null) {
                abbrevs = new Hashtable();
            }
            modeAbbrevs.put(mode, abbrevs);
        }
        abbrevs.put(abbrev, expansion);
        SuperAbbrevsIO.writeModeFile(mode, abbrevs);
    }

    public static void saveAbbrevs(String mode, Hashtable abbrevs) {
        modeAbbrevs.put(mode, abbrevs);
        SuperAbbrevsIO.writeModeFile(mode, abbrevs);
    }

    public static Hashtable loadAbbrevs(String mode) {
        Hashtable abbrevs = (Hashtable) modeAbbrevs.get(mode);
        if (abbrevs == null) {
            abbrevs = SuperAbbrevsIO.readModeFile(mode);
            if (abbrevs != null) modeAbbrevs.put(mode, abbrevs);
        }
        return abbrevs;
    }

    private static String getTemplateString(String mode, String abbrev) {
        Hashtable abbrevs = loadAbbrevs(mode);
        if (abbrevs == null || !abbrevs.containsKey(abbrev)) {
            abbrevs = loadAbbrevs("global");
        }
        if (abbrevs != null) {
            return (String) abbrevs.get(abbrev);
        } else {
            return null;
        }
    }

    private static Hashtable variables = null;

    public static void saveVariables(Hashtable variables) {
        SuperAbbrevs.variables = variables;
        SuperAbbrevsIO.writeModeFile("global.variables", variables);
    }

    public static Hashtable loadVariables() {
        if (variables == null) {
            variables = SuperAbbrevsIO.readModeFile("global.variables");
        }
        return variables;
    }

    /**
	 * Method tab(View view, JEditTextArea textArea, Buffer buffer)
	 * The method that desides what action should be taken for the tab key
	 */
    public static void tab(View view, JEditTextArea textArea, Buffer buffer) {
        if (!textArea.isEditable()) {
            textArea.getToolkit().beep();
        } else if (Handler.enabled(buffer)) {
            nextAbbrev(textArea, buffer);
        } else if (0 < textArea.getSelectionCount()) {
            textArea.insertTabAndIndent();
        } else {
            String abbrev = null;
            boolean zen = false;
            String mode = getMode(textArea, buffer);
            if (jEdit.getBooleanProperty("options.superabbrevs.zencoding") && jEdit.getProperty("options.superabbrevs.zencoding.modes").contains(mode)) {
                String lineText = buffer.getLineText(textArea.getCaretLine()).trim();
                if (!lineText.isEmpty() && (Character.isLetter(lineText.charAt(0)) || lineText.charAt(0) == '.' || lineText.charAt(0) == '#')) {
                    abbrev = lineText;
                    for (int i = 0; i < abbrev.length(); i++) {
                        if (!Character.isLetter(abbrev.charAt(i))) {
                            zen = true;
                            break;
                        }
                    }
                }
            }
            if (abbrev == null) {
                abbrev = getAbbrev(textArea, buffer);
            }
            if (abbrev.trim().equals("")) {
                textArea.insertTabAndIndent();
                return;
            }
            String template = null;
            if (zen) {
                ZenParser zenParser = new HTMLZenParser(new StringReader(abbrev));
                try {
                    if ("xml".equals(mode)) template = zenParser.parse(new XMLSerializer()); else template = zenParser.parse(new HTMLSerializer(jEdit.getProperties()));
                } catch (Throwable e) {
                }
            }
            if (template == null || template.isEmpty()) template = getTemplateString(mode, abbrev);
            if (template != null) {
                if (buffer.getBooleanProperty("noTabs")) {
                    int tabSize = buffer.getTabSize();
                    template = template.replaceAll("\t", spaces(tabSize));
                }
                try {
                    buffer.beginCompoundEdit();
                    removeAbbrev(textArea, buffer, abbrev);
                    Hashtable modeVariables = loadVariables();
                    expandAbbrev(view, template, modeVariables);
                } finally {
                    buffer.endCompoundEdit();
                }
            } else {
                textArea.insertTabAndIndent();
            }
        }
    }

    /**
	 * @return a string containing the number of spaces specified by the given
	 * size.
	 */
    private static String spaces(int size) {
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < size; i++) {
            buf.append(" ");
        }
        return buf.toString();
    }

    /**
	 * Method shiftTab(View view, JEditTextArea textArea, Buffer buffer)
	 * desides what action to take for the shift tab key
	 */
    public static void shiftTab(View view, JEditTextArea textArea, Buffer buffer) {
        if (Handler.enabled(buffer)) {
            SuperAbbrevs.prevAbbrev(textArea, buffer);
        } else if (0 < textArea.getSelectionCount()) {
            textArea.shiftIndentLeft();
        } else {
            String abbrev = getAbbrev(textArea, buffer);
            if (!abbrev.trim().equals("")) {
                Hashtable abbrevs = loadAbbrevs(getMode(textArea, buffer));
                String expansion = "";
                if (abbrevs != null && abbrevs.get(abbrev) != null) {
                    expansion = (String) abbrevs.get(abbrev);
                }
                AddAbbrevDialog dialog = new AddAbbrevDialog(view, abbrev, expansion);
            } else {
                textArea.shiftIndentLeft();
            }
        }
    }

    public static void expandAbbrev(View view) {
        Buffer buffer = view.getBuffer();
        JEditTextArea textArea = view.getTextArea();
        if (!textArea.isEditable()) {
            textArea.getToolkit().beep();
        } else if (Handler.enabled(buffer)) {
            textArea.getToolkit().beep();
        } else {
            String abbrev = getAbbrev(textArea, buffer);
            if (abbrev.trim().equals("")) {
                textArea.getToolkit().beep();
                return;
            }
            String mode = getMode(textArea, buffer);
            String template = getTemplateString(mode, abbrev);
            if (template != null) {
                removeAbbrev(textArea, buffer, abbrev);
                Hashtable modeVariables = loadVariables();
                expandAbbrev(view, template, modeVariables);
            } else {
                textArea.getToolkit().beep();
            }
        }
    }

    /**
	 * Expands the abbrev at the caret position in the specified
	 * view.
	 * @param view The view
	 */
    public static void expandAbbrev(View view, String template, Hashtable variables) {
        Buffer buffer = view.getBuffer();
        JEditTextArea textArea = view.getTextArea();
        int caretPos = textArea.getCaretPosition();
        String indent = getIndent(textArea, buffer);
        try {
            Interpreter interpreter = new Interpreter();
            String selection = "";
            if (textArea.getSelectionCount() == 1) {
                selection = textArea.getSelectedText();
                selection = selection.replaceAll("\\\\", "\\\\\\\\");
                textArea.setSelectedText("");
                caretPos = textArea.getCaretPosition();
            }
            interpreter.set("filename", buffer.getName());
            interpreter.set("selection", selection);
            putVariables(interpreter, variables);
            Template t = TemplateFactory.createTemplate(template, interpreter, indent);
            t.setOffset(caretPos);
            textArea.setSelectedText(t.toString(), false);
            selectField(textArea, t.getCurrentField());
            Handler h = new Handler(t, textArea, buffer);
            Handler.putHandler(buffer, h);
            TemplateCaretListener.putCaretListener(textArea, new TemplateCaretListener());
        } catch (TargetError e) {
            System.out.println("TargetError");
            System.out.println(e.getMessage());
        } catch (ParseException e) {
            System.out.println("ParseException");
            System.out.println(e.getMessage());
        } catch (EvalError e) {
            System.out.println("EvalError");
            System.out.println(e.getErrorLineNumber());
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("IOException");
            System.out.println(e.getMessage());
        }
    }

    /**
	 * Method showAbbrevDialog(View view, JEditTextArea textArea, Buffer buffer)
	 * show a dialog to type in the abbrev
	 */
    public static void showAbbrevDialog(View view, JEditTextArea textArea, Buffer buffer) {
        if (!textArea.isEditable() || 1 < textArea.getSelectionCount() || Handler.enabled(buffer)) {
            textArea.getToolkit().beep();
            return;
        }
        String abbrev = JOptionPane.showInputDialog(view, "Type in an abbreviation", "Abbreviation Input", JOptionPane.INFORMATION_MESSAGE);
        if (abbrev != null && !abbrev.trim().equals("")) {
            String mode = getMode(textArea, buffer);
            String template = getTemplateString(mode, abbrev);
            if (template != null) {
                Hashtable variables = loadVariables();
                expandAbbrev(view, template, variables);
            }
        }
    }

    public static void nextAbbrev(JEditTextArea textArea, Buffer buffer) {
        Handler h = Handler.getHandler(buffer);
        Template t = h.getTemplate();
        if (t != null) {
            TemplateCaretListener listener = TemplateCaretListener.removeCaretListener(textArea);
            t.nextField();
            SelectableField f = t.getCurrentField();
            if (f != null) {
                int start = f.getOffset();
                int end = start + f.getLength();
                textArea.setCaretPosition(end);
                textArea.addToSelection(new Selection.Range(start, end));
            }
            TemplateCaretListener.putCaretListener(textArea, listener);
        }
    }

    public static void prevAbbrev(JEditTextArea textArea, Buffer buffer) {
        Handler h = Handler.getHandler(buffer);
        Template t = h.getTemplate();
        if (t != null) {
            TemplateCaretListener listener = TemplateCaretListener.removeCaretListener(textArea);
            t.prevField();
            SelectableField f = t.getCurrentField();
            if (f != null) {
                int start = f.getOffset();
                int end = start + f.getLength();
                textArea.setCaretPosition(end);
                textArea.addToSelection(new Selection.Range(start, end));
            }
            TemplateCaretListener.putCaretListener(textArea, listener);
        }
    }

    public static String getMode(JEditTextArea textArea, Buffer buffer) {
        int caretPos = textArea.getCaretPosition();
        return buffer.getRuleSetAtOffset(caretPos).getModeName();
    }

    /**
	 * Method selectField(JEditTextArea textArea, SelectableField field)
	 * Select the field in the buffer
	 */
    public static void selectField(JEditTextArea textArea, SelectableField field) {
        int start = field.getOffset();
        int end = start + field.getLength();
        textArea.setCaretPosition(end);
        textArea.addToSelection(new Selection.Range(start, end));
    }

    /**
	 * Get the abbreviation before the caret
	 */
    private static String getAbbrev(JEditTextArea textArea, Buffer buffer) {
        int line = textArea.getCaretLine();
        int lineStart = buffer.getLineStartOffset(line);
        int caretPos = textArea.getCaretPosition();
        int caretPosition = caretPos - lineStart;
        String lineText = buffer.getLineText(line);
        int i = caretPosition - 1;
        while (0 <= i && Character.isJavaIdentifierPart(lineText.charAt(i))) {
            i--;
        }
        return lineText.substring(i + 1, caretPosition);
    }

    private static String getIndent(JEditTextArea textArea, Buffer buffer) {
        int lineNumber = textArea.getCaretLine();
        String line = buffer.getLineText(lineNumber);
        int i = 0;
        String output = "";
        while (i < line.length() && (line.charAt(i) == ' ' || line.charAt(i) == '\t')) {
            output = output + line.substring(i, i + 1);
            i++;
        }
        return output;
    }

    /**
	 * Method removeAbbrev(Buffer buffer, String abbrev)
	 * Removes the abbreviation from the buffer
	 */
    public static void removeAbbrev(JEditTextArea textArea, Buffer buffer, String abbrev) {
        int caretPos = textArea.getCaretPosition();
        int templateStart = caretPos - abbrev.length();
        buffer.remove(templateStart, abbrev.length());
    }

    public static void makeDefaults() {
        SuperAbbrevsIO.createAbbrevsDir();
        SuperAbbrevsIO.removeOldMacros();
        SuperAbbrevsIO.writeDefaultAbbrevs();
        SuperAbbrevsIO.writeDefaultVariables();
        SuperAbbrevsIO.writeDefaultAbbrevFunctions();
        SuperAbbrevsIO.writeDefaultTemplateGenerationFunctions();
    }

    private static void putVariables(Interpreter interpreter, Hashtable variables) throws EvalError {
        if (variables != null) {
            Iterator iter = variables.entrySet().iterator();
            while (iter.hasNext()) {
                Map.Entry entry = (Map.Entry) iter.next();
                String name = (String) entry.getKey();
                String value = (String) entry.getValue();
                interpreter.set(name, value);
            }
        }
    }
}
