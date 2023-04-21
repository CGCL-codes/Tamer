package org.gbif.checklistbank.nub;

import org.gbif.checklistbank.Constants;
import org.gbif.checklistbank.imports.AcceptImportFactory;
import org.gbif.checklistbank.imports.ChecklistImport;
import org.gbif.checklistbank.imports.ChecklistImportFactory;
import org.gbif.checklistbank.imports.ClassificationGenerator;
import org.gbif.checklistbank.imports.ClassificationGeneratorFactory;
import org.gbif.checklistbank.lookup.ClassifiedName;
import org.gbif.checklistbank.lookup.HigherTaxaLookup;
import org.gbif.checklistbank.model.Checklist;
import org.gbif.checklistbank.model.NameUsage;
import org.gbif.checklistbank.model.lite.NameStringLite;
import org.gbif.checklistbank.model.lite.NameUsageLite;
import org.gbif.checklistbank.model.serializer.tab.NameUsageLiteSourceIdTabMapper;
import org.gbif.checklistbank.model.serializer.tab.NameUsageLiteTabMapper;
import org.gbif.checklistbank.model.serializer.tab.TabRowMapper;
import org.gbif.checklistbank.model.voc.Origin;
import org.gbif.checklistbank.service.ChecklistService;
import org.gbif.checklistbank.service.CitationService;
import org.gbif.checklistbank.service.NameStringService;
import org.gbif.checklistbank.service.NameUsageService;
import org.gbif.checklistbank.service.TermService;
import org.gbif.checklistbank.service.impl.IdentifierServicePgSql;
import org.gbif.checklistbank.service.impl.PgSqlBaseService;
import org.gbif.checklistbank.utils.PgSqlUtils;
import org.gbif.checklistbank.utils.RankUtil;
import org.gbif.ecat.cfg.DataDirConfig;
import org.gbif.ecat.utils.PrimitiveUtils;
import org.gbif.ecat.voc.Kingdom;
import org.gbif.ecat.voc.NameType;
import org.gbif.ecat.voc.Rank;
import org.gbif.ecat.voc.TaxonomicStatus;
import org.gbif.utils.file.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import javax.annotation.Nullable;
import com.google.common.base.Preconditions;
import com.google.inject.Inject;
import gnu.trove.TIntHashSet;
import gnu.trove.TIntIterator;
import static org.gbif.checklistbank.Constants.NUB_CHECKLIST_ID;

/**
 * TODO: treat pro parte synonyms correctly
 *
 */
public class NubGeneratorImpl extends NubGeneratorBase {

    private int currKingdomId;

    private int currParentId;

    private final String[] debugNames = { "amphibia rodo" };

    @Inject
    public NubGeneratorImpl(RankUtil rankUtil, HigherTaxaLookup higherTaxaLookup, NameUsageService<NameUsage> usageService, TermService termService, ChecklistService checklistService, NameStringService nameService, CitationService citationService, ClassificationGeneratorFactory classificationGenFactory, PgSqlBaseService baseService, ChecklistImportFactory importFactory, AcceptImportFactory acceptFactory, IdentifierServicePgSql identifierService, DataDirConfig cfg) {
        super(rankUtil, higherTaxaLookup, usageService, termService, checklistService, nameService, citationService, classificationGenFactory, baseService, importFactory, acceptFactory, identifierService, cfg);
    }

    /**
   * add new usages from all other checklists to nub or update a nub usages
   * properties. Apart from species and infraspecific usages this method only
   * adds genera and families as new higher taxa to group them.
   */
    private void addChecklistUsages(Checklist c, boolean createHigherTaxa) {
        log.debug("ADDING *** " + c.getTitle() + " ***");
        log.debug("Loading usages for checklist " + c.getTitle());
        currChecklist = c;
        int startCount = nubUsagesById.size();
        initChecklist(c);
        log.debug("Traverse taxonomy, start with " + rootMap.size() + " roots");
        TIntIterator iter = rootMap.iterator();
        while (iter.hasNext()) {
            int rootId = iter.next();
            currKingdomId = incertaeSedisKingdom.id;
            currParentId = incertaeSedisKingdom.id;
            processUsageRecursively(rootId, NubExistence.EXCLUDED, createHigherTaxa);
        }
        log.debug("Added " + (nubUsagesById.size() - startCount) + " usages to nub with total of " + nubUsagesById.size() + " usages");
    }

    private void addCol() {
        Checklist col = checklistService.get(Constants.COL_CHECKLIST_ID);
        addChecklistUsages(col, true);
    }

