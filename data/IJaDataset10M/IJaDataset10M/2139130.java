package org.posterita.factory;

import java.util.Properties;
import org.compiere.util.Env;
import org.posterita.exceptions.OperationException;
import org.posterita.model.MWebProperties;

public class DBPropertiesManager implements WebProperties {

    public void put(Properties ctx, String key, String value) {
        int[] propertieIds = MWebProperties.getAllIDs(MWebProperties.Table_Name, " U_KEY='" + key + "' and AD_CLIENT_ID = " + Env.getAD_Client_ID(ctx), null);
        if (propertieIds.length == 0) {
            MWebProperties webProperties = new MWebProperties(ctx, 0, null);
            webProperties.setU_Key(key);
            webProperties.setU_Value(value);
            webProperties.save();
        } else {
            MWebProperties webProperties = new MWebProperties(ctx, propertieIds[0], null);
            webProperties.setU_Value(value);
            webProperties.save();
        }
    }

    public String get(Properties ctx, String key) throws OperationException {
        int[] values;
        values = MWebProperties.getAllIDs(MWebProperties.Table_Name, "u_key ='" + key + "' and AD_CLIENT_ID = " + Env.getAD_Client_ID(ctx), null);
        if (values.length == 0) {
            return null;
        }
        MWebProperties webProperties = new MWebProperties(ctx, values[0], null);
        return webProperties.getU_Value();
    }
}
