package com.ibm.wala.cast.java.translator.polyglot;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import polyglot.frontend.Compiler;
import polyglot.frontend.ExtensionInfo;
import polyglot.frontend.Globals;
import polyglot.frontend.Source;
import polyglot.main.Options;
import polyglot.main.UsageError;
import polyglot.util.ErrorInfo;
import polyglot.util.Position;
import com.ibm.wala.cast.java.translator.SourceModuleTranslator;
import com.ibm.wala.classLoader.DirectoryTreeModule;
import com.ibm.wala.classLoader.FileModule;
import com.ibm.wala.classLoader.JarFileModule;
import com.ibm.wala.classLoader.Module;
import com.ibm.wala.classLoader.ModuleEntry;
import com.ibm.wala.classLoader.SourceFileModule;
import com.ibm.wala.ipa.callgraph.AnalysisScope;
import com.ibm.wala.types.ClassLoaderReference;
import com.ibm.wala.util.debug.Assertions;

/**
 * A SourceModuleTranslator whose implementation of loadAllSources() uses a suitably
 * configured Polyglot-based compiler to generate WALA IR for the sources in the
 * compile-time classpath.
 * @author rfuhrer
 */
public class PolyglotSourceModuleTranslator implements SourceModuleTranslator {

    private final ExtensionInfo fExtInfo;

    /**
   * A client-supplied ClassLoaderReference to identify the innermost class loader
   * to use when populating the classpath with loader Modules.
   */
    private final ClassLoaderReference fSearchPathStart;

    protected String fClassPath;

    protected String fSourcePath;

    public PolyglotSourceModuleTranslator(AnalysisScope scope, IRTranslatorExtension extInfo, PolyglotSourceLoaderImpl sourceLoader, ClassLoaderReference searchPathStart) {
        fSearchPathStart = searchPathStart;
        fExtInfo = (ExtensionInfo) extInfo;
        computeClassPath(scope);
        computeSourcePath(scope);
        extInfo.setSourceLoader(sourceLoader);
    }

    protected void computeClassPath(AnalysisScope scope) {
        StringBuilder sb = new StringBuilder();
        ClassLoaderReference cl = fSearchPathStart;
        while (cl != null) {
            addModulesForLoader(scope, cl, sb);
            cl = cl.getParent();
        }
        fClassPath = sb.toString();
    }

    protected void addModulesForLoader(AnalysisScope scope, ClassLoaderReference cl, StringBuilder sb) {
        for (Module m : scope.getModules(cl)) {
            if (sb.length() > 0) sb.append(File.pathSeparator);
            if (m instanceof JarFileModule) {
                JarFileModule jarFileModule = (JarFileModule) m;
                sb.append(jarFileModule.getAbsolutePath());
            } else if (m instanceof DirectoryTreeModule) {
                DirectoryTreeModule directoryTreeModule = (DirectoryTreeModule) m;
                sb.append(directoryTreeModule.getPath());
            } else if (m instanceof FileModule) {
            } else Assertions.UNREACHABLE("Module entry is neither jar file nor directory");
        }
    }

    protected void computeSourcePath(AnalysisScope scope) {
        fSourcePath = ".";
    }

    public void loadAllSources(Set<ModuleEntry> modules) {
        Options opts = fExtInfo.getOptions();
        opts.assertions = true;
        try {
            opts.parseCommandLine(new String[] { "-noserial", "-cp", fClassPath, "-sourcepath", fSourcePath }, new HashSet<String>());
        } catch (UsageError e) {
        }
        Compiler compiler = new Compiler(fExtInfo);
        List<Source> streams = new ArrayList<Source>();
        Globals.initialize(compiler);
        for (Iterator<ModuleEntry> it = modules.iterator(); it.hasNext(); ) {
            SourceFileModule entry = (SourceFileModule) it.next();
            Assertions.productionAssertion(entry.isSourceFile());
            if (skipSourceFile(entry)) {
                continue;
            }
            try {
                ModuleSource srcStream = new ModuleSource(entry);
                streams.add(srcStream);
            } catch (IOException e) {
                compiler.errorQueue().enqueue(new ErrorInfo(ErrorInfo.IO_ERROR, "Unable to open source file '" + entry.getName() + "'", Position.COMPILER_GENERATED));
            }
        }
        compiler.compile(streams);
    }

    /**
   * @return true if the given source file module should not be processed,
   * e.g. because it is generated on behalf of some upstream source.
   */
    protected boolean skipSourceFile(SourceFileModule entry) {
        return false;
    }
}
