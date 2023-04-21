package shellkk.qiq.jdm.engine.algorithm.svm;

import java.util.HashMap;
import java.util.Map;
import javax.datamining.JDMException;
import javax.datamining.MiningAlgorithm;
import org.hibernate.Session;
import shellkk.qiq.jdm.JDMExceptionUtil;
import shellkk.qiq.jdm.base.IModelDetail;
import shellkk.qiq.jdm.base.ModelImpl;
import shellkk.qiq.jdm.common.FormerMetaProperty;
import shellkk.qiq.jdm.common.kernel.KernelFactory;
import shellkk.qiq.jdm.engine.persist.IModelDetailAccessObject;
import shellkk.qiq.jdm.modeldetail.svm.SVMBinaryClassFormer;
import shellkk.qiq.jdm.modeldetail.svm.SVMClassificationModelDetailImpl;
import shellkk.qiq.math.kernel.Kernel;

public class SVMClassificationModelDetailAO implements IModelDetailAccessObject {

    protected Map<String, KernelFactory> kernelFactoryMap = new HashMap();

    public MiningAlgorithm getMiningAlgorithm() {
        return MiningAlgorithm.svmClassification;
    }

    public Map<String, KernelFactory> getKernelFactoryMap() {
        return kernelFactoryMap;
    }

    public void setKernelFactoryMap(Map<String, KernelFactory> kernelFactoryMap) {
        this.kernelFactoryMap = kernelFactoryMap;
    }

    public void deleteDetail(ModelImpl model, Session session) throws Exception {
        String className = SVMClassificationModelDetailImpl.class.getName();
        Long modelId = model.getId();
        String hql = "from " + className + " as m where m.model.id=:modelId";
        SVMClassificationModelDetailImpl md = (SVMClassificationModelDetailImpl) session.createQuery(hql).setParameter("modelId", modelId).uniqueResult();
        if (md != null) {
            session.delete(md);
            for (FormerMetaProperty meta : md.getCategoryMetaProps()) {
                session.delete(meta.getCategorySet());
            }
        }
    }

    public IModelDetail loadDetail(ModelImpl model, Session session) throws Exception {
        String className = SVMClassificationModelDetailImpl.class.getName();
        Long modelId = model.getId();
        String hql = "from " + className + " as m where m.model.id=:modelId";
        SVMClassificationModelDetailImpl md = (SVMClassificationModelDetailImpl) session.createQuery(hql).setParameter("modelId", modelId).uniqueResult();
        if (md == null) {
            throw new Exception("modeldetail not found for model[" + model.getName() + "]!");
        }
        md.getModel().getName();
        md.getElements().size();
        for (SVMBinaryClassFormer former : md.getFormers()) {
            former.getFactors().size();
        }
        md.getKernelProperties().size();
        md.getCategoryMetaProps().size();
        md.getNumberMetaProps().size();
        md.getTextMetaProps().size();
        md.getOtherMetaProps().size();
        md.setKernel(createKernel(md.getKernelName(), md.getKernelProperties()));
        md.init();
        return md;
    }

    public void saveOrUpdateDetail(ModelImpl model, Session session) throws Exception {
        SVMClassificationModelDetailImpl md = (SVMClassificationModelDetailImpl) model.getModelDetail();
        for (FormerMetaProperty meta : md.getCategoryMetaProps()) {
            session.saveOrUpdate(meta.getCategorySet());
        }
        session.saveOrUpdate(model.getModelDetail());
    }

    protected Kernel createKernel(String kernelName, Map<String, String> kernelProps) throws Exception {
        KernelFactory factory = kernelFactoryMap.get(kernelName);
        if (factory == null) {
            JDMExceptionUtil.throwException(JDMException.JDMR_UNSUPPORTED_FEATURE, "kernel:" + kernelName);
        }
        Kernel kernel = factory.createKernel(kernelProps);
        return kernel;
    }
}
