package de.objectcode.soa.common.mfm.api.strategy;

import de.objectcode.soa.common.mfm.api.Component;
import de.objectcode.soa.common.mfm.api.UpgradeException;
import de.objectcode.soa.common.mfm.api.accessor.IDataAccessor;
import de.objectcode.soa.common.mfm.api.collector.IDataCollector;

public interface IUpgradeStrategy {

    public void upgradeComponent(Component component, IDataAccessor oldVersion, IDataCollector newVersion) throws UpgradeException;
}
