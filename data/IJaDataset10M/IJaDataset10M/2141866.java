package com.celiosilva.simbanc.teste;

import org.apache.log4j.Logger;

/**
 *
 * @author celio@celiosilva.com
 */
public class TesteWrappers {

    private static final Logger log = Logger.getLogger(TesteWrappers.class);

    public static void main(String args) {
        byte b = 0;
        Byte byte_ = Byte.valueOf(b);
        Short short_ = new Short(b);
        Integer int_ = Integer.valueOf(0);
        Long long_ = new Long(989898989898989L);
        Character char_ = new Character('a');
        Float float_ = new Float("3322.8");
        Double double_ = Double.valueOf(0.001234123);
    }

    public static void main(String... args) {
        long inicio = System.currentTimeMillis();
        log.info("inicio em:" + inicio);
        for (int i = 0; i < 1000000000; i++) {
        }
        long fim = System.currentTimeMillis();
        log.info("finalização em:" + fim);
        log.info("total tempo gasto:" + ((fim - inicio) / 1000));
    }
}
