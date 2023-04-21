package org.eiichiro.jazzmaster.examples.petstore.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.logging.Level;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import org.eiichiro.jazzmaster.examples.petstore.search.IndexDocument;
import org.eiichiro.jazzmaster.examples.petstore.search.Indexer;
import org.eiichiro.jazzmaster.examples.petstore.search.UpdateIndex;
import org.eiichiro.jazzmaster.examples.petstore.util.PetstoreConstants;
import org.eiichiro.jazzmaster.examples.petstore.util.PetstoreUtil;
import org.eiichiro.jazzmaster.management.Scope;
import org.eiichiro.jazzmaster.management.Scoped;
import org.eiichiro.jazzmaster.service.Implementation;
import org.eiichiro.jazzmaster.service.Refers;

@Scoped(Scope.APPLICATION)
@Implementation
public class CatalogFacadeImpl implements CatalogFacade {

    private static final boolean bDebug = false;

    @Refers
    private EntityManager entityManager;

    @Refers
    private CatalogReset catalogReset;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public void setCatalogReset(CatalogReset catalogReset) {
        this.catalogReset = catalogReset;
    }

    public CatalogFacadeImpl() {
    }

    public synchronized void reset() {
        catalogReset.reset();
    }

    @SuppressWarnings("unchecked")
    public List<Category> getCategories() {
        List<Category> categories = entityManager.createQuery("SELECT c FROM org.eiichiro.jazzmaster.examples.petstore.model.Category c").getResultList();
        return categories;
    }

    @SuppressWarnings("unchecked")
    public List<Product> getProducts() {
        List<Product> products = entityManager.createQuery("SELECT p FROM org.eiichiro.jazzmaster.examples.petstore.model.Product p").getResultList();
        return products;
    }

    @SuppressWarnings("unchecked")
    public List<Item> getAllItemsFromCategory(String catID) {
        List<Item> items = entityManager.createQuery("SELECT i FROM Item i, Product p WHERE i.productID = p.productID AND p.categoryID LIKE :categoryID AND i.disabled = 0").setParameter("categoryID", catID).getResultList();
        return items;
    }

