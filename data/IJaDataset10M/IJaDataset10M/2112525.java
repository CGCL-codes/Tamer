package de.uniAugsburg.MAF.core;

import de.uniAugsburg.MAF.core.evaluator.IEvaluatorFacade;
import de.uniAugsburg.MAF.core.exceptions.CoreException;
import de.uniAugsburg.MAF.core.repository.IRepositoryFacade;
import de.uniAugsburg.MAF.core.util.parameters.CoreParameters;
import de.uniAugsburg.MAF.core.visualizer.IVisualizerFacade;

/**
 * The public interface to the framework which allows only limited access by
 * hiding internal methods..
 */
public interface IMAFCore {

    /**
	 * Returns the core parameters.
	 * 
	 * @return
	 */
    public CoreParameters getCoreParameters();

    /**
	 * Returns the repository facade.
	 * 
	 * @return
	 */
    public IRepositoryFacade getRepositoryFacade();

    /**
	 * Returns the evaluator facade.
	 * 
	 * @return
	 */
    public IEvaluatorFacade getEvaluatorFacade();

    /**
	 * Returns the visualizer facade.
	 * 
	 * @return
	 */
    public IVisualizerFacade getVisualizerFacade();

    /**
	 * Disposes the framework.
	 * 
	 * @throws CoreException
	 */
    public void dispose() throws CoreException;
}
