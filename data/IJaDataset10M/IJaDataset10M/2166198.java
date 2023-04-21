package DE.FhG.IGD.atlas.core;

import DE.FhG.IGD.util.URL;
import DE.FhG.IGD.atlas.lsp.*;

/**
 * Defines the interface to a <code>LSClientService</code> implementation.
 * 
 * The <code>LSClientService</code> provides the functionality of the Location
 * Server Client ({@link LSClient LSClient}) which allows a user to
 * register and lookup locations of mobile objects.
 * 
 * @author Jan Peters
 * @version "$Id: LSClientService.java 462 2001-08-21 18:21:00Z vroth $"
 * @see LSClient
 */
public interface LSClientService {

    /**
     * Processes the given <code>lookup</code> request
     * on the local SeMoA system, on an existing <code>LSProxy</code>
     * and/or the corresponding <code>LSServer</code> in following manner
     * (see {@link DE.FhG.IGD.atlas.lsp.LSPLookup LSPLookup}). <p>
     *
     * <blockquot><pre> 
     *  +--localLookup
     *  |  
     *  +--proxyLookup
     *  |  |
     *  |  +--globalLookup
     *  |
     *  +--globalLookup
     * </pre></blockquot>
     *
     * The lookup request is processed on the local SeMoA first.<p>
     *
     * If this request was not successful and a <code>LSProxy</code> is
     * existent in the local LAN, it is used to process the lookup request.
     * The proxy itself forwards the request to the corresponding
     * <code>LSServer</code> in the case, that the searched entry is not
     * found on the proxy.<p>
     *
     * If no <code>LSProxy</code> is existent in the local LAN, the
     * request is processed directly by the corresponding
     * <code>LSServer</code> as described.<p>
     *
     * Following <code>reply</code> states and bodies are possible 
     * (see {@link DE.FhG.IGD.atlas.lsp.LSPReply LSPReply}):
     * <p><ul>
     * <li> <code>ACKNOWLEDGE</code>
     *   ({@link DE.FhG.IGD.atlas.lsp.LSPLookupResult LSPLookupResult}
     *   body)
     * <li> <code>IMPLICIT_NAME_NOT_FOUND</code> (no body)
     * <p>
     * <li> <code>WRONG_VERSION</code> (no body)
     * <li> <code>REQUEST_TYPE_INVALID</code> (no body)
     * <li> <code>REQUEST_BODY_INVALID</code> (no body)
     * <p>
     * <li> <code>INTERNAL_CLIENT_ERROR</code> (no body)
     * <li> <code>IO_ERROR</code> (no body)
     * </ul><p>
     *
     * The <code>SERVER_ERROR_FLAG</code> of the reply is set,
     * if the reply state of the <code>LSServer</code> received by
     * the proxy had been an <code>IO_ERROR</code>.<p>
     *
     * The <code>PROXY_ERROR_FLAG</code> of the reply is set,
     * if the reply state of the <code>LSProxy</code> received by
     * the client had been an <code>IO_ERROR</code>.<p>
     *
     * If the proxy can be found locally, it is accessed directly
     * through the <code>LSProxyService</code> interface,
     * otherwise the request is send within the local LAN.<p>
     * 
     * If the corresponding server can be found locally, it is
     * accessed directly through the <code>LSServerService</code>
     * interface, otherwise the request is send over the internet.<p>
     * 
     * @param lookup The register lookup.
     * @return The reply to the given request.
     */
    public LSPReply lookup(LSPLookup lookup);

