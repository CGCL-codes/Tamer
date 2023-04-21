package se.studieren.dbvote.importer.parser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import se.studieren.dbvote.importer.parser.Importer.Parteien.Partei;

public class Importer {

    private static String encoding = "ISO-8859-1";

    public static Result analyze(InputStream inputStream) throws Exception {
        Bund bund = null;
        Parteien parties = null;
        Set<Wahlkreis> openKreise = new HashSet<Wahlkreis>();
        Set<Bundesland> openBundeslaender = new HashSet<Bundesland>();
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream, encoding));
        Line nextLine = getNextElement(br);
        while (nextLine != null) {
            if (nextLine.isParteien()) {
                if (parties != null) {
                    throw new ParseException("Parteien mehrfach vorhanden");
                }
                parties = (Parteien) nextLine;
            }
            if (nextLine.isWahlergebnis()) {
                Wahlergebnis wahlergebnis = (Wahlergebnis) nextLine;
                if (wahlergebnis.isBund()) {
                    if (bund == null) {
                        bund = (Bund) nextLine;
                        for (Bundesland openBundesland : openBundeslaender) bund.addBundesland(openBundesland);
                    } else {
                        throw new ParseException("Bund mehrfach vorhanden");
                    }
                } else if (wahlergebnis.isBundesland()) {
                    Bundesland bundesland = (Bundesland) nextLine;
                    if (bund != null) {
                        if (bund.getBundeslaender().contains(bundesland)) throw new ParseException("Bundesland mehrfach vorhanden");
                        bund.addBundesland(bundesland);
                    } else {
                        openBundeslaender.add(bundesland);
                    }
                    for (Wahlkreis wahlkeis : openKreise) {
                        if (bundesland.getId() == wahlkeis.getBundeslandId()) {
                            bundesland.addWahlkreis(wahlkeis);
                        }
                    }
                } else if (wahlergebnis instanceof Wahlkreis) {
                    Wahlkreis wahlkreis = (Wahlkreis) nextLine;
                    if (bund != null) {
                        Bundesland bundesland = bund.getBundesland(wahlkreis.getBundeslandId());
                        if (bundesland != null) bundesland.addWahlkreis(wahlkreis); else {
                            for (Bundesland expBundesland : openBundeslaender) {
                                if (expBundesland.getId() == wahlkreis.getBundeslandId()) {
                                    bundesland = expBundesland;
                                    break;
                                }
                            }
                            if (bundesland != null) {
                                bundesland.addWahlkreis(wahlkreis);
                            } else {
                                openKreise.add(wahlkreis);
                            }
                        }
                    } else {
                        Bundesland bundesland = null;
                        for (Bundesland expBundesland : openBundeslaender) {
                            if (expBundesland.getId() == wahlkreis.getBundeslandId()) {
                                bundesland = expBundesland;
                                break;
                            }
                        }
                        if (bundesland != null) {
                            bundesland.addWahlkreis(wahlkreis);
                        } else {
                            openKreise.add(wahlkreis);
                        }
                    }
                }
            }
            nextLine = getNextElement(br);
        }
        if (bund == null) {
            throw new ParseException("Bund nicht gefunden");
        }
        if (parties == null) {
            throw new ParseException("Parteien nicht gefunden");
        }
        return new Result(parties.getParteien(), bund);
    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
            return true;
        } catch (NumberFormatException nFE) {
            return false;
        }
    }

    private static Line getNextElement(BufferedReader isr) throws IOException {
        String strLine = isr.readLine();
        String[] data;
        if (strLine == null) return null; else data = strLine.split(";");
        if (data == null) {
            return null;
        } else if (data.length < 3) {
            return new None();
        } else if (isInteger(data[0]) && data[2].length() == 0) {
            return new Bund(data);
        } else if (isInteger(data[0]) && data[2].equals("99")) {
            return new Bundesland(data);
        } else if (isInteger(data[0]) && isInteger(data[2])) {
            return new Wahlkreis(data);
        } else if (data.length > 0 && "Nr".equals(data[0])) {
            return new Parteien(data);
        } else {
            return new None();
        }
    }

    public static class Result {

        private Collection<Partei> parties;

        private Bund bund;

        public Result(Collection<Partei> parties, Bund bund) {
            this.parties = parties;
            this.bund = bund;
        }

        public Collection<Partei> getParteien() {
            return parties;
        }

        public Bund getBund() {
            return bund;
        }
    }

    public static class Line {

        public boolean isWahlergebnis() {
            return false;
        }

        public boolean isParteien() {
            return false;
        }
    }

    public static class Parteien extends Line {

        private Collection<Partei> parties = new ArrayList<Partei>();

        public Parteien(String[] data) {
            for (int i = 19; i < data.length; i = i + 4) {
                parties.add(new Partei(i, data[i], i));
            }
        }

        public boolean isParteien() {
            return true;
        }

        public Collection<Partei> getParteien() {
            return parties;
        }

        public static class Partei {

            private String name;

            private int column;

            private int id;

            public Partei(int id, String name, int column) {
                this.id = id;
                this.name = name;
                this.column = column;
            }

            public String getName() {
                return name;
            }

            int getColumn() {
                return column;
            }

            @Override
            public String toString() {
                return name;
            }

            public int getId() {
                return id;
            }
        }
    }

    public static class Wahlergebnis extends Line {

        private String data[];

        private String name;

        private int id;

        private int anzahlWahlberechtigte;

        private int anzahlWaehler;

        private int anzahlUngueltigErststimme;

        private int anzahlUngueltigZweitstimme;

        public Wahlergebnis(String[] data) {
            this.data = data;
            name = data[1];
            id = Integer.parseInt(data[0]);
            anzahlWahlberechtigte = Integer.parseInt(data[3]);
            anzahlWaehler = Integer.parseInt(data[7]);
            anzahlUngueltigErststimme = parseStimmenanzahl(11);
            anzahlUngueltigZweitstimme = parseStimmenanzahl(13);
        }

        public boolean isWahlergebnis() {
            return true;
        }

        public String toString() {
            return name;
        }

        public int getId() {
            return id;
        }

        public boolean isBund() {
            return false;
        }

        public boolean isBundesland() {
            return false;
        }

        public boolean isWahlkeis() {
            return false;
        }

        private int parseStimmenanzahl(int column) {
            if (data.length <= column) {
                return 0;
            }
            if (data[column].length() == 0) return 0;
            return Integer.parseInt(data[column]);
        }

        public int getAnzahlWahlberechtigte() {
            return anzahlWahlberechtigte;
        }

        public int getAnzahlWaehler() {
            return anzahlWaehler;
        }

        public int getAnzahlErststimmen(Partei partei) {
            return parseStimmenanzahl(partei.getColumn());
        }

        public int getAnzahlZweitstimmen(Partei partei) {
            return parseStimmenanzahl(partei.getColumn() + 2);
        }

        public int getAnzahlErstUngueltig() {
            return anzahlUngueltigErststimme;
        }

        public int getAnzahlZweitUngueltig() {
            return anzahlUngueltigZweitstimme;
        }

        @Deprecated
        public int getAnzahlErstEnthaltungen() {
            return 0;
        }

        @Deprecated
        public int getAnzahlZweitEnthaltungen() {
            return 0;
        }
    }

    public static class Bund extends Wahlergebnis {

        private Set<Bundesland> bundeslaender = new HashSet<Bundesland>();

        public Bund(String[] data) {
            super(data);
        }

        public void addBundesland(Bundesland bundesland) {
            bundeslaender.add(bundesland);
        }

        public Set<Bundesland> getBundeslaender() {
            return bundeslaender;
        }

        public Bundesland getBundesland(int id) {
            for (Bundesland bundesland : bundeslaender) {
                if (bundesland.getId() == id) return bundesland;
            }
            return null;
        }

        public boolean isBund() {
            return true;
        }
    }

    public static class Bundesland extends Wahlergebnis {

        private Set<Wahlkreis> wahlkreise = new HashSet<Wahlkreis>();

        public Bundesland(String[] data) {
            super(data);
        }

        public Set<Wahlkreis> getWahlkreise() {
            return wahlkreise;
        }

        public boolean isBundesland() {
            return true;
        }

        public void addWahlkreis(Wahlkreis wahlkreis) {
            wahlkreise.add(wahlkreis);
        }
    }

    public static class Wahlkreis extends Wahlergebnis {

        private int bundeslandId;

        public Wahlkreis(String[] data) {
            super(data);
            bundeslandId = Integer.parseInt(data[2]);
        }

        public boolean isWahlkreis() {
            return true;
        }

        public int getBundeslandId() {
            return bundeslandId;
        }
    }

    public static class None extends Line {
    }
}
