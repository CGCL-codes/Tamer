package com.ag.promanagement.control;

import com.ag.promanagement.Process;
import java.math.BigDecimal;
import java.util.*;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Zathura Code Generator http://code.google.com/p/zathura
 *
 */
public interface IProcessLogic {

    public List<Process> getProcess() throws Exception;

    public void saveProcess(String accionado, String accionante, Long docAdjunto, Date fechaIngreso, Date fechaRadicacion, Long id, Long id_AccionType, Long id_EstadoType, Long id_Users) throws Exception;

    public void deleteProcess(Long id) throws Exception;

    public void updateProcess(String accionado, String accionante, Long docAdjunto, Date fechaIngreso, Date fechaRadicacion, Long id, Long id_AccionType, Long id_EstadoType, Long id_Users) throws Exception;

    public Process getProcess(Long id) throws Exception;

    public List<Process> findByCriteria(Object[] variables, Object[] variablesBetween, Object[] variablesBetweenDates) throws Exception;

    public List<Process> findPageProcess(String sortColumnName, boolean sortAscending, int startRow, int maxResults) throws Exception;

    public Long findTotalNumberProcess() throws Exception;
}
