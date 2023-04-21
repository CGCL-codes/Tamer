package org.decisiondeck.jmcda.persist.xmcda2;

import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeMap;
import org.decisiondeck.jmcda.exc.InvalidInputException;
import org.decisiondeck.jmcda.persist.xmcda2.generated.XCategories;
import org.decisiondeck.jmcda.persist.xmcda2.generated.XCategoriesComparisons;
import org.decisiondeck.jmcda.persist.xmcda2.generated.XCategoriesComparisons.Pairs;
import org.decisiondeck.jmcda.persist.xmcda2.generated.XCategoriesComparisons.Pairs.Pair;
import org.decisiondeck.jmcda.persist.xmcda2.generated.XCategoriesProfiles;
import org.decisiondeck.jmcda.persist.xmcda2.generated.XCategory;
import org.decisiondeck.jmcda.persist.xmcda2.generated.XCategoryProfile;
import org.decisiondeck.jmcda.persist.xmcda2.generated.XCategoryProfile.Limits;
import org.decisiondeck.jmcda.persist.xmcda2.generated.XCategoryReference;
import org.decisiondeck.jmcda.persist.xmcda2.generated.XMCDADoc.XMCDA;
import org.decisiondeck.jmcda.persist.xmcda2.generated.XValue;
import org.decisiondeck.jmcda.persist.xmcda2.utils.XMCDAErrorsManager;
import org.decisiondeck.jmcda.persist.xmcda2.utils.XMCDAErrorsManager.ErrorManagement;
import org.decisiondeck.jmcda.persist.xmcda2.utils.XMCDAHelperWithVarious;
import org.decisiondeck.jmcda.structure.sorting.category.Category;
import org.decisiondeck.jmcda.structure.sorting.category.CatsAndProfs;
import org.decisiondeck.jmcda.structure.sorting.category.ICatsAndProfs;
import org.decisiondeck.utils.collections.CollectionVariousUtils;
import org.decisiondeck.xmcda_oo.structure.Alternative;
import com.google.common.base.Preconditions;
import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

/**
 * Methods for reading and writing categories, including relations between categories and profiles defining them, from
 * and to XMCDA fragments.
 * 
 * @author Olivier Cailloux
 * 
 */
public class XMCDACategories extends XMCDAHelperWithVarious {

    /**
     * When comes from a read, are ordered from worst to best.
     */
    private final Set<Category> m_categories = CollectionVariousUtils.newLinkedHashSetNoNull();

    private final Set<Alternative> m_profiles = CollectionVariousUtils.newHashSetNoNull();

    private final Set<Category> m_inactiveCategories = CollectionVariousUtils.newHashSetNoNull();

    /**
     * Retrieves the categories stored in this object as inactive. The returned set is necessarily empty if no read
     * occurred yet.
     * 
     * @return not <code>null</code>.
     */
    public Set<Category> getInactiveCategories() {
        return Sets.newHashSet(m_inactiveCategories);
    }

    /**
     * <p>
     * Retrieves the categories found in the given fragment in the order defined by their rank, from the worst category
     * (having the greatest number as a rank), to the best one (having the smallest number as a rank). This method only
     * keeps the rank order, the values of the ranks per se are lost. Categories marked as inactive in the given
     * fragment are stored in this object but are not returned.
     * </p>
     * <p>
     * This method expects the categories to have an id and a rank set. In case of unexpected data, an exception is
     * thrown if this object follows the {@link ErrorManagement#THROW} strategy, otherwise, non conforming informations
     * will be skipped.
     * </p>
     * <p>
     * A copy of the returned categories is stored in this object, this replaces any categories previously stored in
     * this object.
     * </p>
     * 
     * @param xCategories
     *            not <code>null</code>.
     * @return not <code>null</code>.
     * @throws InvalidInputException
     *             iff unexpected content has been read and this object follows the {@link ErrorManagement#THROW}
     *             strategy.
     */
    public NavigableSet<Category> read(XCategories xCategories) throws InvalidInputException {
        checkNotNull(xCategories);
        m_categories.clear();
        m_inactiveCategories.clear();
        final TreeMap<Double, Category> byRank = Maps.newTreeMap();
        final List<XCategory> xCategoryList = xCategories.getCategoryList();
        for (XCategory xCategory : xCategoryList) {
            final String id = xCategory.getId();
            if (id == null) {
                error("Has no id: " + xCategory + ".");
                continue;
            }
            final Category category = new Category(id);
            if (!xCategory.isSetRank()) {
                error("Has no rank: " + xCategory + ".");
                continue;
            }
            final XValue xRank = xCategory.getRank();
            final Double rank = readDouble(xRank);
            if (rank == null) {
                continue;
            }
            if (xCategory.isSetActive() && !xCategory.getActive()) {
                m_inactiveCategories.add(category);
                continue;
            }
            byRank.put(rank, category);
        }
        final CatsAndProfs catsAndProfs = new CatsAndProfs();
        for (Category category : byRank.descendingMap().values()) {
            catsAndProfs.addCategory(category);
        }
        m_categories.addAll(catsAndProfs.getCategories());
        return catsAndProfs.getCategories();
    }

