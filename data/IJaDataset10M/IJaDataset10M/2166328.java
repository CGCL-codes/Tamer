package com.migazzi.dm4j.cache.impl;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;
import com.migazzi.dm4j.common.Dependency;
import com.migazzi.dm4j.common.Type;

public class ArtifactInfosParser {

    private static final EnumConverter ENUM_CONVERTER = new EnumConverter();

    private static final TimestampConverter TIMESTAMP_CONVERTER = new TimestampConverter();

    private Digester digester;

    public ArtifactInfosParser() {
        if (ConvertUtils.lookup(Type.class) != ENUM_CONVERTER) {
            ConvertUtils.deregister(Type.class);
            ConvertUtils.register(ENUM_CONVERTER, Type.class);
        }
        if (ConvertUtils.lookup(Date.class) != TIMESTAMP_CONVERTER) {
            ConvertUtils.deregister(Date.class);
            ConvertUtils.register(TIMESTAMP_CONVERTER, Date.class);
        }
        digester = new Digester();
        digester.setValidating(false);
        setRules();
    }

    private void setRules() {
        digester.addObjectCreate(ArtifactInfosTags.NODE_ROOT, CachedArtifactInfos.class);
        digester.addSetProperties(ArtifactInfosTags.NODE_ROOT, ArtifactInfosTags.ATTR_ROOT___TIMESTAMP, "timestamp");
        digester.addObjectCreate(ArtifactInfosTags.NODE_ROOT + "/" + ArtifactInfosTags.NODE_ARTIFACT, CachedArtifact.class);
        digester.addSetProperties(ArtifactInfosTags.NODE_ROOT + "/" + ArtifactInfosTags.NODE_ARTIFACT, ArtifactInfosTags.ATTR_ARTIFACT___TYPE, "type");
        digester.addSetProperties(ArtifactInfosTags.NODE_ROOT + "/" + ArtifactInfosTags.NODE_ARTIFACT, ArtifactInfosTags.ATTR_ARTIFACT___FILENAME, "filename");
        digester.addSetProperties(ArtifactInfosTags.NODE_ROOT + "/" + ArtifactInfosTags.NODE_ARTIFACT, ArtifactInfosTags.ATTR_ARTIFACT___TIMESTAMP_FILENAME, "timestampFilename");
        digester.addSetProperties(ArtifactInfosTags.NODE_ROOT + "/" + ArtifactInfosTags.NODE_ARTIFACT, ArtifactInfosTags.ATTR_ARTIFACT___TIMESTAMP, "timestamp");
        digester.addSetNext(ArtifactInfosTags.NODE_ROOT + "/" + ArtifactInfosTags.NODE_ARTIFACT, "addCachedArtifact");
        digester.addObjectCreate(ArtifactInfosTags.NODE_ROOT + "/" + ArtifactInfosTags.NODE_ARTIFACT + "/" + ArtifactInfosTags.NODE_DEPENDENCY, Dependency.class);
        digester.addSetProperties(ArtifactInfosTags.NODE_ROOT + "/" + ArtifactInfosTags.NODE_ARTIFACT + "/" + ArtifactInfosTags.NODE_DEPENDENCY, ArtifactInfosTags.ATTR_DEPENDENCY___ORG, "organisationName");
        digester.addSetProperties(ArtifactInfosTags.NODE_ROOT + "/" + ArtifactInfosTags.NODE_ARTIFACT + "/" + ArtifactInfosTags.NODE_DEPENDENCY, ArtifactInfosTags.ATTR_DEPENDENCY___NAME, "name");
        digester.addSetProperties(ArtifactInfosTags.NODE_ROOT + "/" + ArtifactInfosTags.NODE_ARTIFACT + "/" + ArtifactInfosTags.NODE_DEPENDENCY, ArtifactInfosTags.ATTR_DEPENDENCY___VERSION, "versionExpression");
        digester.addSetProperties(ArtifactInfosTags.NODE_ROOT + "/" + ArtifactInfosTags.NODE_ARTIFACT + "/" + ArtifactInfosTags.NODE_DEPENDENCY, ArtifactInfosTags.ATTR_DEPENDENCY___TYPE, "type");
        digester.addSetNext(ArtifactInfosTags.NODE_ROOT + "/" + ArtifactInfosTags.NODE_ARTIFACT + "/" + ArtifactInfosTags.NODE_DEPENDENCY, "addDependency");
    }

    public CachedArtifactInfos parse(InputStream is) throws CacheInfosParsingException {
        try {
            return (CachedArtifactInfos) digester.parse(is);
        } catch (IOException e) {
            throw new CacheInfosParsingException(e);
        } catch (SAXException e) {
            throw new CacheInfosParsingException(e);
        }
    }
}
