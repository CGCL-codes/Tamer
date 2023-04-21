package allensoft.javacvs.client;

import java.io.*;

/** Classifies the keyword expansion mode for a particular file. */
public interface KeywordSubstitutionModeClassifier {

    /** Determines what keyword expansion mode should be used for the specified file. */
    KeywordSubstitutionMode getKeywordSubstitutionMode(File file);
}
