package org.jmlspecs.jml4.compiler;

import java.util.Map;

public interface IBatchCompilerExtension {

    public final int ARG_NOT_HANDLED = -1;

    public int configureArgs(String currentArg, String[] args, int index, Map options);

    public boolean handleWarningToken(String token, boolean isEnabling, Map optionsMap);
}
