package net.sf.dynxform.form.data.consumer;

import net.sf.dynxform.exception.business.BusinessException;
import net.sf.dynxform.form.DataSource;
import net.sf.dynxform.form.data.ModuleType;
import net.sf.dynxform.form.data.module.SourceModule;
import net.sf.dynxform.form.data.module.helpers.ModuleValueHelper;
import net.sf.dynxform.form.schema.CalculatedValue;
import net.sf.dynxform.form.types.Report;

/**
 * net.sf.dynxform.form.data.consumer Feb 18, 2004 11:05:07 AM andreyp
 * Copyright (c) dynxform.sf.net. All Rights Reserved
 */
public final class GridColumnValue extends AbstractConsumerValue {

    private final String gridId;

    private final String gridColumn;

    public GridColumnValue(final CalculatedValue parameter) {
        super(ModuleType.GRID_DATA, true);
        this.gridId = parameter.getGridData().getGuid();
        this.gridColumn = parameter.getGridData().getColumn();
    }

    public final boolean update(final SourceModule module, final DataSource dataSource) throws BusinessException {
        final Report report = ModuleValueHelper.getReport(module, gridId);
        final SourceModule requestModule = dataSource.getDataModule(ModuleType.REQUEST);
        final int currentRow = Integer.parseInt(ModuleValueHelper.getParameter(requestModule, gridId, true));
        final String value = report.getColumnValue(gridColumn, currentRow);
        this.setValue(value);
        return getUpdateStatus(value);
    }
}
