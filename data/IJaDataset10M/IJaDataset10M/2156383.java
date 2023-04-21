package org.dbe.kb.qee.oclutil;

/** Instances of (usually inner) classes implementing this class represent
 *  OclExpressions that are parameters to iterating methods. For a explanation
 *  of the itering methods problem, see OclCollection.
 *
 *  <p>This interface is used for the collection operations <CODE>isUnique</CODE>,
 *  <CODE>collect</CODE> and <CODE>iterate</CODE>.
 *
 *  @see OclCollection
 *  @author George Kotopoulos
 */
public interface OclRootEvaluatable {

    public OclRoot evaluate();
}
