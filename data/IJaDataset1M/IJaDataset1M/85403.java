package net.sf.dz.daemon;

/**
 * A server module.
 *
 * @author Copyright &copy; <a href="mailto:vt@freehold.crocodile.org">Vadim Tkachenko</a> 2001-2002
 * @version $Id: ServerModule.java,v 1.2 2004-12-14 22:00:53 vtt Exp $
 */
public interface ServerModule {

    /**
     * Attach the module to the server.
     *
     * This is necessary for the module to be aware of the context. It is a
     * responsibility of the module to know how to interpret the server
     * context.
     *
     * <p>
     *
     * The module is attached to the server right after it is configured,
     * but before it is started.
     *
     * @param server Server to attach to.
     */
    void attach(Server server);
}
