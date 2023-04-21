package org.goodoldai.jeff.report.xml;

import org.goodoldai.jeff.explanation.DataExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationChunk;
import org.goodoldai.jeff.explanation.ExplanationException;
import org.goodoldai.jeff.explanation.data.Dimension;
import org.goodoldai.jeff.explanation.data.OneDimData;
import org.goodoldai.jeff.explanation.data.SingleData;
import org.goodoldai.jeff.explanation.data.ThreeDimData;
import org.goodoldai.jeff.explanation.data.Triple;
import org.goodoldai.jeff.explanation.data.Tuple;
import org.goodoldai.jeff.explanation.data.TwoDimData;
import java.util.ArrayList;
import java.util.Iterator;
import org.dom4j.Document;
import org.dom4j.Element;
import org.goodoldai.jeff.report.ReportChunkBuilder;

/**
 * A concrete builder for transforming data explanation chunks into pieces 
 * of XML report
 *
 * @author Boris Horvat
 */
public class XMLDataChunkBuilder implements ReportChunkBuilder {

    /**
     * Initializes the builder
     */
    public XMLDataChunkBuilder() {
    }

    /**
     * This method transforms a data explanation chunk into an XML report piece
     * and writes this piece into the provided xml document which is, in this
     * case, an instance of org.dom4j.Document. The method first collects all
     * general chunk data (context, rule, group, tags) and inserts them into 
     * the report, and then retrieves the chunk content. Since the content can 
     * be a SingleData, OneDimData, TwoDimData or a ThreeDimData instance,
     * dimension details and concrete data are transformed into XML and 
     * inserted.
     *
     * In all cases, if the dimension unit is missing it should be omitted from
     * the report.
     *
     * @param echunk data explanation chunk that needs to be transformed
     * @param stream output stream to which the transformed chunk will be
     * written in as an xml document (in this case org.dom4j.Document)
     *  @param insertHeaders denotes if chunk headers should be inserted into the
     * report (true) or not (false)
     *
     * @throws org.goodoldai.jeff.explanation.ExplanationException if any of the arguments are
     * null, if the entered chunk is not a DataExplanationChunk instance or if 
     * the entered output stream type is not org.dom4j.Document
     */
    public void buildReportChunk(ExplanationChunk echunk, Object stream, boolean insertHeaders) {
        if (echunk == null && stream == null) {
            throw new ExplanationException("All of the arguments are mandatory, so they can not be null");
        }
        if (echunk == null) {
            throw new ExplanationException("The argument 'echunk' is mandatory, so it can not be null");
        }
        if (stream == null) {
            throw new ExplanationException("The argument 'stream' is mandatory, so it can not be null");
        }
        if (!(echunk instanceof DataExplanationChunk)) {
            throw new ExplanationException("The ExplanationChunk must be type of DataExplanationChunk");
        }
        if (!(stream instanceof Document)) {
            throw new ExplanationException("The stream must be the type of org.dom4j.Document");
        }
        Document document = (Document) stream;
        Element element = document.getRootElement().addElement("dataExplanation");
        if (insertHeaders) XMLChunkUtility.insertExplanationInfo(echunk, element);
        DataExplanationChunk dataExplenationChunk = (DataExplanationChunk) echunk;
        insertContent(dataExplenationChunk, element);
    }

    /**
     * This is a private method that is used to insert content into the document,
     * this method first checks to see what type of content is (SingleData, OneDimData,
     * TwoDimData or a ThreeDimData ) and than calls the right method to insert
     * the content into the document (an instance of org.dom4j.Document)
     *
     * @param imageExplanationChunk image explanation chunk which holds the content
     * that needs to be transformed
     * @param element element in which the content of the transformed chunk will be
     * written in as an xml document (in this case org.dom4j.Document)
     */
    private void insertContent(DataExplanationChunk dataExplenationChunk, Element element) {
        if (dataExplenationChunk.getContent() instanceof SingleData) {
            inputSingleDataContent(dataExplenationChunk, element);
        } else if (dataExplenationChunk.getContent() instanceof OneDimData) {
            inputOneDimDataContent(dataExplenationChunk, element);
        } else if (dataExplenationChunk.getContent() instanceof TwoDimData) {
            inputTwoDimDataContent(dataExplenationChunk, element);
        } else if (dataExplenationChunk.getContent() instanceof ThreeDimData) {
            inputThreeDimDataContent(dataExplenationChunk, element);
        }
    }

    /**
     * This is a private method that is used to insert content into the document.
     * The content must be the instance of explanation.data.SingleData
     *
     * @param dataExplenationChunk data explanation chunk which holds the content
     * that needs to be transformed
     * @param element element in which the content of the transformed chunk will be
     * written in as an xml document (in this case org.dom4j.Document)
     */
    private void inputSingleDataContent(DataExplanationChunk dataExplenationChunk, Element element) {
        SingleData singleData = (SingleData) dataExplenationChunk.getContent();
        String value = String.valueOf(singleData.getValue());
        Dimension dimension = singleData.getDimension();
        String dimensionName = dimension.getName();
        String dimensionUnit = dimension.getUnit();
        Element contentElement = element.addElement("content").addText(value);
        contentElement.addAttribute("dimensionName", dimensionName);
        if (dimensionUnit != null) {
            contentElement.addAttribute("dimensionUnit", dimensionUnit);
        }
    }