    private void addIncertaeSedisKingdom() {
        NameUsageLite usage = new NameUsageLite();
        usage.id = 0;
        usage.nameFk = Constants.INCERTAE_SEDIS_NAME_ID;
        usage.isSynonym = false;
        usage.rankFk = Rank.KINGDOM.termID();
        usage.taxStatusFk = TERM_ACCEPTED_ID;
        usage.accordingToFk = CITATION_GBIF_NUB;
        addToNub(usage);
        incertaeSedisKingdom = usage;
    }

    private void addIrmngHomonyms() {
        Checklist irmng = checklistService.get(Constants.HOMONYM_CHECKLIST_ID);
        addChecklistUsages(irmng, false);
    }

    /**
   * add new usages from all other checklists to nub or update a nub usages
   * properties. Apart from species and infraspecific usages this method only
   * adds genera and families as new higher taxa to group them.
   * The method iterates over all checklists, sorted by their priority, and
   * loads all usages per checklist into memory.
   */
    private void addOtherSpecies() {
        for (Checklist c : checklists) {
            addChecklistUsages(c, false);
        }
    }

    @Override
    public void buildNub() throws NubGenerationError {
        log.debug("Start building a new nub ...");
        init();
        addIncertaeSedisKingdom();
        addCol();
        addIrmngHomonyms();
        addOtherSpecies();
        postProcess();
        log.info("Built nub with " + nubKeys.toString());
        persist();
        Checklist nub = checklistService.get(Constants.NUB_CHECKLIST_ID);
        checklistService.updateCounts(nub);
        checklistService.update(nub);
        log.info("New nub build and copied to public schema");
        report(nub);
    }

    /**
   * Creates a new nub usage based on a given source usage.
   *
   * @param origUsage the source usage to copy properties from
   * @param origName the source name of the origUsage
   * @param rank rank to be applied
   * @param parentNubId
   * @param homonymLookup
   * @return the newly build nub usage, never null
   * @throws IgnoreUsageException if we cannot build the usage
   */
    private NameUsageLite buildNubUsage(NameUsageLite origUsage, NameStringLite origName, Rank rank, int parentNubId, HomonymLookup homonymLookup) throws IgnoreUsageException {
        Preconditions.checkNotNull(rank, "nub rank must exist for usage " + origUsage.id);
        debugTracedName("Building new " + rank + " nub usage with homonymType=" + homonymLookup.type + ", homonymUsageId=" + homonymLookup.refUsageId + ", parentNubId=" + parentNubId);
        NameUsageLite parent = nubUsagesById.get(parentNubId);
        Preconditions.checkNotNull(parent, "nub parent must exist in cache for usage " + origUsage.id);
        Rank parentRank = termService.preferredRank(parent.rankFk);
        Preconditions.checkNotNull(parentRank, "nub parent rank must exist for parent " + parent.id);
        boolean isHigherTaxon = rank.isSuprageneric() && Rank.FAMILY != rank;
        if (!isHigherTaxon && !origUsage.isSynonym && rank.termID() <= parentRank.termID()) {
            log.warn("Ignoring child usage {} [{}] with higher or equal rank (" + rank + "/" + parentRank + ") " + "than its parent: " + parent.id, origName.scientificName, origUsage.id);
            throw new IgnoreUsageException(origUsage.id, NubExistence.PARENT_RANK_NOT_HIGHER);
        }
        NameUsageLite nubUsage = cloneUsage(origUsage);
        nubUsage.rankFk = rank.termID();
        nubUsage.id = idGen.assignId(origName.getCanonOrNameId(), homonymLookup, rank);
        if (parentNubId == 0 && Rank.KINGDOM == rank) {
            nubUsage.parentFk = -1;
        } else {
            nubUsage.parentFk = parentNubId;
        }
        nubUsage.basionymFk = origUsage.basionymFk;
        debugTracedName("Build new nub usage " + nubUsage.id + " synonym=" + nubUsage.isSynonym);
        if (nubUsage.isSynonym) {
        } else {
            if (origName.genus != null && currKingdomId != Kingdom.Viruses.usageID()) {
                addMissingImplicitUsages(origName, rank, nubUsage, parent, parentRank, parentNubId);
            }
        }
        try {
            addToNub(nubUsage, homonymLookup);
        } catch (NubKeyExistsError e) {
            throw new IgnoreUsageException(origUsage.id, NubExistence.DUPLICATE_NUB_KEY);
        }
        return nubUsage;
    }

