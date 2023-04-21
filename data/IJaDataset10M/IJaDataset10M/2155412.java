package fw4ex.markingscriptcompiler.markingscriptrepresentation;

import fw4ex.markingscriptcompiler.compiler.ICompilable;
import fw4ex.markingscriptcompiler.markingscriptrepresentation.chdir.ChDir;
import fw4ex.markingscriptcompiler.markingscriptrepresentation.commandline.AbstractCommand;
import fw4ex.markingscriptcompiler.markingscriptrepresentation.loop.AbstractLoop;

/**
 * Represents the main entities of a {@link MarkingScript}: <ul>
 *  <li>{@link AbstractLoop Loops}</li>
 *  <li>{@link ChDir Directory changes}</li>
 *  <li>{@link AbstractCommand Command}</li>
 * </ul>
 * An {@code AbstractEntity} is identified by its <code>name</code>. The
 * <code>name</code> allows to find a specific {@code AbstractEntity} in the
 * {@link MarkingScript}. <br/>
 * The <code>name</code> is not really necessary for the {@link AbstractCommand},
 * but it eases development to store it in the abstract top class. <br/>
 * Loops and directory changes can have a nested {@code AbstractEntity}. <br/>
 * This abstract class is also meant to ease the nesting.
 */
public abstract class AbstractEntity implements ICompilable {

    /** The name, unique identifier of the entity. */
    protected String name;

    /** Loops and ChDirs can have nested entities. */
    protected AbstractEntity nestedEntity;

    /**
     * The name, unique identifier of the entity.
     * @return The name, unique identifier of the entity.
     */
    public String getName() {
        return name;
    }

    /**
     * Indicates whether or not the instance has a nested
     * {@code AbstractEntity}.
     * @return <code>true</code> if the instance has a nested
     * {@code AbstractEntity}, <code>false</code> otherwise.
     */
    public boolean hasNestedEntity() {
        return nestedEntity != null;
    }

    /**
     * Gets the nested {@code AbstractEntity} of the instance. If it does not
     * have a nested {@code AbstractEntity}, then <code>null</code> is returned.
     * @return The nested {@code AbstractEntity} of the instance, or
     * <code>null</code> if it does not have a nested {@code AbstractEntity}.
     */
    public AbstractEntity getNestedEntity() {
        return nestedEntity;
    }

    /**
     * Sets the given {@code AbstractEntity} as the nested
     * {@code AbstractEntity} of the instance.
     * @param nestedEntity The {@code AbstractEntity} that will become the
     * nested {@code AbstractEntity} of the instance.
     */
    public void setNestedEntity(AbstractEntity nestedEntity) {
        this.nestedEntity = nestedEntity;
    }
}
