package be.oniryx.lean.session;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.ejb.Stateless;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.jboss.seam.annotations.In;
import org.jboss.seam.annotations.Logger;
import org.jboss.seam.annotations.Name;
import be.oniryx.lean.entity.ItemState;
import be.oniryx.lean.entity.Version;
import org.jboss.seam.log.Log;

@Name("exportBean")
@Stateless
public class ExportBeanImpl implements ExportBean {

    private static final String NEW_LINE = "\r\n";

    private static final String DELIMITER = "\t";

    @Logger
    private Log logger;

    @In
    private Factories factories;

    @In(value = "#{facesContext.externalContext}")
    private ExternalContext extCtx;

    @In(value = "#{facesContext}")
    FacesContext facesContext;

    public String exportToCSV() {
        List<ItemState> result = factories.getAllNonRecurrentItemStates();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        StringBuilder csvBuilder = initialiseBuilder();
        for (ItemState itemState : result) {
            csvBuilder.append(itemState.getShortName()).append(DELIMITER).append(itemState.getName()).append(DELIMITER).append(itemState.getItem().getType().getName()).append(DELIMITER).append(itemState.getState().getName()).append(DELIMITER).append(itemState.getPriority()).append(DELIMITER).append(itemState.getValue()).append(DELIMITER).append(itemState.getEtc()).append(DELIMITER).append(nullSafety(itemState.getDueDate(), sdf)).append(DELIMITER).append(nullSafety(itemState.getVersion())).append(NEW_LINE);
        }
        HttpServletResponse response = (HttpServletResponse) extCtx.getResponse();
        response.setContentType("Application/csv");
        response.addHeader("Content-disposition", "attachment; filename=\"NonRecurrentItems.csv\"");
        try {
            ServletOutputStream os = response.getOutputStream();
            os.write(csvBuilder.toString().getBytes());
            os.flush();
            os.close();
            facesContext.responseComplete();
        } catch (Exception e) {
            logger.error("Failed to export the non recurrent items to CSV", e);
        }
        return null;
    }

    private StringBuilder initialiseBuilder() {
        StringBuilder init = new StringBuilder();
        init.append("Short").append(DELIMITER).append("Name").append(DELIMITER).append("Type").append(DELIMITER).append("State").append(DELIMITER).append("Priority").append(DELIMITER).append("Value").append(DELIMITER).append("ETC").append(DELIMITER).append("Due date").append(DELIMITER).append("Version").append(NEW_LINE);
        return init;
    }

    private String nullSafety(Date d, SimpleDateFormat sdf) {
        if (d == null) return "";
        return sdf.format(d);
    }

    private String nullSafety(Version v) {
        if (v == null) return "";
        return v.getProduct().getName() + " v" + v.getName();
    }
}
