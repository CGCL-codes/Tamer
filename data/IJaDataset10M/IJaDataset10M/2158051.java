package com.bluebrim.text.shared;

import java.rmi.*;
import java.util.*;
import com.bluebrim.base.shared.*;

/**
 * Insert the type's description here.
 * Creation date: (2000-10-31 09:47:16)
 * @author Dennis
 */
public interface CoTypographyRuleIF extends com.bluebrim.xml.shared.CoXmlEnabledIF, CoObjectIF, Remote {

    String FACTORY_KEY = "typography_rule";

    com.bluebrim.text.shared.CoCharacterStyleIF addCharacterStyle(String name);

    com.bluebrim.text.shared.CoParagraphStyleIF addParagraphStyle(String name);

    void clear();

    com.bluebrim.text.shared.CoCharacterStyleIF getCharacterStyle(String name);

    List getCharacterStyles();

    com.bluebrim.text.shared.CoParagraphStyleIF getParagraphStyle(String name);

    List getParagraphStyles();

    String readFromXtg(java.io.InputStream s);

    void removeCharacterStyle(String name);

    void removeParagraphStyle(String name);

    void removeStyles(List styles);
}
