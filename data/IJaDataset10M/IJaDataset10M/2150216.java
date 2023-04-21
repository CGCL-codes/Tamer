package de.andreavicentini.teatralia.pages.repertoire;

import org.magiclabs.htmlx.TextStructure;
import de.andreavicentini.teatralia.pages.ObjectListModel;
import de.andreavicentini.teatralia.pages.TDate;
import de.andreavicentini.teatralia.pages.Person.Sex;

public class AuthorsModel extends ObjectListModel<Author> {

    public enum KeysAuthors implements TextStructure.TextKey {

        pirandello_description, buzzati_description, goldoni_description
    }

    public final Author pirandello, buzzati, goldoni;

    public AuthorsModel() {
        this.add(this.pirandello = new Author("Luigi", "Pirandello", Sex.M, new TDate(1867, 6, 28), "Agrigento", new TDate(1936, 12, 10), "Roma", KeysAuthors.pirandello_description));
        this.add(this.buzzati = new Author("Dino", "Buzzati", Sex.M, new TDate(1906, 10, 16), "San Pellegrino di Belluno", new TDate(1972, 1, 28), "Milano", KeysAuthors.buzzati_description));
        this.add(this.goldoni = new Author("Carlo", "Goldoni", Sex.M, new TDate(1707, 2, 15), "Venezia", new TDate(1793, 2, 6), "Paris", KeysAuthors.goldoni_description));
    }
}
