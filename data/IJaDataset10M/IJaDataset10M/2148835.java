package medisis.data;

import java.util.HashMap;

/**
 *
 * @author Stephen Lintott
 */
public class Medication {

    private String name = "", dose = "", medsno = "";

    private HashMap<Illness, Integer> illnessList = new HashMap<Illness, Integer>();

    public Medication(String _medsno, String _name, String _dose) {
        this.medsno = _medsno;
        this.name = _name;
        this.dose = _dose;
    }

    public Medication(String _medsno, String _name, HashMap<Illness, Integer> illnessList) {
        this.medsno = _medsno;
        this.name = _name;
        this.illnessList = illnessList;
    }

    public Medication(String _medsno, String _name) {
        this(_medsno, _name, "");
    }

    public HashMap<Illness, Integer> getIllnessList() {
        return this.illnessList;
    }

    public void setIllnessList(HashMap<Illness, Integer> ilnessList) {
        this.illnessList = illnessList;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String _name) {
        this.name = _name;
    }

    public void setDose(String _dose) {
        this.dose = _dose;
    }

    public String getDose() {
        return this.dose;
    }

    public void setMedsNo(String _medsno) {
        this.medsno = _medsno;
    }

    public String getMedsNo() {
        return this.medsno;
    }
}
