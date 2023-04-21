package org.apache.batik.dom.svg;

import java.util.StringTokenizer;
import org.apache.batik.util.SVGConstants;
import org.apache.batik.parser.ParseException;
import org.apache.batik.parser.DefaultPreserveAspectRatioHandler;
import org.apache.batik.parser.PreserveAspectRatioHandler;
import org.apache.batik.parser.PreserveAspectRatioParser;
import org.w3c.dom.DOMException;
import org.w3c.dom.svg.SVGPreserveAspectRatio;

/**
 * Abstract implementation for SVGPreservAspectRatio
 *
 * This is the base implementation for SVGPreservAspectRatio
 *
 * @author  Tonny Kohar
 */
public abstract class AbstractSVGPreserveAspectRatio implements SVGPreserveAspectRatio {

    /**
     * align property by default the value is
     * SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMIDYMID
     */
    protected short align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMIDYMID;

    /**
     * meetOrSlice property
     * by default the value is SVGPreserveAspectRatio.SVG_MEETORSLICE_MEET;
     */
    protected short meetOrSlice = SVGPreserveAspectRatio.SVG_MEETORSLICE_MEET;

    /** Creates a new instance of AbstractSVGPreserveAspectRatio */
    public AbstractSVGPreserveAspectRatio() {
    }

    public short getAlign() {
        return this.align;
    }

    public short getMeetOrSlice() {
        return this.meetOrSlice;
    }

    public void setAlign(short align) {
        this.align = align;
        setAttributeValue(getValueAsString());
    }

    public void setMeetOrSlice(short meetOrSlice) {
        this.meetOrSlice = meetOrSlice;
        setAttributeValue(getValueAsString());
    }

    public void reset() {
        align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMIDYMID;
        meetOrSlice = SVGPreserveAspectRatio.SVG_MEETORSLICE_MEET;
    }

    protected abstract void setAttributeValue(String value) throws DOMException;

    protected abstract DOMException createDOMException(short type, String key, Object[] args);

    protected void setValueAsString(String value) throws DOMException {
        PreserveAspectRatioParserHandler ph;
        ph = new PreserveAspectRatioParserHandler();
        try {
            PreserveAspectRatioParser p = new PreserveAspectRatioParser();
            p.setPreserveAspectRatioHandler(ph);
            p.parse(value);
            align = ph.getAlign();
            meetOrSlice = ph.getMeetOrSlice();
        } catch (ParseException ex) {
            throw createDOMException(SVG_PRESERVEASPECTRATIO_UNKNOWN, "invalid value for preserveAspectRatio", null);
        }
    }

    /** Return the value of String to be used on setAttributeNS, in
     * other word the mapping of align meetOrSlice to representation
     * string use by SVG
     */
    protected String getValueAsString() {
        String value = null;
        switch(align) {
            case SVG_PRESERVEASPECTRATIO_NONE:
                value = SVGConstants.SVG_NONE_VALUE;
                return value;
            case SVG_PRESERVEASPECTRATIO_XMINYMIN:
                value = SVGConstants.SVG_XMINYMIN_VALUE;
                break;
            case SVG_PRESERVEASPECTRATIO_XMIDYMIN:
                value = SVGConstants.SVG_XMIDYMIN_VALUE;
                break;
            case SVG_PRESERVEASPECTRATIO_XMAXYMIN:
                value = SVGConstants.SVG_XMAXYMIN_VALUE;
                break;
            case SVG_PRESERVEASPECTRATIO_XMINYMID:
                value = SVGConstants.SVG_XMINYMID_VALUE;
                break;
            case SVG_PRESERVEASPECTRATIO_XMIDYMID:
                value = SVGConstants.SVG_XMIDYMID_VALUE;
                break;
            case SVG_PRESERVEASPECTRATIO_XMAXYMID:
                value = SVGConstants.SVG_XMAXYMID_VALUE;
                break;
            case SVG_PRESERVEASPECTRATIO_XMINYMAX:
                value = SVGConstants.SVG_XMINYMAX_VALUE;
                break;
            case SVG_PRESERVEASPECTRATIO_XMIDYMAX:
                value = SVGConstants.SVG_XMIDYMAX_VALUE;
                break;
            case SVG_PRESERVEASPECTRATIO_XMAXYMAX:
                value = SVGConstants.SVG_XMAXYMAX_VALUE;
                break;
            default:
                throw createDOMException(SVG_PRESERVEASPECTRATIO_UNKNOWN, "invalid value for preserveAspectRatio", null);
        }
        switch(meetOrSlice) {
            case SVG_MEETORSLICE_MEET:
                value = value + " " + SVGConstants.SVG_MEET_VALUE;
                break;
            case SVG_MEETORSLICE_SLICE:
                value = value + " " + SVGConstants.SVG_SLICE_VALUE;
                break;
            default:
                throw createDOMException(SVG_MEETORSLICE_UNKNOWN, "invalid value for preserveAspectRatio", null);
        }
        return value;
    }

