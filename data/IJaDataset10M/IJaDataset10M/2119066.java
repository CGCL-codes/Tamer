package org.robotframework.javalib.library;

import org.robotframework.javalib.factory.CompositeKeywordFactory;
import org.robotframework.javalib.factory.KeywordFactory;
import org.robotframework.javalib.keyword.Keyword;

/**
 * Library that combines {@link SpringLibrary} and {@link ClassPathLibrary}.
 */
public class CompositeLibrary extends KeywordFactoryBasedLibrary<Keyword> {

    private String keywordPattern;

    private String configFilePattern;

    private boolean needToSetKeywordPattern;

    private boolean needToSetConfigFilePattern;

    public CompositeLibrary() {
    }

    public CompositeLibrary(String keywordPattern, String configFilePattern) {
        setKeywordPattern(keywordPattern);
        setConfigFilePattern(configFilePattern);
    }

    /**
     * Creates a {@link CompositeKeywordFactory} and adds a
     * {@link SpringLibrary} and a {@link ClassPathLibrary}.
     */
    protected KeywordFactory<Keyword> createKeywordFactory() {
        SpringLibrary springLibrary = new SpringLibrary();
        springLibrary.setClassLoader(getClassLoader());
        if (needToSetConfigFilePattern) {
            springLibrary.setConfigFilePattern(configFilePattern);
        }
        AnnotationLibrary annotationLibrary = new AnnotationLibrary();
        ClassPathLibrary classPathLibrary = new ClassPathLibrary();
        classPathLibrary.setClassLoader(getClassLoader());
        if (needToSetKeywordPattern) {
            classPathLibrary.setKeywordPattern(keywordPattern);
            annotationLibrary.addKeywordPattern(keywordPattern);
        }
        CompositeKeywordFactory factory = new CompositeKeywordFactory();
        factory.addKeywordFactory(springLibrary.createKeywordFactory());
        factory.addKeywordFactory(classPathLibrary.createKeywordFactory());
        factory.addKeywordFactory(annotationLibrary.createKeywordFactory());
        return factory;
    }

    /**
     * @see ClassPathLibrary#setKeywordPattern(String)
     */
    public void setKeywordPattern(String pattern) {
        needToSetKeywordPattern = true;
        keywordPattern = pattern;
    }

    /**
     * @see SpringLibrary#setConfigFilePattern(String)
     */
    public void setConfigFilePattern(String pattern) {
        needToSetConfigFilePattern = true;
        configFilePattern = pattern;
    }
}
