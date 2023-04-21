package org.jaffa.rules.jbossaop;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import javassist.CtConstructor;
import javassist.CtField;
import javassist.CtMethod;
import org.apache.log4j.Logger;
import org.jaffa.rules.variation.VariationRepository;
import org.jboss.aop.Advisor;
import org.jboss.aop.metadata.ClassMetaDataBinding;
import org.jboss.aop.metadata.ClassMetaDataLoader;
import org.w3c.dom.Element;

/** This class is used to import the variations.
 */
public class VariationLoader implements ClassMetaDataLoader {

    private static Logger log = Logger.getLogger(VariationLoader.class);

    private static ClassMetaDataBinding c_classMetaDataBinding = null;

    /** Imports variations into the VariationRepository.
     * This method is invoked by the AopC task during preapplication of aspects.
     * It is also called at runtime during application startup.
     * @param element the element we are processing within an aop file.
     * @param fileName the fileName of the aop file.
     * @param tagName the tagName that this element belongs to.
     * @param className this will typically contain the classname for which we've defined the rules.
     * @return an instance of DummyClassMetaDataBinding.
     * @throws Exception if any error occurs.
     */
    public ClassMetaDataBinding importMetaData(Element element, String fileName, String tagName, String className) throws Exception {
        if (log.isDebugEnabled()) log.debug("Load variation for " + fileName + " / " + tagName + " / " + className);
        fileName = MetaDataLoader.extractFileName(fileName);
        VariationRepository.instance().load(element, fileName);
        if (c_classMetaDataBinding == null) {
            synchronized (this.getClass()) {
                if (c_classMetaDataBinding == null) c_classMetaDataBinding = new ClassMetaDataBinding(this, fileName, tagName, className) {
                };
            }
        }
        return c_classMetaDataBinding;
    }

    /** This method is used to prepare the required pointcuts, so that Interceptors can be bound to them.
     * It is invoked by the AopC task during preapplication of aspects, after loading the metadata.
     * It is called at runtime only in DynamicAOP-mode, when loading the class.
     * @param classAdvisor The class advisor.
     * @param data the meta data.
     * @param ctMethod the advised methods.
     * @param ctField the advised fields.
     * @param ctConstructor the advised constructors.
     * @throws Exception if any error occurs.
     */
    public void bind(Advisor classAdvisor, ClassMetaDataBinding data, CtMethod[] ctMethod, CtField[] ctField, CtConstructor[] ctConstructor) throws java.lang.Exception {
    }

    /** This method can be used to bind Interceptors to the pointcuts.
     * It is method is invoked only at runtime, when the class is loaded.
     * In DynamicAOP-mode, it is called after the prebind stage.
     * @param classAdvisor The class advisor.
     * @param data the meta data.
     * @param methods the advised methods.
     * @param fields the advised fields.
     * @param constructors the advised constructors.
     * @throws Exception if any error occurs.
     */
    public void bind(Advisor classAdvisor, ClassMetaDataBinding data, Method[] methods, Field[] fields, Constructor[] constructors) throws Exception {
    }
}
