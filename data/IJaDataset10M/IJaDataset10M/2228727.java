package gestion_amie;

public class Amie {

    private int idAmie;

    private String nom;

    private String prenom;

    private String password;

    private String dateNaissance;

    private String telephone;

    private String email;

    private String insee;

    private int nbenfants;

    private String adresse;

    private String cp;

    private String ville;

    public Amie() {
    }

    public Amie(int idAmie, String nom, String prenom, String password, String dateNaissance, String telephone, String email, String insee, int nbenfants, String adresse, String cp, String ville) {
        this.idAmie = idAmie;
        this.nom = nom;
        this.prenom = prenom;
        this.password = password;
        this.dateNaissance = dateNaissance;
        this.telephone = telephone;
        this.email = email;
        this.insee = insee;
        this.nbenfants = nbenfants;
        this.adresse = adresse;
        this.cp = cp;
        this.ville = ville;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getDateNaissance(String type) {
        if (type.equals("sql")) {
            String[] dn = new String[3];
            dn = dateNaissance.split("/");
            String date = dn[2] + "-" + dn[1] + "-" + dn[0] + " 00:00:00";
            return date;
        } else {
            return dateNaissance;
        }
    }

    public void setDateNaissance(String dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getInsee() {
        return insee;
    }

    public void setInsee(String insee) {
        this.insee = insee;
    }

    public int getNbenfants() {
        return nbenfants;
    }

    public void setNbenfants(int nbenfants) {
        this.nbenfants = nbenfants;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public int getIdAmie() {
        return idAmie;
    }

    public void setIdAmie(int id) {
        this.idAmie = id;
    }

    public boolean isNull() {
        if (nom == null || prenom == null || password == null || dateNaissance == null || telephone == null || email == null || insee == null || adresse == null || cp == null || ville == null) return true; else return false;
    }
}
