package org.hl7.xml;

import org.hl7.meta.Datatype;
import org.hl7.meta.ParametrizedDatatype;
import org.hl7.meta.UnknownDatatypeException;
import org.hl7.meta.impl.DatatypeMetadataFactoryDatatypes;
import org.hl7.meta.impl.DatatypeMetadataFactoryImpl;
import org.hl7.types.QTY;
import org.hl7.types.RTO;
import org.hl7.types.ValueFactory;
import org.hl7.types.impl.RTOnull;
import org.hl7.util.FactoryException;
import org.hl7.xml.builder.BuilderException;
import org.hl7.xml.builder.DatatypeBuilder;
import org.hl7.xml.builder.RimGraphXMLSpeaker;
import org.hl7.xml.parser.DynamicContentHandler;
import org.hl7.xml.parser.SimpleTypeContentHandler;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * This little guy knows how to build reals. <p/> FIXME: defaults are not configurable.
 */
public class RTOPresenter extends DatatypePresenterBase {

    public static final String TAG_NUMERATOR = "numerator";

    public static final String TAG_DENOMINATOR = "denominator";

    public static final String XSI_TYPE_LOCAL_NAME = "type";

    private static final RTOPresenter INSTANCE = new RTOPresenter();

    private RTOPresenter() {
    }

    public static RTOPresenter instance() {
        return INSTANCE;
    }

    private static class RTOContentHandler extends SimpleTypeContentHandler implements DynamicContentHandler.ResultReceiver {

        ParametrizedDatatype _expectedRTODatatype = null;

        String _currentComponentName = null;

        RTO _result = null;

        QTY.diff _numerator = null;

        QTY.diff _denominator = null;

        boolean _parsingContent = false;

        private RTOContentHandler(ParametrizedDatatype datatype) {
            _expectedRTODatatype = datatype;
        }

        protected void notifyActivation(Attributes atts) {
            String nullFlavorString = atts.getValue(DatatypePresenterBase.ATTR_NULL_FLAVOR);
            if (nullFlavorString != null) {
                _result = RTOnull.valueOf(nullFlavorString);
                _parsingContent = false;
            } else _parsingContent = true;
            if (atts != null) {
                String xsiType = atts.getValue(DatatypePresenterBase.W3_XML_SCHEMA, XSI_TYPE_LOCAL_NAME);
                if (xsiType != null) {
                    try {
                        ParametrizedDatatype datatypeFromXsiType = (ParametrizedDatatype) DatatypeMetadataFactoryImpl.instance().createByXsiType(xsiType);
                        _expectedRTODatatype = datatypeFromXsiType;
                    } catch (UnknownDatatypeException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }

        protected Object getResult() {
            if (_result != null) return _result; else if (_numerator == null && _denominator == null) return RTOnull.NI; else return ValueFactory.getInstance().RTOvalueOf(_numerator, _denominator);
        }

        public void notifyResult(Object result) {
            if (_currentComponentName == TAG_NUMERATOR) _numerator = (QTY.diff) result; else if (_currentComponentName == TAG_DENOMINATOR) _denominator = (QTY.diff) result; else throw new Error("internal error, current component: " + _currentComponentName);
        }

        public void startElement(String namespaceURI, String localName, String qName, Attributes atts) {
            if (_parsingContent) {
                localName = localName.intern();
                if (localName == TAG_NUMERATOR || localName == TAG_DENOMINATOR) {
                    _currentComponentName = localName;
                    Datatype effectiveComponentDatatype = null;
                    if (_expectedRTODatatype != null) {
                        if (_currentComponentName == TAG_NUMERATOR) effectiveComponentDatatype = _expectedRTODatatype.getParameter(0);
                        if (_currentComponentName == TAG_DENOMINATOR) effectiveComponentDatatype = _expectedRTODatatype.getParameter(1);
                    }
                    if (atts != null) {
                        String xsiType = atts.getValue(DatatypePresenterBase.W3_XML_SCHEMA, XSI_TYPE_LOCAL_NAME);
                        if (xsiType != null) {
                            try {
                                Datatype datatypeFromXsiType = DatatypeMetadataFactoryImpl.instance().createByXsiType(xsiType);
                                effectiveComponentDatatype = datatypeFromXsiType;
                            } catch (UnknownDatatypeException ex) {
                                throw new RuntimeException(ex);
                            }
                        }
                    }
                    if (effectiveComponentDatatype == null) throw new RuntimeException("data type to parse could not be determined");
                    try {
                        suspendWith(effectiveComponentDatatype.getHandler(namespaceURI, localName, qName, atts), atts);
                    } catch (FactoryException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        }
    }

    private static class RTOBuilder implements DatatypeBuilder<RTO> {

        public void build(RimGraphXMLSpeaker.ContentBuilder builder, RTO value, String localName) throws BuilderException {
            try {
                RTO rtoValue = (RTO) value;
                if (builder.nullValueHandled(value, localName)) return;
                builder.startElement(localName);
                QTY.diff numerator = rtoValue.numerator();
                QTY.diff denominator = rtoValue.denominator();
                builder.setTypeConstraint(DatatypeMetadataFactoryDatatypes.instance().QTYTYPE);
                QTYPresenter.instance().getBuilder().build(builder, numerator, TAG_NUMERATOR);
                builder.setTypeConstraint(DatatypeMetadataFactoryDatatypes.instance().QTYTYPE);
                QTYPresenter.instance().getBuilder().build(builder, denominator, TAG_DENOMINATOR);
                builder.endElement(localName);
            } catch (SAXException ex) {
                throw new BuilderException(ex);
            }
        }

        public void buildStructural(RimGraphXMLSpeaker.ContentBuilder builder, RTO value, String localName) throws BuilderException {
            throw new BuilderException("RTO cannot be a structural attribute");
        }
    }

    public ContentHandler getContentHandler(String namespaceURI, String localName, String qName, Attributes atts, Datatype datatype) {
        return new RTOContentHandler((ParametrizedDatatype) datatype);
    }

    public DatatypeBuilder getBuilder() {
        return new RTOBuilder();
    }
}
