package acide.process.parser;

import java.io.File;
import javax.swing.JOptionPane;
import acide.files.AcideFileManager;
import acide.files.bytes.AcideByteFileManager;
import acide.gui.mainWindow.AcideMainWindow;
import acide.language.AcideLanguageManager;
import acide.log.AcideLog;
import acide.process.gui.AcideProgressWindow;
import acide.resources.AcideResourceManager;

/**
 * ACIDE - A Configurable IDE grammar file creation process.
 * 
 * @version 0.8
 * @see Thread
 */
public class AcideGrammarFileCreationProcess extends Thread {

    /**
	 * ACIDE - A Configurable IDE grammar file creation process grammar name.
	 */
    private String _grammarName;

    /**
	 * ACIDE - A Configurable IDE grammar file creation process verbose process.
	 */
    private boolean _verboseProcess;

    /**
	 * Creates a new ACIDE - A Configurable IDE grammar file creation process.
	 * 
	 * @param grammarName
	 *            grammar name.
	 * @param verboseProcess
	 *            verbose process flag.
	 */
    public AcideGrammarFileCreationProcess(String grammarName, boolean verboseProcess) {
        _grammarName = grammarName;
        _verboseProcess = verboseProcess;
    }

    @Override
    public void run() {
        if (_verboseProcess) {
            AcideProgressWindow.getInstance().setInitialText(AcideLanguageManager.getInstance().getLabels().getString("s1063"));
            AcideProgressWindow.getInstance().showWindow();
        }
        executeAntlr();
        modifyGrammarParserFile();
        compileGeneratedFiles();
        reallocateGeneratedFiles();
        generateJarFile();
        deleteGeneratedFiles();
        reallocateJarFile();
        if (_verboseProcess) AcideProgressWindow.getInstance().enableCloseButton(); else {
            JOptionPane.showMessageDialog(AcideMainWindow.getInstance(), AcideLanguageManager.getInstance().getLabels().getString("s1065"), AcideLanguageManager.getInstance().getLabels().getString("s1066"), JOptionPane.INFORMATION_MESSAGE);
            AcideMainWindow.getInstance().setEnabled(true);
            AcideMainWindow.getInstance().setAlwaysOnTop(true);
            AcideMainWindow.getInstance().setAlwaysOnTop(false);
        }
    }

