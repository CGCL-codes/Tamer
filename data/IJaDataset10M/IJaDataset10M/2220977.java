package org.nutz.ioc.loader.annotation;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.nutz.castor.Castors;
import org.nutz.ioc.IocException;
import org.nutz.ioc.IocLoader;
import org.nutz.ioc.IocLoading;
import org.nutz.ioc.ObjectLoadException;
import org.nutz.ioc.annotation.InjectName;
import org.nutz.ioc.meta.IocEventSet;
import org.nutz.ioc.meta.IocField;
import org.nutz.ioc.meta.IocObject;
import org.nutz.ioc.meta.IocValue;
import org.nutz.lang.Lang;
import org.nutz.lang.Mirror;
import org.nutz.lang.Strings;
import org.nutz.log.Log;
import org.nutz.log.Logs;
import org.nutz.resource.Scans;

/**
 * 基于注解的Ioc配置
 * @author wendal(wendal1985@gmail.com)
 * 
 */
public class AnnotationIocLoader implements IocLoader {

    private static final Log LOG = Logs.get();

    private HashMap<String, IocObject> map = new HashMap<String, IocObject>();

    public AnnotationIocLoader(String... packages) {
        for (String packageZ : packages) for (Class<?> classZ : Scans.me().scanPackage(packageZ)) addClass(classZ);
        if (LOG.isInfoEnabled()) LOG.infof("Scan complete ! Found %s classes in %s base-packages!\nbeans = %s", map.size(), packages.length, Castors.me().castToString(map.keySet()));
    }

    private void addClass(Class<?> classZ) {
        if (classZ.isInterface() || classZ.isMemberClass() || classZ.isEnum() || classZ.isAnnotation() || classZ.isAnonymousClass()) return;
        int modify = classZ.getModifiers();
        if (Modifier.isAbstract(modify) || (!Modifier.isPublic(modify))) return;
        IocBean iocBean = classZ.getAnnotation(IocBean.class);
        if (iocBean != null) {
            if (LOG.isDebugEnabled()) LOG.debugf("Found a Class with Ioc-Annotation : %s", classZ);
            String beanName = iocBean.name();
            if (Strings.isBlank(beanName)) {
                InjectName innm = classZ.getAnnotation(InjectName.class);
                if (null != innm && !Strings.isBlank(innm.value())) {
                    beanName = innm.value();
                } else {
                    beanName = Strings.lowerFirst(classZ.getSimpleName());
                }
            }
            IocObject iocObject = new IocObject();
            iocObject.setType(classZ);
            map.put(beanName, iocObject);
            iocObject.setSingleton(iocBean.singleton());
            if (!Strings.isBlank(iocBean.scope())) iocObject.setScope(iocBean.scope());
            String[] args = iocBean.args();
            if (null != args && args.length > 0) for (String value : args) iocObject.addArg(convert(value));
            IocEventSet eventSet = new IocEventSet();
            iocObject.setEvents(eventSet);
            if (!Strings.isBlank(iocBean.create())) eventSet.setCreate(iocBean.create().trim().intern());
            if (!Strings.isBlank(iocBean.depose())) eventSet.setDepose(iocBean.depose().trim().intern());
            if (!Strings.isBlank(iocBean.fetch())) eventSet.setFetch(iocBean.fetch().trim().intern());
            List<String> fieldList = new ArrayList<String>();
            Mirror<?> mirror = Mirror.me(classZ);
            Field[] fields = mirror.getFields(Inject.class);
            for (Field field : fields) {
                Inject inject = field.getAnnotation(Inject.class);
                IocField iocField = new IocField();
                iocField.setName(field.getName());
                IocValue iocValue;
                if (Strings.isBlank(inject.value())) {
                    iocValue = new IocValue();
                    iocValue.setType(IocValue.TYPE_REFER);
                    iocValue.setValue(field.getName());
                } else iocValue = convert(inject.value());
                iocField.setValue(iocValue);
                iocObject.addField(iocField);
                fieldList.add(iocField.getName());
            }
            Method[] methods = mirror.getMethods();
            for (Method method : methods) {
                Inject inject = method.getAnnotation(Inject.class);
                if (inject == null) continue;
                int m = method.getModifiers();
                if (Modifier.isAbstract(m) || (!Modifier.isPublic(m)) || Modifier.isStatic(m)) continue;
                if (method.getName().startsWith("set") && method.getName().length() > 3 && method.getParameterTypes().length == 1) {
                    IocField iocField = new IocField();
                    iocField.setName(Strings.lowerFirst(method.getName().substring(3)));
                    if (fieldList.contains(iocField.getName())) throw duplicateField(classZ, iocField.getName());
                    IocValue iocValue;
                    if (Strings.isBlank(inject.value())) {
                        iocValue = new IocValue();
                        iocValue.setType(IocValue.TYPE_REFER);
                        iocValue.setValue(Strings.lowerFirst(method.getName().substring(3)));
                    } else iocValue = convert(inject.value());
                    iocField.setValue(iocValue);
                    iocObject.addField(iocField);
                    fieldList.add(iocField.getName());
                }
            }
            String[] flds = iocBean.fields();
            if (flds != null && flds.length > 0) {
                for (String fieldInfo : flds) {
                    if (fieldList.contains(fieldInfo)) throw duplicateField(classZ, fieldInfo);
                    IocField iocField = new IocField();
                    iocField.setName(fieldInfo);
                    IocValue iocValue = new IocValue();
                    iocValue.setType(IocValue.TYPE_REFER);
                    iocValue.setValue(fieldInfo);
                    iocField.setValue(iocValue);
                    iocObject.addField(iocField);
                    fieldList.add(iocField.getName());
                }
            }
            if (LOG.isDebugEnabled()) LOG.debugf("Processed Ioc Class : %s as [%s]", classZ, beanName);
        }
    }

    protected IocValue convert(String value) {
        IocValue iocValue = new IocValue();
        if (value.indexOf(':') > -1) {
            iocValue.setType(value.substring(0, value.indexOf(':')));
            iocValue.setValue(value.substring(value.indexOf(':') + 1));
        } else iocValue.setValue(value);
        return iocValue;
    }

    public String[] getName() {
        return map.keySet().toArray(new String[map.size()]);
    }

    public boolean has(String name) {
        return map.containsKey(name);
    }

    public IocObject load(IocLoading loading, String name) throws ObjectLoadException {
        if (has(name)) return map.get(name);
        throw new ObjectLoadException("Object '" + name + "' without define!");
    }

    private static final IocException duplicateField(Class<?> classZ, String name) {
        return Lang.makeThrow(IocException.class, "Duplicate filed defined! Class=%s,FileName=%s", classZ, name);
    }
}
