package com.doculibre.intelligid.utils;

import java.util.List;
import org.hibernate.SessionFactory;
import com.doculibre.intelligid.delegate.FGDDelegate;
import com.doculibre.intelligid.entites.skos.SkosConcept;
import com.doculibre.intelligid.entrepot.conversation.ConversationManager;

public class TestSkos {

    public static void main(String[] args) {
        ConversationManager conversationManager = FGDSpringUtils.getConversationManager();
        conversationManager.beginConversation();
        SessionFactory sessionFactory = (SessionFactory) FGDSpringUtils.getBean("sessionFactory");
        System.out.println("search abolir");
        List<SkosConcept> skosConcepts = FGDSpringUtils.getSkosIndexHelper().search("abolir");
        for (SkosConcept skosConcept : skosConcepts) {
            System.out.println(skosConcept.getId() + ":" + skosConcept.getRdfAbout());
        }
        System.out.println("search pref abolir");
        try {
            skosConcepts = FGDSpringUtils.getSkosIndexHelper().searchPrefLabel("RADIO*", new FGDDelegate().getThesaurus(), true);
            for (SkosConcept skosConcept : skosConcepts) {
                System.out.println(skosConcept.getId() + ":" + skosConcept.getRdfAbout());
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        System.out.println("search all abolir");
        try {
            skosConcepts = FGDSpringUtils.getSkosIndexHelper().searchAllLabels("RADIOTHÉRAPIE", new FGDDelegate().getThesaurus(), true);
            for (SkosConcept skosConcept : skosConcepts) {
                System.out.println(skosConcept.getId() + ":" + skosConcept.getRdfAbout());
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        System.out.println("search alt abolir");
        try {
            skosConcepts = FGDSpringUtils.getSkosIndexHelper().searchAltLabel("RADIOTHÉRAPIE", new FGDDelegate().getThesaurus(), true);
            for (SkosConcept skosConcept : skosConcepts) {
                System.out.println(skosConcept.getId() + ":" + skosConcept.getRdfAbout());
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
        conversationManager.commitTransaction();
    }
}
