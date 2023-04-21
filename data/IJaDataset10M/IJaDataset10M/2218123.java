package net.sf.poormans.model.domain.pojo;

import java.util.ArrayList;
import java.util.List;
import net.sf.poormans.configuration.InitializationManager;
import net.sf.poormans.model.IOrderable;
import net.sf.poormans.model.IRenderable;
import org.apache.commons.lang.StringUtils;

/**
 * Base object for a page.
 * {@link Gallery} is inherited from this object. 
 * 
 * @version $Id: Page.java 2099 2011-06-06 09:31:06Z th-schwarz $
 * @author <a href="mailto:th-schwarz@users.sourceforge.net">Thilo Schwarz</a>
 */
public class Page extends APoormansObject<Level> implements IRenderable, IOrderable<Page> {

    protected String name;

    protected String title;

    private String template;

    private List<Content> content = new ArrayList<Content>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String getTemplate() {
        return this.template;
    }

    public void setTemplate(String templateFile) {
        this.template = templateFile;
    }

    public List<Content> getContent() {
        return this.content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public void add(Content content) {
        this.content.add(content);
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String getDecorationString() {
        String deco = StringUtils.defaultIfEmpty(title, name);
        if (InitializationManager.isAdmin()) deco = String.format("%s#%d", deco, getId());
        return deco;
    }

    @Override
    public TemplateType getTemplateType() {
        return TemplateType.PAGE;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public List<Page> getFamily() {
        return getParent().getPages();
    }
}
