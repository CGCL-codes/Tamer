package trstudio.beansmetric.core.beansmetric;

/**
 * Représente un attribut.
 *
 * @author Sebastien Villemain
 */
public interface FieldElement extends ModifierElement, CompilationUnitElement {

    /**
	 * L'élement parent.
	 *
	 * @return
	 */
    public CompilationUnitElement getParentElement();
}
