package scamsoft.squadleader.rules.persistence;

import nu.xom.Element;
import scamsoft.squadleader.rules.Game;
import scamsoft.squadleader.rules.Map;
import scamsoft.squadleader.rules.Weapon;
import scamsoft.squadleader.rules.WeaponUnitType;

/**
 * User: Andreas Mross
 * Date: 22-Jun-2007
 * Time: 20:45:10
 */
public class WeaponBuilder extends AbstractUnitBuilder {

    private CounterBuilder counterBuilder;

    static final String WEAPON = "Weapon";

    public WeaponBuilder(PersistenceRegistry registry) {
        super(registry);
        counterBuilder = new CounterBuilder(registry);
    }

    public Element toXML(Weapon weapon) {
        Element root = new Element(WEAPON);
        addGenericHeader(root, weapon);
        root.appendChild(counterBuilder.toXML(weapon));
        return root;
    }

    public Weapon fromXML(Element root, Map map, Game game, UnitSource unitSource) {
        Element type = root.getFirstChildElement(AbstractUnitBuilder.TYPE);
        WeaponUnitType unitType = (WeaponUnitType) getRegistry().getRegisteredType(type.getAttributeValue(AbstractUnitBuilder.PACKAGE), type.getAttributeValue(AbstractUnitBuilder.KEY));
        Weapon result = new Weapon(getIdFactory(), unitType, getDummyPlayer(), getDummyDate());
        return fromXML(root, map, game, unitSource, result);
    }

    public Weapon fromXML(Element root, Map map, Game game, UnitSource unitSource, Weapon weapon) {
        counterBuilder.fromXML(root.getFirstChildElement(CounterBuilder.UNIT), map, game, unitSource, weapon);
        return weapon;
    }
}
