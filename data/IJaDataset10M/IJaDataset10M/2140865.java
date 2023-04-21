package net.sf.gridarta.model.archetypetype;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * An {@link ArchetypeAttribute} for selecting text fields.
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author Andreas Kirschbaum
 */
public class ArchetypeAttributeText extends ArchetypeAttribute {

    /**
     * The terminating string.
     */
    @NotNull
    private final String endingOld;

    /**
     * The file extension.
     */
    @Nullable
    private final String fileExtension;

    /**
     * Creates a new instance.
     * @param archetypeAttributeName the archetype attribute name
     * @param endingOld the terminating string
     * @param attributeName the user interface attribute name
     * @param description the attribute's description
     * @param inputLength the input length in characters for text input fields
     * @param fileExtension the file extension
     */
    public ArchetypeAttributeText(@NotNull final String archetypeAttributeName, @NotNull final String endingOld, @NotNull final String attributeName, @NotNull final String description, final int inputLength, @Nullable final String fileExtension) {
        super(archetypeAttributeName, attributeName, description, inputLength, attributeName);
        this.endingOld = endingOld;
        this.fileExtension = fileExtension;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void visit(@NotNull final ArchetypeAttributeVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Returns the file extension.
     * @return the file extension
     */
    @Nullable
    public String getFileExtension() {
        return fileExtension;
    }
}
