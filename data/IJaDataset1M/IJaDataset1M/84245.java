package org.myjerry.voyage.model;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
import com.google.appengine.api.datastore.Text;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Template {

    @PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long templateID;

    @Persistent
    private String name;

    @Persistent
    private Text contents;

    public void update(Template template) {
        if (template == null) {
            return;
        }
        this.name = template.name;
        this.contents = template.contents;
    }

    /**
	 * @return the templateID
	 */
    public Long getTemplateID() {
        return templateID;
    }

    /**
	 * @param templateID the templateID to set
	 */
    public void setTemplateID(Long templateID) {
        this.templateID = templateID;
    }

    /**
	 * @return the name
	 */
    public String getName() {
        return name;
    }

    /**
	 * @param name the name to set
	 */
    public void setName(String name) {
        this.name = name;
    }

    /**
	 * @return the contents
	 */
    public String getContents() {
        if (this.contents != null) {
            return this.contents.getValue();
        }
        return null;
    }

    /**
	 * @param contents the contents to set
	 */
    public void setContents(String contents) {
        this.contents = new Text(contents);
    }
}
