package co.edu.usbcali.control;

import co.edu.usbcali.modeloo.Ambitos;
import java.math.BigDecimal;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

public interface IAmbitosLogic {

    public List<Ambitos> getAmbitos() throws Exception;

    public void saveAmbitos(Integer idambito, String nombreambito) throws Exception;

    public void deleteAmbitos(Integer idambito) throws Exception;

    public void updateAmbitos(Integer idambito, String nombreambito) throws Exception;

    public Ambitos getAmbitos(Integer idambito) throws Exception;

    public List<Ambitos> findByCriteria(Object[] variables, Object[] variablesBetween, Object[] variablesBetweenDates) throws Exception;

    public List<Ambitos> findPageAmbitos(String sortColumnName, boolean sortAscending, int startRow, int maxResults) throws Exception;

    public Long findTotalNumberAmbitos() throws Exception;
}
