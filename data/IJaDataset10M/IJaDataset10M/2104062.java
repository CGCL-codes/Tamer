package org.personalsmartspace.spm.identity.api;

import java.util.HashMap;
import java.util.Map;

/**
 * This is the basic interface for access to internals of digital identity used in PERSIST. 
 * Basically, a digital identity has the following structure:<br>
 * <br> 
 * <table border="1" frame="box" rules="none" cellpadding="10" width="200">
 *         <tr>
 *                 <th>{@link Identifier}</th>
 *         </tr>
 * </table>
 * <table border="1" frame="box" rules="none" cellpadding="10" width="200">
 *         <tr>
 *                 <th>{@link IdentityState}</th>
 *         </tr>
 * </table>
 * <table frame="box" border="1" rules="none" cellpadding="0" width="200">
 *         <tr>
 *                 <th><table width="190" cellpadding="10"><tr><th>Attributes</th></tr></table></th>
 *         </tr>
 *         <tr>
 *                 <td>Attribute 1</td>
 *         </tr>
 *         <tr>
 *                 <td>Attribute 2</td>
 *         </tr>
 *         <tr>
 *                 <td>Attribute 3</td>
 *         </tr>
 *         <tr>
 *                 <td>...</td>
 *         </tr>
 * </table>
 * <br>
 * {@link Identifier} is used 
 * for unique referencing of the digital identity and is also disclosed to other PSSs in order 
 * for them to be able to access the attributes negotiated at the time of privacy policy 
 * negotiation.<br>
 * <br> 
 * Attributes are typically {@link org.personalsmartspace.cm.model.api.ICtxIdentifier ICtxIdentifier}s 
 * of the corresponding attributes stored in the context database. However, what other PSSs can 
 * see of attributes are actually only {@link java.lang.String String} descriptions of their 
 * types. Other PSSs can retrieve attributes using {@link org.personalsmartspace.cm.broker.api.ICtxBroker ICtxBroker}
 * and providing the type which is further resolved to the actual ICtxIdentifier by 
 * {@link IIdentityManagement IdentityManagement}.<br>
 */
public interface Identity {

    /**
     * Get the unique identifier of this digital identity.
     */
    public DigitalPersonalIdentifier getID();

    /**
     * Get the state this identity is currently in.
     */
    public IdentityState getState();

    /**
     * Get the set (in a descriptive way) of attributes which are disclosed 
     * under this digital identity.
     */
    public String[] getAttributes();
}
