package net.beeger.osmorc.manifest.lang;

import com.intellij.lang.Language;
import com.intellij.lang.ParserDefinition;
import com.intellij.lang.annotation.Annotator;
import com.intellij.openapi.fileTypes.SyntaxHighlighter;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.components.ServiceManager;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import net.beeger.osmorc.manifest.lang.headerparser.HeaderAnnotator;

public class ManifestLanguage extends Language {

    public ManifestLanguage() {
        super("Manifest");
    }

    @NotNull
    public SyntaxHighlighter getSyntaxHighlighter(Project project, VirtualFile virtualFile) {
        return new ManifestSyntaxHighlighter();
    }

    @Nullable
    public ParserDefinition getParserDefinition() {
        if (_parserDefinition == null) {
            _parserDefinition = new ManifestParserDefinition();
        }
        return _parserDefinition;
    }

    @Nullable
    public Annotator getAnnotator() {
        return ServiceManager.getService(HeaderAnnotator.class);
    }

    private ManifestParserDefinition _parserDefinition;
}
