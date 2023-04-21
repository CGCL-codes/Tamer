package solarex.system;

import java.util.Random;

/**
 * Name genrator. Currently offers methods to generate star, planet
 * and space station names.
 * 
 * @author Hj. Malthaner
 */
public class NameGenerator {

    private static final String starSyllables[] = { "a", "e", "i", "o", "u", "y", "al", "de", "gel", "ri", "be", "tei", "geu", "ze", "ge", "sil", "ste", "gro", "ker", "ti", "pel", "kon", "ons", "da", "tor", "se", "lie", "pe", "ben", "rei", "tur", "sel", "fie", "pam", "mer", "ni", "dri", "zin", "art", "wer", "den", "ke", "an", "sam", "ren", "di", "ma", "bar", "wei", "tel", "big", "ra", "ver", "mi", "ro", "la", "re", "zep", "tor", "tak", "ker", "ve", "der", "ri", "bo", "nis", "ta", "si", "min", "us", "run" };

    private static final String planetSyllables[] = { "a", "e", "i", "o", "u", "y", "mer", "cur", "ius", "ve", "nus", "ter", "ra", "ma", "rs", "ju", "pi", "nep", "tun", "ra", "plu", "to", "cha", "ron", "en", "ca", "le", "dus", "mi", "ran", "da", "ga", "ny", "med", "ti", "tan" };

    private static final String persons[] = { "Kopernikus", "Brahe", "Kepler", "Galilei", "Scheiner", "Huygens", "Cassini", "Halley", "Newton", "Messier", "Herschel", "Olbers", "Bessel", "Struve", "Schwabe", "Fizeau", "Foucault", "Leverrier", "Kirchhoff", "Schiaparelli", "Pickering", "Hale", "Wolf", "Kapteyn", "Leavitt", "Russel", "Einstein", "Curtis", "Eddington", "Hubble", "Tombaugh", "Lyot", "Jansky", "Oort", "Van Allen", "Bell", "Hooke", "Abbot", "Adams", "Agrippa", "Anderson", "Yuzhe", "Chongzhi", "Zwicky", "Young", "Wirtanen", "Westphal", "Whitford", "Vyssotsky", "Turner", "Song", "Suntzeff", "Steinheil", "Shoemaker", "Seyfert", "Chandra", "Peltier", "Piazzi", "Newcomb", "Mayer", "Mitchell", "Laplace", "Hansen", "Graham", "Goldberg", "Delaunay", "Denning", "Dyson", "Encke", "Gauss" };

    private static final String personsFloat[] = { "Aweio", "Uiomya", "Eeiadma", "Meowia", "Saulee", "Liyrino", "Aeiwouu", "Oiuuui", "Uiaoow", "Yiawou", "Uioaowaa", "Oouwuush", "Shooowa", "Geeshuuu", "Eemyuio", "Maowiee", "Sasaa", "Nouwaa", "Youush", "Saamaana", "Woumea", "Laamao", "Yiyowou", "Oulou", "Eemeea", "Neaowo", "Dlawee", "Jouwaa", "Yioyo", "Yee", "Daa", "Noua", "Ouwo", "Anou" };

    private static final String personsRock[] = { "Zkrmths", "Krtbrgd", "Prtsngra", "Drptwrt", "Krimprbt", "Tikimkrt", "Grtkrdrg", "Msntkrms", "Raknrtsk", "Zik", "Pst", "Krkro", "Snurkt", "Smargd", "Rkrit", "Tsikrk", "Catrmt", "Mgrek", "Grtok", "Raktrk", "Snrkit", "Prekrk", "Krkrtik", "Aktraps", "Zigzik", "Tict", "Pritr", "Zrn", "Aktr", "Emptk", "Krark", "Kikr", "Krtki" };

    private static final String personsBreath[] = { "Altramer", "Shetan", "Soanow", "Meroweni", "Parledo", "Winniwo", "Lamerat", "Poilygun", "Ferator", "Varioni", "Germigo", "Farantor", "Tormeg", "Megmeg", "Liawisu", "Ferontea", "Salabero", "Ergota", "Wowanoui", "Wykikro", "Zirmonag", "Mermimg", "Korwang", "Fremong", "Garawen", "Pelogon", "Diamana", "Dreogar", "Lonamer" };

    private static final String stations[] = { "Depot", "Station", "Habitat", "Structure", "Fort", "Outpost", "Lookout", "Spindle", "Wheel", "Box" };

