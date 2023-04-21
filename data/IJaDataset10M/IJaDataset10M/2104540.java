package uk.co.ordnancesurvey.rabbitdao;

import uk.co.ordnancesurvey.rabbitparser.parsedsentences.IParsedSentence;

public class SentenceCannotBePersistedException extends RuntimeException {

    private static final long serialVersionUID = 2099795340376464595L;

    public SentenceCannotBePersistedException(IParsedSentence sentence, IRabbitSentenceDao dao) {
        super("Sentence " + sentence + " cannot be persisted by " + dao);
    }
}
