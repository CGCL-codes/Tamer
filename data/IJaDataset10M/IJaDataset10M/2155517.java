package com.safi.workshop.sheet.actionstep;

import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.eclipse.core.databinding.conversion.Converter;
import org.eclipse.core.databinding.conversion.IConverter;
import org.eclipse.emf.databinding.EMFUpdateValueStrategy;
import com.safi.core.actionstep.DBConnectionId;

public class ConnectionUpdateStrategy extends EMFUpdateValueStrategy {

    private List<DBConnectionId> connections;

    public ConnectionUpdateStrategy(List<DBConnectionId> calls2) {
        this.connections = calls2;
    }

    public ConnectionUpdateStrategy(int updatePolicy) {
        super(updatePolicy);
    }

    public ConnectionUpdateStrategy(boolean provideDefaults, int updatePolicy) {
        super(provideDefaults, updatePolicy);
    }

    @Override
    public Object convert(Object value) {
        return super.convert(value);
    }

    @Override
    protected IConverter createConverter(Object fromType, Object toType) {
        if (fromType == String.class) {
            return new Converter(fromType, toType) {

                @Override
                public Object convert(Object fromObject) {
                    if (fromObject instanceof DBConnectionId) return ((DBConnectionId) fromObject).getId();
                    for (DBConnectionId call : connections) {
                        if (StringUtils.equals(call.getId(), (String) fromObject)) {
                            return call;
                        }
                    }
                    return null;
                }
            };
        }
        return super.createConverter(fromType, toType);
    }

    public List<DBConnectionId> getCalls() {
        return connections;
    }

    public void setCalls(List<DBConnectionId> calls) {
        this.connections = calls;
    }
}
