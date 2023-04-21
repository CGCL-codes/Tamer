package com.bazaaroid.server.gae.core.dto.partner;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import com.bazaaroid.server.gae.core.persistence.model.partner.Tag;

@XmlRootElement(name = "Tag")
public class TagDTO {

    private Tag tag = null;

    public TagDTO() {
    }

    public TagDTO(Tag tag) {
        this.tag = tag;
    }

    @XmlElement
    public String getKey() {
        return tag.getKey().toString();
    }

    @XmlElement
    public String getName() {
        return tag.getName();
    }
}
