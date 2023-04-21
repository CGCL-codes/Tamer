package String.str;

public class manejo {

    public static void main(String[] args) {
        String name = "(a+b)*c";
        for (int i = 0; i < name.length(); i++) {
            if (name.charAt(i) == ')') {
                System.out.println("ESTA EN " + i);
            }
        }
    }
}