    /**
   * If missing and names are parsed well adds implicit genus and species usages.
   * For example for a name "Abies alba var. alpina" linked to the genus Abies
   * it will also create the species usage Abies alba
   * @param origName
   * @param rank
   * @param nubUsage
   * @param parent
   * @param parentRank
   * @param parentNubId
   */
    private void addMissingImplicitUsages(NameStringLite origName, Rank rank, NameUsageLite nubUsage, NameUsageLite parent, Rank parentRank, int parentNubId) {
        if (rank == Rank.SPECIES) {
            if (Rank.GENUS != termService.preferredRank(parent.rankFk)) {
                try {
                    NameUsageLite genus = buildNubUsage(origName.genus, null, Rank.GENUS, parentNubId);
                    nubUsage.parentFk = genus.id;
                } catch (IgnoreUsageException e) {
                    nubUsage.parentFk = parentNubId;
                    log.warn("Ignoring implicit genus {} with parent nub id {}: {}", new Object[] { origName.genus, parentNubId, e.getMessage() });
                }
            } else {
                NameStringLite parentName = nsCache.get(parent.nameFk);
                if (!parentName.scientificName.startsWith(origName.genus)) {
                    nubUsage.taxStatusFk = TaxonomicStatus.Doubtful.termID();
                    log.warn("Parent nub genus {} not matching species {}", parentName.genus, origName.genus);
                }
            }
        } else if (rank.isSpeciesOrBelow()) {
            if (parentRank.isSpeciesOrBelow()) {
                NameStringLite parentName = nsCache.get(parent.nameFk);
                if (!parentName.scientificName.startsWith(origName.genus + " " + origName.specificEpithet)) {
                    nubUsage.taxStatusFk = TaxonomicStatus.Doubtful.termID();
                    log.warn("Parent nub species {} not matching infraspecies {} on genus or species epithet", parentName.scientificName, origName.scientificName);
                }
            } else {
                if (Rank.GENUS != parentRank) {
                    try {
                        NameUsageLite genus = buildNubUsage(origName.genus, null, Rank.GENUS, parentNubId);
                        if (genus != null) {
                            parentNubId = genus.id;
                        }
                    } catch (IgnoreUsageException e) {
                        log.warn("Cannot create implicit genus {} with parent nub id {}", new Object[] { origName.genus, parentNubId }, e);
                    }
                } else {
                    NameStringLite parentName = nsCache.get(parent.nameFk);
                    if (!parentName.scientificName.startsWith(origName.genus)) {
                        nubUsage.taxStatusFk = TaxonomicStatus.Doubtful.termID();
                        log.warn("Parent nub genus {} not matching species {}", parentName.scientificName, origName.scientificName);
                    }
                }
                try {
                    NameUsageLite species = buildNubUsage(origName.genus, origName.specificEpithet, Rank.SPECIES, parentNubId);
                    nubUsage.parentFk = species.id;
                } catch (IgnoreUsageException e) {
                    log.warn("Cannot create implicit species {} {} with parent nub id {}", new Object[] { origName.genus, origName.specificEpithet, parentNubId }, e);
                    nubUsage.parentFk = parentNubId;
                }
            }
        }
    }

    /**
     * creates and populates a classified name for a given nub usage.
     * The higher ranks are populated with canonical names taken from the normalised parentID hierarchy, not the
     * denormalised foreign keys as they dont exist yet
     * for newly created nub usages.
     */
    private ClassifiedName nubUsageAsClassifiedName(int nubUsageID) {
        ClassifiedName cn = new ClassifiedName();
        TIntHashSet visitedIds = new TIntHashSet();
        while (nubUsageID > 0) {
            NameUsageLite nub = nubUsagesById.get(nubUsageID);
            if (nub == null || visitedIds.contains(nubUsageID)) {
                break;
            }
            visitedIds.add(nubUsageID);
            String name = nsCache.getCanonicalName(nub.nameFk);
            Rank r = termService.preferredRank(nub.rankFk);
            cn.setHigherRank(r, name, nubUsageID);
            nubUsageID = nub.parentFk;
        }
        return cn;
    }

