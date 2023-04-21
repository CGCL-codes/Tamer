package org.objectwiz.plugin.uibuilder;

import java.util.Map;
import org.objectwiz.core.Facet;
import org.objectwiz.core.dataset.DataSet;
import org.objectwiz.plugin.customview.model.ApplicationSettings;
import org.objectwiz.core.facet.customization.BinaryResource;
import org.objectwiz.plugin.uibuilder.model.Board;
import org.objectwiz.plugin.uibuilder.model.Component;
import org.objectwiz.plugin.uibuilder.model.UIBuilderGeneralSettings;
import org.objectwiz.plugin.uibuilder.model.widget.Widget;
import org.objectwiz.plugin.uibuilder.model.action.Action;
import org.objectwiz.plugin.uibuilder.model.layout.LayoutInfo;
import org.objectwiz.plugin.uibuilder.model.widget.ActionWidget;
import org.objectwiz.plugin.uibuilder.runtime.result.ActionResult;
import org.objectwiz.plugin.uibuilder.runtime.result.OpenBoardActionResult;
import org.objectwiz.plugin.uibuilder.runtime.ParsedBoard;
import org.objectwiz.plugin.uibuilder.runtime.result.ProcessRunningActionResult;
import org.objectwiz.plugin.uibuilder.runtime.widget.ParsedWidget;

/**
 * <b>UI Builder</b>: a module for generating dynamic administration panels based on
 * all the basic operations (creation/edition, listing, method call, processes...)
 * available in Objectwiz. An implementation of this interface is associated to a
 * specific application.
 *
 * <h1>User interface metadata model</h1>
 *
 * <p>A {@link UIMetadataSource} provides metadata describing a basic user-centric
 * interface for interacting with the target application (both data and business logic).</p>
 *
 * <p>The user interface is composed of {@link Board}s that contains {@link Widget}s
 * organized upon a certain {@link LayoutInfo}. Both boards and widgets are {@link Component}s
 * and thus can be associated with an icon, input parameters. Widgets can be restricted to
 * certain security roles.</p>
 *
 * <p>Widgets may only display data or provide <emp>{@link Action}s</emp> to perform
 * operations on the objects.</p>
 *
 * <p>More details about the metadata model can be found in {@link UIMetadataSource}.</p>
 *
 * <h1>The builder at runtime</h1>
 *
 * <h2>Retrieving the first board</h2>
 *
 * One can call {@link #getDefaultBoard()} to open the first board of the application.
 * It returns an {@link OpenBoardActionResult} which contains a {@link ParsedBoard} (the runtime
 * equivalent of a {@link Board}).
 *
 * Such {@link ParsedBoard} contains {@link ParsedWidgets} which are the runtime equivalents of {@link Widget}s
 * (i.e. widgets that have been evaluated against the {@link EvaluationContext}, thus the templates,
 * runtime values, etc. have been fully resolved).
 *
 * <h2>Action widgets, and results</h2>
 *
 * The UIBuilder provides the client with a token for each {@link ActionWidget}. This token
 * ({@link SecureAttributeBag}) can be used on the {@link #triggerActionWidget(SecureAttributeBag,Map)}
 * method to perform the corresponding action. The outcome of the action is represented by
 * an {@link ActionResult}.
 *
 * Such result describes which user interaction should be performed (example: opening a new board,
 * displaying a message, showing an edition form, showing progress information, etc.).
 *
 * The {@link ActionResult}s may perform additional client-server operations by triggering the
 * {@link #triggerActionResultOperation(ActionResult, String, Map)}.
 *
 * Example: the {@link ProcessRunningActionResult} can pool the builder at a fixed interval
 * to check whether the process is still running or ended, and then retrieve the outcome
 * of the process by another operation.
 *
 * IMPORTANT NOTE: one may not use directly this method but rather use
 * {@link ActionResult#execute(String,Map)}. For more details, please refer to the
 * documentation of {@link ActionResult}.
 *
 * <h1>Binary/media resources</h1>
 *
 * The UI metadata defines icons, images, and other binary/media resources.
 *
 * These resources can be loaded using the two methods: {@link #getBinaryResourcesCount()}
 * and {@link #getBinaryResources(int, int)}.
 *
 * There are two specific resources associated to the metadata of one application:
 * the header image ({@link #getHeaderImageId()} and the stylesheet {@link #getStylesheetId()}.
 * For more information about these two resources, please refer to {@link UIBuilderGeneralSettings}.
 *
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
public interface UIBuilderFacet extends Facet {

    /**
     * Returns the default board for the current user.
     *
     * If such board does not exist, this method will return null. If secure
     * mode this implies that the user cannot access the application.
     *
     * @return the default board (if any)
     */
    public OpenBoardActionResult getDefaultBoard();

    /**
     * Executes one {@link Action}.
     *
     * @param actionContext     The bag with: the ID of the {@link Action}, the widget
     *
     *                          (which widget was activated, with which parameters...). Such context can be retrieve by calling
     *                          {@link ActionWidget#getContextBag()}.
     * @param dependenciesInfo  Context from other components which this this widget depends on.
     *                          The key of the map is the ID of the {@link Widget} that provides
     *                          the context.
     * @return a descriptor ({@link ActionResult} indicating what the UI should
     * do (open a new board, display a list, etc.).
     */
    public ActionResult triggerAction(SecureAttributeBag actionContext, Map<Integer, Map<String, Object>> dependenciesInfo);

    /**
     * Executes a secondary action. Such an action is linked to a primary action
     * and takes the result of it as an input.
     *
     * This method should normally only be called by {@link ActionResult} helpers
     * since the call to this method is dispatched to the {@link ActionResult} after
     * security checks.
     *
     * @param actionResult  The result of the primary action (contains the context of the action)
     * @param operationId   The ID of the secondary action to perform
     * @param params        Parameters of the secondary actions
     * @return an object (to be processed by the {@link ActionResult} that calls this method).
     */
    public Object triggerActionResultOperation(ActionResult actionResult, String operationId, Map<String, Object> params);

    /**
     * Returns the number of binary resources that are available the application.
     */
    public int getBinaryResourcesCount();

    /**
     * Returns the map <binary_resouce_id,binary_resouce_version>.
     * This is used to compare what is in the database and what the user has in his local cache.
     */
    public Map<Integer, Integer> getBinaryResourcesVersions();

    /**
     * Get a {@link BinaryResource} from its id.
     */
    public BinaryResource getBinaryResourceById(int id);

    /**
     * Returns the collection of binary resources that are defined in the
     * configuration of the UIBuilder for the current application.
     */
    public BinaryResource[] getBinaryResources(int firstOffset, int count);

    /**
     * Returns the ID of the binary resource to use as header image.
     * @return NULL if no image was defined as header image.
     */
    public Integer getHeaderImageId();

    /**
     * Returns the ID of the binary resource to use as a stylesheet.
     * @return NULL if no stylesheet was defined.
     */
    public Integer getStylesheetId();
}
