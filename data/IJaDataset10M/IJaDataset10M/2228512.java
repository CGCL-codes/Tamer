package org.kabeja.parser.entities;

import org.kabeja.dxf.DXFAttrib;
import org.kabeja.dxf.DXFConstants;
import org.kabeja.parser.DXFEntitiesSectionHandler;
import org.kabeja.parser.DXFValue;

/**
 * @author <a href="mailto:simon.mieth@gmx.de">Simon Mieth</a>
 *
 */
public class DXFAttribHandler extends DXFTextHandler {

    public static final int ATTRIB_VERTICAL_ALIGN = 74;

    public static final int ATTRIB_TEXT_LENGTH = 73;

    public static final int ATTRIB_ATTRIBUTE_TAG = 2;

    public DXFAttribHandler() {
        super();
    }

    public void parseGroup(int groupCode, DXFValue value) {
        switch(groupCode) {
            case ATTRIB_TEXT_LENGTH:
                break;
            case ATTRIB_VERTICAL_ALIGN:
                text.setValign(value.getIntegerValue());
                break;
            case ATTRIB_ATTRIBUTE_TAG:
                text.setAttribTag(value.getValue());
                break;
            default:
                super.parseGroup(groupCode, value);
        }
    }

    public void startDXFEntity() {
        text = new DXFAttrib();
    }

    public String getDXFEntityName() {
        return DXFConstants.ENTITY_TYPE_ATTRIB;
    }
}
