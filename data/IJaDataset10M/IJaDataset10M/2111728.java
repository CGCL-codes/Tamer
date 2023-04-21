package net.beeger.osmorc.manifest.lang.headerparser;

import com.intellij.lang.annotation.AnnotationHolder;
import com.intellij.openapi.util.TextRange;
import net.beeger.osmorc.manifest.lang.psi.ManifestHeaderValue;
import net.beeger.osmorc.manifest.lang.psi.TestableManifestHeaderValue;
import net.beeger.osmorc.valueobject.Version;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class BundleVersionParser extends AbstractHeaderParserImpl {

    public void annotate(@NotNull ManifestHeaderValue headerValue, @NotNull AnnotationHolder holder) {
        parseVersion(headerValue, holder);
    }

    public Object getValue(@NotNull ManifestHeaderValue headerValue) {
        return parseVersion(headerValue, null);
    }

    protected Version parseVersion(@NotNull TestableManifestHeaderValue headerValue, @Nullable AnnotationHolder annotationHolder) {
        String[] componentNames = new String[] { "major", "minor", "micro" };
        int[] components = new int[] { 0, 0, 0 };
        int end = -1;
        String versionString = headerValue.getValueText();
        for (int componentIdx = 0; componentIdx < components.length; componentIdx++) {
            int start = end + 1;
            end = versionString.indexOf('.', start);
            if (end < 0) {
                end = versionString.length();
            }
            try {
                components[componentIdx] = Integer.parseInt(versionString.substring(start, end));
            } catch (NumberFormatException e) {
                createUnvalidNumberAnnotation(headerValue, start, end, annotationHolder, componentNames[componentIdx]);
            }
            if (end == versionString.length()) {
                break;
            }
        }
        String qualifier = "";
        if (end < versionString.length()) {
            qualifier = versionString.substring(end + 1);
            if (annotationHolder != null) {
            }
        }
        return new Version(components[0], components[1], components[2], qualifier);
    }

    protected void createUnvalidNumberAnnotation(@NotNull TestableManifestHeaderValue headerValue, int start, int end, @Nullable AnnotationHolder annotationHolder, String component) {
        if (annotationHolder != null) {
            TextRange headerValueTextRange = headerValue.getTextRange();
            TextRange textRange = new TextRange(headerValueTextRange.getStartOffset() + start, headerValueTextRange.getStartOffset() + end);
            annotationHolder.createErrorAnnotation(textRange, "The " + component + " component of the defined version is not a valid number");
        }
    }
}
