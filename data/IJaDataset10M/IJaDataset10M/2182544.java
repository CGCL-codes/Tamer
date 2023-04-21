package org.javajavalang.target.PHP;

import java.util.HashMap;
import org.antlr.stringtemplate.StringTemplate;
import org.javajavalang.GenerationObserver;
import org.javajavalang.RuleHandler;
import org.javajavalang.Target;

public class PhpTarget extends Target {

    public static final String FILE_EXTENSION = ".php";

    @Override
    public String getName() {
        return "PHP";
    }

    @Override
    public String getTargetFileExtension() {
        return FILE_EXTENSION;
    }

    @Override
    public void registerTargetHandlers(GenerationObserver observer) throws Exception {
    }
}