    /**
     * <p>
     * Retrieves the categories found in the given fragment together with their relations with the profiles defining
     * them, if found in the given fragment.
     * </p>
     * <p>
     * This method expects that the categories read in the given XMCDA fragment are included in the set of categories
     * stored in this object. In case of unexpected data, an exception is thrown if this object follows the
     * {@link ErrorManagement#THROW} strategy, otherwise, non conforming informations will be skipped.
     * </p>
     * <p>
     * The returned object contains all the categories stored in this object, in order from worst to best, even if they
     * have not been all read. This does <em>not</em> however imply that the returned object is complete as assessed per
     * {@link ICatsAndProfs#isComplete()}.
     * </p>
     * 
     * @param xCategoriesProfiles
     *            not <code>null</code>.
     * @return not <code>null</code>.
     * @throws InvalidInputException
     *             iff unexpected content has been read and this object follows the {@link ErrorManagement#THROW}
     *             strategy.
     * @see #read(XCategories)
     * @see #getCategories
     * @see #setCategories
     * @see #read(XCategoriesProfiles)
     */
    public ICatsAndProfs readUsingCategories(XCategoriesProfiles xCategoriesProfiles) throws InvalidInputException {
        Preconditions.checkNotNull(xCategoriesProfiles);
        final BiMap<Alternative, Category> upCategories = HashBiMap.create();
        final BiMap<Alternative, Category> downCategories = HashBiMap.create();
        final List<XCategoryProfile> xCategoryProfileList = xCategoriesProfiles.getCategoryProfileList();
        for (XCategoryProfile xCategoryProfile : xCategoryProfileList) {
            read(xCategoryProfile, downCategories, upCategories);
        }
        assert downCategories.keySet().equals(upCategories.keySet());
        final CatsAndProfs catsAndProfs = new CatsAndProfs();
        for (Category category : m_categories) {
            catsAndProfs.addCategory(category);
        }
        for (Alternative profile : downCategories.keySet()) {
            final Category upper = upCategories.get(profile);
            final Category lower = downCategories.get(profile);
            if (!m_categories.contains(upper)) {
                error("Not found: " + upper + " for " + profile + ".");
                continue;
            }
            if (!m_categories.contains(lower)) {
                error("Not found: " + lower + " for " + profile + ".");
                continue;
            }
            catsAndProfs.setProfileUp(lower.getName(), profile);
            final Category expectedUp = catsAndProfs.getCategoryUp(profile);
            if (!upper.equals(expectedUp)) {
                error("Unexpected upper category: " + upper + ", expected " + expectedUp + ".");
                continue;
            }
        }
        return catsAndProfs;
    }

    private Alternative read(XCategoryProfile xCategoryProfile, Map<Alternative, Category> downCategories, Map<Alternative, Category> upCategories) throws InvalidInputException {
        Alternative profile;
        final List<String> xIdList = xCategoryProfile.getAlternativeIDList();
        if (xIdList.size() > 1) {
            error("More than one id found at " + xCategoryProfile + ".");
            return null;
        }
        if (xIdList.size() == 0) {
            error("No id found at " + xCategoryProfile + ".");
            return null;
        }
        final String profileId = xIdList.get(0);
        if (profileId == null || profileId.isEmpty()) {
            error("Expected profile id at " + xCategoryProfile + ".");
            return null;
        }
        profile = new Alternative(profileId);
        final List<XCategoryProfile.Limits> xLimitsList = xCategoryProfile.getLimitsList();
        final Limits xLimits = getUnique(xLimitsList, xCategoryProfile.toString());
        if (xLimits == null) {
            return null;
        }
        final XCategoryProfile.Limits.LowerCategory xLower = xLimits.getLowerCategory();
        final XCategoryProfile.Limits.UpperCategory xUpper = xLimits.getUpperCategory();
        final String lowerId = xLower.getCategoryID();
        if (lowerId == null || lowerId.isEmpty()) {
            error("Expected lower category id at " + xCategoryProfile + ".");
            return null;
        }
        final Category lower = new Category(lowerId);
        final String upperId = xUpper.getCategoryID();
        if (upperId == null || upperId.isEmpty()) {
            error("Expected upper category id at " + xCategoryProfile + ".");
            return null;
        }
        final Category upper = new Category(upperId);
        if (downCategories.containsKey(profile)) {
            error("Duplicate " + profile + " at " + xCategoryProfile + ".");
            return null;
        }
        if (upCategories.containsKey(profile)) {
            error("Duplicate " + profile + " at " + xCategoryProfile + ".");
            return null;
        }
        if (downCategories.containsValue(lower)) {
            error("Duplicate " + lower + " at " + xCategoryProfile + ".");
            return null;
        }
        if (upCategories.containsValue(upper)) {
            error("Duplicate " + upper + " at " + xCategoryProfile + ".");
            return null;
        }
        downCategories.put(profile, lower);
        upCategories.put(profile, upper);
        return profile;
    }

