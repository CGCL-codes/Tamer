package br.com.formatura.controller;

import br.com.formatura.util.HibernateUtil;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

/**
 *
 * @author Adolfo
 */
public class ListenerFasesJSF implements PhaseListener {

    @Override
    public void beforePhase(PhaseEvent event) {
        System.out.println("Antes fase" + event.getPhaseId());
        if (event.getPhaseId().equals(PhaseId.RESTORE_VIEW)) {
            HibernateUtil.beginTransaction();
        }
    }

    @Override
    public void afterPhase(PhaseEvent event) {
        System.out.println("Depois fase" + event.getPhaseId());
        if (event.getPhaseId().equals(PhaseId.RENDER_RESPONSE)) {
            try {
                HibernateUtil.commit();
            } catch (Exception e) {
                System.out.println("Erro no commit da transação");
                System.out.println(e.getMessage());
                if (HibernateUtil.getCurrentSession().getTransaction().isActive()) {
                    HibernateUtil.rollback();
                }
            } finally {
                HibernateUtil.getCurrentSession().close();
            }
        }
    }

    @Override
    public PhaseId getPhaseId() {
        return PhaseId.ANY_PHASE;
    }
}
