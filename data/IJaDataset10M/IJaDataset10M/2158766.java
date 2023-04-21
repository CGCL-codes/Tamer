package org.in4ama.documentengine.request;

import java.util.List;
import java.util.Map;
import org.in4ama.datasourcemanager.exception.DataSourceException;
import org.in4ama.documentengine.evaluator.EvaluationContext;
import org.in4ama.documentengine.event.DocumentGenerationListener;
import org.in4ama.documentengine.event.PackGenerationListener;
import org.in4ama.documentengine.exception.EvaluationException;
import org.in4ama.documentengine.exception.InitializationException;
import org.in4ama.documentengine.exception.ProjectException;
import org.in4ama.documentengine.exception.RequestException;
import org.in4ama.documentengine.generator.DocumentRequest;
import org.in4ama.documentengine.generator.Generator;
import org.in4ama.documentengine.generator.PackRequest;
import org.in4ama.documentengine.generator.Request;
import org.in4ama.documentengine.generator.Response;

/** Handles a single request from the client */
public interface RequestHandler {

    /**
	 * Handles the specified request - the method should dispatch the request to
	 * the generator object.
	 * @throws DataSourceException 
	 * @throws EvaluationException 
	 * @throws InitializationException 
	 * @throws RequestException 
	 * @throws ProjectException 
	 */
    Response handle(Request request, Generator generator) throws DataSourceException, EvaluationException, InitializationException, RequestException, ProjectException;

    /**
	 * Handles the specified request - the method should dispatch the request to
	 * the generator object.
	 * @throws DataSourceException 
	 * @throws EvaluationException 
	 * @throws InitializationException 
	 * @throws RequestException 
	 * @throws ProjectException 
	 */
    Response handle(Request request, Generator generator, List<DocumentGenerationListener> documentListeners, List<PackGenerationListener> packListeners) throws DataSourceException, EvaluationException, InitializationException, RequestException, ProjectException;

    /**
	 * Gets a unique name of the request types that the this object should
	 * handle
	 */
    String getName();

    /**
	 * Creates an evaluation context for the specified pack request in the specified project.
	 * @throws InitializationException 
	 * @throws RequestException 
	 * @throws ProjectException 
	 */
    EvaluationContext getEvaluationContext(String projectName, PackRequest packRequest) throws InitializationException, RequestException, ProjectException;

    /**
	 * Creates an evaluation context for the specified document request in the specified project.
	 * @throws InitializationException 
	 * @throws RequestException 
	 * @throws ProjectException 
	 */
    EvaluationContext getEvaluationContext(String projectName, DocumentRequest documentRequest) throws InitializationException, RequestException, ProjectException;

    /**
	 * Creates an evaluation context for the specified list of parameters and the specified project.
	 * @throws InitializationException 
	 * @throws RequestException 
	 * @throws ProjectException 
	 */
    EvaluationContext getEvaluationContext(String projectName, Map<String, Object> parameters) throws InitializationException, RequestException, ProjectException;
}
