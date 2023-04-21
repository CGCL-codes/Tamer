package de.rowbuddy.client.boat;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;
import com.google.gwt.user.client.ui.SuggestOracle;
import de.rowbuddy.boundary.dtos.BoatDTO;
import de.rowbuddy.client.ServerRequestHandler;
import de.rowbuddy.client.services.BoatRemoteServiceAsync;

public class BoatSuggestOracle extends SuggestOracle {

    private BoatRemoteServiceAsync service;

    private Logger logger = Logger.getLogger(BoatSuggestOracle.class.getName());

    private HashMap<String, BoatDTO> suggestions;

    private Request request;

    private Callback callback;

    public BoatSuggestOracle(BoatRemoteServiceAsync service) {
        this.service = service;
        suggestions = new HashMap<String, BoatDTO>();
    }

    private class ItemSuggestion implements Suggestion {

        private String replacement;

        public ItemSuggestion(String replacement) {
            this.replacement = replacement;
        }

        @Override
        public String getDisplayString() {
            return replacement;
        }

        @Override
        public String getReplacementString() {
            return replacement;
        }
    }

    public BoatDTO getSuggestion(String value) {
        return suggestions.get(value);
    }

    @Override
    public void requestSuggestions(Request req, Callback arg1) {
        this.request = req;
        this.callback = arg1;
        service.search(req.getQuery(), new ServerRequestHandler<List<BoatDTO>>(null, "Boote suchen", null, null) {

            @Override
            public void onSuccess(List<BoatDTO> arg0) {
                SuggestOracle.Response resp = new SuggestOracle.Response();
                logger.info("New suggestions received!");
                List<Suggestion> suggs = new LinkedList<SuggestOracle.Suggestion>();
                for (BoatDTO boat : arg0) {
                    suggestions.put(boat.getName(), boat);
                    suggs.add(new ItemSuggestion(boat.getName()));
                }
                resp.setSuggestions(suggs);
                callback.onSuggestionsReady(request, resp);
            }
        });
    }
}
