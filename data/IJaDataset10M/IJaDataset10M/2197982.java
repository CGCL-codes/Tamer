package org.blackdog.type;

import org.apache.log4j.Logger;
import org.siberia.type.annotation.bean.Bean;

/**
 *
 * MP3 implementation of a song
 *
 * @author alexis
 */
@Bean(name = "Speex song item", internationalizationRef = "org.blackdog.rc.i18n.type.SpeexSongItem", expert = false, hidden = true, preferred = true, propertiesClassLimit = Object.class, methodsClassLimit = Object.class)
public class SpeexSongItem extends AbstractTaggedSongItem {

    /** logger */
    private transient Logger logger = Logger.getLogger(SpeexSongItem.class.getName());

    /** Creates a new instance of SpeexSongItem */
    public SpeexSongItem() {
    }
}
