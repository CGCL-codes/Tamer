package vjde.completion;

import java.util.Arrays;

public class PackageCompletion {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("<classpath> [package-name]");
            return;
        }
        String p = args.length >= 1 ? args[0] : "";
        String[] names = new DynamicClassLoader(p).getPackageNames();
        Arrays.sort(names);
        int length = 0;
        String prefix = "";
        if (args.length >= 2) {
            prefix = args[1];
            length = prefix.length();
            for (int i = 0; i < names.length; i++) {
                if (names[i].length() >= length && names[i].startsWith(prefix)) {
                    System.out.println(names[i]);
                }
            }
        } else {
            for (int i = 0; i < names.length; i++) {
                System.out.println(names[i]);
            }
        }
    }
}
