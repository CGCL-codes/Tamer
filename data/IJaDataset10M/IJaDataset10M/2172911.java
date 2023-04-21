package ch.sahits.model;

/**
 * This interface defines methods that are neede to access the JETemplate in a model
 * @author Andi Hotz
 * @since 1.2.0
 */
public interface IJETModel {

    /**
	 * @return the generatorJETemplate
	 */
    public abstract String getGeneratorJETemplate();

    /**
	 * @return the generatorBundle
	 */
    public abstract String getGeneratorBundle();
}
