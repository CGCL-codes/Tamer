package alice.cartago;

/**
 * Wrapper class implementing a CARTAGO environment
 * 
 */
class CartagoEnvironment implements ICartagoEnvironment {

    private EnvironmentKernel envKernel;

    private ICartagoLoggerManager logManager;

    /**
	 * Creates an  environment
	 *
	 * @param name logic name of the environment
	 */
    public CartagoEnvironment(String name) {
        logManager = new CartagoLoggerManager();
        envKernel = new EnvironmentKernel(name, logManager);
    }

    /**
	 * Create an  environment
	 *
	 * @param name logic name of the environment
	 */
    public CartagoEnvironment(String name, ICartagoLoggerManager logMan) {
        logManager = logMan;
        envKernel = new EnvironmentKernel(name, logManager);
    }

    /**
	 * Get a context to work inside the environment
	 * 
	 * @param aid Agent identifier
	 * @return
	 */
    public ICartagoContext join(String name) {
        return envKernel.getContext(name);
    }

    /**
	 * Create an artifact 
	 * 
	 * @param name name of the artifact
	 * @param template class of the artifact template
	 * @param params starting configuration parameters
	 * @return
	 * @throws ArtifactAlreadyPresentException if an artifact with the same logic name is present in the local environment
	 * @throws UnknownArtifactTemplateException if the artifact template is not known
	 * @throws ArtifactConfigurationFailedException if there are errors in init parameters
	 */
    public ArtifactId createArtifact(String name, Class template, ArtifactConfig param) throws ArtifactAlreadyPresentException, UnknownArtifactTemplateException, ArtifactConfigurationFailedException {
        return envKernel.createArtifact(name, template, param);
    }

    /**
	 * Create an artifact 
	 * 
	 * @param name name of the artifact
	 * @param template class of the artifact template
	 * @param params starting configuration parameters
	 * @return
	 * @throws ArtifactAlreadyPresentException if an artifact with the same logic name is present in the local environment
	 * @throws UnknownArtifactTemplateException if the artifact template is not known
	 * @throws ArtifactConfigurationFailedException if there are errors in init parameters
	 */
    public ArtifactId createArtifact(String name, Class template) throws ArtifactAlreadyPresentException, UnknownArtifactTemplateException, ArtifactConfigurationFailedException {
        return envKernel.createArtifact(name, template);
    }

    /**
	 * Destroy an artifact
	 * 
	 * @param id artifact identifier
	 * @throws SimpaException if errors occurrend in disposing the artifact
	 */
    public void disposeArtifact(ArtifactId id) throws CartagoException {
        envKernel.disposeArtifact(id);
    }

    public ArtifactId getFactoryId() {
        return envKernel.getFactoryId();
    }

    public ArtifactId getRegistryId() {
        return envKernel.getRegistryId();
    }

    public void registerLogger(ICartagoLogger logger) {
        logManager.registerLogger(logger);
    }

    public void unregisterLogger(ICartagoLogger logger) {
        logManager.unregisterLogger(logger);
    }
}
