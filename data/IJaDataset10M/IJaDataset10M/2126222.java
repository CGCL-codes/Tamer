package org.cast.isi.service;

import java.util.List;
import net.databinder.hib.Databinder;
import net.databinder.models.hib.HibernateListModel;
import net.databinder.models.hib.HibernateObjectModel;
import org.apache.wicket.model.IModel;
import org.cast.cwm.data.User;
import org.cast.cwm.service.CwmService;
import org.cast.cwm.service.EventService;
import org.cast.isi.data.WordCard;
import org.cast.isi.data.builder.WordCardsQuery;

/**
 * Methods to interface with the database representations of WordCards, WordConnections, etc.
 * @author bgoldowsky
 *
 */
public class WordService {

    private static WordService INSTANCE = new WordService();

    public static WordService get() {
        return INSTANCE;
    }

    public IModel<WordCard> getWordCard(Long id) {
        return new HibernateObjectModel<WordCard>(WordCard.class, id);
    }

    public IModel<WordCard> getWordCard(String word, User user) {
        return new HibernateObjectModel<WordCard>(new WordCardsQuery().setWord(word).setUser(user));
    }

    public IModel<List<WordCard>> listWordCards(User user) {
        return new HibernateListModel<WordCard>(new WordCardsQuery().setUser(user));
    }

    public IModel<WordCard> getWordCardCreate(String word, User user, boolean inGlossary) {
        IModel<WordCard> cardModel = getWordCard(word, user);
        if (cardModel.getObject() != null) return cardModel;
        WordCard wc = new WordCard(word, user);
        wc.setGlossaryWord(inGlossary);
        Databinder.getHibernateSession().save(wc);
        CwmService.get().flushChanges();
        EventService.get().saveEvent("wordcard:create", word, "glossary");
        cardModel.setObject(wc);
        return cardModel;
    }
}
