package org.objectwiz.plugin.customview.model;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.validation.constraints.NotNull;
import org.objectwiz.core.facet.customization.EntityBase;

/**
 * Template that can be used to create a custom display for a class.
 * It must respect the format defined by the {@link HtmlParser} concerning
 * the references to values of properties.
 *
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
@Entity
public class Template extends EntityBase {

    private String content;

    /**
     * Public no-args constructor
     */
    public Template() {
    }

    public Template(String content) {
        this.content = content;
    }

    /**
     * Contents of this template.
     */
    @Lob
    @NotNull
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
