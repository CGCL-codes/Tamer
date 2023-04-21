package org.wltea.analyzer.seg;

import org.wltea.analyzer.Context;
import org.wltea.analyzer.Lexeme;
import org.wltea.analyzer.help.CharacterHelper;

public class DefaultSegmenter implements ISegmenter {

    private static int FLAG_SEPRATOR = 0x00000001;

    private static int FLAG_CJK = 0x00000010;

    private static int FLAG_LETTER = 0x00000100;

    private static int FLAG_NUM = 0x00001000;

    private static int FLAG_CONNECTOR = 0x00010000;

    private static int FLAG_WHITE = 0x00100000;

    private int start;

    private int end;

    private int letterStart;

    private int letterEnd;

    private int numberStart;

    private int numberEnd;

    private char[] segmentBuff;

    private final DictMatchHandler dictMatchHandler = new DictMatchHandler();

    private int charFlag;

    private int hisCharFlag;

    public DefaultSegmenter() {
        start = -1;
        end = -1;
        letterStart = -1;
        letterEnd = -1;
        numberStart = -1;
        numberEnd = -1;
    }

    @Override
    public void nextLexeme(char[] segmentBuff, Context context) {
        this.segmentBuff = segmentBuff;
        char input = segmentBuff[context.getCursor()];
        boolean bufferLockFlag = this.process(input, context);
        if (bufferLockFlag) {
            context.lockBuffer(this);
        } else {
            context.unlockBuffer(this);
        }
    }

    private boolean isAcceptedNotCJKChar() {
        return charFlag != FLAG_SEPRATOR && charFlag != FLAG_WHITE && charFlag != FLAG_CJK;
    }

    private void processCJK(char[] segmentBuff, Context context) {
        if (charFlag == FLAG_CJK) {
            dictMatchHandler.handleCJK(segmentBuff, context);
        }
    }

    private void processLetter(char[] segmentBuff, Context context) {
        if (charFlag == FLAG_LETTER) {
            if (letterStart < 0) {
                letterStart = context.getCursor();
                letterEnd = letterStart;
            } else {
                letterEnd = context.getCursor();
            }
        } else if (letterEnd >= 0) {
            if (isAcceptedNotCJKChar() || start < letterStart) {
                dictMatchHandler.handleMix(segmentBuff, letterStart, letterEnd - letterStart + 1, context, Lexeme.TYPE_LETTER, true);
            }
            letterStart = -1;
            letterEnd = -1;
        }
    }

    private void processNum(char[] segmentBuff, Context context) {
        if (charFlag == FLAG_NUM) {
            if (numberStart < 0) {
                numberStart = context.getCursor();
                numberEnd = numberStart;
            } else {
                numberEnd = context.getCursor();
            }
        } else if (numberEnd >= 0) {
            if (isAcceptedNotCJKChar() || start < numberStart) {
                dictMatchHandler.handleMix(segmentBuff, numberStart, numberEnd - numberStart + 1, context, Lexeme.TYPE_NUM, true);
            }
            numberStart = -1;
            numberEnd = -1;
        }
    }

    private int getMixLexemeType() {
        if (hisCharFlag == FLAG_CONNECTOR) {
            return Lexeme.TYPE_CONNECTOR;
        }
        if ((hisCharFlag & FLAG_CONNECTOR) > 0) {
            return Lexeme.TYPE_CONNECTOR_MIX;
        } else if ((hisCharFlag & FLAG_NUM) > 0 && (hisCharFlag & FLAG_LETTER) > 0) {
            return Lexeme.TYPE_LETTER_MIX;
        } else if ((hisCharFlag & FLAG_LETTER) > 0) {
            return Lexeme.TYPE_LETTER;
        } else if ((hisCharFlag & FLAG_NUM) > 0) {
            return Lexeme.TYPE_NUM;
        }
        throw new RuntimeException("invalid hisCharFlag:" + hisCharFlag);
    }