    protected class PreserveAspectRatioParserHandler extends DefaultPreserveAspectRatioHandler {

        public short align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMIDYMID;

        public short meetOrSlice = SVGPreserveAspectRatio.SVG_MEETORSLICE_MEET;

        public short getAlign() {
            return align;
        }

        public short getMeetOrSlice() {
            return meetOrSlice;
        }

        /**
         * Invoked when 'none' been parsed.
         * @exception ParseException if an error occured while processing
         * the transform
         */
        public void none() throws ParseException {
            align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_NONE;
        }

        /**
         * Invoked when 'xMaxYMax' has been parsed.
         * @exception ParseException if an error occured while processing
         * the transform
         */
        public void xMaxYMax() throws ParseException {
            align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMAXYMAX;
        }

        /**
         * Invoked when 'xMaxYMid' has been parsed.
         * @exception ParseException if an error occured while processing
         * the transform
         */
        public void xMaxYMid() throws ParseException {
            align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMAXYMID;
        }

        /**
         * Invoked when 'xMaxYMin' has been parsed.
         * @exception ParseException if an error occured while processing
         * the transform
         */
        public void xMaxYMin() throws ParseException {
            align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMAXYMIN;
        }

        /**
         * Invoked when 'xMidYMax' has been parsed.
         * @exception ParseException if an error occured while processing
         * the transform
         */
        public void xMidYMax() throws ParseException {
            align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMIDYMAX;
        }

        /**
         * Invoked when 'xMidYMid' has been parsed.
         * @exception ParseException if an error occured while processing
         * the transform
         */
        public void xMidYMid() throws ParseException {
            align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMIDYMID;
        }

        /**
         * Invoked when 'xMidYMin' has been parsed.
         * @exception ParseException if an error occured while processing
         * the transform
         */
        public void xMidYMin() throws ParseException {
            align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMIDYMIN;
        }

        /**
         * Invoked when 'xMinYMax' has been parsed.
         * @exception ParseException if an error occured while processing
         * the transform
         */
        public void xMinYMax() throws ParseException {
            align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMINYMAX;
        }

        /**
         * Invoked when 'xMinYMid' has been parsed.
         * @exception ParseException if an error occured while processing
         * the transform
         */
        public void xMinYMid() throws ParseException {
            align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMINYMID;
        }

        /**
         * Invoked when 'xMinYMin' has been parsed.
         * @exception ParseException if an error occured while processing
         * the transform
         */
        public void xMinYMin() throws ParseException {
            align = SVGPreserveAspectRatio.SVG_PRESERVEASPECTRATIO_XMINYMIN;
        }

        /**
         * Invoked when 'meet' has been parsed.
         * @exception ParseException if an error occured while processing
         * the transform
         */
        public void meet() throws ParseException {
            meetOrSlice = SVGPreserveAspectRatio.SVG_MEETORSLICE_MEET;
        }

        /**
         * Invoked when 'slice' has been parsed.
         * @exception ParseException if an error occured while processing
         * the transform
         */
        public void slice() throws ParseException {
            meetOrSlice = SVGPreserveAspectRatio.SVG_MEETORSLICE_SLICE;
        }
    }
}
