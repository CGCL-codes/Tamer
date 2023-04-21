package se.issi.magnolia.module.blossom.dialog;

import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;
import se.issi.magnolia.module.blossom.annotation.DialogFactory;
import se.issi.magnolia.module.blossom.annotation.I18nBasename;
import se.issi.magnolia.module.blossom.annotation.TabOrder;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class DialogDescriptionBuilder {

    public BlossomDialogDescription buildDescription(Object factoryObject) {
        return buildDescription(factoryObject, null);
    }

    public List<BlossomDialogDescription> buildDescriptions(final Object handler) {
        final List<Method> factoryMethods = new ArrayList<Method>();
        ReflectionUtils.doWithMethods(handler.getClass(), new ReflectionUtils.MethodCallback() {

            public void doWith(Method method) {
                DialogFactory dialogFactory = method.getAnnotation(DialogFactory.class);
                if (dialogFactory != null && method.equals(ClassUtils.getMostSpecificMethod(method, handler.getClass()))) {
                    if (Modifier.isStatic(method.getModifiers())) {
                        throw new IllegalStateException("DialogFactory annotation is not supported on static methods");
                    }
                    factoryMethods.add(method);
                }
            }
        });
        List<BlossomDialogDescription> descriptions = new ArrayList<BlossomDialogDescription>();
        for (Method method : factoryMethods) {
            descriptions.add(buildDescription(handler, method));
        }
        return descriptions;
    }

    protected BlossomDialogDescription buildDescription(Object factoryObject, Method factoryMethod) {
        DialogFactory annotation = findAnnotation(DialogFactory.class, factoryObject, factoryMethod);
        TabOrder tabOrder = findAnnotation(TabOrder.class, factoryObject, factoryMethod);
        I18nBasename i18nBasename = findAnnotation(I18nBasename.class, factoryObject, factoryMethod);
        DialogFactoryMetaData factoryMetaData = new DialogFactoryMetaData();
        factoryMetaData.setLabel(annotation.label());
        factoryMetaData.setI18nBasename(i18nBasename != null ? i18nBasename.value() : null);
        factoryMetaData.setFactoryObject(factoryObject);
        factoryMetaData.setFactoryMethod(factoryMethod);
        factoryMetaData.setTabOrder(tabOrder != null ? tabOrder.value() : null);
        BlossomDialogDescription dialogDescription = new BlossomDialogDescription();
        dialogDescription.setName(annotation.value());
        dialogDescription.setFactoryMetaData(factoryMetaData);
        return dialogDescription;
    }

    private <T extends Annotation> T findAnnotation(Class<T> annotationClass, Object factoryObject, Method factoryMethod) {
        if (factoryMethod != null) return factoryMethod.getAnnotation(annotationClass);
        return factoryObject.getClass().getAnnotation(annotationClass);
    }
}
