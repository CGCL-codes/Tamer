package purej.web.tag.bean;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;

public class Section extends Content {

    /**
     * <code>serialVersionUID</code>�� ����
     */
    private static final long serialVersionUID = 369804740190156987L;

    protected final String name;

    public Section(String name, String content, String direct) {
        super(content, direct);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public void render(PageContext pageContext) throws JspException {
        if (content != null) {
            Region region = (Region) pageContext.findAttribute(content);
            if (region != null) {
                System.out.println(toString());
                RegionStack.push(pageContext, region);
                region.render(pageContext);
                RegionStack.pop(pageContext);
            } else {
                if (isDirect()) {
                    try {
                        System.out.println(toString());
                        pageContext.getOut().print(content.toString());
                    } catch (java.io.IOException ex) {
                        throw new JspException(ex.getMessage());
                    }
                } else {
                    try {
                        System.out.println(toString());
                        pageContext.include(content.toString());
                    } catch (Exception ex) {
                        throw new JspException(ex.getMessage());
                    }
                }
            }
        }
    }

    @Override
    public String toString() {
        return "Section : " + name + ", content= " + content.toString();
    }
}
