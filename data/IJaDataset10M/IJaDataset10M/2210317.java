package info.repo.didl.serialize;

import info.repo.didl.AttributeType;

/**
 * The <code>ContentStrategyConditionType</code> interface is used to construct
 * rules for content matching. The matching solution is explained in 
 * <code> DIDLStrategyType </code>
 *
 * @author Xiaoming Liu <liu_x@lanl.gov>
 * @see DIDLStrategyType
 */
public interface ContentStrategyConditionType {

    /**
     * if the combinations of attributes match a Content 
     * @param attributes attributes associating with the ContentWrapper
     * @param mimeType mimeType attributes of the ContentWrapper
     * @param namespace namespace of the inline content.
     * @return true if matched, false if not.
     */
    public boolean match(AttributeType attributes, String mimeType, String namespace);

    /**
     * find implementation content class associating with this condition
     * @return implementation content class 
     */
    public Class getImplClass();
}
