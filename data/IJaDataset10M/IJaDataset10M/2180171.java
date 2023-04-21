package com.icteam.fiji.manager;

import java.util.List;
import com.icteam.fiji.Service;
import com.icteam.fiji.command.admin.audt.SearchAudtCriterium;
import com.icteam.fiji.manager.exception.CreateException;
import com.icteam.fiji.model.Audt;
import com.icteam.fiji.model.AudtLite;

public interface AudtManager extends Service {

    List<Audt> getAudts(SearchAudtCriterium searchAudtCriterium);

    Audt createAudt(Audt p_audt) throws CreateException;

    List<AudtLite> getAudtLites(SearchAudtCriterium searchAudtCriterium);
}
