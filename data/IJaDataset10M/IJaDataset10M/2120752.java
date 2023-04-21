package br.net.woodstock.rockframework.web.struts2.converter;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.logging.Level;
import br.net.woodstock.rockframework.util.Assert;
import br.net.woodstock.rockframework.web.config.WebLog;
import br.net.woodstock.rockframework.web.types.NumericType;

@SuppressWarnings("rawtypes")
public abstract class NumericConverter<T extends NumericType> extends TypeConverter<T> {

    private NumberFormat format;

    public NumericConverter(final String format) {
        super();
        Assert.notNull(format, "format");
        this.format = new DecimalFormat(format);
    }

    public NumericConverter(final NumberFormat format) {
        super();
        Assert.notNull(format, "format");
        this.format = format;
    }

    @Override
    protected T convertFromString(final String s, final Class toClass) {
        try {
            Number n = this.format.parse(s);
            return this.wrap(n);
        } catch (Exception e) {
            WebLog.getInstance().getLog().log(Level.WARNING, e.getMessage(), e);
            return null;
        }
    }

    @Override
    protected String convertToString(final T o) {
        if (o == null) {
            return null;
        }
        String s = this.format.format(o.getValue());
        return s;
    }

    protected abstract T wrap(Number n);
}
