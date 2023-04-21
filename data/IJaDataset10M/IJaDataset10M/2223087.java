package webserg.pazzlers.ch6;

public class Ruckus {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        Dog dogs[] = { new Dog(), new Dog() };
        for (int i = 0; i < dogs.length; i++) dogs[i].woof();
        Cat cats[] = { new Cat(), new Cat(), new Cat() };
        for (int i = 0; i < cats.length; i++) cats[i].meow();
        System.out.print(Dog.getCount() + " woofs and ");
        System.out.println(Cat.getCount() + " meows");
    }
}
