package org.jgentleframework.context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jgentleframework.configure.AbstractConfig;
import org.jgentleframework.configure.enums.Types;
import org.jgentleframework.configure.objectmeta.ObjectAttach;
import org.jgentleframework.configure.objectmeta.ObjectBindingConstant;
import org.jgentleframework.configure.objectmeta.ObjectConstant;
import org.jgentleframework.context.injecting.AbstractBeanFactory;
import org.jgentleframework.context.injecting.AbstractLoadingFactory;
import org.jgentleframework.context.injecting.Provider;
import org.jgentleframework.core.JGentleException;
import org.jgentleframework.reflection.DefinitionPostProcessor;
import org.jgentleframework.reflection.annohandler.AnnotationBeanProcessor;
import org.jgentleframework.utils.ReflectUtils;

/**
 * The Class AbstractInitLoading.
 * 
 * @author Quoc Chung - mailto: <a
 *         href="mailto:skydunkpro@yahoo.com">skydunkpro@yahoo.com</a>
 * @date Feb 12, 2009
 */
public abstract class AbstractInitLoading {

    /** The Constant log. */
    private static final Log log = LogFactory.getLog(AbstractInitLoading.class);

    /**
	 * Loading.
	 * 
	 * @param OLArray
	 *            the oL array
	 */
    @SuppressWarnings("unchecked")
    public static void loading(Provider provider, List<Map<String, Object>> OLArray) {
        AbstractLoadingFactory loadingFactory = (AbstractLoadingFactory) (ReflectUtils.isCast(AbstractLoadingFactory.class, provider) ? provider : null);
        List<DefinitionPostProcessor> dppList = new ArrayList<DefinitionPostProcessor>();
        Map<Class, AnnotationBeanProcessor> abpHash = new HashMap<Class, AnnotationBeanProcessor>();
        List<ObjectAttach<?>> oaList = new ArrayList<ObjectAttach<?>>();
        List<Class<?>> beanClassList = new ArrayList<Class<?>>();
        List<ObjectBindingConstant> obcList = new ArrayList<ObjectBindingConstant>();
        List<ObjectConstant> ocList = new ArrayList<ObjectConstant>();
        for (Map<String, Object> optionsList : OLArray) {
            List<DefinitionPostProcessor> dpp = (List<DefinitionPostProcessor>) optionsList.get(AbstractConfig.DEFINITION_POST_PROCESSOR);
            if (dpp != null) dppList.addAll(dpp);
            Map<Class, AnnotationBeanProcessor> abp = (Map<Class, AnnotationBeanProcessor>) optionsList.get(AbstractConfig.ANNOTATION_BEAN_PROCESSOR_LIST);
            if (abp != null) abpHash.putAll(abp);
            List<ObjectAttach<?>> oa = (List<ObjectAttach<?>>) optionsList.get(AbstractConfig.OBJECT_ATTACH_LIST);
            if (oa != null) oaList.addAll(oa);
            List<Class<?>> bc = (List<Class<?>>) optionsList.get(AbstractConfig.BEAN_CLASS_LIST);
            if (bc != null) beanClassList.addAll(bc);
            List<ObjectBindingConstant> obc = (List<ObjectBindingConstant>) optionsList.get(AbstractConfig.OBJECT_BINDING_CONSTANT_LIST);
            if (obc != null) obcList.addAll(obc);
            List<ObjectConstant> oc = (List<ObjectConstant>) optionsList.get(AbstractConfig.OBJECT_CONSTANT_LIST);
            if (oc != null) ocList.addAll(oc);
        }
        if (loadingFactory != null) {
            loadingFactory.load_DefinitionPostProcessor(dppList);
            loadingFactory.load_AnnotationBeanProcessor(abpHash);
            List<Object> notLazyList = new ArrayList<Object>();
            List<Object> annotateIDList = new ArrayList<Object>();
            loadingFactory.load_ObjectAttachList(oaList, notLazyList, Types.ANNOTATION, annotateIDList);
            loadingFactory.load_BeanClassList(beanClassList, notLazyList, Types.ANNOTATION, annotateIDList);
            loadingFactory.load_ObjectBindingConstantList(obcList, notLazyList, true, annotateIDList);
            AbstractBeanFactory.buildDefBeanAnnotate(provider, annotateIDList);
            loadingFactory.load_ObjectConstantList(ocList);
            loadingFactory.load_ObjectAttachList(oaList, notLazyList, Types.NON_ANNOTATION);
            loadingFactory.load_BeanClassList(beanClassList, notLazyList, Types.NON_ANNOTATION);
            loadingFactory.load_ObjectBindingConstantList(obcList, notLazyList, false);
            provider.getDetectorController().handling(OLArray);
            AbstractBeanFactory.buildObjectBeanFromInfo(provider, notLazyList);
        } else {
            if (log.isFatalEnabled()) {
                log.fatal("Could not load the binding information !!", new JGentleException());
            }
        }
    }
}
