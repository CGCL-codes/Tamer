package com.modelmetrics.cloudconverter.migrate.struts2;

import com.modelmetrics.cloudconverter.migrate.DataMigrator;
import com.modelmetrics.cloudconverter.migrate.DataMigratorException;
import com.modelmetrics.common.sforce.dao.Sproxy;

public class MigrateVOBuilder {

    public MigrateVO build(DataMigrator dataMigrator, Sproxy element, MigrateContext migrateContext) throws DataMigratorException {
        MigrateVO vo = new MigrateVO();
        vo.setPreview(element.getValue(migrateContext.getPreviewField()));
        vo.setSource(element.getValue(migrateContext.getSourceField()));
        vo.setTarget(element.getValue(migrateContext.getTargetField()));
        vo.setResult(dataMigrator.migrate(vo.getSource(), vo.getTarget()));
        if (migrateContext.getDisposition() == SourceDispositionType.SET_TO_NULL) {
            vo.setNewSource(null);
        } else {
            vo.setNewSource(vo.getSource());
        }
        return vo;
    }
}
