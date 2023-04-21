package org.asianclassics.unicoder;

import java.util.ArrayList;

/** A list of non-empty list of {@link TStackListList
 *  TStackListLists} representing all the ways you could break up a
 *  tsheg bar of ACIP into stacks (i.e., grapheme clusters).
 *
 *  @author David Chandler */
class TParseTree {

    /** a fast, non-thread-safe, random-access list implementation: */
    private ArrayList al = new ArrayList();

    /** Creates an empty list. */
    public TParseTree() {
    }

    /** Returns the ith list of stack lists in this parse tree. */
    public TStackListList get(int i) {
        return (TStackListList) al.get(i);
    }

    /** Adds p to the end of this list. */
    public void add(TStackListList p) throws IllegalArgumentException {
        if (p.isEmpty()) throw new IllegalArgumentException("p is empty");
        al.add(p);
    }

    /** Returns the number of TStackListLists in this list.  See
     * also {@link #numberOfParses()}, which gives a different
     * interpretation of the size of this tree. */
    public int size() {
        return al.size();
    }

    /** Returns the number of different parses one could make from
     *  this parse tree.  Returns zero if this list is empty. */
    public int numberOfParses() {
        if (al.isEmpty()) return 0;
        int k = 1;
        int sz = size();
        for (int i = 0; i < sz; i++) {
            k *= get(i).size();
        }
        return k;
    }

    /** Returns the number of {@link TPair pairs} that are in a
     *  parse of this tree. */
    public int numberOfPairs() {
        if (al.isEmpty()) return 0;
        int k = 1;
        int sz = size();
        for (int i = 0; i < sz; i++) {
            k += get(i).get(0).size();
        }
        return k;
    }

    /** Returns an iterator that will iterate over the {@link
     *  #numberOfParses} available. */
    public ParseIterator getParseIterator() {
        return new ParseIterator(al);
    }

    /** Returns a list containing the legal parses of this parse tree.
     *  By &quot;legal&quot;, we mean a sequence of stacks that is
     *  legal by the rules of Tibetan tsheg bar syntax (sometimes
     *  called spelling).  This will return the {G-YA} parse of {GYA}
     *  as well as the {GYA} parse, so watch yourself. */
    public TStackListList getLegalParses() {
        TStackListList sll = new TStackListList(2);
        ParseIterator pi = getParseIterator();
        while (pi.hasNext()) {
            TStackList sl = pi.next();
            if (sl.isLegalTshegBar(false).isLegal) {
                sll.add(sl);
            }
        }
        return sll;
    }

    /** Returns a list (never null) containing the parses of this
     *  parse tree that are not clearly illegal. */
    public TStackListList getNonIllegalParses() {
        TStackListList sll = new TStackListList(2);
        ParseIterator pi = getParseIterator();
        while (pi.hasNext()) {
            TStackList sl = pi.next();
            BoolTriple bt = sl.isLegalTshegBar(false);
            if (!sl.isClearlyIllegal(bt.candidateType)) {
                sll.add(sl);
            }
        }
        return sll;
    }

    private static final boolean debug = false;

