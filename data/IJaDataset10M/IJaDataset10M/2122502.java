package db.model.identity;

public class Id_IdDay_IdUserAndName extends IdAndName {

    private final Id_IdDay_IdUser id_IdDay_IdUser = new Id_IdDay_IdUser();

    public int getIdDay() {
        return this.id_IdDay_IdUser.getIdDay();
    }

    public void setIdDay(int idDay) {
        this.id_IdDay_IdUser.setIdDay(idDay);
    }

    public int getIdUser() {
        return this.id_IdDay_IdUser.getIdUsers();
    }

    public void setIdUser(int idUsers) {
        this.id_IdDay_IdUser.setIdUsers(idUsers);
    }
}
