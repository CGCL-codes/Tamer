package br.usp.pulga;

import java.math.BigDecimal;
import java.math.MathContext;

public class CopyOfProof {

    static BigDecimal dois = new BigDecimal(2, MathContext.UNLIMITED);

    static BigDecimal um = new BigDecimal(1, MathContext.UNLIMITED);

    static BigDecimal menosUm = new BigDecimal(-1, MathContext.UNLIMITED);

    static BigDecimal zero = new BigDecimal(0, MathContext.UNLIMITED);

    public static void main(String[] args) {
        {
            BigDecimal m = new BigDecimal(150, MathContext.UNLIMITED);
            BigDecimal delta = new BigDecimal(0.000039d, MathContext.UNLIMITED);
            BigDecimal d = new BigDecimal(0.003, MathContext.UNLIMITED);
            BigDecimal a = new BigDecimal(0.741, MathContext.UNLIMITED);
            BigDecimal b = new BigDecimal(0.85, MathContext.UNLIMITED);
            aFuncao f = new aH();
            System.out.println((delta.multiply(m)) + " deveria ser menor que " + (d.multiply(dois)));
            System.out.println(delta.multiply(m).compareTo(d.multiply(dois)) < 0);
            confereHMaiorQueD(f, a, b, delta, d);
        }
    }

    private static void confereHMaiorQueD(aFuncao f, BigDecimal a, BigDecimal b, BigDecimal delta, BigDecimal d) {
        int i = 0;
        BigDecimal last = new BigDecimal(0, MathContext.UNLIMITED);
        BigDecimal at;
        while (true) {
            last = last.add(delta);
            at = a.add(last);
            if (at.compareTo(b) > 0) {
                System.out.println("Ok");
                break;
            }
            BigDecimal resultado = f.at(at);
            if (resultado.compareTo(d) < 0) {
                System.out.println("Oops @ " + at + " deu " + resultado);
                System.exit(0);
            }
            i++;
            if (i % 250 == 0) {
                System.out.println(at + " " + " valia " + resultado.toPlainString());
                System.gc();
            }
        }
    }
}

class aH implements aFuncao {

    public BigDecimal at(BigDecimal c) {
        BigDecimal resultado[] = new BigDecimal[] { CopyOfProof.zero, c };
        for (int i = 1; i <= 5; i++) {
            BigDecimal a = resultado[1];
            BigDecimal b = c.multiply(CopyOfProof.um.subtract(resultado[0].multiply(resultado[0])).add(resultado[1].multiply(resultado[1])));
            resultado[0] = a;
            resultado[1] = b;
        }
        return resultado[1].multiply(CopyOfProof.menosUm);
    }
}

class aQuadrado implements aFuncao {

    public BigDecimal at(BigDecimal x) {
        return x.multiply(x).add(CopyOfProof.um);
    }
}

interface aFuncao {

    BigDecimal at(BigDecimal at);
}
