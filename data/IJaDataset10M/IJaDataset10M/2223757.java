package br.com.ximp.ex1;

public class MyPotenciaService implements PotenciaService {

    public double potencia(double a, int b) {
        System.out.println("NA UNHA");
        double result = 1;
        for (int i = 0; i < b; i++) {
            result *= a;
        }
        return result;
    }
}
