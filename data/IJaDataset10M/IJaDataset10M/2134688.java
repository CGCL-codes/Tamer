package net.sf.excompcel.spreadsheet.impl.odf;

import net.sf.excompcel.spreadsheet.ECTypeEnum;
import net.sf.excompcel.spreadsheet.ECTypeEnumMapper;
import org.apache.commons.lang.NotImplementedException;
import org.apache.log4j.Logger;
import org.odftoolkit.odfdom.dom.attribute.office.OfficeValueTypeAttribute.Value;

/**
 * @author Detlev Struebig
 * @version 0.8
 *
 */
public class OdfECTypeEnumMapper implements ECTypeEnumMapper {

    /** Logger. */
    private static Logger log = Logger.getLogger(OdfECTypeEnumMapper.class);

    public ECTypeEnum convertCellTypeToString(int cellType) {
        throw new NotImplementedException();
    }

    public ECTypeEnum convertCellTypeToString(String cellType) {
        ECTypeEnum ret = ECTypeEnum.CELL_TYPE_UNKNOWN;
        if (cellType != null) {
            if (cellType.equalsIgnoreCase(Value.STRING.toString())) {
                ret = ECTypeEnum.CELL_TYPE_STRING;
            } else if (cellType.equalsIgnoreCase(Value.BOOLEAN.toString())) {
                ret = ECTypeEnum.CELL_TYPE_BOOLEAN;
            } else if (cellType.equalsIgnoreCase(Value.CURRENCY.toString())) {
                ret = ECTypeEnum.CELL_TYPE_NUMERIC;
            } else if (cellType.equalsIgnoreCase(Value.DATE.toString())) {
                ret = ECTypeEnum.CELL_TYPE_STRING;
            } else if (cellType.equalsIgnoreCase(Value.FLOAT.toString())) {
                ret = ECTypeEnum.CELL_TYPE_NUMERIC;
            } else if (cellType.equalsIgnoreCase(Value.PERCENTAGE.toString())) {
                ret = ECTypeEnum.CELL_TYPE_NUMERIC;
            } else if (cellType.equalsIgnoreCase(Value.TIME.toString())) {
                ret = ECTypeEnum.CELL_TYPE_STRING;
            } else if (cellType.equalsIgnoreCase(Value.VOID.toString())) {
                ret = ECTypeEnum.CELL_TYPE_UNKNOWN;
            }
        } else {
            ret = ECTypeEnum.CELL_TYPE_UNKNOWN;
        }
        log.debug("Return=" + ret);
        return ret;
    }
}
