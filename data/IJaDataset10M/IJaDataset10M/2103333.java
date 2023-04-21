package junitconverter;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import junitconverter.testcase.TestCaseClass;
import junitconverter.testcase.TestCaseMethod;

public class SimpleCoderEditor implements CodeEditor {

    private static final String PACKAGE_PREFIX = "package ";

    private static final String IMPORT_PREFIX = "import ";

    private final ClassWriter classWriter;

    public SimpleCoderEditor(ClassWriter classWriter) {
        this.classWriter = classWriter;
    }

    public void annotateMethod(TestCaseClass testCaseClass, TestCaseMethod method, Class<? extends Annotation> annotation) {
        if (method.hasAnnotation(annotation)) {
            return;
        }
        String methodLine = testCaseClass.getLines().get(method.getLine());
        String indent = extractIndent(methodLine);
        classWriter.insertLine(method.getLine(), indent + "@" + annotation.getSimpleName());
    }

    public void importClass(TestCaseClass testCaseClass, Class<?> klass) {
        classWriter.insertLine(indexForInsertingImports(testCaseClass.getLines()), IMPORT_PREFIX + klass.getName() + ";");
    }

    public void changeVisiblity(TestCaseClass testCaseClass, TestCaseMethod method, Visibility visibility) {
        String line = testCaseClass.getLines().get(method.getLine());
        Visibility oldVisibility = extractVisibility(line);
        classWriter.replaceLine(method.getLine(), line.replaceFirst(oldVisibility.toString(), visibility.toString()));
    }

    public void importStaticClass(TestCaseClass testCaseClass, Class<?> klass) {
        classWriter.insertLine(indexForInsertingImports(testCaseClass.getLines()), IMPORT_PREFIX + "static " + klass.getName() + ".*;");
    }

    public void removeSuper(TestCaseClass testCaseClass) {
        if (hasExtendsClause(testCaseClass)) {
            removeExtendsClause(testCaseClass);
        }
        removeSuperCtorInvocations(testCaseClass);
        removeSuperMethodInvocations(testCaseClass);
        removeOverrideAnnotations(testCaseClass);
    }

    private int indexForInsertingImports(List<String> lines) {
        int index = findFirstImport(lines);
        if (index != -1) {
            return index;
        }
        index = findPackageLine(lines);
        if (index != -1) {
            return index;
        }
        return 0;
    }

    private int findPackageLine(List<String> lines) {
        int line = findFirstLineThatStartsWith(PACKAGE_PREFIX, lines);
        if (line != -1) {
            return line + 1;
        }
        return -1;
    }

    private int findFirstImport(List<String> lines) {
        return findFirstLineThatStartsWith(IMPORT_PREFIX, lines);
    }

    private int findFirstLineThatStartsWith(String prefix, List<String> lines) {
        for (int i = 0; i < lines.size(); i++) {
            if (lines.get(i).startsWith(prefix)) {
                return i;
            }
        }
        return -1;
    }

    private String extractIndent(String line) {
        StringBuilder indent = new StringBuilder();
        for (int i = 0; i < line.length(); i++) {
            if (line.charAt(i) == ' ' || line.charAt(i) == '\t') {
                indent.append(line.charAt(i));
            } else {
                break;
            }
        }
        return indent.toString();
    }

    /**
	 * Used to find the current visibility of a method.
	 * We search for the first occurrence of a visibility in the line and return
	 * it (in case someone decides to name his method 'privateTest' or 
	 * whatever).
	 * 
	 * @param line The line to get the visibility of.
	 * @return The visibility in the line.
	 */
    private Visibility extractVisibility(String line) {
        Pattern pattern = Pattern.compile(visibilityPattern());
        Matcher matcher = pattern.matcher(line);
        matcher.find();
        String stringVisibility = line.substring(matcher.start(), matcher.end());
        for (Visibility visibility : Visibility.values()) {
            if (stringVisibility.equals(visibility.toString())) {
                return visibility;
            }
        }
        throw new IllegalArgumentException("Couldn't find a visibility");
    }

    private String visibilityPattern() {
        StringBuilder patternBuilder = new StringBuilder();
        for (Visibility visibility : Visibility.values()) {
            patternBuilder.append(visibility);
            patternBuilder.append('|');
        }
        patternBuilder.setLength(patternBuilder.length() - 1);
        return patternBuilder.toString();
    }

    private void removeExtendsClause(TestCaseClass testCaseClass) {
        String definitionWithExtends = testCaseClass.getLines().get(testCaseClass.getExtendsLine());
        String definitionWithoutExtends = definitionWithExtends.replaceFirst("\\s*extends\\s+" + Pattern.quote(testCaseClass.getSuperName()), "");
        classWriter.replaceLine(testCaseClass.getExtendsLine(), definitionWithoutExtends);
    }

    private boolean hasExtendsClause(TestCaseClass testCaseClass) {
        return testCaseClass.getSuperName() != null;
    }

    private void removeSuperMethodInvocations(TestCaseClass testCaseClass) {
        commentOutLines(testCaseClass, testCaseClass.getSuperMethodInvocations());
    }

    private void removeSuperCtorInvocations(TestCaseClass testCaseClass) {
        commentOutLines(testCaseClass, testCaseClass.getSuperConstructorInvocations());
    }

    private void removeOverrideAnnotations(TestCaseClass testCaseClass) {
        commentOutLines(testCaseClass, testCaseClass.getOverrideAnnotationsLines());
    }

    private void commentOutLines(TestCaseClass testCaseClass, List<Integer> lines) {
        for (int line : lines) {
            classWriter.replaceLine(line, "//" + testCaseClass.getLines().get(line));
        }
    }
}
