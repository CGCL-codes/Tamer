package br.com.petrobras.util;

import java.text.ParseException;
import java.util.Date;
import org.apache.commons.beanutils.Converter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DateConverter implements Converter {

    private static Log logger;

    static {
        logger = LogFactory.getLog(DateConverter.class.getName());
    }

    @SuppressWarnings("unchecked")
    public Object convert(Class arg0, Object arg1) {
        try {
            Date data = (arg1 instanceof Date) ? (Date) arg1 : DateUtils.toDate(arg1.toString());
            return data;
        } catch (ParseException e) {
            logger.error("Erro na conversao: " + e.getMessage());
        }
        return null;
    }
}
