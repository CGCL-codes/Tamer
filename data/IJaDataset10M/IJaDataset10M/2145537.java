package org.opt4j.operator.algebra;

import org.opt4j.operator.common.GenericOperator;
import com.google.inject.ImplementedBy;

/**
 * The {@code AlgebraGeneric} is an interface for a generic {@code Algebra}
 * operator that is able to handle different {@code Genotypes}.
 * 
 * @author lukasiewycz
 * 
 */
@ImplementedBy(AlgebraGenericImplementation.class)
public interface AlgebraGeneric extends Algebra, GenericOperator<Algebra> {
}
