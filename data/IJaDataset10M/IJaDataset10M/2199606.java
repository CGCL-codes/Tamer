package org.deved.antlride.integration.jdt;

import java.util.Map;
import org.deved.antlride.core.AntlrCore;
import org.deved.antlride.core.integration.IAntlrTargetLanguage;
import org.eclipse.core.runtime.IPath;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.ToolFactory;
import org.eclipse.jdt.core.formatter.CodeFormatter;
import org.eclipse.jdt.core.formatter.DefaultCodeFormatterConstants;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.Document;
import org.eclipse.jface.text.IDocument;
import org.eclipse.text.edits.MalformedTreeException;
import org.eclipse.text.edits.TextEdit;

public class AntlrJavaLanguage implements IAntlrTargetLanguage {

    public String format(String source) {
        try {
            Map options = DefaultCodeFormatterConstants.getEclipseDefaultSettings();
            options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_5);
            options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_5);
            options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_5);
            final CodeFormatter codeFormatter = ToolFactory.createCodeFormatter(options);
            final TextEdit edit = codeFormatter.format(CodeFormatter.K_COMPILATION_UNIT, source, 0, source.length(), 0, System.getProperty("line.separator"));
            IDocument document = new Document(source);
            edit.apply(document);
            return document.get();
        } catch (MalformedTreeException ex) {
            AntlrCore.error(ex);
        } catch (BadLocationException ex) {
            AntlrCore.error(ex);
        }
        return source;
    }

    public void buildResources(ISourceModule sourceModule1, IPath resource) {
    }

    public String getLanguage() {
        return "Java";
    }
}
