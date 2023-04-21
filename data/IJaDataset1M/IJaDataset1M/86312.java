package org.objectwiz.plugin.uibuilder.runtime.result;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.objectwiz.core.criteria.Criteria;
import org.objectwiz.core.dataset.DataSet;
import org.objectwiz.core.dataset.DataSetBackedCollection;
import org.objectwiz.core.dataset.DataSetStructure;
import org.objectwiz.core.dataset.DataSetUtils;
import org.objectwiz.core.dataset.DatasetRow;
import org.objectwiz.core.dataset.FilteredDataset;
import org.objectwiz.core.dataset.TableDataSet;
import org.objectwiz.core.ui.rendering.DatasetRenderer;
import org.objectwiz.core.ui.rendering.DatasetRendererConfiguration;
import org.objectwiz.core.ui.rendering.DatasetRendererFactory;
import org.objectwiz.core.ui.rendering.DatasetRendererOperationHandler;
import org.objectwiz.core.ui.rendering.table.ColumnInformation;
import org.objectwiz.core.ui.rendering.table.TableOperationHandler;
import org.objectwiz.core.ui.rendering.table.TableView;
import org.objectwiz.plugin.async.AsyncFacet;
import org.objectwiz.plugin.async.AsyncFacetProxy;
import org.objectwiz.plugin.async.ProcessDescriptor;
import org.objectwiz.plugin.async.iop.dataset.DataSetExportProcessDescriptor;
import org.objectwiz.plugin.async.io.xsl.dataset.XLSDataSetExportFormatProxy;
import org.objectwiz.plugin.uibuilder.EvaluationContext;
import org.objectwiz.plugin.uibuilder.SecureAttributeBag;
import org.objectwiz.plugin.uibuilder.UIBuilderContextException;
import org.objectwiz.plugin.uibuilder.model.dataset.DatasetDescriptor;
import org.objectwiz.plugin.uibuilder.model.value.ValueFromDataset;
import org.objectwiz.plugin.uibuilder.renderer.DataOnlyDataSet;
import org.objectwiz.plugin.uibuilder.renderer.UIBuilderRendererFactory;
import org.objectwiz.plugin.uibuilder.renderer.UIBuilderRendererProxy;
import org.objectwiz.utils.MapUtils;

/**
 * Base class for all the results that display a dataset.
 *
 * Contains the logic for performing the basic operations on a dataset (reading
 * data, selecting objects, etc.).
 *
 * It relies on the rendering framework {@link org.objectwiz.core.ui.rendering}.
 *
 * @author Benoit Del Basso <benoit.delbasso at helmet.fr>
 */
public abstract class AbstractDisplayDatasetActionResult<E extends AbstractDisplayDatasetActionResult> extends ActionResult<E> {

    private static final Log logger = LogFactory.getLog(AbstractDisplayDatasetActionResult.class);

    private int bufSize = 20;

    private String title;

    private String header;

    private DatasetRow[] selectedRows;

    private Object[] selectedRowsIds;

    private Criteria selectionCriteria;

    private static final String GET_RENDERER_CONFIGS = "getRendererConfigs";

    private static final String GET_FILTER_STRUCTURE = "getFilterStructure";

    private static final String RENDERER_HANDLER = "handler";

    private static final String SIZE_OPERATION = "size";

    private static final String DATA_OPERATION = "data";

    private static final String DESCRIPTOR_ID = "descriptorId";

    private static final String CRITERIA = "criteria";

    private static final String EXPORT_OPERATION = "export";

    private static final String SELECT_OPERATION = "select";

    private static final String SELECTION_MIN_CARDINALITY = "minCardinality";

    private static final String SELECTION_MAX_CARDINALITY = "maxCardinality";

    private static final String COLUMN_TO_FETCH = "columnToFetch";

    protected AbstractDisplayDatasetActionResult() {
        super();
    }

    /**
     * Protected constructor for cloning the result.
     */
    protected AbstractDisplayDatasetActionResult(AbstractDisplayDatasetActionResult source) {
        super(source);
        this.bufSize = source.bufSize;
        this.title = source.title;
        this.header = source.header;
        this.selectedRows = source.selectedRows;
        this.selectedRowsIds = source.selectedRowsIds;
        this.selectionCriteria = source.selectionCriteria;
    }

