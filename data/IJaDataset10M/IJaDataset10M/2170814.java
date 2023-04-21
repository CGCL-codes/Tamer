package biz.flammable.waft.types;

import java.io.Serializable;
import java.util.Comparator;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Pattern;

public class Password extends StringType {

    private static final Pattern PATTERN = Pattern.compile("[\\x20-\\x7e]{4,}");

    private static final int[] numbers = getNumbers();

    private static final int[] lowercase = getLowercaseChars();

    private static final int[] uppercase = getUppercaseChars();

    private static final int[] specials = getSpecialChars();

    private static final int[] ALL = getAll();

    private static final int TOTAL_NR = 95;

    private static int[] getAll() {
        int[] foo = new int[TOTAL_NR];
        int index = 0;
        for (int i : numbers) {
            foo[index++] = i;
        }
        for (int i : lowercase) {
            foo[index++] = i;
        }
        for (int i : uppercase) {
            foo[index++] = i;
        }
        for (int i : specials) {
            foo[index++] = i;
        }
        return foo;
    }

    static double getEntropy(char a, char b) {
        return getEntropy(a, b, TOTAL_NR);
    }

    static double getEntropy(char a, char b, int size) {
        int index_a = (int) a - 32;
        int index_b = (int) b - 32;
        if (index_a == index_b) {
            return 0;
        } else if (index_a > index_b) {
            int temp = index_a;
            index_a = index_b;
            index_b = temp;
        }
        int distance = index_b - index_a;
        int wrappedDistance = index_a + size - index_b;
        return (distance == wrappedDistance) ? 1 : (1.0 / Math.max(distance, wrappedDistance));
    }

    static int[] getNumbers() {
        int[] numbers = new int[10];
        for (int i = 0, k = 48; i < 10; ++i, ++k) {
            numbers[i] = k;
        }
        return numbers;
    }

    static int[] getSpecialChars() {
        int[] numbers = new int[33];
        for (int i = 0, k = 32; i < 16; ++i, ++k) {
            numbers[i] = k;
        }
        for (int i = 16, k = 58; i < 23; ++i, ++k) {
            numbers[i] = k;
        }
        for (int i = 23, k = 91; i < 29; ++i, ++k) {
            numbers[i] = k;
        }
        for (int i = 29, k = 123; i < 33; ++i, ++k) {
            numbers[i] = k;
        }
        return numbers;
    }

    static int[] getLowercaseChars() {
        int[] numbers = new int[26];
        for (int i = 0, k = 97; i < 26; ++i, ++k) {
            numbers[i] = k;
        }
        return numbers;
    }

    static int[] getUppercaseChars() {
        int[] numbers = new int[26];
        for (int i = 0, k = 65; i < 26; ++i, ++k) {
            numbers[i] = k;
        }
        return numbers;
    }

    public Password(String pwd) throws TypeValueException {
        super(pwd);
    }

    @Override
    public Pattern getPattern() {
        return PATTERN;
    }

    public static void main(String[] args) throws TypeValueException {
        generateTest();
    }

    private static class PwdComparator implements Comparator<String>, Serializable {

        private static final long serialVersionUID = 1;

        public int compare(String arg0, String arg1) {
            double d0 = getEntropyFor(arg0);
            double d1 = getEntropyFor(arg1);
            int score0 = getScoreFor(arg0);
            int score1 = getScoreFor(arg1);
            d0 = d0 * score0;
            d1 = d1 * score1;
            if (d0 < d1) {
                return 1;
            } else if (d0 > d1) {
                return -1;
            }
            return 0;
        }
    }

    private static void generateTest() throws TypeValueException {
        int count = 100;
        Set<String> passwords = new TreeSet<String>(new PwdComparator());
        passwords.add("(2x+3y)=9Z");
        passwords.add("aA123___");
        for (int i = 0; i < count; ++i) {
            passwords.add(generatePassWord(8));
        }
        System.out.println("Password:\tVerdict:\tScore:\t\tEntropy:\t\tCombined:");
        System.out.println("----------------------------------------------------------------------------------");
        int limit = 10;
        for (String pwd : passwords) {
            new Password(pwd);
            String spaces = "";
            double entropy = getEntropyFor(pwd);
            int foo = 8 - pwd.length();
            if (foo > 0) {
                for (int i = 0; i < foo; ++i) {
                    spaces += " ";
                }
            }
            int score = getScoreFor(pwd);
            System.out.println(pwd + spaces + "\t" + getVerdict(score) + "\t\t" + score + "\t" + entropy + "\t" + (entropy * score));
            --limit;
            if (limit == 0) break;
        }
    }

    private static String getVerdict(int score) {
        if (score >= 85) return "Perfect";
        if (score >= 80) return "Secure";
        if (score >= 75) return "Strong";
        if (score >= 60) return "Hard";
        if (score >= 45) return "Average";
        if (score >= 25) return "Weak";
        if (score >= 0) return "Fainted";
        return "FAILED";
    }

    private static int getScoreFor(String pwd) {
        int score = 0;
        final int length = pwd.length();
        int nUpperCount = pwd.replaceAll("[^A-Z]", "").length();
        int nLowerCount = pwd.replaceAll("[^a-z]", "").length();
        int nLowerUpperCount = nUpperCount + nLowerCount;
        int nNumberCount = pwd.replaceAll("[^0-9]", "").length();
        int characterCount = length - nLowerUpperCount - nNumberCount;
        if (length < 4) {
            score += 5;
        } else if (length < 8) {
            score += 10;
        } else {
            score += 25;
        }
        if (nLowerCount == 0 && nUpperCount > 0) {
            score += 10;
        } else if (nLowerCount > 0 && nUpperCount == 0) {
            score += 10;
        } else if (nLowerUpperCount > 0) {
            score += 20;
        }
        if (nNumberCount == 1) {
            score += 10;
        } else if (nNumberCount > 2) {
            score += 15;
        }
        if (characterCount == 1) {
            score += 15;
        } else if (characterCount > 1) {
            score += 25;
        } else if (characterCount < 0) {
            System.err.println("whatta hell?" + characterCount + " for " + pwd);
        }
        return score;
    }

    private static Double getEntropyFor(String arg0) {
        double entropy = 0.0;
        double cumulative = 0.0;
        for (int i = 0; i < arg0.length(); ++i) {
            for (int j = i + 1; j < arg0.length(); ++j) {
                char a = arg0.charAt(i);
                char b = arg0.charAt(j);
                cumulative += getEntropy(a, b);
            }
            if (i < arg0.length() - 1) {
                char a = arg0.charAt(i);
                char b = arg0.charAt(i + 1);
                entropy += getEntropy(a, b);
            }
        }
        return (entropy + cumulative) / 2.0;
    }

    static Random random = new Random();

    private static String generatePassWord(final int length) {
        StringBuffer sb = new StringBuffer();
        int type = random.nextInt(3);
        for (int i = 0; i < length; ++i) {
            int c = -1;
            switch(type) {
                case 0:
                    c = ALL[random.nextInt(TOTAL_NR)];
                    break;
                case 1:
                    int[] foo = random.nextBoolean() ? lowercase : uppercase;
                    c = foo[random.nextInt(foo.length)];
                    break;
                case 2:
                    c = numbers[random.nextInt(10)];
                    break;
                default:
                    throw new AssertionError();
            }
            sb.append((char) c);
        }
        return sb.toString();
    }
}
