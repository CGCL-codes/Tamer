package business.bibtex;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import business.bibtex.exceptions.BibtexException;
import business.common.Config;
import business.db.DatabaseObject;
import business.management.Categories;
import business.management.Category;
import business.management.User;

/**
 * Base class for all references. Contains bibtex, user, category and statistic information.
 * @author pdressel
 *
 */
public class Publication extends DatabaseObject implements Serializable {

    private static final long serialVersionUID = -3598017033339671622L;

    protected BibtexType fBibtexType = new BibtexType();

    protected BibtexFieldMap fBibtexFields = new BibtexFieldMap();

    protected User fSubmitter = User.anonymous();

    protected Categories fCategories = new Categories();

    protected boolean fVerified = false;

    protected int fReviewCount = 0;

    protected String fTitle = "";

    protected String fAuthor = "";

    protected String fReference = "";

    protected Date fDate = null;

    protected int fAverageRating = 1;

    private String filePrefix = Config.getInstance().getProperty("file.prefix");

    public boolean getPdfExists() {
        String file = filePrefix + getReference().replaceAll(Config.REGEXP_SPECIAL, "-") + ".pdf";
        boolean fileExists = (new File(file)).exists();
        return fileExists;
    }

    /**
   * @author Gregor Poloczek
   * @date 28.06.2006
   * @return
   */
    public int getAverageRating() {
        return fAverageRating;
    }

    /**
   * @author Gregor Poloczek
   * @date 28.06.2006
   * @param pAvarageRating
   */
    public void setAverageRating(int pAverageRating) {
        fAverageRating = pAverageRating;
    }

    /**
   * @author Philipp Dressel
   * @return
   */
    public Categories getCategories() {
        return fCategories;
    }

    /**
   * @author Philipp Dressel
   * @param pCategories
   */
    public void setCategories(Categories pCategories) {
        fCategories = pCategories;
    }

    /**
   * @author Philipp Dressel
   * @return
   */
    public BibtexType getBibtexType() {
        return fBibtexType;
    }

    /**
   * @author Philipp Dressel
   * @param pBibtexType
   */
    public void setBibtexType(BibtexType pBibtexType) {
        fBibtexType = pBibtexType;
    }

    /**
   * @author Philipp Dressel
   * @return
   */
    public BibtexFieldMap getBibtexFields() {
        return fBibtexFields;
    }

    /**
   * @author Philipp Dressel
   * @param pBibtexFields
   * @throws BibtexException
   */
    public void setBibtexFields(BibtexFieldMap pBibtexFields) throws BibtexException {
        fBibtexFields = pBibtexFields;
    }

    /**
   * @author Philipp Dressel
   * @param pFieldName
   * @return
   */
    public boolean hasField(String pFieldName) {
        return (fBibtexFields.get(pFieldName) != null);
    }

    /**
   * @author Philipp Dressel
   * @return
   */
    public User getSubmitter() {
        return fSubmitter;
    }

    /**
   * @author Philipp Dressel
   * @param pSubmitter
   */
    public void setSubmitter(User pSubmitter) {
        fSubmitter = pSubmitter;
    }

