package com.hy.mydesktop.server.rpc;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java_cup.internal_error;
import javassist.tools.framedump;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.DocumentException;
import org.springframework.web.context.WebApplicationContext;
import net.sf.gilead.configuration.ConfigurationHelper;
import net.sf.gilead.core.IPersistenceUtil;
import net.sf.gilead.core.PersistentBeanManager;
import net.sf.gilead.core.hibernate.jpa.HibernateJpaUtil;
import sun.management.GcInfoCompositeData;
import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.BaseModelData;
import com.extjs.gxt.ui.client.data.BeanModel;
import com.extjs.gxt.ui.client.data.ModelData;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.hy.enterprise.framework.service.spring.MySpringUtil;
import com.hy.mydesktop.client.rpc.ServiceInvoker;
import com.hy.mydesktop.server.dao.UserDao;
import com.hy.mydesktop.server.service.ComponentControllerMetaModelService;
import com.hy.mydesktop.server.service.InitGxtSystemDatas;
import com.hy.mydesktop.shared.persistence.domain.User;
import com.hy.mydesktop.shared.persistence.domain.gxt.GwtRpcModel;
import com.hy.mydesktop.shared.persistence.domain.gxt.GxtComponentControllerMetaModel;
import com.hy.mydesktop.shared.persistence.domain.gxt.GxtComponentMetaNodeModel;
import com.hy.mydesktop.shared.persistence.domain.gxt.MyBaseModel;
import com.hy.mydesktop.shared.persistence.domain.gxt.constant.LibConstants;
import com.hy.mydesktop.shared.persistence.domain.gxt.constant.MetaDataConstants;
import com.hy.mydesktop.shared.rpc.meta.GxtToSeamServiceParameter;
import com.hy.mydesktop.shared.rpc.meta.GxtToSeamServiceResult;
import com.hy.mydesktop.shared.rpc.util.RpcDebugUtil;
import com.hy.mydesktop.shared.util.log.MyLoggerUtil;
import com.sun.istack.internal.FinalArrayList;

/**
 * The server side implementation of the RPC service.
 */
@SuppressWarnings("serial")
public class ServiceInvokerImpl extends BaseRpcServiceWithSpring implements ServiceInvoker {

    private Log logger = LogFactory.getLog(ServiceInvokerImpl.class);

    @Override
    public GxtComponentControllerMetaModel callService2(GxtToSeamServiceParameter gxtToSeamServiceParameter) {
        GxtToSeamServiceResult gxtToSeamServiceResult = new GxtToSeamServiceResult();
        gxtToSeamServiceResult.setConversationId("hello");
        GxtComponentControllerMetaModel componentControllerMetaModel = new GxtComponentControllerMetaModel();
        BaseModel result = new BaseModel();
        System.err.println(componentControllerMetaModel);
        result.set("kl", componentControllerMetaModel);
        gxtToSeamServiceResult.setResult(result);
        return componentControllerMetaModel;
    }

    @Override
    public GxtToSeamServiceResult callService(final GxtToSeamServiceParameter gxtToSeamServiceParameter) {
        this.printAsteriskDebug();
        this.debugPrintGxtToSeamServiceParameterOfRpc(getClass(), gxtToSeamServiceParameter);
        this.printAsteriskDebug();
        GxtToSeamServiceResult gxtToSeamServiceResult = new GxtToSeamServiceResult();
        resolveAndInvokeServiceFromSpring(gxtToSeamServiceParameter, gxtToSeamServiceResult);
        return gxtToSeamServiceResult;
    }

    private void test() {
        ComponentControllerMetaModelService componentControllerMetaModelService = getSpringContext().getBean("componentControllerMetaModelService", ComponentControllerMetaModelService.class);
        GxtComponentControllerMetaModel componentControllerMetaModel = componentControllerMetaModelService.findComponentControllerMetaModelByControllerMetaModelId("controllerMetaModel0");
        this.printAsteriskDebug();
        logger.debug("在由gilead处理之前，得到的componentControllerMetaModel:");
        logger.debug("before  class :" + componentControllerMetaModel.getClass().getName());
        logger.debug("before  class :" + componentControllerMetaModel.getList().getClass().getName());
        this.printAsteriskDebug();
        this.printAsteriskDebug();
        logger.debug("把得到的componentControllerMetaModel，由gilead处理，转化为:cloneComponentControllerMetaModel");
        GxtComponentControllerMetaModel cloneControllerMetaModel = (GxtComponentControllerMetaModel) beanManager.clone(componentControllerMetaModel);
        logger.debug("after   :" + cloneControllerMetaModel.getClass().getName());
        logger.debug("after   :" + cloneControllerMetaModel.getList().getClass().getName());
        this.printAsteriskDebug();
        BaseModel result = new BaseModel();
        System.err.println(componentControllerMetaModel);
        result.set(MetaDataConstants.GXT_COMPONENT_CONTROLLER_METAMODEL, cloneControllerMetaModel);
    }

