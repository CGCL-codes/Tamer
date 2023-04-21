package org.nexopenframework.persistence.criterion;

/**
 * <p>NexOpen Framework</p>
 * 
 * <p>Extension of the {@link Criterion} interface 
 *    for dealing with SQL expressions</p>
 *
 * @author Francesc Xavier Magdaleno
 * @version $Revision $,$Date: 2009-07-07 21:31:44 +0100 $ 
 * @since 2.0.0.GA
 */
public interface Expression extends Criterion {

    public String getPropertyName();

    public Object getValue();
}
