package com.hack23.cia.service.impl.commondao.impl;

import com.hack23.cia.model.api.application.Content;
import com.hack23.cia.model.impl.application.common.LanguageContent;
import com.hack23.cia.service.api.common.GenericUserInterfaceLoaderService;
import com.hack23.cia.service.impl.commondao.api.LanguageContentDAO;

/**
 * The Class LanguageContentDAOImpl.
 */
public class LanguageContentDAOImpl extends GenericHibernateDAO<Content, LanguageContent, Long> implements LanguageContentDAO, GenericUserInterfaceLoaderService<Content, LanguageContent> {

    /**
     * Instantiates a new language content dao impl.
     */
    public LanguageContentDAOImpl() {
        super(LanguageContent.class);
    }
}
