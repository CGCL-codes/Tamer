package net.sf.gridarta.var.atrinik.model.io;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.List;
import net.sf.gridarta.model.anim.AnimationObjects;
import net.sf.gridarta.model.archetypeset.ArchetypeSet;
import net.sf.gridarta.model.errorview.ErrorViewCategory;
import net.sf.gridarta.model.errorview.ErrorViewCollector;
import net.sf.gridarta.model.gameobject.GameObjectFactory;
import net.sf.gridarta.model.gameobject.MultiPositionData;
import net.sf.gridarta.model.io.AbstractArchetypeParser;
import net.sf.gridarta.model.io.GameObjectParser;
import net.sf.gridarta.var.atrinik.model.archetype.Archetype;
import net.sf.gridarta.var.atrinik.model.archetype.DefaultArchetypeBuilder;
import net.sf.gridarta.var.atrinik.model.gameobject.GameObject;
import net.sf.gridarta.var.atrinik.model.maparchobject.MapArchObject;
import net.sf.japi.swing.action.ActionBuilder;
import net.sf.japi.swing.action.ActionBuilderFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * The <code>ArchetypeParser</code> class handles the parsing of arches. It is a
 * class separated from ArchetypeSet because it is also involved in loading
 * arches in map files.
 * @author <a href="mailto:michael.toennies@nord-com.net">Michael Toennies</a>
 * @author <a href="mailto:andi.vogl@gmx.net">Andreas Vogl</a>
 * @author <a href="mailto:cher@riedquat.de">Christian Hujer</a>
 */
public class ArchetypeParser extends AbstractArchetypeParser<GameObject, MapArchObject, Archetype, DefaultArchetypeBuilder> {

    /**
     * Action Builder.
     */
    @NotNull
    private static final ActionBuilder ACTION_BUILDER = ActionBuilderFactory.getInstance().getActionBuilder("net.sf.gridarta");

    /**
     * The game object parser instance.
     */
    private final GameObjectParser<GameObject, MapArchObject, Archetype> gameObjectParser;

    /**
     * The {@link MultiPositionData} instance to query for multi-part objects.
     */
    @NotNull
    private final MultiPositionData multiPositionData;

    /**
     * The multi-shape ID of the currently parsed archetype.
     */
    private int multiShapeID = 0;

    /**
     * Creates an ArchetypeParser.
     * @param gameObjectParser the game object parser instance to use
     * @param animationObjects the animation objects instance to use
     * @param archetypeSet the archetype set
     * @param gameObjectFactory the factory for creating game objects
     * @param multiPositionData the multi position data to query for multi-part
     * objects
     */
    public ArchetypeParser(@NotNull final GameObjectParser<GameObject, MapArchObject, Archetype> gameObjectParser, @NotNull final AnimationObjects animationObjects, @NotNull final ArchetypeSet<GameObject, MapArchObject, Archetype> archetypeSet, @NotNull final GameObjectFactory<GameObject, MapArchObject, Archetype> gameObjectFactory, @NotNull final MultiPositionData multiPositionData) {
        super(new DefaultArchetypeBuilder(gameObjectFactory), animationObjects, archetypeSet);
        this.gameObjectParser = gameObjectParser;
        this.multiPositionData = multiPositionData;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void initParseArchetype() {
        multiShapeID = 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean isStartLine(@NotNull final String line) {
        return line.startsWith("Object ") || line.equals("Object");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean processLine(@NotNull final BufferedReader in, @NotNull final String line, @NotNull final String line2, @NotNull final DefaultArchetypeBuilder archetypeBuilder, @NotNull final ErrorViewCollector errorViewCollector, @NotNull final List<GameObject> invObjects) throws IOException {
        if (line.startsWith("arch ")) {
            final GameObject invObject = gameObjectParser.load(in, line, invObjects);
            assert invObject != null;
            archetypeBuilder.addLast(invObject);
            return true;
        }
        if (line.startsWith("mpart_id ")) {
            try {
                multiShapeID = Integer.parseInt(line.substring(9).trim());
                if (multiShapeID <= 0 || multiShapeID >= MultiPositionData.Y_DIM) {
                    errorViewCollector.addWarning(ErrorViewCategory.ARCHETYPE_INVALID, "Arch " + archetypeBuilder.getArchetypeName() + " mpart_id number is '" + line.substring(9) + '\'');
                }
            } catch (final NumberFormatException ignored) {
                errorViewCollector.addWarning(ErrorViewCategory.ARCHETYPE_INVALID, "Arch " + archetypeBuilder.getArchetypeName() + " has a invalid mpart_id (" + line.substring(9) + ')');
                archetypeBuilder.addObjectText(line);
            }
            return true;
        }
        if (line.startsWith("mpart_nr ")) {
            try {
                final int i = Integer.parseInt(line.substring(9).trim());
                archetypeBuilder.setMultiPartNr(i);
            } catch (final NumberFormatException ignored) {
                errorViewCollector.addWarning(ErrorViewCategory.ARCHETYPE_INVALID, "Arch " + archetypeBuilder.getArchetypeName() + " has a invalid mpart_nr (" + line.substring(9) + ')');
                archetypeBuilder.addObjectText(line);
            }
            return true;
        }
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finishParseArchetypePart(@Nullable final Archetype firstArch, @NotNull final Archetype archetype, @NotNull final ErrorViewCollector errorViewCollector) {
        archetype.setMultiShapeID(multiShapeID);
        if (firstArch != null && multiShapeID != firstArch.getMultiShapeID()) {
            errorViewCollector.addWarning(ErrorViewCategory.ARCHETYPE_INVALID, ACTION_BUILDER.format("logDefArchWithInvalidMpartId", archetype.getArchetypeName(), firstArch.getArchetypeName(), Integer.toString(multiShapeID), Integer.toString(firstArch.getMultiShapeID())));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void finishParseArchetype(@NotNull final Archetype archetype) {
        if (archetype.isMulti()) {
            calculateLowestMulti(archetype);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean addToPanel(final boolean isInternPath, @NotNull final String editorFolder, @NotNull final Archetype archetype) {
        return true;
    }

    /**
     * Calculate the lowest part of this multi-arch. This lowest part is needed
     * because in ISO view, the big image is drawn for it's lowest part, in
     * order to get the overlapping correct. <p/>
     * @param arch last tail part of this multi
     */
    private void calculateLowestMulti(final net.sf.gridarta.model.archetype.Archetype<GameObject, MapArchObject, Archetype> arch) {
        final Archetype head = arch.getHead();
        int minYOffset = multiPositionData.getYOffset(head.getMultiShapeID(), head.getMultiPartNr());
        for (net.sf.gridarta.model.archetype.Archetype<GameObject, MapArchObject, Archetype> tail = head.getMultiNext(); tail != null; tail = tail.getMultiNext()) {
            final int t = multiPositionData.getYOffset(tail.getMultiShapeID(), tail.getMultiPartNr());
            if (t < minYOffset) {
                minYOffset = t;
            }
        }
        for (net.sf.gridarta.model.archetype.Archetype<GameObject, MapArchObject, Archetype> part = head; part != null; part = part.getMultiNext()) {
            part.setLowestPart(multiPositionData.getYOffset(part.getMultiShapeID(), part.getMultiPartNr()) <= minYOffset);
        }
    }
}
