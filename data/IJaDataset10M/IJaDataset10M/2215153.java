package co.edu.unal.fdtd.util;

import org.jdom.Element;
import co.edu.unal.ungrid.xml.XmlUtil;

public class Material {

    public Material(Element e) {
        m_nId = XmlUtil.getIntAtt(e, "id");
        m_sName = XmlUtil.getParameter(e, "name");
        m_fConductivity = XmlUtil.getDouble(e, "sigma");
        m_fRelativePermitivity = XmlUtil.getDouble(e, "epsilonr");
        m_fSusceptibility = XmlUtil.getDouble(e, "mur");
    }

    public Material(int id, String name, double conductivity, double relPerm, double susceptibility) {
        m_nId = id;
        m_sName = name;
        m_fConductivity = conductivity;
        m_fRelativePermitivity = relPerm;
        m_fSusceptibility = susceptibility;
    }

    public int getId() {
        return m_nId;
    }

    public boolean isValid() {
        return (m_fConductivity >= 0.0 && m_fRelativePermitivity >= 0.0 && m_fSusceptibility >= 0.0);
    }

    public String toString() {
        StringBuffer sb = new StringBuffer();
        sb.append("ID: " + m_nId + "\n");
        sb.append("Name: " + m_sName + "\n");
        sb.append("Conductivity: " + m_fConductivity + "\n");
        sb.append("Relative Permitivity: " + m_fRelativePermitivity + "\n");
        sb.append("Susceptibility: " + m_fSusceptibility + "\n");
        return sb.toString();
    }

    private int m_nId;

    public String m_sName;

    public double m_fConductivity;

    public double m_fRelativePermitivity;

    public double m_fSusceptibility;

    public static final String AIR = "Air";
}