    /**
   *
   * @param genus
   * @param specificEpithet
   * @param rank
   * @param parentNubId
   * @return
   * @throws IgnoreUsageException if no usage can be build
   */
    private NameUsageLite buildNubUsage(String genus, String specificEpithet, Rank rank, int parentNubId) throws IgnoreUsageException {
        NameStringLite name = newCanonicalNameString(genus, specificEpithet);
        ClassifiedName classifiedNub = nubUsageAsClassifiedName(parentNubId);
        classifiedNub.setScientificName(name.scientificName);
        classifiedNub.setRank(rank);
        HomonymLookup homonymLookup = homonymInformer.findReferenceUsage(classifiedNub);
        NameUsageLite existingUsage = findExistingNubUsage(-1, homonymLookup, name.getCanonOrNameId(), rank);
        if (existingUsage == null) {
            NameUsageLite nubUsage = new NameUsageLite();
            nubUsage.nameFk = name.id;
            nubUsage.id = idGen.newId();
            nubUsage.parentFk = parentNubId;
            nubUsage.rankFk = rank.termID();
            nubUsage.accordingToFk = CITATION_NUB_GENERATOR_CANONICAL_ID;
            nubUsage.taxStatusFk = TERM_ACCEPTED_ID;
            nubUsage.isSynonym = false;
            nubUsage.origin = Origin.implicitName.ordinal();
            try {
                addToNub(nubUsage, homonymLookup);
            } catch (NubKeyExistsError e) {
                throw new IgnoreUsageException(nubUsage.id, NubExistence.DUPLICATE_NUB_KEY);
            }
            debugTracedName("Build implicit nub usage " + name.scientificName + " with usageId=" + nubUsage.id + ", parentNubId=" + parentNubId + ", homonymType=" + homonymLookup.type + ", homRefId=" + homonymLookup.refUsageId);
            return nubUsage;
        } else {
            debugTracedName("Found existing implicit nub usage " + name.scientificName + " with usageId=" + existingUsage.id + ", parentNubId=" + parentNubId + ", homRefId=" + homonymLookup.refUsageId);
            return existingUsage;
        }
    }

    private NameUsageLite cloneUsage(NameUsageLite original) {
        NameUsageLite clone = new NameUsageLite();
        clone.sourceId = original.id;
        clone.nameFk = original.nameFk;
        clone.accordingToFk = checklistId2accordingToId.get(original.checklistFk);
        clone.namePublishedInFk = original.namePublishedInFk;
        clone.rankFk = getPreferredTerm(original.rankFk);
        clone.taxStatusFk = getPreferredTerm(original.taxStatusFk);
        clone.nomStatusFk = getPreferredTerm(original.nomStatusFk);
        clone.nomCodeFk = getPreferredTerm(original.nomCodeFk);
        clone.isSynonym = original.isSynonym;
        clone.origin = Origin.source.ordinal();
        return clone;
    }

    /**
   *
   * @param usageId
   * @param homonymLookup the reference usage id for homonyms or negative if no or undecidable homonym, see HomonymInformer constants
   * @param canonNameID
   * @return
   * @throws IgnoreUsageException
   */
    @Nullable
    private NameUsageLite findExistingNubUsage(int usageId, HomonymLookup homonymLookup, int canonNameID, Rank rank) throws IgnoreUsageException {
        int nubUsageId = -1;
        if (usageId > 0 && usageId2nubId.contains(usageId)) {
            log.info("nubUsagesById found usage {}", usageId);
            nubUsageId = usageId2nubId.get(usageId);
        } else {
            nubUsageId = nubKeys.get(canonNameID, homonymLookup, rank, true);
            if (nubUsageId < 0 && (HomonymType.UNDECIDABLE_GENUS_HOMONYM == homonymLookup.type || HomonymType.UNDECIDABLE_HOMONYM == homonymLookup.type)) {
                throw new IgnoreUsageException(usageId, NubExistence.UNRESOLVABLE_HOMONYM);
            }
        }
        return nubUsagesById.get(nubUsageId);
    }

    /**
   * For a new canonical name we create a new name_string entry in clb and a new NameStringLite instance in the nsCache
   *
   * @return the new name
   */
    private NameStringLite newCanonicalNameString(String genus, String specificEpithet) {
        NameStringLite nl = new NameStringLite();
        nl.genus = genus;
        nl.specificEpithet = specificEpithet;
        nl.scientificName = NameStringLite.buildCanonicalName(nl);
        nl.id = PrimitiveUtils.toInt(nameService.nameStringToId(nl.scientificName, true));
        if (!nsCache.contains(nl.id)) {
            nl.canonicalNameFk = nl.id;
            nl.canonicalFullNameFk = nl.id;
            nl.type = NameType.sciname;
            nsCache.add(nl);
        } else {
            nl = nsCache.get(nl.id);
        }
        return nl;
    }