    /**
	 * Executes ANTLR for generating the required files to generate the .jar
	 * file.
	 */
    private void executeAntlr() {
        String javaPath = null;
        try {
            javaPath = AcideResourceManager.getInstance().getProperty("javaPath");
            if (javaPath.equals("null")) throw new Exception(AcideLanguageManager.getInstance().getLabels().getString("s927"));
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, AcideLanguageManager.getInstance().getLabels().getString("s928"), AcideLanguageManager.getInstance().getLabels().getString("s934"), JOptionPane.ERROR_MESSAGE);
            AcideLog.getLog().error(exception.getMessage());
            exception.printStackTrace();
            AcideProgressWindow.getInstance().closeWindow();
            return;
        }
        AcideProgressWindow.getInstance().setText(AcideLanguageManager.getInstance().getLabels().getString("s1049"));
        AcideProgressWindow.getInstance().setText("\"" + javaPath + "\" -cp ./lib/antlr.jar antlr.Tool grammar.g");
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("\"" + javaPath + "\" -cp ./lib/antlr.jar antlr.Tool grammar.g");
            process.waitFor();
        } catch (Exception exception) {
            AcideLog.getLog().error(exception.getMessage());
            exception.printStackTrace();
            AcideProgressWindow.getInstance().closeWindow();
            return;
        }
        AcideProgressWindow.getInstance().setText(AcideLanguageManager.getInstance().getLabels().getString("s1050"));
    }

    /**
	 * Adds the exceptions to the generated GrammarParser.java file.
	 */
    private void modifyGrammarParserFile() {
        AcideProgressWindow.getInstance().setText(AcideLanguageManager.getInstance().getLabels().getString("s1051"));
        File file = new File("GrammarParser.java");
        if (!file.exists()) {
            return;
        }
        String fileContent = null;
        fileContent = AcideFileManager.getInstance().load("GrammarParser.java");
        String exception = "throw new RecognitionException();";
        String aux = "";
        int index = fileContent.indexOf("recover(ex");
        index++;
        aux = fileContent.substring(index);
        int indexAux = aux.indexOf(";");
        index += indexAux + 1;
        String head = fileContent.substring(0, index);
        String tail = fileContent.substring(index);
        fileContent = head + exception + tail;
        boolean finished = false;
        while (!finished) {
            index = fileContent.lastIndexOf(exception);
            aux = fileContent.substring(index);
            indexAux = aux.indexOf("recover(ex");
            if (indexAux == -1) finished = true; else {
                String aux2 = aux.substring(indexAux);
                int indexAux2 = aux2.indexOf(";");
                index = index + indexAux + indexAux2 + 1;
                head = fileContent.substring(0, index);
                tail = fileContent.substring(index);
                fileContent = head + exception + tail;
            }
        }
        AcideFileManager.getInstance().write("GrammarParser.java", fileContent);
        AcideProgressWindow.getInstance().setText(AcideLanguageManager.getInstance().getLabels().getString("s1052"));
    }

    /**
	 * Compiles the generated files by ANTLR to obtain the .class files.
	 */
    private void compileGeneratedFiles() {
        String javacPath = null;
        try {
            javacPath = AcideResourceManager.getInstance().getProperty("javacPath");
            if (javacPath.equals("null")) throw new Exception(AcideLanguageManager.getInstance().getLabels().getString("s929"));
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, AcideLanguageManager.getInstance().getLabels().getString("s929"), AcideLanguageManager.getInstance().getLabels().getString("s933"), JOptionPane.ERROR_MESSAGE);
            AcideLog.getLog().error(exception.getMessage());
            exception.printStackTrace();
            AcideProgressWindow.getInstance().closeWindow();
            return;
        }
        AcideProgressWindow.getInstance().setText(AcideLanguageManager.getInstance().getLabels().getString("s1053"));
        AcideProgressWindow.getInstance().setText("\"" + javacPath + "\" -cp .;c:\\classes .\\*.java -d .");
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("\"" + javacPath + "\" -cp .;c:\\classes .\\*.java -d .");
            process.waitFor();
        } catch (Exception exception) {
            AcideLog.getLog().error(exception.getMessage());
            exception.printStackTrace();
            AcideProgressWindow.getInstance().closeWindow();
            return;
        }
        AcideProgressWindow.getInstance().setText(AcideLanguageManager.getInstance().getLabels().getString("s1054"));
    }

    /**
	 * Reallocates the generated files into the correspondent folder in the
	 * source folder of ACIDE - A Configurable IDE.
	 */
    private void reallocateGeneratedFiles() {
        AcideProgressWindow.getInstance().setText(AcideLanguageManager.getInstance().getLabels().getString("s1055"));
        AcideByteFileManager.getInstance().reallocateFile("GrammarLexer.java", "src/acide/process/parser/grammar/GrammarLexer.java");
        AcideByteFileManager.getInstance().reallocateFile("GrammarLexer.smap", "src/acide/process/parser/grammar/GrammarLexer.smap");
        AcideByteFileManager.getInstance().reallocateFile("GrammarLexerTokenTypes.java", "src/acide/process/parser/grammar/GrammarLexerTokenTypes.java");
        AcideByteFileManager.getInstance().reallocateFile("GrammarLexerTokenTypes.txt", "src/acide/process/parser/grammar/GrammarLexerTokenTypes.txt");
        AcideByteFileManager.getInstance().reallocateFile("GrammarParser.java", "src/acide/process/parser/grammar/GrammarParser.java");
        AcideByteFileManager.getInstance().reallocateFile("GrammarParser.smap", "src/acide/process/parser/grammar/GrammarParser.smap");
        AcideByteFileManager.getInstance().reallocateFile("grammar.g", "src/acide/process/parser/grammar/grammar.g");
        AcideByteFileManager.getInstance().reallocateFile("syntaxRules.txt", "src/acide/process/parser/grammar/syntaxRules.txt");
        AcideByteFileManager.getInstance().reallocateFile("lexicalCategories.txt", "src/acide/process/parser/grammar/lexicalCategories.txt");
        AcideProgressWindow.getInstance().setText(AcideLanguageManager.getInstance().getLabels().getString("s1056"));
    }

    /**
	 * Generates the .jar file which contains the grammar configuration.
	 */
    private void generateJarFile() {
        String jarPath = null;
        try {
            jarPath = AcideResourceManager.getInstance().getProperty("jarPath");
            if (jarPath.equals("null")) throw new Exception(AcideLanguageManager.getInstance().getLabels().getString("s930"));
        } catch (Exception exception) {
            JOptionPane.showMessageDialog(null, AcideLanguageManager.getInstance().getLabels().getString("s930"), AcideLanguageManager.getInstance().getLabels().getString("s932"), JOptionPane.ERROR_MESSAGE);
            AcideLog.getLog().error(exception.getMessage());
            exception.printStackTrace();
        }
        AcideProgressWindow.getInstance().setText(AcideLanguageManager.getInstance().getLabels().getString("s1057"));
        Process process = null;
        try {
            process = Runtime.getRuntime().exec("\"" + jarPath + "\" cfm " + _grammarName + ".jar " + "acide/process/parser/grammar/manifest.txt " + "acide/process/parser/grammar");
            process.waitFor();
        } catch (Exception exception) {
            AcideLog.getLog().error(exception.getMessage());
            exception.printStackTrace();
            AcideProgressWindow.getInstance().closeWindow();
            return;
        }
        AcideProgressWindow.getInstance().setText(AcideLanguageManager.getInstance().getLabels().getString("s1058"));
    }

    /**
	 * Deletes the generated files by ANTLR once the .jar file is generated and
	 * they have been reallocated in the correspondent folder.
	 */
    private void deleteGeneratedFiles() {
        AcideProgressWindow.getInstance().setText(AcideLanguageManager.getInstance().getLabels().getString("s1059"));
        File file = new File("GrammarLexer.java");
        file.delete();
        file = new File("GrammarLexer.class");
        file.delete();
        file = new File("GrammarLexerTokenTypes.java");
        file.delete();
        file = new File("GrammarLexerTokenTypes.class");
        file.delete();
        file = new File("GrammarParser.java");
        file.delete();
        file = new File("GrammarParser.class");
        file.delete();
        file = new File("GrammarLexer.smap");
        file.delete();
        file = new File("GrammarParser.smap");
        file.delete();
        file = new File("GrammarLexerTokenTypes.txt");
        file.delete();
        file = new File("grammar.g");
        file.delete();
        file = new File("syntaxRules.txt");
        file.delete();
        file = new File("lexicalCategories.txt");
        file.delete();
        file = new File("acide");
        deleteSubdirectories(file);
        file.delete();
        AcideProgressWindow.getInstance().setText(AcideLanguageManager.getInstance().getLabels().getString("s1060"));
    }

    /**
	 * Reallocates the generated .jar file in the the grammar configuration
	 * folder.
	 */
    private void reallocateJarFile() {
        AcideProgressWindow.getInstance().setText(AcideLanguageManager.getInstance().getLabels().getString("s1061"));
        AcideByteFileManager.getInstance().reallocateFile(_grammarName + ".jar", "./configuration/grammars/" + _grammarName + ".jar");
        AcideProgressWindow.getInstance().setText(AcideLanguageManager.getInstance().getLabels().getString("s1062"));
    }

    /**
	 * Deletes the sub directories of a directory given as a parameter.
	 * 
	 * The method file.delete() only works for empty directories. This method
	 * deletes all the files in each sub directory from the original directory
	 * using recursion.
	 * 
	 * @param directory
	 *            directory to delete.
	 */
    private void deleteSubdirectories(File directory) {
        File[] files = directory.listFiles();
        for (int index = 0; index < files.length; index++) {
            if (files[index].isDirectory()) {
                deleteSubdirectories(files[index]);
            }
            files[index].delete();
        }
    }
}
