package gig.examples.songlist;

import org.eiichiro.acidhouse.Entity;
import org.eiichiro.acidhouse.Key;

@Entity
public class Song {

    @Key
    String title;

    String album;

    String author;
}
