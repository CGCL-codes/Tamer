package com.jacum.cms.transform;

import com.jacum.cms.session.ContentRequestContext;

/**
 * The implementations of this class are responsible for choosing
 * appropriate content transformer for given state of content request context
 */
public interface ContentTransformerMapper {

    ContentTransformer nextTransformer(ContentRequestContext ctx);
}
