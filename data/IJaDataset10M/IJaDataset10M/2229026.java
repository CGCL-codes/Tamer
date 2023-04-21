package as.ide.core.parser;

import java.io.File;
import java.util.HashMap;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IncrementalProjectBuilder;
import org.eclipse.core.runtime.CoreException;
import as.ide.core.common.ASProjectConfiguration;
import as.ide.core.common.ConfigurationManager;
import as.ide.core.common.SyntaxTree;

public class ParserFactory {

    /** parser map is per file */
    private static final HashMap<IFile, ASParser> paserMap = new HashMap<IFile, ASParser>();

    /** syntax tree per project */
    private static final HashMap<IProject, SyntaxTree> syntaxTreeMap = new HashMap<IProject, SyntaxTree>();

    private ParserFactory() {
    }

    /**
	 * Only those files who are under editing require their ASParser instances maintained
	 * @param ifile the IFile
	 * @return the ASParser, won't be null
	 */
    public static ASParser getParser(IFile ifile) {
        ASParser parser = paserMap.get(ifile);
        if (parser == null) {
            IProject project = ifile.getProject();
            ASProjectConfiguration cfg = ConfigurationManager.getASConfiguration(project);
            parser = new ASParser(ifile, cfg);
            paserMap.put(ifile, parser);
        }
        return parser;
    }

    /**
	 * Get a temporary parser, the parser instance will not be maintained in this factory class.
	 * <br>
	 * Those libraries and linked sources should use such parser to get the information
	 * @param ifile
	 * @return
	 */
    public static ASParser getTempParser(File file) {
        return new ASParser(file);
    }

    public static FcshBuilder getBuilder() {
        return FcshBuilder.getInstance();
    }

    public static SyntaxTree getSyntaxTree(IFile ifile) {
        assert (ifile != null);
        IProject project = ifile.getProject();
        return getSyntaxTree(project);
    }

    public static SyntaxTree getSyntaxTree(IProject project) {
        SyntaxTree st = syntaxTreeMap.get(project);
        if (st == null) {
            ASProjectConfiguration cfg = ConfigurationManager.getASConfiguration(project);
            st = new SyntaxTree(cfg);
            syntaxTreeMap.put(project, st);
            try {
                project.build(IncrementalProjectBuilder.FULL_BUILD, null);
            } catch (CoreException e) {
                e.printStackTrace();
            }
        }
        return st;
    }

    public static void remove(IFile ifile) {
        ASParser parser = paserMap.remove(ifile);
        if (parser != null) {
            parser.distroy();
        }
    }
}
