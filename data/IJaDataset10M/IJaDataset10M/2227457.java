package self.micromagic.util;

import self.micromagic.eterna.digester.ConfigurationException;
import self.micromagic.eterna.security.Permission;
import self.micromagic.eterna.share.AbstractGenerator;
import self.micromagic.eterna.share.EternaFactory;
import self.micromagic.eterna.sql.ResultFormat;
import self.micromagic.eterna.sql.ResultFormatGenerator;
import self.micromagic.eterna.sql.ResultRow;

public class LimitFormat extends AbstractGenerator implements ResultFormat, ResultFormatGenerator {

    private int limit = 5;

    public void initialize(EternaFactory factory) throws ConfigurationException {
    }

    public String format(Object obj, Permission permission) throws ConfigurationException {
        String temp = obj == null ? "" : obj.toString();
        return Utils.formatLength(temp, this.limit);
    }

    public String format(Object obj, ResultRow row, Permission permission) throws ConfigurationException {
        return this.format(obj, permission);
    }

    public Object create() throws ConfigurationException {
        return this.createFormat();
    }

    public void setType(String type) {
    }

    public void setPattern(String pattern) {
        try {
            this.limit = Integer.parseInt(pattern);
        } catch (NumberFormatException ex) {
        }
    }

    public ResultFormat createFormat() {
        return this;
    }
}
