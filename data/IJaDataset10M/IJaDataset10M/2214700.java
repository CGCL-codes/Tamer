package com.impact.xbm.server.sesta;

import com.impact.xbm.exceptions.XbmException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.NodeList;
import com.impact.xbm.utils.Xml;
import java.util.ArrayList;
import java.util.Iterator;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class ComplexParameter extends DOEParameter {

    protected ArrayList<DOEParameter> childParameters = null;

    private int numberPoints;

    private int currentPointNumber;

    public ArrayList<DOEParameter> getChildParameters() {
        return childParameters;
    }

    @Override
    public void initialize(Element parameterNode, Document doc) throws XbmException {
        super.initialize(parameterNode, doc);
        XPath xpath = XPathFactory.newInstance().newXPath();
        NodeList childParameterNodes = null;
        try {
            childParameterNodes = Xml.xpathEvaluateNodeList("//Parameter[@setName='" + this.setName + "']/Parameter", parameterNode, xpath);
        } catch (Exception ex) {
            throw new XbmException(ex);
        }
        int numChildParameters = childParameterNodes.getLength();
        Element childParameterNode = null;
        childParameters = new ArrayList<DOEParameter>(numChildParameters);
        for (int i = 0; i < numChildParameters; i++) {
            childParameterNode = (Element) childParameterNodes.item(i);
            DOEParameter childParameter = DOEParameterFactory.getDOEParameterInstance(childParameterNode, doc);
            childParameters.add(childParameter);
        }
        numberPoints = Integer.parseInt(parameterNode.getAttribute("numberPoints"));
        reset();
        computeDefault();
        computeNumberOfValues();
    }

    @Override
    public String getCurrentValue() {
        throw new RuntimeException("ComplexParameter.getCurrentValue should never be invoked");
    }

    @Override
    public boolean hasNext() {
        return currentPointNumber < this.numberOfValues;
    }

    @Override
    public void setCurrent2Default() {
        Iterator itr = childParameters.iterator();
        while (itr.hasNext()) {
            DOEParameter doeParameter = (DOEParameter) itr.next();
            doeParameter.setCurrent2Default();
        }
    }

    @Override
    public void computeDefault() {
        Iterator itr = childParameters.iterator();
        while (itr.hasNext()) {
            DOEParameter doeParameter = (DOEParameter) itr.next();
            doeParameter.computeDefault();
        }
    }

    @Override
    public boolean next(boolean wrapAround) {
        Boolean n = null;
        Iterator itr = childParameters.iterator();
        while (itr.hasNext()) {
            DOEParameter doeParameter = (DOEParameter) itr.next();
            n = doeParameter.next(wrapAround);
        }
        if (hasNext()) {
            currentPointNumber++;
        } else {
            currentPointNumber = 1;
        }
        return true;
    }

    @Override
    public void reset() {
        Iterator itr = childParameters.iterator();
        while (itr.hasNext()) {
            DOEParameter doeParameter = (DOEParameter) itr.next();
            doeParameter.reset();
        }
        this.currentPointNumber = 0;
    }

    @Override
    public void computeNumberOfValues() {
        this.numberOfValues = numberPoints;
    }
}
