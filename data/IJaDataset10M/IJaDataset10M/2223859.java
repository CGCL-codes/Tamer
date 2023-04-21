package uk.ac.shef.wit.trex.view;

import uk.ac.shef.wit.commons.UtilCollections;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionCannotHandle;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionRuneExecution;
import uk.ac.shef.wit.runes.runestone.Runestone;
import java.util.HashSet;
import java.util.Set;

public class TokensViewODFAsText extends TokensViewAbstract {

    public TokensViewODFAsText(final String viewName, final String tokenType) {
        super(viewName, tokenType);
    }

    public Set<String> analyseRequired(final Runestone stone) throws RunesExceptionCannotHandle, RunesExceptionRuneExecution {
        final String name = getTokenType();
        return UtilCollections.add(new HashSet<String>(), "start_offset_in_" + name, name + "_string", name + "_token_has_string", "previous_" + name + "_token", "next_" + name + "_token", name + "_has_first_token", "token_from_" + name, "token_has_start_offset_in_" + name);
    }

    public Set<String> analyseProvided(final Runestone stone) throws RunesExceptionCannotHandle, RunesExceptionRuneExecution {
        final String name = getViewName() + '_' + getTokenType();
        return UtilCollections.add(new HashSet<String>(), name + "_token", "start_offset_in_" + name, "end_offset_in_" + name, name + "_string", name + "_token_has_string", "previous_" + name + "_token", "next_" + name + "_token", name + "_has_first_token", name + "_contains_token", name + "_contains_string", "token_from_" + name, "token_has_start_offset_in_" + name, "token_has_end_offset_in_" + name);
    }

    protected void createView(final Runestone stone) throws RunesExceptionCannotHandle {
        boolean inMeta = false;
        int previousId = 0;
        for (final int[] first : _first) {
            for (int tokenId = first[1]; 0 != tokenId; tokenId = _next.follow(tokenId)) {
                final String string = _strings.retrieve(_hasString.follow(tokenId));
                final int from = _from.follow(tokenId);
                final int start = _starts.retrieve(_hasStart.follow(tokenId));
                StringBuilder token = new StringBuilder();
                for (int i = 0, strlen = string.length(); i < strlen; ++i) {
                    final char c = string.charAt(i);
                    if ('<' == c) {
                        inMeta = true;
                        if (0 < token.length()) {
                            previousId = inscribeToken(token.toString(), from, previousId, start + i, token.length());
                            token = new StringBuilder();
                        }
                    } else if ('>' == c) inMeta = false; else if (!inMeta) token.append(c);
                }
                if (0 < token.length()) previousId = inscribeToken(token.toString(), from, previousId, start + string.length(), token.length());
            }
        }
    }

    private int inscribeToken(final String token, final int from, final int previousId, final int pos, final int length) throws RunesExceptionCannotHandle {
        final int vstart = _vstarts.encode(pos - length);
        final int vend = _vends.encode(pos);
        final int vtokenId = _vtokens.encode(from, vstart, vend);
        final int vstringId = _vstrings.encode(token);
        if (0 == previousId) _vfirst.inscribe(from, vtokenId);
        _vcontainsToken.inscribe(from, vtokenId);
        _vcontainsString.inscribe(from, vstringId);
        _vhasString.inscribe(vtokenId, vstringId);
        _vprevious.inscribe(vtokenId, previousId);
        _vnext.inscribe(previousId, vtokenId);
        return vtokenId;
    }
}
