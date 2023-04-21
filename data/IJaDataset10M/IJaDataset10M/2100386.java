package metso.paradigma.core.business.regole.coperture;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import metso.paradigma.core.business.model.PianoTurno;
import metso.paradigma.core.business.model.TurnoOperatore;

public class RegolaCoperturaTurnoGiornoPref extends RegolaCoperturaTurnoGiorno {

    private static final long serialVersionUID = 1432641238231098090L;

    public RegolaCoperturaTurnoGiornoPref() {
        this.setIdRegola(3);
    }

    @Override
    public int calcolaPenalita(PianoTurno pianoTurno) {
        int penalita = 0;
        int count = 0;
        List<Date> giorni = pianoTurno.getGiorni();
        for (Date day : giorni) {
            GregorianCalendar gc = new GregorianCalendar();
            gc.setTime(day);
            if (gc.get(Calendar.DAY_OF_WEEK) == (this.getGiorno() + 8) % 7) {
                List<TurnoOperatore> turniOperatore = pianoTurno.getTurniOperatoreDate(day);
                if (turniOperatore.size() != 0) {
                    count = this.countTurni(turniOperatore, this.getTurno().getIdTurno());
                    if (count != this.getValore()) {
                        penalita += Math.abs(this.getValore() - count) * this.getPeso();
                    }
                }
            }
        }
        return penalita;
    }

    @Override
    public int calcolaPenalitaGiorno(PianoTurno pianoTurno, Date data) {
        int penalita = 0;
        int count = 0;
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(data);
        if (gc.get(Calendar.DAY_OF_WEEK) == (this.getGiorno() + 8) % 7) {
            List<TurnoOperatore> turniOperatore = pianoTurno.getTurniOperatoreDate(data);
            if (turniOperatore.size() != 0) {
                count = this.countTurni(turniOperatore, this.getTurno().getIdTurno());
                if (count != this.getValore()) {
                    penalita += Math.abs(this.getValore() - count) * this.getPeso();
                }
            }
        }
        return penalita;
    }
}
