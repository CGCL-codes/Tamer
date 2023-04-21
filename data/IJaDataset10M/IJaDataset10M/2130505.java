package org.jcvi.glk.helpers;

import org.apache.commons.cli.Options;
import org.jcvi.common.command.CommandLineOptionBuilder;
import org.jcvi.common.core.util.JoinedStringBuilder;
import org.jcvi.glk.Extent;
import org.jcvi.glk.ExtentAttributeType;
import org.jcvi.glk.ExtentType;
import org.jcvi.glk.elvira.ExtentTypeName;
import java.util.*;

/**
 * <p>Wraps the database interactions needed to access Funding information and
 * utility methods for handling Funding.</p>
 */
public class FundingHelper {

    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    public static enum FundingSource {

        GSC, MSC, UNSPECIFIED_FUNDING_SOURCE
    }

    private final Set<FundingSource> fundingSources;

    private final GLKHelper helper;

    public FundingHelper(Set<FundingSource> funding, GLKHelper glkHelper) {
        this.fundingSources = funding;
        this.helper = glkHelper;
    }

    /**
     * <p>Find the grants associated with this Sample/Lot/Collection</p>
     * <p>Ideally a node would be a composite of the funding types used by its
     * children and those associated with its ancestors. See the Restrictions
     * section for differences between this implementation and the ideal.<p>
     *
     * <h3>Nodes as Composites of their children's properties </h3>
     * <p>When traversing the tree of Extents if two extents share the same
     * parent extent, but not the same funding source then the parent extent
     * should be considered to have both funding sources.</p>
     *
     * <h3>Parent to Child inheritance</h3>
     * <p>If a funding type has been assigned to the node then that type is
     * the one used otherwise the ancestors of the node are searched in distance
     * order and the first funding type associated with one of these is used.</p>
     *
     * <h3>Restrictions</h3>
     * <p><B>To avoid having to check the whole tree each path is assumed to
     * contain only one funding attribute.</B> This allows the search to be
     * stopped as soon as a funding attribute is found. </p>
     * <p>The search order used is:
     * <ul>
     *     <li>The Extent passed</li>
     *     <li>The Extent's ancestors
     *              (working from the current node towards the root)</li>
     *     <li>The children of the Extent</li>
     * </ul></p>
     * <p><B>To limit the depth of tree searched only Extents of type
     * Collection or Lot are allowed to have funding attributes.</B></p>
     *
     * <h3>Implementation</h3>
     * <p>Currently funding type is encoded by the presence or absence of the
     * boolean attribute 'GSC' on the Extent. To simulate the above logic
     * the following rules are used:</p>
     * <p>For Collections or above</p>
     * <ul>
     *     <li>If set <B>GSC</B> is returned.</li>
     *     <li>If not set then getFundingSource is called with each of the
     *     Extents children and the results are merged. Note: The search ends
     *     early if all funding types have been found</li>
     *     <li>If the Extent has no children then <B>GSC</B> is returned.
     *     This is because it is assumed that only new Collections will have no
     *     Lots associated and all new Collections are funded by the GSC
     *     agreement.<li>
     * </ul>
     * <p>For Lots or lower
     * <ul>
     *     <li>If the attribute is set on the Extent or one of its ancestors
     *     then <B>GSC</B> is returned.</li>
     *     <li>If it is not set then <B>MSC</B> is returned</li>
     * </ul></p>
     * @param   extent    The sample/lot/collection to check
     * @return  The associated FundingSources in an EnumSet(compact and fast).
     *
     */
    public EnumSet<FundingSource> getFundingSources(Extent extent) {
        boolean isGSC = isGscSample(extent);
        if (isCollectionOrAbove(extent)) {
            if (isGSC || !extent.hasChildren()) {
                return EnumSet.of(FundingSource.GSC);
            } else {
                Collection<Extent> children = extent.getChildren();
                EnumSet<FundingSource> extentsFundingSources = EnumSet.noneOf(FundingSource.class);
                EnumSet<FundingSource> all = EnumSet.allOf(FundingSource.class);
                for (Extent child : children) {
                    extentsFundingSources.addAll(getFundingSources(child));
                    if (all.equals(extentsFundingSources)) {
                        return extentsFundingSources;
                    }
                }
                return extentsFundingSources;
            }
        } else {
            if (isGSC) {
                return EnumSet.of(FundingSource.GSC);
            } else {
                return EnumSet.of(FundingSource.MSC);
            }
        }
    }

