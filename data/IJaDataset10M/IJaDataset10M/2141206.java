package org.openXpertya.sla;

import java.sql.Timestamp;
import java.util.logging.Level;
import org.openXpertya.model.MSLACriteria;
import org.openXpertya.model.MSLAGoal;
import org.openXpertya.model.MSLAMeasure;
import org.openXpertya.process.ProcessInfoParameter;
import org.openXpertya.process.SvrProcess;
import org.openXpertya.util.ErrorUsuarioOXP;

/**
 * Descripción de Clase
 *
 *
 * @version    2.2, 12.10.07
 * @author     Equipo de Desarrollo de openXpertya    
 */
public class SLAMeasureProcess extends SvrProcess {

    /** Descripción de Campos */
    private int p_PA_SLA_Measure_ID;

    /**
     * Descripción de Método
     *
     */
    protected void prepare() {
        ProcessInfoParameter[] para = getParameter();
        for (int i = 0; i < para.length; i++) {
            String name = para[i].getParameterName();
            if (para[i].getParameter() == null) {
                ;
            } else {
                log.log(Level.SEVERE, "prepare - Unknown Parameter: " + name);
            }
        }
        p_PA_SLA_Measure_ID = getRecord_ID();
    }

    /**
     * Descripción de Método
     *
     *
     * @return
     *
     * @throws Exception
     */
    protected String doIt() throws Exception {
        log.info("PA_SLA_Measure_ID=" + p_PA_SLA_Measure_ID);
        MSLAMeasure measure = new MSLAMeasure(getCtx(), p_PA_SLA_Measure_ID, get_TrxName());
        if (measure.getID() == 0) {
            throw new ErrorUsuarioOXP("@PA_SLA_Measure_ID@ " + p_PA_SLA_Measure_ID);
        }
        MSLAGoal goal = new MSLAGoal(getCtx(), measure.getPA_SLA_Goal_ID(), get_TrxName());
        if (goal.getID() == 0) {
            throw new ErrorUsuarioOXP("@PA_SLA_Goal_ID@ " + measure.getPA_SLA_Goal_ID());
        }
        MSLACriteria criteria = MSLACriteria.get(getCtx(), goal.getPA_SLA_Criteria_ID(), get_TrxName());
        if (criteria.getID() == 0) {
            throw new ErrorUsuarioOXP("@PA_SLA_Criteria_ID@ " + goal.getPA_SLA_Criteria_ID());
        }
        SLACriteria pgm = criteria.newInstance();
        goal.setMeasureActual(pgm.calculateMeasure(goal));
        goal.setDateLastRun(new Timestamp(System.currentTimeMillis()));
        goal.save();
        return "@MeasureActual@=" + goal.getMeasureActual();
    }
}
