package org.identifylife.key.engine.core.model.media;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * @author dbarnier
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class VideoSource extends MediaSource {

    private Long duration;

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String toString() {
        return new ToStringBuilder(this).appendSuper(super.toString()).append("duration", getDuration()).toString();
    }
}
