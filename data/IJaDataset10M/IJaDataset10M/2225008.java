package bagotricks.tuga;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.math.BigInteger;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Program {

    private static final Pattern NAME_PATTERN = Pattern.compile("(.+?)(?: (\\d+))?");

    /**
	 * A quick hack for sorting while understanding trailing numbers. Better
	 * would be a more general solution as has been implemented by some people.
	 */
    public static int compareNames(String name1, String name2) {
        Matcher matcher1 = nameMatcher(name1);
        Matcher matcher2 = nameMatcher(name2);
        if (matcher1.group(1).equals(matcher2.group(1))) {
            String number1 = matcher1.group(2);
            String number2 = matcher2.group(2);
            if (number1 != null && number2 != null) {
                return new BigInteger(number1).compareTo(new BigInteger(number2));
            }
        }
        return name1.compareTo(name2);
    }

    private static Matcher nameMatcher(String name) {
        Matcher matcher = NAME_PATTERN.matcher(name);
        if (!matcher.matches()) {
            throw new RuntimeException("doesn't match???: " + name);
        }
        return matcher;
    }

    public String content;

    public File file;

    public String group;

    public String id;

    public Library library;

    public String name;

    public Program copy() {
        return null;
    }

    public void endAction() {
    }

    public void insertText(int index, String text) {
        System.out.println("Inserted: " + index + ", " + text);
        writeFile();
    }

    private void pickSimilarName(Map<String, String> programs, String wantedName) {
        if (programs.containsKey(wantedName)) {
            Matcher matcher = nameMatcher(wantedName);
            String mainName = matcher.group(1);
            String numberString = matcher.group(2);
            BigInteger number = numberString != null ? new BigInteger(numberString) : BigInteger.ONE;
            do {
                number = number.add(BigInteger.ONE);
                wantedName = mainName + " " + number;
            } while (programs.containsKey(wantedName));
        }
        this.name = wantedName;
    }

    public void removeText(int begin, int end) {
        System.out.println("Removed: " + begin + ", " + end);
        writeFile();
    }

    /**
	 * Renames the program. If another program in this group already has the
	 * wantedName, it is given a number to make it unique.
	 */
    public void renameTo(String wantedName) {
        Map<String, String> programs = library.getGroupPrograms(group);
        pickSimilarName(programs, wantedName);
        library.setProgramName(id, name);
    }

    public void setContent(String content) {
        this.content = content;
        writeFile();
    }

    public void setGroup(String group) {
        if (library.getProgramByNameAndGroup(group, name) != null) {
            pickSimilarName(library.getGroupPrograms(group), name);
            library.setProgramName(id, name);
        }
        this.group = group;
        library.updateGroup(this);
    }

    public String toString() {
        return name;
    }

    private void writeFile() {
        try {
            Writer writer = new OutputStreamWriter(new FileOutputStream(file), Library.CHARSET);
            try {
                writer.write(content);
            } finally {
                writer.close();
            }
        } catch (Exception e) {
            Thrower.throwAny(e);
        }
    }
}
