package net.sf.webwarp.ui.trinidad.impl;

import net.sf.webwarp.ui.trinidad.Template;

public final class TemplateImpl implements Template {

    private String name;

    private String description;

    private String templatePath;

    private String[] slots;

    /**
     * @see net.sf.webwarp.ui.trinidad.Template#getName()
     */
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @see net.sf.webwarp.ui.trinidad.Template#getDescription()
     */
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @see net.sf.webwarp.ui.trinidad.Template#getTemplatePath()
     */
    public String getTemplatePath() {
        return templatePath;
    }

    public void setTemplatePath(String templatePath) {
        this.templatePath = templatePath;
    }

    /**
     * @see net.sf.webwarp.ui.trinidad.Template#getSlots()
     */
    public String[] getSlots() {
        if (slots == null) {
            return new String[0];
        }
        return slots.clone();
    }

    public void setSlots(String[] slots) {
        this.slots = slots;
    }
}
