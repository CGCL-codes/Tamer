package net.disy.ogc.wps.v_1_0_0.procedure;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import net.disy.ogc.wps.v_1_0_0.model.CRS;
import net.disy.ogc.wps.v_1_0_0.model.DataType;
import net.disy.ogc.wps.v_1_0_0.model.DataTypeTypeVisitor;
import net.disy.ogc.wps.v_1_0_0.model.Format;
import net.disy.ogc.wps.v_1_0_0.model.FormatId;
import net.disy.ogc.wps.v_1_0_0.model.LiteralType;
import net.disy.ogc.wps.v_1_0_0.util.WpsUtils;
import net.opengis.ows.v_1_1_0.DomainMetadataType;
import net.opengis.ows.v_1_1_0.MetadataType;
import net.opengis.wps.v_1_0_0.CRSsType;
import net.opengis.wps.v_1_0_0.ComplexDataType;
import net.opengis.wps.v_1_0_0.InputDescriptionType;
import net.opengis.wps.v_1_0_0.LiteralDataType;
import net.opengis.wps.v_1_0_0.LiteralInputType;
import net.opengis.wps.v_1_0_0.ProcessDescriptionType;
import net.opengis.wps.v_1_0_0.SupportedCRSsType;
import net.opengis.wps.v_1_0_0.SupportedComplexDataInputType;
import net.opengis.wps.v_1_0_0.ProcessDescriptionType.DataInputs;
import net.opengis.wps.v_1_0_0.SupportedCRSsType.Default;

public class DataInputBuilder extends AbstractDataInputOutputBuilder {

    private final ProcessDescriptionType.DataInputs dataInputs;

    private final Set<String> ids = new HashSet<String>();

    public DataInputBuilder(WpsProcessContext context) {
        super(context);
        dataInputs = context.getWpsObjectFactory().createProcessDescriptionTypeDataInputs();
    }

    public void addParameter(InputParameterDescription parameter) {
        InputDescriptionType inputDescription = getInputDescription(parameter);
        handleInputDescriptionForParameterType(inputDescription, parameter);
        String id = inputDescription.getIdentifier().getValue();
        if (ids.contains(id)) {
            throw new IllegalStateException("Multiple declaration of inputParameter with identifier '" + id + "'");
        }
        ids.add(id);
        dataInputs.getInput().add(inputDescription);
    }

    private InputDescriptionType getInputDescription(InputParameterDescription parameter) {
        final InputDescriptionType input = getContext().getWpsObjectFactory().createInputDescriptionType();
        input.setIdentifier(WpsUtils.createCodeType(parameter.getId()));
        input.setTitle(WpsUtils.createLanguageStringType(parameter.getTitle()));
        input.setAbstract(WpsUtils.createLanguageStringType(parameter.getAbstract()));
        final List<MetadataType> metadata = parameter.getMetadata();
        if (!metadata.isEmpty()) {
            input.setMetadata(metadata);
        }
        return input;
    }

    private void handleInputDescriptionForParameterType(final InputDescriptionType inputDescription, final InputParameterDescription parameter) {
        inputDescription.setMinOccurs(parameter.isOptional() ? BigInteger.ZERO : BigInteger.ONE);
        if (parameter.getParameterClass().isArray()) {
            inputDescription.setMaxOccurs(BigInteger.valueOf(65535l));
        } else {
            inputDescription.setMaxOccurs(BigInteger.valueOf(1));
        }
        final DataType<?> dataType = getContext().getDataTypeHelper().getDataType(parameter, parameter.getParameterClass());
        dataType.getType().accept(new DataTypeTypeVisitor() {

            @Override
            public void visitLiteral() {
                addLiteralInput(parameter, inputDescription, dataType);
            }

            @Override
            public void visitComplex() {
                addComplexInput(parameter, inputDescription, dataType);
            }

            @Override
            public void visitBBox() {
                addBoundingBoxInput(parameter, inputDescription, dataType);
            }
        });
    }

    private void addLiteralInput(InputParameterDescription parameter, InputDescriptionType inputDescription, DataType<?> dataType) {
        DomainMetadataType metadataType = getMetaDataType(dataType);
        LiteralInputType literalData = getContext().getWpsObjectFactory().createLiteralInputType();
        literalData.setDataType(metadataType);
        literalData.setAnyValue(getContext().getOwsObjectFactory().createAnyValue());
        inputDescription.setLiteralData(literalData);
        final Object defaultValue = parameter.getDefaultValue();
        if (defaultValue != null) {
            @SuppressWarnings("unchecked") final LiteralType<Object> literalType = (LiteralType<Object>) getContext().getLiteralTypeRegistry().getLiteralType(dataType);
            final String value = literalType.toString(defaultValue);
            literalData.setDefaultValue(value);
        }
    }

    private void addComplexInput(InputParameterDescription parameter, InputDescriptionType inputDescription, DataType<?> dataType) {
        SupportedComplexDataInputType complexData = getContext().getWpsObjectFactory().createSupportedComplexDataInputType();
        inputDescription.setComplexData(complexData);
        addDataCombinations(dataType, complexData);
        final Object defaultValue = parameter.getDefaultValue();
        if (defaultValue != null) {
            final ComplexDataType value = getContext().getWpsObjectFactory().createComplexDataType();
            @SuppressWarnings("unchecked") final Format<Object> format = (Format<Object>) getContext().getFormatRegistry().getDefaultFormat(dataType);
            final FormatId formatId = format.getFormatId();
            value.setEncoding(formatId.getEncoding());
            value.setMimeType(formatId.getMimeType());
            value.setSchema(formatId.getSchemaDesignator());
            value.setContent(format.marshal(defaultValue));
            complexData.setDefaultValue(value);
        }
    }

    private void addBoundingBoxInput(InputParameterDescription parameter, InputDescriptionType inputDescription, DataType<?> dataType) {
        final SupportedCRSsType supportedCRSsType = getContext().getWpsObjectFactory().createSupportedCRSsType();
        inputDescription.setBoundingBoxData(supportedCRSsType);
        final Default defaultCRS = getContext().getWpsObjectFactory().createSupportedCRSsTypeDefault();
        supportedCRSsType.setDefault(defaultCRS);
        defaultCRS.setCRS(getContext().getCRSRegistry().getDefaultCRS().getName());
        final CRSsType supportedCRSs = getContext().getWpsObjectFactory().createCRSsType();
        for (final CRS supportedCRS : getContext().getCRSRegistry().getSupportedCRSs()) {
            supportedCRSs.getCRS().add(supportedCRS.getName());
        }
        supportedCRSsType.setSupported(supportedCRSs);
    }

    public DataInputs getDataInputs() {
        return dataInputs;
    }
}
