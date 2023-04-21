package com.ericdaugherty.mail.server.configuration;

import com.ericdaugherty.mail.server.auth.Saslprep;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Andreas Kyrmegalos
 */
public class PasswordStrengthChecker {

    static final int[] defaultLimits = new int[] { 3, 4, 3, 3, 2, 2, 10 };

    private static final int[] lowercase = new int[] { 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108, 109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122 };

    private static final int[] uppercase = new int[] { 65, 66, 67, 68, 69, 70, 71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90 };

    private static final int[] numeric = new int[] { 48, 49, 50, 51, 52, 53, 54, 55, 56, 57 };

    private static final int[][] qwerty = new int[][] { { 113, 119, 101, 114, 116, 121, 117, 105, 111, 112, 91, 93, 92 }, { 81, 87, 69, 82, 84, 89, 85, 73, 79, 80, 123, 125, 124 }, { 97, 115, 100, 102, 103, 104, 106, 107, 108, 59, 39 }, { 65, 83, 68, 70, 71, 72, 74, 75, 76, 58, 34 }, { 122, 120, 99, 118, 98, 110, 109, 44, 46, 47 }, { 90, 88, 67, 86, 66, 78, 77, 60, 62, 63 }, { 96, 49, 50, 51, 52, 53, 54, 55, 56, 57, 48, 45, 61 }, { 126, 33, 64, 35, 36, 37, 94, 38, 42, 40, 41, 95, 43 } };

    static Map<String, String> checkStrength(String text) {
        return checkStrength(text, defaultLimits);
    }

    private static Map<String, String> checkStrength(String text, int[] limits) {
        Map<String, String> results = new HashMap<String, String>();
        results.put("success", String.valueOf(containsIllegalCharacters(text) == null && hasConsecutiveNumericCount(text, limits[0]) == null && hasConsecutiveAlphaCount(text, limits[1]) == null && hasQWERTYDigits(text, limits[2]) == null && hasLowEntropyDigits(text, limits[3]) == null && (hasUpperCase(text, limits[4]) || hasNonASCIIAlphaNumeric(text, limits[5])) && hasLeastNumberOfCharacters(text, limits[6])));
        results.put("illegalChar", containsIllegalCharacters(text));
        results.put("consecutiveNumeric", hasConsecutiveNumericCount(text, limits[0]));
        results.put("consecutiveAlpha", hasConsecutiveAlphaCount(text, limits[1]));
        results.put("QWERTYDigits", hasQWERTYDigits(text, limits[2]));
        results.put("lowEntropy", hasLowEntropyDigits(text, limits[3]));
        results.put("upperCase", hasUpperCase(text, limits[4]) ? "" : null);
        results.put("nonASCIIAlphaNumeric", hasNonASCIIAlphaNumeric(text, limits[5]) ? "" : null);
        results.put("leastNumber", hasLeastNumberOfCharacters(text, limits[6]) ? "" : null);
        return results;
    }

    private static String containsIllegalCharacters(String text) {
        char character;
        for (int i = 0; i < text.length(); i++) {
            character = text.charAt(i);
            if (containsIllegalCharacter(Saslprep.c12Entries, character)) {
                return text.substring(i, i + 1);
            }
            if (containsIllegalCharacter(Saslprep.c21Entries, character)) {
                return text.substring(i, i + 1);
            }
            if (containsIllegalCharacter(Saslprep.c22Entries, character)) {
                return text.substring(i, i + 1);
            }
        }
        return null;
    }

    private static boolean containsIllegalCharacter(char[] map, char entry) {
        for (char c : map) {
            if (c == entry) return true;
        }
        return false;
    }

    private static String hasQWERTYDigits(String text, int count) {
        int[] sequence;
        for (int i = 0; i < qwerty.length; i++) {
            sequence = blockSequenceChecker(text, qwerty[i], count);
            if (sequence != null) return new String(sequence, 0, sequence.length);
        }
        return null;
    }

    private static String hasConsecutiveAlphaCount(String text, int count) {
        int[] sequence;
        sequence = blockPresenceChecker(text, lowercase, count);
        if (sequence != null) return new String(sequence, 0, sequence.length);
        sequence = blockPresenceChecker(text, uppercase, count);
        if (sequence != null) return new String(sequence, 0, sequence.length);
        return null;
    }

    private static String hasConsecutiveNumericCount(String text, int count) {
        int[] sequence;
        sequence = blockPresenceChecker(text, numeric, count);
        if (sequence != null) return new String(sequence, 0, sequence.length);
        return null;
    }

