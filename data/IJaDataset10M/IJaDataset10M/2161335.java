package org.okcjug.december.util;

import java.util.ArrayList;
import java.util.List;
import org.okcjug.december.model.Movie;

public class MovieFactory {

    public static Movie getClerks() {
        List<String> quotes = new ArrayList<String>();
        quotes.add("There's a million fine looking women in the world, dude. " + "But, they don't all bring you lasagna at work. Most of 'em just cheat on you.");
        quotes.add("My friend here is trying to convince me that any independent contractors " + "who were working on the uncompleted Death Star, were innocent victims when " + "they were destroyed by the Rebels.");
        quotes.add("I'm a firm believer in the philosophy of a ruling class. Especially since I rule.");
        quotes.add("Salsa shark. We're gonna need a bigger boat. Man goes into cage, cage goes into " + "salsa, shark's in the salsa.");
        return new Movie("Clerks", quotes);
    }

    public static Movie getDrStrangelove() {
        List<String> quotes = new ArrayList<String>();
        quotes.add("The whole point of the doomsday machine is lost... if you keep it a " + "secret! Why didn't you tell the world, ay?!");
        quotes.add("Do you realize that fluoridation is the most monstrously conceived and " + "dangerous communist plot we have ever had to face?");
        quotes.add("Gentlemen, you can't fight in here! This is the War Room!");
        return (new Movie("Dr. Strangelove or: How I Learned to Stop Worrying and Love the Bomb", quotes));
    }

    public static Movie getKentuckyFriedMovie() {
        List<String> quotes = new ArrayList<String>();
        quotes.add("Although, so far there's no known treatment for death's crippling effects, still everyone " + "can acquaint himself with the three early warning signs of death: one, rigor mortis; two, a " + "rotting smell; three, occasional drowsiness.");
        return new Movie("The Kentucky Fried Movie", quotes);
    }

    public static Movie getHolyGrail() {
        List<String> quotes = new ArrayList<String>();
        quotes.add("You mother was a hamster and your father smelt of elderberries.");
        quotes.add("I told you, we're an anarcho-syndicist commune, we take it in " + "turns to act as a sort of executive officer for the week.");
        return new Movie("Monty Python and the Holy Grail", quotes);
    }

    public static Movie getMadMaxThunderDome() {
        List<String> quotes = new ArrayList<String>();
        quotes.add("Two men enter, one man leaves.");
        quotes.add("How the world turns. One day cock-of-the-walk, next a feather duster.");
        return new Movie("Mad Max Beyond Thunderdome", quotes);
    }

    public static Movie getMysteryMen() {
        List<String> quotes = new ArrayList<String>();
        quotes.add("We're not your classic superheros. We're not the favorites. We're the other guys.");
        quotes.add("Don't mess with the volcano my man, 'cause I will go Pompeii on your... butt. ");
        quotes.add("We struck down evil with the mighty sword of teamwork and the hammer of not bickering.");
        quotes.add("All I'm saying is, when we split the cheque three ways the steak-eater picks the pocket of the salad-man.");
        return new Movie("Mystery Men", quotes);
    }

    public static Movie getRockyIII() {
        List<String> quotes = new ArrayList<String>();
        quotes.add("This guy will kill you to death inside a three rounds!");
        quotes.add("Hey, Paulie, don't get mentally irregular.");
        return new Movie("Rocky III", quotes);
    }

    public static Movie getTopSecret() {
        List<String> quotes = new ArrayList<String>();
        quotes.add("I'm not the first guy who fell in love with a girl he met in a restaurant, who " + "then turned out to be the daughter of a kidnapped scientist, only to lose her to a " + "childhood lover who she's last seen on a deserted island, and who turned out fifteen " + "years later to be the leader of the French Underground.");
        return new Movie("Top Secret!", quotes);
    }

    public static Movie getSierraMadre() {
        List<String> quotes = new ArrayList<String>();
        quotes.add("Badges? We ain't got no badges. We don't need no badges. I don't have to show you " + "any stinking badges!");
        return new Movie("The Treasure of the Sierra Madre", quotes);
    }

    public static Movie getPatton() {
        List<String> quotes = new ArrayList<String>();
        quotes.add("Now I want you to remember that no bastard ever won a war by dying for his country. " + "He won it by making the other poor dumb bastard die for his country.");
        quotes.add("There's only one proper way for a professional soldier to die: the last bullet of the " + "last battle of the last war.");
        quotes.add("I'm not going to subsidize cowardice.");
        return new Movie("Patton", quotes);
    }

    public static Movie getTheJerk() {
        List<String> quotes = new ArrayList<String>();
        quotes.add("He hates these cans. Stay away from the cans.");
        quotes.add("Lord loves a workin' man; don't trust whitey; see a doctor and get rid of it.");
        quotes.add("I don't care about losing all the money. It's losing all the stuff.");
        quotes.add("For one dollar I'll guess your weight, your height, or your sex.");
        return new Movie("The Jerk", quotes);
    }

    public static Movie getSummerOfSam() {
        List<String> quotes = new ArrayList<String>();
        return new Movie("Summer of Sam", quotes);
    }
}
