package kz.simplex.photobox.action;

import kz.simplex.photobox.model.*;
import org.jboss.seam.annotations.Name;
import org.jboss.seam.framework.EntityHome;

@Name("tagHome")
public class TagHome extends EntityHome<Tag> {

    public void setTagId(Integer id) {
        setId(id);
    }

    public Integer getTagId() {
        return (Integer) getId();
    }

    @Override
    protected Tag createInstance() {
        Tag tag = new Tag();
        return tag;
    }

    public void load() {
        if (isIdDefined()) {
            wire();
        }
    }

    public void wire() {
        getInstance();
    }

    public boolean isWired() {
        return true;
    }

    public Tag getDefinedInstance() {
        return isIdDefined() ? getInstance() : null;
    }
}