    /**
     * Super constructor.
     *
     * @param ctx                        The current evaluation context.
     * @param descriptor                 The descriptor of the dataset
     * @param valueFromDataset           (optional) This parameter shall contain the {@link ValueFromDataset}
     *                                   that is being resolved by displaying the dataset to the user.
     * @param secureAttributes           The contextual attribute to store in the secure bag
     * @param nextActionAttributes       The bag containing attributes for the next action
     *                                   (the action that will be triggered by the selection
     *                                    of objects from the dataset).     
     */
    protected AbstractDisplayDatasetActionResult(EvaluationContext ctx, DatasetDescriptor descriptor, ValueFromDataset valueFromDataset, Map<String, Object> secureAttributes, SecureAttributeBag nextActionAttributes) {
        super(ctx, createSecureAttributes(descriptor, valueFromDataset, secureAttributes), nextActionAttributes);
        DataSet sourceDs = descriptor.createDataset(ctx);
        if (descriptor.getDescriptionTemplate() == null) {
            this.header = sourceDs.getHumanReadableDescription();
        } else {
            this.header = ctx.parse(descriptor.getDescriptionTemplate().getContent());
        }
    }

    private static Map<String, Object> createSecureAttributes(DatasetDescriptor descriptor, ValueFromDataset valueFromDataset, Map<String, Object> secureAttributes) {
        Map<String, Object> finalAttributes = new HashMap();
        if (secureAttributes != null) finalAttributes.putAll(secureAttributes);
        finalAttributes.put(DESCRIPTOR_ID, descriptor.getId());
        finalAttributes.put(SELECTION_MIN_CARDINALITY, valueFromDataset == null ? 0 : 1);
        finalAttributes.put(SELECTION_MAX_CARDINALITY, valueFromDataset == null ? 0 : 1);
        finalAttributes.put(COLUMN_TO_FETCH, valueFromDataset == null ? -1 : valueFromDataset.getColumnIndex());
        return finalAttributes;
    }

    /**
     * Returns a short sentence that can be displayed as a title for this action.
     */
    public String getTitle() {
        return title;
    }

    /**
     * Child classes should call this method to set the title of this result ;
     */
    protected final void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the header of the dataset.
     *
     * The header is a human-readable sentence that can be displayed to the user
     * to describe the current dataset.
     */
    public String getHeader() {
        return header;
    }

    /**
     * Returns the minimum cardinality that the selection must have to be eligible
     * for the selection operations.
     */
    public final int selectionMinCardinality() {
        return (Integer) getAttributeBag().getAttribute(SELECTION_MIN_CARDINALITY);
    }

    /**
     * Returns the maximum cardinality that the selection must have to be eligible
     * for the selection operations.
     */
    public final int selectionMaxCardinality() {
        return (Integer) getAttributeBag().getAttribute(SELECTION_MAX_CARDINALITY);
    }

    /**
     * (Client-side) Builds the renderers for this dataset, using the given factory.
     */
    public List<DatasetRenderer> buildRenderers() {
        DatasetRendererFactory rendererFactory = DatasetRendererFactory.instance();
        List<DatasetRenderer> renderers = new ArrayList();
        Map<String, DatasetRendererConfiguration> configs = (Map) execute(GET_RENDERER_CONFIGS, null);
        for (Map.Entry entry : configs.entrySet()) {
            String handlerClassName = (String) entry.getKey();
            DatasetRendererConfiguration config = (DatasetRendererConfiguration) entry.getValue();
            try {
                DatasetRenderer renderer = buildRenderer(rendererFactory, handlerClassName);
                renderer.setConfiguration(config);
                renderers.add(renderer);
            } catch (Exception e) {
                logger.warn("Error initializing renderer, skipping: " + handlerClassName, e);
            }
        }
        return renderers;
    }

    /**
     * (Client-side) Builds the renderer for a specific {@link DatasetRendererOperationHandler}.
     */
    protected DatasetRenderer buildRenderer(DatasetRendererFactory rendererFactory, String handlerClassName) throws Exception {
        UIBuilderRendererFactory handlerFactory = UIBuilderRendererFactory.instance();
        final Class operationsClass = rendererFactory.getOperationsApiClass(handlerClassName);
        if (operationsClass == null) {
            throw new Exception("Handler not available on classpath: " + handlerClassName);
        }
        UIBuilderRendererProxy uiBuilderHandlerProxy = handlerFactory.getUIBuilderRendererProxy(operationsClass);
        if (uiBuilderHandlerProxy == null) {
            throw new Exception("No UI builder proxy available for: " + operationsClass);
        }
        DatasetRendererOperationHandler handler = uiBuilderHandlerProxy.createClientSideHandler();
        DataSet exposedDatasetProxy = createExposedDatasetProxy(handlerClassName);
        handler.setDataSet(exposedDatasetProxy);
        return rendererFactory.create(handler);
    }