    /**
   * @author Philipp Dressel
   * @param pConstraints
   */
    public String verifyBibtexConstraints(BibtexConstraints pConstraints, Categories pTaxonomy, boolean admin) throws BibtexException {
        StringBuffer warnings = new StringBuffer();
        String bibtexname = TypoDictionary.correctTypos(fBibtexType.getBibtexName().toLowerCase());
        if (pConstraints.getBibtexTypeMap().containsKey(bibtexname)) {
            fBibtexType = pConstraints.getBibtexTypeMap().get(bibtexname);
        } else {
            throw new BibtexException("Invalid bibtex type: " + fBibtexType.getBibtexName());
        }
        BibtexFields newfields = new BibtexFields();
        for (BibtexField field : fBibtexFields.asVector()) {
            if (field.getKey().getBibtexName().equals("class")) {
                String[] categoryname;
                if (field.getValue().indexOf(",") != -1) {
                    categoryname = field.getValue().split(",");
                } else if (field.getValue().indexOf(" ") != -1) {
                    categoryname = field.getValue().split(" ");
                } else {
                    categoryname = new String[1];
                    categoryname[0] = field.getValue();
                }
                for (int i = 0; i < categoryname.length; i++) {
                    Category cat = new Category();
                    String name = categoryname[i].replaceAll("_", " ").replaceAll("\n", "").trim();
                    name = TypoDictionary.correctTypos(name);
                    if (name.equals("")) {
                        continue;
                    }
                    cat.setName(name);
                    boolean contained = false;
                    Category dbcat = new Category();
                    for (Category c : pTaxonomy) {
                        if (c.getName().toLowerCase().equals(cat.getName().toLowerCase())) {
                            contained = true;
                            dbcat = c;
                            break;
                        }
                    }
                    if (contained) {
                        getCategories().add(dbcat);
                    } else {
                        throw new BibtexException("Invalid category: '" + name + "' in publication: " + this.getReference());
                    }
                }
            } else if (pConstraints.getBibtexKeyMap().containsKey(TypoDictionary.correctTypos(field.getKey().getBibtexName()))) {
                if (field.getValue().equals("") && !admin) {
                    throw new BibtexException("Field '" + field.getKey().getBibtexName() + "' cannot be empty.");
                }
                String name = TypoDictionary.correctTypos(field.getKey().getBibtexName());
                BibtexField newfield = new BibtexField((pConstraints.getBibtexKeyMap().get(name)), field.getValue());
                newfields.add(newfield);
            } else if (!TypoDictionary.correctTypos(field.getKey().getBibtexName()).equals("")) {
                warnings.append("\nUnknown bibtex key '" + field.getKey() + "' in publication: " + this.getReference());
            }
        }
        warnings.append(setFields(newfields, pConstraints.getRequiredKeysByBibtexType(fBibtexType), pConstraints.getOptionalKeysByBibtexType(fBibtexType), admin));
        return warnings.toString();
    }

    private String setFields(BibtexFields pNewFields, BibtexKeys pRequiredKeys, BibtexKeys pOptionalKeys, boolean admin) throws BibtexException {
        StringBuffer warnings = new StringBuffer();
        BibtexFieldMap checkedFields = new BibtexFieldMap();
        for (BibtexField field : pNewFields) {
            if (pRequiredKeys.contains(field.getKey()) || pOptionalKeys.contains(field.getKey())) {
                checkedFields.put(field);
            } else {
                warnings.append("\nIgnoring bibtex key '" + field.getKey() + "' in publication: " + this.getReference() + "\n");
            }
        }
        for (BibtexKey key : pRequiredKeys) {
            if (key.getAlternate() != null && key.getAlternate().isExclusive() && checkedFields.containsKey(key.getAlternate().getBibtexName())) {
                if (admin) {
                    warnings.append("The fields: '" + key.getBibtexName() + "' and '" + key.getAlternate().getBibtexName() + "' are mutual in publication: " + this.getReference() + "\n");
                } else {
                    throw new BibtexException("Missing required field: '" + key.getBibtexName() + "' in publication: " + this.getReference() + "\n");
                }
            }
            if (!checkedFields.containsKey(key.getBibtexName())) {
                if (key.getAlternate() == null || !checkedFields.containsKey(key.getAlternate().getBibtexName())) {
                    if (admin) {
                        warnings.append("Missing required field: '" + key.getBibtexName() + "' in publication: " + this.getReference() + "\n");
                    } else {
                        throw new BibtexException("Missing required field: '" + key.getBibtexName() + "' in publication: " + this.getReference() + "\n");
                    }
                }
            }
        }
        for (BibtexKey key : pOptionalKeys) {
            if (key.getAlternate() != null && key.getAlternate().isExclusive() && checkedFields.containsKey(key.getAlternate().getBibtexName())) {
                warnings.append("The fields: '" + key.getBibtexName() + "' and '" + key.getAlternate().getBibtexName() + "' are mutual in publication: " + this.getReference() + "\n");
                pOptionalKeys.remove(key.getAlternate());
            }
        }
        fBibtexFields = checkedFields;
        return warnings.toString();
    }

