package net.sourceforge.esw.collection;

import java.io.Serializable;
import net.sourceforge.esw.graph.context.INodeContextFactory;

/**
 * Performs Transduction between a specific data source and an
 * <code>IMetaCollection</code> instance.
 * <p>
 *
 * This transduction occurs both ways; <code>put()</code> takes data
 * from the <code>IMetaCollection</code> instance to which this
 * <code>ITransducer</code> instance is attached and places it into the data
 * source represented by this <code>ITransducer</code> instance. The
 * <code>get()</code> method takes data from the data source represented by this
 * <code>ITransducer</code> instance and places it into the
 * <code>IMetaCollection</code> instance to which this <code>ITransducer</code>
 * instance is attached.
 * <p>
 *
 * All <code>ITransducer</code> implementors should remain stateless in their
 * interaction with the <code>IMetaCollection</code> instance to which this
 * <code>ITransducer</code> implementor is attached. This insures that
 * <code>ITransducer</code> implementors may be used in multi-threaded
 * environments.
 * <p>
 *
 * The <code>INodeContextFactory</code> instance attached to this
 * <code>ITransducer</code> instance provides context from which to create the
 * <code>INode</code> instances that are added to the
 * <code>IMetaCollection</code> instances. This <code>INodeContextFactory</code>
 * is only valid during the <code>get</code> operations of the
 * <code>ITransducer</code>. The context determines how new <code>INode</code>
 * instances are obtained during a <code>get()</code> operation. For instance,
 * the context controls whether a new <code>INode</code> instance is created,
 * or an entire subgraph of <code>INode</code> instances with a preset
 * structure. Advanced uses may include returning a graph of <code>INode</code>
 * instances that is reused from a previous transduction. The
 * <code>INodeContectFactory</code> is intended to be the primary form of
 * customization and extension to the creation and behavior of the
 * <code>IMetaCollection</code> / <code>ITransducer</code> / <code>INode</code>
 * framework.
 * <p>
 *
 * @see net.sourceforge.esw.collection.IMetaCollection
 * @see net.sourceforge.esw.graph.context.INodeContextFactory
 *
 */
public interface ITransducer extends Serializable {

    /****************************************************************************
   * Puts the referenced <code>IMetaCollection</code> instance's data into the
   * data source represented by this <code>ITransducer</code> instance.
   *
   * @param aCollection the <code>IMetaCollection</code> instance from which to
   *                    take data.
   *
   * @throws TransducerException if an error occurs during transduction.
   *
   * @see #get( IMetaCollection )
   */
    public void put(IMetaCollection aCollection) throws TransducerException;

    /****************************************************************************
   * Gets the data source's data represented by this <code>ITransducer</code>
   * instance into the referenced <code>IMetaCollection</code> instance.
   *
   * @param aCollection the <code>IMetaCollection</code> instance into which to
   *                    read.
   *
   * @throws TransducerException if an error occurs during transduction.
   *
   * @see #put( IMetaCollection )
   */
    public void get(IMetaCollection aCollection) throws TransducerException;

    /****************************************************************************
   * Sets the <code>INodeContextFactory</code> instance to be used by this
   * <code>ITransducer</code> instance during <code>get</code> operations.
   *
   * @param aContextFactory the <code>INodeContextFactory</code> instance from
   *                        which to take data.
   *
   * @see #getContextFactory()
   */
    public void setContextFactory(INodeContextFactory aContextFactory);

    /****************************************************************************
   * Returns the <code>INodeContextFactory</code> instance used by this
   * <code>ITransducer</code> instance during <code>get</code> operations.
   *
   * @return the <code>INodeContextFactory</code> instance utilitied by this
   *         <code>ITransucer</code> instance.
   *
   * @see #setContextFactory( INodeContextFactory )
   */
    public INodeContextFactory getContextFactory();
}
