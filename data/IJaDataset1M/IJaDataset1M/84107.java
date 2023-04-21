package com.geomanalitica.math;

import com.geomanalitica.utils.Formatacao;
import java.util.Scanner;
import com.geomanalitica.utils._3d.Ponto3D;
import com.geomanalitica.utils._3d.Vetor3D;

/**
 *
 * @author rudieri
 */
public class Matematica {

    private Scanner scanner = new Scanner(System.in);

    public void bascara() throws Exception {
        System.out.print("\nA: ");
        int a = scanner.nextInt();
        System.out.print("\nB: ");
        int b = scanner.nextInt();
        System.out.print("\nC: ");
        int c = scanner.nextInt();
        double delta = Math.pow(b, 2) - (4 * a * c);
        if (delta < 0) {
            throw new Exception("Não tem raiz para delta: " + delta);
        }
        double rDelta = Math.pow(delta, 0.5);
        System.out.println("x': " + ((-b + rDelta) / (a * 2)));
        System.out.println("X'': " + ((-b - rDelta) / (a * 2)));
        System.out.println("= = = = = = = = =\n");
    }

    public static void main(String[] args) throws Exception {
        double escalar = 3;
        Vetor3D va = new Vetor3D(3, -1, -2);
        Vetor3D vb = new Vetor3D(2, 4, -1);
        Vetor3D vc = new Vetor3D(-1, 2, 1);
        System.out.println("ab: " + Vetor3D.produtoMisto(va, vb, vc));
    }
}
