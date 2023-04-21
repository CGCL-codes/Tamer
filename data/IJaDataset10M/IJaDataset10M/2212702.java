package mimosa.table;

/**
 *
 * @author Jean-Pierre Muller
 */
@SuppressWarnings("serial")
public class DeclarationInheritanceException extends Exception {

    private Object key;

    private ValueDescription<? extends TypeDescription> description;

    private ValueDescription<? extends TypeDescription> inheritedDescription;

    /**
	 * For internal use within the package
	 */
    public DeclarationInheritanceException(Object key, ValueDescription<? extends TypeDescription> description, ValueDescription<? extends TypeDescription> inheritedDescription) {
        super();
        this.key = key;
        this.description = description;
        this.inheritedDescription = inheritedDescription;
    }

    /**
	 * For external use
	 */
    @SuppressWarnings("unchecked")
    public DeclarationInheritanceException(Object key, int card, TypeDescription type, int inheritedCard, TypeDescription inheritedType) {
        super();
        this.key = key;
        this.description = new ValueDescription(card, type);
        this.inheritedDescription = new ValueDescription(inheritedCard, inheritedType);
    }

    /**
	 * @return Returns the description.
	 */
    public ValueDescription<? extends TypeDescription> getDescription() {
        return description;
    }

    /**
	 * @return Returns the inheritedDescription.
	 */
    public ValueDescription<? extends TypeDescription> getInheritedDescription() {
        return inheritedDescription;
    }

    /**
	 * @return Returns the source.
	 */
    public Object getKey() {
        return key;
    }
}
