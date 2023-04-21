package adressepostale;

/**
 * Les informations d'acheminement (hameau ou lieu-dit, les indications de distribution spéciale, le
 * numéro de code postal et le nom de la localité de distribution)
 */
public interface Acheminement extends Ligneable {

    String getCodePostal();

    String getVille();
}
