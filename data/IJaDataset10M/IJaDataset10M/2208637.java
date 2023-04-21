package org.libreplan.web.common.typeconverters;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.zkoss.zk.ui.Component;
import org.zkoss.zkplus.databind.TypeConverter;

/**
 * Converter for the type java.util.Date
 *
 * @author Diego Pino Garcia <dpino@igalia.com>
 *
 */
public class DateConverter implements TypeConverter {

    @Override
    public Object coerceToBean(Object arg0, Component arg1) {
        return null;
    }

    @Override
    public Object coerceToUi(Object object, Component component) {
        return object != null ? (new SimpleDateFormat("dd/MM/yyyy")).format((Date) object) : new String("");
    }
}
