package DE.FhG.IGD.atlas.lsp;

import DE.FhG.IGD.util.URL;

/**
 * Represents the interface for a LSP_RegisterPlain, a LSP_RegisterEncrypted
 * or a LSP_RegisterEncoded structure of the Location Service Protocol. <p>
 *
 * The implicit name is the unique identifier of an mobile object
 * for instance the hashcode of its static part. The contact address
 * should consist of a URL string that specifies the protocol, address,
 * and port of a management service provided by the server, which is in
 * possession of the specified object. The current cookie is used as 
 * authorisation mechanism by the <code>LSServer</code> and 
 * <code>LSProxy</code> permitting a <code>LSClient</code> to update or
 * delete an existing entry. The new cookie represents the authorisation
 * code for the next registration.<p>
 *
 * To distinguish between initialization, update and deletion a special
 * {@link DE.FhG.IGD.atlas.core.CookieManager#NULL_COOKIE NULL_COOKIE}
 * is used as follows:
 * <blockquote>
 * 
 * <code>if (currentCookie == NULL_COOKIE) </code>
 * { Initialize a new entry }<p>
 * 
 * <code>if (newCookie == NULL_COOKIE) </code>
 * { Delete the existing entry }<p>
 * 
 * otherwise update the existing entry
 * 
 * </blockquote>
 * 
 * @author Jan Peters
 * @version "$Id: LSPRegister.java 476 2001-08-24 18:19:00Z jpeters $"
 * @see LSPRegisterPlain
 * @see LSPRegisterEncrypted
 * @see LSPRegisterEncoded
 */
public interface LSPRegister {

    /**
     * Returns the implicit name.
     *
     * @return The implicit name.
     */
    public byte[] getImplicitName();

    /**
     * Returns the contact address or an empty URL, 
     * if the encoded contact address is not well-formed.
     *
     * @return The contact address.
     */
    public URL getContactAddress();

    /**
     * Returns the new cookie.
     *
     * @return The new cookie.
     */
    public byte[] getNewCookie();

    /**
     * Returns the current cookie.
     *
     * @return The current cookie.
     */
    public byte[] getCurrentCookie();
}