    private static final String ports[] = { "Complex", "Depot", "Transfer", "Port", "Main", "Central", "City", "Habitat", "Structure", "Fort", "Outpost", "Lookout", "Starport", "Spaceport", "Branch", "Field", "Dock", "Launchpad", "Kontor", "Base" };

    private static String generateNameFromSyllables(final Random rng, final String[] syllables) {
        final int n = 2 + rng.nextInt(2);
        final StringBuffer buf = new StringBuffer();
        int lastSyllable = -1;
        for (int i = 0; i < n; i++) {
            int syllable;
            do {
                syllable = rng.nextInt(syllables.length);
            } while (lastSyllable < 6 && syllable == lastSyllable);
            buf.append(syllables[syllable]);
            lastSyllable = syllable;
            if (i == 0 && rng.nextInt(17) <= 2) {
                buf.append('\'');
            }
        }
        String name = buf.toString();
        name = name.substring(0, 1).toUpperCase() + name.substring(1);
        return name;
    }

    public static String generateStarName(Random rng) {
        return generateNameFromSyllables(rng, starSyllables);
    }

    public static String generatePlanetName(Random rng) {
        return generateNameFromSyllables(rng, planetSyllables);
    }

    /**
     * Space station names for humans.
     *
     * @param rng The random number generator to use.
     * @return the generated name.
     */
    public static String generateStationName(Random rng) {
        StringBuffer buf = new StringBuffer();
        int desc = rng.nextInt(stations.length);
        int person = rng.nextInt(persons.length);
        if (desc < 3) {
            buf.append(persons[person]);
            buf.append(" ");
            buf.append(stations[desc]);
        } else {
            buf.append(stations[desc]);
            buf.append(" ");
            buf.append(persons[person]);
        }
        return buf.toString();
    }

    /**
     * Race specific space station names.
     *
     * @param rng The random number generator to use.
     * @return the generated name.
     */
    public static String generateStationName(final Random rng, final Society.Race race) {
        final StringBuffer buf = new StringBuffer();
        final int desc = rng.nextInt(stations.length);
        String person;
        if (race == Society.Race.Clonkniks) {
            person = "" + (char) ('A' + rng.nextInt(26)) + (char) ('0' + rng.nextInt(9)) + (char) ('0' + rng.nextInt(9)) + (char) ('0' + rng.nextInt(9)) + (char) ('0' + rng.nextInt(9)) + (char) ('0' + rng.nextInt(9));
        } else if (race == Society.Race.Floatees) {
            person = personsFloat[rng.nextInt(personsFloat.length)];
        } else if (race == Society.Race.Poisonbreathers) {
            person = personsBreath[rng.nextInt(personsBreath.length)];
        } else if (race == Society.Race.Rockeaters) {
            person = personsRock[rng.nextInt(personsRock.length)];
        } else {
            person = persons[rng.nextInt(persons.length)];
        }
        if (desc < 3) {
            buf.append(person);
            buf.append(" ");
            buf.append(stations[desc]);
        } else {
            buf.append(stations[desc]);
            buf.append(" ");
            buf.append(person);
        }
        return buf.toString();
    }

    /**
     * Race specific space spaceport names.
     * 
     * @param rng The random number generator to use.
     * @return the generated name.
     */
    public static String generateSpaceportName(final Random rng, final Society.Race race) {
        final StringBuffer buf = new StringBuffer();
        final int desc = rng.nextInt(ports.length);
        String person;
        if (race == Society.Race.Clonkniks) {
            person = "" + (char) ('A' + rng.nextInt(26)) + (char) ('0' + rng.nextInt(9)) + (char) ('0' + rng.nextInt(9)) + (char) ('0' + rng.nextInt(9)) + (char) ('0' + rng.nextInt(9)) + (char) ('0' + rng.nextInt(9));
        } else if (race == Society.Race.Floatees) {
            person = personsFloat[rng.nextInt(personsFloat.length)];
        } else if (race == Society.Race.Poisonbreathers) {
            person = personsBreath[rng.nextInt(personsBreath.length)];
        } else if (race == Society.Race.Rockeaters) {
            person = personsRock[rng.nextInt(personsRock.length)];
        } else {
            person = persons[rng.nextInt(persons.length)];
        }
        if (desc < 8) {
            buf.append(person);
            buf.append(" ");
            buf.append(ports[desc]);
        } else {
            buf.append(ports[desc]);
            buf.append(" ");
            buf.append(person);
        }
        return buf.toString();
    }
}