    /**
   * @param parentId parent id to start the search for
   * @param usageId  usage id to look for in parent hierarchy
   *
   * @return true if the parentId or any of its parent equals the given usageId
   */
    private boolean parentHierarchyContains(int parentId, int usageId) {
        TIntHashSet visitedIds = new TIntHashSet();
        while (parentId > 0) {
            if (parentId == usageId) {
                return true;
            }
            if (visitedIds.contains(parentId)) {
                return false;
            }
            visitedIds.add(parentId);
            parentId = nubUsagesById.get(parentId).parentFk;
        }
        return false;
    }

    /**
   * Debugging method - please leave it although not used in production code.
   * @param usageId
   * @return
   */
    private String getClassificationString(int usageId) {
        StringBuilder sb = new StringBuilder();
        while (usageId > 0) {
            if (sb.length() > 0) {
                sb.append("|");
            }
            sb.append(nsCache.get(nuCache.get(usageId).nameFk).scientificName);
            sb.append("[" + usageId + "]");
            usageId = nubUsagesById.get(usageId).parentFk;
        }
        return sb.toString();
    }

    /**
   *
   */
    private void persist() {
        log.debug("Persisting " + nubUsagesById.size() + " nub usages to checklist bank ...");
        String importTable = NUB_SCHEMA + ".name_usage";
        String usageUpdateTable = "tmp_usage_nub_update";
        ChecklistImport imp = importFactory.create(nubChecklist);
        imp.createImportSchema();
        log.debug("import schema " + NUB_SCHEMA + " initialised.");
        setLogBreakpoint();
        TabRowMapper<NameUsageLite> liteMapper = new NameUsageLiteTabMapper();
        baseService.bulkSave(importTable + " (" + liteMapper.columns() + ")", liteMapper, nubUsagesById);
        logBreakpoint("Persisting nub usages to import schema");
        liteMapper = new NameUsageLiteSourceIdTabMapper();
        baseService.bulkSave(NUB_SCHEMA + ".identifier (" + liteMapper.columns() + ")", liteMapper, nubUsagesById);
        logBreakpoint("Persisting nub source ids to import schema");
        try {
            ClassificationGenerator classificationGenerator = classificationGenFactory.create(NUB_CHECKLIST_ID, incertaeSedisKingdom.id);
            classificationGenerator.updateImportClassification(true);
            logBreakpoint("Update import classification");
        } catch (Exception e) {
            throw new NubGenerationError("Classification generator could not be created by factory", e);
        }
        log.debug("Updating name_usage.nub_fk=null ...");
        baseService.executeUpdate("Update name_usage set nub_fk=null where nub_fk is not null");
        logBreakpoint("Updating name_usage.nub_fk=null");
        log.debug("Updating list_name.nub_fk=null ...");
        baseService.executeUpdate("Update list_name set nub_fk=null where nub_fk is not null");
        logBreakpoint("Updating list_name.nub_fk=null");
        acceptFactory.create(nubChecklist).acceptImport();
        logBreakpoint("Copying import schema to public schema and removing artifacts");
        try {
            baseService.executeUpdate(PgSqlUtils.dropTableSql(usageUpdateTable));
            String ddl = "CREATE TABLE " + usageUpdateTable + " (usage_fk integer PRIMARY KEY, nub_fk integer) without oids";
            baseService.executeUpdate(ddl);
            baseService.bulkSave(usageUpdateTable + "(usage_fk,nub_fk)", usageId2nubId);
            logBreakpoint("Inserting usage update table");
            log.debug("Analyzing usage update table ...");
            baseService.executeUpdate(PgSqlUtils.analyzeSql(usageUpdateTable));
            logBreakpoint("Analyzing usage update table");
            log.debug("Updating name_usage.nub_fk ...");
            String udpateUsagesSql = "update name_usage u set nub_fk=l.nub_fk from " + usageUpdateTable + " l where u.id=l.usage_fk and l.usage_fk is not null";
            int updated = baseService.executeUpdate(udpateUsagesSql);
            logBreakpoint("Updating " + updated + " name_usage.nub_fk records");
        } catch (Exception e) {
            log.error("SQL exception when updating usages with nub ids", e);
        } finally {
            baseService.executeUpdate(PgSqlUtils.dropTableSql(usageUpdateTable));
        }
    }