    /**
     * (Server-side) Creates a proxy to the "exposed" dataset on the server-side, for
     * the given handler.
     */
    protected DataSet createExposedDatasetProxy(final String handlerClassName) {
        return new DataOnlyDataSet() {

            @Override
            public long getRowCount(Criteria criteria) {
                Map<String, Object> params = new HashMap();
                params.put(RENDERER_HANDLER, handlerClassName);
                params.put(CRITERIA, criteria);
                return (Long) execute(SIZE_OPERATION, params);
            }

            @Override
            public Collection<DatasetRow> getDataBlock(Criteria criteria) {
                Map<String, Object> params = new HashMap();
                params.put(RENDERER_HANDLER, handlerClassName);
                params.put(CRITERIA, criteria);
                return (Collection<DatasetRow>) execute(DATA_OPERATION, params);
            }
        };
    }

    /**
     * Returns the description of the structure that can be used for filtering
     * the data.
     */
    public DataSetStructure getFilterStructure() {
        return (DataSetStructure) execute(GET_FILTER_STRUCTURE, null);
    }

    /**
     * Shortcut for {@link #onSelectRows(DatasetRow[])} with a single row.
     */
    public ActionResult onSelectRow(DatasetRow row) {
        return onSelectRows(new DatasetRow[] { row });
    }

    /**
     * Client-side helper for retrieving the action to perform after rows
     * were selected.
     * @param rows          The selected rows
     */
    public ActionResult onSelectRows(DatasetRow[] rows) {
        selectionCriteria = null;
        if (rows[0].getId() != null) {
            selectedRowsIds = DataSetUtils.getIds(rows);
        } else {
            selectedRows = rows;
        }
        return executeSelect(null);
    }

    /**
     * (Client-side) Helper for retrieving the action to perform after rows
     * were selected (not individually but using a {@link Criteria}).
     * @param criteria      The criteria that represents the selection
     */
    public ActionResult onSelectRows(Criteria criteria) {
        selectionCriteria = criteria;
        selectedRows = null;
        return executeSelect(null);
    }

    /**
     * Internal helper for child classes (triggers selection operation).
     */
    protected final ActionResult executeSelect(Map<String, Object> params) {
        return (ActionResult) execute(SELECT_OPERATION, params);
    }

    /**
     * Exports the objects of the dataset matching the given criteria
     * to a spreadsheet.
     */
    public ActionResult exportToSpreadsheet(Criteria criteria) {
        Map<String, Object> params = new HashMap();
        params.put(CRITERIA, criteria);
        return (ActionResult) execute(EXPORT_OPERATION, params);
    }

    /**
     * (Server-side) Implementation of {@link #exportToSpreadsheet(Criteria)}.
     */
    protected ActionResult performExport(EvaluationContext trustedCtx, Criteria criteria) {
        DataSet ds = createTableRendererDataset(trustedCtx, criteria);
        ColumnInformation[] columnsScheme = TableView.create(ds).buildDefaultColumnsInformation();
        ProcessDescriptor desc = new DataSetExportProcessDescriptor(ds, XLSDataSetExportFormatProxy.XLS_FORMAT, columnsScheme, this.getAttributeBag().getLocale());
        AsyncFacet async = AsyncFacetProxy.require(trustedCtx.getApplication());
        String processId = async.startAsynchroneousProcess(desc);
        return new ProcessRunningActionResult(trustedCtx, processId);
    }

    @Override
    public Object perform(String operationId, EvaluationContext trustedCtx, EvaluationContext parameters) throws Exception {
        if (SELECT_OPERATION.equals(operationId)) {
            return performOnSelect(trustedCtx, getCurrentSelection(trustedCtx));
        } else if (GET_FILTER_STRUCTURE.equals(operationId)) {
            return createFilteringStructure(trustedCtx, createServerSideDataset(trustedCtx));
        } else if (GET_RENDERER_CONFIGS.equals(operationId)) {
            return createRendererConfigs(trustedCtx);
        } else if (SIZE_OPERATION.equals(operationId)) {
            Criteria criteria = parameters.requireParameter(Criteria.class, CRITERIA);
            return createExposedDataset(trustedCtx, parameters).getRowCount(criteria);
        } else if (DATA_OPERATION.equals(operationId)) {
            Criteria criteria = parameters.requireParameter(Criteria.class, CRITERIA);
            return createExposedDataset(trustedCtx, parameters).getRows(criteria);
        } else if (EXPORT_OPERATION.equals(operationId)) {
            Criteria criteria = parameters.requireParameter(Criteria.class, CRITERIA);
            return performExport(trustedCtx, criteria);
        } else {
            throw new UnsupportedOperationException("Operation not supported: " + operationId);
        }
    }

