package com.juliashine.domain.report;

import java.util.Date;
import com.juliashine.common.enu.CalcType;

/**
 * @author Juliashine@gmail.com 2011-5-27
 *
 */
public class BaseQueryConditon {

    CalcType resultType = CalcType.Raw;

    Date beginDate;

    Date endDate;
}
