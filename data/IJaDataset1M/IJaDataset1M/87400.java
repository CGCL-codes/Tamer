package ppr.jiasharm.framework.attacks;

import ppr.jiasharm.framework.attacks.Attack;

/**
 * Testangriff, dient nur zum Testen der Klasse Attacker
 * Der angriff tut nix.
 *
 * @author  Christoph Peters
 */
public class TollerTestAngriff extends Attack {

    public TollerTestAngriff() {
        this.attackInfo = "Dieser Angriff ist ein Testangriff und kann nix";
        this.loadDefaultParameter();
        this.attackName = "Toller Testangriff";
        this.attackState = "created, but unused";
    }

    private void loadDefaultParameter() {
        this.attackDefaultParams = "<parameter>" + "<param>" + "<name>versuche</name>" + "<type>Ganzzahl</type>" + "<range>1 bis 50</range>" + "<value>1</value>" + "</param>" + "<param>" + "<name>option1</name>" + "<type>bool</type>" + "<range>true oder false</range>" + "<value>true</value>" + "</param>" + "</parameter>";
    }

    public void setAttackParams(String target, int port, String params) throws IllegalArgumentException {
        if (!(params.equals(this.attackDefaultParams))) {
            throw new IllegalArgumentException("Parameter entsprechen nicht " + "den Default-Parametern.");
        }
    }

    public String getAttackParams() {
        return this.attackDefaultParams;
    }

    public void run() {
        this.attackState = "running";
        for (Integer i = 0; (i < 100) && !this.isInterrupted(); i++) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                this.interrupt();
            }
            System.out.println(this.attackName + i.toString());
        }
        this.attackState = (this.isInterrupted() != true) ? "finished" : "interrupted";
    }
}