    /**
     * Retrieves an XMCDA representation of the given categories.
     * 
     * @param categories
     *            not <code>null</code>, the categories from the worst one to the best one.
     * @return not <code>null</code>.
     */
    public XCategories write(NavigableSet<Category> categories) {
        Preconditions.checkNotNull(categories);
        final XCategories xCategories = XMCDA.Factory.newInstance().addNewCategories();
        int rank = 1;
        for (Category category : categories.descendingSet()) {
            final XCategory xCategory = xCategories.addNewCategory();
            xCategory.setId(category.getName());
            final XValue xRank = xCategory.addNewRank();
            xRank.setInteger(rank);
            ++rank;
        }
        return xCategories;
    }

    /**
     * <p>
     * Retrieves an XMCDA representation of the given categories and profiles. The returned XMCDA fragment contains the
     * associations between the categories and the profiles, for each profile in the given {@link ICatsAndProfs} object.
     * The profiles are written in order from worst to best: this order is considered as more intuitive because the
     * category limits must be written with lower category first.
     * </p>
     * <p>
     * Each profile must have a down and an up category. Ensuring that the given object is complete, as per
     * {@link ICatsAndProfs#isComplete()}, is a sufficient condition, although not necessary.
     * </p>
     * 
     * @param catsAndProfs
     *            not <code>null</code>, may not contain profiles not related to two categories.
     * @return not <code>null</code>.
     */
    public XCategoriesProfiles write(ICatsAndProfs catsAndProfs) {
        Preconditions.checkNotNull(catsAndProfs);
        final XCategoriesProfiles xCategoriesProfiles = XMCDA.Factory.newInstance().addNewCategoriesProfiles();
        for (Alternative profile : catsAndProfs.getProfiles()) {
            final Category down = catsAndProfs.getCategoryDown(profile);
            final Category up = catsAndProfs.getCategoryUp(profile);
            if (down == null) {
                throw new IllegalArgumentException("" + profile);
            }
            if (up == null) {
                throw new IllegalArgumentException("" + profile);
            }
            final XCategoryProfile xCategoryProfile = xCategoriesProfiles.addNewCategoryProfile();
            xCategoryProfile.addAlternativeID(profile.getId());
            final XCategoryProfile.Limits xLimits = xCategoryProfile.addNewLimits();
            final XCategoryProfile.Limits.LowerCategory xLower = xLimits.addNewLowerCategory();
            xLower.setCategoryID(down.getName());
            final XCategoryProfile.Limits.UpperCategory xUpper = xLimits.addNewUpperCategory();
            xUpper.setCategoryID(up.getName());
        }
        return xCategoriesProfiles;
    }

    /**
     * Retrieves a writeable view to the categories stored in this object. The set is empty if no read or set occurred.
     * The returned set does not accept <code>null</code> entries.
     * 
     * @return not <code>null</code>.
     */
    public Set<Category> getCategories() {
        return m_categories;
    }

    /**
     * Sets the categories stored in this object. The iteration order of the given set is reflected in the order of the
     * categories stored in this object.
     * 
     * @param categories
     *            not <code>null</code>, no <code>null</code> entries.
     */
    public void setCategories(Set<Category> categories) {
        Preconditions.checkNotNull(categories);
        m_categories.clear();
        m_categories.addAll(categories);
    }

    /**
     * Retrieves a writeable view to the profiles stored in this object. The set is empty if no read or set occurred.
     * The returned set does not accept <code>null</code> entries.
     * 
     * @return not <code>null</code>.
     */
    public Set<Alternative> getProfiles() {
        return m_profiles;
    }

    /**
     * Sets the profiles stored in this object.
     * 
     * @param profiles
     *            not <code>null</code>, no <code>null</code> entries.
     */
    public void setProfiles(Set<Alternative> profiles) {
        Preconditions.checkNotNull(profiles);
        m_profiles.clear();
        m_profiles.addAll(profiles);
    }

