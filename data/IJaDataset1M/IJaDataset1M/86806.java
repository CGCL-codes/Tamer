package org.hl7.xml;

import org.hl7.meta.Datatype;
import org.hl7.types.ANY;
import org.hl7.types.QSET;
import org.hl7.types.QTY;
import org.hl7.types.enums.SetOperator;
import org.hl7.types.impl.IVLimpl;
import org.hl7.types.impl.PIVLimpl;
import org.hl7.types.impl.QSETDifferenceImpl;
import org.hl7.types.impl.QSETIntersectionImpl;
import org.hl7.types.impl.QSETPeriodicHullImpl;
import org.hl7.types.impl.QSETSingularityImpl;
import org.hl7.types.impl.QSETUnionImpl;
import org.hl7.types.impl.QSETnull;
import org.hl7.xml.builder.BuilderException;
import org.hl7.xml.builder.DatatypeBuilder;
import org.hl7.xml.builder.RimGraphXMLSpeaker;
import org.hl7.xml.parser.TreeContentHandler;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

public class QSETPresenter extends DatatypePresenterBase {

    private static final String ATTR_OPERATOR = "operator";

    private static final String ATTR_COMP = "comp";

    private static final QSETPresenter INSTANCE = new QSETPresenter();

    protected static class QSETContentHandler extends TreeContentHandler {

        Datatype _datatype = null;

        public QSETContentHandler(String namespaceURI, String localName, String qName, Attributes atts, Datatype datatype) {
            super(namespaceURI, localName, qName, atts);
            _datatype = datatype;
        }

        protected void returnResult(Object intermediate) throws SAXException {
            TreeContentHandler.Element element = (TreeContentHandler.Element) intermediate;
            QSET result = valueOf(element);
            super.returnResult(result);
        }

        protected QSET valueOf(TreeContentHandler.Element element) {
            String nullFlavorString = element.getAttributeValue(DatatypePresenterBase.ATTR_NULL_FLAVOR);
            if (nullFlavorString != null) {
                return QSETnull.valueOf(nullFlavorString);
            }
            return QSETnull.NI;
        }
    }

    private static class QSETBuilder implements DatatypeBuilder<QSET> {

