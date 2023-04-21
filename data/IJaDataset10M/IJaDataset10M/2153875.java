package br.org.eteg.curso.javaoo.capitulo02.enums;

public class ProgramaDiaSemana {

    /**
	 * @param args
	 */
    public static void main(String[] args) {
        DiaSemana diaDaSemana = DiaSemana.DOMINGO;
        System.out.println(diaDaSemana.getTarefa());
        diaDaSemana = DiaSemana.SEGUNDA;
        System.out.println(diaDaSemana.getTarefa());
        for (DiaSemana dia : DiaSemana.values()) {
            System.out.println(dia.getTarefa());
        }
        diaDaSemana = DiaSemana.valueOf("SEGUNDA");
        System.out.println(diaDaSemana);
        diaDaSemana = DiaSemana.valueOf("QUARTA");
        System.out.println(diaDaSemana.name());
        System.out.println("Posicao: " + diaDaSemana.ordinal());
        int comparar = diaDaSemana.compareTo(diaDaSemana.DOMINGO);
        System.out.println(comparar);
        comparar = diaDaSemana.compareTo(diaDaSemana.SEXTA);
        System.out.println(comparar);
    }
}
