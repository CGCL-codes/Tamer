package net.sf.mmm.content.parser.impl;

import javax.inject.Named;
import javax.inject.Singleton;
import net.sf.mmm.content.parser.base.AbstractContentParserService;

/**
 * This is the abstract base implementation of the
 * {@link net.sf.mmm.content.parser.api.ContentParserService} interface.
 * 
 * @author Joerg Hohwiller (hohwille at users.sourceforge.net)
 */
@Named
@Singleton
public class ContentParserServiceImpl extends AbstractContentParserService {

    /**
   * The constructor.
   */
    public ContentParserServiceImpl() {
        super();
    }
}
