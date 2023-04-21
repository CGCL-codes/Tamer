package unclej.filepath;

import unclej.javatype.ClassName;
import unclej.javatype.TypeFactory;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Scott
 */
class ListClassVisitor extends RecursivePathVisitor {

    public ListClassVisitor(Wildcard wildcard) {
        this.wildcard = wildcard;
    }

    public boolean shouldRecurse(File dir) throws IOException {
        return wildcard.isRecursive();
    }

    public void visitEntry(Filelike fe) {
        String lower = fe.getName().toLowerCase();
        if (lower.endsWith(ClassName.CLASS_EXTENSION)) {
            String name = classFileToName(fe.getRelativeName(), ClassName.CLASS_EXTENSION);
            if (wildcard.matches(name)) {
                results.put(TypeFactory.getClass(name), fe);
            }
        } else if (lower.endsWith(ClassName.SOURCE_EXTENSION)) {
            String name = classFileToName(fe.getRelativeName(), ClassName.SOURCE_EXTENSION);
            if (wildcard.matches(name)) {
                results.put(TypeFactory.getClass(name), fe);
            }
        }
    }

    private String classFileToName(String filename, String extension) {
        int extLen = extension.length();
        int nameLen = filename.length();
        return filename.substring(0, nameLen - extLen).replace('/', '.').replace('\\', '.');
    }

    public boolean isDone() {
        return false;
    }

    public Map<ClassName, Filelike> getResults() {
        return Collections.unmodifiableMap(results);
    }

    private final Map<ClassName, Filelike> results = new HashMap<ClassName, Filelike>();

    private final Wildcard wildcard;
}
