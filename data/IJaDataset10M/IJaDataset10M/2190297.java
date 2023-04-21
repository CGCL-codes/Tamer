package uk.ac.shef.wit.trex.view;

import uk.ac.shef.wit.runes.exceptions.RunesExceptionCannotHandle;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionNoSuchContent;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionNoSuchStructure;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionRuneExecution;
import uk.ac.shef.wit.runes.rune.Rune;
import uk.ac.shef.wit.runes.runestone.Runestone;
import uk.ac.shef.wit.runes.runestone.Structure;
import uk.ac.shef.wit.runes.runestone.StructureAndContent;
import java.util.Set;

public abstract class TokensViewAbstract implements Rune {

    private final String _viewName;

    private final String _tokenType;

    protected Structure _from;

    protected Structure _hasStart;

    protected Structure _hasEnd;

    protected StructureAndContent<String> _strings;

    protected StructureAndContent<Integer> _starts;

    protected StructureAndContent<Integer> _ends;

    protected Structure _first;

    protected Structure _containsToken;

    protected Structure _containsString;

    protected Structure _hasString;

    protected Structure _previous;

    protected Structure _next;

    protected Structure _vtokens;

    protected StructureAndContent<String> _vstrings;

    protected StructureAndContent<Integer> _vstarts;

    protected StructureAndContent<Integer> _vends;

    protected Structure _vfirst;

    protected Structure _vcontainsToken;

    protected Structure _vcontainsString;

    protected Structure _vhasString;

    protected Structure _vprevious;

    protected Structure _vnext;

    protected TokensViewAbstract(final String viewName, final String tokenType) {
        _viewName = viewName;
        _tokenType = tokenType;
    }

    public String toString() {
        return _viewName + " view over " + _tokenType + " tokens";
    }

    public String getViewName() {
        return _viewName;
    }

    public String getTokenType() {
        return _tokenType;
    }

    public void carve(final Runestone stone) throws RunesExceptionNoSuchStructure, RunesExceptionNoSuchContent, RunesExceptionCannotHandle, RunesExceptionRuneExecution {
        final Set<String> model = stone.getModel();
        if (model.contains("token_from_" + _tokenType)) _from = stone.getStructure("token_from_" + _tokenType);
        if (model.contains("token_has_start_offset_in_" + _tokenType)) _hasStart = stone.getStructure("token_has_start_offset_in_" + _tokenType);
        if (model.contains("token_has_end_offset_in_" + _tokenType)) _hasEnd = stone.getStructure("token_has_end_offset_in_" + _tokenType);
        if (model.contains(_tokenType + "_string")) _strings = stone.getStructureAndContent(_tokenType + "_string");
        if (model.contains("start_offset_in_" + _tokenType)) _starts = stone.getStructureAndContent("start_offset_in_" + _tokenType);
        if (model.contains("end_offset_in_" + _tokenType)) _ends = stone.getStructureAndContent("end_offset_in_" + _tokenType);
        if (model.contains(_tokenType + "_has_first_token")) _first = stone.getStructure(_tokenType + "_has_first_token");
        if (model.contains(_tokenType + "_contains_token")) _containsToken = stone.getStructure(_tokenType + "_contains_token");
        if (model.contains(_tokenType + "_contains_string")) _containsString = stone.getStructure(_tokenType + "_contains_string");
        if (model.contains(_tokenType + "_token_has_string")) _hasString = stone.getStructure(_tokenType + "_token_has_string");
        if (model.contains("previous_" + _tokenType + "_token")) _previous = stone.getStructure("previous_" + _tokenType + "_token");
        if (model.contains("next_" + _tokenType + "_token")) _next = stone.getStructure("next_" + _tokenType + "_token");
        final String name = _viewName + '_' + _tokenType;
        _vtokens = stone.getStructure(name + "_token", "token_from_" + name, "token_has_start_offset_in_" + name, "token_has_end_offset_in_" + name);
        _vstrings = stone.getStructureAndContent(name + "_string");
        _vstarts = stone.getStructureAndContent("start_offset_in_" + name);
        _vends = stone.getStructureAndContent("end_offset_in_" + name);
        _vfirst = stone.getStructure(name + "_has_first_token");
        _vcontainsToken = stone.getStructure(name + "_contains_token");
        _vcontainsString = stone.getStructure(name + "_contains_string");
        _vhasString = stone.getStructure(name + "_token_has_string");
        _vprevious = stone.getStructure("previous_" + name + "_token");
        _vnext = stone.getStructure("next_" + name + "_token");
        createView(stone);
    }

    protected abstract void createView(Runestone stone) throws RunesExceptionCannotHandle, RunesExceptionNoSuchStructure, RunesExceptionNoSuchContent;
}