    /**
   * Replaces basionymFk pointers from their original source with respective nub usages
   */
    private void postProcess() {
        for (NameUsageLite u : nubUsagesById.getValues(new NameUsageLite[1])) {
            if (u.basionymFk > 0) {
                if (usageId2nubId.contains(u.basionymFk)) {
                    u.basionymFk = usageId2nubId.get(u.basionymFk);
                } else {
                    log.warn("Basionym source usage " + u.basionymFk + " is not in the nub");
                    u.basionymFk = -1;
                }
            }
            if (u.taxStatusFk < 1) {
                if (u.isSynonym) {
                    u.taxStatusFk = TaxonomicStatus.Synonym.termID();
                } else {
                    u.taxStatusFk = TaxonomicStatus.Accepted.termID();
                }
            }
        }
    }

    private Rank getPreferredRank(int usageId, int rankId, NameStringLite name) {
        Rank rank = termService.preferredRank(rankId);
        Rank rankInferred = Rank.inferRank(name.genus, name.infraGeneric, name.specificEpithet, name.rankMarker, name.infraSpecificEpithet);
        if (rankInferred.isUncomparable()) {
            if (rank == null) {
                rank = rankInferred;
            }
        } else {
            if (rank == null || rank.isUncomparable()) {
                rank = rankInferred;
            } else {
                if (!rank.equals(rankInferred)) {
                    log.warn("Inferred rank {} is different from given rank {} for usage " + usageId, rankInferred, rank);
                }
            }
        }
        return rank;
    }

    /**
   * @param parentInNub if true the direct parent in the source checklist is included/mapped to the nub
   * @throws IgnoreUsageException if the usage is ignored and not part of the nub
   * @return the new or existing nub usage, never NULL
   */
    private NameUsageLite processUsage(int usageId, NubExistence parentInNub, boolean createHigherTaxa) throws IgnoreUsageException {
        NameUsageLite orig = nuCache.get(usageId);
        NameStringLite name = nsCache.get(orig.nameFk);
        Preconditions.checkNotNull(orig, "UsageID " + usageId + " not contained in usage cache");
        Preconditions.checkNotNull(name, "UsageID " + usageId + " with nameID " + orig.nameFk + " not contained in name cache");
        for (String prefix : debugNames) {
            String n = name.scientificName.toLowerCase().trim();
            if (n.startsWith(prefix)) {
                newTracedName(orig, name, currChecklist.getId(), currParentId);
            }
        }
        if (NameType.blacklisted == name.type || NameType.informal == name.type) {
            log.debug("Ignore blacklisted or informal usage {} {}", usageId, name.scientificName);
            throw new IgnoreUsageException(usageId, NubExistence.BAD_NAME);
        }
        if (Constants.INCERTAE_SEDIS_NAME_ID == name.getCanonOrNameId()) {
            log.debug("Ignore incertae sedis usage {} {}", usageId, name.scientificName);
            throw new IgnoreUsageException(usageId, NubExistence.INCERTAE_SEDIS);
        }
        Rank rank = getPreferredRank(usageId, orig.rankFk, name);
        HomonymLookup hom = homonymInformer.findReferenceUsage(orig);
        debugTracedName(hom.toString());
        NameUsageLite nubUsage = findExistingNubUsage(usageId, hom, name.getCanonOrNameId(), rank);
        if (nubUsage != null) {
            usageId2nubId.put(orig.id, nubUsage.id);
        }
        if (createHigherTaxa || isAcceptedLowerRank(rank)) {
            if (nubUsage == null) {
                if (!orig.isSynonym || NubExistence.INCLUDED == parentInNub) {
                    nubUsage = buildNubUsage(orig, name, rank, currParentId, hom);
                } else if (NubExistence.INCERTAE_SEDIS == parentInNub || NubExistence.UNRANKED == parentInNub) {
                    nubUsage = buildNubUsage(orig, name, rank, currParentId, hom);
                    debugTracedName("Doubtful incertae sedis or unranked parent " + currParentId);
                    nubUsage.taxStatusFk = TaxonomicStatus.Doubtful.termID();
                    nubUsage.isSynonym = false;
                } else {
                    log.debug("Ignoring synonym {} [{}] with parent excluded from nub due to " + parentInNub, name.scientificName, usageId);
                    throw new IgnoreUsageException(usageId, NubExistence.PARENT_EXCLUDED);
                }
                if (nubUsage == null) {
                    throw new IllegalStateException("Processing usage " + usageId + " failed and did not return any nub usage");
                }
            } else {
                if (currParentId == nubUsage.id) {
                    log.warn("Ignore usage {} which matches both the existing nub usage {} and its parent", usageId, currParentId);
                } else {
                    updateUsage(nubUsage, orig, rank);
                }
            }
        } else if (Rank.Unranked == rank) {
            log.debug("Ignoring unranked usage " + usageId + " : " + name.scientificName);
            throw new IgnoreUsageException(usageId, NubExistence.UNRANKED);
        } else if (nubUsage != null && nubUsage.rankFk == rank.termID()) {
            updateHigherUsage(nubUsage, orig, rank);
        } else {
            log.debug("Ignoring usage " + usageId + " with undesired rank " + rank + ": " + name.scientificName);
            throw new IgnoreUsageException(usageId, NubExistence.UNDESIRED_RANK);
        }
        return nubUsage;
    }

