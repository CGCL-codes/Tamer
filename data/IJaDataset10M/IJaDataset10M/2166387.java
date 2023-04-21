package com.ouroboroswiki.core.content.xslt.fop;

import java.util.Map;
import com.ouroboroswiki.core.Content;
import com.ouroboroswiki.core.content.xslt.XSLTContent;
import com.ouroboroswiki.core.content.xslt.XSLTContentRepository;

public class XSLFOContentRepository extends XSLTContentRepository {

    public XSLFOContentRepository(String name, String mimeType, Map<String, Object> globalProperties) {
        super(name, mimeType, globalProperties);
    }

    @Override
    protected XSLTContent makeContent(Content xmlContent, Content xslContent, Content propertiesContent) {
        return new XSLFOContent(xmlContent, xslContent, propertiesContent);
    }
}
