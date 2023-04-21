package org.kabeja.parser.dxf.filter;

import org.kabeja.parser.DXFValue;
import org.kabeja.parser.ParseException;

abstract class DXFStreamSectionFilter extends AbstractDXFStreamFilter {

    private static final String SECTION_START = "SECTION";

    private static final String SECTION_END = "ENDSEC";

    private static final int COMMAND_CODE = 0;

    protected boolean sectionStarts = false;

    protected String section;

    public void parseGroup(int groupCode, DXFValue value) throws ParseException {
        if ((groupCode == COMMAND_CODE) && SECTION_START.equals(value.getValue())) {
            sectionStarts = true;
        } else if (sectionStarts) {
            sectionStarts = false;
            section = value.getValue();
            sectionStart(section);
            parseSection(COMMAND_CODE, new DXFValue(SECTION_START));
            parseSection(groupCode, value);
        } else {
            parseSection(groupCode, value);
        }
        if ((groupCode == COMMAND_CODE) && SECTION_END.equals(value.getValue())) {
            sectionEnd(section);
        }
    }

    protected abstract void parseSection(int groupCode, DXFValue value) throws ParseException;

    protected abstract void sectionStart(String Section) throws ParseException;

    protected abstract void sectionEnd(String Section) throws ParseException;
}