    /**
     * Returns a {@link Collection} that represents the current selection (i.e. the objects
     * from the dataset that the user has chosen).
     * @param trustedCtx            Current trusted evaluation context.
     */
    public DataSetBackedCollection getCurrentSelection(EvaluationContext trustedCtx) {
        DataSet selectionDs = createDatasetFromSelection(trustedCtx);
        int selectionMinCardinality = getAttributeBag().getAttribute(Integer.class, SELECTION_MIN_CARDINALITY);
        int selectionMaxCardinality = getAttributeBag().getAttribute(Integer.class, SELECTION_MAX_CARDINALITY);
        int columnToFetch = getAttributeBag().getAttribute(Integer.class, COLUMN_TO_FETCH);
        long selectionSize = selectionDs.getRowCount();
        if (selectionSize < selectionMinCardinality || (selectionMaxCardinality > 0 && selectionSize > selectionMaxCardinality())) {
            throw new RuntimeException("Invalid cardinality for the selection: " + selectionSize);
        }
        return new DataSetBackedCollection(selectionDs, columnToFetch);
    }

    /**
     * Internal logic that creates a dataset from the data sent by the
     * {@link #createSelectionDescription()} method.
     */
    private DataSet createDatasetFromSelection(EvaluationContext trustedCtx) {
        if (selectionCriteria != null) {
            return new FilteredDataset(createServerSideDataset(trustedCtx), selectionCriteria);
        } else if (selectedRowsIds != null) {
            return DataSetUtils.createTableDataSet(createServerSideDataset(trustedCtx), selectedRowsIds);
        } else if (selectedRows != null) {
            DataSet ds = createServerSideDataset(trustedCtx);
            List<Object[]> untamperedData = ds.getUntamperedData(DataSetUtils.getData(selectedRows));
            return new TableDataSet(ds.getApplication(), ds.getStructure().getColumnTypes(), untamperedData);
        }
        throw new RuntimeException("All selection modes are undefined");
    }

    /**
     * Internal helper that creates the target {@link DataSet} based on information
     * from the secure context.
     */
    private DataSet createServerSideDataset(EvaluationContext trustedCtx) {
        int descriptorId = (Integer) getAttributeBag().getAttribute(DESCRIPTOR_ID);
        DatasetDescriptor descriptor = trustedCtx.getUIBuilder().getMetadataSource().getDatasetDescriptorById(descriptorId);
        return descriptor.createDataset(trustedCtx);
    }

    private UIBuilderRendererProxy createRendererProxy(EvaluationContext trustedCtx, String handlerName) {
        UIBuilderRendererFactory handlerFactory = UIBuilderRendererFactory.instance();
        Class handlerClass = DatasetRendererFactory.instance().getOperationsApiClass(handlerName);
        if (!createRendererConfigs(trustedCtx).containsKey(handlerName)) {
            throw new UIBuilderContextException("Access to renderer handler not allowed: " + handlerName);
        }
        return handlerFactory.getUIBuilderRendererProxy(handlerClass);
    }

    private DataSet createExposedDataset(EvaluationContext trustedCtx, EvaluationContext parameters) {
        String handlerName = parameters.requireParameter(String.class, RENDERER_HANDLER);
        UIBuilderRendererProxy rendererProxy = createRendererProxy(trustedCtx, handlerName);
        return rendererProxy.getExposedDataset(createServerSideDataset(trustedCtx));
    }

    private DataSet createTableRendererDataset(EvaluationContext trustedCtx, Criteria criteria) {
        DataSet ds = createServerSideDataset(trustedCtx);
        if (criteria != null) {
            ds = new FilteredDataset(ds, criteria);
        }
        return createRendererProxy(trustedCtx, TableOperationHandler.class.getName()).getExposedDataset(ds);
    }

    /**
     * Computes which columns should be exposed to the user (e.g. for filtering).
     * Unacessible columns must be removed from the dataset.
     */
    protected DataSetStructure createFilteringStructure(EvaluationContext ctx, DataSet sourceDs) {
        return createTableRendererDataset(ctx, null).getStructure();
    }

    /**
     * This method shall initialise the configuration corresponding to the renderers
     * that will be used on the client-side to display the dataset.
     *
     * The keys of the returned map is the name of the class of the {@link DatasetRendererOperationHandler}
     * corresponding to the renderer. Associated value is the configuration of
     * the renderer (may be null).
     *
     * Default implementation: table renderer only.
     */
    protected Map<String, DatasetRendererConfiguration> createRendererConfigs(EvaluationContext trustedCtx) {
        return MapUtils.singletonLinkedMap(TableOperationHandler.class.getName(), null);
    }

    /**
     * This method shall return the action that is triggered by the select operation.
     * @param trustedCtx     The operation context
     * @param selection      The list of the selected objects. If the selection was represented
     *                       by a dataset, then this list may not be fully initialized and may use
     *                       on-demand loading.
     */
    protected abstract ActionResult performOnSelect(EvaluationContext trustedCtx, DataSetBackedCollection selection);
}
