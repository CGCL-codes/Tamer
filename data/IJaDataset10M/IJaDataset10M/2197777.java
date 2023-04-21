package ar.com.fdvs.bean2bean.population.conversion;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import ar.com.fdvs.bean2bean.conversion.GeneralTypeConverter;
import ar.com.fdvs.bean2bean.conversion.SpecializedTypeConverter;
import ar.com.fdvs.bean2bean.conversion.TypeConverter;

/**
 * Esta interfaz representa un conversor de valores, que unifica las interfaces de los distintos
 * tipos de conversores
 * 
 * @author D.Garcia
 * @since 15/01/2009
 */
public interface TypeConverterCall {

    /**
	 * Realiza la conversion del valor utilizando el conversor base pasado como referencia para
	 * obtener el conversor especificado
	 * 
	 * @param originalValue
	 *            Valor a convertir
	 * @param expectedType
	 *            Tipoe esperado de la conversion
	 * @param contextAnnotations
	 *            Annotaciones auxiliares
	 * @param alternativeConversor
	 *            Nombre del conversor a utilizar
	 * @param baseTypeConverter
	 *            Conversor base desde el cual obtener el conversor indicado
	 * @return El valor convertido
	 */
    Object convertValue(Object originalValue, Type expectedType, Annotation[] contextAnnotations);

    public static class GenericConverterCall implements TypeConverterCall {

        private TypeConverter typeConverter;

        public GenericConverterCall(TypeConverter typeConverter) {
            this.typeConverter = typeConverter;
        }

        public Object convertValue(Object originalValue, Type expectedType, Annotation[] contextAnnotations) {
            Object convertedValue = typeConverter.convertValue(originalValue, expectedType, contextAnnotations);
            return convertedValue;
        }

        @Override
        public String toString() {
            return getClass().getSimpleName();
        }
    }

    public static class SpecializedConverterCall implements TypeConverterCall {

        private SpecializedTypeConverter<Object, Object> specializedConverter;

        @SuppressWarnings("unchecked")
        public SpecializedConverterCall(SpecializedTypeConverter<?, ?> converter) {
            this.specializedConverter = (SpecializedTypeConverter<Object, Object>) converter;
        }

        public Object convertValue(Object originalValue, Type expectedType, Annotation[] contextAnnotations) {
            Object convertedValue = specializedConverter.convertTo(expectedType, originalValue, contextAnnotations);
            return convertedValue;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(getClass().getSimpleName());
            builder.append("[");
            builder.append(specializedConverter);
            builder.append("]");
            return builder.toString();
        }
    }

    public static class GeneralConverterFromCall implements TypeConverterCall {

        private GeneralTypeConverter<Object, Object> generalConverter;

        @SuppressWarnings("unchecked")
        public GeneralConverterFromCall(GeneralTypeConverter<?, ?> generalConverter) {
            this.generalConverter = (GeneralTypeConverter<Object, Object>) generalConverter;
        }

        public Object convertValue(Object originalValue, Type expectedType, Annotation[] contextAnnotations) {
            Object convertedValue = generalConverter.convertFrom(originalValue, expectedType, contextAnnotations);
            return convertedValue;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(getClass().getSimpleName());
            builder.append("[");
            builder.append(generalConverter);
            builder.append("]");
            return builder.toString();
        }
    }

    public static class GeneralConverterToCall implements TypeConverterCall {

        private GeneralTypeConverter<Object, Object> generalConverter;

        @SuppressWarnings("unchecked")
        public GeneralConverterToCall(GeneralTypeConverter<?, ?> generalConverter) {
            this.generalConverter = (GeneralTypeConverter<Object, Object>) generalConverter;
        }

        public Object convertValue(Object originalValue, Type expectedType, Annotation[] contextAnnotations) {
            Object convertedValue = generalConverter.convertTo(expectedType, originalValue, contextAnnotations);
            return convertedValue;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder(getClass().getSimpleName());
            builder.append("[");
            builder.append(generalConverter);
            builder.append("]");
            return builder.toString();
        }
    }
}