    private static String hasLowEntropyDigits(String text, int count) {
        int[] sequenceR = new int[count], sequenceS = new int[count];
        int repeatCount = 0, successCount = 0, codePoint, previousCodePoint;
        previousCodePoint = text.codePointAt(0);
        for (int i = 1; i < text.length(); i++) {
            codePoint = text.codePointAt(i);
            if (text.codePointAt(i - 1) > 0xffff && codePoint <= 0xffff) {
                continue;
            }
            if (codePoint == previousCodePoint) {
                sequenceR[repeatCount] = codePoint;
                repeatCount++;
            } else {
                sequenceR = new int[count];
                repeatCount = 0;
            }
            if (repeatCount == count - 1) {
                return new String(sequenceR, 0, sequenceR.length);
            }
            if ((int) Math.abs(codePoint - previousCodePoint) == 1) {
                sequenceS[repeatCount] = codePoint;
                successCount++;
            } else {
                sequenceS = new int[count];
                successCount = 0;
            }
            if (successCount == count - 1) {
                return new String(sequenceS, 0, sequenceS.length);
            }
            previousCodePoint = codePoint;
        }
        return null;
    }

    private static boolean hasUpperCase(String text, int count) {
        int repeatCount = 0, codePoint;
        for (int i = 0; i < text.length(); i++) {
            codePoint = text.codePointAt(i);
            if (i > 0 && text.codePointAt(i - 1) > 0xffff && codePoint <= 0xffff) {
                continue;
            }
            if (containsDigit(uppercase, codePoint) != -1) {
                repeatCount++;
            }
            if (repeatCount == count) {
                return true;
            }
        }
        return false;
    }

    private static boolean hasNonASCIIAlphaNumeric(String text, int count) {
        int repeatCount = 0, codePoint;
        boolean check = true;
        for (int i = 0; i < text.length(); i++) {
            codePoint = text.codePointAt(i);
            if (i > 0 && text.codePointAt(i - 1) > 0xffff && codePoint <= 0xffff) {
                continue;
            }
            if (containsDigit(uppercase, text.codePointAt(i)) != -1) {
                check = false;
            }
            if (containsDigit(lowercase, text.codePointAt(i)) != -1) {
                check = false;
            }
            if (containsDigit(numeric, text.codePointAt(i)) != -1) {
                check = false;
            }
            if (check) {
                repeatCount++;
            }
            if (repeatCount == count) {
                return true;
            }
            check = true;
        }
        return false;
    }

    private static boolean hasLeastNumberOfCharacters(String text, int count) {
        return text.codePointCount(0, text.length()) >= count;
    }

    private static int[] blockSequenceChecker(String text, int[] block, int count) {
        int length = text.length();
        int[] sequence = null;
        int digit, codePoint, sequenceCount = 0;
        for (int j = 0; j < length; j++) {
            codePoint = text.codePointAt(j);
            if (j > 0 && text.codePointAt(j - 1) > 0xffff && codePoint <= 0xffff) continue;
            digit = containsDigit(block, codePoint);
            if (digit == -1) {
                sequence = null;
                sequenceCount = 0;
            } else {
                if (sequence == null) {
                    sequence = new int[count];
                    sequence[sequenceCount++] = digit;
                } else {
                    sequence[sequenceCount++] = digit;
                    if (sequenceCount == count) {
                        if (detectSequence(block, sequence)) {
                            return sequence;
                        }
                        sequence = null;
                        sequenceCount = 0;
                    }
                }
            }
        }
        return null;
    }

    private static boolean detectSequence(int[] block, int[] sequence) {
        int pos = positionDigit(block, sequence[0]);
        boolean detected = true;
        if (block.length - pos >= sequence.length) {
            for (int i = 0; i < sequence.length; i++) {
                if (sequence[i] != block[pos + i]) {
                    detected = false;
                }
            }
        } else {
            detected = false;
        }
        if (detected) return true;
        detected = true;
        if (pos >= sequence.length - 1) {
            for (int i = 0; i < sequence.length; i++) {
                if (sequence[i] != block[pos--]) {
                    detected = false;
                }
            }
        } else {
            detected = false;
        }
        return detected;
    }

    private static int[] blockPresenceChecker(String text, int[] block, int count) {
        int length = text.length();
        int[] sequence = null;
        int digit, codePoint, sequenceCount = 0;
        for (int j = 0; j < length; j++) {
            codePoint = text.codePointAt(j);
            if (j > 0 && text.codePointAt(j - 1) > 0xffff && codePoint <= 0xffff) continue;
            digit = containsDigit(block, codePoint);
            if (digit == -1) {
                sequence = null;
                sequenceCount = 0;
            } else {
                if (sequence == null) {
                    sequence = new int[count];
                    sequence[sequenceCount++] = digit;
                } else {
                    sequence[sequenceCount++] = digit;
                    if (sequenceCount == count) {
                        return sequence;
                    }
                }
            }
        }
        return null;
    }

    private static int containsDigit(int[] range, int codePoint) {
        for (int i = 0; i < range.length; i++) {
            if (range[i] == codePoint) return range[i];
        }
        return -1;
    }

    private static int positionDigit(int[] range, int codePoint) {
        for (int i = 0; i < range.length; i++) {
            if (range[i] == codePoint) return i;
        }
        return -1;
    }
}
