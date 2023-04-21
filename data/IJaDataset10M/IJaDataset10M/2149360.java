package ar.fi.uba.apolo.command;

import ar.fi.uba.apolo.entities.Correccion;
import ar.fi.uba.apolo.entityManager.CorreccionManager;

public class GuardarResolucionExamenCommand {

    public static void execute(Correccion correccion) {
        CorreccionManager.save(correccion);
    }
}
