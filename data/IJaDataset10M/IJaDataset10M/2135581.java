package mainsuit;

import java.io.*;

public class MainSuit {

    public static int userMenang = 0;

    public static int komputerMenang = 0;

    public static int seri = 0;

    public static void mainGame() {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String input = null;
        boolean inputvalid = false;
        int suitKomputer = 0;
        int suitUser = 0;
        while (!inputvalid) {
            System.out.print("Masukkan suit Anda ");
            try {
                input = br.readLine();
            } catch (IOException ioe) {
                System.out.println("Kesalahan IO, program berhenti");
                System.exit(1);
            }
            if (input.length() > 0) {
                switch(input.charAt(0)) {
                    case 'j':
                    case 'J':
                        suitUser = 0;
                        inputvalid = true;
                        break;
                    case 't':
                    case 'T':
                        suitUser = 1;
                        inputvalid = true;
                        break;
                    case 'k':
                    case 'K':
                        suitUser = 2;
                        inputvalid = true;
                        break;
                }
            }
        }
        suitKomputer = (int) (Math.random() * 3);
        if (suitKomputer == 3) {
            suitKomputer = 2;
        }
        int delta = suitKomputer - suitUser;
        switch(delta) {
            case 0:
                seri++;
                System.out.println("Hasilnya : Seri");
                break;
            case 1:
            case -2:
                userMenang++;
                System.out.println("Hasilnya : Anda menang");
                break;
            case -1:
            case 2:
                komputerMenang++;
                System.out.println("Hasilnya : Anda kalah");
                break;
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        System.out.println("Permainan suit");
        System.out.println("==============");
        System.out.println("Masukkan salah satu dari 3 kemungkinan :");
        System.out.println("J untuk Jempol");
        System.out.println("T untuk Telunjuk");
        System.out.println("K untuk Kelingking");
        System.out.println("");
        while (true) {
            mainGame();
            System.out.println("Statistik :");
            System.out.println("Komputer = " + komputerMenang + "    Anda = " + userMenang + "    Seri = " + seri);
            System.out.println("");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            String input = null;
            System.out.print("Main lagi (Y/T) ? ");
            try {
                input = br.readLine();
            } catch (IOException ioe) {
                System.out.println("Kesalahan IO, program berhenti");
                System.exit(1);
            }
            if ((input.charAt(0) == 't') || (input.charAt(0) == 'T')) break;
        }
    }
}
