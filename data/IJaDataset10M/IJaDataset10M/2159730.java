package com.cell.rpg.ability;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import com.cell.exception.NotImplementedException;
import com.cell.rpg.RPGConfig;

/**
 * 适用于，每类Ability对应一个实体
 * @author WAZA
 *
 */
public abstract class AbilitiesTypeMap implements Abilities, Serializable {

    /**
	 * 
	 */
    private static final long serialVersionUID = 1L;

    LinkedHashMap<Class<?>, AbstractAbility> type_map = new LinkedHashMap<Class<?>, AbstractAbility>();

    transient AbstractAbility[] static_abilities;

    @Override
    public void addAbility(AbstractAbility element) {
        type_map.put(element.getClass(), element);
    }

    @Override
    public void removeAbility(AbstractAbility element) {
        type_map.remove(element.getClass());
    }

    @Override
    public int moveAbility(int index, int offset) throws NotImplementedException {
        throw new NotImplementedException();
    }

    @Override
    public void clearAbilities() {
        type_map.clear();
    }

    @Override
    public AbstractAbility[] getAbilities() {
        if (!RPGConfig.IS_EDIT_MODE) {
            if (static_abilities == null) {
                static_abilities = type_map.values().toArray(new AbstractAbility[type_map.size()]);
            }
            return static_abilities;
        }
        AbstractAbility[] abilities_data = type_map.values().toArray(new AbstractAbility[type_map.size()]);
        Arrays.sort(abilities_data);
        return abilities_data;
    }

    @Override
    public <T> ArrayList<T> getAbilities(Class<T> type) {
        ArrayList<T> ret = new ArrayList<T>(1);
        T ab = type.cast(type_map.get(type));
        if (ab != null) {
            ret.add(ab);
        }
        return ret;
    }

    @Override
    public int getAbilitiesCount() {
        return type_map.size();
    }

    @Override
    public <T> T getAbility(Class<T> type) {
        T ab = type.cast(type_map.get(type));
        return ab;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        for (AbstractAbility a : type_map.values()) {
            buffer.append(a.toString() + ";");
        }
        return buffer.toString();
    }
}