    private void resolveAndInvokeServiceFromSpring(GxtToSeamServiceParameter gxtToSeamServiceParameter, GxtToSeamServiceResult gxtToSeamServiceResult) {
        System.err.println("####ServiceInvokerImpl.callService(gxtToSeamServiceParameter) is running...........................");
        Class c = null;
        Method method = null;
        try {
            Object object = springContext.getBean(gxtToSeamServiceParameter.getServiceComponentName());
            c = object.getClass();
            if (gxtToSeamServiceParameter.getMethodArgumentsType().equals(MetaDataConstants.MODELDATA_ARRAY)) {
                Method[] methods = c.getMethods();
                if (gxtToSeamServiceParameter.getMethodArguments() != null) {
                    if (gxtToSeamServiceParameter.getMethodArguments() instanceof ModelData[]) {
                        method = c.getMethod(gxtToSeamServiceParameter.getServiceMethodName(), ModelData[].class);
                    } else {
                        method = c.getMethod(gxtToSeamServiceParameter.getServiceMethodName(), gxtToSeamServiceParameter.getMethodArguments().getClass());
                    }
                } else if (gxtToSeamServiceParameter.getMethodArguments2() != null) {
                    logger.error("gxtToSeamServiceParameter.getMethodArguments2():" + gxtToSeamServiceParameter.getMethodArguments2());
                    if (gxtToSeamServiceParameter.getMethodArguments2() instanceof List) {
                        method = c.getMethod(gxtToSeamServiceParameter.getServiceMethodName(), List.class);
                    } else {
                        throw new RuntimeException("444444444444444444444444444444");
                    }
                } else if (gxtToSeamServiceParameter.getArgumentValue() != null) {
                    method = c.getMethod(gxtToSeamServiceParameter.getServiceMethodName(), gxtToSeamServiceParameter.getArgumentValue().getClass());
                } else {
                    method = c.getMethod(gxtToSeamServiceParameter.getServiceMethodName(), null);
                }
            } else if (gxtToSeamServiceParameter.getMethodArgumentsType().equals("string") || gxtToSeamServiceParameter.getMethodArgumentsType().equals("String")) {
                method = c.getMethod(gxtToSeamServiceParameter.getServiceMethodName(), String.class);
            } else {
                throw new RuntimeException("444444444444444444444444444444");
            }
            printAsteriskDebug();
            logger.debug("从Spring的bean工厂，得到名称为" + gxtToSeamServiceParameter.getServiceComponentName() + "组件（实际产生的代理类名称为" + object + "），调用的方法名称为" + method + "，");
            ;
            gxtToSeamServiceResult.setConversationId(UUID.randomUUID().toString());
            BaseModel baseModel = new BaseModel();
            logger.info("Object re= method.invoke(object,gxtToSeamServiceParameter.getArgumentValue())");
            logger.info("method:" + method);
            logger.info("object:" + object);
            logger.info("gxtToSeamServiceParameter.getArgumentValue():" + gxtToSeamServiceParameter.getArgumentValue());
            logger.error("gxtToSeamServiceParameter.getMethodArguments():" + gxtToSeamServiceParameter.getMethodArguments());
            Object re = null;
            if (gxtToSeamServiceParameter.getMethodArguments() != null) {
                re = method.invoke(object, (Object) gxtToSeamServiceParameter.getMethodArguments());
            } else if (gxtToSeamServiceParameter.getMethodArguments2() != null) {
                re = method.invoke(object, gxtToSeamServiceParameter.getMethodArguments2());
            } else if (gxtToSeamServiceParameter.getArgumentValue() != null) {
                re = method.invoke(object, gxtToSeamServiceParameter.getArgumentValue());
            } else {
                re = method.invoke(object);
            }
            logger.debug("调用后，得到的返回结果为" + re);
            printAsteriskDebug();
            this.printAsteriskDebug();
            this.printAsteriskDebug();
            this.printAsteriskDebug();
            logger.debug("把得到的componentControllerMetaModel，由gilead处理，转化为:cloneComponentControllerMetaModel");
            logger.debug("after   :" + re);
            baseModel.set("listOfModelData", re);
            baseModel.set("exception", null);
            gxtToSeamServiceResult.setResult(baseModel);
            logger.error("####ServiceInvokerImpl.callService(gxtToSeamServiceParameter) is over...........................");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void printAsteriskDebug() {
        logger.debug("***************************************************************");
    }

    private void debugPrintGxtToSeamServiceParameterOfRpc(Class clazz, GxtToSeamServiceParameter gxtToSeamServiceParameter) {
        if (gxtToSeamServiceParameter == null) {
            logger.debug(clazz.getName() + ": gxtToSeamServiceParameter的组件名称，方法名称，参数均为空！");
            return;
        }
        logger.debug(clazz.getName() + ": gxtToSeamServiceParameter的组件名称" + gxtToSeamServiceParameter.getServiceComponentName());
        logger.debug(clazz.getName() + ": gxtToSeamServiceParameter的方法名称" + gxtToSeamServiceParameter.getServiceMethodName());
        if (gxtToSeamServiceParameter.getMethodArguments2() != null) {
            int i = 0;
            for (ModelData modelData : gxtToSeamServiceParameter.getMethodArguments2()) {
                logger.debug(clazz.getName() + ": gxtToSeamServiceParameter的方法的参数 " + (++i) + ":" + gxtToSeamServiceParameter.getMethodArguments2().toString());
                for (String propertyName : modelData.getPropertyNames()) {
                    logger.debug("属性名称:" + propertyName + ",属性值:" + modelData.get(propertyName));
                }
            }
        } else {
            logger.debug(clazz.getName() + ": gxtToSeamServiceParameter的方法的参数为空");
        }
    }
}