    /**
     * Lookups the given <code>implicitName</code> on the local SeMoA system,
     * on an existing <code>LSProxy</code> and/or the corresponding
     * <code>LSServer</code> in following manner
     * (see {@link DE.FhG.IGD.atlas.lsp.LSPLookup LSPLookup}). <p>
     *
     * <blockquot><pre> 
     *  +--localLookup
     *  |  
     *  +--proxyLookup
     *  |  |
     *  |  +--globalLookup
     *  |
     *  +--globalLookup
     * </pre></blockquot>
     *
     * The lookup request is processed on the local SeMoA first.<p>
     *
     * If this request was not successful and a <code>LSProxy</code> is
     * existent in the local LAN, it is used to process the lookup request.
     * The proxy itself forwards the request to the corresponding
     * <code>LSServer</code> in the case, that the searched entry is not
     * found on the proxy.<p>
     *
     * If no <code>LSProxy</code> is existent in the local LAN, the
     * request is processed directly by the corresponding
     * <code>LSServer</code> as described.<p>
     *
     * Following <code>reply</code> states and bodies are possible 
     * (see {@link DE.FhG.IGD.atlas.lsp.LSPReply LSPReply}):
     * <p><ul>
     * <li> <code>ACKNOWLEDGE</code>
     *   ({@link DE.FhG.IGD.atlas.lsp.LSPLookupResult LSPLookupResult}
     *   body)
     * <li> <code>IMPLICIT_NAME_NOT_FOUND</code> (no body)
     * <p>
     * <li> <code>WRONG_VERSION</code> (no body)
     * <li> <code>REQUEST_TYPE_INVALID</code> (no body)
     * <li> <code>REQUEST_BODY_INVALID</code> (no body)
     * <p>
     * <li> <code>INTERNAL_CLIENT_ERROR</code> (no body)
     * <li> <code>IO_ERROR</code> (no body)
     * </ul><p>
     *
     * The <code>SERVER_ERROR_FLAG</code> of the reply is set,
     * if the reply state of the <code>LSServer</code> received by
     * the proxy had been an <code>IO_ERROR</code>.<p>
     *
     * The <code>PROXY_ERROR_FLAG</code> of the reply is set,
     * if the reply state of the <code>LSProxy</code> received by
     * the client had been an <code>IO_ERROR</code>.<p>
     *
     * If the proxy can be found locally, it is accessed directly
     * through the <code>LSProxyService</code> interface,
     * otherwise the request is send within the local LAN.<p>
     * 
     * If the corresponding server can be found locally, it is
     * accessed directly through the <code>LSServerService</code>
     * interface, otherwise the request is send over the internet.<p>
     * 
     * @param implicitName The implicit name.
     * @return The reply to the given request.
     */
    public LSPReply lookup(byte[] implicitName);

    /**
     * Processes the given <code>register</code> request on an
     * existing <code>LSProxy</code> and/or the corresponding
     * <code>LSServer</code> in following manner
     * (see {@link DE.FhG.IGD.atlas.lsp.LSPRegister LSPRegister}). <p>
     *
     * <blockquot><pre> 
     *  +--proxyRegisterPlain
     *  |  |
     *  |  +--globalRegisterEncrypted/Encoded
     *  |     |
     *  |     +--globalRegisterPlain
     *  |
     *  +--globalRegisterEncrypted/Encoded
     *     |
     *     +--globalRegisterPlain
     * </pre></blockquot>
     *
     * If a <code>LSProxy</code> is existent in the local LAN, it is
     * used to process the register request. The proxy itself forwards
     * the request to the corresponding <code>LSServer</code> by
     * using encryption respectively encoding of the request message if
     * security options are available, using plain messages otherwise.<p>
     *
     * If no <code>LSProxy</code> is existent in the local LAN, the
     * request is processed directly by the corresponding
     * <code>LSServer</code> as described.<p>
     *
     * Following <code>reply</code> states are possible 
     * (see {@link DE.FhG.IGD.atlas.lsp.LSPReply LSPReply}):
     * <p><ul>
     * <li> <code>ACKNOWLEDGE</code>
     * <p>
     * <li> <code>IMPLICIT_NAME_NOT_PRESENT</code>
     * <li> <code>CONTACT_ADDRESS_NOT_EXISTENT</code>
     * <li> <code>COOKIE_INVALID</code>
     * <p>
     * <li> <code>WRONG_VERSION</code>
     * <li> <code>REQUEST_TYPE_INVALID</code>
     * <li> <code>REQUEST_BODY_INVALID</code>
     * <p>
     * <li> <code>INTERNAL_CLIENT_ERROR</code>
     * <li> <code>IO_ERROR</code>
     * </ul><p>
     *
     * The <code>SERVER_ERROR_FLAG</code> of the reply is set,
     * if the reply state of the <code>LSServer</code> received by
     * the proxy had been an <code>IO_ERROR</code>.<p>
     *
     * The <code>PROXY_ERROR_FLAG</code> of the reply is set,
     * if the reply state of the <code>LSProxy</code> received by
     * the client had been an <code>IO_ERROR</code>.<p>
     *
     * If the proxy can be found locally, it is accessed directly
     * through the <code>LSProxyService</code> interface,
     * otherwise the request is send within the local LAN.<p>
     * 
     * If the corresponding server can be found locally, it is
     * accessed directly through the <code>LSServerService</code>
     * interface, otherwise the request is send over the internet.<p>
     * 
     * @param register The register request.
     * @return The reply to the given request.
     */
    public LSPReply register(LSPRegister register);

