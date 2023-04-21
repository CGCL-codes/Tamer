package org.gudy.azureus2.pluginsimpl.remote;

/**
 * @author parg
 *
 */
public interface RPRequestDispatcher {

    public RPPluginInterface getPlugin();

    public RPReply dispatch(RPRequest request) throws RPException;
}
