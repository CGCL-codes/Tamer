package com.bluebrim.page.impl.server;

import java.util.*;

/**
 * An instance of this class represents a page sequence variant where some pages are the same as the
 * original and som pages are replaced with pages specific for the variant. The class is used for modeling
 * the edition concept used in the newspaper industry.
 * Creation date: (2001-11-21 12:07:45)
 * @author: G�ran St�ck 
 */
public class CoPageSequenceVariation extends CoAbstractPageSequence {

    private CoPageSequence m_orginal;

    private Map m_replacedPages;
}
