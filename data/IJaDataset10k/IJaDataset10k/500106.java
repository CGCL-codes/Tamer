package com.loribel.tools.sa.action;

import java.util.ArrayList;
import java.util.List;
import com.loribel.commons.abstraction.GB_LabelIcon;
import com.loribel.commons.abstraction.GB_LabelIconOwner;
import com.loribel.commons.business.GB_BOMetaDataTools;
import com.loribel.commons.business.abstraction.GB_BOConfigurable;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.commons.exception.GB_ConfigException;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.GB_StringTools;
import com.loribel.commons.util.STools;
import com.loribel.commons.util.string.GB_SAAbstract;
import com.loribel.tools.sa.bo.GB_SACsv2XmlBO;

/**
 * SA - Transform CVS to XML.
 *
 * @author Gr�gory Borelli
 */
public class GB_SACsv2Xml2 extends GB_SAAbstract implements GB_LabelIconOwner, GB_BOConfigurable {

    private GB_SACsv2XmlBO config;

    private List header;

    private String lineStart = "";

    private String lineEnd = "";

    private String tagStartA = "";

    private String tagStartB = "";

    private String tagEndA = "";

    private String tagEndB = "";

    public GB_SACsv2Xml2() {
        this(new GB_SACsv2XmlBO());
    }

    public GB_SACsv2Xml2(GB_SACsv2XmlBO a_config) {
        super();
        setConfig(a_config);
    }

    /**
     * Ex�cute l'action.
     * Effectue les remplacements.
     */
    public String doActionStr(String a_string) {
        if (STools.isNull(a_string)) {
            return a_string;
        }
        if (getSeparator().length() == 0) {
            throw new NullPointerException("Separator cannot be null.");
        }
        if (config.isGenHtmlTable()) {
            return transform2HtmlTable(a_string);
        }
        if (config.isUseHeader()) {
            String l_line = (String) GB_StringTools.toLines(a_string).get(0);
            header = parseHeader(l_line);
        }
        switch(config.getColumnTransform()) {
            case GB_SACsv2XmlBO.COLUMN_TRANSFORM.ATTRIBUTES:
                lineStart = "<line>\n";
                lineEnd = "</line>\n";
                tagStartA = "  <col";
                tagStartB = ">";
                tagEndA = "</col";
                tagEndB = ">\n";
                break;
            case GB_SACsv2XmlBO.COLUMN_TRANSFORM.ELEMENTS:
                lineStart = "<line";
                lineEnd = "/>\n";
                tagStartA = " col";
                tagStartB = "=\"";
                tagEndA = "\"";
                tagEndB = "";
                break;
            case GB_SACsv2XmlBO.COLUMN_TRANSFORM.AUTO:
                throw new IllegalArgumentException("Transform column AUTO no supported");
            case GB_SACsv2XmlBO.COLUMN_TRANSFORM.XPATH:
                throw new IllegalArgumentException("Transform column XPATH no supported");
        }
        return transformDefault(a_string);
    }

    protected String genTag(String a_value, int a_index) {
        if (STools.isNull(a_value)) {
            if (config.isIgnoreEmptyCol()) {
                return "";
            }
        }
        String retour = "";
        retour += tagStartA;
        if (tagStartB.length() > 0) {
            retour += a_index + tagStartB;
        }
        retour += a_value;
        retour += tagEndA;
        if (tagEndB.length() > 0) {
            retour += a_index + tagEndB;
        }
        return retour;
    }

    /**
     * getBOConfig
     *
     * @return GB_SimpleBusinessObject
     */
    public GB_SimpleBusinessObject getBOConfig() {
        return config;
    }

    /**
     * Returns labelIcon of this String action.
     */
    public GB_LabelIcon getLabelIcon() {
        return GB_BOMetaDataTools.getLabelIcon(GB_SACsv2XmlBO.BO_NAME);
    }

    private String getSeparator() {
        String retour;
        if (config.isUseTab()) {
            retour = "\t";
        } else {
            retour = config.getSeparator();
        }
        return retour;
    }

    protected List parseHeader(String a_line) {
        List retour = new ArrayList();
        String l_separator = getSeparator();
        int l_separatorLen = l_separator.length();
        int i = 1;
        int l_oldIndex = 0;
        int l_index = a_line.indexOf(l_separator);
        String l_value;
        while (l_index > -1) {
            l_value = a_line.substring(l_oldIndex, l_index);
            retour.add(l_value);
            l_oldIndex = l_index + l_separatorLen;
            l_index = a_line.indexOf(l_separator, l_oldIndex);
            i++;
        }
        if (l_oldIndex < a_line.length()) {
            l_value = a_line.substring(l_oldIndex);
            retour.add(l_value);
        }
        return retour;
    }

    /**
     * setBOConfig
     *
     * @param a_config GB_SimpleBusinessObject
     */
    public void setBOConfig(GB_SimpleBusinessObject a_config) throws GB_ConfigException {
        GB_SACsv2XmlBO l_config = null;
        try {
            l_config = (GB_SACsv2XmlBO) a_config;
        } catch (ClassCastException e) {
            throw new GB_ConfigException(e);
        }
        setConfig(l_config);
    }

    public void setConfig(GB_SACsv2XmlBO a_config) {
        config = a_config;
    }

    protected String transform2HtmlTable(String a_src) {
        lineStart = "  <tr>\n";
        lineEnd = "  </tr>\n";
        tagStartA = "    <td>";
        tagStartB = "";
        tagEndA = "</td>\n";
        tagEndB = "";
        String retour = "<table>\n";
        retour += transformDefault(a_src);
        retour += "</table>\n";
        return retour;
    }

    protected String transformDefault(String a_src) {
        List l_lines = GB_StringTools.toLines(a_src);
        int len = CTools.getSize(l_lines);
        if (len > 0) {
            if (config.isUseHeader()) {
                l_lines.remove(0);
                len--;
            }
            for (int i = 0; i < len; i++) {
                l_lines.set(i, transformLine((String) l_lines.get(i)));
            }
        }
        return GB_StringTools.toString(l_lines, "");
    }

    protected String transformLine(String a_line) {
        String l_separator = getSeparator();
        int l_separatorLen = l_separator.length();
        String retour = lineStart;
        int i = 1;
        int l_oldIndex = 0;
        int l_index = a_line.indexOf(l_separator);
        String tagValue;
        while (l_index > -1) {
            tagValue = a_line.substring(l_oldIndex, l_index);
            retour += genTag(tagValue, i);
            l_oldIndex = l_index + l_separatorLen;
            l_index = a_line.indexOf(l_separator, l_oldIndex);
            i++;
        }
        if (l_oldIndex < a_line.length()) {
            tagValue = a_line.substring(l_oldIndex);
            retour += genTag(tagValue, i);
        }
        retour += lineEnd;
        return retour;
    }
}
