package net.sf.japaki.self;

import net.sf.japaki.beans.AbstractGenericBean;
import net.sf.japaki.beans.Property;

/**
 * Parser bean for bean parsers.
 * @version 1.0
 * @author Ralph Wagner
 */
public class PhraseBean extends AbstractGenericBean<PhraseBean> {

    private String phraseName;

    private Class baseClass;

    private BeanParserBean bean;

    /**
     * Returns the key where the parser is stored.
     * @return the key where the parser is stored
     */
    public String getPhraseName() {
        return phraseName;
    }

    /**
     * Returns the targetType of the created parser.
     * @return the targetType of the created parser
     */
    public Class getBaseClass() {
        return baseClass;
    }

    /**
     * Returns the pattern for the new parser.
     * @return the pattern for the new parser
     */
    public BeanParserBean getBean() {
        return bean;
    }

    /**
     * Changes the value of the phraseName field.
     * @param phraseName new value of the phraseName field
     */
    public void setPhraseName(String phraseName) {
        this.phraseName = phraseName;
    }

    /**
     * Changes the value of the baseClass field.
     * @param baseClass new value of the baseClass field
     */
    public void setBaseClass(Class baseClass) {
        this.baseClass = baseClass;
    }

    /**
     * Changes the value of the bean field.
     * @param bean new value of the bean field
     */
    public void setBean(BeanParserBean bean) {
        this.bean = bean;
    }

    /**
     * Property of the phraseName field
     */
    public static final Property<PhraseBean, String> phraseNameP = GenericProperty.<PhraseBean, String>newInstance("phraseName", 0, String.class);

    /**
     * Property of the baseClass field
     */
    public static final Property<PhraseBean, Class> baseClassP = GenericProperty.<PhraseBean, Class>newInstance("baseClass", 1, Class.class);

    /**
     * Property of the bean field
     */
    public static final Property<PhraseBean, BeanParserBean> beanP = GenericProperty.<PhraseBean, BeanParserBean>newInstance("bean", 2, BeanParserBean.class);

    private static final java.util.List<Property<? super PhraseBean, ?>> PROPERTIES = createProperties();

    private static final java.util.List<Property<? super PhraseBean, ?>> createProperties() {
        java.util.List<Property<? super PhraseBean, ?>> reply = new java.util.ArrayList<Property<? super PhraseBean, ?>>();
        reply.add(phraseNameP);
        reply.add(baseClassP);
        reply.add(beanP);
        return java.util.Collections.unmodifiableList(reply);
    }

    public java.util.List<Property<? super PhraseBean, ?>> getProperties() {
        return PROPERTIES;
    }

    protected Object getValue(Property property) {
        Object reply = null;
        if (property == phraseNameP) {
            reply = getPhraseName();
        } else if (property == baseClassP) {
            reply = getBaseClass();
        } else if (property == beanP) {
            reply = getBean();
        } else {
            throw new IllegalArgumentException(property + " in " + this.getClass());
        }
        return reply;
    }

    protected void setValue(Property property, Object value) {
        if (property == phraseNameP) {
            setPhraseName((String) value);
        } else if (property == baseClassP) {
            setBaseClass((Class) value);
        } else if (property == beanP) {
            setBean((BeanParserBean) value);
        } else {
            throw new IllegalArgumentException(property + " in " + this.getClass());
        }
    }

    public PhraseBean() {
        phraseName = null;
        bean = new BeanParserBean();
    }

    public String toString() {
        return getPhraseName();
    }
}
