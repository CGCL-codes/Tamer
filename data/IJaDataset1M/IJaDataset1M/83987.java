package org.linkedgeodata.osm.mapping.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.Logger;
import org.linkedgeodata.osm.mapping.IOneOneTagMapper;
import org.linkedgeodata.util.ExceptionUtil;
import org.openstreetmap.osmosis.core.domain.v0_6.Tag;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

public class RegexTextTagMapper implements IOneOneTagMapper {

    private static Set<String> knownLangTags = new HashSet<String>(Arrays.asList("en", "de", "fr", "pl", "ja", "it", "nl", "pt", "es", "ru", "sv", "zh", "no", "fi", "ca", "uk", "tr", "cs", "hu", "ro", "vo", "eo", "da", "sk", "id", "ar", "ko", "he", "lt", "vi", "sl", "sr", "bg", "et", "fa", "hr", "simple", "new", "ht", "nn", "gl", "th", "te", "el", "ms", "eu", "ceb", "mk", "hi", "ka", "la", "bs", "lb", "br", "is", "bpy", "mr", "sq", "cy", "az", "sh", "tl", "lv", "pms", "bn", "be_x_old", "jv", "ta", "oc", "io", "be", "an", "su", "nds", "scn", "nap", "ku", "ast", "af", "fy", "sw", "wa", "zh_yue", "bat_smg", "qu", "ur", "cv", "ksh"));

    public Set<String> getKnownLanguageTags() {
        return Collections.unmodifiableSet(knownLangTags);
    }

    private static Logger logger = Logger.getLogger(RegexTextTagMapper.class);

    private Pattern keyPattern;

    private String property;

    private boolean isOSMEntity;

    public String getProperty() {
        return property;
    }

    public boolean describesOSMEntity() {
        return isOSMEntity;
    }

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    /**
	 * Example pattern:
	 * name:([^:]+)
	 * 
	 */
    public RegexTextTagMapper(String property, Pattern keyPattern, boolean isOSMEntity) {
        this.property = property;
        this.keyPattern = keyPattern;
        this.isOSMEntity = isOSMEntity;
    }

    public Pattern getKeyPattern() {
        return keyPattern;
    }

    @Override
    public Model map(String subject, Tag tag, Model model) {
        if (model == null) model = ModelFactory.createDefaultModel();
        try {
            model = _map(subject, tag, model);
        } catch (Exception e) {
            logger.error(ExceptionUtil.toString(e));
        }
        return model;
    }

    public String matchLangTag(Tag tag) {
        return matchLangTag(tag.getKey());
    }

    public String matchLangTag(String key) {
        Matcher matcher = keyPattern.matcher(key);
        if (matcher.matches()) {
            if (matcher.groupCount() != 1) {
                String msg = "Key [" + key + "] matched the pattern [" + keyPattern + "] but no language tag could be extracted.";
                throw new RuntimeException(msg);
            } else {
                String langTag = matcher.group(1);
                if (knownLangTags.contains(langTag)) {
                    return langTag;
                }
            }
        }
        return null;
    }

    public boolean matches(Tag tag) {
        return matchLangTag(tag) != null;
    }

    private Model _map(String subject, Tag tag, Model model) throws Exception {
        String langTag = matchLangTag(tag);
        if (langTag != null) {
            model.add(model.getResource(subject), model.getProperty(property), tag.getValue(), langTag);
        }
        return model;
    }

    @Override
    public <T> T accept(IOneOneTagMapperVisitor<T> visitor) {
        return visitor.accept(this);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (isOSMEntity ? 1231 : 1237);
        result = prime * result + ((keyPattern == null) ? 0 : keyPattern.hashCode());
        result = prime * result + ((property == null) ? 0 : property.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (!(obj instanceof RegexTextTagMapper)) return false;
        RegexTextTagMapper other = (RegexTextTagMapper) obj;
        if (isOSMEntity != other.isOSMEntity) return false;
        if (keyPattern == null) {
            if (other.keyPattern != null) return false;
        } else if (!keyPattern.equals(other.keyPattern)) return false;
        if (property == null) {
            if (other.property != null) return false;
        } else if (!property.equals(other.property)) return false;
        return true;
    }
}
