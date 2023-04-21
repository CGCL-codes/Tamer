package org.posterita.keyname;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import org.compiere.model.MAttributeSet;
import org.compiere.util.Env;
import org.posterita.core.KeyNamePairUtil;
import org.posterita.exceptions.OperationException;

public class ProductAttributeSetKeyNamePair extends KeyNamePairUtil {

    public static ArrayList getKeyNamePairs(Properties ctx) throws OperationException {
        String sql = "ad_client_id=" + Env.getAD_Client_ID(ctx) + " and isactive = 'Y'";
        try {
            return getData(ctx, MAttributeSet.Table_Name, sql);
        } catch (SQLException e) {
            throw new OperationException(e.getMessage());
        }
    }
}
