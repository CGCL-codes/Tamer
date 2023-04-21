package org.antlride.internal.support.antlr;

import java.io.IOException;
import org.antlride.runtime.EmbeddedRuntimeDependency;
import org.antlride.support.antlr.AntlrPlugin;
import org.eclipse.core.runtime.Path;

public class StringTemplateRuntimeDependency extends EmbeddedRuntimeDependency {

    public StringTemplateRuntimeDependency() throws IOException {
        super(AntlrPlugin.getDefault(), Path.fromOSString("lib/stringtemplate-3.2.1.jar"));
    }
}
