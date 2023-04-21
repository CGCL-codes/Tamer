package teachmaster;

import data.Vocabulary;

public interface IMethod {

    public Vocabulary getNext();

    public void setCurrentWordAsKnown(boolean isKnown_p);

    public int getCurrentlyKnownWords();
}
