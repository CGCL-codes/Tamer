package org.fb4j.marketplace;

import java.util.Date;
import java.util.Set;
import org.fb4j.FacebookObject;

/**
 * @author Mino Togna
 * 
 */
public interface Listing extends FacebookObject {

    String getCategory();

    String getDescription();

    Long getId();

    Set<String> getImageUrls();

    Long getPoster();

    String getSubCategory();

    String getTitle();

    Date getUpdateTime();

    String getUrl();

    /***************************************************************************
	 * FORSALE
	 **************************************************************************/
    String getCondition();

    String getIsbn();

    /***************************************************************************
	 * FORSALE / HOUSING :: REALESTATE
	 **************************************************************************/
    Long getPrice();

    /***************************************************************************
	 * HOUSING
	 **************************************************************************/
    Integer getNumBeds();

    Integer getNumBaths();

    Boolean areDogsAllowed();

    Boolean areCatsAllowed();

    Boolean areSmokersAllowed();

    Integer getSquareFootage();

    String getStreet();

    String getCrossStreet();

    String getPostalCode();

    /***************************************************************************
	 * HOUSING :: RENTALS / HOUSING :: SUBLETS
	 **************************************************************************/
    Integer getRent();

    /***************************************************************************
	 * JOBS
	 **************************************************************************/
    Integer getPay();

    Boolean isFullTime();

    Boolean isPartTime();

    Boolean isInternship();

    Boolean isSeasonalJob();

    Boolean isNonProfit();

    Integer getPayType();

    String getPayTypeDescription();

    /***************************************************************************
	 * Set methods
	 **************************************************************************/
    void setCategory(String category);

    void setImageUrls(Set<String> imageUrls);

    void setPoster(Long poster);

    void setSubCategory(String subCategory);

    void setUpdateTime(Date updateTime);

    void setUrl(String url);

    void setCondition(String condition);

    void setIsbn(String isbn);

    void setPrice(Long price);

    void setNumBeds(Integer numBeds);

    void setNumBaths(Integer numBaths);

    void setDogsAllowed(Boolean dogsAllowed);

    void setCatsAllowed(Boolean catsAllowed);

    void setSmokersAllowed(Boolean smokersAllowed);

    void setSquareFootage(Integer squareFootage);

    void setStreet(String street);

    void setCrossStreet(String crossStreet);

    void setPostalCode(String postalCode);

    void setRent(Integer rent);

    void setPay(Integer pay);

    void setFullTime(Boolean fullTime);

    void setPartTime(Boolean partTime);

    void setInternship(Boolean internship);

    void setSeasonalJob(Boolean seasonalJob);

    void setNonProfit(Boolean nonProfit);

    void setPayType(Integer payType);

    void setPayTypeDescription(String payTypeDescription);

    void setTitle(String title);

    void setDescription(String description);
}
