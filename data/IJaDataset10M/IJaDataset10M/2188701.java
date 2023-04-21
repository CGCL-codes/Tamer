package org.koossery.adempiere.core.contract.dto.request;

import org.koossery.adempiere.core.contract.dto.KTADempiereBaseDTO;
import org.koossery.adempiere.core.contract.itf.request.IR_GroupDTO;

public class R_GroupDTO extends KTADempiereBaseDTO implements IR_GroupDTO {

    private static final long serialVersionUID = 1L;

    private String description;

    private String help;

    private int m_BOM_ID;

    private int m_ChangeNotice_ID;

    private String name;

    private int r_Group_ID;

    private String isActive;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHelp() {
        return help;
    }

    public void setHelp(String help) {
        this.help = help;
    }

    public int getM_BOM_ID() {
        return m_BOM_ID;
    }

    public void setM_BOM_ID(int m_BOM_ID) {
        this.m_BOM_ID = m_BOM_ID;
    }

    public int getM_ChangeNotice_ID() {
        return m_ChangeNotice_ID;
    }

    public void setM_ChangeNotice_ID(int m_ChangeNotice_ID) {
        this.m_ChangeNotice_ID = m_ChangeNotice_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getR_Group_ID() {
        return r_Group_ID;
    }

    public void setR_Group_ID(int r_Group_ID) {
        this.r_Group_ID = r_Group_ID;
    }

    public String getIsActive() {
        return isActive;
    }

    public void setIsActive(String _isActive) {
        this.isActive = _isActive;
    }
}
