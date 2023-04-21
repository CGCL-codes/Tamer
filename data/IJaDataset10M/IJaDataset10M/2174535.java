package cc.w3d.jawos.fangs.xresources.xresources.engines.xeDataResources;

import java.util.List;
import cc.w3d.jawos.fangs.xresources.xresources.engine.IXResourcesEngine;
import cc.w3d.jawos.fangs.xresources.xresources.engines.xeDataResources.configurationManager.XEDataResourcesConfigurator;
import cc.w3d.jawos.jinn.xjawosdata.xjawosdata.XJawosDataContainer;
import cc.w3d.jawos.jinn.xjawosdata.xjawosdata.core.XJawosData;
import cc.w3d.jawos.jinn.xjawosdata.xjawosdata.core.structure.Key;
import cc.w3d.jawos.jinn.xuid.xuidGenerator.XUidGeneratorContainer;
import cc.w3d.jawos.jinn.xuid.xuidGenerator.core.XUidGenerator;
import cc.w3d.jawos.jinn.xuid.xuidGenerator.core.structure.Uid;

public class XEDataResources implements IXResourcesEngine {

    XJawosData XJawosData = XJawosDataContainer.XJawosData.getReferencedClone();

    XUidGenerator XUidGenerator = XUidGeneratorContainer.XUidGenerator.getReferencedClone();

    {
        XJawosData.disableAuthomaticTransactionManagement();
        XUidGenerator.disableAuthomaticTransactionManagement();
    }

    public class EngineHandler {

        public void setReference(String reference) {
            XJawosData.setReference(reference);
        }

        public XJawosData getXJawosData() {
            return XJawosData;
        }

        public void setXJawosData(XJawosData xJawosData) {
            XJawosData = xJawosData;
        }

        public XUidGenerator getXUidGenerator() {
            return XUidGenerator;
        }

        public void setXUidGenerator(XUidGenerator xUidGenerator) {
            XUidGenerator = xUidGenerator;
        }

        public void format() {
            String query;
            XJawosData.update("DROP TABLE IF EXISTS #Resource");
            query = "CREATE TABLE #Resource (" + "		  `resourceId`       BIGINT NOT NULL," + "		  `parentResourceId` BIGINT NOT NULL," + "		  `type`             INTEGER        ," + "		  PRIMARY KEY(`resourceId`)" + "		);";
            XJawosData.update(query);
        }
    }

    protected static class Resource {

        public Resource() {
        }

        public Resource(Uid resourceId, Uid parentResourceId, Integer type) {
            this.resourceId = resourceId;
            this.parentResourceId = parentResourceId;
            this.type = type;
        }

        public Resource(Uid resourceId) {
            this.resourceId = resourceId;
        }

        @Key
        Uid resourceId;

        public Uid getResourceId() {
            return resourceId;
        }

        public void setResourceId(Uid resourceId) {
            this.resourceId = resourceId;
        }

        Uid parentResourceId;

        public Uid getParentResourceId() {
            return parentResourceId;
        }

        public void setParentResourceId(Uid parentResourceId) {
            this.parentResourceId = parentResourceId;
        }

        Integer type;

        public Integer getType() {
            return type;
        }

        public void setType(Integer type) {
            this.type = type;
        }
    }

    public Uid addResource(Uid parentResourceId, Integer type) {
        Uid resourceId = XUidGenerator.generate();
        XJawosData.objectInsert(new Resource(resourceId, parentResourceId, type));
        return resourceId;
    }

    public void removeResource(Uid resourceId) {
        XJawosData.objectDelete(new Resource(resourceId));
    }

    public Uid getResourceParent(Uid resourceId) {
        Resource resource = XJawosData.objectFind(new Resource(resourceId));
        return resource.getParentResourceId();
    }

    public Integer getResourceType(Uid resourceId) {
        Resource resource = XJawosData.objectFind(new Resource(resourceId));
        return resource.getType();
    }

    public void setResourceType(Uid resourceId, Integer type) {
        Resource resource = XJawosData.objectFind(new Resource(resourceId));
        resource.setType(type);
        XJawosData.objectUpdate(resource);
    }

    public Uid[] getChilds(Uid id) {
        Uid r[];
        List<Resource> resources;
        resources = XJawosData.query("SELECT * FROM #Resource WHERE ParentResourceId = " + id.toLong() + "", Resource.class);
        r = new Uid[resources.size()];
        int i = 0;
        for (Resource resource : resources) {
            r[i++] = resource.getResourceId();
        }
        return r;
    }

    public Uid[] getChilds(Uid id, boolean positivo, Integer[] filter) {
        Uid r[];
        List<Resource> resources;
        String f = " AND type " + (positivo ? "" : "NOT ") + "IN (";
        for (int i = 0; i < filter.length - 1; i++) {
            f += filter[i] + ", ";
        }
        if (filter.length == 0) f = ""; else f += filter[filter.length - 1] + ")";
        resources = XJawosData.query("SELECT * FROM #Resource WHERE ParentResourceId = " + id.toLong() + "" + f, Resource.class);
        r = new Uid[resources.size()];
        int i = 0;
        for (Resource resource : resources) {
            r[i++] = resource.getResourceId();
        }
        return r;
    }

    public void comitTransaction(Object transaction) {
        XUidGenerator.comitTransaction();
        XJawosData.comitTransaction();
    }

    public Object initTransaction() {
        XJawosData.initTransaction();
        XUidGenerator.initTransaction();
        return null;
    }

    public void rollbackTransaction(Object transaction) {
        XUidGenerator.rollbackTransaction();
        XJawosData.rollbackTransaction();
    }

    public IXResourcesEngine getClone() {
        XEDataResources r = new XEDataResources();
        r.XJawosData = this.XJawosData.getReferencedClone();
        r.XUidGenerator = this.XUidGenerator.getReferencedClone();
        r.XJawosData.disableAuthomaticTransactionManagement();
        r.XUidGenerator.disableAuthomaticTransactionManagement();
        return r;
    }

    public Object getEngineConfigurationManager() {
        return new XEDataResourcesConfigurator(new EngineHandler());
    }
}
