package com.luzan.app.vist.service.bean;

import com.luzan.bean.MediaStream;
import com.luzan.bean.SemanticTag;
import com.luzan.common.httprpc.annotation.HttpHiddenField;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Collection;
import org.apache.commons.lang.StringUtils;

/**
 * Stream
 *
 * @author Alexander Bondar
 */
public class Stream {

    protected String source;

    protected String csvTags;

    public Stream() {
    }

    public Stream(MediaStream mediaStream) {
        final StringBuffer _tags = new StringBuffer();
        boolean first = true;
        source = mediaStream.getSource();
        for (SemanticTag tag : mediaStream.getTags()) {
            if (first) first = false; else _tags.append(", ");
            _tags.append(tag.getName());
        }
        csvTags = _tags.toString();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTags() {
        return csvTags;
    }

    public void setTags(String csvTags) {
        this.csvTags = csvTags;
    }

    @HttpHiddenField
    public MediaStream getMediaStream() {
        HashSet<SemanticTag> tags = new HashSet<SemanticTag>();
        for (String tag : Arrays.asList(csvTags.split(","))) if (!StringUtils.isEmpty(tag)) tags.add(new SemanticTag(tag.trim(), SemanticTag.TagType.OBJECT));
        return new MediaStream(source, tags);
    }

    @HttpHiddenField
    public Collection<SemanticTag> getTagList() {
        HashSet<SemanticTag> tags = new HashSet<SemanticTag>();
        for (String tag : Arrays.asList(csvTags.split(","))) if (!StringUtils.isEmpty(tag)) tags.add(new SemanticTag(tag.trim(), SemanticTag.TagType.OBJECT));
        return tags;
    }
}