    /**
     * Registers the given <code>implicitName</code> and 
     * <code>contactAddress</code> on an existing <code>LSProxy</code>
     * and/or the corresponding <code>LSServer</code> in following manner
     * (see {@link DE.FhG.IGD.atlas.lsp.LSPRegister LSPRegister}). <p>
     *
     * <blockquot><pre> 
     *  +--proxyRegisterPlain
     *  |  |
     *  |  +--globalRegisterEncrypted/Encoded
     *  |     |
     *  |     +--globalRegisterPlain
     *  |
     *  +--globalRegisterEncrypted/Encoded
     *     |
     *     +--globalRegisterPlain
     * </pre></blockquot>
     *
     * If a <code>LSProxy</code> is existent in the local LAN, it is
     * used to process the register request. The proxy itself forwards
     * the request to the corresponding <code>LSServer</code> by
     * using encryption respectively encoding of the request message if
     * security options are available, using plain messages otherwise.<p>
     *
     * If no <code>LSProxy</code> is existent in the local LAN, the
     * request is processed directly by the corresponding
     * <code>LSServer</code> as described.<p>
     *
     * Following <code>reply</code> states are possible 
     * (see {@link DE.FhG.IGD.atlas.lsp.LSPReply LSPReply}):
     * <p><ul>
     * <li> <code>ACKNOWLEDGE</code>
     * <p>
     * <li> <code>IMPLICIT_NAME_NOT_PRESENT</code>
     * <li> <code>CONTACT_ADDRESS_NOT_EXISTENT</code>
     * <li> <code>COOKIE_INVALID</code>
     * <p>
     * <li> <code>WRONG_VERSION</code>
     * <li> <code>REQUEST_TYPE_INVALID</code>
     * <li> <code>REQUEST_BODY_INVALID</code>
     * <p>
     * <li> <code>INTERNAL_CLIENT_ERROR</code>
     * <li> <code>IO_ERROR</code>
     * </ul><p>
     *
     * The <code>SERVER_ERROR_FLAG</code> of the reply is set,
     * if the reply state of the <code>LSServer</code> received by
     * the proxy had been an <code>IO_ERROR</code>.<p>
     *
     * The <code>PROXY_ERROR_FLAG</code> of the reply is set,
     * if the reply state of the <code>LSProxy</code> received by
     * the client had been an <code>IO_ERROR</code>.<p>
     *
     * If the proxy can be found locally, it is accessed directly
     * through the <code>LSProxyService</code> interface,
     * otherwise the request is send within the local LAN.<p>
     * 
     * If the corresponding server can be found locally, it is
     * accessed directly through the <code>LSServerService</code>
     * interface, otherwise the request is send over the internet.<p>
     *
     * In the case that an update request could not be processed 
     * succesfully, because the corresponding entry has not been
     * found, it's tried to reinitialize the entry with an init
     * request.<p>
     * 
     * @param implicitName The implicit name.
     * @param contactAddress The contact address.
     * @param newCookie The new cookie.
     * @param currentCookie The current cookie
     * @return The reply to the given request.
     */
    public LSPReply register(byte[] implicitName, URL contactAddress, byte[] newCookie, byte[] currentCookie);
}
