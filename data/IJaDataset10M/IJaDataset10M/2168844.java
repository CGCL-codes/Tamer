package org.springframework.richclient.core.services;

import org.springframework.richclient.core.services.swing.LabelInfo;

/**
 * An object that can be labeled; where a label consists of text and mnemonic.
 * 
 * @author Keith Donald
 */
public interface LabelConfigurable {

    public void setLabelInfo(LabelInfo label);
}
