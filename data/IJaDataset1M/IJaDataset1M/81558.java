package confGenerator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author pacman
 */
public class TemplateParser {

    private File templateFile;

    private StringBuilder contentBuilder;

    private String content;

    public TemplateParser(String templateFileAddress) throws Exception {
        init(new File(templateFileAddress));
    }

    public TemplateParser(File templateFile) throws Exception {
        init(templateFile);
    }

    private void init(File templateFile) throws Exception {
        this.templateFile = templateFile;
        this.content = readContent();
    }

    private String readContent() throws Exception {
        if (!templateFile.exists()) {
            throw new Exception("Template file indicated does not exist!");
        }
        if (!templateFile.canRead()) {
            throw new Exception("Template file indicated can not be read!");
        }
        Scanner scanner = new Scanner(templateFile);
        contentBuilder = new StringBuilder();
        String newLine = System.getProperty("line.separator");
        try {
            while (scanner.hasNextLine()) {
                contentBuilder.append(scanner.nextLine()).append(newLine);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            scanner.close();
        }
        return contentBuilder.toString();
    }

    public String getContent() {
        return content;
    }

    public List<String> getPatterns() {
        List<String> matches = new ArrayList<String>();
        Pattern pattern = Pattern.compile("%.*%");
        Matcher matcher = pattern.matcher(content);
        while (matcher.find()) {
            matches.add(matcher.group());
        }
        return matches;
    }
}
