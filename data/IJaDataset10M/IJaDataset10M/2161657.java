package com.manydesigns.portofino.base.portal;

import com.manydesigns.portofino.base.*;
import com.manydesigns.portofino.base.search.TextMatchMode;
import com.manydesigns.portofino.util.Escape;
import org.jfree.chart.urls.CategoryURLGenerator;
import org.jfree.data.category.CategoryDataset;
import java.io.UnsupportedEncodingException;

/**
 * @author Paolo Predonzani - paolo.predonzani@manydesigns.com
 * @author Angelo Lupo      - angelo.lupo@manydesigns.com
 */
public class MD2DPrptCategoryURLGenerator implements CategoryURLGenerator {

    public static final String copyright = "Copyright (c) 2005-2009, ManyDesigns srl";

    private final String prefix;

    private final MDAttribute actualPrimaryDimension;

    private final MDAttribute actualSecondaryDimension;

    private final String fullAttr1Name;

    private final String fullAttr2Name;

    public MD2DPrptCategoryURLGenerator(String prefix, MDAttribute primaryDimension, MDAttribute actualPrimaryDimension, MDAttribute secondaryDimension, MDAttribute actualSecondaryDimension) {
        this.prefix = prefix;
        this.actualPrimaryDimension = actualPrimaryDimension;
        this.actualSecondaryDimension = actualSecondaryDimension;
        StringBuffer sb = new StringBuffer();
        sb.append(primaryDimension.getName());
        while (primaryDimension != actualPrimaryDimension) {
            MDClass oppEndCls = ((MDRelAttribute) primaryDimension).getOppositeEndCls();
            primaryDimension = oppEndCls.getContext();
            sb.append("_");
            sb.append(primaryDimension.getName());
        }
        fullAttr1Name = sb.toString();
        sb = new StringBuffer();
        sb.append(secondaryDimension.getName());
        while (secondaryDimension != actualSecondaryDimension) {
            MDClass oppEndCls = ((MDRelAttribute) secondaryDimension).getOppositeEndCls();
            secondaryDimension = oppEndCls.getContext();
            sb.append("_");
            sb.append(secondaryDimension.getName());
        }
        fullAttr2Name = sb.toString();
    }

    public String generateURL(CategoryDataset dataset, int series, int category) {
        Comparable c1 = dataset.getRowKey(series);
        Comparable c1c = ((ComparableWithLabel) c1).getComparable();
        String c1label = c1.toString();
        Comparable c2 = dataset.getColumnKey(category);
        Comparable c2c = ((ComparableWithLabel) c2).getComparable();
        String c2label = c2.toString();
        StringBuffer url = new StringBuffer(prefix);
        try {
            addDimentionString(actualPrimaryDimension, fullAttr1Name, c2c, c2label, url);
            addDimentionString(actualSecondaryDimension, fullAttr2Name, c1c, c1label, url);
        } catch (UnsupportedEncodingException e) {
            url.setLength(0);
        }
        return url.toString();
    }

    private void addDimentionString(MDAttribute dimension, String fullAttrName, final Comparable c2, final String label, final StringBuffer url) throws UnsupportedEncodingException {
        if (dimension instanceof MDDateAttribute || dimension instanceof MDDecimalAttribute || dimension instanceof MDIntegerAttribute) {
            url.append("&attrmin_");
            url.append(Escape.urlencode(fullAttrName));
            url.append("=");
            url.append(Escape.urlencode(label));
            url.append("&attrmax_");
            url.append(Escape.urlencode(fullAttrName));
            url.append("=");
            url.append(Escape.urlencode(label));
        } else if (dimension instanceof MDTextAttribute) {
            url.append("&attr_");
            url.append(Escape.urlencode(fullAttrName));
            url.append("=");
            url.append(Escape.urlencode(c2.toString()));
            url.append("&attrmode_");
            url.append(Escape.urlencode(fullAttrName));
            url.append("=" + TextMatchMode.EQUALS.getStringValue());
        } else {
            url.append("&attr_");
            url.append(Escape.urlencode(fullAttrName));
            url.append("=");
            url.append(Escape.urlencode(c2.toString()));
        }
    }
}
