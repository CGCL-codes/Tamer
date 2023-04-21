package de.jakop.rugby.zustand;

import de.jakop.rugby.types.EingabeNichtZulaessigException;
import de.jakop.rugby.types.IProtokollEintrag;
import de.jakop.rugby.types.ISpiel;
import de.jakop.rugby.types.ITeam;
import de.jakop.rugby.types.IUhrFactory;
import de.jakop.rugby.types.Schalter;
import de.jakop.rugby.zeit.Timer;

/**
 * Schiedsrichterball. Warten auf den (Wieder-)Anpfiff. Anhupen m�glich.
 * @author jakop
 *
 */
public class Schiedsrichterball extends AbstractZustand implements IProtokollEintrag {

    /**
	 * 
	 * @param hupe
	 * @param uhr 
	 * @param objFactory 
	 */
    public Schiedsrichterball(ISpiel hupe, Timer uhr, IUhrFactory objFactory) {
        super(hupe, uhr, objFactory);
        this.aktiveSchalter.add(Schalter.HUPE1);
        this.aktiveSchalter.add(Schalter.STRAFZEIT);
        this.aktiveSchalter.add(Schalter.AUSZEIT);
    }

    @Override
    public void auszeit(ITeam t) throws EingabeNichtZulaessigException {
        super.auszeit(t);
        getSpiel().setZustand(new Auszeit(getSpiel(), t, getUhr(), getUhrFactory()), true);
    }

    @Override
    public void hupen1x() throws EingabeNichtZulaessigException {
        super.hupen1x();
        getSpiel().strafzeitenWeiter();
        getSpiel().setZustand(new Laufend(getSpiel(), getUhr(), getUhrFactory()), false);
    }

    @Override
    public ITeam getTeam() {
        return null;
    }
}
