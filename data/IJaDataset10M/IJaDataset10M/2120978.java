package com.loribel.tools.web.repository.generated;

import com.loribel.commons.business.GB_BORepositoryTools;
import com.loribel.commons.business.abstraction.GB_BOAccessor;
import com.loribel.commons.business.abstraction.GB_SimpleBusinessObject;
import com.loribel.commons.exception.GB_LoadException;
import com.loribel.commons.exception.GB_SaveException;
import com.loribel.commons.util.CTools;
import com.loribel.commons.util.STools;
import com.loribel.tools.web.abstraction.GBW_BOAccessor;
import com.loribel.tools.web.abstraction.GBW_BOEntityShort;

/**
 * Implementation of GB_BOAccessor with GBW_BOAccessor.
 * 
 * @author Gregory Borelli
 */
public class GBW_BOAccessorToGeneric implements GB_BOAccessor {

    private GBW_BOAccessor boAccessor;

    public GBW_BOAccessorToGeneric(GBW_BOAccessor a_boAccessor) {
        boAccessor = a_boAccessor;
    }

    public GBW_BOAccessor getBoAccessor() {
        return boAccessor;
    }

    public void clearCache() {
        boAccessor.clearCache();
    }

    public String[] getIds(String a_pattern, int a_option) throws GB_LoadException {
        if (!a_pattern.endsWith("/*")) {
            return new String[0];
        }
        String l_boName = STools.removeEnd(a_pattern, "/*");
        return boAccessor.getAllIds(l_boName, a_option);
    }

    public void save(GB_SimpleBusinessObject a_item, String a_id, int a_option, boolean a_overwrite) throws GB_SaveException {
        GBW_BOEntityShort l_item = (GBW_BOEntityShort) a_item;
        GBW_BORepositoryGenericTools.saveBO(boAccessor, l_item, a_option, a_overwrite);
    }

    public void delete(GB_SimpleBusinessObject a_item, String a_id) throws GB_SaveException {
        GBW_BOEntityShort l_item = (GBW_BOEntityShort) a_item;
        GBW_BORepositoryGenericTools.deleteBO(boAccessor, l_item);
    }

    public GB_SimpleBusinessObject load(String a_id, int a_option) throws GB_LoadException {
        String[] l_ids = GB_BORepositoryTools.fromId(a_id);
        String l_boName = l_ids[0];
        String l_id = l_ids[1];
        return GBW_BORepositoryGenericTools.getBO(boAccessor, l_boName, l_id, a_option);
    }

    public boolean isExist(String a_id, int a_option) throws GB_LoadException {
        String[] l_ids = GB_BORepositoryTools.fromId(a_id);
        String l_boName = l_ids[0];
        String l_id = l_ids[1];
        return boAccessor.isExist(l_boName, l_id, a_option);
    }

    public long getTs(String a_id, int a_option) throws GB_LoadException {
        return -1;
    }

    public long[] getTs(String[] a_ids, int a_option) throws GB_LoadException {
        int len = CTools.getSize(a_ids);
        long[] retour = new long[len];
        for (int i = 0; i < len; i++) {
            String l_id = a_ids[i];
            retour[i] = getTs(l_id, a_option);
        }
        return retour;
    }
}
