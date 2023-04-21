package org.petsoar.search.lucene;

import org.apache.lucene.index.IndexReader;
import org.petsoar.persistence.hibernate.DefaultHibernateSessionFactory;
import org.petsoar.persistence.hibernate.HibernatePersistenceManager;
import org.petsoar.pets.DefaultPetStore;
import org.petsoar.pets.Pet;
import org.petsoar.pets.PetStore;
import java.io.File;
import java.util.List;

public class OfflineLuceneIndexer {

    private static final String INDEX_FILE = "index";

    public static void main(String[] args) throws Exception {
        deleteIndexFile();
        HibernatePersistenceManager hpm = new HibernatePersistenceManager();
        DefaultHibernateSessionFactory hsf = new DefaultHibernateSessionFactory();
        hpm.setHibernateSessionFactory(hsf);
        hsf.init();
        hpm.init();
        DefaultPetStore petStore = new DefaultPetStore();
        petStore.setPersistenceManager(hpm);
        LuceneIndexStore indexStore = new LuceneIndexStore(INDEX_FILE);
        LuceneIndexer luceneIndexer = new LuceneIndexer();
        luceneIndexer.setIndexStore(indexStore);
        luceneIndexer.setLuceneDocumentFactory(new DefaultLuceneDocumentFactory());
        indexPets(petStore, luceneIndexer, indexStore);
        hpm.dispose();
        hsf.destroy();
    }

    private static void indexPets(PetStore petStore, LuceneIndexer luceneIndexer, LuceneIndexStore indexStore) throws Exception {
        List pets = petStore.getPets();
        for (int i = 0; i < pets.size(); i++) {
            Pet pet = (Pet) pets.get(i);
            luceneIndexer.index(pet);
            System.out.println("Indexing pet:" + pet.getName());
        }
        IndexReader indexReader = indexStore.createReader();
        System.out.println("Indexed " + indexReader.numDocs() + " pets.");
        indexReader.close();
    }

    private static void deleteIndexFile() {
        File indexFile = new File(INDEX_FILE);
        if (indexFile.exists()) {
            indexFile.delete();
        }
    }
}
