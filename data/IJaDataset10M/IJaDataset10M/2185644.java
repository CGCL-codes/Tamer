package fugeOM.Common.Protocol;

import java.util.List;

/**
 * @see fugeOM.Common.Protocol.GenericEquipment
 */
public class GenericEquipmentDaoImpl extends fugeOM.Common.Protocol.GenericEquipmentDaoBase {

    public List getAllLatest(final int transform) {
        return super.getAllLatest(transform, "select geq from fugeOM.Common.Protocol.GenericEquipment as geq " + "join geq.auditTrail as audit " + "where audit.date = (select max(internalaudit.date) from fugeOM.Common.Protocol.GenericEquipment as internalgeq " + "  join internalgeq.auditTrail as internalaudit " + "  where internalgeq.endurant.id = geq.endurant.id)");
    }

    public List getAllLatestDummies(final int transform) {
        String dummy = "% Dummy%";
        return super.getAllLatestDummies(transform, "select geq from fugeOM.Common.Protocol.GenericEquipment as geq " + "join geq.auditTrail as audit " + "where geq.name like \'" + dummy + "\' " + "and audit.date = (select max(internalaudit.date) from fugeOM.Common.Protocol.GenericEquipment as internalgeq " + "  join internalgeq.auditTrail as internalaudit " + "  where internalgeq.endurant.id = geq.endurant.id)");
    }
}