    /**
   * Adds not yet existing family, genus, species or infraspecific usages.
   * Watching out for homonyms.
   *
   * @param parentInNub true if the checklist usage parent exists in the nub
   */
    private void processUsageRecursively(int usageId, NubExistence parentInNub, boolean createHigherTaxa) {
        trace = null;
        int lastParentId = currParentId;
        int lastKingdomId = currKingdomId;
        NubExistence currInNub = NubExistence.INCLUDED;
        try {
            NameUsageLite nubUsage = processUsage(usageId, parentInNub, createHigherTaxa);
            if (nubUsage.isSynonym) {
                currParentId = nubUsage.parentFk;
            } else {
                currParentId = nubUsage.id;
            }
            if (Rank.KINGDOM.termID() == nubUsage.rankFk) {
                currKingdomId = nubUsage.id;
                NameStringLite n = nsCache.get(nubUsage.nameFk);
                nubUsage.nameFk = n.canonicalNameFk;
            }
        } catch (IgnoreUsageException e) {
            debugTracedName(e.getMessage());
            currInNub = e.getReason();
        }
        if (childrenMap.containsKey(usageId)) {
            TIntIterator iter = childrenMap.get(usageId).iterator();
            while (iter.hasNext()) {
                int childId = iter.next();
                processUsageRecursively(childId, currInNub, createHigherTaxa);
            }
        }
        currParentId = lastParentId;
        currKingdomId = lastKingdomId;
        if (NUB_ID_PLANT == currKingdomId) {
        }
    }

    /**
   * @param nub
   * @param orig
   * @param rank orig usage preferred rank
   */
    private void updateHigherUsage(NameUsageLite nub, NameUsageLite orig, Rank rank) {
        debugTracedName("Update existing higher nub usage " + nub.id);
        if (orig.taxStatusFk == TaxonomicStatus.Doubtful.termID()) {
            return;
        }
        if (nub.isSynonym == orig.isSynonym && nub.rankFk == rank.termID()) {
            if (Rank.KINGDOM != rank) {
                nub.nameFk = getNameFk(nub.nameFk, orig.nameFk);
            }
            if (nub.basionymFk < 0) {
                nub.basionymFk = orig.basionymFk;
            }
            if (nub.namePublishedInFk < 0) {
                nub.namePublishedInFk = orig.namePublishedInFk;
            }
        }
    }

    /**
   * @param nub
   * @param orig
   * @param rank
   */
    private void updateUsage(NameUsageLite nub, NameUsageLite orig, Rank rank) {
        debugTracedName("Update existing nub usage " + nub.id);
        if (orig.taxStatusFk == TaxonomicStatus.Doubtful.termID()) {
            return;
        }
        if (nub.rankFk != rank.termID()) {
            return;
        }
        boolean forceUpdate = nub.taxStatusFk == TaxonomicStatus.Doubtful.termID() || (nub.sourceChecklistId == orig.checklistFk && nub.isSynonym && !orig.isSynonym);
        if (forceUpdate || nub.sourceId < 0) {
            nub.sourceId = orig.id;
            nub.accordingToFk = checklistId2accordingToId.get(orig.checklistFk);
            nub.origin = Origin.source.ordinal();
        }
        if (forceUpdate || nub.isSynonym == orig.isSynonym) {
            if (forceUpdate) {
                nub.parentFk = currParentId;
                nub.isSynonym = orig.isSynonym;
            } else if (!nub.isSynonym) {
                NameUsageLite nubParent = nubUsagesById.get(nub.parentFk);
                NameUsageLite currParent = nubUsagesById.get(currParentId);
                if (nubParent != null && currParent.rankFk > nubParent.rankFk) {
                    if (incertaeSedisKingdom.id == nubParent.id || (isAcceptedLowerRank(rank) && parentHierarchyContains(currParentId, nub.parentFk))) {
                        nub.parentFk = currParentId;
                    }
                }
            }
            if (forceUpdate) {
                nub.nameFk = orig.nameFk;
            } else {
                nub.nameFk = getNameFk(nub.nameFk, orig.nameFk);
            }
            if (forceUpdate || nub.basionymFk < 0) {
                nub.basionymFk = orig.basionymFk;
            }
            if (forceUpdate || nub.taxStatusFk < 0) {
                nub.taxStatusFk = getPreferredTerm(orig.taxStatusFk);
            }
            if (forceUpdate || nub.nomStatusFk < 0) {
                nub.nomStatusFk = getPreferredTerm(orig.nomStatusFk);
            }
            if (forceUpdate || nub.namePublishedInFk < 0) {
                nub.namePublishedInFk = orig.namePublishedInFk;
            }
            if (forceUpdate || nub.nomCodeFk < 0) {
                nub.nomCodeFk = getPreferredTerm(orig.nomCodeFk);
            }
        }
    }

