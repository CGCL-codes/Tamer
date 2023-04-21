package com.rapidminer.operator.web.utility;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;
import com.rapidminer.example.Attribute;
import com.rapidminer.example.Example;
import com.rapidminer.example.ExampleSet;
import com.rapidminer.example.table.AttributeFactory;
import com.rapidminer.operator.AbstractExampleSetProcessing;
import com.rapidminer.operator.OperatorDescription;
import com.rapidminer.operator.OperatorException;
import com.rapidminer.parameter.ParameterType;
import com.rapidminer.parameter.ParameterTypeAttribute;
import com.rapidminer.tools.Ontology;
import com.rapidminer.tools.io.Encoding;

/**
 * Decodes URLs.
 * 
 * @author Tobias Malbrecht
 */
public class EncodeURLOperator extends AbstractExampleSetProcessing {

    public static final String PARAMETER_URL_ATTRIBUTE = "url_attribute";

    public EncodeURLOperator(OperatorDescription description) {
        super(description);
    }

    @Override
    public ExampleSet apply(ExampleSet exampleSet) throws OperatorException {
        Attribute decodedURLAttribute = exampleSet.getAttributes().get(getParameterAsString(PARAMETER_URL_ATTRIBUTE));
        Attribute encodedURLAttribute = AttributeFactory.createAttribute(decodedURLAttribute.getName() + System.currentTimeMillis(), decodedURLAttribute.getValueType());
        exampleSet.getExampleTable().addAttribute(encodedURLAttribute);
        exampleSet.getAttributes().addRegular(encodedURLAttribute);
        String encoding = Encoding.getEncoding(this).name();
        try {
            for (Example example : exampleSet) {
                example.setValue(encodedURLAttribute, URLEncoder.encode(example.getValueAsString(decodedURLAttribute), encoding));
            }
        } catch (UnsupportedEncodingException e) {
        }
        if (exampleSet.getAttributes().getRole(decodedURLAttribute).isSpecial()) {
            exampleSet.getAttributes().setSpecialAttribute(encodedURLAttribute, exampleSet.getAttributes().getRole(decodedURLAttribute).getSpecialName());
        }
        exampleSet.getAttributes().remove(decodedURLAttribute);
        encodedURLAttribute.setName(decodedURLAttribute.getName());
        return exampleSet;
    }

    @Override
    public boolean writesIntoExistingData() {
        return false;
    }

    @Override
    public List<ParameterType> getParameterTypes() {
        List<ParameterType> types = super.getParameterTypes();
        types.add(new ParameterTypeAttribute(PARAMETER_URL_ATTRIBUTE, "The attribute that contains the URLs that should be decoded.", getExampleSetInputPort(), Ontology.NOMINAL));
        types.addAll(Encoding.getParameterTypes(this));
        return types;
    }
}
