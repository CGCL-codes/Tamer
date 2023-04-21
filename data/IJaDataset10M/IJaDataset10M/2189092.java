package org.opc.convertion.jivariant2java.singleval;

import org.opc.convertion.ConvertionUtils;
import java.util.Date;
import org.jinterop.dcom.common.JIException;
import org.jinterop.dcom.core.JICurrency;
import org.jinterop.dcom.core.JIVariant;

/**
 * A converter for currency type JIVariants
 *
 * @author Joao Leal
 */
public class JIVariantCY2JavaConverter extends Variant2JavaConverter<Double> {

    public JIVariantCY2JavaConverter(Class<?> outputType) {
        super(JIVariant.VT_CY, outputType);
    }

    @Override
    public Class<Double> getDataTypeClass() {
        return Double.class;
    }

    @Override
    public Short convertToI2(JIVariant variant) throws JIException {
        return convertToI4(variant).shortValue();
    }

    @Override
    public Integer convertToI4(JIVariant variant) throws JIException {
        final JICurrency value = (JICurrency) variant.getObject();
        return value.getUnits();
    }

    @Override
    public Long convertToI8(JIVariant variant) throws JIException {
        return convertToI4(variant).longValue();
    }

    @Override
    public Float convertToR4(JIVariant variant) throws JIException {
        return new Float(convertToDouble(variant));
    }

    @Override
    public Double convertToR8(JIVariant variant) throws JIException {
        return convertToDouble(variant);
    }

    private double convertToDouble(JIVariant variant) throws JIException {
        final JICurrency value = (JICurrency) variant.getObject();
        return ConvertionUtils.convertToDouble(value);
    }

    @Override
    public Date convertToDate(JIVariant variant) throws JIException {
        final JICurrency value = (JICurrency) variant.getObject();
        return ConvertionUtils.convertToDate(value);
    }

    @Override
    public String convertToString(JIVariant variant) throws JIException {
        return convertToR8(variant).toString();
    }

    @Override
    public Boolean convertToBool(JIVariant variant) throws JIException {
        final JICurrency value = (JICurrency) variant.getObject();
        return value.getUnits() != 0 && value.getFractionalUnits() != 0;
    }
}
