package com.meleva.bll;

import java.util.List;
import com.meleva.dao.TelefoneTipoDao;
import com.meleva.model.TelefoneTipo;

;

public class TelefoneTipoBll {

    public static List<TelefoneTipo> getTelefoneTipos() {
        TelefoneTipoDao telefoneTipoDao = new TelefoneTipoDao();
        return telefoneTipoDao.getTelefoneTipos();
    }

    public static TelefoneTipo getTelefoneTipoByTelefoneTipoID(int telefoneTipoId) {
        return TelefoneTipoDao.getTelefoneTipoByTelefoneTipoID(telefoneTipoId);
    }
}
