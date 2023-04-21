package shellkk.qiq.jdm.test;

import java.util.HashMap;
import javax.datamining.NamedObject;
import javax.datamining.base.Model;
import javax.datamining.data.AttributeDataType;
import javax.datamining.data.CategorySet;
import javax.datamining.data.CategorySetFactory;
import javax.datamining.data.PhysicalDataRecord;
import javax.datamining.data.PhysicalDataRecordFactory;
import javax.datamining.data.PhysicalDataSet;
import javax.datamining.data.PhysicalDataSetFactory;
import javax.datamining.modeldetail.tree.TreeModelDetail;
import javax.datamining.modeldetail.tree.TreeNode;
import javax.datamining.resource.Connection;
import javax.datamining.resource.ConnectionFactory;
import javax.datamining.supervised.classification.ClassificationApplyContent;
import javax.datamining.supervised.classification.ClassificationApplySettings;
import javax.datamining.supervised.classification.ClassificationApplySettingsFactory;
import javax.datamining.supervised.classification.CostMatrix;
import javax.datamining.supervised.classification.CostMatrixFactory;
import javax.datamining.task.apply.DataSetApplyTask;
import javax.datamining.task.apply.DataSetApplyTaskFactory;
import javax.datamining.task.apply.RecordApplyTask;
import javax.datamining.task.apply.RecordApplyTaskFactory;
import org.apache.log4j.PropertyConfigurator;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import shellkk.qiq.jdm.data.Instance;
import shellkk.qiq.jdm.data.PhysicalDataRecordImpl;
import shellkk.qiq.jdm.data.PhysicalDataSetImpl;
import shellkk.qiq.jdm.engine.DataMiningEngine;
import shellkk.qiq.jdm.engine.uriaccess.JVMDataStore;
import shellkk.qiq.jdm.task.apply.RecordApplyTaskImpl;

public class TestTreeClsApply {

