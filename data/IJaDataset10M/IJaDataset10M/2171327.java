package com.diccionarioderimas.generator;

public class RhymeFinder {

    public static final int VOWEL = 0;

    public static final int CONSONANT = 1;

    public static final int SOFT = 0;

    public static final int HARD = 1;

    public static final int AGUDA = 0;

    public static final int LLANA = 1;

    public static final int ESDRU = 2;

    private String word;

    private String rhyme;

    private String asonance;

    private int[] oldSil;

    private int location;

    private int silablesTotal;

    private int accentedSil;

    public RhymeFinder(String word) {
        this.word = word;
        calculate();
    }

    public boolean startsWithVoH() {
        char[] chars = word.toCharArray();
        if (chars[0] == 'h' && chars.length > 1) {
            if ((chars[1] != 'i' && chars[1] != 'u') || (chars[1] != 'i' && chars[1] != 'a' && chars[1] != 'e' && chars[1] != '�' && chars[1] != '�' && chars[1] != '�')) return true; else return false;
        }
        return isVowel(chars[0]);
    }

    public int getSilable(int z) {
        if (z > word.length() || z < 0) return -1;
        int res = -1;
        for (int j = 0; j < oldSil.length; j++) {
            if (z >= oldSil[j] && (j == oldSil.length - 1 || z < oldSil[j + 1])) res = j;
        }
        return res;
    }

    public int countSil() {
        return silablesTotal;
    }

    private void calculate() {
        char[] chars = word.toCharArray();
        int[] oldTypes = new int[chars.length];
        int[] oldSoft = new int[chars.length];
        for (int i = 0; i < chars.length; i++) {
            if (isVowel(chars[i]) || (chars[i] == 'y' && (i + 1 == chars.length || (i + 1 < chars.length && !isVowel(chars[i + 1]))))) oldTypes[i] = VOWEL; else oldTypes[i] = CONSONANT;
            if (isWeak(chars[i])) oldSoft[i] = SOFT; else oldSoft[i] = HARD;
        }
        int accentLoc = -1;
        for (int i = 0; i < chars.length; i++) {
            if (isAnAccent(chars[i])) accentLoc = i;
        }
        StringBuilder newWordB = new StringBuilder().append(chars[0]);
        int[] positions = new int[chars.length];
        int[] types = new int[chars.length];
        int[] soft = new int[chars.length];
        positions[0] = 0;
        int newLen = 1;
        for (int i = 1; i < chars.length; i++) {
            boolean skip = false;
            if (i == chars.length - 1 && oldTypes[i] == CONSONANT && oldTypes[i - 1] == CONSONANT) skip = true;
            if (chars[i] == 's' && chars[i - 1] == 'n' && i + 1 < chars.length && oldTypes[i + 1] == CONSONANT) skip = true;
            if (chars[i] == 's' && chars[i - 1] == 'b' && i + 1 < chars.length && oldTypes[i + 1] == CONSONANT) skip = true;
            if (chars[i] == 'r' || chars[i] == 'l') {
                if (chars[i - 1] == 'b' || chars[i - 1] == 'f' || chars[i - 1] == 'g' || chars[i - 1] == 'k' || chars[i - 1] == 'p' || chars[i - 1] == 't' || chars[i - 1] == 'c') skip = true;
            }
            if (chars[i] == 'h' || (chars[i] == 'l' && chars[i - 1] == 'l') || (chars[i] == 'u' && chars[i - 1] == 'q') || (chars[i] == 'u' && chars[i - 1] == 'g' && i + 1 < chars.length && (chars[i + 1] == 'e' || chars[i + 1] == 'i')) || (chars[i] == 'r' && (chars[i - 1] == 'r' || chars[i - 1] == 'd'))) skip = true;
            if (!skip) {
                newWordB.append(chars[i]);
                types[newLen] = oldTypes[i];
                soft[newLen] = oldSoft[i];
                positions[newLen] = i;
                newLen++;
            }
        }
        char[] newChars = newWordB.toString().toCharArray();
        int[] silables = new int[newChars.length];
        int countSilables = 0;
        int lastStart = 0;
        for (int i = 0; i < newLen; i++) {
            if (i + 1 >= newLen) {
                silables[countSilables] = lastStart;
                countSilables++;
                if (types[i] == VOWEL && i > 1 && types[i - 1] == VOWEL && types[i - 2] == VOWEL && soft[i] == HARD && soft[i - 1] == SOFT && soft[i - 2] == HARD) {
                    silables[countSilables] = i;
                    countSilables++;
                } else if (types[i] == VOWEL && soft[i] == HARD && i > 0 && types[i - 1] == VOWEL && soft[i - 1] == HARD) {
                    silables[countSilables] = i;
                    countSilables++;
                }
            } else if (lastStart != i) {
                if (types[i] == CONSONANT) {
                    silables[countSilables] = lastStart;
                    countSilables++;
                    if (types[i + 1] == CONSONANT) lastStart = i + 1; else lastStart = i;
                } else if (soft[i] == HARD && types[i - 1] == VOWEL && soft[i - 1] == HARD) {
                    silables[countSilables] = lastStart;
                    countSilables++;
                    lastStart = i;
                } else if (i > 1 && types[i - 1] == VOWEL && types[i - 2] == VOWEL && soft[i] == HARD && soft[i - 1] == SOFT && soft[i - 2] == HARD) {
                    silables[countSilables] = lastStart;
                    countSilables++;
                    lastStart = i;
                }
            }
        }
        silablesTotal = countSilables;
        oldSil = new int[countSilables];
        for (int i = 0; i < countSilables; i++) {
            oldSil[i] = positions[silables[i]];
        }
        if (accentLoc == -1) {
            char lastLetter = chars[chars.length - 1];
            int begSil = 0;
            int endSil = 0;
            if (countSilables > 1 && (isVowel(lastLetter) || lastLetter == 'n' || lastLetter == 's')) {
                begSil = silables[countSilables - 2];
                endSil = silables[countSilables - 1];
            } else {
                begSil = silables[countSilables - 1];
                endSil = chars.length;
            }
            for (int i = begSil; i < endSil; i++) {
                if (types[i] == VOWEL) {
                    if (i + 1 == endSil || types[i + 1] == CONSONANT) {
                        accentLoc = positions[i];
                    } else {
                        for (int j = i; j < endSil; j++) {
                            if (types[j] == VOWEL && soft[j] == HARD) {
                                accentLoc = positions[j];
                                j = endSil;
                            }
                        }
                        if (accentLoc == -1) {
                            for (int j = endSil - 1; j >= begSil; j--) {
                                if (types[j] == VOWEL) {
                                    accentLoc = positions[j];
                                    j = -1;
                                }
                            }
                        }
                    }
                    i = endSil;
                }
            }
        }
        location = accentLoc;
        StringBuilder rhymeB = new StringBuilder();
        StringBuilder asonanceB = new StringBuilder();
        if (accentLoc != -1) {
            for (int i = accentLoc; i < chars.length; i++) {
                if (chars[i] == '�') {
                    rhymeB.append('a');
                    asonanceB.append('a');
                } else if (chars[i] == '�') {
                    rhymeB.append('e');
                    asonanceB.append('e');
                } else if (chars[i] == '�') {
                    rhymeB.append('i');
                    asonanceB.append('i');
                } else if (chars[i] == '�') {
                    rhymeB.append('o');
                    asonanceB.append('o');
                } else if (chars[i] == '�') {
                    rhymeB.append('u');
                    asonanceB.append('u');
                } else if (chars[i] == 'v') {
                    rhymeB.append('b');
                } else if (chars[i] == 'y' && oldTypes[i] == VOWEL) {
                    rhymeB.append('i');
                    asonanceB.append('i');
                } else {
                    rhymeB.append(chars[i]);
                    if (oldTypes[i] == VOWEL) asonanceB.append(chars[i]);
                }
            }
        } else rhyme = word;
        rhyme = rhymeB.toString();
        asonance = asonanceB.toString();
        accentedSil = -1;
        for (int i = 0; i < oldSil.length; i++) {
            if (accentLoc >= oldSil[i]) accentedSil = i;
        }
    }

