package opennlp.tools.tokenize;

import java.util.ArrayList;
import java.util.List;
import opennlp.maxent.Event;
import opennlp.maxent.EventStream;
import opennlp.tools.util.Span;

/** An implementation of EventStream which allows events to be added by 
 *  offset and returns events for these offset-based tokens.
 */
public class TokSpanEventStream implements EventStream {

    private TokenContextGenerator cg;

    private List events;

    private int eventIndex;

    private boolean skipAlphaNumerics;

    /**
   * Initializes the current instance.
   * 
   * @param skipAlphaNumerics
   * @param cg
   */
    public TokSpanEventStream(boolean skipAlphaNumerics, TokenContextGenerator cg) {
        this.skipAlphaNumerics = skipAlphaNumerics;
        events = new ArrayList(50);
        eventIndex = 0;
        this.cg = cg;
    }

    /**
   * Initializes the current instance.
   * 
   * @param skipAlphaNumerics
   */
    public TokSpanEventStream(boolean skipAlphaNumerics) {
        this(skipAlphaNumerics, new DefaultTokenContextGenerator());
    }

    public static Event[] createEvents(Span[] tokens, String text, boolean skipAlphaNumerics, TokenContextGenerator cg) {
        List events = new ArrayList();
        if (tokens.length > 0) {
            int start = tokens[0].getStart();
            int end = tokens[tokens.length - 1].getEnd();
            String sent = text.substring(start, end);
            Span[] candTokens = WhitespaceTokenizer.INSTANCE.tokenizePos(sent);
            int firstTrainingToken = -1;
            int lastTrainingToken = -1;
            for (int ci = 0; ci < candTokens.length; ci++) {
                Span cSpan = candTokens[ci];
                String ctok = sent.substring(cSpan.getStart(), cSpan.getEnd());
                cSpan = new Span(cSpan.getStart() + start, cSpan.getEnd() + start);
                if (ctok.length() > 1 && (!skipAlphaNumerics || !TokenizerME.alphaNumeric.matcher(ctok).matches())) {
                    boolean foundTrainingTokens = false;
                    for (int ti = lastTrainingToken + 1; ti < tokens.length; ti++) {
                        if (cSpan.contains(tokens[ti])) {
                            if (!foundTrainingTokens) {
                                firstTrainingToken = ti;
                                foundTrainingTokens = true;
                            }
                            lastTrainingToken = ti;
                        } else if (cSpan.getEnd() < tokens[ti].getEnd()) {
                            break;
                        } else if (tokens[ti].getEnd() < cSpan.getStart()) {
                        } else {
                            System.err.println("Bad training token: " + tokens[ti] + " cand: " + cSpan + " token=" + text.substring(tokens[ti].getStart(), tokens[ti].getEnd()));
                        }
                    }
                    if (foundTrainingTokens) {
                        for (int ti = firstTrainingToken; ti <= lastTrainingToken; ti++) {
                            Span tSpan = tokens[ti];
                            int cStart = cSpan.getStart();
                            for (int i = tSpan.getStart() + 1; i < tSpan.getEnd(); i++) {
                                String[] context = cg.getContext(ctok, i - cStart);
                                events.add(new Event(DefaultTokenContextGenerator.NO_SPLIT, context));
                            }
                            if (tSpan.getEnd() != cSpan.getEnd()) {
                                String[] context = cg.getContext(ctok, tSpan.getEnd() - cStart);
                                events.add(new Event(DefaultTokenContextGenerator.SPLIT, context));
                            }
                        }
                    }
                }
            }
        }
        return (Event[]) events.toArray(new Event[events.size()]);
    }

    /**
   * Adds training events to the event stream for each of the specified tokens.
   * @param tokens charachter offsets into the specified text.
   * @param text The text of the tokens.
   */
    public void addEvents(Span[] tokens, String text) {
        Event[] tevents = createEvents(tokens, text, this.skipAlphaNumerics, cg);
        events.addAll(java.util.Arrays.asList(tevents));
    }

    public boolean hasNext() {
        return eventIndex < events.size();
    }

    public Event nextEvent() {
        Event e = (Event) events.get(eventIndex);
        eventIndex++;
        if (eventIndex == events.size()) {
            events.clear();
            eventIndex = 0;
        }
        return e;
    }
}
