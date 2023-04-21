package org.ozoneDB.xml.dom;

import org.w3c.dom.*;
import org.ozoneDB.OzoneRemote;

public interface TextProxy extends OzoneRemote, Text, CharacterDataProxy {

    public void init(DocumentProxy owner, String text);
}
