package hu.rtemplate;

public class TagMatch {

    private RTemplateTagType tagType;

    private String content;

    public String getContent() {
        return content;
    }

    public TagMatch(RTemplateTagType tagType, String content) {
        super();
        this.tagType = tagType;
        this.content = content;
    }

    public int getLength() {
        return tagType.getTemplatePre().length() + content.length() + tagType.getTemplatePost().length();
    }
}