    private void processMix(char[] segmentBuff, Context context) {
        if (isAcceptedNotCJKChar()) {
            if (start == -1) {
                start = context.getCursor();
                end = start;
            } else {
                end = context.getCursor();
                if (context.getCursor() < context.getAvailable() - 2) {
                    if (CharacterHelper.isLetterConnector(segmentBuff[context.getCursor() + 1])) {
                        dictMatchHandler.handleMixDictMatch(segmentBuff, start, end - start + 1, context, getMixLexemeType());
                    }
                }
            }
        } else if (start >= 0) {
            dictMatchHandler.handleMix(segmentBuff, start, end - start + 1, context, getMixLexemeType(), false);
            start = -1;
            end = -1;
        }
    }

    private void processSeprator(char[] segmentBuff, Context context) {
        if (charFlag == FLAG_SEPRATOR) {
            dictMatchHandler.handleSeprator(segmentBuff, context);
        }
    }

    private void processLastChar(char[] segmentBuff, Context context) {
        if (context.getCursor() == context.getAvailable() - 1) {
            if (start != -1 && end != -1) {
                if (start < letterStart) {
                    dictMatchHandler.handleMix(segmentBuff, letterStart, letterEnd - letterStart + 1, context, Lexeme.TYPE_LETTER, true);
                }
                if (start < numberStart) {
                    dictMatchHandler.handleMix(segmentBuff, numberStart, numberEnd - numberStart + 1, context, Lexeme.TYPE_LETTER, true);
                }
                dictMatchHandler.handleMix(segmentBuff, start, end - start + 1, context, getMixLexemeType(), false);
            }
            dictMatchHandler.handleSeprator(segmentBuff, context);
            start = -1;
            end = -1;
            numberStart = -1;
            numberEnd = -1;
            letterStart = -1;
            letterEnd = -1;
        }
    }

    /**
	 * @param input
	 * @param context
	 * @return
	 */
    private boolean process(char input, Context context) {
        boolean needLock = false;
        if (CharacterHelper.isEnglishLetter(input)) {
            charFlag = FLAG_LETTER;
            hisCharFlag |= charFlag;
        } else if (CharacterHelper.isCJKCharacter(input)) {
            charFlag = FLAG_CJK;
        } else if (CharacterHelper.isArabicNumber(input)) {
            charFlag = FLAG_NUM;
            hisCharFlag |= charFlag;
        } else if (CharacterHelper.isLetterConnector(input)) {
            if (input == '.') {
                int pos = context.getCursor();
                if (pos >= context.getAvailable() - 1 || (!CharacterHelper.isEnglishLetter(context.getChar(pos + 1)) && !CharacterHelper.isArabicNumber(context.getChar(pos + 1)))) {
                    charFlag = FLAG_SEPRATOR;
                } else {
                    charFlag = FLAG_CONNECTOR;
                    hisCharFlag |= charFlag;
                }
            } else {
                charFlag = FLAG_CONNECTOR;
                hisCharFlag |= charFlag;
            }
        } else if (input == ' ') {
            charFlag = FLAG_WHITE;
        } else {
            charFlag = FLAG_SEPRATOR;
        }
        processLetter(segmentBuff, context);
        processNum(segmentBuff, context);
        processMix(segmentBuff, context);
        processCJK(segmentBuff, context);
        processSeprator(segmentBuff, context);
        processLastChar(segmentBuff, context);
        if (charFlag == FLAG_CJK || charFlag == FLAG_WHITE || charFlag == FLAG_SEPRATOR) {
            hisCharFlag = 0;
        }
        if (start == -1 && end == -1 && letterStart == -1 && letterEnd == -1 && numberStart == -1 && numberEnd == -1) {
            needLock = false;
        } else {
            needLock = true;
        }
        if (!needLock) {
            needLock = dictMatchHandler.needLocked();
        }
        return needLock;
    }

    @Override
    public void reset() {
        start = -1;
        end = -1;
        letterStart = -1;
        letterEnd = -1;
        numberStart = -1;
        numberEnd = -1;
        dictMatchHandler.reset();
    }
}
