package org.personalsmartspace.cm.model.api;

/**
 * This interface is used to identify context entities. It provides methods that
 * return information about the identified entity including:
 * <ul>
 * <li><tt>PssId</tt>: A unique identifier of the PSS where the identified
 * context entity was first stored.</li>
 * <li><tt>PssDevId</tt>: An identifier of the device where the respective
 * context information was initially sensed/collected and stored. This is the
 * home device ID.</li>
 * <li><tt>ModelType</tt>: Describes the type of the identified context model
 * object, i.e. {@link CtxModelType#ENTITY ENTITY}.</li>
 * <li><tt>Type</tt>: A semantic tag that characterises the identified context
 * entity. e.g. "Person".</li>
 * <li><tt>ObjectNumber</tt>: A unique number within the device where the
 * respective context information was initially sensed/collected and stored.</li>
 * </ul>
 * <p>
 * A context entity identifier can be represented as a String as follows:
 * 
 * <pre>
 * PssId / PssDevId / ENTITY / Type / ObjectNumber
 * </pre>
 * 
 * @see ICtxIdentifier
 * @author <a href="mailto:nicolas.liampotis@cn.ntua.gr">Nicolas Liampotis</a>
 *         (ICCS)
 * @since 0.1.0
 */
public interface ICtxEntityIdentifier extends ICtxIdentifier {

    /**
     * Returns a String representation of this context entity identifier
     * 
     * @return a String representation of this context entity identifier
     */
    public String toString();
}
