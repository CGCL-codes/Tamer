package org.compiere.process;

import java.io.*;
import java.math.*;

/**
 * 	Process Parameter
 *
 *  @author Jorg Janke
 *  @version $Id: ProcessInfoParameter.java,v 1.2 2006/07/30 00:54:44 jjanke Exp $
 */
public class ProcessInfoParameter implements Serializable {

    /**
	 *  Construct Parameter
	 *  @param parameterName parameter name
	 *  @param parameter parameter
	 *  @param parameter_To to parameter
	 *  @param info info
	 *  @param info_To to info
	 */
    public ProcessInfoParameter(String parameterName, Object parameter, Object parameter_To, String info, String info_To) {
        setParameterName(parameterName);
        setParameter(parameter);
        setParameter_To(parameter_To);
        setInfo(info);
        setInfo_To(info_To);
    }

    private String m_ParameterName;

    private Object m_Parameter;

    private Object m_Parameter_To;

    private String m_Info = "";

    private String m_Info_To = "";

    /**
	 * 	String Representation
	 *	@return info
	 */
    public String toString() {
        if (m_Parameter_To != null || m_Info_To.length() > 0) return "ProcessInfoParameter[" + m_ParameterName + "=" + m_Parameter + (m_Parameter == null ? "" : "{" + m_Parameter.getClass().getName() + "}") + " (" + m_Info + ") - " + m_Parameter_To + (m_Parameter_To == null ? "" : "{" + m_Parameter_To.getClass().getName() + "}") + " (" + m_Info_To + ")";
        return "ProcessInfoParameter[" + m_ParameterName + "=" + m_Parameter + (m_Parameter == null ? "" : "{" + m_Parameter.getClass().getName() + "}") + " (" + m_Info + ")";
    }

    /**
	 * Method getInfo
	 * @return String
	 */
    public String getInfo() {
        return m_Info;
    }

    /**
	 * Method getInfo_To
	 * @return String
	 */
    public String getInfo_To() {
        return m_Info_To;
    }

    /**
	 * Method getParameter
	 * @return Object
	 */
    public Object getParameter() {
        return m_Parameter;
    }

    /**
	 * Method getParameter as Int
	 * @return Object
	 */
    public int getParameterAsInt() {
        if (m_Parameter == null) return 0;
        if (m_Parameter instanceof Number) return ((Number) m_Parameter).intValue();
        BigDecimal bd = new BigDecimal(m_Parameter.toString());
        return bd.intValue();
    }

    /**
	 * Method getParameter_To
	 * @return Object
	 */
    public Object getParameter_To() {
        return m_Parameter_To;
    }

    /**
	 * Method getParameter as Int
	 * @return Object
	 */
    public int getParameter_ToAsInt() {
        if (m_Parameter_To == null) return 0;
        if (m_Parameter_To instanceof Number) return ((Number) m_Parameter_To).intValue();
        BigDecimal bd = new BigDecimal(m_Parameter_To.toString());
        return bd.intValue();
    }

    /**
	 * Method getParameterName
	 * @return String
	 */
    public String getParameterName() {
        return m_ParameterName;
    }

    /**
	 * Method setInfo
	 * @param Info String
	 */
    public void setInfo(String Info) {
        if (Info == null) m_Info = ""; else m_Info = Info;
    }

    /**
	 * Method setInfo_To
	 * @param Info_To String
	 */
    public void setInfo_To(String Info_To) {
        if (Info_To == null) m_Info_To = ""; else m_Info_To = Info_To;
    }

    /**
	 * Method setParameter
	 * @param Parameter Object
	 */
    public void setParameter(Object Parameter) {
        m_Parameter = Parameter;
    }

    /**
	 * Method setParameter_To
	 * @param Parameter_To Object
	 */
    public void setParameter_To(Object Parameter_To) {
        m_Parameter_To = Parameter_To;
    }

    /**
	 * Method setParameterName
	 * @param ParameterName String
	 */
    public void setParameterName(String ParameterName) {
        m_ParameterName = ParameterName;
    }
}
