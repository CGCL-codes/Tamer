package ar.fi.uba.apolo.command;

import java.util.Iterator;
import ar.fi.uba.apolo.entities.Correccion;
import ar.fi.uba.apolo.entities.Examen;
import ar.fi.uba.apolo.entities.Pregunta;
import ar.fi.uba.apolo.entities.Respuesta;

public class CorregirExamenCommand {

    public static void execute(Examen examen, Correccion correccion) {
        Iterator<Pregunta> itPregunta = examen.getPreguntas().iterator();
        Iterator<Respuesta> itRespuesta = correccion.getRespuestas().iterator();
        Double calificacion = new Double(0);
        for (; itPregunta.hasNext() && itRespuesta.hasNext(); ) {
            Pregunta pregunta = (Pregunta) itPregunta.next();
            Respuesta respuesta = (Respuesta) itRespuesta.next();
            if (pregunta.getRespuestaCorrecta().getId().equals(respuesta.getId())) calificacion += 1;
        }
        correccion.setCalificacion(calificacion);
    }
}
