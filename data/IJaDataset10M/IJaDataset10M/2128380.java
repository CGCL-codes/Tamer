package vi.regex;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;

public class CmdLineRegexHighlighter extends AbstractRegexHighlighter {

    private List<String> lines;

    private int previousLineNumber = -1;

    private int previousGroupNumber = 1;

    private final String regex;

    private final String text;

    private final String replaceText;

    private final String replacement;

    public CmdLineRegexHighlighter(String regex, String text) {
        this.regex = regex;
        this.text = text;
        this.replaceText = "";
        this.replacement = "";
    }

    @Override
    protected void run() {
        parse();
        if (previousGroupNumber != 0) {
            System.out.println();
        }
    }

    @Override
    protected List<String> getTextInLines() {
        String[] linesArray = text.split(System.getProperty("line.separator"));
        lines = Arrays.asList(linesArray);
        return lines;
    }

    @Override
    protected String getRegex() {
        return regex;
    }

    @Override
    protected void highlight(int start, int end, int lineNumber, int groupNumber) {
        if (lineNumber != previousLineNumber || groupNumber == 0 || groupNumber <= previousGroupNumber) {
            System.out.print((previousLineNumber == -1 ? "" : "\n") + (lineNumber + 1) + ": ");
        }
        System.out.print(" \ngroup " + groupNumber + ":" + lines.get(lineNumber).substring(start, end));
        this.previousLineNumber = lineNumber;
        this.previousGroupNumber = groupNumber;
    }

    @Override
    protected String getReplaceText() {
        return replaceText;
    }

    @Override
    protected String getReplacementText() {
        return replacement;
    }

    @Override
    protected String getSplitText() {
        return text;
    }

    @Override
    protected int getRegexOptions() {
        return Pattern.MULTILINE;
    }
}