    /**
   * @author Gregor Poloczek
   * @return
   */
    public String getTitle() {
        BibtexField mostRelevant = null;
        for (BibtexField field : fBibtexFields.asVector()) {
            if ((mostRelevant == null) && (field.getKey().getTitleRelevance() != 100)) {
                mostRelevant = field;
            } else if (field.getKey().getTitleRelevance() != 100) {
                if (field.getKey().getTitleRelevance() < mostRelevant.getKey().getTitleRelevance()) {
                    mostRelevant = field;
                }
            }
        }
        if (mostRelevant != null) {
            return mostRelevant.getValue();
        } else if ((mostRelevant == null) && (fBibtexFields.asVector().isEmpty())) {
            return fTitle;
        } else {
            return fReference;
        }
    }

    public String getConvertedUTF8Title() {
        return SpecialSymbolsConverter.convert(getTitle(), true);
    }

    /**
   * @author Philipp Dressel
   * @param pTitle
   */
    public void setTitle(String pTitle) {
        fTitle = pTitle;
    }

    /**
   * @author Gregor Poloczek
   * @return Author
   */
    public String getAuthor() {
        BibtexField mostRelevant = null;
        for (BibtexField field : fBibtexFields.asVector()) {
            if ((mostRelevant == null) && (field.getKey().getAuthorRelevance() != 100)) {
                mostRelevant = field;
            } else if (field.getKey().getAuthorRelevance() != 100) {
                if (field.getKey().getAuthorRelevance() < mostRelevant.getKey().getAuthorRelevance()) {
                    mostRelevant = field;
                }
            }
        }
        if (mostRelevant != null) {
            return mostRelevant.getValue();
        } else if ((mostRelevant == null) && (fBibtexFields.asVector().isEmpty())) {
            return fAuthor;
        } else {
            return "";
        }
    }

    /**
   * @author Christian Templin
   * @param pAuthor
   */
    public void setAuthor(String pAuthor) {
        fAuthor = pAuthor;
    }

    /**
   * @author Christian Templin
   * @return Year
   */
    public Date getDate() {
        BibtexField year = fBibtexFields.get("year");
        if (year != null) {
            return new Date();
        } else return fDate;
    }

    /**
   * @author Christian Templin
   * @param pYear
   */
    public void setDate(Date pDate) {
        fDate = pDate;
    }

    /**
   * @author Gregor Poloczek
   * @date 08.06.2006
   * @return empty Publication with no information and primaryKey = '-1'
   */
    public static Publication none() {
        return new Publication();
    }

    /**
   * @author Philipp Dressel
   *
   */
    public String toString() {
        return toBibtexString();
    }

    public boolean equals(Object o) {
        if (o instanceof Publication) {
            Publication p = (Publication) o;
            if (p.getBibtexType().equals(getBibtexType()) && p.getBibtexFields().equals(getBibtexFields())) {
                return true;
            }
        }
        return false;
    }

    public boolean getVerified() {
        return fVerified;
    }

    public void setVerified(boolean pVerified) {
        fVerified = pVerified;
    }

    public String getReference() {
        return fReference;
    }

    public void setReference(String pReference) {
        fReference = pReference;
    }

    public String toBibtexString() {
        StringBuffer result = new StringBuffer();
        result.append("@");
        result.append(fBibtexType.getBibtexName());
        result.append(" {");
        result.append(fReference);
        result.append(",\n");
        for (BibtexField field : fBibtexFields.asVector()) {
            result.append("  ");
            result.append(field.getKey().getBibtexName());
            result.append(" = \"");
            result.append(field.getValue());
            result.append("\",\n");
        }
        result.append("  class = \"");
        for (Category cat : fCategories) {
            result.append(cat.getName().replaceAll(" ", "_"));
            if (cat != fCategories.lastElement()) {
                result.append(", ");
            }
        }
        result.append("\",\n");
        result.append("}\n");
        return SpecialSymbolsConverter.convert(result.toString(), false);
    }

    public int getReviewCount() {
        return fReviewCount;
    }

    public void setReviewCount(int pReviewCount) {
        fReviewCount = pReviewCount;
    }

    public String getShortTitle() {
        int maxLength = 100;
        return (getTitle().length() < maxLength - 1 ? getTitle() : getTitle().substring(0, maxLength - 1) + "...");
    }

    public String getShortAuthor() {
        int maxLength = 50;
        return (getAuthor().length() < maxLength - 1 ? getAuthor() : getAuthor().substring(0, maxLength - 1) + "...");
    }
}
