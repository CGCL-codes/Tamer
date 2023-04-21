package org.avaje.ebean.enhance.agent;

/**
 * Constant values used in byte code generation.
 */
public interface EnhanceConstants {

    public static final String AVAJE_TRANSACTIONAL_ANNOTATION = "Lcom/avaje/ebean/annotation/Transactional;";

    public static final String ENTITY_ANNOTATION = "Ljavax/persistence/Entity;";

    public static final String EMBEDDABLE_ANNOTATION = "Ljavax/persistence/Embeddable;";

    public static final String MAPPEDSUPERCLASS_ANNOTATION = "Ljavax/persistence/MappedSuperclass;";

    public static final String IDENTITY_FIELD = "_ebean_identity";

    public static final String INTERCEPT_FIELD = "_ebean_intercept";

    public static final String C_ENHANCEDTRANSACTIONAL = "com/avaje/ebean/bean/EnhancedTransactional";

    public static final String C_ENTITYBEAN = "com/avaje/ebean/bean/EntityBean";

    public static final String C_INTERCEPT = "com/avaje/ebean/bean/EntityBeanIntercept";

    public static final String L_INTERCEPT = "Lcom/avaje/ebean/bean/EntityBeanIntercept;";

    /**
     * The suffix added to the super class name.
     */
    public static final String SUFFIX = "$$EntityBean";
}
