package net.sf.buildbox.parser.model;

import javax.xml.bind.annotation.XmlAttribute;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Petr Kozelka
 */
public class ParsedBlock {

    private int startLine;

    private int endLine;

    private final List<ParsedIssue> issues = new ArrayList<ParsedIssue>();

    private final List<ParsedBlock> children = new ArrayList<ParsedBlock>();

    @XmlAttribute
    public int getStartLine() {
        return startLine;
    }

    public void setStartLine(int startLine) {
        this.startLine = startLine;
    }

    @XmlAttribute
    public int getEndLine() {
        return endLine;
    }

    public void setEndLine(int endLine) {
        this.endLine = endLine;
    }

    public void addIssue(ParsedIssue issue) {
        issues.add(issue);
    }

    public List<ParsedIssue> getIssues() {
        return issues;
    }

    public void addChildBlock(ParsedBlock childBlock) {
        children.add(childBlock);
    }
}
