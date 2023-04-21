package mjc.tds;

import mjc.gc.*;

/**
 * Classe de renseignement
 * 
 * Sert � acc�der � des rensignements sur le contexte de compilation
 * � l'int�rieur des actions s�mantiques. N'est jamais modifi� par les actions,
 * seulement lu.
 * 
 * @author jjansen
 *
 *	
 */
public class InfosGlobales {

    private static boolean aLaDeuxiemePasse;

    private String nomFichier;

    private AbstractMachine vm;

    public InfosGlobales(String _nom, boolean _deuxiemePasse) {
        this.nomFichier = _nom.substring(_nom.lastIndexOf('/') + 1, _nom.length());
        aLaDeuxiemePasse = _deuxiemePasse;
    }

    public void setMachine(AbstractMachine _vm) {
        this.vm = _vm;
    }

    public String getNomFichier() {
        return this.nomFichier;
    }

    public boolean secondePasse() {
        return aLaDeuxiemePasse;
    }

    public static boolean isSecondePasse() {
        return aLaDeuxiemePasse;
    }

    public int getIntegerSize() {
        return AbstractMachine.INT_SIZE;
    }

    public int getBooleanSize() {
        return AbstractMachine.BOOL_SIZE;
    }

    public int getPointerSize() {
        return AbstractMachine.CLASS_SIZE;
    }

    public int getVoidSize() {
        return AbstractMachine.VOID_SIZE;
    }

    public AbstractMachine getMachine() {
        return this.vm;
    }
}
