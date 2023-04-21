package net.sourceforge.arcamplayer.gui.datamodels;

import net.sourceforge.arcamplayer.library.collection.Field;
import net.sourceforge.arcamplayer.library.collection.Song;

/**
 * <p>Interfaz que debe satisfacer el observador de cambios de información en el
 * modelo de datos de tablas que muestra las listas de canciones.</p>
 * @author David Arranz Oveja, Pelayo Campa González-Nuevo
 */
public interface SongListTableModelChangeListener {

    /**
	 * Mensaje para notificar al observador que ha cambiado un dato.
	 * @param value nuevo valor asignado.
	 * @param songRef canción en la que se ha efectuado el cambio.
	 * @param fieldToChange campo de información afectado.
	 */
    void tableModelValueChanged(Object value, Song songRef, Field fieldToChange);
}
