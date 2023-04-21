package es.cea;

import org.testng.annotations.Test;

public class PreguntaTest {

    @Test
    public void referenciaPreguntaIdentidad() {
        Examen examen1 = new Examen("ref1");
        Examen examen2 = new Examen("ref2");
        Pregunta pregunta1 = new Pregunta(examen1);
        Pregunta pregunta2 = new Pregunta(examen2);
        Pregunta pregunta1Bis = new Pregunta(examen1);
        boolean igualdad1 = pregunta1.equals(pregunta2);
        assert (!igualdad1) : "La pregunta 1 debe ser diferente de la 2";
        boolean igualdad2 = pregunta1.equals(pregunta1Bis);
        assert (igualdad2) : "La pregunta 1 debe ser igual que la 1Bis";
        int a = 1, b = 1, resultado;
        resultado = a + b;
        assert (resultado == 2) : "1+1 de toda la vida ha sido 2, imbecil";
    }
}