    /**
     * This is a private method that is used to insert content into the document.
     * The content must be the instance of explanation.data.OneDimData
     *
     * @param dataExplenationChunk data explanation chunk which holds the content
     * that needs to be transformed
     * @param element element in which the content of the transformed chunk will be
     * written in as an xml document (in this case org.dom4j.Document)
     */
    private void inputOneDimDataContent(DataExplanationChunk dataExplenationChunk, Element element) {
        OneDimData oneDimData = (OneDimData) dataExplenationChunk.getContent();
        ArrayList<Object> objectValues = oneDimData.getValues();
        ArrayList<String> values = new ArrayList<String>();
        for (Iterator<Object> it = objectValues.iterator(); it.hasNext(); ) {
            Object object = it.next();
            values.add(String.valueOf(object));
        }
        Dimension dimension = oneDimData.getDimension();
        String dimensionName = dimension.getName();
        String dimensionUnit = dimension.getUnit();
        Element contentElement = element.addElement("content");
        for (Iterator<String> it = values.iterator(); it.hasNext(); ) {
            String value = it.next();
            contentElement.addElement("value").addText(value);
        }
        contentElement.addAttribute("dimensionName", dimensionName);
        if (dimensionUnit != null) {
            contentElement.addAttribute("dimensionUnit", dimensionUnit);
        }
    }

    /**
     * This is a private method that is used to insert content into the document.
     * The content must be the instance of explanation.data.TwoDimData
     *
     * @param dataExplenationChunk data explanation chunk which holds the content
     * that needs to be transformed
     * @param element element in which the content of the transformed chunk will be
     * written in as an xml document (in this case org.dom4j.Document)
     */
    private void inputTwoDimDataContent(DataExplanationChunk dataExplenationChunk, Element element) {
        TwoDimData twoDimData = (TwoDimData) dataExplenationChunk.getContent();
        ArrayList<Tuple> tupleValues = twoDimData.getValues();
        Dimension dimension1 = twoDimData.getDimension1();
        String dimensionName1 = dimension1.getName();
        String dimensionUnit1 = dimension1.getUnit();
        Dimension dimension2 = twoDimData.getDimension2();
        String dimensionName2 = dimension2.getName();
        String dimensionUnit2 = dimension2.getUnit();
        Element contentElement = element.addElement("content");
        Element subContentElement = contentElement.addElement("tupleValue");
        for (Iterator<Tuple> it = tupleValues.iterator(); it.hasNext(); ) {
            Tuple tuple = it.next();
            String value1 = String.valueOf(tuple.getValue1());
            String value2 = String.valueOf(tuple.getValue2());
            Element value1Element = subContentElement.addElement("value1").addText(value1);
            value1Element.addAttribute("dimensionName", dimensionName1);
            if (dimensionUnit1 != null) {
                value1Element.addAttribute("dimensionUnit", dimensionUnit1);
            }
            Element value2Element = subContentElement.addElement("value2").addText(value2);
            value2Element.addAttribute("dimensionName", dimensionName2);
            if (dimensionUnit2 != null) {
                value2Element.addAttribute("dimensionUnit", dimensionUnit2);
            }
        }
    }

    /**
     * This is a private method that is used to insert content into the document.
     * The content must be the instance of explanation.data.ThreeDimData
     *
     * @param dataExplenationChunk data explanation chunk which holds the content
     * that needs to be transformed
     * @param element element in which the content of the transformed chunk will be
     * written in as an xml document (in this case org.dom4j.Document)
     */
    private void inputThreeDimDataContent(DataExplanationChunk dataExplenationChunk, Element element) {
        ThreeDimData threeDimData = (ThreeDimData) dataExplenationChunk.getContent();
        ArrayList<Triple> tripleValues = threeDimData.getValues();
        Dimension dimension1 = threeDimData.getDimension1();
        String dimensionName1 = dimension1.getName();
        String dimensionUnit1 = dimension1.getUnit();
        Dimension dimension2 = threeDimData.getDimension2();
        String dimensionName2 = dimension2.getName();
        String dimensionUnit2 = dimension2.getUnit();
        Dimension dimension3 = threeDimData.getDimension3();
        String dimensionName3 = dimension3.getName();
        String dimensionUnit3 = dimension3.getUnit();
        Element contentElement = element.addElement("content");
        Element subContentElement = contentElement.addElement("tripleValue");
        for (Iterator<Triple> it = tripleValues.iterator(); it.hasNext(); ) {
            Triple triple = it.next();
            String value1 = String.valueOf(triple.getValue1());
            String value2 = String.valueOf(triple.getValue2());
            String value3 = String.valueOf(triple.getValue3());
            Element value1Element = subContentElement.addElement("value1").addText(value1);
            value1Element.addAttribute("dimensionName", dimensionName1);
            if (dimensionUnit1 != null) {
                value1Element.addAttribute("dimensionUnit", dimensionUnit1);
            }
            Element value2Element = subContentElement.addElement("value2").addText(value2);
            value2Element.addAttribute("dimensionName", dimensionName2);
            if (dimensionUnit2 != null) {
                value2Element.addAttribute("dimensionUnit", dimensionUnit2);
            }
            Element value3Element = subContentElement.addElement("value3").addText(value3);
            value3Element.addAttribute("dimensionName", dimensionName3);
            if (dimensionUnit3 != null) {
                value3Element.addAttribute("dimensionUnit", dimensionUnit3);
            }
        }
    }
}
