package de.fraunhofer.isst.axbench.operations.checker.solver.variabilityvisitor;

import de.fraunhofer.isst.axbench.axlang.api.IAXLangElement;
import de.fraunhofer.isst.axbench.operations.checker.solver.exceptions.SolverException;

/**
 * @brief @todo
 * not stable
 *
 * @author smann
 * @version 0.9.0
 * @since 0.9.0
 *
 *
 */
public interface IAxlVisitor {

    public IAXLangElement axlModel();

    public void setAxlModel(IAXLangElement model) throws SolverException;

    public void interpret() throws SolverException;

    public boolean checkConsistency() throws SolverException;
}