    public static void main(String[] args) {
        ApplicationContext context = null;
        try {
            PropertyConfigurator.configure("log4j.properties");
            context = new ClassPathXmlApplicationContext("jdm-config.xml");
            ConnectionFactory connFactory = (ConnectionFactory) context.getBean("dme-factory");
            Connection conn = connFactory.getConnection();
            long begin = System.currentTimeMillis();
            JVMDataStore store = (JVMDataStore) context.getBean("qiq_jvm");
            PhysicalDataSetImpl metaData = new PhysicalDataSetImpl();
            metaData.addAttribute("a", AttributeDataType.doubleType);
            metaData.addAttribute("b", AttributeDataType.doubleType);
            metaData.addAttribute("a0", AttributeDataType.doubleType);
            metaData.addAttribute("b0", AttributeDataType.doubleType);
            Instance[] instances = new Instance[1000];
            for (int i = 0; i < instances.length; i++) {
                double x1 = Math.random();
                double x2 = Math.random();
                Instance inst = new Instance(metaData);
                inst.setValue("a", x1);
                inst.setValue("b", x2);
                inst.setValue("a0", x1);
                inst.setValue("b0", x2);
                instances[i] = inst;
            }
            store.setData("testCls1", instances);
            PhysicalDataSetFactory dataFactory = (PhysicalDataSetFactory) conn.getFactory(PhysicalDataSet.class.getName());
            PhysicalDataSet data = dataFactory.create("jvm://testCls1", true);
            String dataName = "testCls1";
            conn.saveObject(dataName, data, true);
            String modelName = "treeclsModel1";
            conn.requestModelLoad(modelName);
            Model model = (Model) conn.retrieveObject(modelName, NamedObject.model);
            TreeModelDetail md = (TreeModelDetail) model.getModelDetail();
            CategorySetFactory csfactory = (CategorySetFactory) conn.getFactory(CategorySet.class.getName());
            CategorySet cs = csfactory.create(AttributeDataType.stringType);
            cs.addCategory("a", null);
            cs.addCategory("b", null);
            CostMatrixFactory cmfactory = (CostMatrixFactory) conn.getFactory(CostMatrix.class.getName());
            CostMatrix costMatrix = cmfactory.create(cs);
            conn.saveObject("cm", costMatrix, true);
            ClassificationApplySettingsFactory settingsFactory = (ClassificationApplySettingsFactory) conn.getFactory(ClassificationApplySettings.class.getName());
            ClassificationApplySettings settings = settingsFactory.create();
            settings.mapTopPrediction(ClassificationApplyContent.predictedCategory, "f");
            settings.mapTopPrediction(ClassificationApplyContent.nodeId, "nodeId");
            HashMap sdmap = new HashMap();
            sdmap.put("a", "a");
            sdmap.put("b", "b");
            sdmap.put("a0", "aa");
            sdmap.put("b0", "bb");
            settings.setSourceDestinationMap(sdmap);
            settings.setCostMatrixName("cm");
            String settingsName = "applySettings";
            conn.saveObject(settingsName, settings, true);
            HashMap datamap = new HashMap();
            datamap.put("a", "x1");
            datamap.put("b", "x2");
            String outputData = "predict";
            DataSetApplyTaskFactory taskFactory1 = (DataSetApplyTaskFactory) conn.getFactory(DataSetApplyTask.class.getName());
            DataSetApplyTask task1 = taskFactory1.create(dataName, modelName, settingsName, "jvm://" + outputData);
            task1.setApplyDataMap(datamap);
            conn.execute(task1, null);
            double error1 = 0;
            Instance[] dataresults = store.getData(outputData);
            for (Instance inst : dataresults) {
                double x1 = (Double) inst.getValue("aa");
                double x2 = (Double) inst.getValue("bb");
                String f = (String) inst.getValue("f");
                String yi = foo(x1, x2);
                double ei = f.equals(yi) ? 0 : 1;
                error1 += ei;
            }
            error1 = error1 / 1000;
            RecordApplyTaskFactory taskFactory = (RecordApplyTaskFactory) conn.getFactory(RecordApplyTask.class.getName());
            RecordApplyTaskImpl task = (RecordApplyTaskImpl) taskFactory.create(null, modelName, settingsName);
            task.setApplyDataMap(datamap);
            PhysicalDataRecordFactory recordFactory = (PhysicalDataRecordFactory) conn.getFactory(PhysicalDataRecord.class.getName());
            for (int i = 0; i < 1000; i++) {
                double x1 = Math.random();
                double x2 = Math.random();
                PhysicalDataRecordImpl record = (PhysicalDataRecordImpl) recordFactory.create();
                record.setValue("a", x1);
                record.setValue("b", x2);
                task.getInputRecords().add(record);
            }
            String recordApplyTaskName = "testRecordTaskApply";
            conn.saveObject(recordApplyTaskName, task, true);
            conn.execute(recordApplyTaskName).waitForCompletion(3600);
            task = (RecordApplyTaskImpl) conn.retrieveObject(recordApplyTaskName, NamedObject.task);
            double error = 0;
            for (int i = 0; i < 1000; i++) {
                PhysicalDataRecord inputRecord = task.getInputRecords().get(i);
                PhysicalDataRecord outputRecord = task.getOutputRecords().get(i);
                double x1 = (Double) inputRecord.getValue("a");
                double x2 = (Double) inputRecord.getValue("b");
                String a = (String) outputRecord.getValue("a");
                String b = (String) outputRecord.getValue("b");
                String yi = foo(x1, x2);
                String pi = (String) outputRecord.getValue("f");
                Number nodeId = (Number) outputRecord.getValue("nodeId");
                TreeNode node = md.getNode(nodeId.intValue());
                System.out.println("x1=" + x1 + ", x2=" + x2);
                System.out.println(a + ", " + b);
                System.out.println(node.getRule().translate());
                double ei = pi.equals(yi) ? 0 : 1;
                error += ei;
            }
            error = error / 1000;
            long end = System.currentTimeMillis();
            System.out.println("data apply expect risk:" + error1);
            System.out.println("record apply expect risk:" + error);
            System.out.println("apply time used:" + (end - begin));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            ((DataMiningEngine) context.getBean("qiq_dme")).shutdown();
        }
    }

    private static String foo(double x1, double x2) {
        double v1 = Math.sin(x1 * Math.PI);
        return x2 > v1 ? "a" : "b";
    }
}
