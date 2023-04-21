package org.apache.oro.text.regex;

/**
 * A class used to store and access the results of a Perl5Pattern match.
 *
 * @version @version@
 * @since 1.0
 * @see PatternMatcher
 * @see Perl5Matcher
 */
final class Perl5MatchResult implements MatchResult {

    /**
   * The character offset into the line or stream where the match
   * begins.  Pattern matching methods that look for matches a line at
   * a time should use this field as the offset into the line
   * of the match.  Methods that look for matches independent of line
   * boundaries should use this field as the offset into the entire
   * text stream.
   */
    int _matchBeginOffset;

    /**
   * Arrays containing the beginning and end offsets of the pattern
   * groups matched within the actual matched pattern contained in the 
   * variable <code>match</code>.
   * Pattern matching methods that do not match subgroups, will only contain
   * entries for group 0, which always refers to the entire pattern.
   * <code>beginGroupOffset</code> contains the start offset of the groups,
   * indexed by group number, which will always be 0 for group 0.
   * <code>endGroupOffset</code> contains the ending offset + 1 of the groups.
   * A group matching the null string will have  <code>beginGroupOffset</code>
   * and <code>endGroupOffset</code> entries of equal value.  Following a
   * convention established by the GNU regular expression library for the
   * C language, groups that are not part of a match contain -1 as their
   * begin and end offsets.
   */
    int[] _beginGroupOffset, _endGroupOffset;

    /**
   * The entire string that matched the pattern.
   */
    String _match;

    /**
   * Constructs a MatchResult able to store match information for
   * a number of subpattern groups.
   * <p>
   * @param groups  The number of groups this MatchResult can store.
   *        Only postitive values greater than or equal to 1 make any
   *        sense.  At minimum, a MatchResult stores one group which
   *        represents the entire pattern matched including all subparts.
   */
    Perl5MatchResult(int groups) {
        _beginGroupOffset = new int[groups];
        _endGroupOffset = new int[groups];
    }

    /**
   * @return The length of the match.
   */
    public int length() {
        int length;
        length = (_endGroupOffset[0] - _beginGroupOffset[0]);
        return (length > 0 ? length : 0);
    }

    /**
   * @return The number of groups contained in the result.  This number
   *         includes the 0th group.  In other words, the result refers
   *         to the number of parenthesized subgroups plus the entire match
   *         itself.
   */
    public int groups() {
        return _beginGroupOffset.length;
    }

    /**
   * @param group The pattern subgroup to return.
   * @return A string containing the indicated pattern subgroup.  Group
   *         0 always refers to the entire match.  If a group was never
   *         matched, it returns null.  This is not to be confused with
   *         a group matching the null string, which will return a String
   *         of length 0.
   */
    public String group(int group) {
        int begin, end, length;
        if (group < _beginGroupOffset.length) {
            begin = _beginGroupOffset[group];
            end = _endGroupOffset[group];
            length = _match.length();
            if (begin >= 0 && end >= 0) {
                if (begin < length && end <= length && end > begin) return _match.substring(begin, end); else if (begin <= end) return "";
            }
        }
        return null;
    }

    /**
   * @param group The pattern subgroup.
   * @return The offset into group 0 of the first token in the indicated
   *         pattern subgroup.  If a group was never matched or does
   *         not exist, returns -1.
   */
    public int begin(int group) {
        int begin, end;
        if (group < _beginGroupOffset.length) {
            begin = _beginGroupOffset[group];
            end = _endGroupOffset[group];
            if (begin >= 0 && end >= 0) return begin;
        }
        return -1;
    }

    /**
   * @param group The pattern subgroup.
   * @return Returns one plus the offset into group 0 of the last token in
   *         the indicated pattern subgroup.  If a group was never matched
   *         or does not exist, returns -1.  A group matching the null
   *         string will return its start offset.
   */
    public int end(int group) {
        int begin, end;
        if (group < _beginGroupOffset.length) {
            begin = _beginGroupOffset[group];
            end = _endGroupOffset[group];
            if (begin >= 0 && end >= 0) return end;
        }
        return -1;
    }

    /**
   * Returns an offset marking the beginning of the pattern match
   * relative to the beginning of the input.
   * <p>
   * @param group The pattern subgroup.
   * @return The offset of the first token in the indicated
   *         pattern subgroup.  If a group was never matched or does
   *         not exist, returns -1.
   */
    public int beginOffset(int group) {
        int begin, end;
        if (group < _beginGroupOffset.length) {
            begin = _beginGroupOffset[group];
            end = _endGroupOffset[group];
            if (begin >= 0 && end >= 0) return _matchBeginOffset + begin;
        }
        return -1;
    }

    /**
   * Returns an offset marking the end of the pattern match 
   * relative to the beginning of the input.
   * <p>
   * @param group The pattern subgroup.
   * @return Returns one plus the offset of the last token in
   *         the indicated pattern subgroup.  If a group was never matched
   *         or does not exist, returns -1.  A group matching the null
   *         string will return its start offset.
   */
    public int endOffset(int group) {
        int begin, end;
        if (group < _endGroupOffset.length) {
            begin = _beginGroupOffset[group];
            end = _endGroupOffset[group];
            if (begin >= 0 && end >= 0) return _matchBeginOffset + end;
        }
        return -1;
    }

    /**
   * The same as group(0).
   *
   * @return A string containing the entire match.
   */
    public String toString() {
        return group(0);
    }
}