    private boolean isAcceptedLowerRank(Rank rank) {
        return Rank.FAMILY == rank || Rank.GENUS == rank || rank.isSpeciesOrBelow();
    }

    private int getPreferredTerm(int origTermId) {
        int tid = termService.preferredTermId(origTermId);
        return tid < Constants.TERM_SEQ_ID_START ? tid : -1;
    }

    private int getNameFk(int currNubNameFk, int origNameFk) {
        if (currNubNameFk == origNameFk) {
            return currNubNameFk;
        }
        NameStringLite nubName = nsCache.get(currNubNameFk);
        Preconditions.checkNotNull(nubName, "Current nub name not in cache. NameID=" + currNubNameFk);
        NameStringLite origName = nsCache.get(origNameFk);
        Preconditions.checkNotNull(origName, "Source name not in cache. NameID=" + origNameFk);
        if (currNubNameFk < 0) {
            currNubNameFk = origName.getCanonFullOrNameId();
        } else {
            if (nubName.isCanonical() && origName.hasFullCanonicalWithAuthors()) {
                currNubNameFk = origName.canonicalFullNameFk;
            }
        }
        return currNubNameFk;
    }

    private enum NubExistence {

        INCLUDED, EXCLUDED, UNDESIRED_RANK, UNRANKED, INCERTAE_SEDIS, BAD_NAME, UNRESOLVABLE_HOMONYM, PARENT_RANK_NOT_HIGHER, PARENT_EXCLUDED, DUPLICATE_NUB_KEY
    }

    ;

    private class IgnoreUsageException extends Exception {

        private final int usageId;

        private final NubExistence reason;

        public IgnoreUsageException(int usageId, NubExistence reason) {
            this.usageId = usageId;
            this.reason = reason;
        }

        public NubExistence getReason() {
            return reason;
        }

        public int getUsageId() {
            return usageId;
        }

        @Override
        public String getMessage() {
            return "Ignore usage " + usageId + " reason " + reason;
        }
    }

    private void report(Checklist nub) {
        Writer report = null;
        try {
            File repFile = cfg.loggingFile("nub-report.log");
            log.info("Writing nub report to {}", repFile.getAbsolutePath());
            report = FileUtils.startNewUtf8File(repFile);
            report.write("NEW NUB USAGES\n");
            for (int id = idGen.firstNewIdAssigned(); id != -1 && id <= idGen.lastNewIdAssigned(); id++) {
                NameUsageLite u = nubUsagesById.get(id);
                NameStringLite n = nsCache.get(u.nameFk);
                report.write(id + ": " + n.scientificName + " [" + Rank.valueOfTermID(u.rankFk) + "]\n");
            }
            report.write("\n\nREMOVED NUB USAGES\n");
            for (int id : idGen.unassignedIds()) {
                if (id == 0) {
                    continue;
                }
                NameUsageLite u = nuCache.get(id);
                NameStringLite n = nsCache.get(u.nameFk);
                report.write(id + ": " + n.scientificName + " [" + Rank.valueOfTermID(u.rankFk) + "]\n");
            }
        } catch (Exception e) {
            log.error("Error writing nub report", e);
        } finally {
            try {
                report.close();
            } catch (IOException e) {
            }
        }
    }
}
