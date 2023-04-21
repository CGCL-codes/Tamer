package uk.ac.shef.oak.iracema.filters.window;

import uk.ac.shef.oak.iracema.filters.Filter;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionCannotHandle;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionNoSuchContent;
import uk.ac.shef.wit.runes.exceptions.RunesExceptionNoSuchStructure;
import uk.ac.shef.wit.runes.runestone.Runestone;

public abstract class WindowFilter implements Filter {

    public abstract boolean isStart(Runestone stone, int tokenId) throws RunesExceptionCannotHandle, RunesExceptionNoSuchStructure, RunesExceptionNoSuchContent;

    public abstract boolean isEnd(Runestone stone, int startTokenId, int tokenId) throws RunesExceptionCannotHandle, RunesExceptionNoSuchStructure, RunesExceptionNoSuchContent;
}