    public int getAccentType() {
        if (accentedSil < silablesTotal - 2) return ESDRU;
        if (accentedSil == silablesTotal - 1 && silablesTotal > 1) return AGUDA;
        return LLANA;
    }

    public static boolean isAnAccent(char c) {
        if (c == '�' || c == '�' || c == '�' || c == '�' || c == '�') return true;
        return false;
    }

    public static boolean isVowel(char c) {
        if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == '�' || c == '�' || c == '�' || c == '�' || c == '�' || c == '�') return true;
        return false;
    }

    public static boolean isVowelOrH(char c) {
        if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == '�' || c == '�' || c == '�' || c == '�' || c == '�' || c == '�' || c == 'h') return true;
        return false;
    }

    public static boolean isVowelNoAccent(char c) {
        if (c == 'a' || c == 'e' || c == 'i' || c == 'o' || c == 'u' || c == '�') return true;
        return false;
    }

    public static boolean isWeak(char c) {
        if (c == 'u' || c == 'i' || c == '�' || c == 'y') return true;
        return false;
    }

    public static void printBySilables(char[] chars, int[] silables, int countSilables) {
        int c = 0;
        for (int i = 0; i < chars.length; i++) {
            if (c < countSilables && silables[c] == i) {
                System.out.print('-');
                c++;
            }
            System.out.print(chars[i]);
        }
        System.out.println();
    }

    public String getRhyme() {
        return rhyme;
    }

    public String getAsonance() {
        return asonance;
    }

    public int getLocation() {
        return location;
    }

    public String getNormalized() {
        return normalize(word);
    }

    public String getNormalizedAsonance() {
        return normalize(asonance);
    }

    public String normalize(String word) {
        char[] chars = word.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '�') {
                chars[i] = 'a';
            } else if (chars[i] == '�') {
                chars[i] = 'e';
            } else if (chars[i] == '�') {
                chars[i] = 'i';
            } else if (chars[i] == '�') {
                chars[i] = 'o';
            } else if (chars[i] == '�') {
                chars[i] = 'u';
            } else if (chars[i] == '�') {
                chars[i] = '�';
            } else if (chars[i] == '�') {
                chars[i] = 'n';
            }
        }
        return new String(chars);
    }

    public static void main(String[] args) {
        System.out.println(new RhymeFinder("astr�logo").countSil());
    }
}
