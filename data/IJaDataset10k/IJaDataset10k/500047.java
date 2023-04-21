package org.blackdog.type;

import java.beans.PropertyVetoException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.siberia.type.SibURL;
import org.siberia.type.annotation.bean.Bean;
import org.siberia.type.annotation.bean.BeanProperty;

/**
 *
 * Default implementation of a song
 *
 * @author alexis
 */
@Bean(name = "default song item", internationalizationRef = "org.blackdog.rc.i18n.type.DefaultSongItem", expert = false, hidden = true, preferred = true, propertiesClassLimit = Object.class, methodsClassLimit = Object.class)
public class DefaultSongItem extends AbstractSongItem {

    /** Creates a new instance of DefaultSongItem */
    public DefaultSongItem() {
    }
}
