package Characters;

import Items.Inventory;

public class Character {

    private String name;

    private char sex;

    private int age;

    private Inventory pack;

    /**
      * @param name
      * @param sex
      * @param age
      */
    public Character(String name, char sex, int age) {
        this.name = name;
        this.sex = sex;
        this.age = age;
    }
}
