package exemplo7;

import java.util.List;
import java.util.Arrays;
import java.util.Collections;

public class Sort2 {

    private static final String naipes[] = { "Ouros", "Copas", "Paus", "Espadas" };

    public void printElements() {
        List<String> list = Arrays.asList(naipes);
        System.out.printf("Elementos do array:\n%s\n", list);
        Collections.sort(list, Collections.reverseOrder());
        System.out.printf("Elementos do array ordenados:\n%s\n", list);
    }

    public static void main(String args[]) {
        Sort2 sort2 = new Sort2();
        sort2.printElements();
    }
}
