package com.wrupple.muba.catalogs.client.module.services.presentation.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.view.client.ProvidesKey;
import com.wrupple.muba.catalogs.client.module.services.logic.CatalogDescriptionService;
import com.wrupple.muba.catalogs.client.module.services.logic.CatalogEntryJoiningStrategy;
import com.wrupple.muba.catalogs.domain.CatalogEntry;
import com.wrupple.muba.common.client.application.DataCallback;
import com.wrupple.muba.common.domain.FilterData;
import com.wrupple.muba.common.shared.State.StorageManager;
import com.wrupple.vegetate.domain.CatalogDescriptor;
import com.wrupple.vegetate.domain.FieldDescriptor;
import com.wrupple.vegetate.domain.JsVegetateResultSet;

public abstract class CompositeDataProvider<T extends JavaScriptObject> extends SimpleFilterableDataProvider<T> {

    class UnblockOperations extends DataCallback<CatalogDescriptor> {

        List<FilterData> queue;

        public UnblockOperations() {
            super();
            queue = new ArrayList<FilterData>();
        }

        @Override
        public void execute() {
            descriptor = result;
            dispatchQueued();
        }

        private void dispatchQueued() {
            for (FilterData node : queue) {
                actuallyPerformFetch(node);
            }
        }

        public void addToQueue(FilterData filter) {
            queue.add(filter);
        }
    }

    CatalogEntryJoiningStrategy assemblyService;

    protected CatalogDescriptor descriptor;

    private CatalogDescriptionService descriptionService;

    private UnblockOperations unBlockOperations;

    private String[][] joins;

    public CompositeDataProvider(StorageManager storageManager, ProvidesKey<T> keyprovider, CatalogDescriptionService descriptionService, CatalogEntryJoiningStrategy assemblyService) {
        super(storageManager, keyprovider);
        this.assemblyService = assemblyService;
        this.descriptionService = descriptionService;
        unBlockOperations = new UnblockOperations();
        descriptor = null;
    }

    @Override
    public void setCatalog(String catalog) {
        super.setCatalog(catalog);
        descriptor = null;
        joins = null;
        descriptionService.loadCatalogDescriptor(catalog, unBlockOperations);
    }

    @Override
    protected void fetch(FilterData filter) {
        if (descriptor == null) {
            unBlockOperations.addToQueue(filter);
        } else {
            actuallyPerformFetch(filter);
        }
    }

    private void actuallyPerformFetch(FilterData filter) {
        String[][] joins = generateCatalogJoins();
        if (joins == null || joins.length == 0) {
            filter.setJoins(null);
        } else {
            filter.setJoins(joins);
        }
        super.fetch(filter);
    }

    private String[][] generateCatalogJoins() {
        assert descriptor != null : "Cannot generate joins from a null catalog descriptor";
        if (joins == null) {
            Collection<FieldDescriptor> fields = descriptor.getFields();
            List<FieldDescriptor> joinableFields = new ArrayList<FieldDescriptor>(fields.size());
            String foreignCatalog;
            String foreignField;
            String localField;
            for (FieldDescriptor field : fields) {
                foreignCatalog = field.getForeignCatalog();
                localField = field.getId();
                if (foreignCatalog != null && (field.isKey() || field.isEphemeral())) {
                    joinableFields.add(field);
                }
            }
            int size = joinableFields.size();
            String[][] regreso = new String[size][];
            FieldDescriptor field;
            for (int i = 0; i < size; i++) {
                field = joinableFields.get(i);
                foreignCatalog = field.getForeignCatalog();
                localField = null;
                foreignField = null;
                if (field.isKey()) {
                    localField = field.getId();
                    foreignField = getCatalogKeyFieldId(foreignCatalog);
                }
                if (field.isEphemeral()) {
                    localField = descriptor.getKeyField();
                    foreignField = getIncomingForeignJoinableFieldId(foreignCatalog);
                }
                regreso[i] = new String[] { foreignCatalog, foreignField, localField };
            }
            joins = regreso;
        }
        return joins;
    }

    @Override
    protected List<T> processRawEntries(List<CatalogEntry> result, FilterData filter) {
        if (filter.getJoins() == null) {
            return super.processRawEntries(result, filter);
        } else {
            String foreignCatalog;
            String foreignField;
            String[] join;
            String[][] joins = filter.getJoins();
            int resultSetSize = result.size();
            JsVegetateResultSet resultSet;
            for (int i = 1; i < resultSetSize; i++) {
                join = joins[i - 1];
                foreignCatalog = join[0];
                foreignField = join[1];
                resultSet = result.get(i).cast();
                resultSet.setName(foreignCatalog);
                resultSet.setJoinableField(foreignField);
            }
            return assemblyService.processJoinedEntries(descriptor, result);
        }
    }

    protected abstract String getIncomingForeignJoinableFieldId(String foreignCatalog);

    protected abstract String getCatalogKeyFieldId(String foreignCatalog);
}
