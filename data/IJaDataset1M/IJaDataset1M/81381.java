package com.townsfolkdesigns.jedit.plugins.scripting;

import com.townsfolkdesigns.jedit.plugins.scripting.forms.CreateMacroForm;
import org.gjt.sp.jedit.Macros;
import org.gjt.sp.jedit.Mode;
import org.gjt.sp.jedit.View;
import org.gjt.sp.jedit.jEdit;
import org.gjt.sp.util.Log;
import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.swing.JOptionPane;

/**
 *
 * @author elberry
 */
public class MacroDelegate {

    private static final String MACRO_TEMPLATE = jEdit.getProperty("scriptengine.plugin.macro.template");

    private ScriptEngineDelegate scriptEngineDelegate;

    public MacroDelegate() {
        this(new ScriptEngineDelegate());
    }

    public MacroDelegate(ScriptEngineDelegate scriptEngineManager) {
        this.scriptEngineDelegate = scriptEngineManager;
    }

    /**
    * Prompts the user for the required Macro information (name, directory, language) and then creates an emptry macro
    * source file in the given language.
    * @param view
    */
    public void createMacro(View view) {
        scriptEngineDelegate.getScriptEngineManager();
        CreateMacroForm form = new CreateMacroForm();
        form.show(view);
        int dialogValue = form.getDialogValue();
        if (dialogValue == JOptionPane.OK_OPTION) {
            String macroName = form.getMacroName();
            String directory = form.getDirectoryName();
            Mode mode = form.getMode();
            createMacro(macroName, directory, mode, null);
        }
    }

    /**
    * Prompts the user for the required Macro information (name, directory) and then creates a macro source file
    * containing the contents of the current buffer.
    * @param view
    */
    public void createMacroFromBuffer(View view) {
        scriptEngineDelegate.getScriptEngineManager();
        Mode bufferMode = view.getBuffer().getMode();
        String bufferTitle = view.getBuffer().getName();
        int dotIndex = bufferTitle.lastIndexOf(".");
        if (dotIndex > 0) {
            bufferTitle = bufferTitle.substring(0, dotIndex);
        }
        CreateMacroForm form = new CreateMacroForm();
        form.setShowModeOn(false);
        form.setMacroName(bufferTitle);
        form.show(view);
        int dialogValue = form.getDialogValue();
        if (dialogValue == JOptionPane.OK_OPTION) {
            String macroName = form.getMacroName();
            String directory = form.getDirectoryName();
            createMacro(macroName, directory, bufferMode, view.getTextArea().getText());
        }
    }

    public void executeMacro(View view, String scriptPath, String modeStr) {
        Mode mode = jEdit.getMode(modeStr);
        File bshScriptFile = new File(scriptPath);
        String bshScriptName = bshScriptFile.getName();
        int indexOfDot = bshScriptName.lastIndexOf(".");
        bshScriptName = bshScriptName.substring(0, indexOfDot);
        String scriptExtension = ScriptEngineUtilities.getScriptExtension(mode);
        String macroScriptName = bshScriptName + "." + scriptExtension;
        File macroScriptFile = new File(bshScriptFile.getParentFile(), macroScriptName);
        if (macroScriptFile.exists()) {
            ScriptEngine engine = scriptEngineDelegate.getScriptEngineForMode(mode);
            ScriptContext scriptContext = ScriptEngineUtilities.getDefaultScriptContext(view);
            scriptContext.setAttribute("scriptPath", macroScriptFile, ScriptContext.ENGINE_SCOPE);
            engine.setContext(scriptContext);
            try {
                engine.eval(new FileReader(macroScriptFile));
            } catch (Exception e) {
                Log.log(Log.ERROR, ScriptExecutionDelegate.class, "Error executing script file: " + macroScriptFile.getPath(), e);
            }
        }
    }

    private String cleanMacroName(String macroName) {
        String cleanMacroName = macroName;
        cleanMacroName = cleanMacroName.replaceAll(" ", "_");
        return cleanMacroName;
    }

    private void createMacro(String macroName, String directory, Mode mode, String content) {
        macroName = cleanMacroName(macroName);
        File macroFile = new File(jEdit.getSettingsDirectory(), "macros");
        String scriptExtension = ScriptEngineUtilities.getScriptExtension(mode);
        if (macroFile.exists() && (scriptExtension != null)) {
            macroFile = new File(macroFile, directory);
            if (!macroFile.exists()) {
                macroFile.mkdirs();
            }
            File scriptMacroFile = writeScriptMacroTemplate(macroFile, macroName, mode);
            if (content != null) {
                PrintWriter pw = null;
                try {
                    pw = new PrintWriter(scriptMacroFile);
                    pw.print(content);
                    pw.flush();
                } catch (Exception e) {
                    Log.log(Log.ERROR, MacroDelegate.class, "Error writing buffer contents to script file: " + scriptMacroFile.getPath(), e);
                } finally {
                    if (pw != null) {
                        try {
                            pw.close();
                        } catch (Exception e) {
                            Log.log(Log.ERROR, MacroDelegate.class, "Error closing print stream on script file: " + scriptMacroFile.getPath(), e);
                        }
                    }
                }
            }
            jEdit.openFile(jEdit.getActiveView(), scriptMacroFile.getPath());
            Macros.loadMacros();
        } else if (scriptExtension == null) {
            Log.log(Log.ERROR, MacroDelegate.class, "No extension found for mode \"" + mode + "\" - please make sure you have a ScriptEnginePlugin installed for this language.");
            Macros.error(jEdit.getActiveView(), "No extension found for mode \"" + mode + "\" - please make sure you have a ScriptEnginePlugin installed for this language.");
        }
    }

    private File writeScriptMacroTemplate(File macroFile, String macroName, Mode mode) {
        String bshMacroFileName = macroName + ".bsh";
        String scriptExtension = ScriptEngineUtilities.getScriptExtension(mode);
        String scriptMacroFileName = macroName + "." + scriptExtension;
        File bshMacroFile = new File(macroFile, bshMacroFileName);
        File scriptMacroFile = new File(macroFile, scriptMacroFileName);
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(bshMacroFile);
            writer.format(MACRO_TEMPLATE, mode);
            writer.flush();
        } catch (Exception e) {
            Log.log(Log.ERROR, MacroDelegate.class, "Error creating file writer.", e);
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    Log.log(Log.ERROR, MacroDelegate.class, "Error closing file writer.", e);
                }
            }
        }
        try {
            scriptMacroFile.createNewFile();
        } catch (Exception e) {
            Log.log(Log.ERROR, MacroDelegate.class, "Error creating script macro file: " + scriptMacroFile.getPath(), e);
        }
        return scriptMacroFile;
    }
}