    public boolean isGscSample(Extent extent) {
        ExtentAttributeType gscAttributeType = helper.getExtentAttributeType("GSC");
        return helper.inheritsAttribute(extent, gscAttributeType);
    }

    /**
     * This method compares the funding sources associated with this helper
     * with the funding sources that the passed in Extent has. If they overlap
     * then the filter returns true.
     * @param extent    The extent to test
     * @return  true if this extent should be processed further
     */
    public boolean passesFilterForFundingSource(Extent extent) {
        EnumSet<FundingSource> extentsFundingSources = getFundingSources(extent);
        extentsFundingSources.retainAll(fundingSources);
        return !extentsFundingSources.isEmpty();
    }

    public boolean isCollectionOrAbove(Extent extent) {
        Boolean cachedResult = collectionOrAboveCache.get(extent.getType());
        if (cachedResult == null) {
            cachedResult = isCollectionOrAboveDB(extent);
            collectionOrAboveCache.put(extent.getType(), cachedResult);
        }
        return cachedResult;
    }

    /**
     * Tests the Extent_Type of the Extent to determine where in the
     * Extent tree it is from
     * @param extent    The extent to test
     * @return  true if it is a collection or if none of it's ancestors was
     * a collection (therefore it is above collection)
     */
    private boolean isCollectionOrAboveDB(Extent extent) {
        ExtentType collectionType = helper.getExtentType(ExtentTypeName.COLLECTION);
        if (collectionType.equals(extent.getType())) {
            return true;
        }
        Extent previouslyTested = null;
        Extent nextToTest = extent;
        while (nextToTest != null && previouslyTested != nextToTest) {
            if (collectionType.equals(extent.getType())) {
                return true;
            }
            previouslyTested = nextToTest;
            nextToTest = nextToTest.getParent();
        }
        return false;
    }

    private static Map<ExtentType, Boolean> collectionOrAboveCache = new HashMap<ExtentType, Boolean>();

    public static void addFundingOptionToCommandLine(Options options) {
        options.addOption(new CommandLineOptionBuilder("f", "comma separated list of funding sources to output [GSC, MSC] (default GSC)").isRequired(false).isFlag(false).longName("funding").build());
    }

    public static Set<FundingHelper.FundingSource> parseFundingSource(String value) throws IllegalArgumentException {
        String[] FUNDINGFromParameter;
        if (value == null || value.trim().isEmpty()) {
            FUNDINGFromParameter = EMPTY_STRING_ARRAY;
        } else {
            FUNDINGFromParameter = value.split(",");
        }
        Set<FundingHelper.FundingSource> selectedFUNDING = EnumSet.noneOf(FundingHelper.FundingSource.class);
        Collection<String> invalidGrantNames = new HashSet<String>();
        for (String grantName : FUNDINGFromParameter) {
            if (grantName.trim().isEmpty()) {
                continue;
            }
            try {
                FundingHelper.FundingSource grant = FundingHelper.FundingSource.valueOf(grantName.toUpperCase());
                if (grant == null) {
                    invalidGrantNames.add(grantName);
                } else {
                    selectedFUNDING.add(grant);
                }
            } catch (IllegalArgumentException iae) {
                invalidGrantNames.add(grantName);
            }
        }
        if (!invalidGrantNames.isEmpty() || selectedFUNDING.isEmpty()) {
            Collection<FundingHelper.FundingSource> validSources = EnumSet.complementOf(EnumSet.of(FundingHelper.FundingSource.UNSPECIFIED_FUNDING_SOURCE));
            String validGrantNames = "Valid funding values are; " + new JoinedStringBuilder(validSources).glue(", ").build();
            if (!invalidGrantNames.isEmpty()) {
                throw new IllegalArgumentException("Error: Some of the funding values were not recognized (" + new JoinedStringBuilder(invalidGrantNames).glue(", ").build() + "). " + validGrantNames);
            }
            if (selectedFUNDING.isEmpty()) {
                throw new IllegalArgumentException("At least one valid funding must be specified if the " + "-g option is used.\n" + validGrantNames);
            }
        }
        return selectedFUNDING;
    }
}
