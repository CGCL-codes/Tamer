package ratarata;

import java.io.*;

public class RataRata {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        double jumlah = 0;
        double bilangan = 0;
        int n = 0;
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String strbilangan = null;
        System.out.print("Masukkan bilangan pertama : ");
        try {
            strbilangan = br.readLine();
        } catch (IOException ioe) {
            System.out.println("Kesalahan IO, program berhenti");
            System.exit(1);
        }
        bilangan = Double.parseDouble(strbilangan);
        while (bilangan != 0) {
            jumlah += bilangan;
            n++;
            System.out.print("Masukkan bilangan berikutnya (atau 0 untuk mengakhiri) : ");
            try {
                strbilangan = br.readLine();
            } catch (IOException ioe) {
                System.out.println("Kesalahan IO, program berhenti");
                System.exit(1);
            }
            bilangan = Double.parseDouble(strbilangan);
        }
        double ratarata = jumlah / n;
        if (n == 0) {
            System.out.println("Data kosong, rata-rata tidak bisa dihitung");
        } else {
            System.out.println("Anda memasukkan " + n + " data");
            System.out.println("Rata-rata bilangan yang dimasukkan adalah " + ratarata);
        }
    }
}
