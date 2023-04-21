package hitungratarata5bil;

import java.io.*;

public class HitungRataRata5Bil {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        double x = 0;
        int a = 0;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String strbilangan = null;
        while (a < 5) {
            System.out.print("Masukkan bilangan ke-" + a + " : ");
            try {
                strbilangan = br.readLine();
            } catch (IOException ioe) {
                System.out.println("Kesalahan IO, program berhenti");
                System.exit(1);
            }
            x = x + Double.parseDouble(strbilangan);
            a = a + 1;
        }
        x = x / 5;
        System.out.println("Rata-rata bilangan yang dimasukkan adalah " + x);
    }
}