    /** Returns the best parse, if there is a unique parse that is
     *  clearly preferred to other parses.  Basically, if there's a
     *  unique legal parse, you get it.  If there's not, but there is
     *  a unique non-illegal parse, you get it.  If there's not a
     *  unique answer, null is returned. */
    public TStackList getBestParse() {
        if (debug) System.out.println("getBestParse: parse tree is " + toString());
        TStackListList up = getUniqueParse(false);
        if (up.size() == 1) {
            if (debug) System.out.println("getBestParse: unique parse");
            return up.get(0);
        }
        up = getNonIllegalParses();
        int sz = up.size();
        if (sz == 1) {
            if (debug) System.out.println("getBestParse: sole non-illegal parse");
            return up.get(0);
        } else if (sz > 1) {
            ArrayList candidates = new ArrayList(sz);
            for (int i = 0; i < sz; i++) candidates.add(new Integer(i));
            boolean keepGoing = true;
            int stackNumber = 0;
            boolean someoneHasThisStack = true;
            while (someoneHasThisStack && candidates.size() > 1) {
                someoneHasThisStack = false;
                int maxGlyphsInThisStack = 0;
                for (int k = 0; k < candidates.size(); k++) {
                    TStackList sl = up.get(((Integer) candidates.get(k)).intValue());
                    if (sl.size() > stackNumber) {
                        int ng;
                        if ((ng = sl.get(stackNumber).size()) > maxGlyphsInThisStack) maxGlyphsInThisStack = ng;
                        someoneHasThisStack = true;
                    }
                }
                if (someoneHasThisStack) {
                    for (int k = 0; k < candidates.size(); k++) {
                        TStackList sl = up.get(((Integer) candidates.get(k)).intValue());
                        if (sl.size() > stackNumber) {
                            if (sl.get(stackNumber).size() != maxGlyphsInThisStack) candidates.remove(k--);
                        } else throw new Error("impossible!");
                    }
                }
                ++stackNumber;
            }
            if (candidates.size() == 1) {
                if (debug) System.out.println("getBestParse: one candidate");
                return up.get(((Integer) candidates.get(0)).intValue());
            } else {
                if (debug) {
                    System.out.println("getBestParse: no parse, num candidates=" + candidates.size());
                    for (int i = 0; i < candidates.size(); i++) {
                        System.out.println("candidate " + i + " is " + up.get(((Integer) candidates.get(i)).intValue()));
                        if (i + 1 < candidates.size()) {
                            boolean eq = (up.get(((Integer) candidates.get(i)).intValue()).equals(up.get(((Integer) candidates.get(i + 1)).intValue())));
                            System.out.println("This candidate and the next are" + (eq ? "" : " not") + " equal.");
                        }
                    }
                }
                return null;
            }
        }
        if (debug) System.out.println("getBestParse: no non-illegal parses");
        return null;
    }

    /** Returns a list containing the unique legal parse of this parse
     *  tree if there is a unique legal parse.  Returns an empty list
     *  if there are no legal parses.  Returns a list containing all
     *  legal parses if there two or more equally good parses.  By
     *  &quot;legal&quot;, we mean a sequence of stacks that is legal
     *  by the rules of Tibetan tsheg bar syntax (sometimes called
     *  spelling).
     *  @param noPrefixTests true if you want to pretend that every
     *  stack can take every prefix, which is not the case in
     *  reality */
    public TStackListList getUniqueParse(boolean noPrefixTests) {
        TStackListList allNonillegalParses = new TStackListList(2);
        TStackListList allStrictlyLegalParses = new TStackListList(2);
        TStackListList legalParsesWithVowelOnRoot = new TStackListList(1);
        ParseIterator pi = getParseIterator();
        while (pi.hasNext()) {
            TStackList sl = pi.next();
            BoolTriple bt = sl.isLegalTshegBar(noPrefixTests);
            if (bt.isLegal) {
                if (bt.isLegalAndHasAVowelOnRoot) legalParsesWithVowelOnRoot.add(sl);
                if (!bt.isLegalButSanskrit()) allStrictlyLegalParses.add(sl);
                allNonillegalParses.add(sl);
            }
        }
        if (legalParsesWithVowelOnRoot.size() == 1) return legalParsesWithVowelOnRoot; else {
            if (allStrictlyLegalParses.size() == 1) return allStrictlyLegalParses;
            if (allStrictlyLegalParses.size() > 2) throw new Error("can this happen?");
            if (legalParsesWithVowelOnRoot.size() == 2) {
                if (legalParsesWithVowelOnRoot.get(0).size() != 1 + legalParsesWithVowelOnRoot.get(1).size()) {
                    return new TStackListList();
                } else {
                    return new TStackListList(legalParsesWithVowelOnRoot.get(1));
                }
            }
            if (allNonillegalParses.size() == 2) {
                if (allNonillegalParses.get(0).size() != 1 + allNonillegalParses.get(1).size()) {
                    return new TStackListList();
                }
                return new TStackListList(allNonillegalParses.get(1));
            }
            return allNonillegalParses;
        }
    }

    /** Returns a human-readable representation. */
    public String toString() {
        return al.toString();
    }

    /** Returns true if and only if either x is a TParseTree
     *  object representing the same TPairLists in the same order
     *  or x is a String that is equals to the result of {@link
     *  #toString()}. */
    public boolean equals(Object x) {
        if (x instanceof TParseTree) {
            return al.equals(((TParseTree) x).al);
        } else if (x instanceof String) {
            return toString().equals(x);
        }
        return false;
    }

