package org.identifylife.character.store.oxm.jaxb;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import org.identifylife.character.store.model.VocabEnum;

/**
 * @author dbarnier
 *
 */
public class VocabEnumAdapter extends XmlAdapter<VocabEnum, VocabEnum> {

    @Override
    public VocabEnum marshal(VocabEnum vocab) throws Exception {
        if (VocabEnum.UNKNOWN.equals(vocab)) {
            return null;
        }
        return vocab;
    }

    @Override
    public VocabEnum unmarshal(VocabEnum status) throws Exception {
        return status;
    }
}
