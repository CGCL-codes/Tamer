package org.extwind.osgi.tapestry.annotations.internal;

import java.lang.reflect.Modifier;
import java.util.List;
import org.apache.tapestry5.ioc.util.BodyBuilder;
import org.apache.tapestry5.model.MutableComponentModel;
import org.apache.tapestry5.services.ClassTransformation;
import org.apache.tapestry5.services.ComponentClassTransformWorker;
import org.apache.tapestry5.services.TransformMethodSignature;
import org.extwind.osgi.tapestry.annotations.Extension;
import org.extwind.osgi.tapestry.service.ServiceHood;

/**
 * @author Donf Yang
 * 
 */
public class ExtensionAnnotationWorker implements ComponentClassTransformWorker {

    protected static final String PREFIX_METHOD = "method:";

    protected ServiceHood serviceHood;

    public ExtensionAnnotationWorker(ServiceHood serviceHood) {
        this.serviceHood = serviceHood;
    }

    public void transform(ClassTransformation transformation, MutableComponentModel model) {
        List<String> fieldNameList = transformation.findFieldsWithAnnotation(Extension.class);
        if (fieldNameList.size() == 0) {
            return;
        }
        String serviceHood = transformation.addInjectedField(ServiceHood.class, "ServiceHood", this.serviceHood);
        for (String fieldName : fieldNameList) {
            transformField(fieldName, transformation, serviceHood);
        }
    }

    protected void transformField(String fieldName, ClassTransformation transformation, String serviceHood) {
        Extension annotation = transformation.getFieldAnnotation(fieldName, Extension.class);
        String id = annotation.id();
        String point = annotation.point();
        String fieldType = transformation.getFieldType(fieldName);
        String methodName = transformation.newMemberName("getExtensionObject", fieldName);
        TransformMethodSignature sig = new TransformMethodSignature(Modifier.PRIVATE, fieldType, methodName, null, null);
        BodyBuilder builder = new BodyBuilder();
        builder.begin();
        builder.addln("return (%s) %s.getExtensionObject(\"%s\",\"%s\",%s );", fieldType, serviceHood, point, id, transformation.getResourcesFieldName());
        builder.end();
        transformation.addMethod(sig, builder.toString());
        transformation.replaceReadAccess(fieldName, methodName);
        transformation.makeReadOnly(fieldName);
        transformation.removeField(fieldName);
    }
}
