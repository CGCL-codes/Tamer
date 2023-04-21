package juploader.plugin;

/**
 * Interfejs oznaczający zdolność wtyczki do zwracana metainformacji w postaci
 * obiektu PluginInfo.
 *
 * @author Adam Pawelec
 */
public interface InfoablePlugin {

    /**
     * Zwraca obiekt zawierajacy metainformacje o pluginie.
     *
     * @return obiekt z metainformacjami o pluginie
     */
    PluginInfo getPluginInfo();
}
