package ar.com.larreta.intercambio.client.comunes;

public abstract class TiposRegion extends Dato {

    public static final Dato PAIS = new TipoRegion("Pais");

    public static final Dato PROVINCIA = new TipoRegion("Provincia");

    public static final Dato LOCALIDAD = new TipoRegion("Localidad");
}
