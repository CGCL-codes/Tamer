package cx.ath.contribs.standardApp.attributedTree;

import java.lang.reflect.Constructor;
import cx.ath.contribs.attributedTree.xml.transform.CategoryBase;
import cx.ath.contribs.attributedTree.xml.transform.TransformFactory;

public class StandardTransformFactory<E extends StandardEnvironment> extends TransformFactory<E> {

    public StandardTransformFactory(E env) {
        super(env);
    }

    public CategoryBase newTransformerManager(String className, StandardEnvironment env, boolean useSingleton) {
        try {
            if (useSingleton && _delegate.containsSingleton(className)) return (CategoryBase) _delegate.getSingleton(className);
            Class classTM = Class.forName(className);
            Class[] formParamTM = new Class[1];
            formParamTM[0] = StandardEnvironment.class;
            Constructor constructorTM = classTM.getConstructor(formParamTM);
            Object[] actParamTM = new Object[1];
            actParamTM[0] = env;
            _delegate.setSingleton(className, constructorTM.newInstance(actParamTM));
            return (CategoryBase) (_delegate.getSingleton(className));
        } catch (Exception ex) {
            return null;
        }
    }
}