    /** Returns null if this parse tree is perfectly legal and valid.
     *  Returns a warning for users otherwise.  If and only if
     *  warningLevel is "All", then even unambiguous ACIP like PADMA,
     *  which could be improved by being written as PAD+MA, will cause
     *  a warning.
     *  @param warningLevel "All" if you're paranoid, "Most" to see
     *  warnings about lacking vowels on final stacks, "Some" to see
     *  warnings about lacking vowels on non-final stacks and also
     *  warnings about when prefix rules affect you, "None" if you
     *  like to see IllegalArgumentExceptions thrown.  (Actually, this
     *  refers only to the default values -- the level at which any
     *  particular warning appears is customizable.)
     *  @param pl the pair list from which this parse tree originated
     *  @param originalACIP the original ACIP, or null if you want
     *  this parse tree to make a best guess.
     *  @param shortMessages true iff you want short error and warning
     *  messages */
    public String getWarning(String warningLevel, TPairList pl, String originalACIP, boolean shortMessages, TTraits traits) {
        if (!ErrorsAndWarnings.warningLevelIsKnown(warningLevel)) throw new IllegalArgumentException("warning level bad: is it interned?");
        TStackList bestParse = getBestParse();
        {
            TStackListList noPrefixTestsUniqueParse = getUniqueParse(true);
            if (noPrefixTestsUniqueParse.size() == 1 && !noPrefixTestsUniqueParse.get(0).equals(bestParse)) {
                if (ErrorsAndWarnings.isEnabled(501, warningLevel)) if (shortMessages) return "501: Using " + bestParse + ", not " + noPrefixTestsUniqueParse.get(0); else return "501: Using " + bestParse + ((null != originalACIP) ? (" for the " + traits.shortTranslitName() + " {" + originalACIP + "}") : "") + ", but only because the tool's knowledge of prefix rules (see the documentation) says that " + noPrefixTestsUniqueParse.get(0) + " is not a legal Tibetan tsheg bar (\"syllable\")";
            }
        }
        String translit = (null != originalACIP) ? originalACIP : recoverTranslit();
        TStackListList up = getUniqueParse(false);
        if (null == up || up.size() != 1) {
            boolean isLastStack[] = new boolean[1];
            TStackListList nip = getNonIllegalParses();
            if (nip.size() != 1) {
                if (null == bestParse) {
                    if (ErrorsAndWarnings.isEnabled(101, warningLevel)) return ErrorsAndWarnings.getMessage(101, shortMessages, translit, traits);
                } else {
                    if (bestParse.hasStackWithoutVowel(traits.isACIP(), pl, isLastStack)) {
                        if (isLastStack[0]) {
                            if (ErrorsAndWarnings.isEnabled(502, warningLevel)) return ErrorsAndWarnings.getMessage(502, shortMessages, translit, traits);
                        } else {
                            if (traits.isACIP()) throw new Error("Can't happen now that we stack greedily");
                        }
                    }
                    if (ErrorsAndWarnings.isEnabled(503, warningLevel)) return ErrorsAndWarnings.getMessage(503, shortMessages, translit, traits);
                }
            } else {
                if (nip.get(0).hasStackWithoutVowel(traits.isACIP(), pl, isLastStack)) {
                    if (isLastStack[0]) {
                        if (ErrorsAndWarnings.isEnabled(502, warningLevel)) return ErrorsAndWarnings.getMessage(502, shortMessages, translit, traits);
                    } else {
                        if (traits.isACIP()) throw new Error("Can't happen now that we stack greedily [2]");
                    }
                }
            }
        }
        {
            int plnum = 0;
            while (plnum < pl.size() && pl.get(plnum).isDisambiguator()) {
                ++plnum;
                if (ErrorsAndWarnings.isEnabled(505, warningLevel)) return ErrorsAndWarnings.getMessage(505, shortMessages, translit, traits);
            }
            plnum = 0;
            for (int stackNum = 0; stackNum < bestParse.size(); stackNum++) {
                TPairList stack = bestParse.get(stackNum);
                int type = 0;
                int stackSize = stack.size();
                boolean hasAmbiguousConsonant = false;
                boolean highPriority507 = false;
                for (int j = 0; j < stackSize; j++) {
                    TPair tp = pl.get(plnum++);
                    if (j + 1 < stack.size()) {
                        if (null == tp.getRight()) {
                            if (type == 0) type = -1; else if (type == 1) if (ErrorsAndWarnings.isEnabled(506, warningLevel)) return ErrorsAndWarnings.getMessage(506, shortMessages, translit, traits);
                        } else {
                            if (type == 0) type = 1; else if (type == -1) if (ErrorsAndWarnings.isEnabled(506, warningLevel)) return ErrorsAndWarnings.getMessage(506, shortMessages, translit, traits);
                        }
                    }
                    if (stackSize > 1 && tp.getLeft() != null && tp.getLeft().length() > 1) {
                        if (null != originalACIP && (originalACIP.startsWith("NNY") || originalACIP.startsWith("NGH") || originalACIP.startsWith("GHNY") || originalACIP.startsWith("TNY") || originalACIP.startsWith("TSN") || originalACIP.startsWith("HNY") || originalACIP.startsWith("TSM") || originalACIP.startsWith("TSY") || originalACIP.startsWith("TSR") || originalACIP.startsWith("NTS") || originalACIP.startsWith("TSTH") || originalACIP.startsWith("TSV") || originalACIP.startsWith("RTS"))) {
                            highPriority507 = true;
                        }
                        hasAmbiguousConsonant = true;
                    }
                }
                if (hasAmbiguousConsonant && -1 == type) {
                    int warningNum = (highPriority507) ? 512 : 507;
                    if (ErrorsAndWarnings.isEnabled(warningNum, warningLevel)) return ErrorsAndWarnings.getMessage(warningNum, shortMessages, translit, traits);
                }
                while (plnum < pl.size() && pl.get(plnum).isDisambiguator()) {
                    ++plnum;
                    if (ErrorsAndWarnings.isEnabled(505, warningLevel)) return ErrorsAndWarnings.getMessage(505, shortMessages, translit, traits);
                }
            }
        }
        if (pl.size() >= 3) {
            String left, middle, right;
            left = pl.get(0).getLeft();
            middle = pl.get(1).getLeft();
            right = pl.get(2).getLeft();
            if (pl.get(0).getRight() == null && !pl.get(1).endsStack() && pl.get(2).endsStack() && null != left && null != right) {
                if (("D".equals(left) && "G".equals(middle) && "R".equals(right)) || ("D".equals(left) && "G".equals(middle) && "Y".equals(right))) {
                    if (pl.size() == 3) {
                        if (ErrorsAndWarnings.isEnabled(508, warningLevel)) return ErrorsAndWarnings.getMessage(508, shortMessages, translit, traits);
                    } else {
                        if (ErrorsAndWarnings.isEnabled(509, warningLevel)) return ErrorsAndWarnings.getMessage(509, shortMessages, translit, traits);
                    }
                }
            }
        }
        if (pl.size() >= 2) {
            String left, right;
            left = pl.get(0).getLeft();
            right = pl.get(1).getLeft();
            if (pl.get(0).getRight() == null && pl.get(1).endsStack() && null != left && null != right) {
                if (("D".equals(left) && "B".equals(right)) || ("B".equals(left) && "D".equals(right)) || ("D".equals(left) && "G".equals(right)) || ("D".equals(left) && "M".equals(right)) || ("G".equals(left) && "D".equals(right)) || ("D".equals(left) && "N".equals(right)) || ("M".equals(left) && "N".equals(right))) {
                    if (pl.size() == 2) {
                        if (ErrorsAndWarnings.isEnabled(508, warningLevel)) return ErrorsAndWarnings.getMessage(508, shortMessages, translit, traits);
                    } else {
                        if (ErrorsAndWarnings.isEnabled(509, warningLevel)) return ErrorsAndWarnings.getMessage(509, shortMessages, translit, traits);
                    }
                }
            }
        }
        return null;
    }

    /** Returns something akin to the transliteration that was input
     *  (okay, maybe 1-2-3-4 instead of 1234, and maybe AUTPA instead
     *  of AUT-PA [ACIP examples]) corresponding to this parse
     *  tree. */
    public String recoverTranslit() {
        ParseIterator pi = getParseIterator();
        if (pi.hasNext()) {
            return pi.next().recoverTranslit();
        }
        return null;
    }

    /** Returns a hashCode appropriate for use with our {@link
     *  #equals(Object)} method. */
    public int hashCode() {
        return al.hashCode();
    }

    /** Returns true if and only if this parse tree is empty. */
    public boolean isEmpty() {
        return al.isEmpty();
    }
}
