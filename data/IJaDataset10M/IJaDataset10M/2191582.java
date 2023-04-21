package storm.definition.field.annotation.option;

import java.lang.reflect.Field;
import storm.annotations.options.DefaultValue;
import storm.annotations.options.OldName;
import storm.annotations.options.Size;
import storm.description.field.FieldDescription;
import storm.description.field.FieldOptions;

public class DefaultOptionAnnotationReader implements OptionAnnotationReader {

    @Override
    public void configureOption(Field cfield, FieldDescription field, Class type) throws Throwable {
        if (cfield.isAnnotationPresent(DefaultValue.class)) {
            DefaultValue ann = cfield.getAnnotation(DefaultValue.class);
            String value = ann.value();
            field.getOptions().put(FieldOptions.DEFAULT_VALUE, value);
        }
        if (cfield.isAnnotationPresent(Size.class)) {
            Size ann = cfield.getAnnotation(Size.class);
            String value = ann.value();
            field.getOptions().put(FieldOptions.SIZE, value);
        }
        if (cfield.isAnnotationPresent(OldName.class)) {
            OldName ann = cfield.getAnnotation(OldName.class);
            String value = ann.value();
            field.getOptions().put(FieldOptions.OLD_NAME, value);
        }
    }
}
