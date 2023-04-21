package it.jnrpe.server.xml;

import it.jnrpe.plugins.PluginOption;
import org.apache.commons.cli.Option;

public class XMLOption {

    private String m_sOption = null;

    private boolean m_bHasArgs = false;

    private Integer m_iArgsCount = null;

    private boolean m_bRequired = false;

    private Boolean m_bArgsOptional = null;

    private String m_sArgName = null;

    private String m_sLongOpt = null;

    private String m_sType = null;

    private String m_sValueSeparator = null;

    private String m_sDescription = null;

    public XMLOption() {
    }

    public String getOption() {
        return m_sOption;
    }

    public void setOption(String sOption) {
        m_sOption = sOption;
    }

    public boolean hasArgs() {
        return m_bHasArgs;
    }

    public void setHasArgs(String sHasArgs) {
        m_bHasArgs = sHasArgs.equals("true");
    }

    public Integer getArgsCount() {
        return m_iArgsCount;
    }

    public void setArgsCount(String sArgsCount) {
        m_iArgsCount = new Integer(sArgsCount);
    }

    public String getRequired() {
        return "" + m_bRequired;
    }

    public void setRequired(String sRequired) {
        m_bRequired = sRequired.equals("true");
    }

    public Boolean getArgsOptional() {
        return m_bArgsOptional;
    }

    public void setArgsOptional(String sArgsOptional) {
        m_bArgsOptional = new Boolean(sArgsOptional.equals("true"));
    }

    public String getArgName() {
        return m_sArgName;
    }

    public void setArgName(String sArgName) {
        m_sArgName = sArgName;
    }

    public String getLongOpt() {
        return m_sLongOpt;
    }

    public void setLongOpt(String sLongOpt) {
        m_sLongOpt = sLongOpt;
    }

    public String getType() {
        return m_sType;
    }

    public void setType(String sType) {
        m_sType = sType;
    }

    public String getValueSeparator() {
        return m_sValueSeparator;
    }

    public void setValueSeparator(String sValueSeparator) {
        m_sValueSeparator = sValueSeparator;
    }

    public String getDescription() {
        return m_sDescription;
    }

    public void setDescription(String sDescription) {
        m_sDescription = sDescription;
    }

    Option toOption() {
        Option ret = new Option(m_sOption, m_sDescription);
        if (m_bArgsOptional != null) ret.setOptionalArg(m_bArgsOptional.booleanValue());
        if (m_bHasArgs) {
            if (m_iArgsCount == null) ret.setArgs(Option.UNLIMITED_VALUES);
        }
        ret.setRequired(m_bRequired);
        if (m_iArgsCount != null) ret.setArgs(m_iArgsCount.intValue());
        if (m_sArgName != null) {
            if (m_iArgsCount == null) ret.setArgs(Option.UNLIMITED_VALUES);
            ret.setArgName(m_sArgName);
        }
        if (m_sLongOpt != null) ret.setLongOpt(m_sLongOpt);
        if (m_sValueSeparator != null && m_sValueSeparator.length() != 0) ret.setValueSeparator(m_sValueSeparator.charAt(0));
        return ret;
    }
}
