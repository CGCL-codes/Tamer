package oe11_bean_identifier;

enum Animal2 {
}

/**
 *
 * @author SCJP
 */
public class D01_enum {

    static int flag1 = 0;

    static int flag2 = 1;

    static int flag3 = 2;

    static enum Animal {

        DOG("gaugau"), CAT("meomeo"), FISH("caca");

        String sound;

        private Animal(String sound) {
            this.sound = sound;
        }
    }

    static Animal a;

    public static void main(String[] args) {
        System.out.println(Animal.DOG.sound);
        System.out.println(a.DOG.sound);
        System.out.println("Wave the flag");
        int f = 1;
        if (f == flag1) {
            System.out.println("has flag1");
        }
        System.out.println("Choose a animal");
        Animal a1 = null;
        switch(a1) {
            case DOG:
                break;
            default:
                throw new AssertionError();
        }
    }
}
