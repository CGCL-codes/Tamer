package ch.bfh.egov.internetapps.service.customizing;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts.action.DynaActionForm;
import ch.bfh.egov.internetapps.service.auswahlfeld.AuswahlfeldService;
import ch.bfh.egov.internetapps.service.common.CommonService;
import ch.bfh.egov.internetapps.service.fragebogen.NaOpNuService;
import ch.bfh.egov.internetapps.service.kategorie.KategorieService;
import ch.bfh.egov.internetapps.service.nutzenkriterium.NutzenkriteriumService;
import ch.bfh.egov.internetapps.tos.Customizing;

/**
 * Service-Interface f�r Customizings.
 * 
 * @author Kompetenzzentrum E-Business, Simon Bergamin
 */
public interface CustomizingService {

    public Customizing getById(Customizing c);

    public Customizing getByName(Customizing c);

    public Customizing getByNaOpNuUID(Long naOpNuUID);

    public List getAll(Integer mandantId);

    public Integer insert(Customizing c);

    public void update(Customizing c);

    public void init(CommonService cService, AuswahlfeldService afService, KategorieService kService, NutzenkriteriumService nService, NaOpNuService naOpNuService);

    public void cascadeDelete(HttpServletRequest request, Customizing c);

    public void copy(HttpServletRequest request, DynaActionForm form);

    public void changeStatus(HttpServletRequest request, DynaActionForm form);

    public void changeBefragungStatus(HttpServletRequest request, DynaActionForm form);

    public boolean complete(HttpServletRequest request);

    public void setStatus(HttpServletRequest request);

    public void unsetStatus(HttpServletRequest request);

    public void populate(HttpServletRequest request);
}
