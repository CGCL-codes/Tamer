package de.objectcode.soa.common.mfm.api.strategy;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import de.objectcode.soa.common.mfm.api.Component;
import de.objectcode.soa.common.mfm.api.DowngradeException;
import de.objectcode.soa.common.mfm.api.UpgradeException;
import de.objectcode.soa.common.mfm.api.accessor.IDataAccessor;
import de.objectcode.soa.common.mfm.api.collector.IDataCollector;

@XmlType(name = "rename-strategy")
@Entity
@DiscriminatorValue("R")
public class RenameStrategy extends TransferStrategyBase implements IUpgradeStrategy, IDowngradeStrategy {

    String oldName;

    @XmlAttribute(name = "old-name")
    @Column(name = "OLD_NAME")
    public String getOldName() {
        return oldName;
    }

    public void setOldName(String oldName) {
        this.oldName = oldName;
    }

    public void upgradeComponent(Component component, IDataAccessor oldVersion, IDataCollector newVersion) throws UpgradeException {
        transferUp(component.getType(), component.isArray(), oldVersion, oldName, newVersion, component.getName());
    }

    public void downgradeComponent(Component component, IDataCollector oldVersion, IDataAccessor newVersion) throws DowngradeException {
        transferDown(component.getType(), component.isArray(), newVersion, component.getName(), oldVersion, oldName);
    }
}
