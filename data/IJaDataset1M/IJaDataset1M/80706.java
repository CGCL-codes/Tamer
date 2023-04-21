package com.icteam.fiji.model;

import com.icteam.fiji.ServerServiceLocator;
import com.icteam.fiji.manager.TipManager;
import com.icteam.fiji.model.util.TypeMap;
import java.io.Serializable;
import java.util.Collection;

/**
 * TipRuoloEntBsnsEntBsnsEnum
 * NON E' REALIZZATA CON UN ENUM PERCHE' E' NECESSARIO ESTENDERLA
 */
public class TipRuoloEntBsnsEntBsnsEnum implements Serializable {

    public static final TipRuoloEntBsnsEntBsnsEnum RUOLI = new TipRuoloEntBsnsEntBsnsEnum("Ruoli");

    public static final TipRuoloEntBsnsEntBsnsEnum RUOLI_TARGET = new TipRuoloEntBsnsEntBsnsEnum("Ruoli target");

    private final String tipRuolo;

    protected TipRuoloEntBsnsEntBsnsEnum(String tipRuolo) {
        this.tipRuolo = tipRuolo;
    }

    public static Collection<TipRuoloEntBsnsEntBsns> getValues() {
        return getTypeMap().getInstances();
    }

    public TipRuoloEntBsnsEntBsns getValue() {
        return getValue(tipRuolo);
    }

    public static TipRuoloEntBsnsEntBsns getValue(String p_tipRuolo) {
        return getTypeMap().getInstanceByName(p_tipRuolo);
    }

    public static TipRuoloEntBsnsEntBsns getValue(Long p_internalCode) {
        return getTypeMap().getInstanceByCode(p_internalCode);
    }

    public static void reload() {
        getTypeMap().reload();
    }

    private static TypeMap<TipRuoloEntBsnsEntBsns, Long> s_typeMap = null;

    private static TypeMap<TipRuoloEntBsnsEntBsns, Long> getTypeMap() {
        if (s_typeMap == null) {
            s_typeMap = new TypeMap<TipRuoloEntBsnsEntBsns, Long>() {

                public Class<TipRuoloEntBsnsEntBsns> getTypeClass() {
                    return TipRuoloEntBsnsEntBsns.class;
                }

                public Collection<TipRuoloEntBsnsEntBsns> loadTypeValues() throws Exception {
                    TipManager manager = ServerServiceLocator.getInstance().getService(TipManager.class);
                    return manager.getTipRuoloEntBsnsEntBsnses();
                }

                public Long getTypeCode(TipRuoloEntBsnsEntBsns t) throws Exception {
                    return t.getCTipRuoloEntBsnsEntBsns();
                }

                public String getTypeName(TipRuoloEntBsnsEntBsns t) throws Exception {
                    return t.getNTipRuoloEntBsnsEntBsns();
                }
            };
        }
        return s_typeMap;
    }
}
