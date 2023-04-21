package interfaces.spawnMenu.costumize.weapons;

import logic.nodes.nodeSettings.Settings;
import logic.weapons.WeaponFireProperties;
import logic.weapons.WeaponProperties;
import fileHandling.language.LanguageLoader;
import interfaces.spawnMenu.costumize.InfoContent;
import interfaces.spawnMenu.costumize.PropertyWidget;

public class WeaponInfoContent extends InfoContent {

    protected PropertyWidget damageWidget, rateWidget, lifetimeWidget;

    protected WeaponProperties weaponProps;

    public WeaponInfoContent(int width, int height, int x, int y, WeaponProperties weaponProps, Settings maxValues) {
        super(width, height, x, y, maxValues);
        this.weaponProps = weaponProps;
        initPositions(4);
        createWidgets();
    }

    @Override
    protected void createWidgets() {
        WeaponFireProperties weaponFireProps = weaponProps.getWeaponFireProperties();
        String descr = LanguageLoader.get(WeaponFireProperties.DAMAGE);
        String[] keys = new String[] { WeaponFireProperties.DAMAGE };
        damageWidget = new PropertyWidget(descr, width, infoWidgetHeight, weaponFireProps, keys, maxValues);
        damageWidget.setXY(0, widgetY);
        addWidget(damageWidget);
        allWidgets.add(damageWidget);
        widgetY -= infoWidgetHeight;
        descr = LanguageLoader.get(WeaponProperties.SHOOTRATE);
        keys = new String[] { WeaponProperties.SHOOTRATE };
        rateWidget = new PropertyWidget(descr, width, infoWidgetHeight, weaponProps, keys, maxValues);
        rateWidget.setXY(0, widgetY);
        addWidget(rateWidget);
        allWidgets.add(rateWidget);
        widgetY -= infoWidgetHeight;
        descr = LanguageLoader.get(WeaponFireProperties.LIFETIME);
        keys = new String[] { WeaponFireProperties.LIFETIME };
        lifetimeWidget = new PropertyWidget(descr, width, infoWidgetHeight, weaponFireProps, keys, maxValues);
        lifetimeWidget.setXY(0, widgetY);
        addWidget(lifetimeWidget);
        allWidgets.add(lifetimeWidget);
        widgetY -= infoWidgetHeight;
    }

    public void setProperties(WeaponProperties props) {
        weaponProps = props;
        damageWidget.setProperties(props.getWeaponFireProperties());
        rateWidget.setProperties(props);
        lifetimeWidget.setProperties(props.getWeaponFireProperties());
    }

    public WeaponInfoContent getCopy() {
        WeaponFireProperties fireProps = new WeaponFireProperties(weaponProps.getWeaponFireProperties());
        WeaponProperties props = new WeaponProperties(weaponProps, fireProps);
        return new WeaponInfoContent(width, height, getX(), getY(), props, maxValues);
    }

    @Override
    public void updateMoney(int newMoney) {
    }
}
