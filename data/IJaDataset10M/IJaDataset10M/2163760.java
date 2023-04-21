package net.narusas.si.auction.fetchers;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import net.narusas.si.auction.model.건물;
import net.narusas.si.auction.model.토지;
import net.narusas.util.lang.NFile;

public class 부동산표시TypeMatcher {

    private static LinkedList<String> 토지매칭테이블;

    private static LinkedList<Entry> 대지매칭테이블;

    private static LinkedList<Entry> 건물매칭테이블;

    private static LinkedList<Entry> 전유부분매칭테이블;

    static {
        loadLandWords();
        loadTypeMatches();
    }

    private static void loadLandWords() {
        토지매칭테이블 = new LinkedList<String>();
        try {
            String text = NFile.getText(new File("cfg/land.txt"), "euc-kr");
            String[] values = text.split(",");
            for (String v : values) {
                if ("".equals(v.trim())) {
                    continue;
                }
                토지매칭테이블.add(v.trim());
            }
        } catch (IOException e) {
        }
    }

    /**
	 * Load type matches.
	 */
    private static void loadTypeMatches() {
        대지매칭테이블 = new LinkedList<Entry>();
        건물매칭테이블 = new LinkedList<Entry>();
        전유부분매칭테이블 = new LinkedList<Entry>();
        List<Entry> target = 대지매칭테이블;
        try {
            String text = NFile.getText(new File("cfg/type_match.txt"), "euc-kr");
            String[] lines = text.split("\n");
            for (String line : lines) {
                if (line.startsWith("----1")) {
                    target = 건물매칭테이블;
                    continue;
                }
                if (line.startsWith("----2")) {
                    target = 전유부분매칭테이블;
                    continue;
                }
                String[] tokens = line.split(",");
                if (tokens.length == 2) {
                    target.add(new Entry(tokens[0], Integer.parseInt(tokens[1].trim())));
                } else {
                    String[] key = new String[tokens.length - 1];
                    System.arraycopy(tokens, 0, key, 0, tokens.length - 1);
                    target.add(new Entry(key, Integer.parseInt(tokens[tokens.length - 1].trim())));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    static String matchStart토지(String detail) {
        for (String land : 토지매칭테이블) {
            if (detail.startsWith(land)) {
                return land;
            }
        }
        return null;
    }

    static boolean isStart토지(String detail) {
        for (String land : 토지매칭테이블) {
            if (land.equals(firstWord(detail))) {
                return true;
            }
        }
        return false;
    }

    private static String firstWord(String detail) {
        String[] lines = detail.split("\n");
        String[] words = lines[0].split(" ");
        words = words[0].split("\\(");
        words = words[0].split("\\[");
        return words[0];
    }

    public static int calcType(boolean hasBuilding, boolean has전유부분, Collection<토지> lands, Collection<건물> blds, int type) {
        List<Integer> types = new LinkedList<Integer>();
        if (hasBuilding) {
            if (has전유부분) {
                if (blds == null) return type;
                for (건물 bld : blds) {
                    List<Entry> target = 전유부분매칭테이블;
                    type = iterateTypes(type, bld, target);
                    types.add(type);
                }
                return findHighestType(types, 전유부분매칭테이블);
            } else {
                if (blds == null) return type;
                for (건물 bld : blds) {
                    List<Entry> target = 건물매칭테이블;
                    type = iterateTypes(type, bld, target);
                    types.add(type);
                }
                return findHighestType(types, 건물매칭테이블);
            }
        } else {
            if (lands == null) return type;
            for (토지 land : lands) {
                Iterator<Entry> entries = 대지매칭테이블.iterator();
                while (entries.hasNext()) {
                    Entry entry = entries.next();
                    if (land.isTypeMatch(entry.getKey())) {
                        type = entry.getValue();
                        types.add(type);
                        break;
                    }
                }
            }
            return findHighestType(types, 대지매칭테이블);
        }
    }

    static int iterateTypes(int type, 건물 bld, List<Entry> target) {
        Iterator<Entry> entries = target.iterator();
        while (entries.hasNext()) {
            Entry entry = entries.next();
            if (bld.match(entry.getKey())) {
                type = entry.getValue();
                break;
            }
        }
        return type;
    }

    static int findHighestType(List<Integer> types, List<Entry> matches) {
        for (int i = 0; i < matches.size(); i++) {
            Entry e = matches.get(i);
            if (e.matchAnything(types)) {
                return e.getValue();
            }
        }
        return 410;
    }
}