    /**
     * <p>
     * Retrieves the categories found in the given fragment together with their relations with the profiles defining
     * them, if found in the given fragment.
     * </p>
     * <p>
     * This method expects that the categories and profiles read in the given XMCDA fragment define a complete set of
     * categories with profiles, as defined in {@link ICatsAndProfs#isComplete()}. In case of unexpected data, an
     * exception is thrown if this object follows the {@link ErrorManagement#THROW} strategy, otherwise, non conforming
     * informations is skipped, and an empty {@link ICatsAndProfs} object is returned if this method can't figure out
     * the total ordering of the categories and profiles. This happens for example if the profiles all have different
     * associated categories. Suppose the given fragment contains two profiles having each one associated down and one
     * associated up categories, with four different categories: in such a case, there is no way this method can guess
     * which profile is the worst one.
     * </p>
     * 
     * @param xCategoriesProfiles
     *            not <code>null</code>.
     * @return not <code>null</code>, guaranteed to be a complete object (as defined in <code>ICatsAndProfs</code>) if
     *         no unexpected content has been read.
     * @throws InvalidInputException
     *             iff unexpected content has been read and this object follows the {@link ErrorManagement#THROW}
     *             strategy.
     */
    public ICatsAndProfs read(XCategoriesProfiles xCategoriesProfiles) throws InvalidInputException {
        Preconditions.checkNotNull(xCategoriesProfiles);
        final BiMap<Alternative, Category> upCategories = HashBiMap.create();
        final BiMap<Alternative, Category> downCategories = HashBiMap.create();
        final List<XCategoryProfile> xCategoryProfileList = xCategoriesProfiles.getCategoryProfileList();
        for (XCategoryProfile xCategoryProfile : xCategoryProfileList) {
            read(xCategoryProfile, downCategories, upCategories);
        }
        assert downCategories.keySet().equals(upCategories.keySet());
        final CatsAndProfs catsAndProfs = new CatsAndProfs();
        if (downCategories.isEmpty()) {
            return catsAndProfs;
        }
        final Category cStart = downCategories.values().iterator().next();
        Category worstCategory = cStart;
        while (upCategories.values().contains(worstCategory)) {
            final Alternative previousProfile = upCategories.inverse().get(worstCategory);
            final Category down = downCategories.get(previousProfile);
            worstCategory = down;
        }
        Category category = worstCategory;
        catsAndProfs.addCategory(category.getName());
        while (downCategories.containsValue(category)) {
            final Alternative nextProfile = downCategories.inverse().get(category);
            catsAndProfs.setProfileUp(category.getName(), nextProfile);
            final Category nextCategory = upCategories.get(nextProfile);
            category = nextCategory;
            catsAndProfs.setCategoryUp(nextProfile, category);
        }
        if (catsAndProfs.getProfiles().size() != downCategories.size()) {
            final Set<Alternative> leftOvers = Sets.difference(downCategories.keySet(), catsAndProfs.getProfiles());
            assert leftOvers.size() >= 1;
            error("The graph of neighbors from " + cStart + " is not connected to the whole set of profiles and categories: " + leftOvers.iterator().next() + " is unreachable.");
            return new CatsAndProfs();
        }
        return catsAndProfs;
    }

    /**
     * Retrieves an XMCDA representation of the given categories. The given set may be empty in which case the returned
     * xml fragment is still a valid XMCDA fragment.
     * 
     * @param categories
     *            not <code>null</code>, the categories from the worst one to the best one.
     * @return not <code>null</code>, empty if the input set is empty (this is a valid XMCDA content).
     */
    public XCategoriesComparisons writeComparisons(NavigableSet<Category> categories) {
        Preconditions.checkNotNull(categories);
        final XCategoriesComparisons xComparisons = XMCDA.Factory.newInstance().addNewCategoriesComparisons();
        final Pairs xPairs = xComparisons.addNewPairs();
        final Iterator<Category> iterator = categories.iterator();
        Category init = iterator.hasNext() ? iterator.next() : null;
        while (iterator.hasNext()) {
            final Category term = iterator.next();
            final Pair xPair = xPairs.addNewPair();
            final XCategoryReference xInitial = xPair.addNewInitial();
            xInitial.setCategoryID(init.getName());
            xPair.addNewTerminal().setCategoryID(term.getName());
            init = term;
        }
        return xComparisons;
    }

    /**
     * Creates a new object which will use the default error management strategy {@link ErrorManagement#THROW}.
     */
    public XMCDACategories() {
        super();
    }

    /**
     * Creates a new object delegating error management to the given error manager in case of unexpected data read.
     * 
     * @param errorsManager
     *            not <code>null</code>.
     */
    public XMCDACategories(XMCDAErrorsManager errorsManager) {
        super(errorsManager);
    }
}
