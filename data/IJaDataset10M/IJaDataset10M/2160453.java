package com.blommesteijn.uva.sc.saf.runner.model;

import java.util.List;
import com.blommesteijn.uva.sc.saf.ast.types.IAstNode;
import com.blommesteijn.uva.sc.saf.runner.model.ast.AstLoader;
import com.blommesteijn.uva.sc.saf.runner.model.game.Draw;
import com.blommesteijn.uva.sc.saf.runner.model.game.GameException;
import com.blommesteijn.uva.sc.saf.runner.model.interpreter.IInterpreter;
import com.blommesteijn.uva.sc.saf.runner.model.interpreter.SuperAwesomeGameInterpreter;
import com.blommesteijn.uva.sc.saf.runner.model.utils.Common;
import com.blommesteijn.uva.sc.saf.runner.model.utils.Files;
import com.blommesteijn.uva.sc.saf.runner.model.utils.Options;
import com.blommesteijn.uva.sc.saf.runner.view.CliMessenger;
import com.blommesteijn.uva.sc.saf.runner.view.ExitCode;
import com.blommesteijn.uva.sc.saf.runner.view.GuiLoader;

/**
 * MVC - Model
 * @author dblommesteijn
 * @since 12 Feb, 2012
 */
public class Model {

    private static Model _instance = null;

    /**
	 * Get Singleton instance of this object
	 * @return instance of this object
	 */
    public static Model getInstance() {
        if (_instance == null) _instance = new Model();
        return _instance;
    }

    private Options _options = null;

    private Files _files = null;

    private AstLoader _astLoader = null;

    private IInterpreter _interpreter = null;

    /**
	 * Constructor
	 */
    private Model() {
    }

    /**
	 * Load model properties
	 * @param appName application name
	 * @param appVersion application version
	 * @param args application arguments
	 */
    public void load(String appName, String appVersion, String[] args) {
        this.loadSources(appName, appVersion, args);
        this.loadFiles();
        this.loadAst();
        this.loadInterpreter();
    }

    private void loadSources(String appName, String appVersion, String[] args) {
        try {
            _options = new Options(appName, appVersion, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
        CliMessenger.msg(new String[] { "arguments:", _options.toString() });
        if (!_options.hasOption(Options.SOURCE) || _options.getOption(Options.SOURCE).size() <= 0) CliMessenger.exit(ExitCode.EC_SOURCES, "no source file(s)");
    }

    private void loadFiles() {
        try {
            List<String> option = _options.getOption(Options.SOURCE);
            _files = new Files(Common.USER_DIR, option);
        } catch (FileLoadException e) {
            CliMessenger.exit(ExitCode.EC_FILES, e.getMessage());
        }
        if (_files == null) CliMessenger.exit(ExitCode.EC_FILES, "no source file(s) could be loaded");
        CliMessenger.msg(new String[] { "current dir:", _files.toString() });
    }

    private void loadAst() {
        try {
            _astLoader = new AstLoader(_files);
        } catch (FileLoadException e) {
            CliMessenger.exit(ExitCode.EC_AST, e.getMessage());
        }
        if (_astLoader.getAstNodes().size() < _files.getFiles().size()) CliMessenger.exit(ExitCode.EC_AST, "not all files contain valid ast(s)");
    }

    private void loadInterpreter() {
        try {
            _interpreter = new SuperAwesomeGameInterpreter(_astLoader.getAstNodes(), new Draw(new GuiLoader()));
        } catch (GameException e) {
            CliMessenger.exit(ExitCode.EC_ERROR, e.getMessage());
        }
        _interpreter.start();
    }

    public Options getOption() {
        return _options;
    }

    public boolean isDebug() {
        return _options.hasOption(Options.DEBUG);
    }
}
