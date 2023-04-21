package ac.jp.u_tokyo.SyncLib.language2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.antlr.runtime.ANTLRFileStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import ac.jp.u_tokyo.SyncLib.interactive.Console;
import ac.jp.u_tokyo.SyncLib.language.EvaluationFailedException;
import ac.jp.u_tokyo.SyncLib.util.StringHelper;

public class Compiler {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        String inputFile = args[0];
        String outputClass = args[1];
        try {
            ANTLRFileStream input = new ANTLRFileStream(new File(inputFile).getAbsolutePath());
            LanguageLexer lexer = new LanguageLexer(input);
            lexer.setBasePath(new File(inputFile).getAbsoluteFile().getParent());
            final CommonTokenStream tokenStream = new CommonTokenStream(lexer);
            LanguageParser parser = new LanguageParser(tokenStream);
            HigherProgram p = parser.prog();
            FileWriter fileWriter = new FileWriter(outputClass + ".java");
            try {
                fileWriter.append(String.format(Header, "", outputClass, outputClass));
                p.compile().write(fileWriter);
                fileWriter.append(Footer);
            } finally {
                fileWriter.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (RecognitionException e) {
            e.printStackTrace();
        } catch (EvaluationFailedException e) {
            e.printStackTrace();
        }
    }

    static final String Header = "%1s" + "import ac.jp.u_tokyo.SyncLib.language2.Console;" + "import java.io.IOException;" + "public class %2s extends Console {" + "public static void main(String[] args) throws IOException\r\n" + "	{\r\n" + "		Console.interactiveExec(%3s.class);\r\n" + "	}";

    static final String Footer = "}";
}
