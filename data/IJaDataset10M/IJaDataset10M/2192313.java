package net.sf.gridarta.gui.map.renderer;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import net.sf.gridarta.model.archetype.Archetype;
import net.sf.gridarta.model.gameobject.GameObject;
import net.sf.gridarta.model.io.GameObjectParser;
import net.sf.gridarta.model.maparchobject.MapArchObject;
import net.sf.gridarta.model.mapmodel.MapModel;
import net.sf.gridarta.model.mapmodel.MapSquare;
import net.sf.gridarta.model.validation.errors.ValidationError;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Abstract base class for classes implementing {@link MapRenderer}.
 * @author Andreas Kirschbaum
 */
public abstract class AbstractMapRenderer<G extends GameObject<G, A, R>, A extends MapArchObject<A>, R extends Archetype<G, A, R>> extends JComponent implements MapRenderer {

    /**
     * The serial version UID.
     */
    private static final long serialVersionUID = 1L;

    /**
     * The MapSquares that are known to contain errors.
     */
    @NotNull
    private Map<MapSquare<G, A, R>, ValidationError<G, A, R>> erroneousMapSquares = Collections.emptyMap();

    /**
     * Used to avoid creation millions of points.
     */
    @NotNull
    private final Point tmpPoint = new Point();

    /**
     * The rendered {@link MapModel}.
     */
    @NotNull
    private final MapModel<G, A, R> mapModel;

    /**
     * The {@link GameObjectParser} for creating tooltip information or
     * <code>null</code>.
     */
    @Nullable
    private final GameObjectParser<G, A, R> gameObjectParser;

    /**
     * Creates a new instance.
     * @param mapModel the rendered map model
     * @param gameObjectParser the game object parser for generating tooltip
     * information or <code>null</code>
     */
    protected AbstractMapRenderer(@NotNull final MapModel<G, A, R> mapModel, @Nullable final GameObjectParser<G, A, R> gameObjectParser) {
        this.mapModel = mapModel;
        this.gameObjectParser = gameObjectParser;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printFullImage(@NotNull final File file) throws IOException {
        ImageIO.write(getFullImage(), "png", file);
    }

    /**
     * Sets the MapSquares that are known to contain errors.
     * @param erroneousMapSquares the MapSquares that are known to contain
     * errors
     */
    public void setErroneousMapSquares(@NotNull final Map<MapSquare<G, A, R>, ValidationError<G, A, R>> erroneousMapSquares) {
        this.erroneousMapSquares = new HashMap<MapSquare<G, A, R>, ValidationError<G, A, R>>(erroneousMapSquares);
    }

    /**
     * Must be called when this renderer is not used anymore.
     */
    public abstract void closeNotify();

    /**
     * {@inheritDoc}
     * @noinspection RefusedBequest
     */
    @Nullable
    @Override
    public String getToolTipText(@NotNull final MouseEvent event) {
        final Point mapLocation = getSquareLocationAt(event.getPoint(), tmpPoint);
        if (mapLocation == null) {
            return null;
        }
        final MapSquare<G, A, R> mapSquare = mapModel.getMapSquare(mapLocation);
        final ToolTipAppender<G, A, R> toolTipAppender = new ToolTipAppender<G, A, R>(gameObjectParser);
        for (final G gameObject : mapSquare.reverse()) {
            toolTipAppender.appendGameObject(gameObject, false, "");
        }
        final ValidationError<G, A, R> error = erroneousMapSquares.get(mapSquare);
        if (error != null) {
            toolTipAppender.appendValidationError(error);
        }
        return toolTipAppender.finish();
    }
}
