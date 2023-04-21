package com.jacum.cms.source.jcr;

import com.jacum.cms.session.content.ContentItem;

/**
 * Maps template IDs to nodes
 */
public interface TemplateMapper {

    String getTemplateId(ContentItem item);
}
