package fr.cpbrennestt.presentation.display.frontal.news;

public class DbCoupleMois {

    private DbMois dbMois1;

    private DbMois dbMois2;

    public DbCoupleMois(DbMois dbMois1, DbMois dbMois2) {
        this.dbMois1 = dbMois1;
        this.dbMois2 = dbMois2;
    }

    public DbCoupleMois() {
    }

    public DbMois getDbMois1() {
        return dbMois1;
    }

    public void setDbMois1(DbMois dbMois1) {
        this.dbMois1 = dbMois1;
    }

    public DbMois getDbMois2() {
        return dbMois2;
    }

    public void setDbMois2(DbMois dbMois2) {
        this.dbMois2 = dbMois2;
    }
}
