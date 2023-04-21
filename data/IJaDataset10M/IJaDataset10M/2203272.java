package cc.w3d.jawos.fangs.xcms.xcms.engines.xeDataCms;

import cc.w3d.jawos.fangs.xcms.xcms.engine.IXCmsEngine;
import cc.w3d.jawos.fangs.xcms.xcms.engines.xeDataCms.configurationManager.XEDataCmsConfigurator;
import cc.w3d.jawos.jinn.xjawosdata.xjawosdata.XJawosDataContainer;
import cc.w3d.jawos.jinn.xjawosdata.xjawosdata.core.XJawosData;
import cc.w3d.jawos.jinn.xjawosdata.xjawosdata.core.structure.Key;
import cc.w3d.jawos.jinn.xjawosdata.xjawosdata.core.structure.exceptions.ItemNotFoundException;
import cc.w3d.jawos.jinn.xuid.xuidGenerator.XUidGeneratorContainer;
import cc.w3d.jawos.jinn.xuid.xuidGenerator.core.XUidGenerator;
import cc.w3d.jawos.jinn.xuid.xuidGenerator.core.structure.Uid;

public class XEDataCms implements IXCmsEngine {

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
            XJawosData.update("DROP TABLE IF EXISTS #Entry");
            query = "CREATE TABLE #Entry (" + "		  `id`    BIGINT NOT NULL," + "		  `value` TEXT   NOT NULL," + "		  PRIMARY KEY(`id`)" + "		);";
            XJawosData.update(query);
        }
    }

    protected static class Entry {

        public Entry() {
        }

        public Entry(Uid id, String type) {
            this.id = id;
            this.value = type;
        }

        public Entry(Uid id) {
            this.id = id;
        }

        @Key
        Uid id;

        public Uid getId() {
            return id;
        }

        public void setId(Uid id) {
            this.id = id;
        }

        String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public String get(Uid id) {
        try {
            Entry result = XJawosData.objectFind(new Entry(id));
            return result.getValue();
        } catch (ItemNotFoundException e) {
            return "";
        }
    }

    public void set(Uid id, String value) {
        if (value == null || value.equals("")) XJawosData.objectDelete(new Entry(id)); else {
            try {
                XJawosData.objectFind(new Entry(id));
                XJawosData.objectUpdate(new Entry(id, value));
            } catch (ItemNotFoundException e) {
                XJawosData.objectInsert(new Entry(id, value));
            }
        }
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

    public IXCmsEngine getClone() {
        XEDataCms r = new XEDataCms();
        r.XJawosData = this.XJawosData.getReferencedClone();
        r.XUidGenerator = this.XUidGenerator.getReferencedClone();
        r.XJawosData.disableAuthomaticTransactionManagement();
        r.XUidGenerator.disableAuthomaticTransactionManagement();
        return r;
    }

    public Object getEngineConfigurationManager() {
        return new XEDataCmsConfigurator(new EngineHandler());
    }
}
