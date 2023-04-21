package edu.vub.util.regexp;

final class RETokenRepeated extends REToken {

    private REToken token;

    private int min, max;

    private boolean stingy;

    private boolean possessive;

    RETokenRepeated(int subIndex, REToken token, int min, int max) {
        super(subIndex);
        this.token = token;
        this.min = min;
        this.max = max;
    }

    /** Sets the minimal matching mode to true. */
    void makeStingy() {
        stingy = true;
    }

    /** Queries if this token has minimal matching enabled. */
    boolean isStingy() {
        return stingy;
    }

    /** Sets possessive matching mode to true. */
    void makePossessive() {
        possessive = true;
    }

    /** Queries if this token has possessive matching enabled. */
    boolean isPossessive() {
        return possessive;
    }

    /**
     * The minimum length of a repeated token is the minimum length
     * of the token multiplied by the minimum number of times it must
     * match.
     */
    int getMinimumLength() {
        return (min * token.getMinimumLength());
    }

    int getMaximumLength() {
        if (max == Integer.MAX_VALUE) return Integer.MAX_VALUE;
        int tmax = token.getMaximumLength();
        if (tmax == Integer.MAX_VALUE) return tmax;
        return (max * tmax);
    }

    private static REMatch findDoables(REToken tk, CharIndexed input, REMatch mymatch) {
        REMatch.REMatchList doables = new REMatch.REMatchList();
        for (REMatch current = mymatch; current != null; current = current.next) {
            REMatch recurrent = (REMatch) current.clone();
            int origin = recurrent.index;
            tk = (REToken) tk.clone();
            tk.next = tk.uncle = null;
            recurrent.matchFlags |= REMatch.MF_FIND_ALL;
            if (tk.match(input, recurrent)) {
                for (REMatch m = recurrent; m != null; m = m.next) {
                    m.matchFlags &= ~REMatch.MF_FIND_ALL;
                }
                if (recurrent.index == origin) recurrent.empty = true;
                doables.addTail(recurrent);
            }
        }
        return doables.head;
    }

    boolean match(CharIndexed input, REMatch mymatch) {
        boolean stopMatchingIfSatisfied = (mymatch.matchFlags & REMatch.MF_FIND_ALL) == 0;
        REMatch newMatch = matchMinimum(input, mymatch);
        if (newMatch == null) return false;
        int[] visited = initVisited();
        for (REMatch m = newMatch; m != null; m = m.next) {
            visited = addVisited(m.index, visited);
        }
        int max1 = decreaseMax(max, min);
        newMatch = _match(input, newMatch, max1, stopMatchingIfSatisfied, visited);
        if (newMatch != null) {
            mymatch.assignFrom(newMatch);
            return true;
        }
        return false;
    }

    private static int decreaseMax(int m, int n) {
        if (m == Integer.MAX_VALUE) return m;
        return m - n;
    }

    private static int[] initVisited() {
        int[] visited = new int[32];
        visited[0] = 0;
        return visited;
    }

    private static boolean visitedContains(int n, int[] visited) {
        for (int i = 1; i < visited[0]; i++) {
            if (n == visited[i]) return true;
        }
        return false;
    }

    private static int[] addVisited(int n, int[] visited) {
        if (visitedContains(n, visited)) return visited;
        if (visited[0] >= visited.length - 1) {
            int[] newvisited = new int[visited.length + 32];
            System.arraycopy(visited, 0, newvisited, 0, visited.length);
            visited = newvisited;
        }
        visited[0]++;
        visited[visited[0]] = n;
        return visited;
    }

    private REMatch _match(CharIndexed input, REMatch mymatch, int max1, boolean stopMatchingIfSatisfied, int[] visited) {
        if (max1 == 0) {
            return matchRest(input, mymatch);
        }
        max1 = decreaseMax(max1, 1);
        REMatch.REMatchList allResults = new REMatch.REMatchList();
        for (REMatch cur = mymatch; cur != null; cur = cur.next) {
            REMatch cur1 = (REMatch) cur.clone();
            if (stingy) {
                REMatch results = matchRest(input, cur1);
                if (results != null) {
                    if (stopMatchingIfSatisfied) {
                        return results;
                    }
                    allResults.addTail(results);
                }
            }
            DO_THIS: do {
                boolean emptyMatchFound = false;
                REMatch doables = findDoables(token, input, cur1);
                if (doables == null) break DO_THIS;
                if (doables.empty) emptyMatchFound = true;
                if (!emptyMatchFound) {
                    REMatch.REMatchList list = new REMatch.REMatchList();
                    for (REMatch m = doables; m != null; m = m.next) {
                        REMatch m1 = (REMatch) m.clone();
                        int n = m1.index;
                        if (!visitedContains(n, visited)) {
                            visited = addVisited(n, visited);
                            list.addTail(m1);
                        }
                    }
                    if (list.head == null) break DO_THIS;
                    doables = list.head;
                }
                for (REMatch m = doables; m != null; m = m.next) {
                    if (!emptyMatchFound) {
                        REMatch m1 = _match(input, m, max1, stopMatchingIfSatisfied, visited);
                        if (possessive) return m1;
                        if (m1 != null) {
                            if (stopMatchingIfSatisfied) {
                                return m1;
                            }
                            allResults.addTail(m1);
                        }
                    } else {
                        REMatch m1 = matchRest(input, m);
                        if (m1 != null) {
                            if (stopMatchingIfSatisfied) {
                                return m1;
                            }
                            allResults.addTail(m1);
                        }
                    }
                }
            } while (false);
            if (!stingy) {
                REMatch m2 = matchRest(input, cur1);
                if (m2 != null) {
                    if (stopMatchingIfSatisfied) {
                        return m2;
                    }
                    allResults.addTail(m2);
                }
            }
        }
        return allResults.head;
    }

    private REMatch matchMinimum(CharIndexed input, final REMatch mymatch) {
        REMatch newMatch = mymatch;
        int numRepeats = 0;
        while (numRepeats < min) {
            REMatch doables = findDoables(token, input, newMatch);
            if (doables == null) return null;
            newMatch = doables;
            ++numRepeats;
            if (newMatch.empty) break;
        }
        return newMatch;
    }

    private REMatch matchRest(CharIndexed input, final REMatch newMatch) {
        REMatch current, single;
        REMatch.REMatchList doneIndex = new REMatch.REMatchList();
        for (current = newMatch; current != null; current = current.next) {
            single = (REMatch) current.clone();
            if (next(input, single)) {
                doneIndex.addTail(single);
            }
        }
        return doneIndex.head;
    }

    void dump(StringBuffer os) {
        os.append("(?:");
        token.dumpAll(os);
        os.append(')');
        if ((max == Integer.MAX_VALUE) && (min <= 1)) os.append((min == 0) ? '*' : '+'); else if ((min == 0) && (max == 1)) os.append('?'); else {
            os.append('{').append(min);
            if (max > min) {
                os.append(',');
                if (max != Integer.MAX_VALUE) os.append(max);
            }
            os.append('}');
        }
        if (stingy) os.append('?');
    }
}
