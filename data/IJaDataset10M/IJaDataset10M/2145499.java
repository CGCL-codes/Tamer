package com.migazzi.dm4j.versionExpressionParser.impl;

import org.antlr.runtime.ANTLRStringStream;
import org.antlr.runtime.CommonTokenStream;
import org.antlr.runtime.RecognitionException;
import com.migazzi.dm4j.spring.Factory;
import com.migazzi.dm4j.versionExpressionParser.Version;
import com.migazzi.dm4j.versionExpressionParser.VersionChecker;
import com.migazzi.dm4j.versionExpressionParser.VersionMatcher;
import com.migazzi.dm4j.versionExpressionParser.VersionMatchingException;
import com.migazzi.dm4j.versionExpressionParser.VersionParsingException;

public class VersionMatcherImpl implements VersionMatcher {

    private VersionChecker checker;

    private String _pattern;

    public VersionMatcherImpl(String pattern) throws VersionMatcherInitialisationException {
        this._pattern = pattern;
        VersionExpressionLexer patternLexer = new VersionExpressionLexer(new ANTLRStringStream(pattern));
        CommonTokenStream patternTokenStream = new CommonTokenStream(patternLexer);
        VersionExpressionParser patternParser = new VersionExpressionParser(patternTokenStream);
        VersionExpressionParser.expression_rule_return patternResult;
        try {
            patternResult = patternParser.expression_rule();
        } catch (RecognitionException e) {
            throw new VersionMatcherInitialisationException("cannot parse pattern : " + pattern, e);
        }
        checker = patternResult.expression;
    }

    public boolean match(String version) throws VersionMatchingException {
        Version v;
        try {
            v = Factory.getVersionHelper().getVersion(version);
        } catch (VersionParsingException e) {
            throw new VersionMatchingException("cannot check if version " + version + " respect pattern " + _pattern, e);
        }
        return checker.check(v);
    }
}
