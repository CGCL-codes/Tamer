package org.xactor.tm.recovery;

/**
 * Interface with a single method for releasing the access to an
 * <code>XAResource</code>. The holder of an <code>XAResourceAccess<code>
 * instance has access to some <code>XAResource</code> through a connection
 * to an XA datasource. When it finishes using the <code>XAResource</code>,  
 * it must call <code>release</code> on the <code>XAResourceAccess<code> 
 * instance. If no other object has access to the <code>XAResource</code>,
 * then the <code>relase</code> call closes the underlying 
 * <code>XAConnection</code>. 
 * 
 * @author <a href="reverbel@ime.usp.br">Francisco Reverbel</a>
 * @version $Revision: 37634 $
 */
public interface XAResourceAccess {

    void release();
}