        public void build(RimGraphXMLSpeaker.ContentBuilder builder, QSET value, String localName) throws BuilderException {
            QSET qset = (QSET) value;
            if (isAQSETUnion(qset)) {
                QSETUnionImpl union = (QSETUnionImpl) qset;
                for (int i = 0; i < union.size(); i++) {
                    QSET arg = union.getArgument(i);
                    if (isAnIVL(arg)) {
                        if (i != 0) builder.addAttribute(ATTR_OPERATOR, SetOperator.Include.code());
                        IVLPresenter.instance().getBuilder().build(builder, arg, localName);
                    } else if (isAPIVL(arg)) {
                        if (i != 0) builder.addAttribute(ATTR_OPERATOR, SetOperator.Include.code());
                        PIVLPresenter.instance().getBuilder().build(builder, arg, localName);
                    } else if (isAQSET(arg)) {
                        try {
                            if (i != 0) builder.addAttribute(ATTR_OPERATOR, SetOperator.Include.code());
                            builder.addAttribute(DatatypePresenterBase.W3_XML_SCHEMA, "type", "xsi:type", "CDATA", "SXPR_TS");
                            builder.startElement(DatatypePresenterBase.HL7_URI, localName, localName, builder.getAttributes());
                            QSETPresenter.instance().getBuilder().build(builder, arg, ATTR_COMP);
                            builder.endElement(DatatypePresenterBase.HL7_URI, localName, localName);
                        } catch (SAXException ex) {
                            throw new BuilderException(ex);
                        }
                    }
                }
            } else if (isAQSETSingularity(qset)) {
                QSETSingularityImpl singularity = (QSETSingularityImpl) qset;
                QTY arg = singularity.getElement();
                QTYPresenter.instance().getBuilder().build(builder, arg, localName);
            } else if (isAQSETIntersection(qset)) {
                QSETIntersectionImpl intersection = (QSETIntersectionImpl) qset;
                for (int i = 0; i < intersection.size(); i++) {
                    QSET arg = intersection.getArgument(i);
                    if (isAQSET(arg)) {
                        try {
                            if (i != 0) builder.addAttribute(ATTR_OPERATOR, SetOperator.Intersect.code());
                            builder.addAttribute(DatatypePresenterBase.W3_XML_SCHEMA, "type", "xsi:type", "CDATA", "SXPR_TS");
                            builder.startElement(DatatypePresenterBase.HL7_URI, localName, localName, builder.getAttributes());
                            QSETPresenter.instance().getBuilder().build(builder, arg, ATTR_COMP);
                            builder.endElement(DatatypePresenterBase.HL7_URI, localName, localName);
                        } catch (SAXException ex) {
                            throw new BuilderException(ex);
                        }
                    } else if (isAnIVL(arg)) {
                        if (i != 0) builder.addAttribute(ATTR_OPERATOR, SetOperator.Intersect.code());
                        IVLPresenter.instance().getBuilder().build(builder, arg, localName);
                    } else if (isAPIVL(arg)) {
                        if (i != 0) builder.addAttribute(ATTR_OPERATOR, SetOperator.Intersect.code());
                        PIVLPresenter.instance().getBuilder().build(builder, arg, localName);
                    }
                }
            } else if (isAQSETDifference(qset)) {
                QSETDifferenceImpl diff = (QSETDifferenceImpl) qset;
                QSET subtrahend = diff.getSubtrahend();
                QSET minuend = diff.getMinuend();
                if (isAQSET(minuend)) {
                    try {
                        builder.addAttribute(DatatypePresenterBase.W3_XML_SCHEMA, "type", "xsi:type", "CDATA", "SXPR_TS");
                        builder.startElement(DatatypePresenterBase.HL7_URI, localName, localName, builder.getAttributes());
                        QSETPresenter.instance().getBuilder().build(builder, minuend, ATTR_COMP);
                        builder.endElement(DatatypePresenterBase.HL7_URI, localName, localName);
                    } catch (SAXException ex) {
                        throw new BuilderException(ex);
                    }
                } else if (isAnIVL(minuend)) {
                    IVLPresenter.instance().getBuilder().build(builder, minuend, localName);
                } else if (isAPIVL(minuend)) {
                    PIVLPresenter.instance().getBuilder().build(builder, minuend, localName);
                }
                if (isAQSET(subtrahend)) {
                    try {
                        builder.addAttribute(ATTR_OPERATOR, SetOperator.Exclude.code());
                        builder.addAttribute(DatatypePresenterBase.W3_XML_SCHEMA, "type", "xsi:type", "CDATA", "SXPR_TS");
                        builder.startElement(DatatypePresenterBase.HL7_URI, localName, localName, builder.getAttributes());
                        QSETPresenter.instance().getBuilder().build(builder, subtrahend, ATTR_COMP);
                        builder.endElement(DatatypePresenterBase.HL7_URI, localName, localName);
                    } catch (SAXException ex) {
                        throw new BuilderException(ex);
                    }
                } else if (isAnIVL(subtrahend)) {
                    builder.addAttribute(ATTR_OPERATOR, SetOperator.Exclude.code());
                    IVLPresenter.instance().getBuilder().build(builder, subtrahend, localName);
                } else if (isAPIVL(subtrahend)) {
                    builder.addAttribute(ATTR_OPERATOR, SetOperator.Exclude.code());
                    PIVLPresenter.instance().getBuilder().build(builder, subtrahend, localName);
                }
            } else if (isAQSETPeriodicHull(qset)) {
                QSETPeriodicHullImpl ph = (QSETPeriodicHullImpl) qset;
                QSET thisset = ph.getThisSet();
                QSET thatset = ph.getThatSet();
                if (isAQSET(thisset)) {
                    try {
                        builder.addAttribute(DatatypePresenterBase.W3_XML_SCHEMA, "type", "xsi:type", "CDATA", "SXPR_TS");
                        builder.startElement(DatatypePresenterBase.HL7_URI, localName, localName, builder.getAttributes());
                        QSETPresenter.instance().getBuilder().build(builder, thisset, ATTR_COMP);
                        builder.endElement(DatatypePresenterBase.HL7_URI, localName, localName);
                    } catch (SAXException ex) {
                        throw new BuilderException(ex);
                    }
                } else if (isAnIVL(thisset)) {
                    IVLPresenter.instance().getBuilder().build(builder, thisset, localName);
                } else if (isAPIVL(thisset)) {
                    PIVLPresenter.instance().getBuilder().build(builder, thisset, localName);
                }
                if (isAQSET(thatset)) {
                    try {
                        builder.addAttribute(ATTR_OPERATOR, SetOperator.PeriodicHull.code());
                        builder.addAttribute(DatatypePresenterBase.W3_XML_SCHEMA, "type", "xsi:type", "CDATA", "SXPR_TS");
                        builder.startElement(DatatypePresenterBase.HL7_URI, localName, localName, builder.getAttributes());
                        QSETPresenter.instance().getBuilder().build(builder, thatset, ATTR_COMP);
                        builder.endElement(DatatypePresenterBase.HL7_URI, localName, localName);
                    } catch (SAXException ex) {
                        throw new BuilderException(ex);
                    }
                } else if (isAnIVL(thatset)) {
                    builder.addAttribute(ATTR_OPERATOR, SetOperator.PeriodicHull.code());
                    IVLPresenter.instance().getBuilder().build(builder, thatset, localName);
                } else if (isAPIVL(thatset)) {
                    builder.addAttribute(ATTR_OPERATOR, SetOperator.PeriodicHull.code());
                    PIVLPresenter.instance().getBuilder().build(builder, thatset, localName);
                }
            }
        }

        private boolean isAQSETUnion(ANY arg) {
            return (arg instanceof QSETUnionImpl);
        }

        private boolean isAQSETIntersection(ANY arg) {
            return arg instanceof QSETIntersectionImpl;
        }

        private boolean isAQSETDifference(ANY arg) {
            return (arg instanceof QSETDifferenceImpl);
        }

        private boolean isAQSETPeriodicHull(ANY arg) {
            return (arg instanceof QSETPeriodicHullImpl);
        }

        private boolean isAQSETSingularity(ANY arg) {
            return (arg instanceof QSETSingularityImpl);
        }

        private boolean isAQSET(ANY arg) {
            return (isAQSETSingularity(arg) || isAQSETDifference(arg) || isAQSETUnion(arg) || isAQSETPeriodicHull(arg) || isAQSETIntersection(arg));
        }

        private boolean isAnIVL(ANY arg) {
            return (arg instanceof IVLimpl);
        }

        private boolean isAPIVL(ANY arg) {
            return (arg instanceof PIVLimpl);
        }

        public void buildStructural(RimGraphXMLSpeaker.ContentBuilder builder, QSET value, String localName) throws BuilderException {
            throw new BuilderException("QSET<T> cannot be a structural attribute");
        }
    }

    private QSETPresenter() {
    }

    public static QSETPresenter instance() {
        return INSTANCE;
    }

    public ContentHandler getContentHandler(String namespaceURI, String localName, String qName, Attributes atts, Datatype datatype) {
        return new QSETContentHandler(namespaceURI, localName, qName, atts, datatype);
    }

    public DatatypeBuilder getBuilder() {
        return new QSETBuilder();
    }
}
