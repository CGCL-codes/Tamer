package org.dhcc.gui.print;

import java.util.HashMap;
import org.dhcc.UserChar;
import org.dhcc.skills.Gender;
import org.dhcc.utils.print.CharacterSheet1;
import org.junit.Test;

public class CharacterSheet1Test {

    @Test
    public void sheet1test() {
        UserChar ch = new UserChar();
        ch.setCharacterFirstName("Benjamin");
        ch.setCharacterLastName("Zindel");
        ch.setCharacterNickName("Str0hk0pf");
        ch.setHomeWorld("Homeworld");
        ch.setCareer("Career");
        ch.setDivination("Divination");
        ch.setQuirk("Quirk");
        ch.setGender(Gender.MALE);
        ch.setHeight(190);
        ch.setWeight(84);
        ch.setSkinColor("white");
        ch.setHairColor("black");
        ch.setEyeColor("green");
        ch.setAge(28);
        ch.setExpToSpend(400);
        ch.setExpTotal(400);
        ch.setCharacteristics(createCharacteristics());
        CharacterSheet1 cs1 = new CharacterSheet1(ch);
    }

    private HashMap<String, Integer> createCharacteristics() {
        HashMap<String, Integer> characteristics = new HashMap<String, Integer>();
        characteristics.put("WS", 37);
        characteristics.put("BS", 37);
        characteristics.put("Str", 39);
        characteristics.put("T", 35);
        characteristics.put("Ag", 40);
        characteristics.put("Int", 33);
        characteristics.put("Per", 36);
        characteristics.put("WP", 38);
        characteristics.put("Fel", 42);
        return characteristics;
    }
}