    @SuppressWarnings("unchecked")
    public List<Item> getItemInChunkVLH(String pID, String iID, int chunkSize) {
        Query query = entityManager.createQuery("SELECT i FROM org.eiichiro.jazzmaster.examples.petstore.model.Item i WHERE i.productID = :pID AND i.disabled = 0");
        List<Item> items;
        int index = 0;
        while (true) {
            items = query.setFirstResult(index++ * chunkSize).setMaxResults(chunkSize).setParameter("pID", pID).getResultList();
            if ((items == null) || items.size() <= 0) {
                break;
            }
            for (Item i : items) {
                if (i.getItemID().equals(iID)) {
                    return items;
                }
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public List<Item> getItemsVLH(String pID, int start, int chunkSize) {
        Query query = entityManager.createQuery("SELECT i FROM org.eiichiro.jazzmaster.examples.petstore.model.Item i WHERE i.productID = :pID AND i.disabled = 0");
        List<Item> items = query.setFirstResult(start).setMaxResults(chunkSize).setParameter("pID", pID).getResultList();
        return items;
    }

    @SuppressWarnings("unchecked")
    public List<Item> getItemsByItemID(String[] itemIDs) {
        List<Item> items = new ArrayList<Item>();
        StringBuffer sbItemIDs = new StringBuffer();
        if (itemIDs.length != 0) {
            for (int i = 0; i < itemIDs.length; ++i) {
                sbItemIDs.append("'");
                sbItemIDs.append(itemIDs[i]);
                sbItemIDs.append("',");
            }
            String idString = sbItemIDs.toString();
            idString = idString.substring(0, idString.length() - 1);
            String queryString = "SELECT i FROM Item i WHERE " + "i.itemID IN (" + idString + ")  AND i.disabled = 0";
            Query query = entityManager.createQuery(queryString + " ORDER BY i.name");
            items = query.getResultList();
        }
        return items;
    }

    @SuppressWarnings("unchecked")
    public List<Item> getItemsByItemIDByRadius(String[] itemIDs, double fromLat, double toLat, double fromLong, double toLong) {
        List<Item> items = new ArrayList<Item>();
        StringBuffer sbItemIDs = new StringBuffer();
        if (itemIDs.length != 0) {
            for (int i = 0; i < itemIDs.length; ++i) {
                sbItemIDs.append("'");
                sbItemIDs.append(itemIDs[i]);
                sbItemIDs.append("',");
            }
            String idString = sbItemIDs.toString();
            idString = idString.substring(0, idString.length() - 1);
            String queryString = "SELECT i FROM Item i WHERE ((" + "i.itemID IN (" + idString + "))";
            Query query = entityManager.createQuery(queryString + " AND " + " ((i.address.latitude BETWEEN :fromLatitude AND :toLatitude) AND " + "(i.address.longitude BETWEEN :fromLongitude AND :toLongitude ))) AND i.disabled = 0" + "  ORDER BY i.name");
            query.setParameter("fromLatitude", fromLat);
            query.setParameter("toLatitude", toLat);
            query.setParameter("fromLongitude", fromLong);
            query.setParameter("toLongitude", toLong);
            items = query.getResultList();
        }
        return items;
    }

    @SuppressWarnings("unchecked")
    public List<Item> getItemsByCategoryVLH(String catID, int start, int chunkSize) {
        Query productQuery = entityManager.createQuery("SELECT p FROM org.eiichiro.jazzmaster.examples.petstore.model.Product p WHERE p.categoryID = :categoryID");
        List<Product> products = productQuery.setParameter("categoryID", catID).getResultList();
        List<String> productIDs = new ArrayList<String>(products.size());
        for (Product product : products) {
            productIDs.add(product.getProductID());
        }
        List<Item> items = new ArrayList<Item>();
        for (String productID : productIDs) {
            Query itemQuery = entityManager.createQuery("SELECT i FROM org.eiichiro.jazzmaster.examples.petstore.model.Item i WHERE i.productID = :productID");
            items.addAll(itemQuery.setParameter("productID", productID).getResultList());
        }
        Collections.sort(items, new Comparator<Item>() {

            public int compare(Item o1, Item o2) {
                return o1.getItemID().compareTo(o2.getItemID());
            }
        });
        if (items.size() < chunkSize) {
            chunkSize = items.size();
        }
        return items.subList(start, chunkSize);
    }

    @SuppressWarnings("unchecked")
    public List<Item> getItemsByCategoryByRadiusVLH(String catID, int start, int chunkSize, double fromLat, double toLat, double fromLong, double toLong) {
        Query productQuery = entityManager.createQuery("SELECT p FROM org.eiichiro.jazzmaster.examples.petstore.model.Product p " + "WHERE p.categoryID = :categoryID");
        List<Product> products = productQuery.setParameter("categoryID", catID).getResultList();
        List<String> productIDs = new ArrayList<String>(products.size());
        for (Product product : products) {
            productIDs.add(product.getProductID());
        }
        List<Item> items = new ArrayList<Item>();
        for (String productID : productIDs) {
            Query itemQuery = entityManager.createQuery("SELECT i FROM org.eiichiro.jazzmaster.examples.petstore.model.Item i " + "WHERE i.productID = :productID " + "AND i.disabled = 0");
            itemQuery.setParameter("productID", productID);
            items.addAll(itemQuery.getResultList());
        }
        List<Item> itemsInRadius = new ArrayList<Item>();
        for (Item item : items) {
            double latitude = item.getAddress().getLatitude();
            double longitude = item.getAddress().getLongitude();
            if ((latitude >= fromLat && latitude <= toLat) && (longitude >= fromLong && longitude <= toLong)) {
                itemsInRadius.add(item);
            }
        }
        Collections.sort(itemsInRadius, new Comparator<Item>() {

            public int compare(Item o1, Item o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });
        if (itemsInRadius.size() < chunkSize) {
            chunkSize = itemsInRadius.size();
        }
        return itemsInRadius.subList(start, chunkSize);
    }

    @SuppressWarnings("unchecked")
    public List<ZipLocation> getZipCodeLocations(String city, int start, int chunkSize) {
        String pattern = "'" + city.toUpperCase() + "%'";
        Query query = entityManager.createQuery("SELECT  z FROM ZipLocation z where UPPER(z.city) LIKE " + pattern);
        List<ZipLocation> zipCodeLocations = query.setFirstResult(start).setMaxResults(chunkSize).getResultList();
        return zipCodeLocations;
    }

    @SuppressWarnings("unchecked")
    public List<Product> getProducts(String catID) {
        List<Product> products = entityManager.createQuery("SELECT p FROM org.eiichiro.jazzmaster.examples.petstore.model.Product p WHERE p.categoryID = :categoryID").setParameter("categoryID", catID).getResultList();
        return products;
    }

    @SuppressWarnings("unchecked")
    public List<Item> getItems(String prodID) {
        List<Item> items = entityManager.createQuery("SELECT i FROM Item i WHERE i.productID LIKE :productID AND i.disabled = 0").setParameter("productID", prodID).getResultList();
        return items;
    }

    public Category getCategory(String categoryID) {
        Category result = entityManager.find(Category.class, categoryID);
        return result;
    }

    public Item getItem(String itemID) {
        Item result = entityManager.find(Item.class, itemID);
        return result;
    }

    public String addItem(Item item) {
        for (String tagID : item.getTags()) {
            Tag tag = entityManager.find(Tag.class, tagID);
            tag.incrementRefCount();
            tag.getItems().add(item.getItemID());
            entityManager.merge(tag);
        }
        entityManager.persist(item);
        if (bDebug) {
            System.out.println("\n***Item id of new item is : " + item.getItemID());
        }
        indexItem(new IndexDocument(item));
        return item.getItemID();
    }

    public void updateItem(Item item) {
        entityManager.merge(item);
    }

    @SuppressWarnings("unchecked")
    public Collection doSearch(String querryString) {
        Query searchQuery = entityManager.createNativeQuery("SELECT * FROM Item WHERE (name LIKE ? OR description LIKE ?) AND disabled = 0");
        searchQuery.setParameter(1, "%" + querryString + "%");
        searchQuery.setParameter(2, "%" + querryString + "%");
        Collection results = searchQuery.getResultList();
        return results;
    }

    public void addTagsToItemId(String sxTags, String itemId) {
        Item item = getItem(itemId);
        StringTokenizer stTags = new StringTokenizer(sxTags, " ");
        String tagx = null;
        Tag tag = null;
        while (stTags.hasMoreTokens()) {
            tagx = stTags.nextToken().toLowerCase();
            if (!item.containsTag(tagx)) {
                if (bDebug) {
                    System.out.println("Adding TAG = " + tagx);
                }
                tag = addTag(tagx);
                tag.getItems().add(item.getItemID());
                tag.incrementRefCount();
                item.getTags().add(tag.getTagID());
            }
        }
        entityManager.merge(item);
        for (String tagID : item.getTags()) {
            if (bDebug) {
                Tag tagz = entityManager.find(Tag.class, tagID);
                System.out.println("\n***Merging tag = " + tagz.getTag());
                entityManager.merge(tagz);
            }
        }
        UpdateIndex update = new UpdateIndex();
        try {
            update.updateDocTag(PetstoreConstants.PETSTORE_INDEX_DIRECTORY, "tag", item.tagsAsString(), item.getItemID(), UpdateIndex.REPLACE_FIELD);
        } catch (Exception e) {
            throw new RuntimeException("Error persisting tag", e);
        }
    }

    @SuppressWarnings("unchecked")
    public Tag addTag(String sxTag) {
        Tag tag = null;
        List<Tag> tags = entityManager.createQuery("SELECT t FROM Tag t WHERE t.tag = :tag").setParameter("tag", sxTag).getResultList();
        if (tags.isEmpty()) {
            tag = new Tag(sxTag);
            entityManager.persist(tag);
        } else {
            tag = tags.get(0);
        }
        return tag;
    }

    @SuppressWarnings("unchecked")
    public List<Tag> getTagsInChunk(int start, int chunkSize) {
        Query query = entityManager.createQuery("SELECT t FROM org.eiichiro.jazzmaster.examples.petstore.model.Tag t ORDER BY t.refCount DESC, t.tag");
        List<Tag> tags = query.setFirstResult(start).setMaxResults(chunkSize).getResultList();
        return tags;
    }

    @SuppressWarnings("unchecked")
    public Tag getTag(String sxTag) {
        Tag tag = null;
        List<Tag> tags = entityManager.createQuery("SELECT t FROM org.eiichiro.jazzmaster.examples.petstore.model.Tag t WHERE t.tag = :tag").setParameter("tag", sxTag).getResultList();
        if (tags != null && !tags.isEmpty()) {
            tag = tags.get(0);
        }
        return tag;
    }

    private void indexItem(IndexDocument indexDoc) {
        if (bDebug) {
            System.out.println("\n*** document to index - " + indexDoc);
        }
        Indexer indexer = null;
        try {
            indexer = new Indexer(PetstoreConstants.PETSTORE_INDEX_DIRECTORY, false);
            PetstoreUtil.getLogger().log(Level.FINE, "Adding document to index: " + indexDoc.toString());
            indexer.addDocument(indexDoc);
        } catch (Exception e) {
            PetstoreUtil.getLogger().log(Level.WARNING, "index.exception", e);
            e.printStackTrace();
        } finally {
            try {
                if (indexer != null) {
                    indexer.close();
                }
            } catch (Exception ee) {
                ee.printStackTrace();
            }
        }
    }

    @SuppressWarnings("unchecked")
    public Collection<Item> getItems(Collection<String> items) {
        Query query = entityManager.createQuery("SELECT i " + "FROM org.eiichiro.jazzmaster.examples.petstore.model.Item i " + "WHERE i.itemID = :itemID");
        return query.setParameter("itemID", items).getResultList();
    }

    @SuppressWarnings("unchecked")
    public String tagsAsString(Collection<String> tags) {
        StringBuilder stringBuilder = new StringBuilder();
        Query query = entityManager.createQuery("SELECT t " + "FROM org.eiichiro.jazzmaster.examples.petstore.model.Tag t " + "WHERE t.tagID = :tagID");
        for (Tag tag : (List<Tag>) query.setParameter("tagID", tags).getResultList()) {
            stringBuilder.append(tag.getTag());
            stringBuilder.append(" ");
        }
        return stringBuilder.toString().trim();
    }
}
