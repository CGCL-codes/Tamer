package javaapplication;

public class Barman {

    public Barman(int PersonaleID) {
        this.PersonaleID = PersonaleID;
    }

    public int getPersonaleID() {
        return PersonaleID;
    }

    public void setPersonaleID(int newPersonaleID) {
        PersonaleID = newPersonaleID;
    }

    int PersonaleID;
}
